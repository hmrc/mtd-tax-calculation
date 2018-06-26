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

import play.api.test.Helpers._
import support.UnitSpec
import uk.gov.hmrc.http.HttpResponse
import v2.fixtures.{DESErrorsFixture, TaxCalculationFixture}
import v2.httpparsers.TaxCalcHttpParser.taxCalcHttpReads
import v2.models.errors.{InternalServerError, InvalidCalcID, InvalidNino, NotFound}
import v2.outcomes.TaxCalcOutcome.TaxCalcOutcome

class TaxCalcHttpParserSpec extends UnitSpec {

  val method = "GET"
  val url = "test-url"

  "read" should {
    "return a TaxCalculation model" when {
      "the HttpResponse contains a 200 status and correct response body" in {
        val response = HttpResponse(OK, Some(TaxCalculationFixture.desTaxCalcJson))
        val result: TaxCalcOutcome = taxCalcHttpReads.read(method, url, response)

        result shouldBe Right(TaxCalculationFixture.taxCalc)
      }
    }
    "return an Invalid NINO error" when {
      "the HttpResponse contains a 400 status and InvalidNino code" in {
        val response = HttpResponse(BAD_REQUEST, Some(DESErrorsFixture.invalidIdentifierJson))
        val result: TaxCalcOutcome = taxCalcHttpReads.read(method, url, response)

        result shouldBe Left(InvalidNino)
      }
    }
    "return an Invalid CalcID error" when {
      "the HttpResponse contains a 400 status and InvalidCalcID code" in {
        val response = HttpResponse(BAD_REQUEST, Some(DESErrorsFixture.invalidCalcIDJson))
        val result: TaxCalcOutcome = taxCalcHttpReads.read(method, url, response)

        result shouldBe Left(InvalidCalcID)
      }

    }
    "return a Not Found error" when {
      "the HttpResponse contains a 404 status and NotFound code" in {
        val response = HttpResponse(NOT_FOUND, Some(DESErrorsFixture.notFoundJson))
        val result: TaxCalcOutcome = taxCalcHttpReads.read(method, url, response)

        result shouldBe Left(NotFound)
      }
    }
    "return an Internal Server Error" when {
      "the HttpResponse contains a 500 status and ServerError code" in {
        val response = HttpResponse(INTERNAL_SERVER_ERROR, Some(DESErrorsFixture.serverErrorJson))
        val result: TaxCalcOutcome = taxCalcHttpReads.read(method, url, response)

        result shouldBe Left(InternalServerError)
      }
      "the HttpResponse contains a 500 status and ServiceUnavailable code" in {
        val response = HttpResponse(INTERNAL_SERVER_ERROR, Some(DESErrorsFixture.serviceUnavailableJson))
        val result: TaxCalcOutcome = taxCalcHttpReads.read(method, url, response)

        result shouldBe Left(InternalServerError)
      }
    }
  }
}
