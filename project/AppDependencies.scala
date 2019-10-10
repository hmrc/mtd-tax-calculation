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

import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "bootstrap-play-25" % "5.1.0",
    "uk.gov.hmrc" %% "domain" % "5.6.0-play-25",
    "org.typelevel" %% "cats-core" % "1.1.0"
  )

  def test(scope: String = "test, it"): Seq[sbt.ModuleID] = Seq(
    "uk.gov.hmrc" %% "hmrctest" % "3.9.0-play-25" % scope,
    "org.scalatest" %% "scalatest" % "3.0.4" % scope,
    "org.scalamock" %% "scalamock" % "4.1.0" % scope,
    "org.pegdown" % "pegdown" % "1.6.0" % scope,

    "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,

    "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % scope,

    "com.github.tomakehurst" % "wiremock" % "2.6.0" % scope
  )
}
