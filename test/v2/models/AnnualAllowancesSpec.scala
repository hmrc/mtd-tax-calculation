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

package v2.models

import play.api.libs.json.{JsObject, JsValue, Json}
import support.UnitSpec
import v2.models.utils.JsonErrorValidators

class AnnualAllowancesSpec extends UnitSpec with JsonErrorValidators {

  val int = 123456789

  val validAnnualAllowancesInputJson: String =
    """
      |{
      |		"personalAllowance": 123456789,
      |		"reducedPersonalAllowanceThreshold": 123456789,
      |		"reducedPersonalisedAllowance": 123456789,
      |		"giftAidExtender": 123456789
      |}
    """.stripMargin

  val validMandatoryOnlyAnnualAllowancesInputJson: String =
    """
      |{
      |		"personalAllowance": 123456789
      |}
    """.stripMargin

  val validAnnualAllowancesModel = AnnualAllowances(
    personalAllowance = int,
    personalAllowanceThreshold = Some(int),
    reducedPersonalAllowance = Some(int),
    giftAidExtender = Some(int)
  )

  val validMandatoryAnnualAllowancesModel = AnnualAllowances(
    personalAllowance = int,
    personalAllowanceThreshold = None,
    reducedPersonalAllowance = None,
    giftAidExtender = None
  )

  val validAnnualAllowancesOutputJson: JsValue = Json.parse(
    """
      |{
      |		"personalAllowance": 123456789,
      |		"personalAllowanceThreshold": 123456789,
      |		"reducedPersonalAllowance": 123456789,
      |		"giftAidExtender": 123456789
      |}
    """.stripMargin
  )

  "reads" should {

    import JsonError._

    "return correct validation errors" when {
      testMandatoryProperty[AnnualAllowances](validAnnualAllowancesInputJson)(property = "personalAllowance")

      testPropertyType[AnnualAllowances](validAnnualAllowancesInputJson)(
        property = "personalAllowance",
        invalidValue = "\"some string\"",
        errorPathAndError = "/personalAllowance" -> JSNUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[AnnualAllowances](validAnnualAllowancesInputJson)(
        property = "reducedPersonalAllowanceThreshold",
        invalidValue = "\"some string\"",
        errorPathAndError = "/reducedPersonalAllowanceThreshold" -> JSNUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[AnnualAllowances](validAnnualAllowancesInputJson)(
        property = "reducedPersonalisedAllowance",
        invalidValue = "\"some string\"",
        errorPathAndError = "/reducedPersonalisedAllowance" -> JSNUMBER_FORMAT_EXCEPTION
      )

      testPropertyType[AnnualAllowances](validAnnualAllowancesInputJson)(
        property = "giftAidExtender",
        invalidValue = "\"some string\"",
        errorPathAndError = "/giftAidExtender" -> JSNUMBER_FORMAT_EXCEPTION
      )

    }
  }

  "return a successfully read Annual Allowances model" when {
    "all fields exist" in {
      Json.parse(validAnnualAllowancesInputJson).as[AnnualAllowances] shouldBe validAnnualAllowancesModel
    }

    "only mandatory fields exist" in {
      Json.parse(validMandatoryOnlyAnnualAllowancesInputJson).as[AnnualAllowances] shouldBe validMandatoryAnnualAllowancesModel
    }
  }

  "writes" should {
    "return client json" when {
      "all fields exist" in {
        Json.toJson(validAnnualAllowancesModel) shouldBe validAnnualAllowancesOutputJson
      }
    }

    "not render the personalAllowanceThreshold field" when {
      "it has no value" in {

        val model = validAnnualAllowancesModel.copy(personalAllowanceThreshold = None)
        val json: JsValue = validAnnualAllowancesOutputJson.as[JsObject] - "personalAllowanceThreshold"

        Json.toJson(model) shouldBe json
      }

      "not render the reducedPersonalAllowance field" when {
        "it has no value" in {

          val model = validAnnualAllowancesModel.copy(reducedPersonalAllowance = None)
          val json: JsValue = validAnnualAllowancesOutputJson.as[JsObject] - "reducedPersonalAllowance"

          Json.toJson(model) shouldBe json
        }

        "not render the giftAidExtender field" when {
          "it has no value" in {

            val model = validAnnualAllowancesModel.copy(giftAidExtender = None)
            val json: JsValue = validAnnualAllowancesOutputJson.as[JsObject] - "giftAidExtender"

            Json.toJson(model) shouldBe json
          }
        }
      }
    }
  }
}