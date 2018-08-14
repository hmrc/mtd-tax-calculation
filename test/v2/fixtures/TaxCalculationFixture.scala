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
import v2.models.errors.DesErrorCode.DesErrorCode

object TaxCalculationFixture {

  val taxCalc =
    TaxCalculation(
      year = Some(2016),
      intentToCrystallise = Some(false),
      crystallised = Some(false),
      validationMessageCount = Some(3),
      incomeTaxAndNicYTD = Some(1000.25),
      nationalRegime = Some("UK"),
      taxableIncome = TaxableIncome(
        employments = Some(Employments(
          totalIncome = Some(1000.25),
          totalPay = 1000.25,
          totalBenefitsAndExpenses = 1000.25,
          totalAllowableExpenses = 1000.25,
          employment = Seq(
            Employment(
              employmentId = "ABIS10000000001",
              netPay = 1000.25,
              benefitsAndExpenses = 1000.25,
              allowableExpenses = 1000.25
            )
          )
        )),
        selfEmployments = Some(SelfEmployments(
          totalIncome = Some(1000.25),
          selfEmployment = Some(Seq(
            SelfEmployment(
              selfEmploymentId = "XKIS00000000988",
              taxableIncome = 1000.25,
              finalised = Some(true),
              losses = Some(1000.25)
            )
          ))
        )),
        ukProperty = Some(UKProperty(
          totalIncome = Some(1000.25),
          nonFurnishedHolidayLettingsTaxableProfit = Some(1000.25),
          nonFurnishedHolidayLettingsLoss = Some(1000.25),
          furnishedHolidayLettingsTaxableProfit = Some(1000.25),
          furnishedHolidayLettingsLoss = Some(1000.25),
          finalised = Some(true)
        )),
        ukDividends = Some(UKDividends(
          totalIncome = Some(1000.25)
        )),
        totalIncomeReceived = Some(1000.25),
        allowancesAndDeductions = AllowancesAndDeductions(
          totalAllowancesAndDeductions = 1000.25,
          giftOfInvestmentsAndPropertyToCharity = Some(1000.25),
          apportionedPersonalAllowance = 1000.25
        ),
        totalTaxableIncome = Some(1000.25)
      ),
      incomeTax = IncomeTax(
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
      ),
      nic = Nic(
        totalNic = 1000.25,
        class2 = Some(Class2Nic(
          amount = 1000.25,
          weekRate = 1000.25,
          weeks = 1,
          limit = 99999999,
          apportionedLimit = 2
        )),
        class4 = Some(Class4Nic(
          totalAmount = 1000.25,
          band = Seq(
            NicBand(
              name = "BRT",
              rate = 99.99,
              threshold = Some(99999999),
              apportionedThreshold = Some(99999999),
              income = 1000.25,
              amount = 1000.25
            )
          )
        ))
      ),
      totalBeforeTaxDeducted = Some(1000.25),
      taxDeducted = Some(TaxDeducted(
        ukLandAndProperty = Some(1000.25),
        totalTaxDeducted = Some(1000.25)
      )),
      eoyEstimate = EoyEstimate(
        employments = Some(Seq(
          EoyEmployment(
            employmentId = "ABIS10000000001",
            taxableIncome = 99999999.99,
            supplied = false,
            finalised = Some(false)
          )
        )),
        selfEmployments = Some(Seq(
          EoySelfEmployment(
            selfEmploymentId = "XKIS00000000988",
            taxableIncome = 99999999.99,
            supplied = false,
            finalised = Some(false)
          )
        )),
        ukProperty = Some(EoyItem(
          taxableIncome = 99999999.99,
          supplied = false,
          finalised = Some(false)
        )),
        ukDividends = Some(EoyItem(
          taxableIncome = 99999999.99,
          supplied = false,
          finalised = Some(false)
        )),
        totalTaxableIncome = 99999999.99,
        incomeTaxAmount = 99999999.99,
        nic2 = 99999999,
        nic4 = 99999999,
        totalNicAmount = 99999999.99,
        incomeTaxNicAmount = 99999999.99
      ),
      calculationMessageCount = Some(1),
      calculationMessages = Some(Seq(
        CalculationMessage(
          `type`= "WARN",
          text = "You have entered a large amount in total Gift Aid payments. Please check."
        )
      )),
      annualAllowances = AnnualAllowances(
        personalAllowance = 99999999,
        personalAllowanceThreshold = Some(99999999),
        reducedPersonalAllowance = Some(99999999),
        giftAidExtender = Some(99999999)
      )
    )

  val taxCalcJson: JsValue =
    Json.parse("""
      |{
      | "year": 2016,
      |	"intentToCrystallise": false,
      |	"crystallised": false,
      | "validationMessageCount":3,
      |	"incomeTaxAndNicYTD": 1000.25,
      |	"nationalRegime": "UK",
      |	"taxableIncome": {
      |		"employments": {
      |			"totalIncome": 1000.25,
      |			"totalPay": 1000.25,
      |			"totalBenefitsAndExpenses": 1000.25,
      |			"totalAllowableExpenses": 1000.25,
      |			"employment": [
      |				{
      |					"employmentId": "ABIS10000000001",
      |					"netPay": 1000.25,
      |					"benefitsAndExpenses": 1000.25,
      |					"allowableExpenses": 1000.25
      |				}
      |			]
      |		},
      |		"selfEmployments" : {
      |			"totalIncome": 1000.25,
      |			"selfEmployment": [
      |				{
      |					"selfEmploymentId": "XKIS00000000988",
      |					"taxableIncome": 1000.25,
      |					"finalised": true,
      |					"losses": 1000.25
      |				}
      |			]
      |		},
      |		"ukProperty": {
      |			"totalIncome": 1000.25,
      |			"nonFurnishedHolidayLettingsTaxableProfit": 1000.25,
      |			"nonFurnishedHolidayLettingsLoss": 1000.25,
      |			"furnishedHolidayLettingsTaxableProfit": 1000.25,
      |			"furnishedHolidayLettingsLoss": 1000.25,
      |			"finalised": true
      |		},
      |		"ukDividends": {
      |			"totalIncome": 1000.25
      |		},
      |		"totalIncomeReceived": 1000.25,
      |		"allowancesAndDeductions": {
      |			"totalAllowancesAndDeductions": 1000.25,
      |			"giftOfInvestmentsAndPropertyToCharity": 1000.25,
      |			"apportionedPersonalAllowance": 1000.25
      |		},
      |		"totalTaxableIncome": 1000.25
      |	},
      |	"incomeTax": {
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
      |	},
      |	"nic": {
      |		"totalNic": 1000.25,
      |		"class2": {
      |			"amount": 1000.25,
      |			"weekRate": 1000.25,
      |			"weeks": 1,
      |			"limit": 99999999,
      |			"apportionedLimit": 2
      |		},
      |		"class4": {
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
      |			]
      |		}
      |	},
      |	"totalBeforeTaxDeducted": 1000.25,
      |	"taxDeducted": {
      |		"ukLandAndProperty": 1000.25,
      |		"totalTaxDeducted": 1000.25
      |	},
      |	"eoyEstimate": {
      |		"employments": [
      |			{
      |				"employmentId": "ABIS10000000001",
      |				"taxableIncome": 99999999.99,
      |				"supplied": false,
      |				"finalised": false
      |			}
      |		],
      |		"selfEmployments": [
      |			{
      |				"selfEmploymentId": "XKIS00000000988",
      |				"taxableIncome": 99999999.99,
      |				"supplied": false,
      |				"finalised": false
      |			}
      |		],
      |		"ukProperty": {
      |			"taxableIncome": 99999999.99,
      |			"supplied": false,
      |			"finalised": false
      |		},
      |		"ukDividends" : {
      |			"taxableIncome": 99999999.99,
      |			"supplied": false,
      |			"finalised": false
      |		},
      |		"totalTaxableIncome": 99999999.99,
      |		"incomeTaxAmount": 99999999.99,
      |		"nic2": 99999999,
      |		"nic4": 99999999,
      |		"totalNicAmount": 99999999.99,
      |		"incomeTaxNicAmount": 99999999.99
      |	},
      |	"calculationMessageCount": 1,
      |	"calculationMessages": [
      |		{
      |			"type": "WARN",
      |			"text": "You have entered a large amount in total Gift Aid payments. Please check."
      |		}
      |	],
      |	"annualAllowances": {
      |		"personalAllowance": 99999999,
      |		"personalAllowanceThreshold": 99999999,
      |		"reducedPersonalAllowance": 99999999,
      |		"giftAidExtender": 99999999
      |	}
      |}
    """.stripMargin)

