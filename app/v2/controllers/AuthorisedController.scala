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
import uk.gov.hmrc.auth.core.Enrolment
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.controller.BaseController
import v2.models.errors.{AuthError, InvalidNinoError, MtdError, UnauthorisedError}
import v2.outcomes.MtdIdLookupOutcome._
import v2.services.{EnrolmentsAuthService, MtdIdLookupService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

abstract class AuthorisedController extends BaseController {

  val authService: EnrolmentsAuthService
  val lookupService: MtdIdLookupService

  case class UserRequest[A](mtdId: String, request: Request[A]) extends WrappedRequest[A](request)

  def authorisedAction(nino: String): ActionBuilder[UserRequest] = new ActionBuilder[UserRequest] {

    def predicate(mtdId: String): Predicate = Enrolment("HMRC-MTD-IT")
      .withIdentifier("MTDITID", mtdId)
      .withDelegatedAuthRule("mtd-it-auth")

    def invokeBlockWithAuthCheck[A](mtdId: String,
                                    request: Request[A],
                                    block: UserRequest[A] => Future[Result])
                                   (implicit headerCarrier: HeaderCarrier): Future[Result] = {
      authService.authorised(predicate(mtdId)).flatMap[Result] {
        case Right(_) => block(UserRequest(mtdId, request))
        case Left(UnauthorisedError) => Future.successful(Forbidden(Json.toJson(Unauthorised.error)))
        case Left(_) => Future.successful(Forbidden(Json.toJson(Unauthorised.error)))
      }
    }

    override def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] = {

      implicit val headerCarrier: HeaderCarrier = hc(request)

      lookupService.lookup(nino).flatMap[Result] {
        case Right(mtdId) => invokeBlockWithAuthCheck(mtdId, request, block)
        case Left(InvalidNinoError) => Future.successful(BadRequest(Json.toJson(InvalidNinoError)))
        case Left(UnauthorisedError) => Future.successful(Forbidden(Json.toJson(Unauthorised.error)))
        case Left(DownstreamError) => Future.successful(InternalServerError(Json.toJson(DownstreamError.error)))
      }
    }
  }
}
