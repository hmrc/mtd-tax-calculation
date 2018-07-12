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

case class UKProperty(totalIncome: Option[BigDecimal],
                      nonFurnishedHolidayLettingsTaxableProfit: Option[BigDecimal],
                      nonFurnishedHolidayLettingsLoss: Option[BigDecimal],
                      furnishedHolidayLettingsTaxableProfit: Option[BigDecimal],
                      furnishedHolidayLettingsLoss: Option[BigDecimal],
                      finalised: Option[Boolean])

object UKProperty {
  implicit val writes: Writes[UKProperty] = Json.writes[UKProperty]

  implicit val reads: Reads[UKProperty] = (
    (__ \ "ukPropertyIncome").readNullable[BigDecimal].orElse(Reads.pure(None)) and
      (__ \ "ukProperty" \ "taxableProfit").readNullable[BigDecimal].orElse(Reads.pure(None)) and
      (__ \ "ukProperty" \ "losses").readNullable[BigDecimal].orElse(Reads.pure(None)) and
      (__ \ "ukProperty" \ "taxableProfitFhlUk").readNullable[BigDecimal].orElse(Reads.pure(None)) and
      (__ \ "ukProperty" \ "lossesFhlUk").readNullable[BigDecimal].orElse(Reads.pure(None)) and
      (__ \ "ukProperty" \ "finalised").readNullable[Boolean].orElse(Reads.pure(None))
    ) (UKProperty.apply _)

}
