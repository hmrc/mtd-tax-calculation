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

import play.api.libs.json.{JsValue, Json}
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class CalculationMessageSpec extends JsonErrorValidators with UnitSpec {

  import JsonError._

  val calculationMessage =
    CalculationMessage(
      `type` = "warning"  ,
      text = "You have entered a large amount in total Gift Aid payments. Please check."
    )

  val calculationMessageJson: JsValue = Json.parse(
    s"""
       |{
       | "type": "warning",
       | "text": "You have entered a large amount in total Gift Aid payments. Please check."
       |}
      """.stripMargin)

  val calculationMessageDesJson: String =
    s"""
       |{
       | "type": "WARN",
       | "text": "You have entered a large amount in total Gift Aid payments. Please check."
       |}
      """.stripMargin


  "CalculationMessage reads" should {
    "return valid errors for invalid json" when {

      testMandatoryProperty[CalculationMessage](calculationMessageDesJson)(property = "type")
      testMandatoryProperty[CalculationMessage](calculationMessageDesJson)(property = "text")

      testPropertyType[CalculationMessage](calculationMessageDesJson)(
        property = "type",
        invalidValue = "10",
        errorPathAndError = ".type" -> STRING_FORMAT_EXCEPTION
      )

      testPropertyType[CalculationMessage](calculationMessageDesJson)(
        property = "text",
        invalidValue = "20",
        errorPathAndError = ".text" -> STRING_FORMAT_EXCEPTION
      )
    }
  }

  "reads for CalculationMessage" should {

    "successfully parse the JSON" when {
      "all fields are passed" in {
        CalculationMessage.reads.reads(Json.parse(calculationMessageDesJson)).get shouldBe calculationMessage
      }
    }

    "writes for CalculationMessage" should {

      "successfully render the correct JSON" when {
        "all fields are available" in {
          CalculationMessage.writes.writes(calculationMessage) shouldBe calculationMessageJson
        }
      }
    }
  }
}
