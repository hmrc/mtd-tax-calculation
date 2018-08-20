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

package v2.connectors

import uk.gov.hmrc.http.HttpResponse
import v2.fixtures.TaxCalculationFixture
import v2.mocks.{MockAppConfig, MockHttpClient}
import v2.models.errors.MatchingResourceNotFound
import v2.outcomes.TaxCalcOutcome.TaxCalcOutcome

import scala.concurrent.Future

class TaxCalcConnectorSpec extends ConnectorSpec {

  val desBaseUrl = "test-base-url"
  val desEnv = "TestEnv"
  val desToken = "token"
  val mtdId = "123456789012345"
  val calculationId = "test-calc-id"
  val url = s"$desBaseUrl/income-tax/calculation-data/$mtdId/calcId/$calculationId"

  val httpResponseOk = HttpResponse(OK)
  val httpResponseNotFound = HttpResponse(NOT_FOUND)

  class Test extends MockHttpClient with MockAppConfig {

    val connector = new TaxCalcConnector(
      http = mockHttpClient,
      appConfig = mockAppConfig
    )

    MockedAppConfig.desBaseUrl returns desBaseUrl
    MockedAppConfig.desEnv returns desEnv
    MockedAppConfig.desToken returns desToken
  }

  "getTaxCalculation" should {
    "return a TaxCalculation model" when {
      "the http parser returns a TaxCalculation model" in new Test {
        MockedHttpClient.get[TaxCalcOutcome](url)
          .returns(Future.successful(Right(TaxCalculationFixture.v3_2ClientTaxCalc)))

        val result: TaxCalcOutcome = await(connector.getTaxCalculation(mtdId, calculationId))
        result shouldBe Right(TaxCalculationFixture.v3_2ClientTaxCalc)
      }
    }

    "return an NotFound error" when {
      "the http parser returns a NotFound error" in new Test {
        MockedHttpClient.get[TaxCalcOutcome](url)
          .returns(Future.successful(Left(MatchingResourceNotFound)))

        val result: TaxCalcOutcome = await(connector.getTaxCalculation(mtdId, calculationId))
        result shouldBe Left(MatchingResourceNotFound)
      }
    }
  }
}
