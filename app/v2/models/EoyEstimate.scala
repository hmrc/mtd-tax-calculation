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
import play.api.libs.json._

case class EoyEstimate(employments: Seq[EoyEmployment],
                       selfEmployments: Seq[EoySelfEmployment],
                       ukProperty: EoyItem,
                       ukDividends: EoyItem,
                       totalTaxableIncome: Option[BigDecimal],
                       incomeTaxAmount: Option[BigDecimal],
                       nic2: Option[BigDecimal],
                       nic4: Option[BigDecimal],
                       totalNicAmount: Option[BigDecimal],
                       incomeTaxAndNicAmount: Option[BigDecimal])

object EoyEstimate {
  implicit val writes: OWrites[EoyEstimate] = Json.writes[EoyEstimate]

  implicit val reads: Reads[EoyEstimate] = {

    ((__ \ "incomeSource").read[Seq[JsValue]] and
      (__ \ "totalTaxableIncome").readNullable[BigDecimal].orElse(Reads.pure(None)) and
      (__ \ "incomeTaxAmount").readNullable[BigDecimal].orElse(Reads.pure(None)) and
      (__ \ "nic2").readNullable[BigDecimal].orElse(Reads.pure(None)) and
      (__ \ "nic4").readNullable[BigDecimal].orElse(Reads.pure(None)) and
      (__ \ "totalNicAmount").readNullable[BigDecimal].orElse(Reads.pure(None)) and
      (__ \ "incomeTaxNicAmount").readNullable[BigDecimal].orElse(Reads.pure(None))
      ){ (incomeSource, totalTaxableIncome, incomeTaxAmount, nic2, nic4, totalNicAmount, incomeTaxNicAmount) =>

      val emptyEoyItem = EoyItem(None, None, None)

      val estimate = EoyEstimate(
        employments = Seq(),
        selfEmployments = Seq(),
        ukProperty = emptyEoyItem,
        ukDividends = emptyEoyItem,
        totalTaxableIncome, incomeTaxAmount, nic2, nic4, totalNicAmount, incomeTaxNicAmount
      )

      incomeSource.foldLeft(estimate){ (old, json) =>
        (json \ "type").as[String] match {
          case "05" => old.copy(employments = old.employments ++ Seq(json.as[EoyEmployment]))
          case "01" => old.copy(selfEmployments = old.selfEmployments ++ Seq(json.as[EoySelfEmployment]))
          case "02" => old.copy(ukProperty = json.as[EoyItem])
          case "10" => old.copy(ukDividends = json.as[EoyItem])
        }
      }
    }
  }
}