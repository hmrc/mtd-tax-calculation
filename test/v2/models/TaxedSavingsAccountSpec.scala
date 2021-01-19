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

import play.api.libs.json.Json
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class TaxedSavingsAccountSpec extends UnitSpec with JsonErrorValidators {

  import JsonError._

  val model = TaxedSavingsAccount(
    savingsAccountId = "accountId",
    name = Some("accountName"),
    gross = Some(456.78),
    net = Some(567.89),
    taxDeducted = Some(678.90)
  )

  val desJson =
     """
      |{
      |   "incomeSourceID": "accountId",
      |   "name": "accountName",
      |   "gross": 456.78,
      |   "net": 567.89,
      |   "taxDeducted": 678.90
      |}
    """.stripMargin

  val emptyModel = TaxedSavingsAccount("accountId", None, None, None, None)

  "TaxedSavingsAccount reads" should {

    "return correct validation errors" when {

      testPropertyType[TaxedSavingsAccount](desJson)(
        property = "gross",
        invalidValue = "\"nan\"",
        errorPathAndError = ".gross" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[TaxedSavingsAccount](desJson)(
        property = "net",
        invalidValue = "\"nan\"",
        errorPathAndError = ".net" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[TaxedSavingsAccount](desJson)(
        property = "taxDeducted",
        invalidValue = "\"nan\"",
        errorPathAndError = ".taxDeducted" -> NUMBER_FORMAT_EXCEPTION
      )
    }

    "return a correctly read SavingsIncome model" when {
      "all fields exist" in {
        TaxedSavingsAccount.reads.reads(Json.parse(desJson)).get shouldBe model
      }

      "nullable fields don't exist" in {
        TaxedSavingsAccount.reads.reads(Json.parse(
          """
            |{
            |   "incomeSourceID": "accountId"
            |}
          """.stripMargin)).get shouldBe emptyModel
      }
    }
  }

  "TaxedSavingsAccount reads" should {

    "return client json" when {
      "all fields exist" in {

        val clientJson = Json.parse(
          """
            |{
            |   "savingsAccountId": "accountId",
            |   "name": "accountName",
            |   "gross": 456.78,
            |   "net": 567.89,
            |   "taxDeducted": 678.90
            |}
          """.stripMargin)

        TaxedSavingsAccount.writes.writes(model) shouldBe clientJson
      }

      "all fields don't exist" in {
        TaxedSavingsAccount.writes.writes(emptyModel) shouldBe Json.parse(
          """
            |{
            |   "savingsAccountId": "accountId"
            |}
          """.stripMargin)
      }
    }

  }


}
