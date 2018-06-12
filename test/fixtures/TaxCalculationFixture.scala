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

package fixtures

import models.taxcalculation.TaxCalculation
import play.api.libs.json.Json

object TaxCalculationFixture {

  val ten: Int = 10
  val testTaxCalc =
    TaxCalculation(
      year = "2016-17",
      intentToCrystallise = Some(false),
      crystallised = Some(false),
      validationMessageCount = Some(ten),
      incomeTaxAndNicYTD = Some(1000.25),
      nationalRegime = "UK",
      taxableIncome = Json.obj(),
      incomeTax = Json.obj(),
      nic = Json.obj(),
      totalBeforeTaxDeducted = Some(1000.25),
      taxDeducted = Some(Json.obj()),
      eoyEstimate = Json.obj(),
      calculationMessageCount = Some(1),
      calculationMessages = Json.arr(),
      annualAllowances = Json.obj()
    )

  val testTaxCalcString: String =
    """
      |{
      |"year": "2016-17",
      |	"intentToCrystallise": false,
      |	"crystallised": false,
      | "validationMessageCount":10,
      |	"incomeTaxAndNicYTD": 1000.25,
      |	"nationalRegime": "UK",
      |	"taxableIncome": {
      |	},
      |	"incomeTax": {
      |	},
      |	"nic": {
      |	},
      |	"totalBeforeTaxDeducted": 1000.25,
      |	"taxDeducted": {
      |	},
      |	"eoyEstimate": {
      |	},
      |	"calculationMessageCount": 1,
      |	"calculationMessages": [
      |	],
      |	"annualAllowances": {
      |	}
      |}
    """.stripMargin
}
