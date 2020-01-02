/*
 * Copyright 2020 HM Revenue & Customs
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
                          intentToCrystallise: Boolean,
                          crystallised: Boolean,
                          validationMessageCount: Int,
                          incomeTaxAndNicYTD: Option[BigDecimal],
                          nationalRegime: Option[String],
                          taxableIncome: Option[TaxableIncome],
                          incomeTax: Option[IncomeTax],
                          nic: Option[Nic],
                          totalBeforeTaxDeducted: Option[BigDecimal],
                          taxDeducted: Option[TaxDeducted],
                          eoyEstimate: Option[EoyEstimate],
                          calculationMessageCount: Option[Int],
                          calculationMessages: Option[Seq[CalculationMessage]],
                          annualAllowances: Option[AnnualAllowances]) extends ITaxCalc

object TaxCalculation {
  implicit val writes: OWrites[TaxCalculation] = Json.writes[TaxCalculation]

  implicit val reads: Reads[TaxCalculation] = (
      (__ \ "calcOutput" \ "year").readNestedNullable[Int] and
        (__ \ "calcOutput" \ "intentToCrystallise").readNestedNullable[Boolean].map(_.exists(identity)) and
        (__ \ "calcOutput" \ "calcResult" \ "crystallised").readNestedNullable[Boolean].map(_.exists(identity)) and
        ((__ \ "calcOutput" \ "bvrErrors").readNestedNullable[Int] and
          (__ \ "calcOutput" \ "bvrWarnings").readNestedNullable[Int])
          ((errs, warns) => (errs ++ warns).reduceOption(_ + _)).map(_.fold(0)(identity)) and
        (__ \ "calcOutput" \ "calcResult" \ "incomeTaxNicYtd").readNestedNullable[BigDecimal] and
        (__ \ "calcOutput" \ "calcResult" \ "nationalRegime").readNestedNullable[String] and
        (__ \ "calcOutput" \ "calcResult").readNestedNullable[TaxableIncome] and
        (__ \ "calcOutput" \ "calcResult").readNestedNullable[IncomeTax] and
        (__ \ "calcOutput" \ "calcResult").readNestedNullable[Nic] and
        (__ \ "calcOutput" \ "calcResult" \ "totalBeforeTaxDeducted").readNestedNullable[BigDecimal] and
        (__ \ "calcOutput" \ "calcResult").readNestedNullable[TaxDeducted]
          .map(_.flatMap {
            case TaxDeducted(None, None, None) => None
            case x => Some(x)
          }) and
        (__ \ "calcOutput" \ "calcResult" \ "eoyEstimate").readNestedNullable[EoyEstimate] and
        (__ \ "calcOutput" \ "calcResult" \ "msgCount").readNestedNullable[Int] and
        (__ \ "calcOutput" \ "calcResult" \ "msg").readNestedNullable[Seq[CalculationMessage]]
          .map(_.flatMap {
            case Nil => None
            case xs => Some(xs)
          }) and
        (__ \ "calcOutput" \ "calcResult" \ "annualAllowances").readNestedNullable[AnnualAllowances]
      ) (TaxCalculation.apply _)

}
