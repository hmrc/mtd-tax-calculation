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

class Class4NicSpec extends UnitSpec with JsonErrorValidators {

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

  val validClass4NicParsedModel =
    Class4Nic(
      totalAmount = exampleBigDecimal,
      band = Seq(nicBand)
    )

  val validClass4NicJson: String =
    """
      |{
      |     "totalAmount": 123.45,
      |     "band":[
      |     				{
      |					"name": "ZRT",
      |					"rate": 123.45,
      |					"threshold": 123,
      |					"apportionedThreshold": 123,
      |					"income": 123.45,
      |					"amount": 123.45
      |				}
      |      ]
      |}
    """.stripMargin

  val validClass4NicMultipleBandsJson: String =
    """
      |{
      |     "totalAmount": 123.45,
      |     "band":
      |     [
      |     	{
      |					"name": "ZRT",
      |					"rate": 123.45,
      |					"threshold": 123,
      |					"apportionedThreshold": 123,
      |					"income": 123.45,
      |					"amount": 123.45
      |				},
      |       {
      |					"name": "BRT",
      |					"rate": 123.45,
      |					"threshold": 123,
      |					"apportionedThreshold": 123,
      |					"income": 123.45,
      |					"amount": 123.45
      |				}
      |     ]
      |}
    """.stripMargin

  val validClass4NicJsonOutput: JsValue = Json.parse(
    """
      |{
      |     "totalAmount": 123.45,
      |     "band":[
      |     				{
      |					"name": "ZRT",
      |					"rate": 123.45,
      |					"threshold": 123,
      |					"apportionedThreshold": 123,
      |					"income": 123.45,
      |					"amount": 123.45
      |				}
      |      ]
      |}
    """.stripMargin)

  val validClass4NicMultipleBandsJsonOutput: JsValue = Json.parse(
    """
      |{
      |     "totalAmount": 123.45,
      |     "band":
      |     [
      |     	{
      |					"name": "ZRT",
      |					"rate": 123.45,
      |					"threshold": 123,
      |					"apportionedThreshold": 123,
      |					"income": 123.45,
      |					"amount": 123.45
      |				},
      |       {
      |					"name": "BRT",
      |					"rate": 123.45,
      |					"threshold": 123,
      |					"apportionedThreshold": 123,
      |					"income": 123.45,
      |					"amount": 123.45
      |				}
      |     ]
      |}
    """.stripMargin)

  val validClass4NicMultipleBandsParsedModel = Class4Nic(
    totalAmount = exampleBigDecimal,
    band = Seq(nicBand, nicBand.copy(name = "BRT"))
  )

  "reads" should {

    import JsonError._

    "return correct validation errors" when {
      testMandatoryProperty[Class4Nic](validClass4NicJson)(property = "totalAmount")
      testMandatoryProperty[Class4Nic](validClass4NicJson)(property = "band")

      testPropertyType[Class4Nic](validClass4NicJson)(
        property = "totalAmount",
        invalidValue = "\"some string\"",
        errorPathAndError = "/totalAmount" -> NUMBER_FORMAT_EXCEPTION
      )

    }
  }

  "successfully parse the JSON" when {
    "all fields exist" in {
      Json.parse(validClass4NicJson).as[Class4Nic] shouldBe validClass4NicParsedModel
    }

    "successfully parse the JSON" when {
      "all fields exist and multiple bands are passed" in {
        Json.parse(validClass4NicMultipleBandsJson).as[Class4Nic] shouldBe validClass4NicMultipleBandsParsedModel
      }
    }

    "writes" should {
      "return client Json" when {
        "all fields exist" in {
          Json.toJson(validClass4NicParsedModel) shouldBe validClass4NicJsonOutput
        }
      }
      "return client Json" when {
        "all fields exist and multiple bands are passed" in {
          Json.toJson(validClass4NicMultipleBandsParsedModel) shouldBe validClass4NicMultipleBandsJsonOutput
        }
      }
    }
  }
}

