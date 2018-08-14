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

package v2.stubs

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.api.http.Status._
import play.api.libs.json.JsValue
import support.WireMockMethods
import v2.fixtures.{DESErrorsFixture, TaxCalculationFixture => TestData}

object TaxCalcStub extends WireMockMethods {

  private val taxCalcUri: (String, String) => String = (mtdId, calcId) => s"/income-tax/calculation-data/$mtdId/calcId/$calcId"

  def successfulTaxCalc(mtdId: String, calcId: String): StubMapping = {
    when(method = GET, uri = taxCalcUri(mtdId, calcId))
      .thenReturn(status = OK, body = successfulTaxCalcResponse)
  }

  def unsuccessfulTaxCalc(mtdId: String, calcId: String): StubMapping = {
    when(method = GET, uri = taxCalcUri(mtdId, calcId))
      .thenReturn(status = INTERNAL_SERVER_ERROR, body = unsuccessfulTaxCalcResponse)
  }

  lazy val successfulTaxCalcResponse: JsValue = {
    TestData.v3_2DesTaxCalcJson
  }
  lazy val unsuccessfulTaxCalcResponse: JsValue = {
    DESErrorsFixture.serverErrorJson
  }
}
