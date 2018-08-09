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

case class TaxDeducted(ukLandAndProperty: Option[BigDecimal],
                       totalTaxDeducted: Option[BigDecimal])

object TaxDeducted {
  implicit val writes: Writes[TaxDeducted] = Json.writes[TaxDeducted]

  implicit val reads: Reads[TaxDeducted] = (
    (__ \\ "ukLandAndProperty").readNullable[BigDecimal] and
      (__ \ "totalTaxDeducted").readNullable[BigDecimal]
    )(TaxDeducted.apply _)

  case class TaxDeducted_(ukLandAndProperty: Option[BigDecimal])

  object TaxDeducted_ {
    implicit val writes: OWrites[TaxDeducted_] = Json.writes[TaxDeducted_]
    implicit val reads: Reads[TaxDeducted_] = (__ \ "ukLandAndProperty").readNullable[BigDecimal].map(TaxDeducted_.apply)
  }

}
