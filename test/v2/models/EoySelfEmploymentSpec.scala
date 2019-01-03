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

package v2.models

import play.api.libs.json.{JsObject, JsValue, Json}
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class EoySelfEmploymentSpec extends UnitSpec with JsonErrorValidators {

  val validEoySelfEmploymentInputJson: String =
    """
      |{
      |     "id": "Some ID",
      |     "taxableIncome": 1234567.89,
      |     "supplied": true,
      |     "finalised": true
      |}
    """.stripMargin

  val validEoySelfEmploymentModel = EoySelfEmployment(
    selfEmploymentId = "Some ID",
    taxableIncome = BigDecimal("1234567.89"),
    supplied = true,
    finalised = Some(true)
  )

  val validEoySelfEmploymentOutputJson: JsValue = Json.parse(
    """
      |{
      |     "selfEmploymentId": "Some ID",
      |     "taxableIncome": 1234567.89,
      |     "supplied": true,
      |     "finalised": true
      |}
    """.stripMargin
  )

  "reads" should {

    import JsonError._

    "return correct validation errors" when {
      testMandatoryProperty[EoySelfEmployment](validEoySelfEmploymentInputJson)(property = "id")
      testMandatoryProperty[EoySelfEmployment](validEoySelfEmploymentInputJson)(property = "taxableIncome")
      testMandatoryProperty[EoySelfEmployment](validEoySelfEmploymentInputJson)(property = "supplied")

      testPropertyType[EoySelfEmployment](validEoySelfEmploymentInputJson)(
        property = "id",
        invalidValue = "800",
        errorPathAndError = "/id" -> STRING_FORMAT_EXCEPTION
      )

      testPropertyType[EoySelfEmployment](validEoySelfEmploymentInputJson)(
        property = "taxableIncome",
        invalidValue = "\"some string\"",
        errorPathAndError = "/taxableIncome" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[EoySelfEmployment](validEoySelfEmploymentInputJson)(
        property = "supplied",
        invalidValue = "800",
        errorPathAndError = "/supplied" -> BOOLEAN_FORMAT_EXCEPTION
      )

      testPropertyType[EoySelfEmployment](validEoySelfEmploymentInputJson)(
        property = "finalised",
        invalidValue = "800",
        errorPathAndError = "/finalised" -> BOOLEAN_FORMAT_EXCEPTION
      )
    }
  }

  "return a successfully read eoy self employment model" when {
    "all fields exist" in {
      Json.parse(validEoySelfEmploymentInputJson).as[EoySelfEmployment] shouldBe validEoySelfEmploymentModel
    }

  }

  "writes" should {
    "render the correct Json" when {

      "all fields exist" in {
        Json.toJson(validEoySelfEmploymentModel) shouldBe validEoySelfEmploymentOutputJson
      }
    }

    "not render the finalised field" when {
      "it has no value" in {

        val model = validEoySelfEmploymentModel.copy(finalised = None)

        val json: JsValue = validEoySelfEmploymentOutputJson.as[JsObject] - "finalised"

        Json.toJson(model) shouldBe json
      }
    }
  }
}
