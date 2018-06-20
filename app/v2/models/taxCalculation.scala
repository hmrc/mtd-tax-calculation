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

import play.api.libs.json.{Json, OFormat}
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

//  #0
case class TaxCalculation(
                         year: String,
                         intentToCrystallise: Option[Boolean],
                         crystallised: Option[Boolean],
                         validationMessageCount: Option[Int],
                         incomeTaxAndNicYTD: Option[BigDecimal],
                         nationalRegime: Option[String],
                         taxableIncome: TaxableIncome,
                         incomeTax: IncomeTax,
                         nic: Nic,
                         totalBeforeTaxDeducted: Option[BigDecimal],
                         taxDeducted: TaxDeducted,
                         eoyEstimate: EoyEstimate,
                         calculationMessageCount: Option[Int],
                         calculationMessages: Seq[CalculationMessage],
                         annualAllowances: AnnualAllowances
                         )
// #1
case class TaxableIncome(
                        employments: Employments,
                        selfEmployments: SelfEmployments,
                        ukProperty: UKProperty,
                        ukDividends: UKDividends,
                        totalIncomeReceived: Option[BigDecimal],
                        allowancesAndDeductions: AllowancesAndDeductions,
                        totalTaxableIncome: Option[BigDecimal]
                        )

// #2
case class Employments(
                      totalIncome: Option[BigDecimal],
                      totalPay: Option[BigDecimal],
                      totalBenefitsAndExpenses: Option[BigDecimal],
                      totalAllowableExpenses: Option[BigDecimal],
                      employment: Seq[Employment]
                      )

//#3
case class Employment(
                     employmentId: Option[String],
                     netPay: Option[BigDecimal],
                     benefitsAndExpenses: Option[BigDecimal],
                     allowableExpenses: Option[BigDecimal]
                     )
//#2
case class SelfEmployments(
                          totalIncome: Option[BigDecimal],
                          selfEmployment: Seq[SelfEmployment]
                          )
//#3
case class SelfEmployment(
                         selfEmploymentId: String,
                         taxableIncome: Option[BigDecimal],
                         finalised: Option[Boolean],
                         losses: Option[BigDecimal]
                         )

//#2
case class UKProperty(
                     totalIncome: Option[BigDecimal],
                     nonFurnishedHolidayLettingsTaxableProfit: Option[BigDecimal],
                     nonFurnishedHolidayLettingsLoss: Option[BigDecimal],
                     furnishedHolidayLettingsTaxableProfit: Option[BigDecimal],
                     furnishedHolidayLettingsLoss: Option[BigDecimal],
                     finalised: Option[Boolean]
                     )

//#2
case class UKDividends(
                      totalIncome: Option[BigDecimal]
                      )

//#2
case class AllowancesAndDeductions(
                                  totalAllowancesAndDeductions: Option[BigDecimal],
                                  giftOfInvestmentsAndPropertyToCharity: Option[BigDecimal],
                                  apportionedPersonalAllowance: Option[BigDecimal]
                                  )
//#1
case class IncomeTax(
                    payPensionsProfit: IncomeTaxItem,
                    savingsAndGains: IncomeTaxItem,
                    dividends: IncomeTaxItem,
                    totalBeforeReliefs: Option[BigDecimal],
                    allowancesAndReliefs: AllowancesAndReliefs,
                    totalAfterReliefs: Option[BigDecimal],
                    giftAid: GiftAid,
                    totalAfterGiftAid: Option[BigDecimal],
                    totalIncomeTax: Option[BigDecimal]
                    )
//#2
case class IncomeTaxItem(
                        totalAmount: Option[BigDecimal],
                        band: Seq[Band],
                        personalAllowanceUsed: Option[BigDecimal],
                        taxableIncome: Option[BigDecimal]
                        )
//#3
case class Band(
                 name: String,
                 rate: BigDecimal,
                 threshold: Int,
                 apportionedThreshold: Int,
                 income: BigDecimal,
                 amount: BigDecimal
                 )

//#2
case class AllowancesAndReliefs(
                               propertyFinanceRelief: Option[BigDecimal],
                               totalAllowancesAndReliefs: Option[BigDecimal]
                               )
//#2
case class GiftAid(
                  paymentsMade: Option[BigDecimal],
                  rate: BigDecimal,
                  taxableAmount: Option[BigDecimal]
                  )

//#1
case class Nic(
              totalNic: Option[BigDecimal],
              class2: Class2Nic,
              class4: Class4Nic
              )

