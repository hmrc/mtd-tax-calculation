/*
 * Copyright 2021 HM Revenue & Customs
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

case class AnnualAllowances(personalAllowance: Long,
                            personalAllowanceThreshold: Option[Long],
                            reducedPersonalAllowance: Option[Long],
                            giftAidExtender: Option[Long])

object AnnualAllowances {
  implicit val writes: Writes[AnnualAllowances] = Json.writes[AnnualAllowances]

  implicit val reads: Reads[AnnualAllowances] = (
    (__ \ "personalAllowance").read[Long] and
      (__ \ "reducedPersonalAllowanceThreshold").readNullable[Long] and
      (__ \ "reducedPersonalisedAllowance").readNullable[Long] and
      (__ \ "giftAidExtender").readNullable[Long]
    ) (AnnualAllowances.apply _)

}
