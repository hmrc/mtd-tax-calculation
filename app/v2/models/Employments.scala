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

package v2.models

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Json, _}

case class Employments(totalIncome: Option[BigDecimal],
                       totalPay: BigDecimal,
                       totalBenefitsAndExpenses: BigDecimal,
                       totalAllowableExpenses: BigDecimal,
                       employment: Seq[Employment])

object Employments {
  implicit val writes: Writes[Employments] = Json.writes[Employments]

  implicit val reads: Reads[Employments] = (
    (__ \ "employmentIncome").readNullable[BigDecimal] and
      (__ \ "employments" \ "totalPay").read[BigDecimal] and
      (__ \ "employments" \ "totalBenefitsAndExpenses").read[BigDecimal] and
      (__ \ "employments" \ "totalAllowableExpenses").read[BigDecimal] and
      (__ \ "employments" \ "employment").read[Seq[Employment]]
    )(Employments.apply _)
}
