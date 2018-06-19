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

package v2.services

import javax.inject.{Inject, Singleton}
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.{AuthConnector, AuthorisationException, AuthorisedFunctions, MissingBearerToken}
import uk.gov.hmrc.http.HeaderCarrier
import v2.models.ServiceResponse
import v2.models.errors.AuthError

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EnrolmentsAuthService @Inject()(val connector: AuthConnector) {

  private val authFunction: AuthorisedFunctions = new AuthorisedFunctions {
    override def authConnector: AuthConnector = connector
  }

  def authorised(predicate: Predicate)(implicit hc: HeaderCarrier, ec: ExecutionContext): ServiceResponse[AuthError, Boolean] = {
    authFunction.authorised(predicate) {
      Future.successful(Right(true))
    } recoverWith {
      case _: MissingBearerToken => Future.successful(Left(AuthError()))
      case _: AuthorisationException => Future.successful(Left(AuthError(authenticated = true)))
    }
  }
}
