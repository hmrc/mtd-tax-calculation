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

package v2.services

import v2.mocks.connectors.MockTaxCalcConnector
import v2.models.errors.InvalidCalcID
import v2.outcomes.TaxCalcOutcome.TaxCalcOutcome

import scala.concurrent.Future

class TaxCalcServiceSpec extends ServiceSpec {

  class Test extends MockTaxCalcConnector {
    val service = new TaxCalcService(
      connector = mockTaxCalcConnector
    )
  }

  val mtdId = "test-mtdId"

  "getTaxCalculation" should {
    "return an InvalidCalcId error" when {
      "the supplied calculation ID does not match the internally defined calcID format" in new Test {
        val invalidCalcId = "invalid-calc-id"

        val result: TaxCalcOutcome = await(service.getTaxCalculation(mtdId, invalidCalcId))
        result shouldBe Left(InvalidCalcID)
      }
    }

    "return a TaxCalculation" when {
      import v2.fixtures.{TaxCalculationFixture => TestData}

      "a valid calcId is passed and the connector returns a tax calculation" in new Test {
        val calcId = "67918878"
        MockedTaxCalcConnector.getTaxCalculation(mtdId, calcId)
          .returns(Future.successful(Right(TestData.testTaxCalc)))

        val result: TaxCalcOutcome = await(service.getTaxCalculation(mtdId, calcId))
        result shouldBe Right(TestData.testTaxCalc)
      }
    }
  }
}
