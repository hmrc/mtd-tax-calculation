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

package v2.models.utils

import org.scalatest.Matchers
import play.api.data.validation.ValidationError
import play.api.libs.json.JsPath

trait JsonErrorValidators {
  _: Matchers =>

  def singleJsonErrorValidator(errorPath: String, errorMessage: String, error: (JsPath,Seq[ValidationError])): Unit = {
    val (path, validationError) = error
    path.path.head.toString shouldBe errorPath
    validationError.head.message shouldBe errorMessage
  }

  def multipleJsonErrorValidator(error: Seq[(JsPath,Seq[ValidationError])])(errorPathAndMessage: (String, String)*): Unit = {
    val mappedErrors: Seq[(String, String)] = error.map{
      case (path, validationError) => path.path.head.toString -> validationError.head.message
    }
    mappedErrors should contain theSameElementsAs errorPathAndMessage
  }
}
