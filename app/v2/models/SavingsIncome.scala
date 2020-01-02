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
import play.api.libs.json.{Json, Reads, Writes, _}

case class TaxedSavingsAccount(
                                savingsAccountId: String,
                                name: Option[String],
                                gross: Option[BigDecimal],
                                net: Option[BigDecimal],
                                taxDeducted: Option[BigDecimal]
                              )

object TaxedSavingsAccount {
  implicit val writes: Writes[TaxedSavingsAccount] = Json.writes[TaxedSavingsAccount]
  implicit val reads: Reads[TaxedSavingsAccount] = (
    (__ \ "incomeSourceID").read[String] and
      (__ \ "name").readNullable[String] and
      (__ \ "gross").readNullable[BigDecimal] and
      (__ \ "net").readNullable[BigDecimal] and
      (__ \ "taxDeducted").readNullable[BigDecimal]
    ) (TaxedSavingsAccount.apply _)
}


case class UntaxedSavingsAccount(
                                  savingsAccountId: String,
                                  name: Option[String],
                                  gross: Option[BigDecimal]
                                )

object UntaxedSavingsAccount {
  implicit val writes: Writes[UntaxedSavingsAccount] = Json.writes[UntaxedSavingsAccount]
  implicit val reads: Reads[UntaxedSavingsAccount] = (
    (__ \ "incomeSourceID").read[String] and
      (__ \ "name").readNullable[String] and
      (__ \ "gross").readNullable[BigDecimal]
    ) (UntaxedSavingsAccount.apply _)
}


case class SavingsIncome(
                          totalIncome: Option[BigDecimal],
                          totalTaxedInterestIncome: Option[BigDecimal],
                          totalUntaxedInterestIncome: Option[BigDecimal],
                          taxedAccounts: Option[Seq[TaxedSavingsAccount]],
                          untaxedAccounts: Option[Seq[UntaxedSavingsAccount]])

object SavingsIncome {
  implicit val writes: Writes[SavingsIncome] = Json.writes[SavingsIncome]
  implicit val reads: Reads[SavingsIncome] = (
    (__ \ "bbsiIncome").readNullable[BigDecimal] and
      (__ \ "bbsi" \ "totalTaxedInterestIncome").readNestedNullable[BigDecimal] and
      (__ \ "bbsi" \"totalUntaxedInterestIncome").readNestedNullable[BigDecimal] and
      (__ \ "bbsi" \"taxedAccounts").readNestedNullable[Seq[TaxedSavingsAccount]] and
      (__ \ "bbsi" \"untaxedAccounts").readNestedNullable[Seq[UntaxedSavingsAccount]]
    ) (SavingsIncome.apply _)
}


