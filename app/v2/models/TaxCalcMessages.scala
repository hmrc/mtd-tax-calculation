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

package v2.models

import play.api.libs.json.{Json, OWrites, Reads, __}
import play.api.libs.functional.syntax._

case class TaxCalcMessages(warningCount: Int,
                           errorCount: Int,
                           messages: Option[Seq[Message]])

object TaxCalcMessages{

  implicit val reads: Reads[TaxCalcMessages] = (
    (__ \ "calcOutput" \ "bvrWarnings").read[Int] and
      (__ \ "calcOutput" \ "bvrErrors").read[Int] and
      (__ \ "calcOutput" \ "bvrMsg").readNestedNullable[Seq[Message]]
        .map(_.flatMap {
          case Nil => None
          case message => Some(message)
        })
    )(TaxCalcMessages.apply _)

  implicit val writes: OWrites[TaxCalcMessages] = Json.writes[TaxCalcMessages]
}

case class Message(id: String,
                   `type`: String,
                   text: String)

object Message {
  implicit val reads: Reads[Message] = (
    (__ \ "id").read[String] and
    (__ \ "type").read[String] and
      (__ \ "text").read[String]
    ) (Message.apply _)
  implicit val writes: OWrites[Message] = Json.writes[Message]
}