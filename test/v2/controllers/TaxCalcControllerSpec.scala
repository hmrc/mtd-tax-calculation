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

package v2.controllers

import play.api.mvc.Result
import uk.gov.hmrc.http.HeaderCarrier
import v2.fixtures.TaxCalculationFixture
import v2.mocks.services.{MockEnrolmentsAuthService, MockMtdIdLookupService, MockTaxCalcService}
import v2.models.errors.{Error, InvalidNino}
import v2.outcomes.MtdIdLookupOutcome.NotAuthorised

import scala.concurrent.Future

class TaxCalcControllerSpec extends ControllerBaseSpec {

  trait Test extends MockEnrolmentsAuthService with MockMtdIdLookupService with MockTaxCalcService{
    val hc = HeaderCarrier()

    val mtdId = "test-mtd-id"
    val calcId = "12345678"

    lazy val controller = new TaxCalcController(
      authService = mockEnrolmentsAuthService,
      service = mockTaxCalcService,
      lookupService = mockMtdIdLookupService)
  }

  val nino = "test-nino"

  "getTaxCalculation" should {
    "return a 200 with the correct body" in new Test {
      MockedEnrolmentsAuthService.authoriseUser()

      MockedMtdIdLookupService.lookup(nino)
        .returns(Future.successful(Right(mtdId)))

      MockedTaxCalcService.getTaxCalculation(mtdId, calcId)
        .returns(Future.successful(Right(TaxCalculationFixture.taxCalc)))

      val result: Future[Result] = controller.getTaxCalculation(nino, calcId)(fakeRequest)

      status(result) shouldBe OK
      contentAsJson(result) shouldBe TaxCalculationFixture.taxCalcJson
    }

    "return a 500" in new Test {

      MockedEnrolmentsAuthService.authoriseUser()

      MockedMtdIdLookupService.lookup(nino)
        .returns(Future.successful(Right(mtdId)))

      MockedTaxCalcService.getTaxCalculation(mtdId, calcId)
        .returns(Future.successful(Left(Error("",""))))

      val result: Future[Result] = controller.getTaxCalculation(nino, calcId)(fakeRequest)
      status(result) shouldBe INTERNAL_SERVER_ERROR
    }

    "return a 400" when {
      "a invalid NI number is passed" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Left(InvalidNino)))

        private val result = controller.getTaxCalculation(nino, "calcId")(fakeGetRequest)
        status(result) shouldBe BAD_REQUEST
      }
    }

    "return a 500" when {
      "the details passed or not authorised" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Left(NotAuthorised)))

        private val result = controller.getTaxCalculation(nino, "calcId")(fakeGetRequest)
        status(result) shouldBe FORBIDDEN
      }
    }
  }
}
