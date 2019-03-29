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

package v2.models.old

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

case class Employment(employmentId: String,
                      netPay: BigDecimal,
                      benefitsAndExpenses: BigDecimal,
                      allowableExpenses: BigDecimal)

object Employment {
  implicit val writes: Writes[Employment] = Json.writes[Employment]

  implicit val reads: Reads[Employment] = (
    (__ \ "incomeSourceID").read[String] and
      (__ \ "netPay").read[BigDecimal] and
      (__ \ "benefitsAndExpenses").read[BigDecimal] and
      (__ \ "allowableExpenses").read[BigDecimal]
    )(Employment.apply _)
}
