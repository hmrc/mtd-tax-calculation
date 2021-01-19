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

package v2.models

import play.api.libs.json.{JsValue, Json}
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class UKPropertySpec extends JsonErrorValidators with UnitSpec {

  import JsonError._

  val ukProperty =
    UKProperty(
      totalIncome = Some(123.45),
      nonFurnishedHolidayLettingsTaxableProfit = Some(123.45),
      nonFurnishedHolidayLettingsLoss = Some(123.45),
      furnishedHolidayLettingsTaxableProfit = Some(123.45),
      furnishedHolidayLettingsLoss = Some(123.45),
      finalised = Some(true)
    )

  val ukPropertyDesJson: String =
    """
      |{
      | "ukPropertyIncome": 123.45,
      | "ukProperty": {
      |    "taxableProfit": 123.45,
      |    "losses": 123.45,
      |    "taxableProfitFhlUk": 123.45,
      |    "lossesFhlUk": 123.45,
      |    "finalised": true
      | }
      |}
      |""".stripMargin

  val emptyUkProperty = UKProperty(None,None,None,None,None,None)

  val emptyJson: JsValue = Json.parse("{}")

  "reads" should {
    "return correct validation errors" when {

      testPropertyType[UKProperty](ukPropertyDesJson)(
        property = "ukPropertyIncome",
        invalidValue = "\"nan\"",
        errorPathAndError = "/ukPropertyIncome" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[UKProperty](ukPropertyDesJson)(
        property = "taxableProfit",
        invalidValue = "\"nan\"",
        errorPathAndError = "/ukProperty/taxableProfit" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[UKProperty](ukPropertyDesJson)(
        property = "losses",
        invalidValue = "\"nan\"",
        errorPathAndError = "/ukProperty/losses" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[UKProperty](ukPropertyDesJson)(
        property = "taxableProfitFhlUk",
        invalidValue = "\"nan\"",
        errorPathAndError = "/ukProperty/taxableProfitFhlUk" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[UKProperty](ukPropertyDesJson)(
        property = "finalised",
        invalidValue = "\"not a boolean\"",
        errorPathAndError = "/ukProperty/finalised" -> BOOLEAN_FORMAT_EXCEPTION
      )
    }

    "return a correctly read UKProperty model" when {
      "all fields exist" in {
        val json = Json.parse(
          s"""
             |{
             | "ukPropertyIncome": 123.45,
             | "ukProperty": {
             |    "taxableProfit": 123.45,
             |    "losses": 123.45,
             |    "taxableProfitFhlUk": 123.45,
             |    "lossesFhlUk": 123.45,
             |    "finalised": true
             | }
             |}
             """.stripMargin)

        UKProperty.reads.reads(json).get shouldBe ukProperty
      }

      "nullable fields don't exist" in {
        val json = Json.parse(
          """
            |{
            | "ukProperty": {}
            |}
            |""".stripMargin)
        UKProperty.reads.reads(json).get shouldBe emptyUkProperty
      }
    }
  }

  "write" should {
    "return client json" when {
      "all fields exist" in {

        val clientJson = Json.parse(
          s"""
             |{
             |  "totalIncome": 123.45,
             |  "nonFurnishedHolidayLettingsTaxableProfit": 123.45,
             |  "nonFurnishedHolidayLettingsLoss": 123.45,
             |  "furnishedHolidayLettingsTaxableProfit": 123.45,
             |  "furnishedHolidayLettingsLoss": 123.45,
             |  "finalised": true
             |}
           """.stripMargin)

        UKProperty.writes.writes(ukProperty) shouldBe clientJson
      }

      "all fields don't exist" in {
        UKProperty.writes.writes(emptyUkProperty) shouldBe emptyJson
      }
    }
  }
}
