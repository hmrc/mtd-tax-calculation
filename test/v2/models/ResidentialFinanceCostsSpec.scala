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

class ResidentialFinanceCostsSpec extends UnitSpec with JsonErrorValidators {

  val validDesData: String =
    """
      |{
      |     "amountClaimed": 1000.25,
      |     "allowableAmount": 1000.25,
      |     "rate": 10.25
      |}
    """.stripMargin

  val validModel = ResidentialFinanceCosts(
    amountClaimed = 1000.25,
    allowableAmount = Some(1000.25),
    rate = 10.25
  )

  val validClientData: JsValue = Json.parse(validDesData)


  "reads" should {

    import JsonError._

    "return correct validation errors" when {
      testMandatoryProperty[ResidentialFinanceCosts](validDesData)(property = "amountClaimed")
      testMandatoryProperty[ResidentialFinanceCosts](validDesData)(property = "rate")

      testPropertyType[ResidentialFinanceCosts](validDesData)(
        property = "amountClaimed",
        invalidValue = "\"string\"",
        errorPathAndError = "/amountClaimed" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[ResidentialFinanceCosts](validDesData)(
        property = "allowableAmount",
        invalidValue = "\"string\"",
        errorPathAndError = "/allowableAmount" -> NUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[ResidentialFinanceCosts](validDesData)(
        property = "rate",
        invalidValue = "\"string\"",
        errorPathAndError = "/rate" -> NUMBER_FORMAT_EXCEPTION
      )

    }

    "return a valid model" when {
      "all fields exist" in {
        Json.parse(validDesData).as[ResidentialFinanceCosts] shouldBe validModel
      }
    }
  }

  "writes" should {
    "return valid json" when {
      "passed a valid model" in {
        Json.toJson(validModel) shouldBe validClientData
      }
    }
  }
}

