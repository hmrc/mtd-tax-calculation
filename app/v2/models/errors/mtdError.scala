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

package v2.models.errors

import play.api.libs.json._

trait MtdError

case class Error(code: String, message: String) extends MtdError

//2xx
object CalculationNotReady extends MtdError

//4xx
object NotFound extends MtdError
object InvalidNinoError extends Error("FORMAT_NINO", "The format of the National Insurance number is invalid")
object InvalidCalcIDError extends Error("FORMAT_CALC_ID", "The format of the tax calculation ID is invalid")
object UnauthorisedError extends Error("CLIENT_OR_AGENT_NOT_AUTHORISED", "The client and/or agent is not authorised")
//5xx
object InternalServerError extends Error("INTERNAL_SERVER_ERROR", "An internal server error occurred")

object Error {
  implicit val format: OFormat[Error] = Json.format[Error]
}
object MtdError {
  implicit val mtdErrorWrites: Writes[MtdError] =
    new Writes[MtdError]{
      def writes(o: MtdError): JsValue = o match {
        case error: Error => Error.format.writes(error)
        case _: MtdError => JsNull
      }
    }
}