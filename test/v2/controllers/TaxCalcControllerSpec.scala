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

import mocks.services.{MockEnrolmentsAuthService, MockMtdIdLookupService}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.Future

class TaxCalcControllerSpec extends ControllerBaseSpec {

  trait Test extends MockEnrolmentsAuthService with MockMtdIdLookupService {
    val hc = HeaderCarrier()

    lazy val target = new TaxCalcController(
      authService = mockEnrolmentsAuthService,
      lookupService = mockMtdIdLookupService
    )
  }

  val nino = "test-nino"

  "getTaxCalculation" should {
    "return a 200" in new Test {

      MockedMtdIdLookupService.lookup(nino)
        .returns(Future.successful(Right("test-mtd-id")))

      MockedEnrolmentsAuthService.authoriseUser()

      private val result = target.getTaxCalculation(nino)(hc)(fakeGetRequest)
      status(result) shouldBe OK
      contentAsString(result) shouldBe "test-mtd-id"
    }
  }
}
