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

import play.api.libs.json.{Format, JsValue, Json}
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class IncomeTaxBandSpec extends JsonErrorValidators with UnitSpec {

  import JsonError._

  val threshold = 4
  val apportionedThreshold = 10
  val rate = 10

  val incomeTaxBand =
    IncomeTaxBand(
      name = "Me",
      rate = rate,
      threshold = Some(threshold),
      apportionedThreshold = Some(apportionedThreshold),
      income = 1000.00,
      amount = 2000.00
    )

  val onlyRequiredDataIncomeTaxBand =
    IncomeTaxBand(
      name = "Me",
      rate = rate,
      threshold = None,
      apportionedThreshold = None,
      income = 1000.00,
      amount = 2000.00
    )

  val incomeTaxBandJson: JsValue = Json.parse(
    s"""
       |{
       | "name": "Me",
       | "rate": 10,
       | "threshold" : 4,
       | "apportionedThreshold" : 10,
       | "income" : 1000.00,
       | "amount" : 2000.00
       |}
      """.stripMargin)

  val onlyRequiredDataIncomeTaxBandJson: JsValue = Json.parse(
    s"""
       |{
       | "name": "Me",
       | "rate": 10,
       | "income" : 1000.00,
       | "amount" : 2000.00
       |}
      """.stripMargin)

  val incomeTaxBandDesJson: String =
    s"""
       |{
       | "name": "Me",
       | "rate": 10,
       | "threshold" : 4,
       | "apportionedThreshold" : 10,
       | "income" : 1000.00,
       | "taxAmount" : 2000.00
       |}
      """.stripMargin

  val onlyRequiredDataIncomeTaxBandDesJson: String =
    s"""
       |{
       | "name": "Me",
       | "rate": 10,
       | "income" : 1000.00,
       | "taxAmount" : 2000.00
       |}
      """.stripMargin

  "IncomeTaxBand reads" should {
    "return valid errors for invalid json" when {

      testMandatoryProperty[IncomeTaxBand](incomeTaxBandDesJson)(property = "name")
      testMandatoryProperty[IncomeTaxBand](incomeTaxBandDesJson)(property = "rate")
      testMandatoryProperty[IncomeTaxBand](incomeTaxBandDesJson)(property = "income")
      testMandatoryProperty[IncomeTaxBand](incomeTaxBandDesJson)(property = "taxAmount")

      testPropertyType[IncomeTaxBand](incomeTaxBandDesJson)(
        property = "name",
        invalidValue = "10",
        errorPathAndError = ".name" -> STRING_FORMAT_EXCEPTION
      )

      testPropertyType[IncomeTaxBand](incomeTaxBandDesJson)(
        property = "rate",
        invalidValue = "\"nan\"",
        errorPathAndError = ".rate" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[IncomeTaxBand](incomeTaxBandDesJson)(
        property = "threshold",
        invalidValue = "\"nan\"",
        errorPathAndError = ".threshold" -> JSNUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[IncomeTaxBand](incomeTaxBandDesJson)(
        property = "apportionedThreshold",
        invalidValue = "\"nan\"",
        errorPathAndError = ".apportionedThreshold" -> JSNUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[IncomeTaxBand](incomeTaxBandDesJson)(
        property = "income",
        invalidValue = "\"nan\"",
        errorPathAndError = ".income" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[IncomeTaxBand](incomeTaxBandDesJson)(
        property = "taxAmount",
        invalidValue = "\"nan\"",
        errorPathAndError = ".taxAmount" -> NUMBER_FORMAT_EXCEPTION
      )
    }
  }

  "reads for IncomeTaxBand" should {

    "successfully validate all fields" in {
      IncomeTaxBand.reads.reads(Json.parse(incomeTaxBandDesJson)).get shouldBe incomeTaxBand
    }

    "successfully validate only required fields" in {
      IncomeTaxBand.reads.reads(Json.parse(onlyRequiredDataIncomeTaxBandDesJson)).get shouldBe onlyRequiredDataIncomeTaxBand
    }
  }

  "writes for IncomeTaxBand" should {

    "successfully validate all fields" in {
      IncomeTaxBand.writes.writes(incomeTaxBand) shouldBe incomeTaxBandJson
    }

    "successfully validate only required fields" in {
      IncomeTaxBand.writes.writes(onlyRequiredDataIncomeTaxBand) shouldBe onlyRequiredDataIncomeTaxBandJson
    }
  }
}
