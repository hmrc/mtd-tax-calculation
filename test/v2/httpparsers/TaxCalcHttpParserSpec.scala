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

import play.api.test.Helpers._
import support.UnitSpec
import uk.gov.hmrc.http.HttpResponse
import v2.fixtures.{DESErrorsFixture, TaxCalcMessagesFixture, TaxCalculationFixture}
import v2.httpparsers.TaxCalcHttpParser.{taxCalcHttpReads, taxCalcMessagesHttpReads}
import v2.models.errors._
import v2.outcomes.TaxCalcOutcome.{TaxCalcMessagesOutcome, TaxCalcOutcome}

class TaxCalcHttpParserSpec extends UnitSpec {

  val method = "GET"
  val url = "test-url"

  "read taxCalc" should {

    "return a TaxCalculation model" when {
      "the HttpResponse contains a 200 status and correct response body" in {
        val response = HttpResponse(OK, Some(TaxCalculationFixture.taxCalcDesJson))
        val result: TaxCalcOutcome = taxCalcHttpReads.read(method, url, response)

        result shouldBe Right(TaxCalculationFixture.taxCalc)
      }
    }

    "return a NotReady response" when {
      "the HttpResponse contains a 204 status" in {
        val response = HttpResponse(NO_CONTENT, None)
        val result: TaxCalcOutcome = taxCalcHttpReads.read(method, url, response)

        result shouldBe Left(CalculationNotReady)
      }
    }

    "return an Invalid NINO error" when {
      "the HttpResponse contains a 400 status and InvalidNino code" in {
        val response = HttpResponse(BAD_REQUEST, Some(DESErrorsFixture.invalidIdentifierJson))
        val result: TaxCalcOutcome = taxCalcHttpReads.read(method, url, response)

        result shouldBe Left(InvalidNinoError)
      }
    }

    "return an Invalid CalcID error" when {
      "the HttpResponse contains a 400 status and InvalidCalcID code" in {
        val response = HttpResponse(BAD_REQUEST, Some(DESErrorsFixture.invalidCalcIDJson))
        val result: TaxCalcOutcome = taxCalcHttpReads.read(method, url, response)

        result shouldBe Left(InvalidCalcIDError)
      }

    }

    "return a Not Found error" when {
      "the HttpResponse contains a 404 status and NotFound code" in {
        val response = HttpResponse(NOT_FOUND, Some(DESErrorsFixture.notFoundJson))
        val result: TaxCalcOutcome = taxCalcHttpReads.read(method, url, response)

        result shouldBe Left(MatchingResourceNotFound)
      }

      "the HttpResponse contains a 404 status and a list of errors" in {
        val errorListJson = TaxCalculationFixture.v3_2DesTaxCalcErrorJson(
          DesErrorCode.INVALID_IDENTIFIER -> "some reason",
          DesErrorCode.INVALID_CALCID -> "some reason"
        )
        val response = HttpResponse(NOT_FOUND, Some(errorListJson))
        val result: TaxCalcOutcome = taxCalcHttpReads.read(method, url, response)

        result shouldBe Left(MatchingResourceNotFound)
      }
    }

    "return an Internal Server Error" when {
      "the HttpResponse contains a 403 status" in {
        val response = HttpResponse(FORBIDDEN, None)
        val result: TaxCalcOutcome = taxCalcHttpReads.read(method, url, response)

        result shouldBe Left(InternalServerError)
      }

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

  "read taxCalcMessages" should {
    "return a TaxCalculationMessages model" when {
      "the HttpResponse contains a 200 status and correct response body" in {
        val response = HttpResponse(OK, Some(TaxCalcMessagesFixture.taxCalcDesWarningsAndErrors))
        val result: TaxCalcMessagesOutcome = taxCalcMessagesHttpReads.read(method, url, response)

        result shouldBe Right(TaxCalcMessagesFixture.taxCalcMessages)
      }
    }

    "return a NoContent response" when {
      "the HttpResponse contains a 200 status and response body warning and error counts of 0" in {
        val response = HttpResponse(OK, Some(TaxCalcMessagesFixture.taxCalcDesNoWarningsAndErrors))
        val result: TaxCalcMessagesOutcome = taxCalcMessagesHttpReads.read(method, url, response)

        result shouldBe Left(NoContentReturned)
      }
    }

    "return a NotReady response" when {
      "the HttpResponse contains a 204 status" in {
        val response = HttpResponse(NO_CONTENT, None)
        val result: TaxCalcMessagesOutcome = taxCalcMessagesHttpReads.read(method, url, response)

        result shouldBe Left(CalculationNotReady)
      }
    }

    "return an Invalid NINO error" when {
      "the HttpResponse contains a 400 status and InvalidNino code" in {
        val response = HttpResponse(BAD_REQUEST, Some(DESErrorsFixture.invalidIdentifierJson))
        val result: TaxCalcMessagesOutcome = taxCalcMessagesHttpReads.read(method, url, response)

        result shouldBe Left(InvalidNinoError)
      }
    }

    "return an Invalid CalcID error" when {
      "the HttpResponse contains a 400 status and InvalidCalcID code" in {
        val response = HttpResponse(BAD_REQUEST, Some(DESErrorsFixture.invalidCalcIDJson))
        val result: TaxCalcMessagesOutcome = taxCalcMessagesHttpReads.read(method, url, response)

        result shouldBe Left(InvalidCalcIDError)
      }

    }

    "return a Not Found error" when {
      "the HttpResponse contains a 404 status and NotFound code" in {
        val response = HttpResponse(NOT_FOUND, Some(DESErrorsFixture.notFoundJson))
        val result: TaxCalcMessagesOutcome = taxCalcMessagesHttpReads.read(method, url, response)

        result shouldBe Left(MatchingResourceNotFound)
      }

      "the HttpResponse contains a 404 status and a list of errors" in {
        val errorListJson = TaxCalculationFixture.v3_2DesTaxCalcErrorJson(
          DesErrorCode.INVALID_IDENTIFIER -> "some reason",
          DesErrorCode.INVALID_CALCID -> "some reason"
        )
        val response = HttpResponse(NOT_FOUND, Some(errorListJson))
        val result: TaxCalcMessagesOutcome = taxCalcMessagesHttpReads.read(method, url, response)

        result shouldBe Left(MatchingResourceNotFound)
      }
    }

    "return an Internal Server Error" when {
      "the HttpResponse contains a 403 status" in {
        val response = HttpResponse(FORBIDDEN, None)
        val result: TaxCalcMessagesOutcome = taxCalcMessagesHttpReads.read(method, url, response)

        result shouldBe Left(InternalServerError)
      }

      "the HttpResponse contains a 500 status and ServerError code" in {
        val response = HttpResponse(INTERNAL_SERVER_ERROR, Some(DESErrorsFixture.serverErrorJson))
        val result: TaxCalcMessagesOutcome = taxCalcMessagesHttpReads.read(method, url, response)

        result shouldBe Left(InternalServerError)
      }

      "the HttpResponse contains a 500 status and ServiceUnavailable code" in {
        val response = HttpResponse(INTERNAL_SERVER_ERROR, Some(DESErrorsFixture.serviceUnavailableJson))
        val result: TaxCalcMessagesOutcome = taxCalcMessagesHttpReads.read(method, url, response)

        result shouldBe Left(InternalServerError)
      }
    }
  }

}
