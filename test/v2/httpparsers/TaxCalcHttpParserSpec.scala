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

package v2.httpparsers

import v2.fixtures.TaxCalculationFixture
import play.api.test.Helpers.OK
import support.UnitSpec
import uk.gov.hmrc.http.HttpResponse
import TaxCalcHttpParser.taxCalcHttpReads
import v2.outcomes.TaxCalcOutcome.TaxCalcOutcome

class TaxCalcHttpParserSpec extends UnitSpec {

  val method = "GET"
  val url = "test-url"

  "read" should {
    "return a TaxCalculation model" when {
      "the HttpResponse contains a 200 status and correct response body" in {
        val response = HttpResponse(OK, Some(TaxCalculationFixture.testTaxCalcJson))
        val result: TaxCalcOutcome = taxCalcHttpReads.read(method, url, response)

        result shouldBe Right(TaxCalculationFixture.testTaxCalc)
      }
    }
  }
}
