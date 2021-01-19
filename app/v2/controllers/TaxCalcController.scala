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

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json.toJson
import play.api.libs.json.{Json, Writes}
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditResult
import v2.config.AppConfig
import v2.models._
import v2.models.audit._
import v2.models.auth.UserDetails
import v2.models.errors._
import v2.outcomes.TaxCalcOutcome.Outcome
import v2.services.{AuditService, EnrolmentsAuthService, MtdIdLookupService, TaxCalcService}
import v2.utils.{IdGenerator, Logging}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TaxCalcController @Inject()(val authService: EnrolmentsAuthService,
                                  val lookupService: MtdIdLookupService,
                                  service: TaxCalcService,
                                  appConfig: AppConfig,
                                  auditService: AuditService,
                                  cc: ControllerComponents,
                                  val idGenerator: IdGenerator)(implicit ec: ExecutionContext)
  extends AuthorisedController(cc) with Logging {

  implicit val endpointLogContext: EndpointLogContext =
    EndpointLogContext(
      controllerName = "TaxCalcController",
      endpointName = "retrieveTaxCalculation"
    )

  def getTaxCalculation(nino: String, calcId: String): Action[AnyContent] = authorisedAction(nino).async { implicit request =>

    implicit val correlationId: String = idGenerator.generateCorrelationId
    logger.info(
      s"[${endpointLogContext.controllerName}][${endpointLogContext.endpointName}] " +
        s"with CorrelationId: $correlationId")

    get[TaxCalculation](nino, calcId, request.userDetails, isAudit = true)(service.getTaxCalculation(_, _))

  }

  def getTaxCalculationMessages(nino: String, calcId: String): Action[AnyContent] = authorisedAction(nino).async { implicit request =>

    implicit val endpointLogContext: EndpointLogContext =
      EndpointLogContext(
        controllerName = "TaxCalcController",
        endpointName = "retrieveTaxCalculationMessages"
      )

    implicit val correlationId: String = idGenerator.generateCorrelationId
    logger.info(
      s"[${endpointLogContext.controllerName}][${endpointLogContext.endpointName}] " +
        s"with CorrelationId: $correlationId")

    get[TaxCalcMessages](nino, calcId, request.userDetails, isAudit = false)(service.getTaxCalculationMessages(_, _))

  }

  private def get[M:Writes](nino: String, calcId: String, userDetails: UserDetails, isAudit: Boolean)
                           (f: (String, String) => Future[Outcome[M]])(implicit hc: HeaderCarrier): Future[Result] = {

    f(nino,calcId).map {
      case Right(v2.outcomes.DesResponse(resultCorrelationId, responseData)) =>
        logger.info(s"[${endpointLogContext.controllerName}][${endpointLogContext.endpointName}]" +
          s" - Success response received with CorrelationId: $resultCorrelationId")
        if (isAudit) {
          auditSubmission(RetrieveTaxCalcAuditDetail(userDetails.userType, userDetails.agentReferenceNumber,
            nino, calcId, resultCorrelationId, RetrieveTaxCalcAuditResponse(OK, None, Some(toJson(responseData)))))
        }
        Ok(toJson(responseData)).withHeaders("X-CorrelationId" -> resultCorrelationId)
      case Left(errorWrapper) =>
        val resCorrelationId = errorWrapper.correlationId
        val result = processError(errorWrapper).withHeaders("X-CorrelationId" -> resCorrelationId)
        logger.info(
          s"[${endpointLogContext.controllerName}][${endpointLogContext.endpointName}] - " +
            s"Error response received with CorrelationId: $resCorrelationId")
        if (isAudit) {
          auditSubmission(RetrieveTaxCalcAuditDetail(userDetails.userType, userDetails.agentReferenceNumber,
            nino, calcId, resCorrelationId,
            RetrieveTaxCalcAuditResponse(result.header.status, Some(errorWrapper.allErrors.map(error => AuditError(error.code))), None)))
        }
        result
    }
  }

  private def processError(errorWrapper: ErrorWrapper): Result = {
    errorWrapper.error match {
      case InvalidCalcIDError | InvalidNinoError => BadRequest(Json.toJson(errorWrapper))
      case CalculationNotReady | NoContentReturned => NoContent
      case MatchingResourceNotFound => NotFound(Json.toJson(errorWrapper))
      case _ => InternalServerError(Json.toJson(errorWrapper))
    }
  }

  private def auditSubmission[M: Writes](details: RetrieveTaxCalcAuditDetail)
                                        (implicit hc: HeaderCarrier,
                                         ec: ExecutionContext): Future[AuditResult] = {
    val event = AuditEvent("retrieveTaxCalculation", "mtd-tax-calculation", details)
    auditService.auditEvent(event)
  }
}