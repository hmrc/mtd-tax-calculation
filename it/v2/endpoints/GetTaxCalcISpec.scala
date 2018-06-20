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

class GetTaxCalcISpec extends IntegrationBaseSpec {

  private trait Test {

    val calcId = "12345678"
    val nino: String
    def setupStubs(): StubMapping

    def request(): WSRequest = {
      setupStubs()
      buildRequest(s"/2.0/self-assessment/ni/$nino/calculations/$calcId")
    }
  }

  "Calling the Get Tax Calculation endpoint" should {

    "return a 200 with a valid Tax Calculation response" when {

      //todo: change the description for this when the parser changes
      "any valid request is made" in new Test {
        override val nino: String = "AA123456A"
        override def setupStubs(): StubMapping = {
          AuthStub.authorised()
          TaxCalcStub.successfulTaxCalc("any-mtdid", calcId)
          MtdIdLookupStub.ninoFound(nino)
        }

        val response: WSResponse = await(request().get())
        response.status shouldBe Status.OK
      }
    }
  }
}
