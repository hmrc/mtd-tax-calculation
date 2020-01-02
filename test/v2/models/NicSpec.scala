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

import play.api.libs.json.{JsObject, JsValue, Json}
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class NicSpec extends JsonErrorValidators with UnitSpec {

  val standardString: String = "some string"
  val standardBigDecimal: BigDecimal = BigDecimal("1000.25")
  val standardInt: Int = 12

  val validInputJson: JsObject = Json.parse(
    s"""
       |{
       |  "totalNic": $standardBigDecimal,
       |  "nic": {
       |    "class2": {
       |      "amount": $standardBigDecimal,
       |      "weekRate": $standardBigDecimal,
       |      "weeks": $standardInt,
       |      "limit": $standardInt,
       |      "apportionedLimit": $standardInt
       |    },
       |    "class4": {
       |      "totalAmount": $standardBigDecimal,
       |      "band": [
       |        {
       |          "name": "$standardString",
       |          "rate": $standardBigDecimal,
       |          "threshold": $standardInt,
       |          "apportionedThreshold": $standardInt,
       |          "income": $standardBigDecimal,
       |          "amount": $standardBigDecimal
       |        }
       |      ]
       |    }
       |  }
       |}
    """.stripMargin
  ).as[JsObject]

  val validModel = Nic(
    totalNic = standardBigDecimal,
    class2 = Some(
      Class2Nic(
        amount = standardBigDecimal,
        weekRate = standardBigDecimal,
        weeks = standardInt,
        limit = standardInt,
        apportionedLimit = standardInt
      )
    ),
    class4 = Some(
      Class4Nic(
        totalAmount = standardBigDecimal,
        band = Seq(
          NicBand(
            name = standardString,
            rate = standardBigDecimal,
            threshold = Some(standardInt),
            apportionedThreshold = Some(standardInt),
            income = standardBigDecimal,
            amount = standardBigDecimal
          )
        )
      )
    )
  )

  val validOutputJason: JsObject = Json.parse(
    s"""
       |{
       |  "totalNic": $standardBigDecimal,
       |  "class2": {
       |    "amount": $standardBigDecimal,
       |    "weekRate": $standardBigDecimal,
       |    "weeks": $standardInt,
       |    "limit": $standardInt,
       |    "apportionedLimit": $standardInt
       |  },
       |  "class4": {
       |    "totalAmount": $standardBigDecimal,
       |    "band": [
       |      {
       |        "name": "$standardString",
       |        "rate": $standardBigDecimal,
       |        "threshold": $standardInt,
       |        "apportionedThreshold": $standardInt,
       |        "income": $standardBigDecimal,
       |        "amount": $standardBigDecimal
       |      }
       |    ]
       |  }
       |}
    """.stripMargin
  ).as[JsObject]

  "reads" should {
    "create the correct Nic object" when {
      "all fields are present" in {
        validInputJson.as[Nic] shouldBe validModel
      }

      "class2 field is missing" in {
        val nic = validInputJson.value("nic").as[JsObject] - "class2"
        val json = (validInputJson - "nic") + (("nic", nic))
        val model = validModel.copy(class2 = None)

        json.as[Nic] shouldBe model
      }

      "class4 field is missing" in {
        val nic = validInputJson.value("nic").as[JsObject] - "class4"
        val json = (validInputJson - "nic") + (("nic", nic))
        val model = validModel.copy(class4 = None)

        json.as[Nic] shouldBe model
      }

      "nic property containing class2/4 is missing" in {
        val json = validInputJson - "nic"
        val model = validModel.copy(class2 = None, class4 = None)

        json.as[Nic] shouldBe model
      }
    }

    "return correct validation errors" when {
      import JsonError._

      testMandatoryProperty[Nic](Json.prettyPrint(validInputJson))("totalNic")

      testPropertyType[Nic](Json.prettyPrint(validInputJson))(
        property = "totalNic",
        invalidValue = "true",
        errorPathAndError = "/totalNic" -> NUMBER_OR_STRING_FORMAT_EXCEPTION
      )
    }
  }

  "write" should {
    "return client json" when {

      "all fields exist" in {
        Json.toJson(validModel) shouldBe validOutputJason
      }

      "not render the class2 field" when {
        "it has no value" in {
          val model = validModel.copy(class2 = None)
          val json: JsValue = validOutputJason.as[JsObject] - "class2"

          Json.toJson(model) shouldBe json
        }
      }

      "not render the class4 field" when {
        "it has no value" in {
          val model = validModel.copy(class4 = None)
          val json: JsValue = validOutputJason.as[JsObject] - "class4"

          Json.toJson(model) shouldBe json
        }
      }
    }
  }

}
