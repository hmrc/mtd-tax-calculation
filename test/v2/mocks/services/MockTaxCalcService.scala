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

package v2.mocks.services

import org.scalamock.handlers.CallHandler
import org.scalamock.scalatest.MockFactory
import play.api.libs.json.Reads
import uk.gov.hmrc.http.HeaderCarrier
import v2.outcomes.TaxCalcOutcome.Outcome
import v2.services.TaxCalcService

import scala.concurrent.{ExecutionContext, Future}

trait MockTaxCalcService extends MockFactory {

  val mockTaxCalcService: TaxCalcService = mock[TaxCalcService]

  object MockedTaxCalcService {
    def getTaxCalculation[A: Reads](nino: String, calcId: String): CallHandler[Future[Outcome[A]]] = {
      (mockTaxCalcService.getTaxCalculation(_: String, _: String)(_: Reads[A], _: HeaderCarrier, _: ExecutionContext))
        .expects(nino, calcId, *, *, *)
    }

    def getTaxCalculationMessages[A: Reads](nino: String, calcId: String): CallHandler[Future[Outcome[A]]] = {
      (mockTaxCalcService.getTaxCalculationMessages(_: String, _: String)(_: Reads[A], _: HeaderCarrier, _: ExecutionContext))
        .expects(nino, calcId, *, *, *)
    }
  }

}
