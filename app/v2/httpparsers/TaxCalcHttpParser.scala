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

import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.Reads
import uk.gov.hmrc.http.{HttpReads, HttpResponse}
import v2.models.{TaxCalcMessages, _}
import v2.models.errors._
import v2.outcomes.TaxCalcOutcome.Outcome

object TaxCalcHttpParser extends HttpParser {

  val logger = Logger(this.getClass)

  implicit def genericHttpReads[A: Reads]: HttpReads[Outcome[A]] = reads[A]

  private def reads[A: Reads] = new HttpReads[Outcome[A]] {
    override def read(method: String, url: String, response: HttpResponse): Outcome[A] = {
      (response.status, response.jsonOpt) match {
        case (OK, _) => parseResponse(response)
        case (NO_CONTENT, _) => Left(CalculationNotReady)
        case (BAD_REQUEST, ErrorCode(DesErrorCode.INVALID_IDENTIFIER)) => Left(InvalidNinoError)
        case (BAD_REQUEST, ErrorCode(DesErrorCode.INVALID_CALCID)) => Left(InvalidCalcIDError)
        case (FORBIDDEN, _) => Left(InternalServerError)
        case (NOT_FOUND, _) => Left(MatchingResourceNotFound)
        case (INTERNAL_SERVER_ERROR, ErrorCode(DesErrorCode.SERVER_ERROR)) => Left(InternalServerError)
        case (SERVICE_UNAVAILABLE, ErrorCode(DesErrorCode.SERVICE_UNAVAILABLE)) => Left(InternalServerError)
        case (_, _) => logger.warn(s"Unexpected error received from DES with status ${response.status} and body ${response.jsonOpt}")
          Left(InternalServerError)
      }
    }

    private def parseResponse(response: HttpResponse): Outcome[A] = response.validateJson[A] match {
      case Some(taxCalc) if taxCalc.isInstanceOf[ITaxCalc] => Right(taxCalc)
      case Some(taxCalcMessages) if taxCalcMessages.isInstanceOf[ITaxCalcMessages] =>
        taxCalcMessages match {
          case TaxCalcMessages(0, 0, _) => Left(NoContentReturned)
          case old.TaxCalcMessages(0, 0, _) => Left(NoContentReturned)
          case _ => Right(taxCalcMessages)
        }
      case None => Left(InternalServerError)
    }
  }
}