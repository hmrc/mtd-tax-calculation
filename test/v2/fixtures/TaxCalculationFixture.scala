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

package v2.fixtures

import play.api.libs.json.{JsValue, Json}
import v2.models._

object TaxCalculationFixture {

  val taxCalc = TaxCalculation(
    year = Some(2016),
    intentToCrystallise = true,
    crystallised = true,
    validationMessageCount = 1,
    incomeTaxAndNicYTD = Some(1000.25),
    nationalRegime = Some("UK"),
    taxableIncome = Some(TaxableIncome(
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
        totalIncome = Some(1000.25),
        ukDividends = Some(1000.25),
        otherUkDividends = Some(1000.25)
      )),
      savings = Some(SavingsIncome(
        totalIncome = Some(1000.25),
        totalTaxedInterestIncome = Some(1000.25),
        totalUntaxedInterestIncome = Some(1000.25),
        taxedAccounts = Some(Seq(TaxedSavingsAccount(
          savingsAccountId = "ABIS10000000001",
          name=  Some("accountName"),
          gross = Some(1000.25),
          net = Some(1000.25),
          taxDeducted = Some(1000.25)
        ))),
        untaxedAccounts = Some(Seq(UntaxedSavingsAccount(
          savingsAccountId = "ABIS10000000001",
          name=  Some("accountName"),
          gross = Some(1000.25)
        ))
      ))),
      totalIncomeReceived = 1000.25,
      allowancesAndDeductions = AllowancesAndDeductions(
        totalAllowancesAndDeductions = 1000.25,
            giftOfInvestmentsAndPropertyToCharity = Some(1000.25),
        apportionedPersonalAllowance = 1000.25
      ),
      totalTaxableIncome = Some(1000.25)
    )),
    incomeTax = Some(IncomeTax(
      taxableIncome = 1000.25,
      payAndPensionsProfit = Some(IncomeTaxItem(
        totalAmount = 1000.25,
        band = Seq(
          IncomeTaxBand(
            name = "ZRT",
            rate = 99.99,
            threshold = Some(99999999999L),
            apportionedThreshold = Some(99999999999L),
            bandLimit = Some(1000),
            apportionedBandLimit = Some(1000),
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
            threshold = Some(99999999999L),
            apportionedThreshold = Some(99999999999L),
            bandLimit = Some(1000),
            apportionedBandLimit = Some(1000),
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
            threshold = Some(99999999999L),
            apportionedThreshold = Some(99999999999L),
            bandLimit = Some(1000),
            apportionedBandLimit = Some(1000),
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
      totalIncomeTax = 1000.25,
      residentialFinanceCosts = Some(ResidentialFinanceCosts(
        amountClaimed = 1000.25,
        allowableAmount = Some(1000.25),
        rate = 10.25
      ))
    )),
    nic = Some(Nic(
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
    )),
    totalBeforeTaxDeducted = Some(1000.25),
    taxDeducted = Some(TaxDeducted(Some(1000.25), Some(1000.25), Some(1000.25))),
    eoyEstimate = Some(EoyEstimate(
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
      savings = Some(Seq(EoySavings(
        savingsAccountId = "Some ID",
        taxableIncome = 99999999.99,
        supplied = true
      ))),
      totalTaxableIncome = 99999999.99,
      incomeTaxAmount = 99999999.99,
      nic2 = 99999999,
      nic4 = 99999999,
      totalNicAmount = 99999999.99,
      incomeTaxNicAmount = 99999999.99
    )),
    calculationMessageCount = Some(1),
    calculationMessages = Some(Seq(
      CalculationMessage(
        `type`= "warning",
        text = "You have entered a large amount in total Gift Aid payments. Please check."
      )
    )),
    annualAllowances = Some(AnnualAllowances(
      personalAllowance = 99999999,
      personalAllowanceThreshold = Some(99999999),
      reducedPersonalAllowance = Some(99999999),
      giftAidExtender = None
    ))
  )

  val taxCalcClientJson: JsValue = Json.parse(
    """{
      |  "year": 2016,
      |  "intentToCrystallise": true,
      |  "crystallised": true,
      |  "validationMessageCount": 1,
      |  "incomeTaxAndNicYTD": 1000.25,
      |  "totalBeforeTaxDeducted": 1000.25,
      |  "nationalRegime": "UK",
      |  "taxableIncome": {
      |    "employments": {
      |      "totalIncome": 1000.25,
      |      "totalPay": 1000.25,
      |      "totalBenefitsAndExpenses": 1000.25,
      |      "totalAllowableExpenses": 1000.25,
      |      "employment": [
      |        {
      |          "employmentId": "ABIS10000000001",
      |          "netPay": 1000.25,
      |          "benefitsAndExpenses": 1000.25,
      |          "allowableExpenses": 1000.25
      |        }
      |      ]
      |    },
      |    "selfEmployments": {
      |      "totalIncome": 1000.25,
      |      "selfEmployment": [
      |        {
      |          "selfEmploymentId": "XKIS00000000988",
      |          "taxableIncome": 1000.25,
      |          "finalised": true
      |        }
      |      ]
      |    },
      |    "ukProperty": {
      |      "totalIncome": 1000.25,
      |      "nonFurnishedHolidayLettingsTaxableProfit": 1000.25,
      |      "furnishedHolidayLettingsTaxableProfit": 1000.25,
      |      "finalised": true
      |    },
      |    "ukDividends": {
      |      "totalIncome": 1000.25,
      |      "ukDividends": 1000.25,
      |      "otherUkDividends": 1000.25
      |    },
      |    "savings": {
      |      "totalIncome": 1000.25,
      |      "totalTaxedInterestIncome": 1000.25,
      |      "totalUntaxedInterestIncome": 1000.25,
      |      "taxedAccounts": [
      |        {
      |          "gross": 1000.25,
      |          "savingsAccountId": "ABIS10000000001",
      |          "name": "accountName",
      |          "net": 1000.25,
      |          "taxDeducted": 1000.25
      |        }
      |      ],
      |      "untaxedAccounts": [
      |        {
      |          "gross": 1000.25,
      |          "savingsAccountId": "ABIS10000000001",
      |          "name": "accountName"
      |        }
      |      ]
      |    },
      |    "totalIncomeReceived": 1000.25,
      |    "allowancesAndDeductions": {
      |      "totalAllowancesAndDeductions": 1000.25,
      |      "giftOfInvestmentsAndPropertyToCharity": 1000.25,
      |      "apportionedPersonalAllowance": 1000.25
      |    },
      |    "totalTaxableIncome": 1000.25
      |  },
      |  "incomeTax": {
      |    "taxableIncome": 1000.25,
      |    "payAndPensionsProfit": {
      |      "totalAmount": 1000.25,
      |      "band": [
      |        {
      |          "name": "ZRT",
      |          "rate": 99.99,
      |          "threshold": 99999999999,
      |          "apportionedThreshold": 99999999999,
      |          "bandLimit": 1000,
      |          "apportionedBandLimit": 1000,
      |          "income": 1000.25,
      |          "amount": 1000.25
      |        }
      |      ],
      |      "personalAllowanceUsed": 1000.25,
      |      "taxableIncome": 1000.25
      |    },
      |    "savingsAndGains": {
      |      "totalAmount": 1000.25,
      |      "band": [
      |        {
      |          "name": "BRT",
      |          "rate": 99.99,
      |          "threshold": 99999999999,
      |          "apportionedThreshold": 99999999999,
      |          "bandLimit": 1000,
      |          "apportionedBandLimit": 1000,
      |          "income": 1000.25,
      |          "amount": 1000.25
      |        }
      |      ],
      |      "personalAllowanceUsed": 1000.25,
      |      "taxableIncome": 1000.25
      |    },
      |    "dividends": {
      |      "totalAmount": 1000.25,
      |      "band": [
      |        {
      |          "name": "HRT",
      |          "rate": 99.99,
      |          "threshold": 99999999999,
      |          "apportionedThreshold": 99999999999,
      |          "bandLimit": 1000,
      |          "apportionedBandLimit": 1000,
      |          "income": 1000.25,
      |          "amount": 1000.25
      |        }
      |      ],
      |      "personalAllowanceUsed": 1000.25,
      |      "taxableIncome": 1000.25
      |    },
      |    "totalBeforeReliefs": 1000.25,
      |    "allowancesAndReliefs": {
      |      "propertyFinanceRelief": 1000.25,
      |      "totalAllowancesAndReliefs": 1000.25
      |    },
      |    "totalAfterReliefs": 1000.25,
      |    "totalIncomeTax": 1000.25,
      |    "residentialFinanceCosts": {
      |      "amountClaimed": 1000.25,
      |      "allowableAmount": 1000.25,
      |      "rate": 10.25
      |    }
      |  },
      |  "nic": {
      |    "totalNic": 1000.25,
      |    "class2": {
      |      "amount": 1000.25,
      |      "weekRate": 1000.25,
      |      "weeks": 1,
      |      "limit": 99999999,
      |      "apportionedLimit": 2
      |    },
      |    "class4": {
      |      "totalAmount": 1000.25,
      |      "band": [
      |        {
      |          "name": "HRT",
      |          "rate": 99.99,
      |          "threshold": 99999999,
      |          "apportionedThreshold": 99999999,
      |          "income": 1000.25,
      |          "amount": 1000.25
      |        }
      |      ]
      |    }
      |  },
      |  "taxDeducted": {
      |    "totalTaxDeducted": 1000.25,
      |    "ukLandAndProperty": 1000.25,
      |    "savings": 1000.25
      |  },
      |  "eoyEstimate": {
      |    "employments": [
      |      {
      |        "employmentId": "abcdefghijklm",
      |        "taxableIncome": 99999999.99,
      |        "supplied": true,
      |        "finalised": true
      |      }
      |    ],
      |    "selfEmployments": [
      |      {
      |        "selfEmploymentId": "abcdefghijklm",
      |        "taxableIncome": 99999999.99,
      |        "supplied": true,
      |        "finalised": true
      |      }
      |    ],
      |    "ukProperty": {
      |      "taxableIncome": 99999999.99,
      |      "supplied": true,
      |      "finalised": true
      |    },
      |    "ukDividends": {
      |      "taxableIncome": 99999999.99,
      |      "supplied": true,
      |      "finalised": true
      |    },
      |    "savings": [{
      |      "savingsAccountId": "Some ID",
      |      "taxableIncome": 99999999.99,
      |      "supplied": true
      |    }],
      |    "totalTaxableIncome": 99999999.99,
      |    "incomeTaxAmount": 99999999.99,
      |    "nic2": 99999999,
      |    "nic4": 99999999,
      |    "totalNicAmount": 99999999.99,
      |    "incomeTaxNicAmount": 99999999.99
      |  },
      |  "calculationMessageCount": 1,
      |  "calculationMessages": [
      |    {
      |      "type": "warning",
      |      "text": "You have entered a large amount in total Gift Aid payments. Please check."
      |    }
      |  ],
      |  "annualAllowances": {
      |    "personalAllowance": 99999999,
      |    "personalAllowanceThreshold": 99999999,
      |    "reducedPersonalAllowance": 99999999
      |  }
      |}
    """.stripMargin)

  val taxCalcDesJson: JsValue = Json.parse(
    """{
      |  "calcOutput": {
      |    "bvrErrors": 0,
      |    "bvrWarnings": 1,
      |    "intentToCrystallise": true,
      |    "calcName": "IncomeTaxCalculator",
      |    "calcVersion": "Version1a",
      |    "calcVersionDate": "2016-01-01",
      |    "calcID": "12345678",
      |    "sourceName": "MDTP",
      |    "sourceRef": "ACKREF0001",
      |    "identifier": "AB10001A",
      |    "year": 2016,
      |    "periodFrom": "2016-01-01",
      |    "periodTo": "2016-01-01",
      |    "calcAmount": 1000.25,
      |    "calcTimestamp": "4498-07-06T21:42:24.294Z",
      |    "calcResult": {
      |      "crystallised": true,
      |      "incomeTaxNicYtd": 1000.25,
      |      "incomeTaxNicDelta": 1000.25,
      |      "nationalRegime": "UK",
      |      "totalTaxableIncome": 1000.25,
      |      "taxableIncome": {
      |        "totalIncomeReceived": 1000.25,
      |        "incomeReceived": {
      |          "employmentIncome": 1000.25,
      |          "employments": {
      |            "totalPay": 1000.25,
      |            "totalBenefitsAndExpenses": 1000.25,
      |            "totalAllowableExpenses": 1000.25,
      |            "employment": [
      |              {
      |                "incomeSourceID": "ABIS10000000001",
      |                "latestDate": "2016-01-01",
      |                "netPay": 1000.25,
      |                "benefitsAndExpenses": 1000.25,
      |                "allowableExpenses": 1000.25
      |              }
      |            ]
      |          },
      |          "shareSchemeIncome": 1000.25,
      |          "shareSchemes": {
      |            "incomeSourceID": "ABIS10000000001",
      |            "latestDate": "2016-01-01"
      |          },
      |          "selfEmploymentIncome": 1000.25,
      |          "selfEmployment": [
      |            {
      |              "incomeSourceID": "XKIS00000000988",
      |              "latestDate": "2016-01-01",
      |              "accountStartDate": "2016-01-01",
      |              "accountEndDate": "2016-01-01",
      |              "taxableIncome": 1000.25,
      |              "finalised": true
      |            }
      |          ],
      |          "partnershipIncome": 1000.25,
      |          "partnership": [
      |            {
      |              "incomeSourceID": "abcdefghijklm",
      |              "latestDate": "2016-01-01",
      |              "taxableIncome": 1000.25
      |            }
      |          ],
      |          "ukPropertyIncome": 1000.25,
      |          "ukProperty": {
      |            "incomeSourceID": "ABIS10000000001",
      |            "latestDate": "2016-01-01",
      |            "taxableProfit": 1000.25,
      |            "taxableProfitFhlUk": 1000.25,
      |            "taxableProfitFhlEea": 1000.25,
      |            "finalised": true
      |          },
      |          "foreignIncome": 1000.25,
      |          "foreign": {
      |            "incomeSourceID": "ABIS10000000001",
      |            "latestDate": "2016-01-01"
      |          },
      |          "foreignDividendIncome": 1000.25,
      |          "foreignDividends": {
      |            "incomeSourceID": "ABIS10000000001",
      |            "latestDate": "2016-01-01"
      |          },
      |          "trustsIncome": 1000.25,
      |          "trusts": {
      |            "incomeSourceID": "ABIS10000000001",
      |            "latestDate": "2016-01-01"
      |          },
      |          "bbsiIncome": 1000.25,
      |          "bbsi": {
      |            "totalTaxedInterestIncome": 1000.25,
      |            "totalUntaxedInterestIncome": 1000.25,
      |            "taxedAccounts": [
      |              {
      |                "gross": 1000.25,
      |                "incomeSourceID": "ABIS10000000001",
      |                "name": "accountName",
      |                "net": 1000.25,
      |                "taxDeducted": 1000.25,
      |                "totalTaxedIncome": 1000.25,
      |                "totalUntaxedIncome": 1000.25
      |              }
      |            ],
      |            "untaxedAccounts": [
      |              {
      |                "gross": 1000.25,
      |                "incomeSourceID": "ABIS10000000001",
      |                "name": "accountName"
      |              }
      |            ]
      |          },
      |          "ukDividendIncome": 1000.25,
      |          "ukDividend": {
      |            "ukDividends": 1000.25,
      |            "otherUkDividends": 1000.25
      |          },
      |          "ukPensionsIncome": 1000.25,
      |          "ukPensions": {
      |            "incomeSourceID": "ABIS10000000001",
      |            "latestDate": "2016-01-01"
      |          },
      |          "gainsOnLifeInsuranceIncome": 1000.25,
      |          "gainsOnLifeInsurance": {
      |            "incomeSourceID": "ABIS10000000001",
      |            "latestDate": "2016-01-01"
      |          },
      |          "otherIncome": 1000.25
      |        },
      |        "totalAllowancesAndDeductions": 1000.25,
      |        "allowancesAndDeductions": {
      |          "paymentsIntoARetirementAnnuity": 1000.25,
      |          "foreignTaxOnEstates": 1000.25,
      |          "incomeTaxRelief": 1000.25,
      |          "annuities": 1000.25,
      |          "giftOfInvestmentsAndPropertyToCharity": 1000.25,
      |          "apportionedPersonalAllowance": 1000.25,
      |          "marriageAllowanceTransfer": 1000.25,
      |          "blindPersonAllowance": 1000.25,
      |          "blindPersonSurplusAllowanceFromSpouse": 1000.25,
      |          "incomeExcluded": 1000.25
      |        }
      |      },
      |      "totalIncomeTax": 1000.25,
      |      "incomeTax": {
      |        "totalBeforeReliefs": 1000.25,
      |        "totalAfterReliefs": 1000.25,
      |        "taxableIncome": 1000.25,
      |        "payPensionsProfit": {
      |          "totalAmount": 1000.25,
      |          "taxableIncome": 1000.25,
      |          "band": [
      |            {
      |              "name": "ZRT",
      |              "rate": 99.99,
      |              "threshold": 99999999999,
      |              "apportionedThreshold": 99999999999,
      |              "bandLimit": 1000,
      |              "apportionedBandLimit": 1000,
      |              "income": 1000.25,
      |              "taxAmount": 1000.25
      |            }
      |          ],
      |          "personalAllowanceUsed": 1000.25
      |        },
      |        "savingsAndGains": {
      |          "totalAmount": 1000.25,
      |          "taxableIncome": 1000.25,
      |          "band": [
      |            {
      |              "name": "BRT",
      |              "rate": 99.99,
      |              "threshold": 99999999999,
      |              "apportionedThreshold": 99999999999,
      |              "bandLimit": 1000,
      |              "apportionedBandLimit": 1000,
      |              "income": 1000.25,
      |              "taxAmount": 1000.25
      |            }
      |          ],
      |          "personalAllowanceUsed": 1000.25
      |        },
      |        "dividends": {
      |          "totalAmount": 1000.25,
      |          "taxableIncome": 1000.25,
      |          "band": [
      |            {
      |              "name": "HRT",
      |              "rate": 99.99,
      |              "threshold": 99999999999,
      |              "apportionedThreshold": 99999999999,
      |              "bandLimit": 1000,
      |              "apportionedBandLimit": 1000,
      |              "income": 1000.25,
      |              "taxAmount": 1000.25
      |            }
      |          ],
      |          "personalAllowanceUsed": 1000.25
      |        },
      |        "residentialFinanceCosts": {
      |          "amountClaimed": 1000.25,
      |          "allowableAmount": 1000.25,
      |          "rate": 10.25
      |        },
      |        "excludedIncome": 1000.25,
      |        "totalAllowancesAndReliefs": 1000.25,
      |        "allowancesAndReliefs": {
      |          "deficiencyRelief": 1000.25,
      |          "topSlicingRelief": 1000.25,
      |          "ventureCapitalTrustRelief": 1000.25,
      |          "enterpriseInvestmentSchemeRelief": 1000.25,
      |          "seedEnterpriseInvestmentSchemeRelief": 1000.25,
      |          "communityInvestmentTaxRelief": 1000.25,
      |          "socialInvestmentTaxRelief": 1000.25,
      |          "maintenanceAndAlimonyPaid": 1000.25,
      |          "marriedCoupleAllowanceRate": 1000.25,
      |          "marriedCoupleAllowanceAmount": 1000.25,
      |          "marriedCoupleAllowanceRelief": 1000.25,
      |          "surplusMarriedCoupleAllowanceAmount": 1000.25,
      |          "surplusMarriedCoupleAllowanceRelief": 1000.25,
      |          "notionalTaxFromLifePolicies": 1000.25,
      |          "notionalTaxFromDividendsAndOtherIncome": 1000.25,
      |          "foreignTaxCreditRelief": 1000.25,
      |          "propertyFinanceRelief": 1000.25
      |        }
      |      },
      |      "totalNic": 1000.25,
      |      "nic": {
      |        "class2": {
      |          "amount": 1000.25,
      |          "weekRate": 1000.25,
      |          "weeks": 1,
      |          "limit": 99999999,
      |          "apportionedLimit": 2
      |        },
      |        "class4": {
      |          "totalAmount": 1000.25,
      |          "band": [
      |            {
      |              "name": "HRT",
      |              "rate": 99.99,
      |              "threshold": 99999999,
      |              "apportionedThreshold": 99999999,
      |              "income": 1000.25,
      |              "amount": 1000.25
      |            }
      |          ]
      |        }
      |      },
      |      "totalBeforeTaxDeducted": 1000.25,
      |      "totalTaxDeducted": 1000.25,
      |      "taxDeducted": {
      |        "ukLandAndProperty": 1000.25,
      |        "bbsi": 1000.25
      |      },
      |      "eoyEstimate": {
      |        "incomeSource": [
      |          {
      |            "id": "abcdefghijklm",
      |            "type": "01",
      |            "taxableIncome": 99999999.99,
      |            "supplied": true,
      |            "finalised": true
      |          },
      |          {
      |            "id": "abcdefghijklm",
      |            "type": "05",
      |            "taxableIncome": 99999999.99,
      |            "supplied": true,
      |            "finalised": true
      |          },
      |          {
      |            "type": "02",
      |            "taxableIncome": 99999999.99,
      |            "supplied": true,
      |            "finalised": true
      |          },
      |          {
      |            "type": "10",
      |            "taxableIncome": 99999999.99,
      |            "supplied": true,
      |            "finalised": true
      |          },
      |          {
      |            "type": "98",
      |            "taxableIncome": 99999999.99,
      |            "supplied": true
      |          },
      |          {
      |            "type": "09",
      |            "id": "Some ID",
      |            "taxableIncome": 99999999.99,
      |            "supplied": true
      |          }
      |        ],
      |        "totalTaxableIncome": 99999999.99,
      |        "incomeTaxAmount": 99999999.99,
      |        "nic2": 99999999,
      |        "nic4": 99999999,
      |        "totalNicAmount": 99999999.99,
      |        "incomeTaxNicAmount": 99999999.99
      |      },
      |      "msgCount": 1,
      |      "msg": [
      |        {
      |          "type": "WARN",
      |          "text": "You have entered a large amount in total Gift Aid payments. Please check."
      |        }
      |      ],
      |      "previousCalc": {
      |        "calcTimestamp": "4498-07-06T21:42:24.294Z",
      |        "calcID": "00000000",
      |        "calcAmount": 1000.25
      |      },
      |      "annualAllowances": {
      |        "personalAllowance": 99999999,
      |        "reducedPersonalAllowanceThreshold": 99999999,
      |        "reducedPersonalisedAllowance": 99999999
      |      }
      |    }
      |  }
      |}
    """.stripMargin)

  def v3_2DesTaxCalcErrorJson(errors: (String, String)*): JsValue = {
    if(errors.size > 1) {
      Json.obj("failures" -> Json.arr(errors.map {
        case (code, reason) => Json.obj("code" -> code, "reason" -> reason)
      }))
    } else {
      val (code, reason) = errors.head
      Json.obj("code" -> code, "reason" -> reason)
    }
  }
  val bvrErrorJson: JsValue = Json.parse(
    """
      |{
      |    "calcOutput": {
      |        "calcID": "041f7e4d-87b9-4d4a-a296-3cfbdf92f7e2",
      |        "bvrTimestamp": "2018-06-04T12:13:48.651Z",
      |        "bvrVersion": "1.0.0",
      |        "bvrErrors": 4,
      |        "bvrWarnings": 1,
      |        "bvrMsg": [
      |            {
      |                "id": "C11101",
      |                "type": "WARN",
      |                "text": "You have entered a large amount in total Gift Aid payments. Please check."
      |            },
      |            {
      |                "id": "C15102",
      |                "type": "ERR",
      |                "text": "Total amount of one-off Gift Aid payments cannot exceed the total gift aid payments. Please check."
      |            },
      |            {
      |                "id": "C15103",
      |                "type": "ERR",
      |                "text": "Gift aid payments made this year treated as paid in the previous year cannot exceed the total gift aid payments. Please check."
      |            },
      |            {
      |                "id": "C15104",
      |                "type": "ERR",
      |                "text": "Value of qualifying investments gifted to non-UK charities cannot exceed the sum of 'Value of qualifying shares and securities gifted to charity' and 'Value of qualifying land and buildings gifted to charity'. Please check."
      |            },
      |            {
      |                "id": "C15105",
      |                "type": "ERR",
      |                "text": "Gift aid payments to non-UK charities cannot exceed the total gift aid payments. Please check."
      |            }
      |        ]
      |    }
      |}
    """.stripMargin
  )
}
