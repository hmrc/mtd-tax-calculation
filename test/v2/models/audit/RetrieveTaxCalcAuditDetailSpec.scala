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

package v2.models.audit

import play.api.http.Status
import play.api.libs.json.Json
import support.UnitSpec
import v2.fixtures.TaxCalculationFixture

class RetrieveTaxCalcAuditDetailSpec extends UnitSpec {

  private val userType = "Organisation"
  private val agentReferenceNumber = Some("012345678")
  private val calculationId = "77427777"
  private val nino = "AA123456A"
  private val `X-CorrelationId` = "X-123"
  private val responseSuccess = RetrieveTaxCalcAuditResponse(Status.OK, None, Some(TaxCalculationFixture.taxCalcClientJson))
  private val responseFail = RetrieveTaxCalcAuditResponse(Status.BAD_REQUEST, Some(Seq(AuditError("FORMAT_NINO"))), None)

  "writes" when {
    "passed an audit detail model with success tax calculation field" should {
      "produce valid json" in {

        val taxCalcResponse = Json.stringify(TaxCalculationFixture.taxCalcClientJson)
        val json = Json.parse(
          s"""
             |{
             |        "nino": "AA123456A",
             |        "calculationId": "77427777",
             |        "arn": "012345678",
             |        "userType": "Agent",
             |        "X-CorrelationId": "X-123",
             |        "response": {
             |            "httpStatus": 200,
             |            "payload": $taxCalcResponse
             |        }
             |}
           """.stripMargin)

        val model = RetrieveTaxCalcAuditDetail("Agent", agentReferenceNumber, nino, calculationId, `X-CorrelationId`, responseSuccess)

        Json.toJson(model) shouldBe json
      }
    }

    "passed an audit detail model with errors field" should {
      "produce valid json" in {
        val json = Json.parse(
          s"""
             |{
             |    "nino": "AA123456A",
             |    "calculationId": "77427777",
             |    "userType":"Organisation",
             |    "X-CorrelationId": "X-123",
             |    "response": {
             |      "httpStatus": 400,
             |      "errors": [
             |      {
             |        "errorCode": "FORMAT_NINO"
             |      }
             |     ]
             |    }
             |}
           """.stripMargin)

        val model = RetrieveTaxCalcAuditDetail(userType, None, nino, calculationId, `X-CorrelationId`, responseFail)

        Json.toJson(model) shouldBe json
      }
    }
  }
}
