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

import play.api.libs.json.{JsValue, Json}
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class NicBandSpec extends JsonErrorValidators with UnitSpec {

  import JsonError._

  val exampleBigDecimal = 123.45
  val exampleInt = 123

  val nicBand =
    NicBand(
      name = "ZRT",
      rate = exampleBigDecimal,
      threshold = Some(exampleInt),
      apportionedThreshold = Some(exampleInt),
      income = exampleBigDecimal,
      amount = exampleBigDecimal
    )

  val mandatoryNicBand =
    NicBand(
      name = "ZRT",
      rate = exampleBigDecimal,
      threshold = None,
      apportionedThreshold = None,
      income = exampleBigDecimal,
      amount = exampleBigDecimal
    )

  val emptyJson: JsValue = Json.parse("{}")

  val nicBandJson: String =
    s"""
       |				{
       |					"name": "ZRT",
       |					"rate": 123.45,
       |					"threshold": 123,
       |					"apportionedThreshold": 123,
       |					"income": 123.45,
       |					"amount": 123.45
       |				}
           """.stripMargin

  val nicBandDesJson: JsValue = Json.parse(
    s"""
       |				{
       |					"name": "ZRT",
       |					"rate": 123.45,
       |					"threshold": 123,
       |					"apportionedThreshold": 123,
       |					"income": 123.45,
       |					"amount": 123.45
       |				}
           """.stripMargin)

  val mandatoryOnlyNicBandDesJson: JsValue = Json.parse(
    s"""
       |				{
       |					"name": "ZRT",
       |					"rate": 123.45,
       |					"income": 123.45,
       |					"amount": 123.45
       |				}
           """.stripMargin)

  val noOptionalNicBand = NicBand("ZRT", 123.45, None, None, 123.45, 123.45)

  "reads" should {
    "return correct validation errors" when {

      val mandatoryFieldTest = testMandatoryProperty[NicBand](nicBandJson) _

      mandatoryFieldTest("name")
      mandatoryFieldTest("rate")
      mandatoryFieldTest("income")
      mandatoryFieldTest("amount")

      testPropertyType[NicBand](nicBandJson)(
        property = "name",
        invalidValue = "123",
        errorPathAndError = "/name" -> STRING_FORMAT_EXCEPTION
      )

      testPropertyType[NicBand](nicBandJson)(
        property = "rate",
        invalidValue = "\"nan\"",
        errorPathAndError = "/rate" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[NicBand](nicBandJson)(
        property = "threshold",
        invalidValue = "\"nan\"",
        errorPathAndError = "/threshold" -> JSNUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[NicBand](nicBandJson)(
        property = "apportionedThreshold",
        invalidValue = "\"nan\"",
        errorPathAndError = "/apportionedThreshold" -> JSNUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[NicBand](nicBandJson)(
        property = "income",
        invalidValue = "\"nan\"",
        errorPathAndError = "/income" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[NicBand](nicBandJson)(
        property = "amount",
        invalidValue = "\"nan\"",
        errorPathAndError = "/amount" -> NUMBER_FORMAT_EXCEPTION
      )
    }

    "return a correctly read NicBand model" when {
      "all fields exist" in {

        NicBand.reads.reads(nicBandDesJson).get shouldBe nicBand
      }
      "optional fields don't exist" in {

        NicBand.reads.reads(mandatoryOnlyNicBandDesJson).get shouldBe mandatoryNicBand
      }
    }
  }

  "write" should {
    "return client json" when {
      "all fields exist" in {

        NicBand.writes.writes(nicBand) shouldBe nicBandDesJson
      }
      "only mandatory fields exist" in {

        NicBand.writes.writes(mandatoryNicBand) shouldBe mandatoryOnlyNicBandDesJson
      }
    }
  }
}

