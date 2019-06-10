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

trait TaxableIncomeFixture {

  val bigDecimal = 1234567.89

  val validTaxableIncomeInputJson: JsValue = Json.parse(
    """
      | {
      | "taxableIncome": {
      |        "totalIncomeReceived": 1234567.89,
      |        "incomeReceived": {
      |          "employmentIncome": 1234567.89,
      |          "employments": {
      |            "totalPay": 1234567.89,
      |            "totalBenefitsAndExpenses": 1234567.89,
      |            "totalAllowableExpenses": 1234567.89,
      |            "employment": [
      |              {
      |                "incomeSourceID": "ABIS10000000001",
      |                "latestDate": "2018-07-12",
      |                "netPay": 1234567.89,
      |                "benefitsAndExpenses": 1234567.89,
      |                "allowableExpenses": 1234567.89
      |              }
      |            ]
      |          },
      |          "selfEmploymentIncome": 1234567.89,
      |          "selfEmployment": [
      |            {
      |              "incomeSourceID": "XKIS00000000988",
      |              "latestDate": "2018-07-12",
      |              "accountStartDate": "2017-04-06",
      |              "taxableIncome": 1234567.89,
      |              "losses": 0
      |            }
      |          ],
      |          "ukPropertyIncome": 1234567.89,
      |          "ukProperty": {
      |            "incomeSourceID": "XDIS00000000114",
      |            "latestDate": "2018-07-12",
      |            "taxableProfit": 1234567.89,
      |            "taxableProfitFhlUk": 1234567.89,
      |            "finalised": false
      |          },
      |          "bbsiIncome": 1234567.89,
      |          "bbsi": {
      |             "totalTaxedInterestIncome": 1234567.89,
      |             "totalUntaxedInterestIncome": 1234567.89,
      |             "taxedAccounts": [{
      |               "incomeSourceID": "accountId",
      |               "name": "accountName",
      |               "gross": 1234567.89,
      |               "net": 1234567.89,
      |               "taxDeducted": 1234567.89
      |             }],
      |             "untaxedAccounts": [{
      |               "incomeSourceID": "accountId",
      |               "name": "accountName",
      |               "gross": 1234567.89
      |             }]
      |           },
      |          "ukDividendIncome": 1234567.89,
      |          "ukDividend": {
      |             "otherUkDividends": 1234567.89,
      |             "ukDividends": 1234567.89
      |          },
      |          "ukDividendIncome": 1234567.89
      |        },
      |        "totalAllowancesAndDeductions": 1234567.89,
      |        "allowancesAndDeductions": {
      |          "giftOfInvestmentsAndPropertyToCharity": 1234567.89,
      |          "apportionedPersonalAllowance": 1234567.89
      |        }
      |      },
      |      "totalTaxableIncome": 1234567.89
      |}
    """.stripMargin)

  val noIncomeReceivedInputJson: JsValue = Json.parse(
    """
      | {
      | "taxableIncome": {
      |        "totalIncomeReceived": 1234567.89,
      |        "totalAllowancesAndDeductions": 1234567.89,
      |        "allowancesAndDeductions": {
      |          "giftOfInvestmentsAndPropertyToCharity": 1234567.89,
      |          "apportionedPersonalAllowance": 1234567.89
      |        }
      |      },
      |      "totalTaxableIncome": 1234567.89
      |}
    """.stripMargin)


  val validTaxableIncomeModel = TaxableIncome(
    employments = Some(Employments(
      totalIncome = Some(bigDecimal),
      totalPay = bigDecimal,
      totalBenefitsAndExpenses = bigDecimal,
      totalAllowableExpenses = bigDecimal,
      employment = Seq(
        Employment(
          employmentId = "ABIS10000000001",
          netPay = bigDecimal,
          benefitsAndExpenses = bigDecimal,
          allowableExpenses = bigDecimal
        )
      )
    )),
    selfEmployments = Some(SelfEmployments(
      totalIncome = Some(bigDecimal),
      selfEmployment = Some(Seq(
        SelfEmployment(
          selfEmploymentId = "XKIS00000000988",
          taxableIncome = bigDecimal,
          finalised = Some(true),
          losses = Some(bigDecimal)
        )
      ))
    )),
    ukProperty = Some(UKProperty(
      totalIncome = Some(bigDecimal),
      nonFurnishedHolidayLettingsTaxableProfit = Some(bigDecimal),
      nonFurnishedHolidayLettingsLoss = Some(bigDecimal),
      furnishedHolidayLettingsTaxableProfit = Some(bigDecimal),
      furnishedHolidayLettingsLoss = Some(bigDecimal),
      finalised = Some(true)
    )),
    ukDividends = Some(UKDividends(
      totalIncome = Some(bigDecimal),
      ukDividends = Some(bigDecimal),
      otherUkDividends = Some(bigDecimal)
    )),
    savings = Some(SavingsIncome(
      totalIncome = Some(bigDecimal),
      totalTaxedInterestIncome = Some(bigDecimal),
      totalUntaxedInterestIncome = Some(bigDecimal),
      taxedAccounts = Some(Seq(TaxedSavingsAccount(
        savingsAccountId = "accountId",
        name = Some("accountName"),
        gross = Some(bigDecimal),
        net = Some(bigDecimal),
        taxDeducted = Some(bigDecimal)
      ))),
      untaxedAccounts = Some(Seq(UntaxedSavingsAccount(
        savingsAccountId = "accountId",
        name = Some("accountName"),
        gross = Some(bigDecimal)
      ))
      ))),
    totalIncomeReceived = bigDecimal,
    allowancesAndDeductions = AllowancesAndDeductions(
      totalAllowancesAndDeductions = bigDecimal,
      giftOfInvestmentsAndPropertyToCharity = Some(bigDecimal),
      apportionedPersonalAllowance = bigDecimal
    ),
    totalTaxableIncome = Some(bigDecimal)
  )

