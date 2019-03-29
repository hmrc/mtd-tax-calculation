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

package v2.models.old

import play.api.libs.json.{JsValue, Json}
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class SelfEmploymentsSpec extends UnitSpec with JsonErrorValidators {

  import JsonError._

  val validSelfEmploymentJson: String =
  """
    |{
    |     "incomeSourceID": "Some ID",
    |     "taxableIncome": 1234567.89,
    |     "finalised": true,
    |     "losses": 1234567.89
    |}
  """.stripMargin.replace("\n", "")

  val validSelfEmployment = SelfEmployment(
    selfEmploymentId = "Some ID",
    taxableIncome = BigDecimal("1234567.89"),
    finalised = Some(true),
    losses = Some(BigDecimal("1234567.89"))
  )

  val selfEmployments =
    SelfEmployments(
      totalIncome = Some(123.45),
      selfEmployment = Some(Seq(validSelfEmployment))
    )

  val selfEmploymentsDesJson: String =
    s"""
      |{
      | "selfEmploymentIncome": 123.45,
      | "selfEmployment" : [$validSelfEmploymentJson]
      |}
    """.stripMargin

  val emptySelfEmployments = SelfEmployments(None, None)

  val emptyJson: JsValue = Json.parse("{}")

  "reads" should {
    "return correct validation errors" when {

      testPropertyType[SelfEmployments](selfEmploymentsDesJson)(
        property = "selfEmploymentIncome",
        invalidValue = "\"nan\"",
        errorPathAndError = "/selfEmploymentIncome" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[SelfEmployments](selfEmploymentsDesJson)(
        property = "selfEmployment",
        invalidValue = "\"nan\"",
        errorPathAndError = "/selfEmployment" -> JSARRAY_FORMAT_EXCEPTION
      )
    }

    "return a correctly read SelfEmployments model" when {
      "all fields exist" in {
        SelfEmployments.reads.reads(Json.parse(selfEmploymentsDesJson)).get shouldBe selfEmployments
      }

      "optional fields don't exist" in {
        SelfEmployments.reads.reads(emptyJson).get shouldBe emptySelfEmployments
      }
    }
  }

  "write" should {
    "return client json" when {
      "all fields exist" in {

        val clientJson = Json.parse(
          s"""
             |{
             |  "totalIncome": 123.45,
             |  "selfEmployment" : [${Json.toJson(validSelfEmployment)}]
             |}
           """.stripMargin)

        SelfEmployments.writes.writes(selfEmployments) shouldBe clientJson
      }

      "all fields don't exist" in {
        SelfEmployments.writes.writes(emptySelfEmployments) shouldBe emptyJson
      }
    }
  }
}
