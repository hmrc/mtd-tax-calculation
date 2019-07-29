# MTD Tax Calualation Service

[![Build Status](https://travis-ci.org/hmrc/mtd-tax-calculation.svg)](https://travis-ci.org/hmrc/mtd-tax-calculation) [ ![Download](https://api.bintray.com/packages/hmrc/releases/mtd-tax-calculation/images/download.svg) ](https://bintray.com/hmrc/releases/mtd-tax-calculation/_latestVersion)

This service retrieves the MTD tax calculation.

sbt "~run 9770"

## GRAPHQL ENDPOINT

Hit `POST /ni/:nino/calculations/:calcId` with your graphql request in the request body.

Schema:

```
type AllowancesAndDeductions {
  totalAllowancesAndDeductions: BigDecimal!
  giftOfInvestmentsAndPropertyToCharity: BigDecimal
  apportionedPersonalAllowance: BigDecimal!
}

type AllowancesAndReliefs {
  propertyFinanceRelief: BigDecimal
  totalAllowancesAndReliefs: BigDecimal!
}

type AnnualAllowances {
  personalAllowance: Long!
  personalAllowanceThreshold: Long
  reducedPersonalAllowance: Long
  giftAidExtender: Long
}

type CalculationMessage {
  type: String!
  text: String!
}

type Class2Nic {
  amount: BigDecimal!
  weekRate: BigDecimal!
  weeks: Int!
  limit: Long!
  apportionedLimit: Long!
}

type Class4Nic {
  totalAmount: BigDecimal!
  band: [NicBand!]!
}

type Employment {
  employmentId: String!
  netPay: BigDecimal!
  benefitsAndExpenses: BigDecimal!
  allowableExpenses: BigDecimal!
}

type Employments {
  totalIncome: BigDecimal
  totalPay: BigDecimal!
  totalBenefitsAndExpenses: BigDecimal!
  totalAllowableExpenses: BigDecimal!
  employment: [Employment!]
}

type EoyEmployment {
  employmentId: String!
  taxableIncome: BigDecimal!
  supplied: Boolean!
  finalised: Boolean
}

type EoyEstimate {
  totalTaxableIncome: BigDecimal!
  incomeTaxAmount: BigDecimal!
  nic2: BigDecimal!
  nic4: BigDecimal!
  totalNicAmount: BigDecimal!
  incomeTaxNicAmount: BigDecimal!
  employments: [EoyEmployment!]
  selfEmployments: [EoySelfEmployment!]
  ukProperty: EoyItem
  ukDividends: EoyItem
  savings: [EoySavings!]
}

type EoyItem {
  taxableIncome: BigDecimal!
  supplied: Boolean!
  finalised: Boolean
}

type EoySavings {
  savingsAccountId: String!
  taxableIncome: BigDecimal!
  supplied: Boolean!
}

type EoySelfEmployment {
  selfEmploymentId: String!
  taxableIncome: BigDecimal!
  supplied: Boolean!
  finalised: Boolean
}

type GiftAid {
  paymentsMade: BigDecimal!
  rate: BigDecimal!
  taxableAmount: BigDecimal!
}

type IncomeTax {
  taxableIncome: BigDecimal!
  totalBeforeReliefs: BigDecimal!
  totalAfterReliefs: BigDecimal!
  totalAfterGiftAid: BigDecimal
  totalIncomeTax: BigDecimal!
  payAndPensionsProfit: IncomeTaxItem
  savingsAndGains: IncomeTaxItem
  dividends: IncomeTaxItem
  allowancesAndReliefs: AllowancesAndReliefs
  giftAid: GiftAid
  residentialFinanceCosts: ResidentialFinanceCosts
}

type IncomeTaxBand {
  name: String!
  rate: BigDecimal!
  threshold: Long
  apportionedThreshold: Long
  bandLimit: Long
  apportionedBandLimit: Long
  income: BigDecimal!
  amount: BigDecimal!
}

type IncomeTaxItem {
  totalAmount: BigDecimal!
  personalAllowanceUsed: BigDecimal
  taxableIncome: BigDecimal!
  band: [IncomeTaxBand!]!
}

type Nic {
  totalNic: BigDecimal!
  class2: Class2Nic
  class4: Class4Nic
}

type NicBand {
  name: String!
  rate: BigDecimal!
  threshold: Long
  apportionedThreshold: Long
  income: BigDecimal!
  amount: BigDecimal!
}

type ResidentialFinanceCosts {
  amountClaimed: BigDecimal!
  allowableAmount: BigDecimal
  rate: BigDecimal!
}

type SavingsIncome {
  totalIncome: BigDecimal
  totalTaxedInterestIncome: BigDecimal
  totalUntaxedInterestIncome: BigDecimal
  taxedAccounts: [TaxedSavingsAccount!]
  untaxedAccounts: [UntaxedSavingsAccount!]
}

type SelfEmployment {
  selfEmploymentId: String!
  taxableIncome: BigDecimal!
  finalised: Boolean
  losses: BigDecimal
}

type SelfEmployments {
  totalIncome: BigDecimal
  selfEmployment: [SelfEmployment!]
}

type TaxDeducted {
  ukLandAndProperty: BigDecimal
  savings: BigDecimal
  totalTaxDeducted: BigDecimal
}

type TaxableIncome {
  totalIncomeReceived: BigDecimal!
  totalTaxableIncome: BigDecimal
  employments: Employments
  selfEmployments: SelfEmployments
  ukProperty: UKProperty
  ukDividends: UKDividends
  savings: SavingsIncome
  allowancesAndDeductions: AllowancesAndDeductions
}

type TaxedSavingsAccount {
  savingsAccountId: String!
  name: String
  gross: BigDecimal
  net: BigDecimal
  taxDeducted: BigDecimal
}

type UKDividends {
  totalIncome: BigDecimal
  ukDividends: BigDecimal
  otherUkDividends: BigDecimal
}

type UKProperty {
  totalIncome: BigDecimal
  nonFurnishedHolidayLettingsTaxableProfit: BigDecimal
  nonFurnishedHolidayLettingsLoss: BigDecimal
  furnishedHolidayLettingsTaxableProfit: BigDecimal
  furnishedHolidayLettingsLoss: BigDecimal
  finalised: Boolean
}

type UntaxedSavingsAccount {
  savingsAccountId: String!
  name: String
  gross: BigDecimal
}

---------------------------------------------------------

type TaxCalculation {
  year: Int
  intentToCrystallise: Boolean!
  crystallised: Boolean!
  validationMessageCount: Int!
  incomeTaxAndNicYTD: BigDecimal
  nationalRegime: String
  totalBeforeTaxDeducted: BigDecimal
  calculationMessageCount: Int
  taxableIncome: TaxableIncome
  incomeTax: IncomeTax
  nic: Nic
  taxDeducted: TaxDeducted
  eoyEstimate: EoyEstimate
  calculationMessages: [CalculationMessage!]
  annualAllowances: AnnualAllowances
}

type Query {
  "Returns the tax calculation"
  taxCalc: TaxCalculation!
}
```

### License 

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")

