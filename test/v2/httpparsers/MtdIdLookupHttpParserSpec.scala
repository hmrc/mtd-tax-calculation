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

import play.api.libs.json.Writes.StringWrites
import play.api.libs.json.{JsObject, Json}
import play.api.test.Helpers.{FORBIDDEN, INTERNAL_SERVER_ERROR, OK}
import support.UnitSpec
import uk.gov.hmrc.http.HttpResponse
import v2.httpparsers.MtdIdLookupHttpParser.mtdIdLookupHttpReads
import v2.models.errors.InvalidNino
import v2.outcomes.MtdIdLookupOutcome.{DownstreamError, MtdIdLookupOutcome}

class MtdIdLookupHttpParserSpec extends UnitSpec {

  val method = "GET"
  val url = "test-url"
  val mtdId = "test-mtd-id"

  val mtdIdJson: JsObject = Json.obj("mtdbsa" -> mtdId)

  "read" should {
    "return an MtdId" when {
      "the HttpResponse contains a 200 status and a correct response body" in {
        val response = HttpResponse(OK, Some(mtdIdJson))
        val result: MtdIdLookupOutcome = mtdIdLookupHttpReads.read(method, url, response)

        result shouldBe Right(mtdId)
      }
    }

    "return an InvalidNino error" when {
      "the HttpResponse contains a 403 status" in {
        val response = HttpResponse(FORBIDDEN)
        val result: MtdIdLookupOutcome = mtdIdLookupHttpReads.read(method, url, response)

        result shouldBe Left(InvalidNino)
      }
    }

    "return a DownstreamError" when {
      "the HttpResponse contains any other status" in {
        val response = HttpResponse(INTERNAL_SERVER_ERROR)
        val result: MtdIdLookupOutcome = mtdIdLookupHttpReads.read(method, url, response)

        result shouldBe Left(DownstreamError)
      }
    }
  }
}
