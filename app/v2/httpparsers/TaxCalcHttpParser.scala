/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package v2.httpparsers

import play.api.http.Status._
import play.api.libs.json.{Reads, __}
import uk.gov.hmrc.http.{HttpReads, HttpResponse}
import v2.models.errors._
import v2.models.{TaxCalcMessages, _}
import v2.outcomes.DesResponse
import v2.outcomes.TaxCalcOutcome.Outcome

object TaxCalcHttpParser extends HttpParser {

  implicit def genericHttpReads[A: Reads]: HttpReads[Outcome[A]] = reads[A]

  private def reads[A: Reads]: HttpReads[Outcome[A]] = new HttpReads[Outcome[A]] {
    override def read(method: String, url: String, response: HttpResponse): Outcome[A] = {
      response.status match {
        case OK => parseResponse(response)
        case NO_CONTENT => Left(ErrorWrapper(retrieveCorrelationId(response), CalculationNotReady, None))
        case BAD_REQUEST => Left(parseError(response))
        case FORBIDDEN => Left(ErrorWrapper(retrieveCorrelationId(response), InternalServerError, None))
        case NOT_FOUND => Left(ErrorWrapper(retrieveCorrelationId(response), MatchingResourceNotFound, None))
        case INTERNAL_SERVER_ERROR | SERVICE_UNAVAILABLE =>
          Left(parseError(response))
        case _ => logger.warn(s"Unexpected error received from DES with status ${response.status} and body ${response.body}")
          Left(parseError(response))
      }
    }

    private def parseResponse(response: HttpResponse): Outcome[A] = response.validateJson[A] match {
      case Some(taxCalc) if taxCalc.isInstanceOf[ITaxCalc] => Right(DesResponse(retrieveCorrelationId(response), taxCalc))
      case Some(taxCalcMessages) if taxCalcMessages.isInstanceOf[ITaxCalcMessages] =>
        val correlationId = retrieveCorrelationId(response)
        taxCalcMessages match {
          case TaxCalcMessages(0, 0, _) => Left(ErrorWrapper(correlationId, NoContentReturned, None))
          case _ => Right(DesResponse(retrieveCorrelationId(response), taxCalcMessages))
        }
      case None => Left(ErrorWrapper(retrieveCorrelationId(response), InternalServerError, None))
    }

    private def parseError(response: HttpResponse): ErrorWrapper = {
      val errorReads: Reads[Option[String]] = (__ \ "code").readNullable[String]
      val default = ErrorWrapper(retrieveCorrelationId(response), InternalServerError, None)

      response.validateJson(errorReads).fold(default) {
        case Some(error) => ErrorWrapper(retrieveCorrelationId(response), desErrorToMtdErrorCreate(error), None)
        case _ => default
      }
    }
  }

  private def desErrorToMtdErrorCreate: Map[String, Error] = Map(
    "INVALID_IDENTIFIER" -> InvalidNinoError,
    "INVALID_CALCID" -> InvalidCalcIDError,
    "SERVER_ERROR" -> InternalServerError,
    "SERVICE_UNAVAILABLE" -> InternalServerError
  ).withDefault { error =>
    logger.warn(s"[TaxCalcHttpParser] [desErrorToMtdErrorCreate] - No mapping found for error code $error")
    InternalServerError
  }
}