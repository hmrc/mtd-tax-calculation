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

import models.errors.AuthError
import outcomes.MtdIdLookupOutcome._
import play.api.libs.json.Json
import play.api.mvc._
import services.{EnrolmentsAuthService, MtdIdLookupService}
import uk.gov.hmrc.auth.core.Enrolment
import uk.gov.hmrc.auth.core.authorise.{EmptyPredicate, Predicate}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.controller.BaseController

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

abstract class AuthorisedController extends BaseController {

  val authService: EnrolmentsAuthService
  val lookupService: MtdIdLookupService

  case class UserRequest[A](mtdId: String, request: Request[A]) extends WrappedRequest[A](request)

  def authorisedAction(nino: String)(implicit hc: HeaderCarrier): ActionBuilder[UserRequest] = new ActionBuilder[UserRequest] {

    def predicate(mtdId: String): Predicate = Enrolment("HMRC-MTD-IT")
      .withIdentifier("MTDITID", mtdId)
      .withDelegatedAuthRule("mtd-it-auth")

    def invokeBlockWithAuthCheck[A](mtdId: String,
                                    request: Request[A],
                                    block: UserRequest[A] => Future[Result]): Future[Result] = {

      authService.authorised(predicate(mtdId)).flatMap[Result] {
        case Right(_) => block(UserRequest(mtdId, request))
        case Left(AuthError(false, _)) => Future.successful(Unauthorized(Json.obj()))
        case Left(_) => Future.successful(Forbidden(Json.obj()))
      }
    }

    override def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] = {
      lookupService.lookup(nino).flatMap[Result] {
        case Right(mtdId) => invokeBlockWithAuthCheck(mtdId, request, block)
        case Left(InvalidNino) => Future.successful(BadRequest(""))
        case Left(NotAuthorised) => Future.successful(Forbidden(""))
        case Left(DownstreamError) => Future.successful(InternalServerError(""))
      }
    }
  }

  def authAction(predicate: Predicate = EmptyPredicate)
                (block: Request[AnyContent] => Future[Result]): Action[AnyContent] = Action.async {
    implicit request =>

      authService.authorised(predicate) flatMap {
        case Right(_) => block(request)
        case Left(AuthError(false, _)) => Future.successful(Unauthorized(Json.obj()))
        case Left(_) => Future.successful(Forbidden(Json.obj()))
      }
  }
}
