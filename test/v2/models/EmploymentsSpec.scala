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

import play.api.libs.json.{JsValue, Json}
import support.UnitSpec
import v2.fixtures.EmploymentsFixture
import v2.models.utils.JsonErrorValidators

class EmploymentsSpec extends UnitSpec
  with JsonErrorValidators
  with EmploymentsFixture {

  import JsonError._

  "reads" should {

    testMandatoryProperty[Employments](employmentsDesJsonString)(property = "totalPay")
    testMandatoryProperty[Employments](employmentsDesJsonString)(property = "totalBenefitsAndExpenses")
    testMandatoryProperty[Employments](employmentsDesJsonString)(property = "totalAllowableExpenses")

    testPropertyType[Employments](employmentsDesJson)(
      path = "/employments/totalPay",
      replacement = "test".toJson,
      expectedError = NUMBER_FORMAT_EXCEPTION
    )

    testPropertyType[Employments](employmentsDesJson)(
      path = "/employments/totalBenefitsAndExpenses",
      replacement = "test".toJson,
      expectedError = NUMBER_FORMAT_EXCEPTION
    )

    testPropertyType[Employments](employmentsDesJson)(
      path = "/employments/totalAllowableExpenses",
      replacement = "test".toJson,
      expectedError = NUMBER_FORMAT_EXCEPTION
    )

    testPropertyType[Employments](employmentsDesJson)(
      path = "/employments/employment",
      replacement = "test".toJson,
      expectedError = JSARRAY_FORMAT_EXCEPTION
    )

    "return a successfully read employments model" when {
      "all fields are valid" in {
        val result = Employments.reads.reads(employmentsDesJson).get
        result shouldBe employments
      }
    }

    "return validation errors" when {
      "all fields are missing" in {
        val emptyEmploymentsJson: JsValue = Json.obj()
        val Left(errors) = Employments.reads.reads(emptyEmploymentsJson).asEither
      }
    }
  }

  "writes" should {
    "return correct client Json" when {
      "a valid employments model is supplied" in {
        val result = Employments.writes.writes(employments)
        result shouldBe employmentsClientJson
      }
    }
  }
}
