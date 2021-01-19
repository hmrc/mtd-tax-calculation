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

package v2.fixtures

import play.api.libs.json.{JsValue, Json}
import v2.models.{Message, TaxCalcMessages}

object TaxCalcMessagesFixture {


  val taxCalcMessages = TaxCalcMessages(
    warningCount = 1,
    errorCount = 4,
    messages = Some(List(
      Message("C11101", "warning", "You have entered a large amount in total Gift Aid payments. Please check."),
      Message("C15102", "error", "Total amount of one-off Gift Aid payments cannot exceed the total gift aid payments. Please check."),
      Message("C15103", "error",
        "Gift aid payments made this year treated as paid in the previous year cannot exceed the total gift aid payments. Please check."),
      Message("C15104", "error",
        "Value of qualifying investments gifted to non-UK charities cannot exceed the sum of 'Value of qualifying shares and " +
          "securities gifted to charity' and 'Value of qualifying land and buildings gifted to charity'. Please check."),
      Message("C15105", "error", "Gift aid payments to non-UK charities cannot exceed the total gift aid payments. Please check."))))

  val bvrMessage = Message("C11101", "warning", "You have entered a large amount in total Gift Aid payments. Please check.")
  val bvrMessageString: String =
    """
      |{
      |    "id": "C11101",
      |    "type": "warning",
      |    "text": "You have entered a large amount in total Gift Aid payments. Please check."
      | }
    """.stripMargin

  val bvrDesMessageString: String =
    """
      |{
      |    "id": "C11101",
      |    "type": "WARN",
      |    "text": "You have entered a large amount in total Gift Aid payments. Please check."
      | }
    """.stripMargin

  val emptyTaxCalcMessages = TaxCalcMessages(
    warningCount = 0,
    errorCount = 0,
    messages = None)

  val taxCalcDesWarningsAndErrors: JsValue = Json.parse(
    """
      |{
      |	"calcOutput": {
      |		"calcID": "041f7e4d-87b9-4d4a-a296-3cfbdf92f7e2",
      |		"bvrTimestamp": "2018-06-04T12:13:48.651Z",
      |		"bvrVersion": "1.0.0",
      |		"bvrErrors": 4,
      |		"bvrWarnings": 1,
      |		"bvrMsg": [
      |			{
      |				"id": "C11101",
      |				"type": "WARN",
      |				"text": "You have entered a large amount in total Gift Aid payments. Please check."
      |			},
      |			{
      |				"id": "C15102",
      |				"type": "ERR",
      |				"text": "Total amount of one-off Gift Aid payments cannot exceed the total gift aid payments. Please check."
      |			},
      |			{
      |				"id": "C15103",
      |				"type": "ERR",
      |				"text": "Gift aid payments made this year treated as paid in the previous year cannot exceed the total gift aid payments. Please check."
      |			},
      |			{
      |				"id": "C15104",
      |				"type": "ERR",
      |				"text": "Value of qualifying investments gifted to non-UK charities cannot exceed the sum of 'Value of qualifying shares and securities gifted to charity' and 'Value of qualifying land and buildings gifted to charity'. Please check."
      |			},
      |			{
      |				"id": "C15105",
      |				"type": "ERR",
      |				"text": "Gift aid payments to non-UK charities cannot exceed the total gift aid payments. Please check."
      |			}
      |		]
      |	}
      |}
    """.stripMargin)

  val taxCalcDesNoWarningsAndErrors: JsValue = Json.parse(
    """
      |{
      |	"calcOutput": {
      |		"calcID": "041f7e4d-87b9-4d4a-a296-3cfbdf92f7e2",
      |		"bvrTimestamp": "2018-06-04T12:13:48.651Z",
      |		"bvrVersion": "1.0.0",
      |		"bvrErrors": 0,
      |		"bvrWarnings": 0
      |	}
      |}
    """.stripMargin)

  val taxCalcDesEmptyWarningsAndErrors: JsValue = Json.parse(
    """
      |{
      |	"calcOutput": {
      |		"calcID": "041f7e4d-87b9-4d4a-a296-3cfbdf92f7e2",
      |		"bvrTimestamp": "2018-06-04T12:13:48.651Z",
      |		"bvrVersion": "1.0.0",
      |		"bvrErrors": 0,
      |		"bvrWarnings": 0,
      |		"bvrMsg": [
      |
      |		]
      |	}
      |}
    """.stripMargin)

  val taxCalcMessagesWithWarningsAndErrors: JsValue = Json.parse(
    """
      |{
      |   "warningCount":1,
      |   "errorCount":4,
      |   "messages":[
      |      {
      |         "id":"C11101",
      |         "type":"warning",
      |         "text":"You have entered a large amount in total Gift Aid payments. Please check."
      |      },
      |      {
      |         "id":"C15102",
      |         "type":"error",
      |         "text":"Total amount of one-off Gift Aid payments cannot exceed the total gift aid payments. Please check."
      |      },
      |      {
      |         "id":"C15103",
      |         "type":"error",
      |         "text":"Gift aid payments made this year treated as paid in the previous year cannot exceed the total gift aid payments. Please check."
      |      },
      |      {
      |         "id":"C15104",
      |         "type":"error",
      |         "text":"Value of qualifying investments gifted to non-UK charities cannot exceed the sum of 'Value of qualifying shares and securities gifted to charity' and 'Value of qualifying land and buildings gifted to charity'. Please check."
      |      },
      |      {
      |         "id":"C15105",
      |         "type":"error",
      |         "text":"Gift aid payments to non-UK charities cannot exceed the total gift aid payments. Please check."
      |      }
      |   ]
      |}
      |
    """.stripMargin)


}
