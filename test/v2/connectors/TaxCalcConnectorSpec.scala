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

package v2.connectors

import uk.gov.hmrc.http.HttpResponse
import v2.fixtures.{TaxCalcMessagesFixture, TaxCalculationFixture}
import v2.mocks.{MockAppConfig, MockHttpClient}
import v2.models.errors.{ErrorWrapper, MatchingResourceNotFound}
import v2.models.{TaxCalcMessages, TaxCalculation}
import v2.outcomes.DesResponse
import v2.outcomes.TaxCalcOutcome.Outcome

import scala.concurrent.Future

class TaxCalcConnectorSpec extends ConnectorSpec {

  val desBaseUrl = "test-base-url"
  val desEnv = "TestEnv"
  val desToken = "token"
  val mtdId = "123456789012345"
  val calculationId = "test-calc-id"
  val url = s"$desBaseUrl/income-tax/calculation-data/$mtdId/calcId/$calculationId"
  val correlationId = "X-123"

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
        MockedHttpClient.get[Outcome[TaxCalculation]](url)
          .returns(Future.successful(Right(DesResponse(correlationId, TaxCalculationFixture.taxCalc))))

        private val result = await(connector.getTaxCalculation[TaxCalculation](mtdId, calculationId))
        result shouldBe Right(DesResponse(correlationId, TaxCalculationFixture.taxCalc))
      }
    }

    "return an NotFound error" when {
      "the http parser returns a NotFound error" in new Test {
        MockedHttpClient.get[Outcome[TaxCalculation]](url)
          .returns(Future.successful(Left(ErrorWrapper(Some(correlationId), MatchingResourceNotFound, None))))

        private val result = await(connector.getTaxCalculation[TaxCalculation](mtdId, calculationId))
        result shouldBe Left(ErrorWrapper(Some(correlationId), MatchingResourceNotFound, None))
      }
    }
  }

  "getTaxCalculationMessages" should {
    "return a TaxCalculation model" when {
      "the http parser returns a TaxCalculation model" in new Test {
        MockedHttpClient.get[Outcome[TaxCalcMessages]](url)
          .returns(Future.successful(Right(DesResponse(correlationId, TaxCalcMessagesFixture.taxCalcMessages))))

        private val result = await(connector.getTaxCalculationMessages[TaxCalcMessages](mtdId, calculationId))
        result shouldBe Right(DesResponse(correlationId, TaxCalcMessagesFixture.taxCalcMessages))
      }
    }

    "return an NotFound error" when {
      "the http parser returns a NotFound error" in new Test {
        MockedHttpClient.get[Outcome[TaxCalcMessages]](url)
          .returns(Future.successful(Left(ErrorWrapper(Some(correlationId), MatchingResourceNotFound, None))))

        private val result = await(connector.getTaxCalculationMessages[TaxCalcMessages](mtdId, calculationId))
        result shouldBe Left(ErrorWrapper(Some(correlationId), MatchingResourceNotFound, None))
      }
    }
  }
}
