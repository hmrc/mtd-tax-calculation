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

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json.toJson
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.http.logging.MdcLoggingExecutionContext._
import v2.models.errors.{InvalidCalcID, InvalidNino, CalculationNotReady, InternalServerError => ISE, NotFound => Not_Found}
import v2.services.{EnrolmentsAuthService, MtdIdLookupService, TaxCalcService}

@Singleton
class TaxCalcController @Inject()(val authService: EnrolmentsAuthService,
                                  val lookupService: MtdIdLookupService,
                                  val service: TaxCalcService) extends AuthorisedController {

  def getTaxCalculation(nino: String, calcId: String): Action[AnyContent] = authorisedAction(nino).async { implicit request =>
    service.getTaxCalculation(nino, calcId).map {
      case Right(taxCalculation) => Ok(toJson(taxCalculation))
      case Left(mtdError) =>
        mtdError match {
          case CalculationNotReady => NoContent
          case InvalidCalcID | InvalidNino => BadRequest(toJson(mtdError))
          case Not_Found => NotFound
          case ISE => InternalServerError(toJson(mtdError))
        }
    }
  }
}
