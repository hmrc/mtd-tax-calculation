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

package v2.models.old

import play.api.libs.json.{JsObject, JsValue, Json}
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class SelfEmploymentSpec extends UnitSpec with JsonErrorValidators {

  val validInputJson: String =
    """
      |{
      |     "incomeSourceID": "Some ID",
      |     "taxableIncome": 1234567.89,
      |     "finalised": true,
      |     "losses": 1234567.89
      |}
    """.stripMargin

  val validParsedModel = SelfEmployment(
    selfEmploymentId = "Some ID",
    taxableIncome = BigDecimal("1234567.89"),
    finalised = Some(true),
    losses = Some(BigDecimal("1234567.89"))
  )

  val validOutputJson: JsValue = Json.parse(
    """
      |{
      |     "selfEmploymentId": "Some ID",
      |     "taxableIncome": 1234567.89,
      |     "finalised": true,
      |     "losses": 1234567.89
      |}
    """.stripMargin
  )

  "reads" should {

    import JsonError._

    "return correct validation errors" when {
      testMandatoryProperty[SelfEmployment](validInputJson)(property = "incomeSourceID")
      testMandatoryProperty[SelfEmployment](validInputJson)(property = "taxableIncome")

      testPropertyType[SelfEmployment](validInputJson)(
        property = "incomeSourceID",
        invalidValue = "6",
        errorPathAndError = "/incomeSourceID" -> STRING_FORMAT_EXCEPTION
      )

      testPropertyType[SelfEmployment](validInputJson)(
        property = "taxableIncome",
        invalidValue = "\"some string\"",
        errorPathAndError = "/taxableIncome" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[SelfEmployment](validInputJson)(
        property = "finalised",
        invalidValue = "6",
        errorPathAndError = "/finalised" -> BOOLEAN_FORMAT_EXCEPTION
      )

      testPropertyType[SelfEmployment](validInputJson)(
        property = "losses",
        invalidValue = "\"some string\"",
        errorPathAndError = "/losses" -> NUMBER_FORMAT_EXCEPTION
      )
    }
  }

  "return a successfully read self employment model" when {
    "all fields exist" in {
      Json.parse(validInputJson).as[SelfEmployment] shouldBe validParsedModel
    }

  }

  "writes" should {
    "render the correct Json" when {

      "all fields exist" in {
        Json.toJson(validParsedModel) shouldBe validOutputJson
      }
    }

    "not render the finalised field" when {
      "it has no value" in {

        val model = validParsedModel.copy(finalised = None)

        val json: JsValue = validOutputJson.as[JsObject] - "finalised"

        Json.toJson(model) shouldBe json
      }
    }

    "not render the losses field" when {
      "it has no value" in {

        val model = validParsedModel.copy(losses = None)

        val json: JsValue = validOutputJson.as[JsObject] - "losses"

        Json.toJson(model) shouldBe json
      }
    }
  }
}
