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

package v2.controllers.mysangriathings


import javax.inject.Inject
import sangria.schema._
import v2.models.TaxCalculation

class SangriaSchema {
  private def QueryType(implicit responseData: TaxCalculation) = ObjectType("Query", fields[TaxCalculation, Unit](
    Field("taxCalc", TaxCalculation.TaxCalculationType,
      description = Some("Returns the tac calculation"),
      resolve = _.ctx
    )
  ))

  def schema(implicit responseData: TaxCalculation) = Schema(QueryType)

}