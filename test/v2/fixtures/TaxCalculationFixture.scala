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

package v2.fixtures

import play.api.libs.json.{JsValue, Json}
import v2.models._

object TaxCalculationFixture {

  val testTaxCalc =
    TaxCalculation(
      year = "2016-17",
      intentToCrystallise = Some(false),
      crystallised = Some(false),
      validationMessageCount = Some(3),
      incomeTaxAndNicYTD = Some(1000.25),
      nationalRegime = Some("UK"),
      taxableIncome = TaxableIncome(
        employments = Employments(
          totalIncome = Some(1000.25),
          totalPay = Some(1000.25),
          totalBenefitsAndExpenses = Some(1000.25),
          totalAllowableExpenses = Some(1000.25),
          employment = Seq(
            Employment(
              employmentId = Some("ABIS10000000001"),
              netPay = Some(1000.25),
              benefitsAndExpenses = Some(1000.25),
              allowableExpenses = Some(1000.25)
            )
          )
        ),
        selfEmployments = SelfEmployments(
          totalIncome = Some(1000.25),
          selfEmployment = Seq(
            SelfEmployment(
              selfEmploymentId = "XKIS00000000988",
              taxableIncome = Some(1000.25),
              finalised = Some(true),
              losses = Some(1000.25)
            )
          )
        ),
        ukProperty = UKProperty(
          totalIncome = Some(1000.25),
          nonFurnishedHolidayLettingsTaxableProfit = Some(1000.25),
          nonFurnishedHolidayLettingsLoss = Some(1000.25),
          furnishedHolidayLettingsTaxableProfit = Some(1000.25),
          furnishedHolidayLettingsLoss = Some(1000.25),
          finalised = Some(true)
        ),
        ukDividends = UKDividends(
          totalIncome = Some(1000.25)
        ),
        totalIncomeReceived = Some(1000.25),
        allowancesAndDeductions = AllowancesAndDeductions(
          totalAllowancesAndDeductions = Some(1000.25),
          giftOfInvestmentsAndPropertyToCharity = Some(1000.25),
          apportionedPersonalAllowance = Some(1000.25)
        ),
        totalTaxableIncome = Some(1000.25)
      ),
      incomeTax = IncomeTax(
        payPensionsProfit = IncomeTaxItem(
          totalAmount = Some(1000.25),
          band = Seq(
            Band(
              name = "ZRT",
              rate = 99.99,
              threshold = 99999999,
              apportionedThreshold = 99999999,
              income = 1000.25,
              amount = 1000.25
            )
          ),
          personalAllowanceUsed = Some(1000.25),
          taxableIncome = Some(1000.25)
        ),
        savingsAndGains = IncomeTaxItem(
          totalAmount = Some(1000.25),
          band = Seq(
            Band(
              name = "BRT",
              rate = 99.99,
              threshold = 99999999,
              apportionedThreshold = 99999999,
              income = 1000.25,
              amount = 1000.25
            )
          ),
          personalAllowanceUsed = Some(1000.25),
          taxableIncome = Some(1000.25)
        ),
        dividends = IncomeTaxItem(
          totalAmount = Some(1000.25),
          band = Seq(
            Band(
              name = "BRT",
              rate = 99.99,
              threshold = 99999999,
              apportionedThreshold = 99999999,
              income = 1000.25,
              amount = 1000.25
            )
          ),
          personalAllowanceUsed = Some(1000.25),
          taxableIncome = Some(1000.25)
        ),
        totalBeforeReliefs = Some(1000.25),
        allowancesAndReliefs = AllowancesAndReliefs(
          propertyFinanceRelief = Some(1000.25),
          totalAllowancesAndReliefs = Some(1000.25)
        ),
        totalAfterReliefs = Some(1000.25),
        giftAid = GiftAid(
          paymentsMade = Some(1000.25),
          rate = 99.99,
          taxableAmount = Some(1000.25)
        ),
        totalAfterGiftAid = Some(1000.25),
        totalIncomeTax = Some(1000.25)
      ),
      nic = Nic(
        totalNic = Some(1000.25),
        class2 = Class2Nic(
          amount = Some(1000.25),
          weekRate = Some(1000.25),
          weeks = Some(1.1),
          limit = Some(99999999),
          apportionedLimit = Some(2)
        ),
        class4 = Class4Nic(
          totalAmount = Some(1000.25),
          band = Seq(
            Band(
              name = "BRT",
              rate = 99.99,
              threshold = 99999999,
              apportionedThreshold = 99999999,
              income = 1000.25,
              amount = 1000.25
            )
          )
        )
      ),
      totalBeforeTaxDeducted = Some(1000.25),
      taxDeducted = TaxDeducted(
        ukLandAndProperty = Some(1000.25),
        totalTaxDeducted = Some(1000.25)
      ),
      eoyEstimate = EoyEstimate(
        employments = Seq(
          EoyEmployment(
            employmentId = Some("ABIS10000000001"),
            taxableIncome = Some(99999999.99),
            supplied = Some(false),
            finalised = Some(false)
          )
        ),
        selfEmployments = Seq(
          EoySelfEmployment(
            selfEmploymentId = "ABIS10000000001",
            taxableIncome = Some(99999999.99),
            supplied = Some(false),
            finalised = Some(false)
          )
        ),
        ukProperty = EoyItem(
          taxableIncome = Some(99999999.99),
          supplied = Some(false),
          finalised = Some(false)
        ),
        ukDividends = EoyItem(
          taxableIncome = Some(99999999.99),
          supplied = Some(false),
          finalised = Some(false)
        ),
        totalTaxableIncome = Some(99999999.99),
        incomeTaxAmount = Some(99999999.99),
        nic2 = Some(99999999),
        nic4 = Some(99999999),
        totalNicAmount = Some(99999999.99),
        incomeTaxAndNicAmount = Some(99999999.99)
      ),
      calculationMessageCount = Some(1),
      calculationMessages = Seq(
        CalculationMessage(
          `type`= "warning",
          text = Some("abcdefghijklm")
        )
      ),
      annualAllowances = AnnualAllowances(
        personalAllowance = Some(99999999),
        personalAllowanceThreshold = Some(99999999),
        reducedPersonalisedAllowance = Some(99999999),
        giftAidExtender = Some(99999999)
      )
    )

