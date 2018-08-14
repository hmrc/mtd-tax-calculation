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

package v2.connectors

import javax.inject.{Inject, Singleton}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import v2.config.AppConfig
import v2.outcomes.TaxCalcOutcome.TaxCalcOutcome

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TaxCalcConnector @Inject()(http: HttpClient,
                                 implicit val appConfig: AppConfig) extends DesConnector {

  def getTaxCalculation(mtdId: String, calculationId: String)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[TaxCalcOutcome] = {
    import v2.httpparsers.TaxCalcHttpParser.taxCalcHttpReads
    http.GET[TaxCalcOutcome](s"${appConfig.desBaseUrl}/income-tax/calculation-data/$mtdId/calcId/$calculationId")(implicitly, hc.withDesHeaders(), ec)
  }
}
