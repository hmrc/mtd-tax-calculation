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

package v2.models.taxcalculation

import play.api.libs.json.Json
import uk.gov.hmrc.play.test.UnitSpec
import v2.models._

class TaxCalculationSpec extends UnitSpec {

  "A tax calculation" should {
    import v2.fixtures.{TaxCalculationFixture => TestData}

    "be parsed from correct JSON" in {
      val result = TestData.v3_2DesTaxCalcJson.as[TaxCalculation]
      result shouldEqual TestData.v3_2ClientTaxCalc
    }

    val validJsonPayloadsForVersion3_2 = Map(
    "JSON representing self employment with UK property" -> TestData.selfEmploymentUkPropertyJson,
    "JSON representing self employment with periodic updates" -> TestData.selfEmploymentPeriodicJson,
    "JSON representing UK property with non-FHL with periodic and annual updates" -> TestData.ukPropertyOtherPeriodicAndAnnualJson,
    "JSON representing self employment with periodic and annual updates" -> TestData.selfEmploymentPeriodicAndAnnualsJson,
    "JSON representing UK property with FHL with periodic and annual updates" -> TestData.ukPropertyFhlPeriodicAndAnnualsJson,
    "JSON representing Scottish self employment with multiple income sources" -> TestData.selfEmploymentScottishMultipleIncomeSourcesJson
    )

    validJsonPayloadsForVersion3_2.foreach{
      case (description, payload) => s"be able to parse $description without falling over" in { payload.as[TaxCalculation] }
    }
  }

  "EoyEstimate" should {
    "be parsed correctly from valid JSON" in {

      val eoyEstimateJson = Json.parse(
        """
          |{
          |  "totalTaxableIncome":1234.56,
          |  "incomeTaxAmount":9938.22,
          |  "nic2":7738.33,
          |  "nic4":228.22,
          |  "totalNicAmount":3321.11,
          |  "incomeTaxNicAmount":99.22,
          |  "incomeSource" : [
          |    {
          |      "id":"id1",
          |      "type":"05",
          |      "taxableIncome":62345.67,
          |      "supplied":true,
          |      "finalised":true
          |    },
          |    {
          |      "id":"id2",
          |      "type":"05",
          |      "taxableIncome":423.22,
          |      "supplied":false,
          |      "finalised":true
          |    },
          |    {
          |      "id":"id3",
          |      "type":"01",
          |      "taxableIncome":12443.22,
          |      "supplied":false,
          |      "finalised":false
          |    },
          |    {
          |      "type":"02",
          |      "taxableIncome":9982.03,
          |      "supplied":true,
          |      "finalised":true
          |    },
          |    {
          |      "type":"10",
          |      "taxableIncome":1123.2,
          |      "supplied":true,
          |      "finalised":false
          |    }
          |  ]
          |}
        """.stripMargin)

      val expectedEoyEstimate = EoyEstimate(
        employments = List(
          EoyEmployment(Some("id1"), Some(62345.67), Some(true), Some(true)),
          EoyEmployment(Some("id2"), Some(423.22), Some(false), Some(true))
        ),
        selfEmployments = List(
          EoySelfEmployment("id3", 12443.22, false, Some(false))
        ),
        ukProperty = EoyItem(9982.03, supplied = true, Some(true)),
        ukDividends = EoyItem(1123.2, supplied = true, Some(false)),
        totalTaxableIncome = Some(1234.56),
        incomeTaxAmount = Some(9938.22),
        nic2 = Some(7738.33),
        nic4 = Some(228.22),
        totalNicAmount = Some(3321.11),
        incomeTaxAndNicAmount = Some(99.22)
      )

      eoyEstimateJson.as[EoyEstimate](EoyEstimate.reads) shouldBe expectedEoyEstimate
    }
  }
}
