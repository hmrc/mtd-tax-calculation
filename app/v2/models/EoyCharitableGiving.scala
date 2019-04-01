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

case class EoyCharitableGiving(taxableIncome: BigDecimal,
                               supplied: Boolean,
                               finalised: Option[Boolean])

object EoyCharitableGiving {
  implicit val writes: Writes[EoyCharitableGiving] = Json.writes[EoyCharitableGiving]

  implicit val reads: Reads[EoyCharitableGiving] = (
      (__ \ "taxableIncome").read[BigDecimal] and
      (__ \ "supplied").read[Boolean] and
        (__ \ "finalised").readNullable[Boolean]
    )(EoyCharitableGiving.apply _)

}
