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

package v2.models.audit

import play.api.http.Status
import play.api.libs.json.Json
import support.UnitSpec
import v2.fixtures.TaxCalculationFixture

class RetrieveTaxCalcAuditResponseSpec extends UnitSpec {

  private val responseSuccess = RetrieveTaxCalcAuditResponse(Status.OK, None, Some(TaxCalculationFixture.taxCalcClientJson))
  private val responseFail = RetrieveTaxCalcAuditResponse(Status.BAD_REQUEST, Some(Seq(AuditError("FORMAT_NINO"))), None)

  "writes" when {
    "passed an audit response model with success tax calculation field" should {
      "produce valid json" in {

        val taxCalcResponse = Json.stringify(TaxCalculationFixture.taxCalcClientJson)
        val json = Json.parse(
          s"""
             |{
             |   "httpStatus": 200,
             |   "payload": $taxCalcResponse
             |}
           """.stripMargin)

        Json.toJson(responseSuccess) shouldBe json
      }
    }

    "passed an audit response model with error field" should {
      "produce valid json" in {
        val json = Json.parse(
          s"""
             |{
             |    "httpStatus": 400,
             |    "errors": [
             |      {
             |        "errorCode": "FORMAT_NINO"
             |      }
             |     ]
             |}
           """.stripMargin)

        Json.toJson(responseFail) shouldBe json
      }
    }
  }
}