//#2
case class Class2Nic(
                    amount: Option[BigDecimal],
                    weekRate: Option[BigDecimal],
                    weeks: Option[BigDecimal],
                    limit: Option[Int],
                    apportionedLimit: Option[Int]
                    )
//#2
case class Class4Nic(
                    totalAmount: Option[BigDecimal],
                    band: Seq[Band]
                    )
//#1
case class TaxDeducted(
                      ukLandAndProperty: Option[BigDecimal],
                      totalTaxDeducted: Option[BigDecimal]
                      )
//#1
case class EoyEstimate(
                        employments: Seq[EoyEmployment],
                        selfEmployments: Seq[EoySelfEmployment],
                        ukProperty: EoyItem,
                        ukDividends: EoyItem,
                        totalTaxableIncome: Option[BigDecimal],
                        incomeTaxAmount: Option[BigDecimal],
                        nic2: Option[BigDecimal],
                        nic4: Option[BigDecimal],
                        totalNicAmount: Option[BigDecimal],
                        incomeTaxAndNicAmount: Option[BigDecimal]
                      )
//#2
case class EoyEmployment(
                        employmentId: Option[String],
                        taxableIncome: Option[BigDecimal],
                        supplied: Option[Boolean],
                        finalised: Option[Boolean]
                        )
//#2
case class EoySelfEmployment(
                            selfEmploymentId: String,
                            taxableIncome: Option[BigDecimal],
                            supplied: Option[Boolean],
                            finalised: Option[Boolean]
                            )
//#2
case class EoyItem(
                  taxableIncome: Option[BigDecimal],
                  supplied: Option[Boolean],
                  finalised: Option[Boolean]
                  )
//#1
case class CalculationMessage(`type`: String,
                              text: Option[String])
//#1
case class AnnualAllowances(
                           personalAllowance: Option[Int],
                           personalAllowanceThreshold: Option[Int],
                           reducedPersonalisedAllowance: Option[Int],
                           giftAidExtender: Option[Int]
                           )

object TaxCalculation {
  implicit val writes: OWrites[TaxCalculation] = Json.writes[TaxCalculation]

