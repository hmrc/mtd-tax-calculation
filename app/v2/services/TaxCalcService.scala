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

package v2.services

import javax.inject.{Inject, Singleton}
import play.api.Logger
import uk.gov.hmrc.http.HeaderCarrier
import v2.connectors.TaxCalcConnector
import v2.models.errors.InvalidCalcID
import v2.outcomes.TaxCalcOutcome.TaxCalcOutcome

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TaxCalcService @Inject()(connector: TaxCalcConnector) {

  private val calcIdRegex = "^[0-9]{8}$|^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"

  def getTaxCalculation(mtdid: String, calcId: String)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[TaxCalcOutcome] = {
    if (!calcId.matches(calcIdRegex)) {
      Logger.warn(s"[TaxCalcService] [getTaxCalculation] Invalid CalculationID supplied.")
      Future.successful(Left(InvalidCalcID))
    }
    else {
      connector.getTaxCalculation(mtdid, calcId)
    }
  }
}