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

import models.ServiceResponse
import models.errors.AuthError
import play.api.http.Status
import services.EnrolmentsAuthService
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class HelloWorldControllerSpec extends ControllerBaseSpec {

  private trait Test {
    val authResult: ServiceResponse[AuthError, Boolean] = Future.successful(Right(true))

    val mockAuthService: EnrolmentsAuthService = mock[EnrolmentsAuthService]

    def setup(): Any = {
      (mockAuthService.authorised(_: Predicate)(_: HeaderCarrier, _: ExecutionContext))
        .expects(*, *, *)
        .returning(authResult)
    }

    lazy val target: HelloWorldController = {
      setup()
      new HelloWorldController(mockAuthService)
    }
  }

  "GET /" should {
    "return 200" in new Test {
      private val result = target.hello()(fakeRequest)
      status(result) shouldBe Status.OK
    }
  }

}
