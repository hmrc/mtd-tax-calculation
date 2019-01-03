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
import v2.models.errors.{CalculationNotReady, InvalidCalcIDError, InvalidNinoError, MatchingResourceNotFound, NoContentReturned, InternalServerError => ISE}
import v2.models.{TaxCalcMessages, TaxCalculation}
import v2.outcomes.TaxCalcOutcome.Outcome
import v2.services.{EnrolmentsAuthService, MtdIdLookupService, TaxCalcService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class TaxCalcController @Inject()(val authService: EnrolmentsAuthService,
                                  val lookupService: MtdIdLookupService,
                                  val service: TaxCalcService) extends AuthorisedController {

  def getTaxCalculation(nino: String, calcId: String): Action[AnyContent] = authorisedAction(nino).async { implicit request =>
    get[TaxCalculation](nino,calcId)(service.getTaxCalculation(_,_)(hc,implicitly))
  }

  def getTaxCalculationMessages(nino: String, calcId: String): Action[AnyContent] = authorisedAction(nino).async { implicit request =>
    get[TaxCalcMessages](nino,calcId)(service.getTaxCalculationMessages(_,_)(hc,implicitly))
  }

  private def get[M:Writes](nino: String, c: String)
                          (f: (String, String) => Future[Outcome[M]]): Future[Result] = {
    f(nino,c).map {
      case Right(model) => Ok(toJson(model))
      case Left(mtdError) =>
        mtdError match {
          case CalculationNotReady | NoContentReturned => NoContent
          case InvalidCalcIDError | InvalidNinoError => BadRequest(toJson(mtdError))
          case MatchingResourceNotFound => NotFound(toJson(mtdError))
          case ISE => InternalServerError(toJson(mtdError))
        }
    }
  }
}
