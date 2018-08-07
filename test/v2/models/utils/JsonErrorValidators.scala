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

import org.scalatest.Assertion
import play.api.data.validation.ValidationError
import play.api.libs.json.{JsResultException, Json, Reads}
import support.UnitSpec

import scala.util.{Failure, Success, Try}

trait JsonErrorValidators {
  _: UnitSpec =>

  object JsonError {
    val NUMBER_FORMAT_EXCEPTION = "error.expected.numberformatexception"
    val PATH_MISSING_EXCEPTION = "error.path.missing"
    val BOOLEAN_FORMAT_EXCEPTION = "error.expected.jsboolean"
  }

  def testPropertyType[A](jsonString: String)(property: String, invalidValue: String, errorPathAndError: (String, String))
                         (implicit rd: Reads[A]): Unit = {

    val invalidTypedJson: String = jsonString.split('\n').map { line =>
      if (line.trim.startsWith(s""""$property""")) {
        s""""$property":$invalidValue,"""
      } else {
        line
      }
    }.mkString(" ").replace(",  }", " }")

    val errorPathJson = errorPathAndError._1.replace("/", ".")

    s"the JSON has the wrong data type for property $property" should {

      val json = Json.parse(invalidTypedJson)

      val result = Try(json.as[A])

      "only throw one error" in {
        result match {
          case Failure(e: JsResultException) => e.errors.size shouldBe 1
          case Success(s) => fail("A JSON error was expected")
        }
      }

      "throw the error against the correct property" in {
        result match {
          case Failure(e: JsResultException) =>
            val propertyName = getOnlyJsonErrorPath(e)
            if (propertyName.isRight) {
              propertyName.right.get should endWith (errorPathJson)
            }
          case _ => fail("A JSON error was expected")
        }
      }

      "throw an invalid type error" in {
        result match {
          case Failure(e: JsResultException) =>
            val message = getOnlyJsonErrorMessage(e)
            if (message.isRight) {
              message.right.get shouldBe errorPathAndError._2
            }
          case _ => fail("A JSON error was expected")
        }
      }
    }
  }

  private def getOnlyJsonErrorPath(ex: JsResultException): Either[Assertion, String] = {
    ex.errors match {
      case (jsonPath, _) :: Nil =>
        Right(jsonPath.toJsonString)
      case _ :: _ => Left(cancel("Too many JSON errors only expected one."))
    }
  }

  private def getOnlyJsonErrorMessage(ex: JsResultException): Either[Assertion, String] = {
    ex.errors match {
      case (_, ValidationError(onlyError :: Nil) :: Nil) :: Nil => Right(onlyError)
      case (_, ValidationError(_ :: _) :: Nil) :: Nil => Left(cancel("Too many error messages for property"))
      case _ :: _ => Left(cancel("Too many JSON errors only expected one."))
    }
  }
}
