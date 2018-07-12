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


case class Nic(totalNic: Option[BigDecimal],
               class2: Option[Class2Nic],
               class4: Option[Class4Nic])

object Nic {
  implicit val writes: Writes[Nic] = Json.writes[Nic]

  implicit val reads: Reads[Nic] = (
    (__ \ "totalNic").readNullable[BigDecimal].orElse(Reads.pure(None)) and
      (__ \ "nic" \ "class2").readNullable[Class2Nic].orElse(Reads.pure(None)) and
      (__ \ "nic" \ "class4").readNullable[Class4Nic].orElse(Reads.pure(None))
    ) (Nic.apply _)

}
