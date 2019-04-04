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
import play.api.libs.json._

case class EoyEstimate(employments: Option[Seq[EoyEmployment]],
                       selfEmployments: Option[Seq[EoySelfEmployment]],
                       ukProperty: Option[EoyItem],
                       ukDividends: Option[EoyItem],
                       totalTaxableIncome: BigDecimal,
                       incomeTaxAmount: BigDecimal,
                       nic2: BigDecimal,
                       nic4: BigDecimal,
                       totalNicAmount: BigDecimal,
                       incomeTaxNicAmount: BigDecimal,
                       charitableGiving: Option[EoyCharitableGiving],
                       savings: Option[Seq[EoySavings]])

object EoyEstimate {
  implicit val writes: OWrites[EoyEstimate] = Json.writes[EoyEstimate]

  implicit val reads: Reads[EoyEstimate] = {

    ((__ \ "incomeSource").read[Seq[JsValue]] and
      (__ \ "totalTaxableIncome").read[BigDecimal] and
      (__ \ "incomeTaxAmount").read[BigDecimal] and
      (__ \ "nic2").read[BigDecimal] and
      (__ \ "nic4").read[BigDecimal] and
      (__ \ "totalNicAmount").read[BigDecimal] and
      (__ \ "incomeTaxNicAmount").read[BigDecimal]
      ){ (incomeSource, totalTaxableIncome, incomeTaxAmount, nic2, nic4, totalNicAmount, incomeTaxNicAmount) =>

      val estimate = EoyEstimate(
        employments = None,
        selfEmployments = None,
        ukProperty = None,
        ukDividends = None,
        totalTaxableIncome, incomeTaxAmount, nic2, nic4, totalNicAmount, incomeTaxNicAmount,
        charitableGiving = None,
        savings = None
      )

      incomeSource.foldLeft(estimate){ (old, json) =>
        (json \ "type").as[String] match {
          case "05" => old.copy(employments = Some(old.employments.getOrElse(Seq()) ++  Seq(json.as[EoyEmployment])))
          case "01" => old.copy(selfEmployments = Some(old.selfEmployments.getOrElse(Seq()) ++ Seq(json.as[EoySelfEmployment])))
          case "02" => old.copy(ukProperty = Some(json.as[EoyItem]))
          case "10" => old.copy(ukDividends = Some(json.as[EoyItem]))
          case "09" => old.copy(savings = Some(old.savings.getOrElse(Seq()) ++  Seq(json.as[EoySavings])))
          case "98" => old.copy(charitableGiving = Some(json.as[EoyCharitableGiving]))
          case _ => old
        }
      }

    }
  }
}
