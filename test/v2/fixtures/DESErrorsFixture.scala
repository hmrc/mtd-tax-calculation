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

package v2.fixtures

import play.api.libs.json.{JsValue, Json}

object DESErrorsFixture {

  private def desError(code:String, reason: String = "Do not care") = {
    Json.parse(
      s"""
         |{
         |  "code": "$code",
         |  "reason": "$reason"
         |}
         |""".stripMargin)
  }

  //Des Error Responses
  val invalidIdentifierJson: JsValue = desError("INVALID_IDENTIFIER")
  val invalidCalcIDJson: JsValue = desError("INVALID_CALCID")
  val notFoundJson: JsValue = desError("NOT_FOUND")
  val serverErrorJson: JsValue = desError("SERVER_ERROR")
  val serviceUnavailableJson: JsValue = desError("SERVICE_UNAVAILABLE")
  val invalidErrorCodeJson: JsValue = desError("INVALID_NINO")
}
