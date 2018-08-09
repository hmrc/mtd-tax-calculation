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

package v2.models

import play.api.libs.json.{JsValue, Json}
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class EmploymentSpec extends JsonErrorValidators with UnitSpec {

  import JsonError._

  val employment = Employment(
    employmentId = "ABIS10000000001",
    netPay = 1000.25,
    benefitsAndExpenses = 1000.25,
    allowableExpenses = 1000.25
  )

  val employmentDesJson: String =
    """
      |{
      |  "incomeSourceID": "ABIS10000000001",
      |  "netPay": 1000.25,
      |  "benefitsAndExpenses": 1000.25,
      |  "allowableExpenses": 1000.25
      |}
    """.stripMargin

  val emptyEmploymentJson: JsValue = Json.parse("{}")

  val employmentClientJson: JsValue = Json.parse(
    """
      |{
      |  "employmentId": "ABIS10000000001",
      |  "netPay": 1000.25,
      |  "benefitsAndExpenses": 1000.25,
      |  "allowableExpenses": 1000.25
      |}
    """.stripMargin)

  "reads" should {

    "return correct validation errors" when {

      testMandatoryProperty[Employment](employmentDesJson)(property = "incomeSourceID")
      testMandatoryProperty[Employment](employmentDesJson)(property = "netPay")
      testMandatoryProperty[Employment](employmentDesJson)(property = "benefitsAndExpenses")
      testMandatoryProperty[Employment](employmentDesJson)(property = "allowableExpenses")

      testPropertyType[Employment](employmentDesJson)(
        property = "incomeSourceID",
        invalidValue = "6",
        errorPathAndError = "/incomeSourceID" -> STRING_FORMAT_EXCEPTION
      )

      testPropertyType[Employment](employmentDesJson)(
        property = "netPay",
        invalidValue = "\"nan\"",
        errorPathAndError = "/netPay" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[Employment](employmentDesJson)(
        property = "benefitsAndExpenses",
        invalidValue = "\"nan\"",
        errorPathAndError = "/benefitsAndExpenses" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[Employment](employmentDesJson)(
        property = "allowableExpenses",
        invalidValue = "\"nan\"",
        errorPathAndError = "/allowableExpenses" -> NUMBER_FORMAT_EXCEPTION
      )
    }
  }

  "return a successfully read employment model" when {
    "all fields exist" in {
      Employment.reads.reads(Json.parse(employmentDesJson)).get shouldBe employment
    }

  }

  "writes" should {
    "return client Json" when {
      "all fields exist" in {
        Employment.writes.writes(employment) shouldBe employmentClientJson
      }
    }
  }
}
