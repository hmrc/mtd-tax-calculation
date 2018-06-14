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

package connectors

import fixtures.TaxCalculationFixture
import mocks.{MockAppConfig, MockHttpClient}
import outcomes.TaxCalcOutcome
import outcomes.TaxCalcOutcome.TaxCalcOutcome
import uk.gov.hmrc.http.HttpResponse

import scala.concurrent.Future

class TaxCalcConnectorSpec extends ConnectorSpec {

  val desBaseUrl = "test-base-url"
  val nino = "test-nino"
  val calculationId = "test-calc-id"
  val url = s"$desBaseUrl/income-tax/calculation-data/$nino/calcId/$calculationId"

  val httpResponseOk = HttpResponse(OK)
  val httpResponseNotFound = HttpResponse(NOT_FOUND)

  class Test extends MockHttpClient with MockAppConfig {

    val connector = new TaxCalcConnector(
      http = mockHttpClient,
      appConfig = mockAppConfig
    )

    MockedAppConfig.desBaseUrl returns desBaseUrl
  }

  "getTaxCalculation" should {
    "return a TaxCalculation model" when {
      "the http parser returns a TaxCalculation model" in new Test {
        MockedHttpClient.get[TaxCalcOutcome](url)
          .returns(Future.successful(Right(TaxCalculationFixture.testTaxCalc)))

        val result: TaxCalcOutcome = await(connector.getTaxCalculation(nino, calculationId))
        result shouldBe Right(TaxCalculationFixture.testTaxCalc)
      }
    }

    "return an NotFound error" when {
      "the http parser returns a NotFound error" in new Test {
        MockedHttpClient.get[TaxCalcOutcome](url)
          .returns(Future.successful(Left(TaxCalcOutcome.NotFound)))

        val result: TaxCalcOutcome = await(connector.getTaxCalculation(nino, calculationId))
        result shouldBe Left(TaxCalcOutcome.NotFound)
      }
    }
  }
}
