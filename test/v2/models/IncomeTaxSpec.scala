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

import play.api.libs.json.{JsValue, Json}
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class IncomeTaxSpec extends JsonErrorValidators with UnitSpec {

  import JsonError._

  val threshold = 4
  val apportionedThreshold = 10
  val rate = 10

  val incomeTax =
    IncomeTax(
      taxableIncome = 1000.25,
      payAndPensionsProfit = Some(IncomeTaxItem(
        totalAmount = 1000.25,
        band = Seq(
          IncomeTaxBand(
            name = "ZRT",
            rate = 99.99,
            threshold = Some(99999999),
            apportionedThreshold = Some(99999999),
            income = 1000.25,
            amount = 1000.25
          )
        ),
        personalAllowanceUsed = Some(1000.25),
        taxableIncome = 1000.25
      )),
      savingsAndGains = Some(IncomeTaxItem(
        totalAmount = 1000.25,
        band = Seq(
          IncomeTaxBand(
            name = "BRT",
            rate = 99.99,
            threshold = Some(99999999),
            apportionedThreshold = Some(99999999),
            income = 1000.25,
            amount = 1000.25
          )
        ),
        personalAllowanceUsed = Some(1000.25),
        taxableIncome = 1000.25
      )),
      dividends = Some(IncomeTaxItem(
        totalAmount = 1000.25,
        band = Seq(
          IncomeTaxBand(
            name = "BRT",
            rate = 99.99,
            threshold = Some(99999999),
            apportionedThreshold = Some(99999999),
            income = 1000.25,
            amount = 1000.25
          )
        ),
        personalAllowanceUsed = Some(1000.25),
        taxableIncome = 1000.25
      )),
      totalBeforeReliefs = 1000.25,
      allowancesAndReliefs = Some(AllowancesAndReliefs(
        propertyFinanceRelief = Some(1000.25),
        totalAllowancesAndReliefs = 1000.25
      )),
      totalAfterReliefs = 1000.25,
      giftAid = Some(GiftAid(
        paymentsMade = 1000.25,
        rate = 99.99,
        taxableAmount = 1000.25
      )),
      totalAfterGiftAid = Some(1000.25),
      totalIncomeTax = 1000.25
    )

  val incomeTaxJson: JsValue = Json.parse(
    s"""
       |{
       |   "taxableIncome": 1000.25,
       |		"payAndPensionsProfit": {
       |			"totalAmount": 1000.25,
       |			"band": [
       |				{
       |					"name": "ZRT",
       |					"rate": 99.99,
       |					"threshold": 99999999,
       |					"apportionedThreshold": 99999999,
       |					"income": 1000.25,
       |					"amount": 1000.25
       |				}
       |			],
       |			"personalAllowanceUsed": 1000.25,
       |			"taxableIncome": 1000.25
       |		},
       |		"savingsAndGains": {
       |			"totalAmount": 1000.25,
       |			"band": [
       |				{
       |					"name": "BRT",
       |					"rate": 99.99,
       |					"threshold": 99999999,
       |					"apportionedThreshold": 99999999,
       |					"income": 1000.25,
       |					"amount": 1000.25
       |				}
       |			],
       |			"personalAllowanceUsed": 1000.25,
       |			"taxableIncome": 1000.25
       |		},
       |		"dividends": {
       |			"totalAmount": 1000.25,
       |			"band": [
       |				{
       |					"name": "BRT",
       |					"rate": 99.99,
       |					"threshold": 99999999,
       |					"apportionedThreshold": 99999999,
       |					"income": 1000.25,
       |					"amount": 1000.25
       |				}
       |			],
       |			"personalAllowanceUsed": 1000.25,
       |			"taxableIncome": 1000.25
       |		},
       |		"totalBeforeReliefs": 1000.25,
       |		"allowancesAndReliefs": {
       |			"propertyFinanceRelief": 1000.25,
       |			"totalAllowancesAndReliefs": 1000.25
       |		},
       |		"totalAfterReliefs": 1000.25,
       |		"giftAid": {
       |			"paymentsMade": 1000.25,
       |			"rate": 99.99,
       |			"taxableAmount": 1000.25
       |		},
       |		"totalAfterGiftAid": 1000.25,
       |		"totalIncomeTax": 1000.25
       |	}
      """.stripMargin)

  val incomeTaxDesJson: String =
    s"""
       |{
       |   "incomeTax": {
       |   "taxableIncome": 1000.25,
       |      	"payPensionsProfit": {
       |      		"totalAmount": 1000.25,
       |      		"band": [
       |      			{
       |      				"name": "ZRT",
       |      				"rate": 99.99,
       |      				"threshold": 99999999,
       |      				"apportionedThreshold": 99999999,
       |      				"income": 1000.25,
       |      				"taxAmount": 1000.25
       |      			}
       |      		],
       |      		"personalAllowanceUsed": 1000.25,
       |      		"taxableIncome": 1000.25
       |      	},
       |      	"savingsAndGains": {
       |      		"totalAmount": 1000.25,
       |      		"band": [
       |      			{
       |      				"name": "BRT",
       |      				"rate": 99.99,
       |      				"threshold": 99999999,
       |      				"apportionedThreshold": 99999999,
       |      				"income": 1000.25,
       |      				"taxAmount": 1000.25
       |      			}
       |      		],
       |      		"personalAllowanceUsed": 1000.25,
       |      		"taxableIncome": 1000.25
       |      	},
       |      	"dividends": {
       |      		"totalAmount": 1000.25,
       |      		"band": [
       |      			{
       |      				"name": "BRT",
       |      				"rate": 99.99,
       |      				"threshold": 99999999,
       |      				"apportionedThreshold": 99999999,
       |      				"income": 1000.25,
       |      				"taxAmount": 1000.25
       |      			}
       |      		],
       |      		"personalAllowanceUsed": 1000.25,
       |      		"taxableIncome": 1000.25
       |      	},
       |      	"totalBeforeReliefs": 1000.25,
       |      	"allowancesAndReliefs": {
       |      		"propertyFinanceRelief": 1000.25
       |      	},
       |       "totalAllowancesAndReliefs": 1000.25,
       |      	"totalAfterReliefs": 1000.25,
       |      	"giftAid": {
       |      		"paymentsMade": 1000.25,
       |      		"rate": 99.99,
       |      		"taxableAmount": 1000.25
       |      	},
       |      	"totalAfterGiftAid": 1000.25
       |      },
       |      "totalIncomeTax": 1000.25
       |  }
      """.stripMargin


  "IncomeTax reads" should {
    "return valid errors for invalid json" when {

      testMandatoryProperty[IncomeTax](incomeTaxDesJson)(property = "taxableIncome")
      testMandatoryProperty[IncomeTax](incomeTaxDesJson)(property = "totalBeforeReliefs")
      testMandatoryProperty[IncomeTax](incomeTaxDesJson)(property = "totalAfterReliefs")
      testMandatoryProperty[IncomeTax](incomeTaxDesJson)(property = "totalIncomeTax")

      testPropertyType[IncomeTax](incomeTaxDesJson)(
        property = "taxableIncome",
        invalidValue = "\"nan\"",
        errorPathAndError = ".taxableIncome" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[IncomeTax](incomeTaxDesJson)(
        property = "totalBeforeReliefs",
        invalidValue = "\"nan\"",
        errorPathAndError = ".totalBeforeReliefs" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[IncomeTax](incomeTaxDesJson)(
        property = "totalAfterReliefs",
        invalidValue = "\"nan\"",
        errorPathAndError = ".totalAfterReliefs" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[IncomeTax](incomeTaxDesJson)(
        property = "totalIncomeTax",
        invalidValue = "\"nan\"",
        errorPathAndError = ".totalIncomeTax" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[IncomeTax](incomeTaxDesJson)(
        property = "totalAfterGiftAid",
        invalidValue = "\"nan\"",
        errorPathAndError = ".totalAfterGiftAid" -> NUMBER_FORMAT_EXCEPTION
      )
    }
  }

  "reads for IncomeTax" should {

    "successfully parse the JSON" when {
      "all fields are passed" in {
        IncomeTax.reads.reads(Json.parse(incomeTaxDesJson)).get shouldBe incomeTax
      }
    }
  }

  "writes for IncomeTax" should {

    "successfully render the correct JSON" when {
      "all fields are available" in {
        IncomeTax.writes.writes(incomeTax) shouldBe incomeTaxJson
      }
    }

  }
}