  implicit val reads: Reads[TaxCalculation] = {
    (
      (__ \ "calcOutput" \ "year").read[String] and
        (__ \ "calcOutput" \ "intentToCrystallise").readNullable[Boolean] and
        (__ \ "calcOutput" \ "crystallised").readNullable[Boolean] and
        ((__ \ "calcOutput" \ "bvrErrors").readNullable[Int] and
         (__ \ "calcOutput" \ "bvrWarnings").readNullable[Int])
        ((errs, warns) => (errs ++ warns).reduceOption(_ + _)) and
        (__ \ "calcOutput" \ "calcResult" \ "incomeTaxNicYtd").readNullable[BigDecimal] and
        (__ \ "calcOutput" \ "calcResult" \ "nationalRegime").readNullable[String] and
        (__ \ "calcOutput" \ "calcResult").read[TaxableIncome]((
          (__ \ "taxableIncome" \ "incomeReceived").read[Employments]((
            (__ \ "employmentIncome").readNullable[BigDecimal] and
            (__ \ "employments" \ "totalPay").readNullable[BigDecimal] and
            (__ \ "employments" \ "totalBenefitsAndExpenses").readNullable[BigDecimal] and
            (__ \ "employments" \ "totalAllowableExpenses").readNullable[BigDecimal] and
            (__ \ "employments" \ "employment").read[Seq[Employment]](traversableReads[Seq, Employment](implicitly, (
              (__ \ "incomeSourceId").readNullable[String] and
              (__ \ "netPay").readNullable[BigDecimal] and
              (__ \ "benefitsAndExpenses").readNullable[BigDecimal] and
              (__ \ "allowableExpenses").readNullable[BigDecimal]
            )(Employment.apply _)))
          )(Employments.apply _)) and
          (__ \ "taxableIncome" \ "incomeReceived").read[SelfEmployments]((
            (__ \ "selfEmploymentIncome").readNullable[BigDecimal] and
            (__ \ "selfEmployment").read[Seq[SelfEmployment]](traversableReads[Seq, SelfEmployment](implicitly, (
              (__ \ "incomeSourceId").read[String] and
              (__ \ "taxableIncome").readNullable[BigDecimal] and
              (__ \ "finalised").readNullable[Boolean] and
              (__ \ "losses").readNullable[BigDecimal]
            )(SelfEmployment.apply _)))
          )(SelfEmployments.apply _)) and
          (__ \ "taxableIncome" \ "incomeReceived").read[UKProperty]((
            (__ \ "ukPropertyIncome").readNullable[BigDecimal] and
            (__ \ "ukProperty" \ "taxableProfit").readNullable[BigDecimal] and
            (__ \ "ukProperty" \ "losses").readNullable[BigDecimal] and
            (__ \ "ukProperty" \ "taxableProfitFhlUk").readNullable[BigDecimal] and
            (__ \ "ukProperty" \ "lossesFhlUk").readNullable[BigDecimal] and
            (__ \ "ukProperty" \ "finalised").readNullable[Boolean]
          )(UKProperty.apply _)) and
          (__ \ "taxableIncome" \ "incomeReceived").read[UKDividends](
            (__ \ "ukDividendsIncome").readNullable[BigDecimal].map(UKDividends.apply)
          ) and
          (__ \ "taxableIncome" \ "totalIncomeReceived").readNullable[BigDecimal] and
          (__ \ "taxableIncome").read[AllowancesAndDeductions]((
            (__ \ "totalAllowancesAndDeductions").readNullable[BigDecimal] and
            (__ \ "allowancesAndDeductions" \ "giftOfInvestmentsAndPropertyToCharity").readNullable[BigDecimal] and
            (__ \ "allowancesAndDeductions" \ "apportionedPersonalAllowance").readNullable[BigDecimal]
          )(AllowancesAndDeductions.apply _)) and
          (__ \ "totalTaxableIncome").readNullable[BigDecimal]
        )(TaxableIncome.apply _)) and
        (__ \ "calcOutput" \ "calcResult").read[IncomeTax]((
          (__ \ "incomeTax" \ "payAndPensionsProfit").read[IncomeTaxItem] and
          (__ \ "incomeTax" \ "savingsAndGains").read[IncomeTaxItem] and
          (__ \ "incomeTax" \ "dividends").read[IncomeTaxItem] and
          (__ \ "incomeTax" \ "totalBeforeReliefs").readNullable[BigDecimal] and
          (__ \ "incomeTax").read[AllowancesAndReliefs]((
            (__ \ "allowancesAndReliefs" \ "propertyFinanceRelief").readNullable[BigDecimal] and
            (__ \ "totalAllowancesAndReliefs").readNullable[BigDecimal]
          )(AllowancesAndReliefs.apply _ )) and
          (__ \ "incomeTax" \ "totalAfterReliefs").readNullable[BigDecimal] and
          (__ \ "incomeTax" \ "giftAid").read[GiftAid]((
            (__ \ "paymentsMade").readNullable[BigDecimal] and
            (__ \ "rate").read[BigDecimal] and
            (__ \ "taxableAmount").readNullable[BigDecimal]
          )(GiftAid.apply _)) and
          (__ \ "incomeTax" \ "totalAfterGiftAid").readNullable[BigDecimal] and
          (__ \ "totalIncomeTax").readNullable[BigDecimal]
        )(IncomeTax.apply _)) and
        (__ \ "calcOutput" \ "calcResult").read[Nic]((
          (__ \ "totalNic").readNullable[BigDecimal] and
          (__ \ "nic" \ "class2").read[Class2Nic]((
            (__ \ "amount").readNullable[BigDecimal] and
            (__ \ "weekRate").readNullable[BigDecimal] and
            (__ \ "weeks").readNullable[BigDecimal] and
            (__ \ "limit").readNullable[Int] and
            (__ \ "apportionedLimit").readNullable[Int]
          )(Class2Nic.apply _)) and
          (__ \ "nic" \ "class4").read[Class4Nic]
        )(Nic.apply _)) and
        (__ \ "calcOutput" \ "calcResult" \ "totalBeforeTaxDeducted").readNullable[BigDecimal] and
        (__ \ "calcOutput" \ "calcResult").read[TaxDeducted]((
          (__ \ "taxDeducted" \ "ukLandAndProperty").readNullable[BigDecimal] and
          (__ \ "totalTaxDeducted").readNullable[BigDecimal]
        )(TaxDeducted.apply _)) and
        (__ \ "calcOutput" \ "calcResult" \ "eoyEstimate").read[EoyEstimate] and
        (__ \ "calcOutput" \ "calcResult" \ "msgCount").readNullable[Int] and
        (__ \ "calcOutput" \ "calcResult" \ "msg").read[Seq[CalculationMessage]] and
        (__ \ "calcOutput" \ "calcResult" \ "annualAllowances").read[AnnualAllowances]((
          (__ \ "personalAllowance").readNullable[Int] and
          (__ \ "reducedPersonalAllowanceThreshold").readNullable[Int] and
          (__ \ "reducedPersonalAllowance").readNullable[Int] and
          (__ \ "giftAidExtender").readNullable[Int]
        )(AnnualAllowances.apply _))
    )(TaxCalculation.apply _)
  }
}

