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

package v2.models.utils

import org.scalatest.Assertion
import play.api.libs.json._
import support.UnitSpec

import scala.util.{Failure, Try}

trait JsonErrorValidators {
  _: UnitSpec =>

  object JsonError {
    val NUMBER_OR_STRING_FORMAT_EXCEPTION = "error.expected.jsnumberorjsstring"
    val NUMBER_FORMAT_EXCEPTION = "error.expected.numberformatexception"
    val BOOLEAN_FORMAT_EXCEPTION = "error.expected.jsboolean"
    val STRING_FORMAT_EXCEPTION = "error.expected.jsstring"
    val JSNUMBER_FORMAT_EXCEPTION = "error.expected.jsnumber"
    val JSARRAY_FORMAT_EXCEPTION = "error.expected.jsarray"
    val PATH_MISSING_EXCEPTION = "error.path.missing"
  }

  implicit class toJsonImp[T : Writes](a: T){
    def toJson: JsValue = Json.toJson(a)
  }

  def testMandatoryProperty[A](jsonString: String)(property: String)(implicit rd: Reads[A]): Unit = {
    s"the JSON is missing the required property $property" should {

      val json = Json.parse(
        jsonString.replace(property, s"_$property")
      )

      val result = Try(json.as[A])

      "only throw one error" in {
        result match {
          case Failure(e: JsResultException) =>
            if (e.errors.size != 1) withClue(s"${e.errors.mkString("\n")} errors found, 1 expected : $e.errors")(e.errors.size shouldBe 1)
          case _ => fail("A JSON error was expected")
        }
      }

      "throw the error against the correct property" in {
        result match {
          case Failure(e: JsResultException) =>
            val propertyName = getOnlyJsonErrorPath(e)
            if (propertyName.isRight) {
              propertyName.right.get should endWith (s".$property")
            }
          case _ => fail("A JSON error was expected")
        }
      }

      "throw a missing path error" in {
        result match {
          case Failure(e: JsResultException) =>
            val message = getOnlyJsonErrorMessage(e)
            if (message.isRight) {
              message.right.get shouldBe JsonError.PATH_MISSING_EXCEPTION
            }
          case _ => fail("A JSON error was expected")
        }
      }
    }
  }

  def testPropertyType[T](json: JsValue)(path: String, replacement: JsValue, expectedError: String)
                         (implicit rds: Reads[T]): Unit = {

    val jsPath = path.split("/").filterNot(_ == "").foldLeft(JsPath())(_ \ _)

    lazy val readResult = {

      val overwriteJsonValue: JsValue = {
        val updateReads: Reads[JsObject] = __.json.update(jsPath.json.put(replacement))
        json.as[JsObject](updateReads)
      }

      val amendedJson: JsValue = jsPath.json.pickBranch.reads(json).fold(
        invalid = errs => fail(s"an error occurred when reading $path : $errs"),
        valid = _ => overwriteJsonValue
      )

      rds.reads(amendedJson)
    }

    s"the JSON has the wrong data type for path $path" should {

      "only throw one error" in {
        readResult match {
          case JsError(errs) => withClue(s"${errs.mkString("\n")} errors found, 1 expected : $errs")(errs.size shouldBe 1)
          case _ => fail(s"expected to fail but didn't")
        }
      }

      lazy val pathFilteredErrors: Seq[JsonValidationError] = readResult.fold(
        invalid = _.filter { case (_path, _) => _path == jsPath }.flatMap(_._2),
        valid = _ => fail(s"expected to fail but didn't")
      )

      "throw the error against the correct property" in {
        pathFilteredErrors.size shouldBe 1
      }

      "throw an invalid type error" in {
        pathFilteredErrors match {
          case err :: Nil => err.message shouldBe expectedError
          case errs @ _ :: _ => fail(s"multiple errors returned for $path but only 1 required : $errs")
          case Nil => fail(s"no property type error found for $path")
        }
      }
    }
  }


  def testPropertyType[A](jsonString: String)(property: String, invalidValue: String, errorPathAndError: (String, String))
                         (implicit rd: Reads[A]): Unit = {

    val invalidTypedJson: String = jsonString.split('\n').map { line =>
      if (line.trim.startsWith(s""""$property""")) {
        s""""$property":$invalidValue,"""
      } else {
        line
      }
    }.mkString(" ").replaceAll(",\\s*}", " }")

    executeJsonPropertyTests(invalidTypedJson, property, errorPathAndError._2)
  }

  def executeJsonPropertyTests[A](invalidJson: String, property: String, errorMessage: String)
                              (implicit rd: Reads[A]): Unit = {
    s"the JSON has the wrong data type for property $property" should {

      val json = Json.parse(invalidJson)

      val result = Try(json.as[A])

      "only throw one error" in {
        result match {
          case Failure(e: JsResultException) => withClue(s"${e.errors.size} errors found, 1 expected : ${e.errors}")(e.errors.size shouldBe 1)
          case _ => fail("A JSON error was expected")
        }
      }

      "throw the error against the correct property" in {
        result match {
          case Failure(e: JsResultException) =>
            val propertyName = getOnlyJsonErrorPath(e)
            if (propertyName.isRight) {
              propertyName.right.get should endWith (property)
            }
          case _ => fail("A JSON error was expected")
        }
      }

      "throw an invalid type error" in {
        result match {
          case Failure(e: JsResultException) =>
            val message = getOnlyJsonErrorMessage(e)
            if (message.isRight) {
              message.right.get shouldBe errorMessage
            }
          case _ => fail("A JSON error was expected")
        }
      }
    }

  }

  def removeJsonProperty[T](json: JsValue)(pathToProperty: String): JsValue = {
    val jsPath = pathToProperty.split("/").filterNot(_ == "").foldLeft(JsPath())(_ \ _)
    jsPath.prune(json).fold(
      invalid = errs => fail(s"an error occurred when reading $pathToProperty: $errs"),
      valid = x => x
    )
  }

  private def getOnlyJsonErrorPath(ex: JsResultException): Either[Assertion, String] = {
    ex.errors match {
      case (jsonPath, _) :: Nil =>
        //recursive paths using ( __ \\ "field") return `obj*`, while nested objects return `obj.`.
        //Replace these to match the useful part of the path
        Right(jsonPath.toJsonString.replaceAll("obj([.*])", "."))
      case _ :: _ => Left(cancel("Too many JSON errors only expected one."))
    }
  }

  private def getOnlyJsonErrorMessage(ex: JsResultException): Either[Assertion, String] = {
    ex.errors match {
      case (_, JsonValidationError(onlyError :: Nil) :: Nil) :: Nil => Right(onlyError)
      case (_, JsonValidationError(_ :: _) :: Nil) :: Nil => Left(cancel("Too many error messages for property"))
      case _ :: _ => Left(cancel("Too many JSON errors only expected one."))
    }
  }
}
