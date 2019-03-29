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

package v2.services

import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.json.Reads
import uk.gov.hmrc.http.HeaderCarrier
import v2.connectors.TaxCalcConnector
import v2.models.errors.InvalidCalcIDError
import v2.outcomes.TaxCalcOutcome.Outcome

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TaxCalcService @Inject()(connector: TaxCalcConnector) {

  private val calcIdRegex = "^[0-9]{8}$|^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"

  def getTaxCalculation[A:Reads](nino: String, calcId: String)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Outcome[A]] =
    get[A](nino,calcId)(connector.getTaxCalculation)

  def getTaxCalculationMessages[A:Reads](nino: String, calcId: String)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Outcome[A]] =
    get[A](nino, calcId)(connector.getTaxCalculationMessages)

  private def get[A:Reads](nino: String, calcId: String)
                            (f: (String,String) => Future[Outcome[A]])(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Outcome[A]] = {
    if (!calcId.matches(calcIdRegex)) {
      Logger.warn(s"[TaxCalcService] [get] Invalid CalculationID supplied.")
      Future.successful(Left(InvalidCalcIDError))
    } else {
      f(nino,calcId)
    }
  }
}
