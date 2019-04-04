/*
 * Copyright 2019 HM Revenue & Customs
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

import play.api.Logger
import play.api.libs.json.{JsValue, Reads}
import uk.gov.hmrc.http.HttpResponse

import scala.util.{Failure, Success, Try}

trait HttpParser {

  implicit class HttpResponseOps(resp: HttpResponse) {
    def jsonOpt: Option[JsValue] = {
      Try(resp.json) match {
        case Success(json: JsValue) => Some(json)
        case Success(_) =>
          Logger.warn("No JSON was returned")
          None
        case Failure(error) =>
          Logger.warn(s"Unable to retrieve JSON: ${error.getMessage}")
          None
      }
    }
  }

  implicit class JsonResponseOps(response: HttpResponse) {
    def validateJson[T](implicit reads: Reads[T]): Option[T] = {
      Try(response.json) match {
        case Success(js: JsValue) => js.asOpt
        case Success(_) =>
          Logger.warn("No JSON was returned")
          None
        case Failure(error) =>
          Logger.warn(s"Unable to parse JSON: ${error.getMessage}")
          None
      }
    }
  }

  def retrieveCorrelationId(response: HttpResponse): String = response.header("CorrelationId").getOrElse("")

}
