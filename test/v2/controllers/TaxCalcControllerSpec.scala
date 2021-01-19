/*
 * Copyright 2021 HM Revenue & Customs
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
import v2.fixtures.{TaxCalcMessagesFixture, TaxCalculationFixture}
import v2.mocks.{MockAppConfig, MockIdGenerator}
import v2.mocks.services.{MockAuditService, MockEnrolmentsAuthService, MockMtdIdLookupService, MockTaxCalcService}
import v2.models.audit._
import v2.models.auth.UserDetails
import v2.models.errors._
import v2.models.{TaxCalcMessages, TaxCalculation}
import v2.outcomes.DesResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TaxCalcControllerSpec extends ControllerBaseSpec
  with MockEnrolmentsAuthService
  with MockMtdIdLookupService
  with MockTaxCalcService
  with MockAppConfig
  with MockAuditService
  with MockIdGenerator {

  trait Test {

    val hc: HeaderCarrier = HeaderCarrier()

    val mtdId = "test-mtd-id"
    val calcId = "12345678"
    val expected = Right(UserDetails("", "Individual", None))
    val correlationId = "X-123"

    lazy val controller = new TaxCalcController(
      authService = mockEnrolmentsAuthService,
      service = mockTaxCalcService,
      lookupService = mockMtdIdLookupService,
      appConfig = mockAppConfig,
      auditService = mockAuditService,
      cc = cc,
      idGenerator = mockIdGenerator
    )
  }

  val nino: String = "test-nino"

  "getTaxCalculation" should {
    "return a 200 with the correct body" in new Test {

      MockedMtdIdLookupService.lookup(nino)
        .returns(Future.successful(Right(mtdId)))

      MockedEnrolmentsAuthService.authoriseUser().returns(Future.successful(expected))

      MockIdGenerator.generateCorrelationId.returns(correlationId)

      MockedTaxCalcService.getTaxCalculation[TaxCalculation](nino, calcId)
        .returns(Future.successful(Right(DesResponse(correlationId, TaxCalculationFixture.taxCalc))))

      val result: Future[Result] = controller.getTaxCalculation(nino, calcId)(fakeRequest)

      status(result) shouldBe OK
      contentAsJson(result) shouldBe TaxCalculationFixture.taxCalcClientJson
      header("X-CorrelationId", result) shouldBe Some(correlationId)

      val detail: RetrieveTaxCalcAuditDetail =
        RetrieveTaxCalcAuditDetail("Individual", None, nino, calcId, "X-123",
          RetrieveTaxCalcAuditResponse(OK, None, Some(TaxCalculationFixture.taxCalcClientJson)))

      val event: AuditEvent[RetrieveTaxCalcAuditDetail] =
        AuditEvent("retrieveTaxCalculation", "mtd-tax-calculation", detail)

      MockedAuditService.verifyAuditEvent(event).once
    }

    "return a 204" when {
      "the calculation is not ready" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Right(mtdId)))

        MockedEnrolmentsAuthService.authoriseUser().returns(Future.successful(expected))

        MockIdGenerator.generateCorrelationId.returns(correlationId)

        MockedTaxCalcService.getTaxCalculation[TaxCalculation](nino, calcId)
          .returns(Future.successful(Left(ErrorWrapper(correlationId, CalculationNotReady, None))))

        private val result = controller.getTaxCalculation(nino, calcId)(fakeRequest)

        status(result) shouldBe NO_CONTENT
        header("X-CorrelationId", result) shouldBe Some(correlationId)
      }
    }

    "return a 400" when {
      "a invalid NI number is passed" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Left(InvalidNinoError)))

        private val result = controller.getTaxCalculation(nino, "calcId")(fakeGetRequest)
        status(result) shouldBe BAD_REQUEST
      }

      "the service returns an Invalid Identifier error" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Right(mtdId)))

        MockedEnrolmentsAuthService.authoriseUser().returns(Future.successful(expected))

        MockIdGenerator.generateCorrelationId.returns(correlationId)

        MockedTaxCalcService.getTaxCalculation[TaxCalculation](nino, calcId)
          .returns(Future.successful(Left(ErrorWrapper(correlationId, InvalidNinoError, None))))

        val result: Future[Result] = controller.getTaxCalculation(nino, calcId)(fakeRequest)

        status(result) shouldBe BAD_REQUEST
        contentAsJson(result) shouldBe Json.toJson(InvalidNinoError)
        header("X-CorrelationId", result).nonEmpty shouldBe true

        val detail: RetrieveTaxCalcAuditDetail =
          RetrieveTaxCalcAuditDetail("Individual", None, nino, calcId, header("X-CorrelationId", result).get,
            RetrieveTaxCalcAuditResponse(BAD_REQUEST, Some(Seq(AuditError(InvalidNinoError.code))), None))

        val event: AuditEvent[RetrieveTaxCalcAuditDetail] =
          AuditEvent("retrieveTaxCalculation", "mtd-tax-calculation", detail)

        MockedAuditService.verifyAuditEvent(event).once
      }

      "the service returns an Invalid CalcID error" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Right(mtdId)))

        MockedEnrolmentsAuthService.authoriseUser().returns(Future.successful(expected))

        MockIdGenerator.generateCorrelationId.returns(correlationId)

        MockedTaxCalcService.getTaxCalculation[TaxCalculation](nino, calcId)
          .returns(Future.successful(Left(ErrorWrapper(correlationId, InvalidCalcIDError, None))))

        val result: Future[Result] = controller.getTaxCalculation(nino, calcId)(fakeRequest)

        status(result) shouldBe BAD_REQUEST
        contentAsJson(result) shouldBe Json.toJson(InvalidCalcIDError)
        header("X-CorrelationId", result).nonEmpty shouldBe true

        val detail: RetrieveTaxCalcAuditDetail =
          RetrieveTaxCalcAuditDetail("Individual", None, nino, calcId, header("X-CorrelationId", result).get,
            RetrieveTaxCalcAuditResponse(BAD_REQUEST, Some(Seq(AuditError(InvalidCalcIDError.code))), None))

        val event: AuditEvent[RetrieveTaxCalcAuditDetail] =
          AuditEvent("retrieveTaxCalculation", "mtd-tax-calculation", detail)

        MockedAuditService.verifyAuditEvent(event).once
      }
    }

    "return a 404" when {
      "the service returns a NotFound error" in new Test {

        MockedEnrolmentsAuthService.authoriseUser().returns(Future.successful(expected))

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Right(mtdId)))

        MockIdGenerator.generateCorrelationId.returns(correlationId)

        MockedTaxCalcService.getTaxCalculation[TaxCalculation](nino, calcId)
          .returns(Future.successful(Left(ErrorWrapper(correlationId, MatchingResourceNotFound, None))))

        val result: Future[Result] = controller.getTaxCalculation(nino, calcId)(fakeRequest)

        status(result) shouldBe NOT_FOUND
        contentAsJson(result) shouldBe Json.toJson(MatchingResourceNotFound)
        header("X-CorrelationId", result) shouldBe Some(correlationId)

        val detail: RetrieveTaxCalcAuditDetail =
          RetrieveTaxCalcAuditDetail("Individual", None, nino, calcId, correlationId,
            RetrieveTaxCalcAuditResponse(NOT_FOUND, Some(Seq(AuditError(MatchingResourceNotFound.code))), None))

        val event: AuditEvent[RetrieveTaxCalcAuditDetail] =
          AuditEvent("retrieveTaxCalculation", "mtd-tax-calculation", detail)

        MockedAuditService.verifyAuditEvent(event).once
      }
    }

    "return a 500" when {
      "the details passed are not authorised" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Left(UnauthorisedError)))

        private val result = controller.getTaxCalculation(nino, "calcId")(fakeGetRequest)
        status(result) shouldBe FORBIDDEN
      }

      "the service returns an InternalServerError response" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Right(mtdId)))

        MockedEnrolmentsAuthService.authoriseUser().returns(Future.successful(expected))

        MockIdGenerator.generateCorrelationId.returns(correlationId)

        MockedTaxCalcService.getTaxCalculation[TaxCalculation](nino, calcId)
          .returns(Future.successful(Left(ErrorWrapper(correlationId, InternalServerError, None))))

        val result: Future[Result] = controller.getTaxCalculation(nino, calcId)(fakeRequest)

        status(result) shouldBe INTERNAL_SERVER_ERROR
        contentAsJson(result) shouldBe Json.toJson(InternalServerError)
        header("X-CorrelationId", result) shouldBe Some(correlationId)

        val detail: RetrieveTaxCalcAuditDetail =
          RetrieveTaxCalcAuditDetail("Individual", None, nino, calcId, "X-123",
            RetrieveTaxCalcAuditResponse(INTERNAL_SERVER_ERROR, Some(Seq(AuditError(InternalServerError.code))), None))

        val event: AuditEvent[RetrieveTaxCalcAuditDetail] =
          AuditEvent("retrieveTaxCalculation", "mtd-tax-calculation", detail)

        MockedAuditService.verifyAuditEvent(event).once
      }
    }
  }

  "getTaxCalculationMessages" should {
    "return a 200 with the correct body" in new Test {

      MockedMtdIdLookupService.lookup(nino)
        .returns(Future.successful(Right(mtdId)))

      MockedEnrolmentsAuthService.authoriseUser().returns(Future.successful(expected))

      MockIdGenerator.generateCorrelationId.returns(correlationId)

      MockedTaxCalcService.getTaxCalculationMessages[TaxCalcMessages](nino, calcId)
        .returns(Future.successful(Right(DesResponse(correlationId, TaxCalcMessagesFixture.taxCalcMessages))))

      val result: Future[Result] = controller.getTaxCalculationMessages(nino, calcId)(fakeRequest)

      status(result) shouldBe OK
      contentAsJson(result) shouldBe TaxCalcMessagesFixture.taxCalcMessagesWithWarningsAndErrors
      header("X-CorrelationId", result) shouldBe Some(correlationId)
    }

    "return a 204" when {
      "the calculation is not ready" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Right(mtdId)))

        MockedEnrolmentsAuthService.authoriseUser().returns(Future.successful(expected))

        MockIdGenerator.generateCorrelationId.returns(correlationId)

        MockedTaxCalcService.getTaxCalculationMessages[TaxCalcMessages](nino, calcId)
          .returns(Future.successful(Left(ErrorWrapper(correlationId, CalculationNotReady, None))))

        private val result = controller.getTaxCalculationMessages(nino, calcId)(fakeRequest)

        status(result) shouldBe NO_CONTENT
        header("X-CorrelationId", result) shouldBe Some(correlationId)
      }
      "the result contains no validation messages" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Right(mtdId)))

        MockedEnrolmentsAuthService.authoriseUser().returns(Future.successful(expected))

        MockIdGenerator.generateCorrelationId.returns(correlationId)

        MockedTaxCalcService.getTaxCalculationMessages[TaxCalcMessages](nino, calcId)
          .returns(Future.successful(Left(ErrorWrapper(correlationId, NoContentReturned, None))))

        private val result = controller.getTaxCalculationMessages(nino, calcId)(fakeRequest)

        status(result) shouldBe NO_CONTENT
        header("X-CorrelationId", result) shouldBe Some(correlationId)
      }
    }

    "return a 400" when {
      "a invalid NI number is passed" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Left(InvalidNinoError)))

        private val result = controller.getTaxCalculationMessages(nino, "calcId")(fakeGetRequest)
        status(result) shouldBe BAD_REQUEST
      }

      "the service returns an Invalid Identifier error" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Right(mtdId)))

        MockedEnrolmentsAuthService.authoriseUser().returns(Future.successful(expected))

        MockIdGenerator.generateCorrelationId.returns(correlationId)

        MockedTaxCalcService.getTaxCalculationMessages[TaxCalcMessages](nino, calcId)
          .returns(Future.successful(Left(ErrorWrapper(correlationId, InvalidNinoError, None))))

        val result: Future[Result] = controller.getTaxCalculationMessages(nino, calcId)(fakeRequest)

        status(result) shouldBe BAD_REQUEST
        contentAsJson(result) shouldBe Json.toJson(InvalidNinoError)
        header("X-CorrelationId", result).nonEmpty shouldBe true
      }

      "the service returns an Invalid CalcID error" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Right(mtdId)))

        MockedEnrolmentsAuthService.authoriseUser().returns(Future.successful(expected))

        MockIdGenerator.generateCorrelationId.returns(correlationId)

        MockedTaxCalcService.getTaxCalculationMessages[TaxCalcMessages](nino, calcId)
          .returns(Future.successful(Left(ErrorWrapper(correlationId, InvalidCalcIDError, None))))

        val result: Future[Result] = controller.getTaxCalculationMessages(nino, calcId)(fakeRequest)

        status(result) shouldBe BAD_REQUEST
        contentAsJson(result) shouldBe Json.toJson(InvalidCalcIDError)
        header("X-CorrelationId", result).nonEmpty shouldBe true
      }
    }

    "return a 404" when {
      "the service returns a NotFound error" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Right(mtdId)))

        MockedEnrolmentsAuthService.authoriseUser().returns(Future.successful(expected))

        MockIdGenerator.generateCorrelationId.returns(correlationId)

        MockedTaxCalcService.getTaxCalculationMessages[TaxCalcMessages](nino, calcId)
          .returns(Future.successful(Left(ErrorWrapper(correlationId, MatchingResourceNotFound, None))))

        val result: Future[Result] = controller.getTaxCalculationMessages(nino, calcId)(fakeRequest)

        status(result) shouldBe NOT_FOUND
        contentAsJson(result) shouldBe Json.toJson(MatchingResourceNotFound)
        header("X-CorrelationId", result) shouldBe Some(correlationId)
      }
    }

    "return a 500" when {
      "the details passed are not authorised" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Left(UnauthorisedError)))

        private val result = controller.getTaxCalculationMessages(nino, "calcId")(fakeGetRequest)
        status(result) shouldBe FORBIDDEN
      }

      "the service returns an InternalServerError response" in new Test {

        MockedMtdIdLookupService.lookup(nino)
          .returns(Future.successful(Right(mtdId)))

        MockedEnrolmentsAuthService.authoriseUser().returns(Future.successful(expected))

        MockIdGenerator.generateCorrelationId.returns(correlationId)

        MockedTaxCalcService.getTaxCalculationMessages[TaxCalcMessages](nino, calcId)
          .returns(Future.successful(Left(ErrorWrapper(correlationId, InternalServerError, None))))

        val result: Future[Result] = controller.getTaxCalculationMessages(nino, calcId)(fakeRequest)

        status(result) shouldBe INTERNAL_SERVER_ERROR
        contentAsJson(result) shouldBe Json.toJson(InternalServerError)
      }
    }
  }

}