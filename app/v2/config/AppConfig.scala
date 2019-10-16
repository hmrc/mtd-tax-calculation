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

package v2.config

import javax.inject.{Inject, Singleton}
import play.api.{Configuration, Environment}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

trait AppConfig {
  def desBaseUrl: String
  def mtdIdBaseUrl: String
  def desEnv: String
  def desToken: String
  def featureSwitch: Option[Configuration]
}

@Singleton
class AppConfigImpl @Inject()(servicesConfig: ServicesConfig, environment: Environment,
                          config: Configuration) extends AppConfig {

  val desBaseUrl: String = servicesConfig.baseUrl("des")
  val mtdIdBaseUrl: String = servicesConfig.baseUrl("mtd-id-lookup")
  val desEnv: String = servicesConfig.getString("microservice.services.des.env")
  val desToken: String = servicesConfig.getString("microservice.services.des.token")

  def featureSwitch: Option[Configuration] = config.getOptional[Configuration](s"feature-switch")

}
