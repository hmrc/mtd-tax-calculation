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

package v2.models.taxcalculation

import play.api.libs.json._
import support.UnitSpec
import v2.fixtures.{TaxCalculationFixture => TestData}
import v2.models._
import v2.models.utils.JsonErrorValidators

class TaxCalculationSpec extends JsonErrorValidators with UnitSpec {

  val bvrErrorTaxCalc = TaxCalculation(
    None,
    false, false, 5,
    None, None, None, None, None, None, None, None, None, None, None)

  "reads" should {
    import JsonError._
    "return correct validation errors" when {

      testPropertyType[TaxCalculation](TestData.taxCalcDesJson)(
        path = "/calcOutput/year",
        replacement = "test".toJson,
        expectedError = JSNUMBER_FORMAT_EXCEPTION
      )
      testPropertyType[TaxCalculation](TestData.taxCalcDesJson)(
        path = "/calcOutput/intentToCrystallise",
        replacement = "test".toJson,
        expectedError = BOOLEAN_FORMAT_EXCEPTION
      )
      testPropertyType[TaxCalculation](TestData.taxCalcDesJson)(
        path = "/calcOutput/calcResult/crystallised",
        replacement = "test".toJson,
        expectedError = BOOLEAN_FORMAT_EXCEPTION
      )
      testPropertyType[TaxCalculation](TestData.taxCalcDesJson)(
        path = "/calcOutput/bvrErrors",
        replacement = "test".toJson,
        expectedError = JSNUMBER_FORMAT_EXCEPTION
      )
      testPropertyType[TaxCalculation](TestData.taxCalcDesJson)(
        path = "/calcOutput/bvrWarnings",
        replacement = "test".toJson,
        expectedError = JSNUMBER_FORMAT_EXCEPTION
      )
      testPropertyType[TaxCalculation](TestData.taxCalcDesJson)(
        path = "/calcOutput/calcResult/incomeTaxNicYtd",
        replacement = "test".toJson,
        expectedError = NUMBER_FORMAT_EXCEPTION
      )
      testPropertyType[TaxCalculation](TestData.taxCalcDesJson)(
        path = "/calcOutput/calcResult/nationalRegime",
        replacement = 10001.toJson,
        expectedError = STRING_FORMAT_EXCEPTION
      )
      testPropertyType[TaxCalculation](TestData.taxCalcDesJson)(
        path = "/calcOutput/calcResult/totalBeforeTaxDeducted",
        replacement = "test".toJson,
        expectedError = NUMBER_FORMAT_EXCEPTION
      )
      testPropertyType[TaxCalculation](TestData.taxCalcDesJson)(
        path = "/calcOutput/calcResult/msg",
        replacement = "test".toJson,
        expectedError = JSARRAY_FORMAT_EXCEPTION
      )
      testPropertyType[TaxCalculation](TestData.taxCalcDesJson)(
        path = "/calcOutput/calcResult/msgCount",
        replacement = "test".toJson,
        expectedError = JSNUMBER_FORMAT_EXCEPTION
      )
    }

    "create the correct optional field" when {

      val json = TestData.taxCalcDesJson

      "the 'year' field is not received" in {
        val updatedJson = removeJsonProperty[TaxCalculation](json)(pathToProperty = "calcOutput/year")
        val model = updatedJson.as[TaxCalculation]
        model.year shouldBe None
      }

      "the 'incomeTaxAndNicYtd' field is not received" in {
        val updatedJson = removeJsonProperty[TaxCalculation](json)(pathToProperty = "calcOutput/calcResult/incomeTaxNicYtd")
        val model = updatedJson.as[TaxCalculation]
        model.incomeTaxAndNicYTD shouldBe None
      }

      "the 'nationalRegime' field is not received" in {
        val updatedJson = removeJsonProperty[TaxCalculation](json)(pathToProperty = "calcOutput/calcResult/nationalRegime")
        val model = updatedJson.as[TaxCalculation]
        model.nationalRegime shouldBe None
      }

      "the 'totalBeforeTaxDeducted' field is not received" in {
        val updatedJson = removeJsonProperty[TaxCalculation](json)(pathToProperty = "calcOutput/calcResult/totalBeforeTaxDeducted")
        val model = updatedJson.as[TaxCalculation]
        model.totalBeforeTaxDeducted shouldBe None
      }

      "the entire 'calcResult' jsobject is not received" in {
        val updatedJson = removeJsonProperty[TaxCalculation](json)(pathToProperty = "calcOutput/calcResult")
        val model = updatedJson.as[TaxCalculation]
        model shouldBe
          TaxCalculation(
            year = Some(2016),
            intentToCrystallise = true,
            crystallised = false,
            validationMessageCount = 1,
            incomeTaxAndNicYTD = None,
            nationalRegime = None,
            taxableIncome = None,
            incomeTax = None,
            nic = None,
            totalBeforeTaxDeducted = None,
            taxDeducted = None,
            eoyEstimate = None,
            calculationMessageCount = None,
            calculationMessages = None,
            annualAllowances = None
          )
      }
    }
    "create the correct fields" when {
      "at least one 'bvrError' is returned" in {
        TaxCalculation.reads.reads(TestData.bvrErrorJson).get shouldEqual bvrErrorTaxCalc
      }
      "all fields are present" in {
        TaxCalculation.reads.reads(TestData.taxCalcDesJson).get shouldEqual TestData.taxCalc
      }
    }
  }