  val testTaxCalcJson: JsValue = Json.parse(
    """
      |{
      |  "calcOutput": {
      |    "year": "2016-17",
      |	   "intentToCrystallise": false,
      |	   "crystallised": false,
      |    "bvrErrors": 1,
      |    "bvrWarnings": 2,
      |    "calcResult":{
      |      "totalBeforeTaxDeducted": 951.75,
      |      "validationMessageCount":10,
      |	     "incomeTaxNicYtd": 1000.25,
      |      "nationalRegime": "UK",
      |      "taxableIncome": {
      |        "incomeReceived": {
      |         "employmentIncome":1000.25,
      |      	  "employments": {
      |      	  	"totalIncome": 1000.25,
      |      	  	"totalPay": 1000.25,
      |      	  	"totalBenefitsAndExpenses": 1000.25,
      |      	  	"totalAllowableExpenses": 1000.25,
      |      	  	"employment": [
      |      	  		{
      |      	  			"incomeSourceId": "ABIS10000000001",
      |      	  			"netPay": 1000.25,
      |      	  			"benefitsAndExpenses": 1000.25,
      |      	  			"allowableExpenses": 1000.25
      |      	  		}
      |      	  	]
      |      	  },
      |      	  "selfEmploymentIncome": 1000.25,
      |      	  "selfEmployment": [
      |      	  	{
      |      	  		"incomeSourceId": "XKIS00000000988",
      |      	  		"taxableIncome": 1000.25,
      |      	  		"finalised": true,
      |      	  		"losses": 1000.25
      |      	  	}
      |      	  ],
      |         "ukPropertyIncome": 1000.25,
      |      	  "ukProperty": {
      |      	  	"taxableProfit": 1000.25,
      |      	  	"losses": 1000.25,
      |      	  	"taxableProfitFhlUk": 1000.25,
      |      	  	"lossesFhlUk": 1000.25,
      |      	  	"finalised": true
      |      	  },
      |      	  "ukDividendsIncome": 1000.25
      |        },
      |      	"totalIncomeReceived": 1000.25,
      |       "totalAllowancesAndDeductions": 1000.25,
      |      	"allowancesAndDeductions": {
      |      		"giftOfInvestmentsAndPropertyToCharity": 1000.25,
      |      		"apportionedPersonalAllowance": 1000.25
      |      	}
      |      },
      |      "totalTaxableIncome": 1000.25,
      |      "incomeTax": {
      |      	"payAndPensionsProfit": {
      |      		"totalAmount": 1000.25,
      |      		"band": [
      |      			{
      |      				"name": "ZRT",
      |      				"rate": 99.99,
      |      				"threshold": 99999999,
      |      				"apportionedThreshold": 99999999,
      |      				"income": 1000.25,
      |      				"amount": 1000.25
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
      |      				"amount": 1000.25
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
      |      				"amount": 1000.25
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
      |      "totalIncomeTax": 1000.25,
      |      "totalNic": 1000.25,
      |      "nic": {
      |      	"class2": {
      |      		"amount": 1000.25,
      |      		"weekRate": 1000.25,
      |      		"weeks": 1.1,
      |      		"limit": 99999999,
      |      		"apportionedLimit": 2
      |      	},
      |      	"class4": {
      |      		"totalAmount": 1000.25,
      |      		"band": [
      |      			{
      |      				"name": "BRT",
      |      				"rate": 99.99,
      |      				"threshold": 99999999,
      |      				"apportionedThreshold": 99999999,
      |      				"income": 1000.25,
      |      				"amount": 1000.25
      |      			}
      |      		]
      |      	}
      |      },
      |      "totalBeforeTaxDeducted": 1000.25,
      |      "taxDeducted": {
      |      	"ukLandAndProperty": 1000.25
      |      },
      |      "totalTaxDeducted": 1000.25,
      |      "eoyEstimate": {
      |      	"incomeSource": [
      |      		{
      |      			"id": "ABIS10000000001",
      |      			"taxableIncome": 99999999.99,
      |      			"supplied": false,
      |      			"finalised": false,
      |            "type":"05"
      |      		},
      |          {
      |      			"id": "ABIS10000000001",
      |      			"taxableIncome": 99999999.99,
      |      			"supplied": false,
      |      			"finalised": false,
      |            "type":"01"
      |      		},
      |          {
      |      			"taxableIncome": 99999999.99,
      |      			"supplied": false,
      |      			"finalised": false,
      |            "type":"02"
      |      		},
      |          {
      |      			"taxableIncome": 99999999.99,
      |      			"supplied": false,
      |      			"finalised": false,
      |            "type":"10"
      |      		}
      |      	],
      |      	"totalTaxableIncome": 99999999.99,
      |      	"incomeTaxAmount": 99999999.99,
      |      	"nic2": 99999999,
      |      	"nic4": 99999999,
      |      	"totalNicAmount": 99999999.99,
      |      	"incomeTaxNicAmount": 99999999.99
      |      },
      |      "msgCount": 1,
      |      "msg": [
      |      	{
      |      		"type": "warning",
      |      		"text": "abcdefghijklm"
      |      	}
      |      ],
      |      "annualAllowances": {
      |      	"personalAllowance": 99999999,
      |      	"reducedPersonalAllowanceThreshold": 99999999,
      |      	"reducedPersonalAllowance": 99999999,
      |      	"giftAidExtender": 99999999
      |      }
      |    }
      |  }
      |}
    """.stripMargin)
}
