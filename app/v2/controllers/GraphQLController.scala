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

package v2.controllers

import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.{Action, Result}
import sangria._
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.parser.{QueryParser, SyntaxError}
import sangria.renderer.SchemaRenderer
import v2.config.AppConfig
import v2.controllers.mysangriathings.SangriaSchema
import v2.models._
import v2.services.{AuditService, EnrolmentsAuthService, MtdIdLookupService, TaxCalcService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class GraphQLController @Inject()(val authService: EnrolmentsAuthService,
                                  val lookupService: MtdIdLookupService,
                                  service: TaxCalcService,
                                  appConfig: AppConfig,
                                  auditService: AuditService
                                 )(implicit ec: ExecutionContext, val sangriaSchema: SangriaSchema) extends AuthorisedController {

  val logger: Logger = Logger(this.getClass)

  def graphQLRequest(nino: String, calcId: String): Action[JsValue] = authorisedAction(nino).async(parse.json) { implicit request =>
    service.getTaxCalculation[TaxCalculation](nino, calcId).flatMap {
      case Right(v2.outcomes.DesResponse(_, responseData)) => parseGraphQLRequest(request.body, responseData)
      case _ => Future.successful(NotFound)
    }
  }

  private def parseGraphQLRequest(requestBody: JsValue, responseData: TaxCalculation): Future[Result] = {
    val query = (requestBody \ "query").as[String]
    QueryParser.parse(query) match {
      case Success(queryAst) =>
        executeGraphQLQuery(queryAst)(responseData)
      // can't parse GraphQL query, return error
      case Failure(error: SyntaxError) =>
        Future.successful(BadRequest(Json.obj("error" → error.getMessage)))
    }
  }

  private def parseVariables(variables: String) =
    if (variables.trim == "" || variables.trim == "null") Json.obj() else Json.parse(variables).as[JsObject]

  import sangria.marshalling.playJson._

  private def executeGraphQLQuery(query: ast.Document)(implicit responseData: TaxCalculation): Future[Result] = {
    Executor.execute(sangriaSchema.schema, query, responseData)
      .map(Ok(_))
      .recover {
        case error: QueryAnalysisError ⇒ BadRequest(error.resolveError)
        case error: ErrorWithResolver ⇒ InternalServerError(error.resolveError)
      }
  }
}
