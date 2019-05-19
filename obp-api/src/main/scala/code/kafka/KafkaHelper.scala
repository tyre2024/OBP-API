package code.kafka

import akka.pattern.{AskTimeoutException, ask}
import code.actorsystem.{ObpActorInit, ObpLookupSystem}
import code.api.util.CustomJsonFormats
import code.api.util.ErrorMessages._
import code.util.Helper.MdcLoggable
import com.openbankproject.commons.model.TopicTrait
import net.liftweb
import net.liftweb.common._
import net.liftweb.json.{JValue, MappingException}

import scala.concurrent.Future
import net.liftweb.json.JsonParser.ParseException
import org.apache.kafka.common.KafkaException
import org.apache.kafka.common.errors._

object KafkaHelper extends KafkaHelper

trait KafkaHelper extends ObpActorInit with MdcLoggable {

  override val actorName = "KafkaStreamsHelperActor" //CreateActorNameFromClassName(this.getClass.getName)
  override val actor = ObpLookupSystem.getKafkaActor(actorName)
  
  
  /**
    * Have this function just to keep compatibility for KafkaMappedConnector_vMar2017 and  KafkaMappedConnector.scala
    * In KafkaMappedConnector.scala, we use Map[String, String]. Now we change to case class
    * eg: case class Company(name: String, address: String) -->
    * Company("TESOBE","Berlin")
    * Map(name->"TESOBE", address->"2")
    *
    * @param caseClassObject
    * @return Map[String, String]
    */
  def transferCaseClassToMap(caseClassObject: scala.Product) =
    caseClassObject.getClass.getDeclaredFields.map(_.getName) // all field names
    .zip(caseClassObject.productIterator.to).toMap.asInstanceOf[Map[String, String]] // zipped with all values

  def process(request: scala.Product): JValue = {
    val mapRequest:Map[String, String] = transferCaseClassToMap(request)
    process(mapRequest)
  }

  /**
    * This function is used for Old Style Endpoints.
    * It processes Kafka's Outbound message to JValue.
    * @param request The request we send to Kafka
    * @return Kafka's Inbound message as JValue
    */
  def process (request: Map[String, String]): JValue ={
    extractFuture(actor ? request)
  }

  /**
    * This function is used for Old Style Endpoints at Kafka connector.
    * It processes Kafka's Outbound message to JValue wrapped into Box.
    * @param request The request we send to Kafka
    * @tparam T the type of the Outbound message
    * @return Kafka's Inbound message as JValue wrapped into Box
    */
  def processToBox[T](request: T): Box[JValue] = {
    extractFutureToBox(actor ? request)
  }

  /**
    * This function is used for Old Style Endpoints at Kafka connector.
    * It processes Kafka's Outbound message to JValue wrapped into Box.
    * @param request The request we send to Kafka
    * @tparam T the type of the Outbound message
    * @return Kafka's Inbound message as JValue wrapped into Future
    */
  def processToFuture[T](request: T): Future[JValue] = {
    (actor ? request).mapTo[JValue]
  }
  /**
    * This function is used for send request to kafka, and get the result extract to Box result.
    * It processes Kafka's Outbound message to JValue wrapped into Box.
    * @param request The request we send to Kafka
    * @tparam T the type of the Inbound message
    * @return Kafka's Inbound message into Future
    */
  def processRequest[T: Manifest](request: TopicTrait): Future[Box[T]] = {
    import scala.concurrent.ExecutionContext.Implicits.global
    import liftweb.json.compactRender
    implicit val formats = CustomJsonFormats.formats
    val tp = manifest[T].runtimeClass

    (actor ? request)
      .mapTo[JValue]
      .map { jvalue =>
        try {
          Full(jvalue.extract[T])
        } catch {
          case e: Exception => Failure(s"${InvalidConnectorResponse} extract response payload to type ${tp} fail. the payload content: ${compactRender(jvalue)}", Full(e), Empty)
        }
      }
      .recover {
        case e: ParseException => Failure(s"${InvalidConnectorResponse} parse response payload to JValue fail. ${e.getMessage}", Box !! (e.getCause) or Full(e), Empty)
        case e: AskTimeoutException => Failure(s"${KafkaUnknownError} Timeout error, because no response return from kafka server.", Full(e), Empty)
        case e @ (_:AuthenticationException| _:AuthorizationException|
                  _:IllegalStateException| _:InterruptException|
                  _:SerializationException| _:TimeoutException|
                  _:KafkaException| _:ApiException)
          => Failure(s"${KafkaUnknownError} OBP-API send message to kafka server failed", Full(e), Empty)
      }
  }

  def sendOutboundAdapterError(error: String): Unit = actor ! OutboundAdapterError(error)

}
