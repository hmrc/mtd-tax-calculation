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
case class CalculationMessage(
                             `type`: String,
                             text: Option[String]
                              )
//#1
case class AnnualAllowances(
                           personalAllowance: Option[Int],
                           personalAllowanceThreshold: Option[Int],
                           reducedPersonalisedAllowance: Option[Int],
                           giftAidExtender: Option[Int]
                           )

object TaxCalculation {
  implicit val format: OFormat[TaxCalculation] = Json.format[TaxCalculation]
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
  implicit val format: OFormat[IncomeTax] = Json.format[IncomeTax]
}
object IncomeTaxItem {
  implicit val format: OFormat[IncomeTaxItem] = Json.format[IncomeTaxItem]
}
object Band {
  implicit val format: OFormat[Band] = Json.format[Band]
}
object AllowancesAndReliefs {
  implicit val format: OFormat[AllowancesAndReliefs] = Json.format[AllowancesAndReliefs]
}
object GiftAid {
  implicit val format: OFormat[GiftAid] = Json.format[GiftAid]
}
object Nic {
  implicit val format: OFormat[Nic] = Json.format[Nic]
}
object Class2Nic {
  implicit val format: OFormat[Class2Nic] = Json.format[Class2Nic]
}
object Class4Nic {
  implicit val format: OFormat[Class4Nic] = Json.format[Class4Nic]
}
object TaxDeducted {
  implicit val format: OFormat[TaxDeducted] = Json.format[TaxDeducted]
}
object EoyEstimate {
  implicit val format: OFormat[EoyEstimate] = Json.format[EoyEstimate]
}
object EoyEmployment {
  implicit val format: OFormat[EoyEmployment] = Json.format[EoyEmployment]
}
object EoySelfEmployment {
  implicit val format: OFormat[EoySelfEmployment] = Json.format[EoySelfEmployment]
}
object EoyItem {
  implicit val format: OFormat[EoyItem] = Json.format[EoyItem]
}
object CalculationMessage {
  implicit val format: OFormat[CalculationMessage] = Json.format[CalculationMessage]
}
object AnnualAllowances {
  implicit val format: OFormat[AnnualAllowances] = Json.format[AnnualAllowances]
}