/*
 * Copyright 2020 HM Revenue & Customs
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

class IncomeTaxItemSpec extends JsonErrorValidators with UnitSpec {

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
      bandLimit = Some(200),
      apportionedBandLimit = Some(300),
      income = 1000.00,
      amount = 2000.00
    )

  val incomeTaxItem =
    IncomeTaxItem(
      totalAmount = 10000.00,
      band = Seq(incomeTaxBand),
      personalAllowanceUsed = Some(100.45),
      taxableIncome = 3000.00
    )

  val incomeTaxItemWithMoreThanOneBand =
    IncomeTaxItem(
      totalAmount = 10000.00,
      band = Seq(incomeTaxBand, incomeTaxBand.copy(name = "You", income = 2000.00)),
      personalAllowanceUsed = Some(100.45),
      taxableIncome = 3000.00
    )

  val incomeTaxItemWithMoreThanOneBandsJson: JsValue = Json.parse(
    s"""
       |{
       | "totalAmount" : 10000.00,
       |  "band":[
       |  {
       |    "name": "Me",
       |    "rate": 10,
       |    "threshold" : 4,
       |    "apportionedThreshold" : 10,
       |    "bandLimit": 200,
       |    "apportionedBandLimit": 300,
       |    "income" : 1000.00,
       |    "amount" : 2000.00
       |  },
       |  {
       |    "name": "You",
       |    "rate": 10,
       |    "threshold" : 4,
       |    "apportionedThreshold" : 10,
       |    "bandLimit": 200,
       |    "apportionedBandLimit": 300,
       |    "income" : 2000.00,
       |    "amount" : 2000.00
       |  }
       | ],
       | "personalAllowanceUsed" : 100.45,
       | "taxableIncome" : 3000.00
       |}
      """.stripMargin)

  val incomeTaxItemJson: JsValue = Json.parse(
    s"""
       |{
       | "totalAmount" : 10000.00,
       |  "band":[
       |  {
       |    "name": "Me",
       |    "rate": 10,
       |    "threshold" : 4,
       |    "apportionedThreshold" : 10,
       |    "bandLimit": 200,
       |    "apportionedBandLimit": 300,
       |    "income" : 1000.00,
       |    "amount" : 2000.00
       |  }
       | ],
       | "personalAllowanceUsed" : 100.45,
       | "taxableIncome" : 3000.00
       |}
      """.stripMargin)

  val onlyRequiredDataIncomeTaxItemDesJson: String =
    s"""
       |{
       | "totalAmount" : 10000.00,
       |  "band":[
       |  {
       |    "name": "Me",
       |    "rate": 10,
       |    "threshold" : 4,
       |    "apportionedThreshold" : 10,
       |    "bandLimit": 200,
       |    "apportionedBandLimit": 300,
       |    "income" : 1000.00,
       |    "taxAmount" : 2000.00
       |  }
       | ],
       | "taxableIncome" : 3000.00
       |}
      """.stripMargin

  val onlyRequiredDataIncomeTaxItem =
    IncomeTaxItem(
      totalAmount = 10000.00,
      band = Seq(incomeTaxBand),
      personalAllowanceUsed = None,
      taxableIncome = 3000.00
    )

  val onlyRequiredDataIncomeTaxItemJson: JsValue = Json.parse(
    s"""
       |{
       | "totalAmount" : 10000.00,
       |  "band":[
       |  {
       |    "name": "Me",
       |    "rate": 10,
       |    "threshold" : 4,
       |    "apportionedThreshold" : 10,
       |    "bandLimit": 200,
       |    "apportionedBandLimit": 300,
       |    "income" : 1000.00,
       |    "amount" : 2000.00
       |  }
       | ],
       | "taxableIncome" : 3000.00
       |}
      """.stripMargin)

  val incomeTaxItemDesJson: String =
    s"""
       |{
       | "totalAmount" : 10000.00,
       |  "band":[
       |  {
       |    "name": "Me",
       |    "rate": 10,
       |    "threshold" : 4,
       |    "apportionedThreshold" : 10,
       |    "bandLimit": 200,
       |    "apportionedBandLimit": 300,
       |    "income" : 1000.00,
       |    "taxAmount" : 2000.00
       |  }
       | ],
       | "personalAllowanceUsed" : 100.45,
       | "taxableIncome" : 3000.00
       |}
      """.stripMargin

  val incomeTaxItemWithMoreThanOneBandsDesJson: String =
    s"""
       |{
       | "totalAmount" : 10000.00,
       |  "band":[
       |  {
       |    "name": "Me",
       |    "rate": 10,
       |    "threshold" : 4,
       |    "apportionedThreshold" : 10,
       |    "bandLimit": 200,
       |    "apportionedBandLimit": 300,
       |    "income" : 1000.00,
       |    "taxAmount" : 2000.00
       |  },
       |  {
       |    "name": "You",
       |    "rate": 10,
       |    "threshold" : 4,
       |    "apportionedThreshold" : 10,
       |    "bandLimit": 200,
       |    "apportionedBandLimit": 300,
       |    "income" : 2000.00,
       |    "taxAmount" : 2000.00
       |  }
       | ],
       | "personalAllowanceUsed" : 100.45,
       | "taxableIncome" : 3000.00
       |}
      """.stripMargin

  "IncomeTaxItem reads" should {
    "return valid errors for invalid json" when {

      testMandatoryProperty[IncomeTaxItem](incomeTaxItemDesJson)(property = "totalAmount")
      testMandatoryProperty[IncomeTaxItem](incomeTaxItemDesJson)(property = "taxableIncome")
      testMandatoryProperty[IncomeTaxItem](incomeTaxItemDesJson)(property = "band")

      testPropertyType[IncomeTaxItem](incomeTaxItemDesJson)(
        property = "totalAmount",
        invalidValue = "\"nan\"",
        errorPathAndError = ".totalAmount" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[IncomeTaxItem](incomeTaxItemDesJson)(
        property = "taxableIncome",
        invalidValue = "\"nan\"",
        errorPathAndError = ".taxableIncome" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[IncomeTaxItem](incomeTaxItemDesJson)(
        property = "personalAllowanceUsed",
        invalidValue = "\"nan\"",
        errorPathAndError = ".personalAllowanceUsed" -> NUMBER_FORMAT_EXCEPTION
      )
    }
  }

  "reads for IncomeTaxItem" should {

    "successfully parse the JSON" when {
      "all fields are passed" in {
        IncomeTaxItem.reads.reads(Json.parse(incomeTaxItemDesJson)).get shouldBe incomeTaxItem
      }
    }

    "successfully parse the JSON" when {
      "only required fields are passed" in {
        IncomeTaxItem.reads.reads(Json.parse(onlyRequiredDataIncomeTaxItemDesJson)).get shouldBe onlyRequiredDataIncomeTaxItem
      }
    }

    "successfully parse the JSON" when {
      "more than one bands" in {
        IncomeTaxItem.reads.reads(Json.parse(incomeTaxItemWithMoreThanOneBandsDesJson)).get shouldBe incomeTaxItemWithMoreThanOneBand
      }
    }

    "writes for IncomeTaxItem" should {

      "successfully render the correct JSON" when {
        "all fields are available" in {
          IncomeTaxItem.writes.writes(incomeTaxItem) shouldBe incomeTaxItemJson
        }
      }

      "successfully render the correct JSON" when {
        "only required fields are available" in {
          IncomeTaxItem.writes.writes(onlyRequiredDataIncomeTaxItem) shouldBe onlyRequiredDataIncomeTaxItemJson
        }
      }

      "successfully render the correct JSON" when {
        "more than one bands are available" in {
          IncomeTaxItem.writes.writes(incomeTaxItemWithMoreThanOneBand) shouldBe incomeTaxItemWithMoreThanOneBandsJson
        }
      }
    }
  }
}
