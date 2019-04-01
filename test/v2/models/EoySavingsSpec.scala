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

import play.api.libs.json.Json
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class EoySavingsSpec extends UnitSpec with JsonErrorValidators {

  val validEoySavingsJson: String =
    """
      |{
      |     "savingsAccountId": "Some ID",
      |     "taxableIncome": 1234567.89,
      |     "supplied": true
      |}
    """.stripMargin

  val validEoySavingsModel = EoySavings(
    savingsAccountId = "Some ID",
    taxableIncome = BigDecimal("1234567.89"),
    supplied = true
  )

  "reads" should {

    import JsonError._

    "return correct validation errors" when {
      testMandatoryProperty[EoySavings](validEoySavingsJson)(property = "savingsAccountId")
      testMandatoryProperty[EoySavings](validEoySavingsJson)(property = "taxableIncome")
      testMandatoryProperty[EoySavings](validEoySavingsJson)(property = "supplied")

      testPropertyType[EoySavings](validEoySavingsJson)(
        property = "savingsAccountId",
        invalidValue = "800",
        errorPathAndError = "/savingsAccountId" -> STRING_FORMAT_EXCEPTION
      )

      testPropertyType[EoySavings](validEoySavingsJson)(
        property = "taxableIncome",
        invalidValue = "\"some string\"",
        errorPathAndError = "/taxableIncome" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[EoySavings](validEoySavingsJson)(
        property = "supplied",
        invalidValue = "800",
        errorPathAndError = "/supplied" -> BOOLEAN_FORMAT_EXCEPTION
      )
    }
  }

  "return a successfully read eoy Savings model" when {
    "all fields exist" in {
      Json.parse(validEoySavingsJson).as[EoySavings] shouldBe validEoySavingsModel
    }

  }

  "writes" should {
    "render the correct Json" when {

      "all fields exist" in {
        Json.toJson(validEoySavingsModel) shouldBe Json.parse(validEoySavingsJson)
      }
    }
  }
}
