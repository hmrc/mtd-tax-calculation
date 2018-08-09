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

import play.api.libs.json.{JsValue, Json}
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class AllowancesAndDeductionsSpec extends JsonErrorValidators with UnitSpec {

  import JsonError._

  val allowancesAndDeductions =
    AllowancesAndDeductions(
      totalAllowancesAndDeductions = 123.45,
      giftOfInvestmentsAndPropertyToCharity = Some(123.45),
      apportionedPersonalAllowance = 123.45
    )

  val mandatoryFieldsallowancesAndDeductions =
    AllowancesAndDeductions(
      totalAllowancesAndDeductions = 123.45,
      giftOfInvestmentsAndPropertyToCharity = None,
      apportionedPersonalAllowance = 123.45
    )

  val emptyJson: JsValue = Json.parse("{}")

  val allowancesAndDeductionsJson: String =
    s"""
       |{
       |"totalAllowancesAndDeductions": "123.45",
       |	"allowancesAndDeductions": {
       |			"giftOfInvestmentsAndPropertyToCharity": "123.45",
       |			"apportionedPersonalAllowance": "123.45"
       |   }
       |}
           """.stripMargin

  val noOptionalAllowancesAndDeductions = AllowancesAndDeductions(1000.25, None, 1000.25)

  "reads" should {
    "return correct validation errors" when {

      val mandatoryFieldTest = testMandatoryProperty[AllowancesAndDeductions](allowancesAndDeductionsJson) _

      mandatoryFieldTest("totalAllowancesAndDeductions")
      mandatoryFieldTest("apportionedPersonalAllowance")

      testPropertyType[AllowancesAndDeductions](allowancesAndDeductionsJson)(
        property = "totalAllowancesAndDeductions",
        invalidValue = "\"nan\"",
        errorPathAndError = "/totalAllowancesAndDeductions" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[AllowancesAndDeductions](allowancesAndDeductionsJson)(
        property = "giftOfInvestmentsAndPropertyToCharity",
        invalidValue = "\"nan\"",
        errorPathAndError = "/allowancesAndDeductions/giftOfInvestmentsAndPropertyToCharity" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[AllowancesAndDeductions](allowancesAndDeductionsJson)(
        property = "apportionedPersonalAllowance",
        invalidValue = "\"nan\"",
        errorPathAndError = "/allowancesAndDeductions/apportionedPersonalAllowance" -> NUMBER_FORMAT_EXCEPTION
      )
    }

    "return a correctly read Allowances and Deductions model" when {
      "all fields exist" in {
        val json = Json.parse(
          s"""
             |{
             |  "totalAllowancesAndDeductions": 123.45,
             |		"allowancesAndDeductions": {
             |			"giftOfInvestmentsAndPropertyToCharity": 123.45,
             |			"apportionedPersonalAllowance": 123.45
             |   }
             |}
             """.stripMargin)

        AllowancesAndDeductions.reads.reads(json).get shouldBe allowancesAndDeductions
      }
      "optional fields don't exist" in {
        val json = Json.parse(
          s"""
             |{
             |  "totalAllowancesAndDeductions": 1000.25,
             |		"allowancesAndDeductions": {
             |			"apportionedPersonalAllowance": 1000.25
             |   }
             |}
             """.stripMargin)
        AllowancesAndDeductions.reads.reads(json).get shouldBe noOptionalAllowancesAndDeductions
      }
    }
  }

  "write" should {
    "return client json" when {
      "all fields exist" in {

        val mandatoryFieldJson = Json.parse(
          s"""
             |{
             |      "totalAllowancesAndDeductions": 123.45,
             |			"giftOfInvestmentsAndPropertyToCharity": 123.45,
             |			"apportionedPersonalAllowance": 123.45
             |}
           """.stripMargin)

        AllowancesAndDeductions.writes.writes(allowancesAndDeductions) shouldBe mandatoryFieldJson
      }
      "only mandatory fields exist" in {
        val optionalFieldJson = Json.parse(
          s"""
             |{
             |      "totalAllowancesAndDeductions": 123.45,
             |			"apportionedPersonalAllowance": 123.45
             |}
           """.stripMargin)

        AllowancesAndDeductions.writes.writes(mandatoryFieldsallowancesAndDeductions) shouldBe optionalFieldJson
      }
    }
  }
}

