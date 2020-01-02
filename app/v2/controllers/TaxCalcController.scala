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

package v2.controllers

import java.util.UUID

import javax.inject.{Inject, Singleton}
import play.api.Logger
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

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TaxCalcController @Inject()(val authService: EnrolmentsAuthService,
                                  val lookupService: MtdIdLookupService,
                                  service: TaxCalcService,
                                  appConfig: AppConfig,
                                  auditService: AuditService,
                                  cc: ControllerComponents
                                 )(implicit ec: ExecutionContext) extends AuthorisedController(cc) {

  val logger: Logger = Logger(this.getClass)

  def getTaxCalculation(nino: String, calcId: String): Action[AnyContent] = authorisedAction(nino).async { implicit request =>

    get[TaxCalculation](nino, calcId, request.userDetails, isAudit = true)(service.getTaxCalculation(_, _))

  }

  def getTaxCalculationMessages(nino: String, calcId: String): Action[AnyContent] = authorisedAction(nino).async { implicit request =>

    get[TaxCalcMessages](nino, calcId, request.userDetails, isAudit = false)(service.getTaxCalculationMessages(_, _))

  }

  private def get[M:Writes](nino: String, calcId: String, userDetails: UserDetails, isAudit: Boolean)
                           (f: (String, String) => Future[Outcome[M]])(implicit hc: HeaderCarrier): Future[Result] = {
    f(nino,calcId).map {
      case Right(v2.outcomes.DesResponse(correlationId, responseData)) =>
        if (isAudit) {
          auditSubmission(RetrieveTaxCalcAuditDetail(userDetails.userType, userDetails.agentReferenceNumber,
            nino, calcId, correlationId, RetrieveTaxCalcAuditResponse(OK, None, Some(toJson(responseData)))))
        }
        Ok(toJson(responseData)).withHeaders("X-CorrelationId" -> correlationId)
      case Left(errorWrapper) =>
        val correlationId = getCorrelationId(errorWrapper)
        val result = processError(errorWrapper).withHeaders("X-CorrelationId" -> correlationId)
        if (isAudit) {
          auditSubmission(RetrieveTaxCalcAuditDetail(userDetails.userType, userDetails.agentReferenceNumber,
            nino, calcId, correlationId,
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

  private def getCorrelationId(errorWrapper: ErrorWrapper): String = {
    errorWrapper.correlationId match {
      case Some(correlationId) => logger.info("[TaxCalcController][getCorrelationId] - " +
        s"Error received from DES ${Json.toJson(errorWrapper)} with CorrelationId: $correlationId")
        correlationId
      case None =>
        val correlationId = UUID.randomUUID().toString
        logger.info("[TaxCalcController][getCorrelationId] - " +
          s"Validation error: ${Json.toJson(errorWrapper)} with CorrelationId: $correlationId")
        correlationId
    }
  }

  private def auditSubmission[M: Writes](details: RetrieveTaxCalcAuditDetail)
                                        (implicit hc: HeaderCarrier,
                                         ec: ExecutionContext): Future[AuditResult] = {
    val event = AuditEvent("retrieveTaxCalculation", "mtd-tax-calculation", details)
    auditService.auditEvent(event)
  }
}
