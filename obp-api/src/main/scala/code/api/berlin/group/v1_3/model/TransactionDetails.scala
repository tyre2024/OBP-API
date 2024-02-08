/**
 * BG PSD2 API
 * # Summary The **NextGenPSD2** *Framework Version 1.3* offers a modern, open, harmonised and interoperable set of  Application Programming Interfaces (APIs) as the safest and most efficient way to provide data securely.  The NextGenPSD2 Framework reduces XS2A complexity and costs, addresses the problem of multiple competing standards  in Europe and, aligned with the goals of the Euro Retail Payments Board, enables European banking customers to benefit from innovative products and services ('Banking as a Service')  by granting TPPs safe and secure (authenticated and authorised) access to their bank accounts and financial data.  The possible Approaches are:   * Redirect SCA Approach   * OAuth SCA Approach   * Decoupled SCA Approach   * Embedded SCA Approach without SCA method   * Embedded SCA Approach with only one SCA method available   * Embedded SCA Approach with Selection of a SCA method    Not every message defined in this API definition is necessary for all approaches.    Furthermore this API definition does not differ between methods which are mandatory, conditional, or optional   Therefore for a particular implementation of a Berlin Group PSD2 compliant API it is only necessary to support    a certain subset of the methods defined in this API definition.    **Please have a look at the implementation guidelines if you are not sure    which message has to be used for the approach you are going to use.**  ## Some General Remarks Related to this version of the OpenAPI Specification: * **This API definition is based on the Implementation Guidelines of the Berlin Group PSD2 API.**    It is not an replacement in any sense.   The main specification is (at the moment) always the Implementation Guidelines of the Berlin Group PSD2 API. * **This API definition contains the REST-API for requests from the PISP to the ASPSP.** * **This API definition contains the messages for all different approaches defined in the Implementation Guidelines.** * According to the OpenAPI-Specification [https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.1.md]        \"If in is \"header\" and the name field is \"Accept\", \"Content-Type\" or \"Authorization\", the parameter definition SHALL be ignored.\"      The element \"Accept\" will not be defined in this file at any place.      The elements \"Content-Type\" and \"Authorization\" are implicitly defined by the OpenApi tags \"content\" and \"security\".    * There are several predefined types which might occur in payment initiation messages,    but are not used in the standard JSON messages in the Implementation Guidelines.   Therefore they are not used in the corresponding messages in this file either.   We added them for the convenience of the user.   If there is a payment product, which need these field, one can easily use the predefined types.   But the ASPSP need not to accept them in general.    * **We omit the definition of all standard HTTP header elements (mandatory/optional/conditional)    except they are mention in the Implementation Guidelines.**   Therefore the implementer might add the in his own realisation of a PSD2 comlient API in addition to the elements define in this file.     ## General Remarks on Data Types  The Berlin Group definition of UTF-8 strings in context of the PSD2 API have to support at least the following characters  a b c d e f g h i j k l m n o p q r s t u v w x y z  A B C D E F G H I J K L M N O P Q R S T U V W X Y Z  0 1 2 3 4 5 6 7 8 9  / - ? : ( ) . , ' +  Space 
 *
 * The version of the OpenAPI document: 1.3 Dec 20th 2018
 * Contact: info@berlin-group.org
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package code.api.berlin.group.v1_3.model

import java.time.LocalDate


case class TransactionDetails (
  /* the Transaction Id can be used as access-ID in the API, where more details on an transaction is offered.  If this data attribute is provided this shows that the AIS can get access on more details about this  transaction using the GET Transaction Details Request   */
  transactionId: Option[String] = None,
  /* Is the identification of the transaction as used e.g. for reference for deltafunction on application level.  The same identification as for example used within camt.05x messages.  */
  entryReference: Option[String] = None,
  /* Unique end to end identity. */
  endToEndId: Option[String] = None,
  /* Identification of Mandates, e.g. a SEPA Mandate ID. */
  mandateId: Option[String] = None,
  /* Identification of a Cheque. */
  checkId: Option[String] = None,
  /* Identification of Creditors, e.g. a SEPA Creditor ID. */
  creditorId: Option[String] = None,
  /* The Date when an entry is posted to an account on the ASPSPs books.  */
  bookingDate: Option[LocalDate] = None,
  /* The Date at which assets become available to the account owner in case of a credit. */
  valueDate: Option[LocalDate] = None,
  transactionAmount: Amount,
  /* Array of exchange rates */
  exchangeRate: Option[Seq[ExchangeRate]] = None,
  /* Creditor Name */
  creditorName: Option[String] = None,
  creditorAccount: Option[AccountReference] = None,
  /* Ultimate Creditor */
  ultimateCreditor: Option[String] = None,
  /* Debtor Name */
  debtorName: Option[String] = None,
  debtorAccount: Option[AccountReference] = None,
  /* Ultimate Debtor */
  ultimateDebtor: Option[String] = None,
  remittanceInformationUnstructured: Option[String] = None,
  /* Reference as contained in the structured remittance reference structure (without the surrounding XML structure).  Different from other places the content is containt in plain form not in form of a structered field.  */
  remittanceInformationStructured: Option[String] = None,
  purposeCode: Option[PurposeCode] = None,
  /* Bank transaction code as used by the ASPSP and using the sub elements of this structured code defined by ISO 20022.   This code type is concatenating the three ISO20022 Codes    * Domain Code,    * Family Code, and    * SubFamiliy Code  by hyphens, resulting in �DomainCode�-�FamilyCode�-�SubFamilyCode�.  */
  bankTransactionCode: Option[String] = None,
  /* Proprietary bank transaction code as used within a community or within an ASPSP e.g.  for MT94x based transaction reports.  */
  proprietaryBankTransactionCode: Option[String] = None,
  _links: Option[LinksTransactionDetails] = None
) extends ApiModel

object TransactionDetailsEnums {

}
