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

package v2.models

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Json, _}

case class IncomeTaxBand(name: String,
                         rate: BigDecimal,
                         threshold: Option[Long],
                         apportionedThreshold: Option[Long],
                         bandLimit: Option[Long],
                         apportionedBandLimit: Option[Long],
                         income: BigDecimal,
                         amount: BigDecimal)

object IncomeTaxBand {
  implicit val writes: Writes[IncomeTaxBand] = Json.writes[IncomeTaxBand]

  implicit val reads: Reads[IncomeTaxBand] = (
    (__ \ "name").read[String] and
      (__ \ "rate").read[BigDecimal] and
      (__ \ "threshold").readNullable[Long] and
      (__ \ "apportionedThreshold").readNullable[Long] and
      (__ \ "bandLimit").readNullable[Long] and
      (__ \ "apportionedBandLimit").readNullable[Long] and
      (__ \ "income").read[BigDecimal] and
      (__ \ "taxAmount").read[BigDecimal]
    ) (IncomeTaxBand.apply _)

}
