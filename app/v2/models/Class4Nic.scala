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

case class Class4Nic(totalAmount: BigDecimal,
                     band: Seq[NicBand])

object Class4Nic {
  implicit val writes: Writes[Class4Nic] = Json.writes[Class4Nic]

  implicit val reads: Reads[Class4Nic] = (
    (__ \ "totalAmount").read[BigDecimal] and
      (__ \ "band").read[Seq[NicBand]]
    ) (Class4Nic.apply _)

}
