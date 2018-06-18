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

package controllers

import mocks.services.MockEnrolmentsAuthService
import play.api.mvc.Result
import services.MtdIdLookupService
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class TaxCalcControllerSpec extends ControllerBaseSpec {

  class Test extends MockEnrolmentsAuthService {
    val hc = HeaderCarrier()
    val mockLookupService = mock[MtdIdLookupService]


    def controller: TaxCalcController = {
      (mockLookupService.lookup(_:String)(_: HeaderCarrier, _: ExecutionContext))
        .expects(nino, *, *)
        .returns(Future.successful(Right("test-mtd-id")))

      (mockEnrolmentsAuthService.authorised(_: Predicate)(_: HeaderCarrier, _: ExecutionContext))
        .expects(*, *, *)
        .returns(Future.successful(Right(true)))

      new TaxCalcController(
        authService = mockEnrolmentsAuthService,
        lookupService = mockLookupService
      )
    }
  }

  val nino = "test-nino"

  "getTaxCalculation" should {
    "return a 200" in new Test {
      val result: Future[Result] = controller.getTaxCalculation(nino)(hc)(fakeRequest)
      status(result) shouldBe OK
      contentAsString(result) shouldBe "test-mtd-id"
    }
  }
}
