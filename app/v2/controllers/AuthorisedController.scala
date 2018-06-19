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
import play.api.mvc._
import uk.gov.hmrc.auth.core.authorise.{EmptyPredicate, Predicate}
import uk.gov.hmrc.play.bootstrap.controller.BaseController
import v2.models.errors.AuthError
import v2.services.EnrolmentsAuthService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

abstract class AuthorisedController extends BaseController {

  val authService: EnrolmentsAuthService

  def authorisedAction(nino: String): ActionBuilder[MtdIdRequest] = {
    new ActionBuilder[MtdIdRequest] {
      override def invokeBlock[A](request: Request[A], block: MtdIdRequest[A] => Future[Result]): Future[Result] = {
        block(MtdIdRequest("test-mtd-id", request))
      }
    }
  }

  case class MtdIdRequest[A](mtdId: String, request: Request[A]) extends WrappedRequest[A](request)

  def authorisedAction(predicate: Predicate = EmptyPredicate)
                      (block: Request[AnyContent] => Future[Result]): Action[AnyContent] = Action.async {
    implicit request =>

      authService.authorised(predicate) flatMap {
        case Right(_) => block(request)
        case Left(AuthError(false, _)) => Future.successful(Unauthorized(Json.obj()))
        case Left(_) => Future.successful(Forbidden(Json.obj()))
      }
  }
}
