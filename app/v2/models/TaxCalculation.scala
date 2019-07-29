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
import sangria.macros.derive._
import sangria.schema._

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

//noinspection ScalaStyle
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

  def TaxCalculationType(implicit responseData: TaxCalculation): ObjectType[Unit, TaxCalculation] = deriveObjectType[Unit, TaxCalculation](
    ReplaceField("taxableIncome", Field("taxableIncome", OptionType(TaxableIncomeType), resolve = c => c.value.taxableIncome)),
    ReplaceField("incomeTax", Field("incomeTax", OptionType(IncomeTaxType), resolve = c => c.value.incomeTax)),
    ReplaceField("nic", Field("nic", OptionType(NicType), resolve = c => c.value.nic)),
    ReplaceField("taxDeducted", Field("taxDeducted", OptionType(TaxDeductedType), resolve = c => c.value.taxDeducted)),
    ReplaceField("eoyEstimate", Field("eoyEstimate", OptionType(EoyEstimateType), resolve = c => c.value.eoyEstimate)),
    ReplaceField("calculationMessages", Field("calculationMessages", OptionType(ListType(CalculationMessageType)), resolve = c => c.value.calculationMessages)),
    ReplaceField("annualAllowances", Field("annualAllowances", OptionType(AnnualAllowancesType), resolve = c => c.value.annualAllowances))
  )

  def TaxableIncomeType(implicit responseData: TaxCalculation): ObjectType[Unit, TaxableIncome] = deriveObjectType[Unit, TaxableIncome](
    ReplaceField("employments", Field("employments", OptionType(EmploymentsType), resolve = c => c.value.employments)),
    ReplaceField("selfEmployments", Field("selfEmployments", OptionType(SelfEmploymentsType), resolve = c => c.value.selfEmployments)),
    ReplaceField("ukProperty", Field("ukProperty", OptionType(UKPropertyType), resolve = c => c.value.ukProperty)),
    ReplaceField("ukDividends", Field("ukDividends", OptionType(UKDividendsType), resolve = c => c.value.ukDividends)),
    ReplaceField("savings", Field("savings", OptionType(SavingsIncomeType), resolve = c => c.value.savings)),
    ReplaceField("allowancesAndDeductions", Field("allowancesAndDeductions", OptionType(AllowancesAndDeductionsType), resolve = c => c.value.allowancesAndDeductions))
  )

  def EmploymentsType(implicit responseData: TaxCalculation): ObjectType[Unit, Employments] = deriveObjectType[Unit, Employments](
    ReplaceField("employment", Field("employment", OptionType(ListType(EmploymentType)), resolve = c => c.value.employment))
  )

  def EmploymentType(implicit responseData: TaxCalculation): ObjectType[Unit, Employment] = deriveObjectType[Unit, Employment]()

  def SelfEmploymentsType(implicit responseData: TaxCalculation): ObjectType[Unit, SelfEmployments] = deriveObjectType[Unit, SelfEmployments](
    ReplaceField("selfEmployment", Field("selfEmployment", OptionType(ListType(SelfEmploymentType)), resolve = c => c.value.selfEmployment))
  )

  def SelfEmploymentType(implicit responseData: TaxCalculation): ObjectType[Unit, SelfEmployment] = deriveObjectType[Unit, SelfEmployment]()

  def UKPropertyType(implicit responseData: TaxCalculation): ObjectType[Unit, UKProperty] = deriveObjectType[Unit, UKProperty]()

  def UKDividendsType(implicit responseData: TaxCalculation): ObjectType[Unit, UKDividends] = deriveObjectType[Unit, UKDividends]()

  def SavingsIncomeType(implicit responseData: TaxCalculation): ObjectType[Unit, SavingsIncome] = deriveObjectType[Unit, SavingsIncome](
    ReplaceField("taxedAccounts", Field("taxedAccounts", OptionType(ListType(TaxedSavingsAccountType)), resolve = c => c.value.taxedAccounts)),
    ReplaceField("untaxedAccounts", Field("untaxedAccounts", OptionType(ListType(UntaxedSavingsAccountType)), resolve = c => c.value.untaxedAccounts))
  )

  def TaxedSavingsAccountType(implicit responseData: TaxCalculation): ObjectType[Unit, TaxedSavingsAccount] = deriveObjectType[Unit, TaxedSavingsAccount]()

  def UntaxedSavingsAccountType(implicit responseData: TaxCalculation): ObjectType[Unit, UntaxedSavingsAccount] = deriveObjectType[Unit, UntaxedSavingsAccount]()

  def AllowancesAndDeductionsType(implicit responseData: TaxCalculation): ObjectType[Unit, AllowancesAndDeductions] = deriveObjectType[Unit, AllowancesAndDeductions]()

  def IncomeTaxType(implicit responseData: TaxCalculation): ObjectType[Unit, IncomeTax] = deriveObjectType[Unit, IncomeTax](
    ReplaceField("payAndPensionsProfit", Field("payAndPensionsProfit", OptionType(IncomeTaxItemType), resolve = c => c.value.payAndPensionsProfit)),
    ReplaceField("savingsAndGains", Field("savingsAndGains", OptionType(IncomeTaxItemType), resolve = c => c.value.savingsAndGains)),
    ReplaceField("dividends", Field("dividends", OptionType(IncomeTaxItemType), resolve = c => c.value.dividends)),
    ReplaceField("allowancesAndReliefs", Field("allowancesAndReliefs", OptionType(AllowancesAndReliefsType), resolve = c => c.value.allowancesAndReliefs)),
    ReplaceField("giftAid", Field("giftAid", OptionType(GiftAidType), resolve = c => c.value.giftAid)),
    ReplaceField("residentialFinanceCosts", Field("residentialFinanceCosts", OptionType(ResidentialFinanceCostsType), resolve = c => c.value.residentialFinanceCosts))
  )

  def IncomeTaxItemType(implicit responseData: TaxCalculation): ObjectType[Unit, IncomeTaxItem] = deriveObjectType[Unit, IncomeTaxItem](
    ReplaceField("band", Field("band", ListType(IncomeTaxBandType), resolve = c => c.value.band))
  )

  def IncomeTaxBandType(implicit responseData: TaxCalculation): ObjectType[Unit, IncomeTaxBand] = deriveObjectType[Unit, IncomeTaxBand]()

  def AllowancesAndReliefsType(implicit responseData: TaxCalculation): ObjectType[Unit, AllowancesAndReliefs] = deriveObjectType[Unit, AllowancesAndReliefs]()

  def GiftAidType(implicit responseData: TaxCalculation): ObjectType[Unit, GiftAid] = deriveObjectType[Unit, GiftAid]()

  def ResidentialFinanceCostsType(implicit responseData: TaxCalculation): ObjectType[Unit, ResidentialFinanceCosts] = deriveObjectType[Unit, ResidentialFinanceCosts]()

  def NicType(implicit responseData: TaxCalculation): ObjectType[Unit, Nic] = deriveObjectType[Unit, Nic](
    ReplaceField("class2", Field("class2", OptionType(Class2Type), resolve = c => c.value.class2)),
    ReplaceField("class4", Field("class4", OptionType(Class4Type), resolve = c => c.value.class4))
  )

  def Class2Type(implicit responseData: TaxCalculation): ObjectType[Unit, Class2Nic] = deriveObjectType[Unit, Class2Nic]()

  def Class4Type(implicit responseData: TaxCalculation): ObjectType[Unit, Class4Nic] = deriveObjectType[Unit, Class4Nic](
    ReplaceField("band", Field("band", ListType(NicBandType), resolve = c => c.value.band))
  )

  def NicBandType(implicit responseData: TaxCalculation): ObjectType[Unit, NicBand] = deriveObjectType[Unit, NicBand]()

  def TaxDeductedType(implicit responseData: TaxCalculation): ObjectType[Unit, TaxDeducted] = deriveObjectType[Unit, TaxDeducted]()

  def EoyEstimateType(implicit responseData: TaxCalculation): ObjectType[Unit, EoyEstimate] = deriveObjectType[Unit, EoyEstimate](
    ReplaceField("employments", Field("employments", OptionType(ListType(EoyEmploymentType)), resolve = c => c.value.employments)),
    ReplaceField("selfEmployments", Field("selfEmployments", OptionType(ListType(EoySelfEmploymentType)), resolve = c => c.value.selfEmployments)),
    ReplaceField("ukProperty", Field("ukProperty", OptionType(EoyItemType), resolve = c => c.value.ukProperty)),
    ReplaceField("ukDividends", Field("ukDividends", OptionType(EoyItemType), resolve = c => c.value.ukDividends)),
    ReplaceField("savings", Field("savings", OptionType(ListType(EoySavingsType)), resolve = c => c.value.savings))
  )

  def EoyEmploymentType(implicit responseData: TaxCalculation): ObjectType[Unit, EoyEmployment] = deriveObjectType[Unit, EoyEmployment]()

  def EoySelfEmploymentType(implicit responseData: TaxCalculation): ObjectType[Unit, EoySelfEmployment] = deriveObjectType[Unit, EoySelfEmployment]()

  def EoyItemType(implicit responseData: TaxCalculation): ObjectType[Unit, EoyItem] = deriveObjectType[Unit, EoyItem]()

  def EoySavingsType(implicit responseData: TaxCalculation): ObjectType[Unit, EoySavings] = deriveObjectType[Unit, EoySavings]()

  def CalculationMessageType(implicit responseData: TaxCalculation): ObjectType[Unit, CalculationMessage] = deriveObjectType[Unit, CalculationMessage]()

  def AnnualAllowancesType(implicit responseData: TaxCalculation): ObjectType[Unit, AnnualAllowances] = deriveObjectType[Unit, AnnualAllowances]()


}