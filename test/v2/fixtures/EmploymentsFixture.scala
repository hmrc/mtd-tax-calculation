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

package v2.fixtures

import play.api.libs.json.{JsValue, Json}
import v2.models.Employments

trait EmploymentsFixture extends EmploymentFixture {

  val employments = Employments(
    totalIncome = Some(1000.25),
    totalPay = 1000.25,
    totalBenefitsAndExpenses = 1000.25,
    totalAllowableExpenses = 1000.25,
    employment = Seq(
      employment
    )
  )

  val employmentsClientJson: JsValue = Json.parse(
    s"""
       |{
       |  "totalIncome": 1000.25,
       |  "totalPay": 1000.25,
       |  "totalBenefitsAndExpenses": 1000.25,
       |  "totalAllowableExpenses": 1000.25,
       |  "employment": [
       |    $employmentClientJson
       |  ]
       |}
    """.stripMargin)

  val employmentsDesJsonString: String =
    s"""
       |{
       |  "employmentIncome": 1000.25,
       |  "employments": {
       |    "totalPay": 1000.25,
       |    "totalBenefitsAndExpenses": 1000.25,
       |    "totalAllowableExpenses": 1000.25,
       |    "employment": [
       |    $employmentDesJson]
       |  }
       |}
    """.stripMargin

  val employmentsDesJson: JsValue = Json.parse(employmentsDesJsonString)

}
