/*
 * Copyright 2020 HM Revenue & Customs
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

class Class2NicSpec extends JsonErrorValidators with UnitSpec {

  import JsonError._

  val class2Nic =
    Class2Nic(
      amount = 123.45,
      weekRate = 123.45,
      weeks = 12,
      limit = 12345,
      apportionedLimit = 12345
    )

  val class2NicDesJson: String =
    """
      |{
      | "amount": 123.45,
      | "weekRate": 123.45,
      | "weeks": 12,
      | "limit": 12345,
      | "apportionedLimit": 12345
      |}
      |""".stripMargin

  "reads" should {
    "return correct validation errors" when {

      testMandatoryProperty[Class2Nic](class2NicDesJson)("amount")
      testMandatoryProperty[Class2Nic](class2NicDesJson)("weekRate")
      testMandatoryProperty[Class2Nic](class2NicDesJson)("weeks")
      testMandatoryProperty[Class2Nic](class2NicDesJson)("limit")
      testMandatoryProperty[Class2Nic](class2NicDesJson)("apportionedLimit")

      testPropertyType[Class2Nic](class2NicDesJson)(
        property = "amount",
        invalidValue = "\"nan\"",
        errorPathAndError = "/amount" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[Class2Nic](class2NicDesJson)(
        property = "weekRate",
        invalidValue = "\"nan\"",
        errorPathAndError = "/weekRate" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[Class2Nic](class2NicDesJson)(
        property = "weeks",
        invalidValue = "\"nan\"",
        errorPathAndError = "/weeks" -> JSNUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[Class2Nic](class2NicDesJson)(
        property = "limit",
        invalidValue = "\"nan\"",
        errorPathAndError = "/limit" -> JSNUMBER_FORMAT_EXCEPTION
      )
      testPropertyType[Class2Nic](class2NicDesJson)(
        property = "apportionedLimit",
        invalidValue = "\"nan\"",
        errorPathAndError = "/apportionedLimit" -> JSNUMBER_FORMAT_EXCEPTION
      )
    }

    "return a correctly read Class2Nic model" when {
      "all fields exist" in {
        Class2Nic.reads.reads(Json.parse(class2NicDesJson)).get shouldBe class2Nic
      }
    }
  }

  "write" should {
    "return client json" when {
      "all fields exist" in {

        val clientJson = Json.parse(
          s"""
             |{
             | "amount": 123.45,
             | "weekRate": 123.45,
             | "weeks": 12,
             | "limit": 12345,
             | "apportionedLimit": 12345
             |}
           """.stripMargin)

        Class2Nic.writes.writes(class2Nic) shouldBe clientJson
      }
    }
  }
}
