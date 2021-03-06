/*
 * Copyright 2021 HM Revenue & Customs
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

package v2.endpoints

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.api.http.Status
import play.api.libs.json.Json
import play.api.libs.ws.{WSRequest, WSResponse}
import support.IntegrationBaseSpec
import v2.fixtures.TaxCalcMessagesFixture
import v2.models.errors.InternalServerError
import v2.stubs.{AuthStub, MtdIdLookupStub, TaxCalcMessagesStub}

class GetTaxCalcMessagesISpec extends IntegrationBaseSpec {

  private trait Test {

    val calcId: String = "12345678"
    val mtdId: String  = "XA123456789"
    val nino: String
    def setupStubs(): StubMapping

    def request(): WSRequest = {
      setupStubs()
      buildRequest(s"/2.0/ni/$nino/calculations/$calcId/validation-messages")
    }
  }

  "Calling the Get Tax Calculation Messages endpoint" should {

    "return a 200 with a valid Tax Calculation Message response" when {

      "DES returns a valid Tax Calculation" in new Test {
        override val nino: String = "AA123456A"
        override def setupStubs(): StubMapping = {
          AuthStub.authorised()
          MtdIdLookupStub.ninoFound(nino, mtdId)
          TaxCalcMessagesStub.successfulTaxCalcMessage(nino, calcId)
        }

        val response: WSResponse = await(request().get())
        response.status shouldBe Status.OK
        response.json shouldBe TaxCalcMessagesFixture.taxCalcMessagesWithWarningsAndErrors
      }
    }

    "return a 500" when {
      "DES returns a ServerError" in new Test {
        override val nino: String = "AA123456A"
        override def setupStubs(): StubMapping = {
          AuthStub.authorised()
          MtdIdLookupStub.ninoFound(nino, mtdId)
          TaxCalcMessagesStub.unsuccessfulTaxCalcMessage(nino, calcId)
        }

        val response: WSResponse = await(request().get())
        response.status shouldBe Status.INTERNAL_SERVER_ERROR
        response.json shouldBe Json.toJson(InternalServerError)
      }
    }
  }
}
