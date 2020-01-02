/*
 * Copyright 2020 HM Revenue & Customs
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

import play.api.libs.json.{JsArray, JsObject, JsValue, Json}
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class EoyEstimateSpec extends UnitSpec with JsonErrorValidators {


  val validEoyEmploymentModel = EoyEmployment(
    employmentId = "Some ID",
    taxableIncome = BigDecimal("1234567.89"),
    supplied = true,
    finalised = Some(true)
  )
  val validEoySelfEmploymentModel = EoySelfEmployment(
    selfEmploymentId = "Some ID",
    taxableIncome = BigDecimal("1234567.89"),
    supplied = true,
    finalised = Some(true)
  )
  val validEoyItemModel = EoyItem(
    taxableIncome = BigDecimal("1234567.89"),
    supplied = true,
    finalised = Some(true)
  )

  val savings = EoySavings("Some ID", 1234567.89, supplied = true)

  val validEoyEstimateModel = EoyEstimate(
    employments = Some(Seq(validEoyEmploymentModel)),
    selfEmployments = Some(Seq(validEoySelfEmploymentModel)),
    ukProperty = Some(validEoyItemModel),
    ukDividends = Some(validEoyItemModel),
    savings = Some(Seq(savings)),
    totalTaxableIncome= 123.45,
    incomeTaxAmount= 123.45,
    nic2= 123.45,
    nic4= 123.45,
    totalNicAmount= 123.45,
    incomeTaxNicAmount= 123.45
  )

  val validEoyEstimateJson: String =
    """
      |{
      | "incomeSource": [
      |   {
      |     "type": "05",
      |     "id": "Some ID",
      |     "taxableIncome": 1234567.89,
      |     "supplied": true,
      |     "finalised": true
      |   },
      |   {
      |     "type": "01",
      |     "id": "Some ID",
      |     "taxableIncome": 1234567.89,
      |     "supplied": true,
      |     "finalised": true
      |   },
      |   {
      |     "type": "02",
      |     "taxableIncome": 1234567.89,
      |     "supplied": true,
      |     "finalised": true
      |   },
      |   {
      |     "type": "10",
      |     "taxableIncome": 1234567.89,
      |     "supplied": true,
      |     "finalised": true
      |   },
      |   {
      |     "type": "98",
      |     "taxableIncome": 1234567.89,
      |     "supplied": true
      |   },
      |   {
      |     "type": "09",
      |     "id": "Some ID",
      |     "taxableIncome": 1234567.89,
      |     "supplied": true
      |   }
      | ],
      | "totalTaxableIncome": 123.45,
      | "incomeTaxAmount": 123.45,
      | "nic2": 123.45,
      | "nic4": 123.45,
      | "totalNicAmount": 123.45,
      | "incomeTaxNicAmount": 123.45
      |}
    """.stripMargin

  val validEoyItemInputJson: JsValue =
    Json.parse("""
      |{
      |     "taxableIncome": 1234567.89,
      |     "supplied": true,
      |     "finalised": true
      |}
    """.stripMargin)

  val validEoyEstimateOutputJson: JsValue = Json.parse(
    s"""
      |{
      |  "employments" : [${validEoyItemInputJson.as[JsObject] + ("employmentId" -> Json.parse("\"Some ID\""))}],
      |  "selfEmployments" : [${validEoyItemInputJson.as[JsObject] + ("selfEmploymentId" -> Json.parse("\"Some ID\""))}],
      |  "ukProperty" : $validEoyItemInputJson,
      |  "ukDividends" : $validEoyItemInputJson,
      |  "savings" : [${validEoyItemInputJson.as[JsObject]  - "finalised" + ("savingsAccountId" -> Json.parse("\"Some ID\""))}],
      |  "totalTaxableIncome": 123.45,
      |  "incomeTaxAmount": 123.45,
      |  "nic2": 123.45,
      |  "nic4": 123.45,
      |  "totalNicAmount": 123.45,
      |  "incomeTaxNicAmount": 123.45
      |}
    """.stripMargin)

  "reads" should {

    import JsonError._

    "return correct validation errors" when {
      testMandatoryProperty[EoyEstimate](validEoyEstimateJson)(property = "totalTaxableIncome")
      testMandatoryProperty[EoyEstimate](validEoyEstimateJson)(property = "incomeTaxAmount")
      testMandatoryProperty[EoyEstimate](validEoyEstimateJson)(property = "nic2")
      testMandatoryProperty[EoyEstimate](validEoyEstimateJson)(property = "nic4")
      testMandatoryProperty[EoyEstimate](validEoyEstimateJson)(property = "totalNicAmount")
      testMandatoryProperty[EoyEstimate](validEoyEstimateJson)(property = "incomeTaxNicAmount")

      testPropertyType[EoyEstimate](validEoyEstimateJson)(
        property = "totalTaxableIncome",
        invalidValue = "\"some string\"",
        errorPathAndError = "/totalTaxableIncome" -> NUMBER_FORMAT_EXCEPTION
      )
      testPropertyType[EoyEstimate](validEoyEstimateJson)(
        property = "incomeTaxAmount",
        invalidValue = "\"some string\"",
        errorPathAndError = "/incomeTaxAmount" -> NUMBER_FORMAT_EXCEPTION
      )
      testPropertyType[EoyEstimate](validEoyEstimateJson)(
        property = "nic2",
        invalidValue = "\"some string\"",
        errorPathAndError = "/nic2" -> NUMBER_FORMAT_EXCEPTION
      )
      testPropertyType[EoyEstimate](validEoyEstimateJson)(
        property = "nic4",
        invalidValue = "\"some string\"",
        errorPathAndError = "/nic4" -> NUMBER_FORMAT_EXCEPTION
      )
      testPropertyType[EoyEstimate](validEoyEstimateJson)(
        property = "totalNicAmount",
        invalidValue = "\"some string\"",
        errorPathAndError = "/totalNicAmount" -> NUMBER_FORMAT_EXCEPTION
      )
      testPropertyType[EoyEstimate](validEoyEstimateJson)(
        property = "incomeTaxNicAmount",
        invalidValue = "\"some string\"",
        errorPathAndError = "/incomeTaxNicAmount" -> NUMBER_FORMAT_EXCEPTION
      )
    }
  }

  "return a successfully read eoy estimate model" when {
    "all fields exist" in {
      Json.parse(validEoyEstimateJson).as[EoyEstimate] shouldBe validEoyEstimateModel
    }

    "only mandatory fields exist" in {
      val json = Json.parse(validEoyEstimateJson).as[JsObject] - "incomeSource" + ("incomeSource" -> JsArray.apply())
      val model = validEoyEstimateModel.copy(employments = None, selfEmployments = None, ukProperty = None, ukDividends = None,
        savings = None)
      json.as[EoyEstimate] shouldBe model
    }

  }

  "writes" should {
    "return client json" when {
      "all fields exist" in {
        Json.toJson(validEoyEstimateModel) shouldBe validEoyEstimateOutputJson
      }
    }
  }
}
