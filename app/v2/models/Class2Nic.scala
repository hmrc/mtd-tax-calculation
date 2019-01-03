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

case class Class2Nic(amount: BigDecimal,
                     weekRate: BigDecimal,
                     weeks: Int,
                     limit: Int,
                     apportionedLimit: Int)

object Class2Nic {
  implicit val writes: Writes[Class2Nic] = Json.writes[Class2Nic]

  implicit val reads: Reads[Class2Nic] = (
    (__ \ "amount").read[BigDecimal] and
      (__ \ "weekRate").read[BigDecimal] and
      (__ \ "weeks").read[Int] and
      (__ \ "limit").read[Int] and
      (__ \ "apportionedLimit").read[Int]
    ) (Class2Nic.apply _)

}
