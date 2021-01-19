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

import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.test.Helpers._
import support.UnitSpec
import uk.gov.hmrc.http.HttpResponse
import v2.fixtures.{DESErrorsFixture, TaxCalcMessagesFixture, TaxCalculationFixture}
import v2.httpparsers.TaxCalcHttpParser.genericHttpReads
import v2.models.errors._
import v2.models.{TaxCalcMessages, TaxCalculation}
import v2.outcomes.DesResponse
import v2.outcomes.TaxCalcOutcome.Outcome

class TaxCalcHttpParserSpec extends UnitSpec {

  val method = "GET"
  val url = "test-url"
  val correlationId = "X-123"

  "read taxCalc" should {

    "return a TaxCalculation model" when {
      "the HttpResponse contains a 200 status and correct response body" in {
        val response: HttpResponse = HttpResponse(OK, TaxCalculationFixture.taxCalcDesJson, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalculation] = genericHttpReads[TaxCalculation].read(method, url, response)

        result shouldBe Right(DesResponse(correlationId, TaxCalculationFixture.taxCalc))
      }
    }

    "return a NotReady response" when {
      "the HttpResponse contains a 204 status" in {
        val response: HttpResponse = HttpResponse(NO_CONTENT, "", Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalculation] = genericHttpReads[TaxCalculation].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId, CalculationNotReady, None))
      }
    }

    "return an Invalid NINO error" when {
      "the HttpResponse contains a 400 status and InvalidNino code" in {
        val response: HttpResponse = HttpResponse(BAD_REQUEST, DESErrorsFixture.invalidIdentifierJson, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalculation] = genericHttpReads[TaxCalculation].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InvalidNinoError, None))
      }
    }

    "return an Invalid CalcID error" when {
      "the HttpResponse contains a 400 status and InvalidCalcID code" in {
        val response: HttpResponse = HttpResponse(BAD_REQUEST, DESErrorsFixture.invalidCalcIDJson, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalculation] = genericHttpReads[TaxCalculation].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InvalidCalcIDError, None))
      }
    }

    "return a Not Found error" when {
      "the HttpResponse contains a 404 status and NotFound code" in {
        val response: HttpResponse = HttpResponse(NOT_FOUND, DESErrorsFixture.notFoundJson, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalculation] = genericHttpReads[TaxCalculation].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,MatchingResourceNotFound, None))
      }

      "the HttpResponse contains a 404 status and a list of errors" in {
        val errorListJson: JsValue = TaxCalculationFixture.v3_2DesTaxCalcErrorJson(
          "INVALID_IDENTIFIER" -> "some reason",
          "INVALID_CALCID" -> "some reason"
        )
        val response: HttpResponse = HttpResponse(NOT_FOUND, errorListJson, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalculation] = genericHttpReads[TaxCalculation].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,MatchingResourceNotFound, None))
      }
    }

    "return an Internal Server Error" when {
      "the HttpResponse contains a 403 status" in {
        val response: HttpResponse = HttpResponse(FORBIDDEN, "", Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalculation] = genericHttpReads[TaxCalculation].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InternalServerError, None))
      }

      "the HttpResponse contains a 500 status and ServerError code" in {
        val response: HttpResponse = HttpResponse(INTERNAL_SERVER_ERROR, DESErrorsFixture.serverErrorJson, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalculation] = genericHttpReads[TaxCalculation].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InternalServerError, None))
      }

      "the HttpResponse contains a 500 status and ServiceUnavailable code" in {
        val response: HttpResponse = HttpResponse(INTERNAL_SERVER_ERROR, DESErrorsFixture.serviceUnavailableJson, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalculation] = genericHttpReads[TaxCalculation].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InternalServerError, None))
      }

      "the HttpResponse contains a 200 status and invalid json" in {
        val response: HttpResponse = HttpResponse(OK, "", Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalculation] = genericHttpReads[TaxCalculation].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InternalServerError, None))
      }

      "the HttpResponse contains a 200 status and no json" in {
        val response: HttpResponse = HttpResponse(OK, "", Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalculation] = genericHttpReads[TaxCalculation].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InternalServerError, None))
      }

      "the HttpResponse contains a 500 status and with invalid json" in {
        val json: JsObject = Json.obj("code1" -> "code", "reason" -> "reason")

        val response: HttpResponse = HttpResponse(INTERNAL_SERVER_ERROR , json, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalcMessages] = genericHttpReads[TaxCalcMessages].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InternalServerError, None))
      }

      "the HttpResponse contains a 500 status and with no json body" in {
        val response: HttpResponse = HttpResponse(INTERNAL_SERVER_ERROR, "", Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalcMessages] = genericHttpReads[TaxCalcMessages].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InternalServerError, None))
      }

      "the HttpResponse contains a 500 status and with invalid DES error code" in {

        val response: HttpResponse = HttpResponse(INTERNAL_SERVER_ERROR, DESErrorsFixture.invalidErrorCodeJson,
          Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalcMessages] = genericHttpReads[TaxCalcMessages].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InternalServerError, None))
      }

      "the HttpResponse contains a 502 status" in {
        val json: JsObject = Json.obj("code" -> "code", "reason" -> "reason")

        val response: HttpResponse = HttpResponse(BAD_GATEWAY , json, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalcMessages] = genericHttpReads[TaxCalcMessages].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InternalServerError, None))
      }
    }
  }

  "read taxCalcMessages" should {
    "return a TaxCalculationMessages model" when {
      "the HttpResponse contains a 200 status and correct response body" in {
        val response: HttpResponse = HttpResponse(OK, TaxCalcMessagesFixture.taxCalcDesWarningsAndErrors, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalcMessages] = genericHttpReads[TaxCalcMessages].read(method, url, response)

        result shouldBe Right(DesResponse(correlationId, TaxCalcMessagesFixture.taxCalcMessages))
      }
    }

    "return a NoContent response" when {
      "the HttpResponse contains a 200 status and response body warning and error counts of 0" in {
        val response: HttpResponse = HttpResponse(OK, TaxCalcMessagesFixture.taxCalcDesNoWarningsAndErrors, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalcMessages] = genericHttpReads[TaxCalcMessages].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,NoContentReturned, None))
      }
    }

    "return a NotReady response" when {
      "the HttpResponse contains a 204 status" in {
        val response: HttpResponse = HttpResponse(NO_CONTENT, "", Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalcMessages] = genericHttpReads[TaxCalcMessages].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,CalculationNotReady, None))
      }
    }

    "return an Invalid NINO error" when {
      "the HttpResponse contains a 400 status and InvalidNino code" in {
        val response: HttpResponse = HttpResponse(BAD_REQUEST, DESErrorsFixture.invalidIdentifierJson, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalcMessages] = genericHttpReads[TaxCalcMessages].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InvalidNinoError, None))
      }
    }

    "return an Invalid CalcID error" when {
      "the HttpResponse contains a 400 status and InvalidCalcID code" in {
        val response: HttpResponse = HttpResponse(BAD_REQUEST, DESErrorsFixture.invalidCalcIDJson, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalcMessages] = genericHttpReads[TaxCalcMessages].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InvalidCalcIDError, None))
      }

    }

    "return a Not Found error" when {
      "the HttpResponse contains a 404 status and NotFound code" in {
        val response: HttpResponse = HttpResponse(NOT_FOUND, DESErrorsFixture.notFoundJson, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalcMessages] = genericHttpReads[TaxCalcMessages].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,MatchingResourceNotFound, None))
      }

      "the HttpResponse contains a 404 status and a list of errors" in {
        val errorListJson: JsValue = TaxCalculationFixture.v3_2DesTaxCalcErrorJson(
          "INVALID_IDENTIFIER" -> "some reason",
          "INVALID_CALCID" -> "some reason"
        )
        val response: HttpResponse = HttpResponse(NOT_FOUND, errorListJson, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalcMessages] = genericHttpReads[TaxCalcMessages].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,MatchingResourceNotFound, None))
      }
    }

    "return an Internal Server Error" when {
      "the HttpResponse contains a 403 status" in {
        val response: HttpResponse = HttpResponse(FORBIDDEN, "", Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalcMessages] = genericHttpReads[TaxCalcMessages].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InternalServerError, None))
      }

      "the HttpResponse contains a 500 status and ServerError code" in {
        val response: HttpResponse = HttpResponse(INTERNAL_SERVER_ERROR, DESErrorsFixture.serverErrorJson, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalcMessages] = genericHttpReads[TaxCalcMessages].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InternalServerError, None))
      }

      "the HttpResponse contains a 503 status and ServiceUnavailable code" in {
        val response: HttpResponse = HttpResponse(SERVICE_UNAVAILABLE, DESErrorsFixture.serviceUnavailableJson, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalcMessages] = genericHttpReads[TaxCalcMessages].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InternalServerError, None))
      }

      "the HttpResponse contains a 500 status and with invalid json" in {
        val errorListJson: JsObject = Json.obj("code1" -> "code", "reason" -> "reason")

        val response: HttpResponse = HttpResponse(INTERNAL_SERVER_ERROR , errorListJson, Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalcMessages] = genericHttpReads[TaxCalcMessages].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InternalServerError, None))
      }

      "the HttpResponse contains a 500 status and with non json body" in {
        val response: HttpResponse = HttpResponse(INTERNAL_SERVER_ERROR , "", Map("CorrelationId" -> Seq(correlationId)))
        val result: Outcome[TaxCalcMessages] = genericHttpReads[TaxCalcMessages].read(method, url, response)

        result shouldBe Left(ErrorWrapper(correlationId,InternalServerError, None))
      }
    }
  }

}