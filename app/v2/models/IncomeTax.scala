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

case class IncomeTax(taxableIncome: BigDecimal,
                     payAndPensionsProfit: Option[IncomeTaxItem],
                     savingsAndGains: Option[IncomeTaxItem],
                     dividends: Option[IncomeTaxItem],
                     totalBeforeReliefs: BigDecimal,
                     allowancesAndReliefs: Option[AllowancesAndReliefs],
                     totalAfterReliefs: BigDecimal,
                     giftAid: Option[GiftAid],
                     totalAfterGiftAid: Option[BigDecimal],
                     totalIncomeTax: BigDecimal,
                     residentialFinanceCosts: Option[ResidentialFinanceCosts])

object IncomeTax {
  implicit val writes: Writes[IncomeTax] = Json.writes[IncomeTax]

  implicit val reads: Reads[IncomeTax] = (
    (__ \ "incomeTax" \ "taxableIncome").read[BigDecimal] and
      (__ \ "incomeTax" \ "payPensionsProfit").readNullable[IncomeTaxItem].orElse(Reads.pure(None)) and
      (__ \ "incomeTax" \ "savingsAndGains").readNullable[IncomeTaxItem].orElse(Reads.pure(None)) and
      (__ \ "incomeTax" \ "dividends").readNullable[IncomeTaxItem].orElse(Reads.pure(None)) and
      (__ \ "incomeTax" \ "totalBeforeReliefs").read[BigDecimal] and
      (__ \ "incomeTax").readNullable[AllowancesAndReliefs].orElse(Reads.pure(None)) and
      (__ \ "incomeTax" \ "totalAfterReliefs").read[BigDecimal] and
      (__ \ "incomeTax" \ "giftAid").readNullable[GiftAid].orElse(Reads.pure(None)) and
      (__ \ "incomeTax" \ "totalAfterGiftAid").readNullable[BigDecimal] and
      (__ \ "totalIncomeTax").read[BigDecimal] and
      (__ \ "incomeTax" \ "residentialFinanceCosts").readNullable[ResidentialFinanceCosts].orElse(Reads.pure(None))
    ) (IncomeTax.apply _)

}
