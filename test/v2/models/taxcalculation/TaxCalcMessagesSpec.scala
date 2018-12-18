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

package v2.models.taxcalculation

import play.api.libs.json.{JsObject, JsValue, Json}
import support.UnitSpec
import v2.models.{Message, TaxCalcMessages}
import v2.models.utils.JsonErrorValidators
import v2.fixtures.{TaxCalcMessagesFixture => TestData}

class TaxCalcMessagesSpec extends JsonErrorValidators with UnitSpec {

  "reads" should {
    import JsonError._

    "return correct validation errors" when {

      testMandatoryProperty[TaxCalcMessages](Json.stringify(TestData.taxCalcDesWarningsAndErrors))(property = "bvrWarnings")
      testMandatoryProperty[TaxCalcMessages](Json.stringify(TestData.taxCalcDesWarningsAndErrors))(property = "bvrErrors")

      testMandatoryProperty[Message](TestData.bvrDesMessageString)(property = "id")
      testMandatoryProperty[Message](TestData.bvrDesMessageString)(property = "type")
      testMandatoryProperty[Message](TestData.bvrDesMessageString)(property = "text")

      testPropertyType[TaxCalcMessages](TestData.taxCalcDesWarningsAndErrors)(
        path = "/calcOutput/bvrWarnings",
        replacement = "test".toJson,
        expectedError = JSNUMBER_FORMAT_EXCEPTION
      )
      testPropertyType[TaxCalcMessages](TestData.taxCalcDesWarningsAndErrors)(
        path = "/calcOutput/bvrErrors",
        replacement = "test".toJson,
        expectedError = JSNUMBER_FORMAT_EXCEPTION
      )
      testPropertyType[TaxCalcMessages](TestData.taxCalcDesWarningsAndErrors)(
        path = "/calcOutput/bvrMsg",
        replacement = "test".toJson,
        expectedError = JSARRAY_FORMAT_EXCEPTION
      )

      testPropertyType[Message](TestData.bvrDesMessageString)(
        property = "id",
        invalidValue = "1",
        errorPathAndError = ".id" -> STRING_FORMAT_EXCEPTION
      )
      testPropertyType[Message](TestData.bvrDesMessageString)(
        property = "type",
        invalidValue = "1",
        errorPathAndError = ".type" -> STRING_FORMAT_EXCEPTION
      )
      testPropertyType[Message](TestData.bvrDesMessageString)(
        property = "text",
        invalidValue = "1",
        errorPathAndError = ".text" -> STRING_FORMAT_EXCEPTION
      )
    }

    "return a valid json" when {
      "all fields exist" in {
        TaxCalcMessages.reads.reads(TestData.taxCalcDesWarningsAndErrors).get shouldBe TestData.taxCalcMessages
        Message.reads.reads(Json.parse(TestData.bvrDesMessageString)).get shouldBe TestData.bvrMessage
      }

      "only mandatory fields exist" in {
        TaxCalcMessages.reads.reads(TestData.taxCalcDesNoWarningsAndErrors).get shouldBe TestData.emptyTaxCalcMessages
      }

      "empty bvrmsg exist" in {
        TaxCalcMessages.reads.reads(TestData.taxCalcDesEmptyWarningsAndErrors).get shouldBe TestData.emptyTaxCalcMessages
      }
    }
  }

  "writes" should {
    "return client json" when {
      "all fields exist" in {
        Json.toJson(TestData.taxCalcMessages) shouldBe TestData.taxCalcMessagesWithWarningsAndErrors
        Json.toJson(TestData.bvrMessage) shouldBe Json.parse(TestData.bvrMessageString)
      }
    }

    "not render the messages object" when {
      "it has no value" in {
        val model = TestData.taxCalcMessages.copy(messages = None)
        val json: JsValue = TestData.taxCalcMessagesWithWarningsAndErrors.as[JsObject] - "messages"
        Json.toJson(model) shouldBe json
      }
    }
  }
}