object TaxableIncome {
  implicit val format: OFormat[TaxableIncome] = Json.format[TaxableIncome]
}
object Employments {
  implicit val format: OFormat[Employments] = Json.format[Employments]
}
object Employment {
  implicit val format: OFormat[Employment] = Json.format[Employment]
}
object SelfEmployments {
  implicit val format: OFormat[SelfEmployments] = Json.format[SelfEmployments]
}
object SelfEmployment {
  implicit val format: OFormat[SelfEmployment] = Json.format[SelfEmployment]
}
object UKProperty {
  implicit val format: OFormat[UKProperty] = Json.format[UKProperty]
}
object UKDividends {
  implicit val format: OFormat[UKDividends] = Json.format[UKDividends]
}
object AllowancesAndDeductions {
  implicit val format: OFormat[AllowancesAndDeductions] = Json.format[AllowancesAndDeductions]
}
object IncomeTax {
  implicit val writes: OWrites[IncomeTax] = Json.writes[IncomeTax]
}
object IncomeTaxItem {
  implicit val format: OFormat[IncomeTaxItem] = Json.format[IncomeTaxItem]
}
object Band {
  implicit val format: OFormat[Band] = Json.format[Band]
}
object AllowancesAndReliefs {
  implicit val writes: OWrites[AllowancesAndReliefs] = Json.writes[AllowancesAndReliefs]
}
object GiftAid {
  implicit val writes: OWrites[GiftAid] = Json.writes[GiftAid]
}
object Nic {
  implicit val writes: OWrites[Nic] = Json.writes[Nic]
}
object Class2Nic {
  implicit val writes: OWrites[Class2Nic] = Json.writes[Class2Nic]
}
object Class4Nic {
  implicit val format: OFormat[Class4Nic] = Json.format[Class4Nic]
}
object TaxDeducted {
  implicit val writes: OWrites[TaxDeducted] = Json.writes[TaxDeducted]
}
object EoyEstimate {
  implicit val writes: OWrites[EoyEstimate] = Json.writes[EoyEstimate]

  implicit val reads: Reads[EoyEstimate] = {

    ((__ \ "incomeSource").read[Seq[JsValue]] and
     (__ \ "totalTaxableIncome").readNullable[BigDecimal] and
     (__ \ "incomeTaxAmount").readNullable[BigDecimal] and
     (__ \ "nic2").readNullable[BigDecimal] and
     (__ \ "nic4").readNullable[BigDecimal] and
     (__ \ "totalNicAmount").readNullable[BigDecimal] and
     (__ \ "incomeTaxNicAmount").readNullable[BigDecimal]
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
object EoyEmployment {
  implicit val writes: OWrites[EoyEmployment] = Json.writes[EoyEmployment]

  implicit val reads: Reads[EoyEmployment] = (
    (__ \ "id").readNullable[String] and
    (__ \ "taxableIncome").readNullable[BigDecimal] and
    (__ \ "supplied").readNullable[Boolean] and
    (__ \ "finalised").readNullable[Boolean]
  )(EoyEmployment.apply _)
}
object EoySelfEmployment {
  implicit val writes: OWrites[EoySelfEmployment] = Json.writes[EoySelfEmployment]

  implicit val reads: Reads[EoySelfEmployment] = (
    (__ \ "id").read[String] and
    (__ \ "taxableIncome").readNullable[BigDecimal] and
    (__ \ "supplied").readNullable[Boolean] and
    (__ \ "finalised").readNullable[Boolean]
  )(EoySelfEmployment.apply _)
}
object EoyItem {
  implicit val format: OFormat[EoyItem] = Json.format[EoyItem]
}
object CalculationMessage {
  implicit val format: OFormat[CalculationMessage] = Json.format[CalculationMessage]
}
object AnnualAllowances {
  implicit val writes: OWrites[AnnualAllowances] = Json.writes[AnnualAllowances]
}
