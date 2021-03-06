/*
 * Copyright 2021 HM Revenue & Customs
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

import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.logging.Authorization
import v2.config.AppConfig

trait DesConnector {

  implicit class DesHeaders(hc: HeaderCarrier)(implicit appConfig: AppConfig, correlationId: String) {
    def withDesHeaders(): HeaderCarrier = {
      hc
        .copy(authorization = Some(Authorization(s"Bearer ${appConfig.desToken}")))
        .withExtraHeaders(
          "Environment" -> appConfig.desEnv,
          "Originator-Id" -> "DA_SDI",
          "CorrelationId" -> correlationId
      )
    }
  }
}