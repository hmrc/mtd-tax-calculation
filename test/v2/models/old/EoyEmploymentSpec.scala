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

class EoyEmploymentSpec extends UnitSpec with JsonErrorValidators {

  val validEoyEmploymentInputJson: String =
    """
      |{
      |     "id": "Some ID",
      |     "taxableIncome": 1234567.89,
      |     "supplied": true,
      |     "finalised": true
      |}
    """.stripMargin

  val validEoyEmploymentModel = EoyEmployment(
    employmentId = "Some ID",
    taxableIncome = BigDecimal("1234567.89"),
    supplied = true,
    finalised = Some(true)
  )

  val validEoyEmploymentOutputJson: JsValue = Json.parse(
    """
      |{
      |     "employmentId": "Some ID",
      |     "taxableIncome": 1234567.89,
      |     "supplied": true,
      |     "finalised": true
      |}
    """.stripMargin
  )

  "reads" should {

    import JsonError._

    "return correct validation errors" when {
      testMandatoryProperty[EoyEmployment](validEoyEmploymentInputJson)(property = "id")
      testMandatoryProperty[EoyEmployment](validEoyEmploymentInputJson)(property = "taxableIncome")
      testMandatoryProperty[EoyEmployment](validEoyEmploymentInputJson)(property = "supplied")

      testPropertyType[EoyEmployment](validEoyEmploymentInputJson)(
        property = "id",
        invalidValue = "800",
        errorPathAndError = "/id" -> STRING_FORMAT_EXCEPTION
      )

      testPropertyType[EoyEmployment](validEoyEmploymentInputJson)(
        property = "taxableIncome",
        invalidValue = "\"some string\"",
        errorPathAndError = "/taxableIncome" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[EoyEmployment](validEoyEmploymentInputJson)(
        property = "supplied",
        invalidValue = "800",
        errorPathAndError = "/supplied" -> BOOLEAN_FORMAT_EXCEPTION
      )

      testPropertyType[EoyEmployment](validEoyEmploymentInputJson)(
        property = "finalised",
        invalidValue = "800",
        errorPathAndError = "/finalised" -> BOOLEAN_FORMAT_EXCEPTION
      )
    }
  }

  "return a successfully read eoy employment model" when {
    "all fields exist" in {
      Json.parse(validEoyEmploymentInputJson).as[EoyEmployment] shouldBe validEoyEmploymentModel
    }

  }

  "writes" should {
    "render the correct Json" when {

      "all fields exist" in {
        Json.toJson(validEoyEmploymentModel) shouldBe validEoyEmploymentOutputJson
      }
    }

    "not render the finalised field" when {
      "it has no value" in {

        val model = validEoyEmploymentModel.copy(finalised = None)

        val json: JsValue = validEoyEmploymentOutputJson.as[JsObject] - "finalised"

        Json.toJson(model) shouldBe json
      }
    }
  }
}
