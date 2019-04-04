/*
 * Copyright 2019 HM Revenue & Customs
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
import play.api.libs.json.Reads
import uk.gov.hmrc.http.{HttpReads, HttpResponse}
import v2.models.errors._
import v2.models.{TaxCalcMessages, _}
import v2.outcomes.DesResponse
import v2.outcomes.TaxCalcOutcome.Outcome

object TaxCalcHttpParser extends HttpParser {

  implicit def genericHttpReads[A: Reads]: HttpReads[Outcome[A]] = reads[A]

  private def reads[A: Reads] = new HttpReads[Outcome[A]] {
    override def read(method: String, url: String, response: HttpResponse): Outcome[A] = {
      (response.status, response.jsonOpt) match {
        case (OK, _) => parseResponse(response)
        case (NO_CONTENT, _) => Left(ErrorWrapper(Some(retrieveCorrelationId(response)), CalculationNotReady, None))
        case (BAD_REQUEST, ErrorCode(DesErrorCode.INVALID_IDENTIFIER)) =>
          Left(ErrorWrapper(Some(retrieveCorrelationId(response)), InvalidNinoError, None))
        case (BAD_REQUEST, ErrorCode(DesErrorCode.INVALID_CALCID)) =>
          Left(ErrorWrapper(Some(retrieveCorrelationId(response)), InvalidCalcIDError, None))
        case (FORBIDDEN, _) => Left(ErrorWrapper(Some(retrieveCorrelationId(response)), InternalServerError, None))
        case (NOT_FOUND, _) => Left(ErrorWrapper(Some(retrieveCorrelationId(response)), MatchingResourceNotFound, None))
        case (INTERNAL_SERVER_ERROR, ErrorCode(DesErrorCode.SERVER_ERROR)) =>
          Left(ErrorWrapper(Some(retrieveCorrelationId(response)), InternalServerError, None))
        case (SERVICE_UNAVAILABLE, ErrorCode(DesErrorCode.SERVICE_UNAVAILABLE)) =>
          Left(ErrorWrapper(Some(retrieveCorrelationId(response)), InternalServerError, None))
        case (_, _) => logger.warn(s"Unexpected error received from DES with status ${response.status} and body ${response.jsonOpt}")
          Left(ErrorWrapper(Some(retrieveCorrelationId(response)), InternalServerError, None))
      }
    }

    private def parseResponse(response: HttpResponse): Outcome[A] = response.validateJson[A] match {
      case Some(taxCalc) if taxCalc.isInstanceOf[ITaxCalc] => Right(DesResponse(retrieveCorrelationId(response), taxCalc))
      case Some(taxCalcMessages) if taxCalcMessages.isInstanceOf[ITaxCalcMessages] =>
        val correlationId = retrieveCorrelationId(response)
        taxCalcMessages match {
          case TaxCalcMessages(0, 0, _) => Left(ErrorWrapper(Some(correlationId), NoContentReturned, None))
          case old.TaxCalcMessages(0, 0, _) => Left(ErrorWrapper(Some(correlationId), NoContentReturned, None))
          case _ => Right(DesResponse(retrieveCorrelationId(response), taxCalcMessages))
        }
      case None => Left(ErrorWrapper(Some(retrieveCorrelationId(response)), InternalServerError, None))
    }
  }
}
