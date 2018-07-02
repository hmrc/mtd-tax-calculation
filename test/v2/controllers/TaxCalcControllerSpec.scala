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

import play.api.libs.json.Json
import play.api.mvc.Result
import uk.gov.hmrc.http.HeaderCarrier
import v2.fixtures.TaxCalculationFixture
import v2.mocks.services.{MockEnrolmentsAuthService, MockMtdIdLookupService, MockTaxCalcService}
import v2.models.errors.{InternalServerError, InvalidCalcID, InvalidNino, NotFound}
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

    "return a 400" when {
      "a invalid NI number is passed" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Left(InvalidNino)))

        private val result = controller.getTaxCalculation(nino, "calcId")(fakeGetRequest)
        status(result) shouldBe BAD_REQUEST
      }
      "the service returns an Invalid Identifier error" in new Test {
        MockedEnrolmentsAuthService.authoriseUser()

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Right(mtdId)))

        MockedTaxCalcService.getTaxCalculation(mtdId, calcId)
          .returns(Future.successful(Left(InvalidNino)))

        val result: Future[Result] = controller.getTaxCalculation(nino, calcId)(fakeRequest)

        status(result) shouldBe BAD_REQUEST
        contentAsJson(result) shouldBe Json.toJson(InvalidNino)
      }
      "the service returns an Invalid CalcID error" in new Test {
        MockedEnrolmentsAuthService.authoriseUser()

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Right(mtdId)))

        MockedTaxCalcService.getTaxCalculation(mtdId, calcId)
          .returns(Future.successful(Left(InvalidCalcID)))

        val result: Future[Result] = controller.getTaxCalculation(nino, calcId)(fakeRequest)

        status(result) shouldBe BAD_REQUEST
        contentAsJson(result) shouldBe Json.toJson(InvalidCalcID)
      }
    }

    "return a 404" when {
      "the service returns a NotFound error" in new Test {
        MockedEnrolmentsAuthService.authoriseUser()

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Right(mtdId)))

        MockedTaxCalcService.getTaxCalculation(mtdId, calcId)
          .returns(Future.successful(Left(NotFound)))

        val result: Future[Result] = controller.getTaxCalculation(nino, calcId)(fakeRequest)

        status(result) shouldBe NOT_FOUND
        await(result).body.isKnownEmpty shouldBe true
      }
    }

    "return a 500" when {
      "the details passed or not authorised" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Left(NotAuthorised)))

        private val result = controller.getTaxCalculation(nino, "calcId")(fakeGetRequest)
        status(result) shouldBe FORBIDDEN
      }
      "the service returns an InternalServerError response" in new Test {
        MockedEnrolmentsAuthService.authoriseUser()

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Right(mtdId)))

        MockedTaxCalcService.getTaxCalculation(mtdId, calcId)
          .returns(Future.successful(Left(InternalServerError)))

        val result: Future[Result] = controller.getTaxCalculation(nino, calcId)(fakeRequest)

        status(result) shouldBe INTERNAL_SERVER_ERROR
        contentAsJson(result) shouldBe Json.toJson(InternalServerError)
      }
    }
  }
}