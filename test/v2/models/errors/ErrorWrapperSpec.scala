/*
 * Copyright 2020 HM Revenue & Customs
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

package v2.models.errors

import play.api.libs.json.{JsValue, Json}
import support.UnitSpec

class ErrorWrapperSpec extends UnitSpec {

  val correlationId: String = "X-123"

  "Rendering a error response with one error" should {
    val error: ErrorWrapper = ErrorWrapper(correlationId, InvalidNinoError, Some(Seq.empty))

    val json: JsValue = Json.parse(
      """
        |{
        |   "code": "FORMAT_NINO",
        |   "message": "The format of the National Insurance number is invalid"
        |}
      """.stripMargin
    )

    "generate the correct JSON" in {
      Json.toJson(error) shouldBe json
    }
  }

  "Rendering a error response with one error and an empty sequence of errors" should {
    val error: ErrorWrapper = ErrorWrapper(correlationId, InvalidNinoError, Some(Seq.empty))

    val json: JsValue = Json.parse(
      """
        |{
        |   "code": "FORMAT_NINO",
        |   "message": "The format of the National Insurance number is invalid"
        |}
      """.stripMargin
    )

    "generate the correct JSON" in {
      Json.toJson(error) shouldBe json
    }
  }

  "Rendering a error response with two errors" should {
    val error: ErrorWrapper = ErrorWrapper(correlationId, Error("INVALID_REQUEST", "Invalid request"),
      Some (
        Seq(
          InvalidNinoError,
          InvalidCalcIDError
        )
      )
    )

    val json: JsValue = Json.parse(
      """
        |{
        |   "code": "INVALID_REQUEST",
        |   "message": "Invalid request",
        |   "errors": [
        |       {
        |         "code": "FORMAT_NINO",
        |         "message": "The format of the National Insurance number is invalid"
        |       },
        |       {
        |         "code": "FORMAT_CALC_ID",
        |         "message": "The format of the tax calculation ID is invalid"
        |       }
        |   ]
        |}
      """.stripMargin
    )

    "generate the correct JSON" in {
      Json.toJson(error) shouldBe json
    }
  }

}