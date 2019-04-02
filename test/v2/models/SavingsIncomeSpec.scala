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

class SavingsIncomeSpec extends UnitSpec with JsonErrorValidators {

  import JsonError._

  val model =
    SavingsIncome(
      totalIncome = Some(123.45),
      totalTaxedInterestIncome = Some(234.56),
      totalUntaxedInterestIncome = Some(345.67),
      taxedAccounts = Some(Seq(TaxedSavingsAccount(
        savingsAccountId = "accountId",
        name = Some("accountName"),
        gross = Some(456.78),
        net = Some(567.89),
        taxDeducted = Some(678.90)
      ))),
      untaxedAccounts = Some(Seq(UntaxedSavingsAccount(
        savingsAccountId = "accountId",
        name = Some("accountName"),
        gross = Some(456.78)
      )))
    )

  val desJson =
    """{
      |"bbsiIncome": 123.45,
      |"bbsi": {
      |   "totalTaxedInterestIncome": 234.56,
      |   "totalUntaxedInterestIncome": 345.67,
      |   "taxedAccounts" : [ {
      |       "incomeSourceId": "accountId",
      |       "name": "accountName",
      |       "gross": 456.78,
      |       "net": 567.89,
      |      "taxDeducted": 678.90
      |   }],
      |   "untaxedAccounts" : [ {
      |       "incomeSourceId": "accountId",
      |       "name": "accountName",
      |       "gross": 456.78
      |   }]
      |}
      |}
    """.stripMargin

  val emptySavings = SavingsIncome(None, None, None, None, None)

  val emptyJson: JsValue = Json.parse("{}")

  "SavingsIncome reads" should {

    "return correct validation errors" when {

      testPropertyType[SavingsIncome](desJson)(
        property = "bbsiIncome",
        invalidValue = "\"nan\"",
        errorPathAndError = ".bbsiIncome" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[SavingsIncome](desJson)(
        property = "totalTaxedInterestIncome",
        invalidValue = "\"nan\"",
        errorPathAndError = ".totalTaxedInterestIncome" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[SavingsIncome](desJson)(
        property = "totalUntaxedInterestIncome",
        invalidValue = "\"nan\"",
        errorPathAndError = ".totalUntaxedInterestIncome" -> NUMBER_FORMAT_EXCEPTION
      )
    }

    "return a correctly read SavingsIncome model" when {
      "all fields exist" in {
        SavingsIncome.reads.reads(Json.parse(desJson)).get shouldBe model
      }

      "nullable fields don't exist" in {
        SavingsIncome.reads.reads(emptyJson).get shouldBe emptySavings
      }
    }
  }

  "SavingsIncome reads" should {

    "return client json" when {
      "all fields exist" in {

        val clientJson = Json.parse(
          """
            |{
            |"totalIncome": 123.45,
            |"totalTaxedInterestIncome": 234.56,
            |"totalUntaxedInterestIncome": 345.67,
            |"taxedAccounts" : [ {
            |   "savingsAccountId": "accountId",
            |   "name": "accountName",
            |   "gross": 456.78,
            |   "net": 567.89,
            |   "taxDeducted": 678.90
            |} ],
            |"untaxedAccounts" : [ {
            |   "savingsAccountId": "accountId",
            |   "name": "accountName",
            |   "gross": 456.78
            |} ]
            |}
          """.stripMargin)

        SavingsIncome.writes.writes(model) shouldBe clientJson
      }

      "all fields don't exist" in {
        SavingsIncome.writes.writes(emptySavings) shouldBe emptyJson
      }
    }

  }


}