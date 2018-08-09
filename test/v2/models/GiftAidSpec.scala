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

import play.api.libs.json.Json
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class GiftAidSpec extends JsonErrorValidators with UnitSpec {

  import JsonError._

  val giftAid =
    GiftAid(
      paymentsMade = 123.45,
      rate = 12.34,
      taxableAmount = 123.45
    )

  val giftAidDesJson: String =
    """
      |{
      | "paymentsMade": 123.45,
      | "rate": 12.34,
      | "taxableAmount": 123.45
      |}
      |""".stripMargin

  "reads" should {
    "return correct validation errors" when {

      testMandatoryProperty[GiftAid](giftAidDesJson)("paymentsMade")
      testMandatoryProperty[GiftAid](giftAidDesJson)("rate")
      testMandatoryProperty[GiftAid](giftAidDesJson)("taxableAmount")

      testPropertyType[GiftAid](giftAidDesJson)(
        property = "paymentsMade",
        invalidValue = "\"nan\"",
        errorPathAndError = "/paymentsMade" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[GiftAid](giftAidDesJson)(
        property = "rate",
        invalidValue = "\"nan\"",
        errorPathAndError = "/rate" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[GiftAid](giftAidDesJson)(
        property = "taxableAmount",
        invalidValue = "\"nan\"",
        errorPathAndError = "/taxableAmount" -> NUMBER_FORMAT_EXCEPTION
      )
    }

    "return a correctly read GiftAid model" when {
      "all fields exist" in {
        val json = Json.parse(
          s"""
             |{
             | "paymentsMade":123.45,
             | "rate": 12.34,
             | "taxableAmount": 123.45
             |}
             """.stripMargin)

        GiftAid.reads.reads(json).get shouldBe giftAid
      }
    }
  }

  "write" should {
    "return client json" when {
      "all fields exist" in {

        val clientJson = Json.parse(
          s"""
             |{
             |  "paymentsMade": 123.45,
             |  "rate": 12.34,
             |  "taxableAmount": 123.45
             |}
           """.stripMargin)

        GiftAid.writes.writes(giftAid) shouldBe clientJson
      }
    }
  }
}