  "write" should {

    val taxCalculation: TaxCalculation = TestData.taxCalc
    val taxCalcJson: JsValue = TestData.taxCalcClientJson

    s"not render the 'year' field" when {
      "the value is not present" in {
        val model = taxCalculation.copy(year = None)
        val json = taxCalcJson.as[JsObject] - "year"
        Json.toJson(model) shouldBe json
      }
    }
    s"not render the 'incomeTaxAndNicYTD' field" when {
      "the value is not present" in {
        val model = taxCalculation.copy(incomeTaxAndNicYTD = None)
        val json = taxCalcJson.as[JsObject] - "incomeTaxAndNicYTD"
        Json.toJson(model) shouldBe json
      }
    }
    s"not render the 'nationalRegime' field" when {
      "the value is not present" in {
        val model = taxCalculation.copy(nationalRegime = None)
        val json = taxCalcJson.as[JsObject] - "nationalRegime"
        Json.toJson(model) shouldBe json
      }
    }
    s"not render the 'taxableIncome' field" when {
      "the value is not present" in {
        val model = taxCalculation.copy(taxableIncome = None)
        val json = taxCalcJson.as[JsObject] - "taxableIncome"
        Json.toJson(model) shouldBe json
      }
    }
    s"not render the 'incomeTax' field" when {
      "the value is not present" in {
        val model = taxCalculation.copy(incomeTax = None)
        val json = taxCalcJson.as[JsObject] - "incomeTax"
        Json.toJson(model) shouldBe json
      }
    }
    s"not render the 'nic' field" when {
      "the value is not present" in {
        val model = taxCalculation.copy(nic = None)
        val json = taxCalcJson.as[JsObject] - "nic"
        Json.toJson(model) shouldBe json
      }
    }
    s"not render the 'totalBeforeTaxDeducted' field" when {
      "the value is not present" in {
        val model = taxCalculation.copy(totalBeforeTaxDeducted = None)
        val json = taxCalcJson.as[JsObject] - "totalBeforeTaxDeducted"
        Json.toJson(model) shouldBe json
      }
    }
    s"not render the 'taxDeducted' field" when {
      "the value is not present" in {
        val model = taxCalculation.copy(taxDeducted = None)
        val json = taxCalcJson.as[JsObject] - "taxDeducted"
        Json.toJson(model) shouldBe json
      }
    }
    s"not render the 'eoyEstimate' field" when {
      "the value is not present" in {
        val model = taxCalculation.copy(eoyEstimate = None)
        val json = taxCalcJson.as[JsObject] - "eoyEstimate"
        Json.toJson(model) shouldBe json
      }
    }
    s"not render the 'calculationMessageCount' field" when {
      "the value is not present" in {
        val model = taxCalculation.copy(calculationMessageCount = None)
        val json = taxCalcJson.as[JsObject] - "calculationMessageCount"
        Json.toJson(model) shouldBe json
      }
    }
    s"not render the 'calculationMessages' field" when {
      "the value is not present" in {
        val model = taxCalculation.copy(calculationMessages = None)
        val json = taxCalcJson.as[JsObject] - "calculationMessages"
        Json.toJson(model) shouldBe json
      }
    }
    s"not render the 'annualAllowances' field" when {
      "the value is not present" in {
        val model = taxCalculation.copy(annualAllowances = None)
        val json = taxCalcJson.as[JsObject] - "annualAllowances"
        Json.toJson(model) shouldBe json
      }
    }


    "at least one 'bvrError' is returned" in {
      val clientJson = Json.parse(
        """
          |{
          | "intentToCrystallise": false,
          | "crystallised": false,
          | "validationMessageCount": 5
          |}
        """.stripMargin)

      TaxCalculation.writes.writes(bvrErrorTaxCalc) shouldBe clientJson
    }
    "all fields are present" in {
      TaxCalculation.writes.writes(taxCalculation) shouldBe TestData.taxCalcClientJson
    }
  }

}