  val desTaxCalcJson: JsValue = Json.parse(
    s"""
      |{
      |  "calcOutput": {
      |    "year": 2016,
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
      |      	  			"incomeSourceID": "ABIS10000000001",
      |      	  			"netPay": 1000.25,
      |      	  			"benefitsAndExpenses": 1000.25,
      |      	  			"allowableExpenses": 1000.25
      |      	  		}
      |      	  	]
      |      	  },
      |      	  "selfEmploymentIncome": 1000.25,
      |      	  "selfEmployment": [
      |      	  	{
      |      	  		"incomeSourceID": "XKIS00000000988",
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
      |      "totalIncomeTax": 1000.25,
      |      "totalNic": 1000.25,
      |      "nic": {
      |      	"class2": {
      |      		"amount": 1000.25,
      |      		"weekRate": 1000.25,
      |      		"weeks": 1,
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
      |      			"id": "XKIS00000000988",
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

  val v3_2ClientTaxCalc = TaxCalculation(
    year = Some(2016),
    intentToCrystallise = None,
    crystallised = Some(true),
    validationMessageCount = None,
    incomeTaxAndNicYTD = Some(1000.25),
    nationalRegime = Some("UK"),
    taxableIncome = TaxableIncome(
      employments = Some(Employments(
        totalIncome = Some(1000.25),
        totalPay = 1000.25,
        totalBenefitsAndExpenses = 1000.25,
        totalAllowableExpenses = 1000.25,
        employment = Seq(
          Employment(
            employmentId = "ABIS10000000001",
            netPay = 1000.25,
            benefitsAndExpenses = 1000.25,
            allowableExpenses = 1000.25
          )
        )
      )),
      selfEmployments = Some(SelfEmployments(
        totalIncome = Some(1000.25),
        selfEmployment = Some(Seq(
          SelfEmployment(
            selfEmploymentId = "XKIS00000000988",
            taxableIncome = 1000.25,
            finalised = Some(true),
            losses = None
          )
        ))
      )),
      ukProperty = Some(UKProperty(
        totalIncome = Some(1000.25),
        nonFurnishedHolidayLettingsTaxableProfit = Some(1000.25),
        nonFurnishedHolidayLettingsLoss = None,
        furnishedHolidayLettingsTaxableProfit = Some(1000.25),
        furnishedHolidayLettingsLoss = None,
        finalised = Some(true)
      )),
      ukDividends = Some(UKDividends(
        totalIncome = Some(1000.25)
      )),
      totalIncomeReceived = Some(1000.25),
      allowancesAndDeductions = AllowancesAndDeductions(
        totalAllowancesAndDeductions = 1000.25,
        giftOfInvestmentsAndPropertyToCharity = Some(1000.25),
        apportionedPersonalAllowance = 1000.25
      ),
      totalTaxableIncome = Some(1000.25)
    ),
    incomeTax = IncomeTax(
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
            name = "HRT",
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
      giftAid = None,
      totalAfterGiftAid = None,
      totalIncomeTax = 1000.25
    ),
    nic = Nic(
      totalNic = 1000.25,
      class2 = Some(Class2Nic(
        amount = 1000.25,
        weekRate = 1000.25,
        weeks = 1,
        limit = 99999999,
        apportionedLimit = 2
      )),
      class4 = Some(Class4Nic(
        totalAmount = 1000.25,
        band = Seq(
          NicBand(
            name = "HRT",
            rate = 99.99,
            threshold = Some(99999999),
            apportionedThreshold = Some(99999999),
            income = 1000.25,
            amount = 1000.25
          )
        )
      ))
    ),
    totalBeforeTaxDeducted = None,
    taxDeducted = None,
    eoyEstimate = EoyEstimate(
      employments = Some(Seq(
        EoyEmployment(
          employmentId = "abcdefghijklm",
          taxableIncome = 99999999.99,
          supplied = true,
          finalised = Some(true)
        )
      )),
      selfEmployments = Some(Seq(
        EoySelfEmployment(
          selfEmploymentId = "abcdefghijklm",
          taxableIncome = 99999999.99,
          supplied = true,
          finalised = Some(true)
        )
      )),
      ukProperty = Some(EoyItem(
        taxableIncome = 99999999.99,
        supplied = true,
        finalised = Some(true)
      )),
      ukDividends = Some(EoyItem(
        taxableIncome = 99999999.99,
        supplied = true,
        finalised = Some(true)
      )),
      totalTaxableIncome = 99999999.99,
      incomeTaxAmount = 99999999.99,
      nic2 = 99999999,
      nic4 = 99999999,
      totalNicAmount = 99999999.99,
      incomeTaxNicAmount = 99999999.99
    ),
    calculationMessageCount = Some(1),
    calculationMessages = Some(Seq(
      CalculationMessage(
        `type`= "WARN",
        text = "You have entered a large amount in total Gift Aid payments. Please check."
      )
    )),
    annualAllowances = AnnualAllowances(
      personalAllowance = 99999999,
      personalAllowanceThreshold = Some(99999999),
      reducedPersonalAllowance = Some(99999999),
      giftAidExtender = None
    )
  )

  val v3_2ClientTaxCalcJson: JsValue = Json.parse(
    """
      |{
      | "year": 2016,
      |	"crystallised": true,
      |	"incomeTaxAndNicYTD": 1000.25,
      |	"nationalRegime": "UK",
      |	"taxableIncome": {
      |		"employments": {
      |			"totalIncome": 1000.25,
      |			"totalPay": 1000.25,
      |			"totalBenefitsAndExpenses": 1000.25,
      |			"totalAllowableExpenses": 1000.25,
      |			"employment": [
      |				{
      |					"employmentId": "ABIS10000000001",
      |					"netPay": 1000.25,
      |					"benefitsAndExpenses": 1000.25,
      |					"allowableExpenses": 1000.25
      |				}
      |			]
      |		},
      |		"selfEmployments" : {
      |			"totalIncome": 1000.25,
      |			"selfEmployment": [
      |				{
      |					"selfEmploymentId": "XKIS00000000988",
      |					"taxableIncome": 1000.25,
      |					"finalised": true
      |				}
      |			]
      |		},
      |		"ukProperty": {
      |			"totalIncome": 1000.25,
      |			"nonFurnishedHolidayLettingsTaxableProfit": 1000.25,
      |			"furnishedHolidayLettingsTaxableProfit": 1000.25,
      |			"finalised": true
      |		},
      |		"ukDividends": {
      |			"totalIncome": 1000.25
      |		},
      |		"totalIncomeReceived": 1000.25,
      |		"allowancesAndDeductions": {
      |			"totalAllowancesAndDeductions": 1000.25,
      |			"giftOfInvestmentsAndPropertyToCharity": 1000.25,
      |			"apportionedPersonalAllowance": 1000.25
      |		},
      |		"totalTaxableIncome": 1000.25
      |	},
      |	"incomeTax": {
      |		"taxableIncome": 1000.25,
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
      |     "personalAllowanceUsed":1000.25,
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
      |     "personalAllowanceUsed":1000.25,
      |			"taxableIncome": 1000.25
      |		},
      |		"dividends": {
      |			"totalAmount": 1000.25,
      |			"band": [
      |				{
      |					"name": "HRT",
      |					"rate": 99.99,
      |					"threshold": 99999999,
      |					"apportionedThreshold": 99999999,
      |					"income": 1000.25,
      |					"amount": 1000.25
      |				}
      |			],
      |     "personalAllowanceUsed":1000.25,
      |			"taxableIncome": 1000.25
      |		},
      |		"totalBeforeReliefs": 1000.25,
      |		"allowancesAndReliefs": {
      |			"propertyFinanceRelief": 1000.25,
      |			"totalAllowancesAndReliefs": 1000.25
      |		},
      |  "totalAfterReliefs":1000.25,
      |		"totalIncomeTax": 1000.25
      |	},
      |	"nic": {
      |		"totalNic": 1000.25,
      |		"class2": {
      |			"amount": 1000.25,
      |			"weekRate": 1000.25,
      |			"weeks": 1,
      |			"limit": 99999999,
      |			"apportionedLimit": 2
      |		},
      |		"class4": {
      |			"totalAmount": 1000.25,
      |			"band": [
      |				{
      |					"name": "HRT",
      |					"rate": 99.99,
      |					"threshold": 99999999,
      |					"apportionedThreshold": 99999999,
      |					"income": 1000.25,
      |					"amount": 1000.25
      |				}
      |			]
      |		}
      |	},
      |	"eoyEstimate": {
      |		"employments": [
      |			{
      |				"employmentId": "abcdefghijklm",
      |				"taxableIncome": 99999999.99,
      |				"supplied": true,
      |				"finalised": true
      |			}
      |		],
      |		"selfEmployments": [
      |			{
      |				"selfEmploymentId": "abcdefghijklm",
      |				"taxableIncome": 99999999.99,
      |				"supplied": true,
      |				"finalised": true
      |			}
      |		],
      |		"ukProperty": {
      |			"taxableIncome": 99999999.99,
      |			"supplied": true,
      |			"finalised": true
      |		},
      |		"ukDividends" : {
      |			"taxableIncome": 99999999.99,
      |			"supplied": true,
      |			"finalised": true
      |		},
      |		"totalTaxableIncome": 99999999.99,
      |		"incomeTaxAmount": 99999999.99,
      |		"nic2": 99999999,
      |		"nic4": 99999999,
      |		"totalNicAmount": 99999999.99,
      |		"incomeTaxNicAmount": 99999999.99
      |	},
      |	"calculationMessageCount": 1,
      |	"calculationMessages": [
      |		{
      |			"type": "WARN",
      |			"text": "You have entered a large amount in total Gift Aid payments. Please check."
      |		}
      |	],
      |	"annualAllowances": {
      |		"personalAllowance": 99999999,
      |		"personalAllowanceThreshold": 99999999,
      |		"reducedPersonalAllowance": 99999999
      |	}
      |}
""".stripMargin)

  val v3_2DesTaxCalcJson: JsValue = Json.parse(
    """
      |{
      |	"calcOutput": {
      |		"calcName": "IncomeTaxCalculator",
      |		"calcVersion": "Version1a",
      |		"calcVersionDate": "2016-01-01",
      |		"calcID": "12345678",
      |		"sourceName": "MDTP",
      |		"sourceRef": "ACKREF0001",
      |		"identifier": "AB10001A",
      |		"year": 2016,
      |		"periodFrom": "2016-01-01",
      |		"periodTo": "2016-01-01",
      |		"calcAmount": 1000.25,
      |		"calcTimestamp": "4498-07-06T21:42:24.294Z",
      |		"calcResult": {
      |			"incomeTaxNicYtd": 1000.25,
      |			"incomeTaxNicDelta": 1000.25,
      |			"crystallised": true,
      |			"nationalRegime": "UK",
      |			"totalTaxableIncome": 1000.25,
      |			"taxableIncome": {
      |				"totalIncomeReceived": 1000.25,
      |				"incomeReceived": {
      |					"employmentIncome": 1000.25,
      |					"employments": {
      |						"totalPay": 1000.25,
      |						"totalBenefitsAndExpenses": 1000.25,
      |						"totalAllowableExpenses": 1000.25,
      |						"employment": [
      |					    {
      |								"incomeSourceID": "ABIS10000000001",
      |								"latestDate": "2016-01-01",
      |								"netPay": 1000.25,
      |								"benefitsAndExpenses": 1000.25,
      |								"allowableExpenses": 1000.25
      |							}
      |						]
      |					},
      |					"shareSchemeIncome": 1000.25,
      |					"shareSchemes": {
      |						"incomeSourceID": "ABIS10000000001",
      |						"latestDate": "2016-01-01"
      |					},
      |					"selfEmploymentIncome": 1000.25,
      |					"selfEmployment": [
      |						{
      |							"incomeSourceID": "XKIS00000000988",
      |							"latestDate": "2016-01-01",
      |							"accountStartDate": "2016-01-01",
      |							"accountEndDate": "2016-01-01",
      |							"taxableIncome": 1000.25,
      |							"finalised": true
      |						}
      |					],
      |					"partnershipIncome": 1000.25,
      |					"partnership": [
      |						{
      |							"incomeSourceID": "abcdefghijklm",
      |							"latestDate": "2016-01-01",
      |							"taxableIncome": 1000.25
      |						}
      |					],
      |					"ukPropertyIncome": 1000.25,
      |					"ukProperty": {
      |						"incomeSourceID": "ABIS10000000001",
      |						"latestDate": "2016-01-01",
      |						"taxableProfit": 1000.25,
      |						"taxableProfitFhlUk": 1000.25,
      |						"taxableProfitFhlEea": 1000.25,
      |						"finalised": true
      |					},
      |					"foreignIncome": 1000.25,
      |					"foreign": {
      |						"incomeSourceID": "ABIS10000000001",
      |						"latestDate": "2016-01-01"
      |					},
      |					"foreignDividendIncome": 1000.25,
      |					"foreignDividends": {
      |						"incomeSourceID": "ABIS10000000001",
      |						"latestDate": "2016-01-01"
      |					},
      |					"trustsIncome": 1000.25,
      |					"trusts": {
      |						"incomeSourceID": "ABIS10000000001",
      |						"latestDate": "2016-01-01"
      |					},
      |					"bbsiIncome": 1000.25,
      |					"bbsi": {
      |						"incomeSourceID": "ABIS10000000001",
      |						"latestDate": "2016-01-01",
      |						"interestReceived": 1000.25
      |					},
      |					"ukDividendsIncome": 1000.25,
      |					"ukDividends": {
      |						"incomeSourceID": "ABIS10000000001",
      |						"latestDate": "2016-01-01"
      |					},
      |					"ukPensionsIncome": 1000.25,
      |					"ukPensions": {
      |						"incomeSourceID": "ABIS10000000001",
      |						"latestDate": "2016-01-01"
      |					},
      |					"gainsOnLifeInsuranceIncome": 1000.25,
      |					"gainsOnLifeInsurance": {
      |						"incomeSourceID": "ABIS10000000001",
      |						"latestDate": "2016-01-01"
      |					},
      |					"otherIncome": 1000.25
      |				},
      |				"totalAllowancesAndDeductions": 1000.25,
      |				"allowancesAndDeductions": {
      |					"paymentsIntoARetirementAnnuity": 1000.25,
      |					"foreignTaxOnEstates": 1000.25,
      |					"incomeTaxRelief": 1000.25,
      |					"annuities": 1000.25,
      |					"giftOfInvestmentsAndPropertyToCharity": 1000.25,
      |					"apportionedPersonalAllowance": 1000.25,
      |					"marriageAllowanceTransfer": 1000.25,
      |					"blindPersonAllowance": 1000.25,
      |					"blindPersonSurplusAllowanceFromSpouse": 1000.25,
      |					"incomeExcluded": 1000.25
      |				}
      |			},
      |			"totalIncomeTax": 1000.25,
      |			"incomeTax": {
      |				"totalBeforeReliefs": 1000.25,
      |       "totalAfterReliefs": 1000.25,
      |				"taxableIncome": 1000.25,
      |				"payPensionsProfit": {
      |					"totalAmount": 1000.25,
      |					"taxableIncome": 1000.25,
      |					"band": [
      |						{
      |							"name": "ZRT",
      |							"rate": 99.99,
      |							"threshold": 99999999,
      |							"apportionedThreshold": 99999999,
      |							"income": 1000.25,
      |							"taxAmount": 1000.25
      |						}
      |					],
      |         "personalAllowanceUsed":1000.25
      |				},
      |				"savingsAndGains": {
      |					"totalAmount": 1000.25,
      |					"taxableIncome": 1000.25,
      |					"band": [
      |						{
      |							"name": "BRT",
      |							"rate": 99.99,
      |							"threshold": 99999999,
      |							"apportionedThreshold": 99999999,
      |							"income": 1000.25,
      |							"taxAmount": 1000.25
      |						}
      |					],
      |         "personalAllowanceUsed":1000.25
      |				},
      |				"dividends": {
      |					"totalAmount": 1000.25,
      |					"taxableIncome": 1000.25,
      |					"band": [
      |						{
      |							"name": "HRT",
      |							"rate": 99.99,
      |							"threshold": 99999999,
      |							"apportionedThreshold": 99999999,
      |							"income": 1000.25,
      |							"taxAmount": 1000.25
      |						}
      |					],
      |         "personalAllowanceUsed":1000.25
      |				},
      |				"excludedIncome": 1000.25,
      |				"totalAllowancesAndReliefs": 1000.25,
      |				"allowancesAndReliefs": {
      |					"deficiencyRelief": 1000.25,
      |					"topSlicingRelief": 1000.25,
      |					"ventureCapitalTrustRelief": 1000.25,
      |					"enterpriseInvestmentSchemeRelief": 1000.25,
      |					"seedEnterpriseInvestmentSchemeRelief": 1000.25,
      |					"communityInvestmentTaxRelief": 1000.25,
      |					"socialInvestmentTaxRelief": 1000.25,
      |					"maintenanceAndAlimonyPaid": 1000.25,
      |					"marriedCoupleAllowanceRate": 1000.25,
      |					"marriedCoupleAllowanceAmount": 1000.25,
      |					"marriedCoupleAllowanceRelief": 1000.25,
      |					"surplusMarriedCoupleAllowanceAmount": 1000.25,
      |					"surplusMarriedCoupleAllowanceRelief": 1000.25,
      |					"notionalTaxFromLifePolicies": 1000.25,
      |					"notionalTaxFromDividendsAndOtherIncome": 1000.25,
      |					"foreignTaxCreditRelief": 1000.25,
      |					"propertyFinanceRelief": 1000.25
      |				}
      |			},
      |			"totalNic": 1000.25,
      |			"nic": {
      |				"class2": {
      |					"amount": 1000.25,
      |					"weekRate": 1000.25,
      |					"weeks": 1,
      |					"limit": 99999999,
      |					"apportionedLimit": 2
      |				},
      |				"class4": {
      |					"totalAmount": 1000.25,
      |					"band": [{
      |							"name": "HRT",
      |							"rate": 99.99,
      |							"threshold": 99999999,
      |							"apportionedThreshold": 99999999,
      |							"income": 1000.25,
      |							"amount": 1000.25
      |						}
      |					]
      |				}
      |			},
      |			"eoyEstimate": {
      |				"incomeSource": [
      |					{
      |						"id": "abcdefghijklm",
      |						"type": "01",
      |						"taxableIncome": 99999999.99,
      |						"supplied": true,
      |						"finalised": true
      |					},
      |         {
      |						"id": "abcdefghijklm",
      |						"type": "05",
      |						"taxableIncome": 99999999.99,
      |						"supplied": true,
      |						"finalised": true
      |					},
      |         {
      |						"type": "02",
      |						"taxableIncome": 99999999.99,
      |						"supplied": true,
      |						"finalised": true
      |					},
      |         {
      |						"type": "10",
      |						"taxableIncome": 99999999.99,
      |						"supplied": true,
      |						"finalised": true
      |					}
      |				],
      |				"totalTaxableIncome": 99999999.99,
      |				"incomeTaxAmount": 99999999.99,
      |				"nic2": 99999999,
      |				"nic4": 99999999,
      |				"totalNicAmount": 99999999.99,
      |				"incomeTaxNicAmount": 99999999.99
      |			},
      |			"msgCount": 1,
      |			"msg": [
      |				{
      |					"type": "WARN",
      |					"text": "You have entered a large amount in total Gift Aid payments. Please check."
      |				}
      |			],
      |			"previousCalc": {
      |				"calcTimestamp": "4498-07-06T21:42:24.294Z",
      |				"calcID": "00000000",
      |				"calcAmount": 1000.25
      |			},
      |			"annualAllowances": {
      |				"personalAllowance": 99999999,
      |				"reducedPersonalAllowanceThreshold": 99999999,
      |				"reducedPersonalisedAllowance": 99999999
      |			}
      |		}
      |	}
      |}
    """.stripMargin)

  def v3_2DesTaxCalcErrorJson(errors: (DesErrorCode, String)*): JsValue = {
    if(errors.size > 1) {
      Json.obj("failures" -> Json.arr(errors.map {
        case (code, reason) => Json.obj("code" -> code, "reason" -> reason)
      }))
    } else {
      val (code, reason) = errors.head
      Json.obj("code" -> code, "reason" -> reason)
    }
  }

  val selfEmploymentUkPropertyJson = Json.parse(
    """
      |{
      |  "calcOutput": {
      |    "calcName": "IncomeTaxCalculator",
      |    "calcVersion": "2.0.6",
      |    "calcVersionDate": "2017-12-05",
      |    "calcID": "95816347",
      |    "sourceName": "Income Store",
      |    "sourceRef": "7",
      |    "identifier": "NG940613D",
      |    "year": 2018,
      |    "periodFrom": "2017-01-01",
      |    "periodTo": "2018-04-05",
      |    "calcAmount": 25462.8,
      |    "calcTimestamp": "2017-12-05T18:01:15.385Z",
      |    "calcResult": {
      |      "incomeTaxNicYtd": 25462.8,
      |      "incomeTaxNicDelta": 0,
      |      "crystallised": false,
      |      "nationalRegime": "UK",
      |      "totalTaxableIncome": 71136,
      |      "taxableIncome": {
      |        "totalIncomeReceived": 82636,
      |        "incomeReceived": {
      |          "selfEmploymentIncome": 57248,
      |          "selfEmployment": [
      |            {
      |              "incomeSourceID": "XCIS00000000697",
      |              "latestDate": "2017-12-31",
      |              "accountStartDate": "2017-01-01",
      |              "accountEndDate": "2017-12-31",
      |              "taxableIncome": 57248
      |            }
      |          ],
      |          "ukPropertyIncome": 25388,
      |          "ukProperty": {
      |            "incomeSourceID": "XDIS00000000698",
      |            "latestDate": "2018-04-05"
      |          }
      |        },
      |        "totalAllowancesAndDeductions": 11500,
      |        "allowancesAndDeductions": {
      |          "apportionedPersonalAllowance": 11500
      |        }
      |      },
      |      "totalIncomeTax": 21754.4,
      |      "incomeTax": {
      |        "totalBeforeReliefs": 21754.4,
      |        "totalAfterReliefs": 1000.25,
      |        "taxableIncome": 71136,
      |        "payPensionsProfit": {
      |          "totalAmount": 21754.4,
      |          "taxableIncome": 82636,
      |          "band": [
      |            {
      |              "name": "BRT",
      |              "rate": 20,
      |              "threshold": 45000,
      |              "apportionedThreshold": 45000,
      |              "income": 33500,
      |              "taxAmount": 6700
      |            },
      |            {
      |              "name": "HRT",
      |              "rate": 40,
      |              "threshold": 150000,
      |              "apportionedThreshold": 150000,
      |              "income": 37636,
      |              "taxAmount": 15054.4
      |            }
      |          ]
      |        },
      |        "totalAllowancesAndReliefs": 0
      |      },
      |      "totalNic": 3708.4,
      |      "nic": {
      |        "class2": {
      |          "amount": 148.2,
      |          "weekRate": 2.85,
      |          "weeks": 52,
      |          "limit": 6025,
      |          "apportionedLimit": 6025
      |        },
      |        "class4": {
      |          "totalAmount": 3560.2,
      |          "band": [
      |            {
      |              "name": "ZRT",
      |              "rate": 0,
      |              "threshold": 8164,
      |              "apportionedThreshold": 8164,
      |              "income": 8164,
      |              "amount": 0
      |            },
      |            {
      |              "name": "BRT",
      |              "rate": 9,
      |              "threshold": 45000,
      |              "apportionedThreshold": 45000,
      |              "income": 36836,
      |              "amount": 3315.24
      |            },
      |            {
      |              "name": "HRT",
      |              "rate": 2,
      |              "threshold": 99999999,
      |              "apportionedThreshold": 99999999,
      |              "income": 12248,
      |              "amount": 244.96
      |            }
      |          ]
      |        }
      |      },
      |      "eoyEstimate": {
      |        "incomeSource": [
      |          {
      |            "id": "XCIS00000000697",
      |            "type": "01",
      |            "taxableIncome": 57248,
      |            "supplied": false
      |          },
      |          {
      |            "id": "XDIS00000000698",
      |            "type": "02",
      |            "taxableIncome": 25388,
      |            "supplied": false
      |          }
      |        ],
      |        "totalTaxableIncome": 82636,
      |        "incomeTaxAmount": 21754.4,
      |        "nic2": 148.2,
      |        "nic4": 3560.2,
      |        "totalNicAmount": 3708.4,
      |        "incomeTaxNicAmount": 25462.8
      |      },
      |      "msgCount": 0,
      |      "previousCalc": {
      |        "calcTimestamp": "2017-12-04T21:50:07.233Z",
      |        "calcID": "69774672",
      |        "calcAmount": 25462.8
      |      },
      |      "annualAllowances": {
      |        "personalAllowance": 11500,
      |        "reducedPersonalAllowanceThreshold": 100000
      |      }
      |    }
      |  }
      |}
    """.stripMargin
  )

  val selfEmploymentPeriodicJson = Json.parse(
    """
      |{
      |    "calcOutput": {
      |        "calcName": "IncomeTaxCalculator",
      |        "calcVersion": "2.0.11",
      |        "calcVersionDate": "2018-03-20",
      |        "calcID": "55093485",
      |        "sourceName": "Income Store",
      |        "sourceRef": "2",
      |        "identifier": "NG442614D",
      |        "year": 2018,
      |        "periodFrom": "2017-04-06",
      |        "periodTo": "2017-07-05",
      |        "calcAmount": 51.81,
      |        "calcTimestamp": "2018-04-17T21:03:09.559Z",
      |        "calcResult": {
      |            "incomeTaxNicYtd": 51.81,
      |            "incomeTaxNicDelta": 0,
      |            "crystallised": false,
      |            "nationalRegime": "UK",
      |            "totalTaxableIncome": 0,
      |            "taxableIncome": {
      |                "totalIncomeReceived": 2200,
      |                "incomeReceived": {
      |                    "selfEmploymentIncome": 2200,
      |                    "selfEmployment": [{
      |                        "incomeSourceID": "XEIS00000000434",
      |                        "latestDate": "2017-07-05",
      |                        "accountStartDate": "2017-04-06",
      |                        "accountEndDate": "2018-04-05",
      |                        "taxableIncome": 2200.25
      |                    }]
      |                },
      |                "totalAllowancesAndDeductions": 2868,
      |                "allowancesAndDeductions": {
      |                    "apportionedPersonalAllowance": 2868
      |                }
      |            },
      |            "totalIncomeTax": 0,
      |            "incomeTax": {
      |                "totalBeforeReliefs": 0,
      |                "totalAfterReliefs": 0,
      |                "taxableIncome": 0,
      |                "totalAllowancesAndReliefs": 0
      |            },
      |            "totalNic": 51.81,
      |            "nic": {
      |                "class2": {
      |                    "amount": 37.05,
      |                    "weekRate": 2.85,
      |                    "weeks": 13,
      |                    "limit": 6025,
      |                    "apportionedLimit": 1503
      |                },
      |                "class4": {
      |                    "totalAmount": 14.76,
      |                    "band": [{
      |                        "name": "ZRT",
      |                        "rate": 0,
      |                        "threshold": 8164,
      |                        "apportionedThreshold": 2036,
      |                        "income": 2036,
      |                        "amount": 0
      |                    }, {
      |                        "name": "BRT",
      |                        "rate": 9,
      |                        "threshold": 45000,
      |                        "apportionedThreshold": 11220,
      |                        "income": 164,
      |                        "amount": 14.76
      |                    }]
      |                }
      |            },
      |            "eoyEstimate": {
      |                "incomeSource": [{
      |                    "id": "XEIS00000000434",
      |                    "type": "01",
      |                    "taxableIncome": 8825,
      |                    "supplied": false
      |                }],
      |                "totalTaxableIncome": 8825,
      |                "incomeTaxAmount": 0,
      |                "nic2": 148.2,
      |                "nic4": 59.49,
      |                "totalNicAmount": 207.69,
      |                "incomeTaxNicAmount": 207.69
      |            },
      |            "msgCount": 0,
      |            "previousCalc": {
      |                "calcTimestamp": "2018-03-26T09:41:41.356Z",
      |                "calcID": "94768929",
      |                "calcAmount": 51.81
      |            },
      |            "annualAllowances": {
      |                "personalAllowance": 11500,
      |                "reducedPersonalAllowanceThreshold": 100000
      |            }
      |        }
      |    }
      |}
    """.stripMargin
  )

  val ukPropertyOtherPeriodicAndAnnualJson = Json.parse(
    """
      |{
      |    "calcOutput": {
      |        "calcName": "IncomeTaxCalculator",
      |        "calcVersion": "2.0.6",
      |        "calcVersionDate": "2017-12-05",
      |        "calcID": "99169867",
      |        "sourceName": "Income Store",
      |        "sourceRef": "4",
      |        "identifier": "NG543013D",
      |        "year": 2018,
      |        "periodFrom": "2017-04-06",
      |        "periodTo": "2018-04-05",
      |        "calcAmount": 1561.8,
      |        "calcTimestamp": "2017-12-05T15:37:54.687Z",
      |        "calcResult": {
      |            "incomeTaxNicYtd": 1561.8,
      |            "incomeTaxNicDelta": 0,
      |            "crystallised": false,
      |            "nationalRegime": "UK",
      |            "totalTaxableIncome": 7809,
      |            "taxableIncome": {
      |                "totalIncomeReceived": 19309,
      |                "incomeReceived": {
      |                    "ukPropertyIncome": 19309,
      |                    "ukProperty": {
      |                        "incomeSourceID": "XHIS00000000437",
      |                        "latestDate": "2018-04-05"
      |                    }
      |                },
      |                "totalAllowancesAndDeductions": 11500,
      |                "allowancesAndDeductions": {
      |                    "apportionedPersonalAllowance": 11500
      |                }
      |            },
      |            "totalIncomeTax": 1561.8,
      |            "incomeTax": {
      |                "totalBeforeReliefs": 1561.8,
      |                "totalAfterReliefs": 1000.25,
      |                "taxableIncome": 7809,
      |                "payPensionsProfit": {
      |                    "totalAmount": 1561.8,
      |                    "taxableIncome": 19309,
      |                    "band": [{
      |                        "name": "BRT",
      |                        "rate": 20,
      |                        "threshold": 45000,
      |                        "apportionedThreshold": 45000,
      |                        "income": 7809,
      |                        "taxAmount": 1561.8
      |                    }]
      |                },
      |                "totalAllowancesAndReliefs": 0
      |            },
      |            "totalNic": 0,
      |            "eoyEstimate": {
      |                "incomeSource": [{
      |                    "id": "XHIS00000000437",
      |                    "type": "02",
      |                    "taxableIncome": 19309,
      |                    "supplied": false
      |                }],
      |                "totalTaxableIncome": 19309,
      |                "incomeTaxAmount": 1561.8,
      |                "nic2": 0,
      |                "nic4": 0,
      |                "totalNicAmount": 0,
      |                "incomeTaxNicAmount": 1561.8
      |            },
      |            "msgCount": 0,
      |            "previousCalc": {
      |                "calcTimestamp": "2017-12-01T12:34:43.123Z",
      |                "calcID": "74968682",
      |                "calcAmount": 1561.8
      |            },
      |            "annualAllowances": {
      |                "personalAllowance": 11500,
      |                "reducedPersonalAllowanceThreshold": 100000
      |            }
      |        }
      |    }
      |}
    """.stripMargin
  )

  val selfEmploymentPeriodicAndAnnualsJson = Json.parse(
    """
      |{
      |    "calcOutput": {
      |        "calcName": "IncomeTaxCalculator",
      |        "calcVersion": "2.0.11",
      |        "calcVersionDate": "2018-03-20",
      |        "calcID": "68275501",
      |        "sourceName": "Income Store",
      |        "sourceRef": "2",
      |        "identifier": "HW392714B",
      |        "year": 2018,
      |        "periodFrom": "2017-04-06",
      |        "periodTo": "2018-04-05",
      |        "calcAmount": 10432809.19,
      |        "calcTimestamp": "2018-04-17T21:06:58.662Z",
      |        "calcResult": {
      |            "incomeTaxNicYtd": 10432809.19,
      |            "incomeTaxNicDelta": 0,
      |            "crystallised": false,
      |            "nationalRegime": "UK",
      |            "totalTaxableIncome": 22222225,
      |            "taxableIncome": {
      |                "totalIncomeReceived": 22222225,
      |                "incomeReceived": {
      |                    "selfEmploymentIncome": 22222225,
      |                    "selfEmployment": [{
      |                        "incomeSourceID": "XMIS00000000717",
      |                        "latestDate": "2018-04-05",
      |                        "accountStartDate": "2017-04-06",
      |                        "accountEndDate": "2018-04-05",
      |                        "taxableIncome": 22222225.75
      |                    }]
      |                },
      |                "totalAllowancesAndDeductions": 0,
      |                "allowancesAndDeductions": {
      |                    "apportionedPersonalAllowance": 0
      |                }
      |            },
      |            "totalIncomeTax": 9985801.25,
      |            "incomeTax": {
      |                "totalBeforeReliefs": 9985801.25,
      |                "totalAfterReliefs": 1000.25,
      |                "taxableIncome": 22222225,
      |                "payPensionsProfit": {
      |                    "totalAmount": 9985801.25,
      |                    "taxableIncome": 22222225,
      |                    "band": [{
      |                            "name": "BRT",
      |                            "rate": 20,
      |                            "threshold": 33500,
      |                            "apportionedThreshold": 33500,
      |                            "income": 33500,
      |                            "taxAmount": 6700
      |                        },
      |                        {
      |                            "name": "HRT",
      |                            "rate": 40,
      |                            "threshold": 150000,
      |                            "apportionedThreshold": 150000,
      |                            "income": 116500,
      |                            "taxAmount": 46600
      |                        },
      |                        {
      |                            "name": "ART",
      |                            "rate": 45,
      |                            "threshold": 99999999,
      |                            "apportionedThreshold": 99999999,
      |                            "income": 22072225,
      |                            "taxAmount": 9932501.25
      |                        }
      |                    ]
      |                },
      |                "totalAllowancesAndReliefs": 0
      |            },
      |            "totalNic": 447007.94,
      |            "nic": {
      |                "class2": {
      |                    "amount": 148.2,
      |                    "weekRate": 2.85,
      |                    "weeks": 52,
      |                    "limit": 6025,
      |                    "apportionedLimit": 6025
      |                },
      |                "class4": {
      |                    "totalAmount": 446859.74,
      |                    "band": [{
      |                            "name": "ZRT",
      |                            "rate": 0,
      |                            "threshold": 8164,
      |                            "apportionedThreshold": 8164,
      |                            "income": 8164,
      |                            "amount": 0
      |                        },
      |                        {
      |                            "name": "BRT",
      |                            "rate": 9,
      |                            "threshold": 45000,
      |                            "apportionedThreshold": 45000,
      |                            "income": 36836,
      |                            "amount": 3315.24
      |                        },
      |                        {
      |                            "name": "HRT",
      |                            "rate": 2,
      |                            "threshold": 99999999,
      |                            "apportionedThreshold": 99999999,
      |                            "income": 22177225,
      |                            "amount": 443544.5
      |                        }
      |                    ]
      |                }
      |            },
      |            "eoyEstimate": {
      |                "incomeSource": [{
      |                    "id": "XMIS00000000717",
      |                    "type": "01",
      |                    "taxableIncome": 22222225,
      |                    "supplied": false
      |                }],
      |                "totalTaxableIncome": 22222225,
      |                "incomeTaxAmount": 9985801.25,
      |                "nic2": 148.2,
      |                "nic4": 446859.74,
      |                "totalNicAmount": 447007.94,
      |                "incomeTaxNicAmount": 10432809.19
      |            },
      |            "msgCount": 0,
      |            "previousCalc": {
      |                "calcTimestamp": "2017-12-04T13:46:21.839Z",
      |                "calcID": "37598845",
      |                "calcAmount": 10432809.19
      |            },
      |            "annualAllowances": {
      |                "personalAllowance": 11500,
      |                "reducedPersonalAllowanceThreshold": 100000,
      |                "reducedPersonalisedAllowance": 0
      |            }
      |        }
      |    }
      |}
    """.stripMargin
  )

  val ukPropertyFhlPeriodicAndAnnualsJson = Json.parse(
    """
      |{
      |    "calcOutput": {
      |        "calcName": "IncomeTaxCalculator",
      |        "calcVersion": "2.0.7",
      |        "calcVersionDate": "2017-12-07",
      |        "calcID": "03530242",
      |        "sourceName": "Income Store",
      |        "sourceRef": "2",
      |        "identifier": "NG899113D",
      |        "year": 2018,
      |        "periodFrom": "2017-04-06",
      |        "periodTo": "2018-04-05",
      |        "calcAmount": 3809.6,
      |        "calcTimestamp": "2017-12-19T13:19:35.953Z",
      |        "calcResult": {
      |            "incomeTaxNicYtd": 3809.6,
      |            "incomeTaxNicDelta": 0,
      |            "crystallised": false,
      |            "nationalRegime": "UK",
      |            "totalTaxableIncome": 19048,
      |            "taxableIncome": {
      |                "totalIncomeReceived": 30548,
      |                "incomeReceived": {
      |                    "ukPropertyIncome": 30548,
      |                    "ukProperty": {
      |                        "incomeSourceID": "XQIS00000000494",
      |                        "latestDate": "2018-04-05"
      |                    }
      |                },
      |                "totalAllowancesAndDeductions": 11500,
      |                "allowancesAndDeductions": {
      |                    "apportionedPersonalAllowance": 11500
      |                }
      |            },
      |            "totalIncomeTax": 3809.6,
      |            "incomeTax": {
      |                "totalBeforeReliefs": 3809.6,
      |                "totalAfterReliefs": 1000.25,
      |                "taxableIncome": 19048,
      |                "payPensionsProfit": {
      |                    "totalAmount": 3809.6,
      |                    "taxableIncome": 30548,
      |                    "band": [{
      |                        "name": "BRT",
      |                        "rate": 20,
      |                        "threshold": 45000,
      |                        "apportionedThreshold": 45000,
      |                        "income": 19048,
      |                        "taxAmount": 3809.6
      |                    }]
      |                },
      |                "totalAllowancesAndReliefs": 0
      |            },
      |            "totalNic": 0,
      |            "eoyEstimate": {
      |                "incomeSource": [{
      |                    "id": "XQIS00000000494",
      |                    "type": "02",
      |                    "taxableIncome": 30548,
      |                    "supplied": false
      |                }],
      |                "totalTaxableIncome": 30548,
      |                "incomeTaxAmount": 3809.6,
      |                "nic2": 0,
      |                "nic4": 0,
      |                "totalNicAmount": 0,
      |                "incomeTaxNicAmount": 3809.6
      |            },
      |            "msgCount": 0,
      |            "previousCalc": {
      |                "calcTimestamp": "2017-12-05T17:25:18.995Z",
      |                "calcID": "31461216",
      |                "calcAmount": 3809.6
      |            },
      |            "annualAllowances": {
      |                "personalAllowance": 11500,
      |                "reducedPersonalAllowanceThreshold": 100000
      |            }
      |        }
      |    }
      |}
    """.stripMargin
  )

  val selfEmploymentScottishMultipleIncomeSourcesJson = Json.parse(
    """
      |{
      |    "calcOutput": {
      |        "calcName": "IncomeTaxCalculator",
      |        "calcVersion": "2.0.7",
      |        "calcVersionDate": "2017-12-07",
      |        "calcID": "03442299",
      |        "sourceName": "Income Store",
      |        "sourceRef": "3",
      |        "identifier": "AA204513C",
      |        "year": 2018,
      |        "periodFrom": "2017-01-01",
      |        "periodTo": "2017-12-31",
      |        "calcAmount": 26861.8,
      |        "calcTimestamp": "2017-12-19T13:28:23.567Z",
      |        "calcResult": {
      |            "incomeTaxNicYtd": 26861.8,
      |            "incomeTaxNicDelta": 0,
      |            "crystallised": false,
      |            "nationalRegime": "Scotland",
      |            "totalTaxableIncome": 82749,
      |            "taxableIncome": {
      |                "totalIncomeReceived": 94249,
      |                "incomeReceived": {
      |                    "selfEmploymentIncome": 94249,
      |                    "selfEmployment": [{
      |                        "incomeSourceID": "XEIS00000000701",
      |                        "latestDate": "2017-12-31",
      |                        "accountStartDate": "2017-01-01",
      |                        "accountEndDate": "2017-12-31",
      |                        "taxableIncome": 69050.82
      |                    }, {
      |                        "incomeSourceID": "XFIS00000000702",
      |                        "latestDate": "2017-12-31",
      |                        "accountStartDate": "2017-01-01",
      |                        "accountEndDate": "2017-12-31",
      |                        "taxableIncome": 25198.9
      |                    }]
      |                },
      |                "totalAllowancesAndDeductions": 11500,
      |                "allowancesAndDeductions": {
      |                    "apportionedPersonalAllowance": 11500
      |                }
      |            },
      |            "totalIncomeTax": 26713.6,
      |            "incomeTax": {
      |                "totalBeforeReliefs": 26713.6,
      |                "totalAfterReliefs": 1000.25,
      |                "taxableIncome": 82749,
      |                "payPensionsProfit": {
      |                    "totalAmount": 26713.6,
      |                    "taxableIncome": 94249,
      |                    "band": [{
      |                        "name": "BRT",
      |                        "rate": 20,
      |                        "threshold": 43430,
      |                        "apportionedThreshold": 43430,
      |                        "income": 31930,
      |                        "taxAmount": 6386
      |                    }, {
      |                        "name": "HRT",
      |                        "rate": 40,
      |                        "threshold": 150000,
      |                        "apportionedThreshold": 150000,
      |                        "income": 50819,
      |                        "taxAmount": 20327.6
      |                    }]
      |                },
      |                "totalAllowancesAndReliefs": 0
      |            },
      |            "totalNic": 148.2,
      |            "nic": {
      |                "class2": {
      |                    "amount": 148.2,
      |                    "weekRate": 2.85,
      |                    "weeks": 52,
      |                    "limit": 6025,
      |                    "apportionedLimit": 6025
      |                }
      |            },
      |            "eoyEstimate": {
      |                "incomeSource": [{
      |                    "id": "XFIS00000000702",
      |                    "type": "01",
      |                    "taxableIncome": 25198,
      |                    "supplied": false
      |                }, {
      |                    "id": "XEIS00000000701",
      |                    "type": "01",
      |                    "taxableIncome": 69050,
      |                    "supplied": false
      |                }],
      |                "totalTaxableIncome": 94249,
      |                "incomeTaxAmount": 26713.6,
      |                "nic2": 148.2,
      |                "nic4": 0,
      |                "totalNicAmount": 148.2,
      |                "incomeTaxNicAmount": 26861.8
      |            },
      |            "msgCount": 0,
      |            "previousCalc": {
      |                "calcTimestamp": "2017-12-05T19:04:03.689Z",
      |                "calcID": "35512366",
      |                "calcAmount": 26861.8
      |            },
      |            "annualAllowances": {
      |                "personalAllowance": 11500,
      |                "reducedPersonalAllowanceThreshold": 100000
      |            }
      |        }
      |    }
      |}
    """.stripMargin
  )
}
