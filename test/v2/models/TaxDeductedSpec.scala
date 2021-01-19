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

class TaxDeductedSpec extends UnitSpec with JsonErrorValidators {

  import JsonError._

  val taxDeductedDesJson: String =
    """
      |{
      | "taxDeducted": {
      |   "ukLandAndProperty": 123.45,
      |   "bbsi": 345.67
      | },
      | "totalTaxDeducted": 234.56
      |}
    """.stripMargin

  val taxDeducted =
    TaxDeducted(
      ukLandAndProperty = Some(123.45),
      savings = Some(345.67),
      totalTaxDeducted = Some(234.56)
    )

  val clientJson: JsValue = Json.parse(
    s"""
       |{
       |  "ukLandAndProperty": 123.45,
       |  "totalTaxDeducted": 234.56,
       |  "savings": 345.67
       |}
     """.stripMargin
  )

  val emptyTaxDeducted = TaxDeducted(None, None, None)

  "reads" should {
    "return correct validation errors" when {

      testPropertyType[TaxDeducted](taxDeductedDesJson)(
        property = "bbsi",
        invalidValue = "\"nan\"",
        errorPathAndError = "taxDeducted/bbsi" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[TaxDeducted](taxDeductedDesJson)(
        property = "ukLandAndProperty",
        invalidValue = "\"nan\"",
        errorPathAndError = "taxDeducted/ukLandAndProperty" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[TaxDeducted](taxDeductedDesJson)(
        property = "totalTaxDeducted",
        invalidValue = "\"nan\"",
        errorPathAndError = "/totalTaxDeducted" -> NUMBER_FORMAT_EXCEPTION
      )
    }

    "return a correctly read TaxDeducted model" when {
      "all fields exist" in {
        TaxDeducted.reads.reads(Json.parse(taxDeductedDesJson)).get shouldBe taxDeducted
      }

      "the optional totalTaxDeducted property does not exist" in {

        val json: JsValue = Json.parse(
          """{
            |"taxDeducted": {
            | "ukLandAndProperty": 123.45,
            | "bbsi": 345.67
            | }
            |}
            |"""".stripMargin)

        TaxDeducted.reads.reads(json).get shouldBe taxDeducted.copy(totalTaxDeducted = None)
      }
      "the optional ukLandAndProperty property does not exist and the parent taxDeducted model exists" in {

        val json: JsValue = Json.parse(
          """{
            | "taxDeducted": {
            |   "bbsi": 345.67
            | },
            | "totalTaxDeducted": 234.56
            |}
            |"""".stripMargin)

        TaxDeducted.reads.reads(json).get shouldBe taxDeducted.copy(ukLandAndProperty = None)
      }
      "the optional bbsi property does not exist and the parent taxDeducted model exists" in {

        val json: JsValue = Json.parse(
          """{
            | "taxDeducted": {
            |   "ukLandAndProperty": 123.45
            | },
            | "totalTaxDeducted": 234.56
            |}
            |"""".stripMargin)

        TaxDeducted.reads.reads(json).get shouldBe taxDeducted.copy(savings = None)
      }

      "no optional properties exist" in {
        val json: JsValue = Json.parse("{}")

        TaxDeducted.reads.reads(json).get shouldBe emptyTaxDeducted
      }
    }
  }

  "write" should {
    "return client json" when {

      val emptyClientJson = Json.parse("{}")

      "all fields exist" in {
        TaxDeducted.writes.writes(taxDeducted) shouldBe clientJson
      }

      "all fields don't exist" in {
        TaxDeducted.writes.writes(emptyTaxDeducted) shouldBe emptyClientJson
      }
    }
  }
}
