/*
 * Copyright 2018 HM Revenue & Customs
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
import uk.gov.hmrc.http.{HttpReads, HttpResponse}
import v2.models._
import v2.models.errors._
import v2.outcomes.TaxCalcOutcome.TaxCalcOutcome

object TaxCalcHttpParser extends HttpParser {

  implicit val taxCalcHttpReads: HttpReads[TaxCalcOutcome] = new HttpReads[TaxCalcOutcome] {
    override def read(method: String, url: String, response: HttpResponse): TaxCalcOutcome = {
      (response.status, response.jsonOpt) match {
        case (OK, _) => parseResponse(response)
        case (NO_CONTENT, _) => Left(CalculationNotReady)
        case (BAD_REQUEST, ErrorCode(DesErrorCode.INVALID_IDENTIFIER)) => Left(InvalidNinoError)
        case (BAD_REQUEST, ErrorCode(DesErrorCode.INVALID_CALCID)) => Left(InvalidCalcIDError)
        case (FORBIDDEN, _) => Left(InternalServerError)
        case (NOT_FOUND, _) => Left(NotFound)
        case (INTERNAL_SERVER_ERROR, ErrorCode(DesErrorCode.SERVER_ERROR)) => Left(InternalServerError)
        case (INTERNAL_SERVER_ERROR, ErrorCode(DesErrorCode.SERVICE_UNAVAILABLE)) => Left(InternalServerError)
      }
    }

    private def parseResponse(response: HttpResponse): TaxCalcOutcome = response.validateJson[TaxCalculation] match {
      case Some(taxCalc) => Right(taxCalc)
      case None => Left(InternalServerError)
    }
  }
}