  val validTaxableIncomeInputString: String =
    """
      | {
      | "taxableIncome": {
      |        "totalIncomeReceived": 1234567.89,
      |        "incomeReceived": {
      |          "employmentIncome": 1234567.89,
      |          "employments": {
      |            "totalPay": 1234567.89,
      |            "totalBenefitsAndExpenses": 1234567.89,
      |            "totalAllowableExpenses": 1234567.89,
      |            "employment": [
      |              {
      |                "incomeSourceID": "ABIS10000000001",
      |                "latestDate": "2018-07-12",
      |                "netPay": 1234567.89,
      |                "benefitsAndExpenses": 1234567.89,
      |                "allowableExpenses": 1234567.89
      |              }
      |            ]
      |          },
      |          "selfEmploymentIncome": 1234567.89,
      |          "selfEmployment": [
      |            {
      |              "incomeSourceID": "XKIS00000000988",
      |              "latestDate": "2018-07-12",
      |              "accountStartDate": "2017-04-06",
      |              "taxableIncome": 1234567.89,
      |              "losses": 1234567.89,
      |              "finalised":true
      |            }
      |          ],
      |          "ukPropertyIncome": 1234567.89,
      |          "ukProperty": {
      |            "incomeSourceID": "XDIS00000000114",
      |            "latestDate": "2018-07-12",
      |            "taxableProfit": 1234567.89,
      |            "taxableProfitFhlUk": 1234567.89,
      |            "finalised": true,
      |            "lossesFhlUk": 1234567.89,
      |            "losses":1234567.89
      |          },
      |          "bbsiIncome": 1234567.89,
      |          "bbsi": {
      |             "totalTaxedInterestIncome": 1234567.89,
      |             "totalUntaxedInterestIncome": 1234567.89,
      |             "taxedAccounts": [{
      |               "incomeSourceID": "accountId",
      |               "name": "accountName",
      |               "gross": 1234567.89,
      |               "net": 1234567.89,
      |               "taxDeducted": 1234567.89
      |             }],
      |             "untaxedAccounts": [{
      |               "incomeSourceID": "accountId",
      |               "name": "accountName",
      |               "gross": 1234567.89
      |             }]
      |          },
      |          "ukDividendIncome": 1234567.89,
      |          "ukDividend": {
      |            "ukDividends": 1234567.89,
      |            "otherUkDividends": 1234567.89
      |          }
      |        },
      |        "totalAllowancesAndDeductions": 1234567.89,
      |        "allowancesAndDeductions": {
      |          "giftOfInvestmentsAndPropertyToCharity": 1234567.89,
      |          "apportionedPersonalAllowance": 1234567.89
      |        }
      |      },
      |      "totalTaxableIncome": 1234567.89
      |}
    """.stripMargin

  val validTaxableIncomeOutputJson: JsValue = Json.parse(
    """{
      |   "employments":{
      |      "totalIncome":1234567.89,
      |      "totalPay":1234567.89,
      |      "totalBenefitsAndExpenses":1234567.89,
      |      "totalAllowableExpenses":1234567.89,
      |      "employment":[
      |         {
      |            "employmentId":"ABIS10000000001",
      |            "netPay":1234567.89,
      |            "benefitsAndExpenses":1234567.89,
      |            "allowableExpenses":1234567.89
      |         }
      |      ]
      |   },
      |   "selfEmployments":{
      |      "totalIncome":1234567.89,
      |      "selfEmployment":[
      |         {
      |            "selfEmploymentId":"XKIS00000000988",
      |            "taxableIncome":1234567.89,
      |            "finalised":true,
      |            "losses":1234567.89
      |         }
      |      ]
      |   },
      |   "ukProperty":{
      |      "totalIncome":1234567.89,
      |      "nonFurnishedHolidayLettingsTaxableProfit":1234567.89,
      |      "nonFurnishedHolidayLettingsLoss":1234567.89,
      |      "furnishedHolidayLettingsTaxableProfit":1234567.89,
      |      "furnishedHolidayLettingsLoss":1234567.89,
      |      "finalised":true
      |   },
      |   "ukDividends":{
      |      "totalIncome":1234567.89,
      |      "otherUkDividends": 1234567.89,
      |      "ukDividends": 1234567.89
      |   },
      |   "savings": {
      |     "totalIncome": 1234567.89,
      |     "totalTaxedInterestIncome": 1234567.89,
      |     "totalUntaxedInterestIncome": 1234567.89,
      |     "taxedAccounts": [{
      |        "savingsAccountId": "accountId",
      |        "name": "accountName",
      |        "gross": 1234567.89,
      |        "net": 1234567.89,
      |        "taxDeducted": 1234567.89
      |     }],
      |     "untaxedAccounts": [{
      |        "savingsAccountId": "accountId",
      |        "name": "accountName",
      |        "gross": 1234567.89
      |     }]
      |   },
      |   "totalIncomeReceived":1234567.89,
      |   "allowancesAndDeductions":{
      |      "totalAllowancesAndDeductions":1234567.89,
      |      "giftOfInvestmentsAndPropertyToCharity":1234567.89,
      |      "apportionedPersonalAllowance":1234567.89
      |   },
      |   "totalTaxableIncome":1234567.89
      |}
    """.stripMargin)
}