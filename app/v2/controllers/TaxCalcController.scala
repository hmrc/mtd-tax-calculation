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

package v2.controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json.toJson
import play.api.libs.json.Writes
import play.api.mvc.{Action, AnyContent, Result}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditResult
import v2.config.{AppConfig, FeatureSwitch}
import v2.models._
import v2.models.audit._
import v2.models.auth.UserDetails
import v2.models.errors.{CalculationNotReady, Error, InvalidCalcIDError, InvalidNinoError, MatchingResourceNotFound, NoContentReturned, InternalServerError => ISE}
import v2.outcomes.TaxCalcOutcome.Outcome
import v2.services.{AuditService, EnrolmentsAuthService, MtdIdLookupService, TaxCalcService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TaxCalcController @Inject()(val authService: EnrolmentsAuthService,
                                  val lookupService: MtdIdLookupService,
                                  service: TaxCalcService,
                                  appConfig: AppConfig,
                                  auditService: AuditService
                                 ) extends AuthorisedController {

  private val featureSwitch = FeatureSwitch(appConfig.featureSwitch)

  def getTaxCalculation(nino: String, calcId: String): Action[AnyContent] = authorisedAction(nino).async { implicit request =>

    if (featureSwitch.isRelease2Enabled) {
      get[TaxCalculation](nino, calcId, request.userDetails)(service.getTaxCalculation(_, _))
    } else {
      get[old.TaxCalculation](nino, calcId, request.userDetails)(service.getTaxCalculation(_, _))
    }

  }

  def getTaxCalculationMessages(nino: String, calcId: String): Action[AnyContent] = authorisedAction(nino).async { implicit request =>

    if (featureSwitch.isRelease2Enabled) {
      get[TaxCalcMessages](nino, calcId, request.userDetails)(service.getTaxCalculationMessages(_, _))
    } else {
      get[old.TaxCalcMessages](nino, calcId, request.userDetails)(service.getTaxCalculationMessages(_, _))
    }


  }

  private def get[M: Writes](nino: String, calcId: String, userDetails: UserDetails)
                            (f: (String, String) => Future[Outcome[M]])(implicit hc: HeaderCarrier): Future[Result] = {
    f(nino, calcId).map {
      case outcome@Right(model) =>
        val auditDetails = createAuditDetails(nino, calcId, OK, ???, userDetails, outcome)
        auditSubmission(auditDetails)
        Ok(toJson(model))

      case outcome@Left(mtdError) =>
        val (status, result) = mtdError match {
          case CalculationNotReady | NoContentReturned => (NO_CONTENT, NoContent)
          case InvalidCalcIDError | InvalidNinoError => (BAD_REQUEST, BadRequest(toJson(mtdError)))
          case MatchingResourceNotFound => (NOT_FOUND, NotFound(toJson(mtdError)))
          case ISE => (INTERNAL_SERVER_ERROR, InternalServerError(toJson(mtdError)))
        }

        val auditDetails = createAuditDetails(nino, calcId, status, ???, userDetails, outcome)
        auditSubmission(auditDetails)

        result
    }
  }

  private def createAuditDetails[M](nino: String,
                                    calcId: String,
                                    statusCode: Int,
                                    correlationId: String,
                                    userDetails: UserDetails,
                                    outcome: Outcome[M]
                                   ): RetrieveTaxCalcAuditDetail[M] = {
    val response = outcome match {
      case Right(taxCalc) => RetrieveTaxCalcAuditResponse(statusCode, None, Some(taxCalc))
      case Left(error: Error) => RetrieveTaxCalcAuditResponse(statusCode, Some(Seq(AuditError(error.code))), None)
    }

    RetrieveTaxCalcAuditDetail(userDetails.userType, userDetails.agentReferenceNumber, nino, calcId, correlationId, response)
  }

  private def auditSubmission[M: Writes](details: RetrieveTaxCalcAuditDetail[M])
                                        (implicit hc: HeaderCarrier,
                                         ec: ExecutionContext): Future[AuditResult] = {
    val event = AuditEvent("retrieveTaxCalculation", "mtd-tax-calculation", details)
    auditService.auditEvent(event)
  }
}
