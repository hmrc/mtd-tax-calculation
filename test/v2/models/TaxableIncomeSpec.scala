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

import play.api.libs.json.{JsObject, JsValue, Json}
import support.UnitSpec
import v2.fixtures.TaxableIncomeFixture
import v2.models.utils.JsonErrorValidators

class TaxableIncomeSpec extends UnitSpec
  with JsonErrorValidators
  with TaxableIncomeFixture{


  "reads" should {

    import JsonError._

    "return correct validation errors" when {

      testMandatoryProperty[TaxableIncome](validTaxableIncomeInputString)(property = "totalIncomeReceived")

      testPropertyType[TaxableIncome](validTaxableIncomeInputJson)(
        path = "totalTaxableIncome",
        replacement = "test".toJson,
        expectedError = NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[TaxableIncome](validTaxableIncomeInputJson)(
        path = "/taxableIncome/totalIncomeReceived",
        replacement = "test".toJson,
        expectedError = NUMBER_FORMAT_EXCEPTION
      )

      "return a successfully read Taxable Income model" when {
        "all fields exist" in {
          Json.parse(validTaxableIncomeInputString).as[TaxableIncome] shouldBe validTaxableIncomeModel
        }

        "incomeReceived property is missing" in {
          val json = noIncomeReceivedInputJson.as[JsObject]
          val model = validTaxableIncomeModel.copy(
            employments = None,
            selfEmployments = None,
            ukProperty = None,
            ukDividends = None
          )
          json.as[TaxableIncome] shouldBe model
        }

        "selfEmployments property is present but fields are empty" in {
          val json = removeJsonProperty(Json.parse(validTaxableIncomeInputString))(pathToProperty = "taxableIncome/incomeReceived/selfEmployment")
          val noSelfEmploymentJson = removeJsonProperty(json)(pathToProperty = "taxableIncome/incomeReceived/selfEmploymentIncome")
          val model = validTaxableIncomeModel.copy(selfEmployments = None)
          noSelfEmploymentJson.as[TaxableIncome] shouldBe model
        }

        "ukProperty property is present but fields are empty" in {
          val json = removeJsonProperty(Json.parse(validTaxableIncomeInputString))(pathToProperty = "taxableIncome/incomeReceived/ukProperty")
          val noUkPropertyJson = removeJsonProperty(json)(pathToProperty = "taxableIncome/incomeReceived/ukPropertyIncome")
          val model = validTaxableIncomeModel.copy(ukProperty = None)
          noUkPropertyJson.as[TaxableIncome] shouldBe model
        }
        "ukDividends property is present but fields are empty" in {
          val json = removeJsonProperty(Json.parse(validTaxableIncomeInputString))(pathToProperty = "taxableIncome/incomeReceived/ukDividends")
          val noUkDividendjson = removeJsonProperty(json)(pathToProperty = "taxableIncome/incomeReceived/ukDividendIncome")
          val model = validTaxableIncomeModel.copy(ukDividends = None)
          noUkDividendjson.as[TaxableIncome] shouldBe model
        }
      }
    }
  }

  "writes" should {
    "return client json" when {
      "all fields exist" in {
        Json.toJson(validTaxableIncomeModel) shouldBe validTaxableIncomeOutputJson
      }
    }

    "not render the employments object" when {
      "it has no value" in {
        val model = validTaxableIncomeModel.copy(employments = None)
        val json: JsValue = validTaxableIncomeOutputJson.as[JsObject] - "employments"
        Json.toJson(model) shouldBe json
      }
    }

    "not render the selfEmployments object" when {
      "it has no value" in {
        val model = validTaxableIncomeModel.copy(selfEmployments = None)
        val json: JsValue = validTaxableIncomeOutputJson.as[JsObject] - "selfEmployments"
        Json.toJson(model) shouldBe json
      }
    }

    "not render the ukProperty object" when {
      "it has no value" in {
        val model = validTaxableIncomeModel.copy(ukProperty = None)
        val json: JsValue = validTaxableIncomeOutputJson.as[JsObject] - "ukProperty"
        Json.toJson(model) shouldBe json
      }
    }

    "not render the ukDividends object" when {
      "it has no value" in {
        val model = validTaxableIncomeModel.copy(ukDividends = None)
        val json: JsValue = validTaxableIncomeOutputJson.as[JsObject] - "ukDividends"
        Json.toJson(model) shouldBe json
      }
    }

    "not render the totalTaxableIncome object" when {
      "it has no value" in {
        val model = validTaxableIncomeModel.copy(totalTaxableIncome = None)
        val json: JsValue = validTaxableIncomeOutputJson.as[JsObject] - "totalTaxableIncome"
        Json.toJson(model) shouldBe json
      }
    }
  }
}
