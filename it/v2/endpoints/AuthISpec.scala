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
 * WITHOUT WARRANTIED OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package v2.endpoints

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.api.http.Status
import play.api.libs.ws.{WSRequest, WSResponse}
import support.IntegrationBaseSpec
import v2.stubs.{AuthStub, MtdIdLookupStub, TaxCalcStub}

class AuthISpec extends IntegrationBaseSpec {

  private trait Test {
    val nino: String
    val mtdId: String = "XA123456789"
    val calcId: String = "12345678"
    def setupStubs(): StubMapping

    def request(): WSRequest = {
      setupStubs()
      val x = buildRequest(s"/2.0/ni/$nino/calculations/$calcId")
      x
    }
  }

  "Calling the get calc endpoint" when {

    "the NINO cannot be converted to a MTD ID" should {

      "return 500" in new Test {
        override val nino: String = "AA123456A"
        override def setupStubs(): StubMapping = {
          MtdIdLookupStub.ninoFound(nino)
          MtdIdLookupStub.internalServerError(nino)
        }

        val response: WSResponse = await(request().get())
        response.status shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }

    "an MTD ID is successfully retrieve from the NINO and the user is authorised" should {

      "return 200" in new Test {
        override val nino: String = "AA123456A"
        override def setupStubs(): StubMapping = {
          MtdIdLookupStub.ninoFound(nino, mtdId)
          AuthStub.authorised()
          TaxCalcStub.successfulTaxCalc(mtdId, calcId)
        }

        val response: WSResponse = await(request().get())
        response.status shouldBe Status.OK
      }
    }

    "an MTD ID is successfully retrieve from the NINO and the user is NOT logged in" should {

      "return 401" in new Test {
        override val nino: String = "AA123456A"
        override def setupStubs(): StubMapping = {
          MtdIdLookupStub.ninoFound(nino)
          AuthStub.unauthorisedNotLoggedIn()
        }

        val response: WSResponse = await(request().get())
        response.status shouldBe Status.UNAUTHORIZED
      }
    }

    "an MTD ID is successfully retrieve from the NINO and the user is NOT authorised" should {

      "return 403" in new Test {
        override val nino: String = "AA123456A"
        override def setupStubs(): StubMapping = {
          MtdIdLookupStub.ninoFound(nino)
          AuthStub.unauthorisedOther()
        }

        val response: WSResponse = await(request().get())
        response.status shouldBe Status.FORBIDDEN
      }
    }

  }

}
