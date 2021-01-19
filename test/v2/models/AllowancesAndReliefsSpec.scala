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

package v2.models

import play.api.libs.json.Json
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class AllowancesAndReliefsSpec extends JsonErrorValidators with UnitSpec {

  import JsonError._

  val allowancesAndReliefs =
    AllowancesAndReliefs(
      propertyFinanceRelief = Some(123.45),
      totalAllowancesAndReliefs = 123.45
    )

  val allowancesAndReliefsDesJson: String =
    """
      |{
      | "allowancesAndReliefs": {
      |     "propertyFinanceRelief": 123.45
      |  },
      |  "totalAllowancesAndReliefs": 123.45
      |}
    """.stripMargin

  "reads" should {
    "return correct validation errors" when {

      testMandatoryProperty[AllowancesAndReliefs](allowancesAndReliefsDesJson)("totalAllowancesAndReliefs")

      testPropertyType[AllowancesAndReliefs](allowancesAndReliefsDesJson)(
        property = "propertyFinanceRelief",
        invalidValue = "\"nan\"",
        errorPathAndError = "allowancesAndReliefs/propertyFinanceRelief" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[AllowancesAndReliefs](allowancesAndReliefsDesJson)(
        property = "totalAllowancesAndReliefs",
        invalidValue = "\"nan\"",
        errorPathAndError = "totalAllowancesAndReliefs" -> NUMBER_FORMAT_EXCEPTION
      )

    }

    "return a correctly read AllowancesAndReliefs model" when {
      "all fields exist" in {
        AllowancesAndReliefs.reads.reads(Json.parse(allowancesAndReliefsDesJson)).get shouldBe allowancesAndReliefs
      }
    }
  }

  "write" should {
    "return client json" when {
      "all fields exist" in {

        val clientJson = Json.parse(
          s"""
             |{
             |  "propertyFinanceRelief": 123.45,
             |  "totalAllowancesAndReliefs": 123.45
             |}
           """.stripMargin)

        AllowancesAndReliefs.writes.writes(allowancesAndReliefs) shouldBe clientJson
      }
      "only mandatory fields exist" in {
        val clientJson  = Json.parse(
          """
            |{
            | "totalAllowancesAndReliefs": 123.45
            |}
          """.stripMargin)

        val minAllowancesAndReliefs = allowancesAndReliefs.copy(propertyFinanceRelief = None)

        AllowancesAndReliefs.writes.writes(minAllowancesAndReliefs) shouldBe clientJson
      }
    }
  }
}
