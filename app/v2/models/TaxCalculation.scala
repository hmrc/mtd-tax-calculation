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

case class TaxCalculation(year: Option[Int],
                          intentToCrystallise: Option[Boolean],
                          crystallised: Option[Boolean],
                          validationMessageCount: Option[Int],
                          incomeTaxAndNicYTD: Option[BigDecimal],
                          nationalRegime: Option[String],
                          taxableIncome: TaxableIncome,
                          incomeTax: IncomeTax,
                          nic: Nic,
                          totalBeforeTaxDeducted: Option[BigDecimal],
                          taxDeducted: Option[TaxDeducted],
                          eoyEstimate: EoyEstimate,
                          calculationMessageCount: Option[Double],
                          calculationMessages: Option[Seq[CalculationMessage]],
                          annualAllowances: AnnualAllowances)

object TaxCalculation {
  implicit val writes: OWrites[TaxCalculation] = Json.writes[TaxCalculation]

  implicit val reads: Reads[TaxCalculation] = (
    (__ \ "calcOutput" \ "year").readNullable[Int].orElse(Reads.pure(None)) and
      (__ \ "calcOutput" \ "intentToCrystallise").readNullable[Boolean].orElse(Reads.pure(None)) and
      (__ \ "calcOutput" \ "calcResult" \ "crystallised").readNullable[Boolean].orElse(Reads.pure(None)) and
      ((__ \ "calcOutput" \ "bvrErrors").readNullable[Int].orElse(Reads.pure(None)) and
        (__ \ "calcOutput" \ "bvrWarnings").readNullable[Int].orElse(Reads.pure(None)))
        ((errs, warns) => (errs ++ warns).reduceOption(_ + _)) and
      (__ \ "calcOutput" \ "calcResult" \ "incomeTaxNicYtd").readNullable[BigDecimal].orElse(Reads.pure(None)) and
      (__ \ "calcOutput" \ "calcResult" \ "nationalRegime").readNullable[String].orElse(Reads.pure(None)) and
      (__ \ "calcOutput" \ "calcResult").read[TaxableIncome] and
      (__ \ "calcOutput" \ "calcResult").read[IncomeTax] and
      (__ \ "calcOutput" \ "calcResult").read[Nic] and
      (__ \ "calcOutput" \ "calcResult" \ "totalBeforeTaxDeducted").readNullable[BigDecimal].orElse(Reads.pure(None)) and
      (__ \ "calcOutput" \ "calcResult").readNullable[TaxDeducted].orElse(Reads.pure(None))
        .map(_.flatMap {
          case TaxDeducted(None, None) => None
          case x => Some(x)
        }) and
      (__ \ "calcOutput" \ "calcResult" \ "eoyEstimate").read[EoyEstimate] and
      (__ \ "calcOutput" \ "calcResult" \ "msgCount").readNullable[Double].orElse(Reads.pure(None)) and
      (__ \ "calcOutput" \ "calcResult" \ "msg").readNullable[Seq[CalculationMessage]].orElse(Reads.pure(None))
        .map(_.flatMap {
          case Nil => None
          case xs => Some(xs)
        }) and
      (__ \ "calcOutput" \ "calcResult" \ "annualAllowances").read[AnnualAllowances]
    ) (TaxCalculation.apply _)

}
