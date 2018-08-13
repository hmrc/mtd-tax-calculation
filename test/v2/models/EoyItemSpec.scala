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

package v2.models

import play.api.libs.json.{JsObject, JsValue, Json}
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class EoyItemSpec extends UnitSpec with JsonErrorValidators {

  val validEoyItemInputJson: String =
    """
      |{
      |     "taxableIncome": 1234567.89,
      |     "supplied": true,
      |     "finalised": true
      |}
    """.stripMargin

  val validMandatoryOnlyEoyItemInputJson: String =
    """
      |{
      |     "taxableIncome": 1234567.89,
      |     "supplied": true
      |}
    """.stripMargin

  val validEoyItemModel = EoyItem(
    taxableIncome = BigDecimal("1234567.89"),
    supplied = true,
    finalised = Some(true)
  )

  val validMandatoryOnlyEoyItemModel = EoyItem(
    taxableIncome = BigDecimal("1234567.89"),
    supplied = true,
    finalised = None
  )

  val validEoyItemOutputJson: JsValue = Json.parse(
    """
      |{
      |     "taxableIncome": 1234567.89,
      |     "supplied": true,
      |     "finalised": true
      |}
    """.stripMargin
  )

  "reads" should {

    import JsonError._

    "return correct validation errors" when {
      testMandatoryProperty[EoyItem](validEoyItemInputJson)(property = "taxableIncome")
      testMandatoryProperty[EoyItem](validEoyItemInputJson)(property = "supplied")

      testPropertyType[EoyItem](validEoyItemInputJson)(
        property = "taxableIncome",
        invalidValue = "\"some string\"",
        errorPathAndError = "/taxableIncome" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[EoyItem](validEoyItemInputJson)(
        property = "supplied",
        invalidValue = "800",
        errorPathAndError = "/supplied" -> BOOLEAN_FORMAT_EXCEPTION
      )

      testPropertyType[EoyItem](validEoyItemInputJson)(
        property = "finalised",
        invalidValue = "800",
        errorPathAndError = "/finalised" -> BOOLEAN_FORMAT_EXCEPTION
      )
    }
  }

  "return a successfully read eoy item model" when {
    "all fields exist" in {
      Json.parse(validEoyItemInputJson).as[EoyItem] shouldBe validEoyItemModel
    }

    "only mandatory fields exist" in {
      Json.parse(validMandatoryOnlyEoyItemInputJson).as[EoyItem] shouldBe validMandatoryOnlyEoyItemModel
    }

  }

  "writes" should {
    "return client json" when {
      "all fields exist" in {
        Json.toJson(validEoyItemModel) shouldBe validEoyItemOutputJson
      }
    }

    "not render the finalised field" when {
      "it has no value" in {

        val model = validEoyItemModel.copy(finalised = None)

        val json: JsValue = validEoyItemOutputJson.as[JsObject] - "finalised"

        Json.toJson(model) shouldBe json
      }
    }
  }
}