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

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Json, _}

case class Employment(employmentId: Option[String],
                      netPay: Option[BigDecimal],
                      benefitsAndExpenses: Option[BigDecimal],
                      allowableExpenses: Option[BigDecimal])

object Employment {
  implicit val writes: Writes[Employment] = Json.writes[Employment]

  implicit val reads: Reads[Employment] = (
    (__ \ "incomeSourceID").readNullable[String].orElse(Reads.pure(None)) and
      (__ \ "netPay").readNullable[BigDecimal].orElse(Reads.pure(None)) and
      (__ \ "benefitsAndExpenses").readNullable[BigDecimal].orElse(Reads.pure(None)) and
      (__ \ "allowableExpenses").readNullable[BigDecimal].orElse(Reads.pure(None))
    )(Employment.apply _)

}