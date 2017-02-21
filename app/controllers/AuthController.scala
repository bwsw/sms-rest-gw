package controllers


import pdi.jwt._
import play.api.mvc.{Action, Controller}
import play.api.libs.json._
import utils.LdapAuthService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by Ruslan Komarov on 15.02.17.
  */
class AuthController extends Controller {
  case class Credentials(username: String, password: String)
  implicit val credentialsReads = Json.reads[Credentials]

  def auth = Action.async(parse.json) { request =>
    request.body.validate[Credentials] match {
      case JsError(_) => Future.successful(BadRequest(Json.obj("error" -> "Invalid json")).as(JSON))
      case JsSuccess(c: Credentials, _) => {
        LdapAuthService.authenticate(c.username, c.password) match {
          case Failure(_) => Future.successful(BadRequest(Json.obj("error" -> "Invalid credentials")).as(JSON))
          case Success(r) => {
            val jwtClaim = JwtSession.defaultClaim.about(c.username)
            val session = JwtSession(jwtClaim)
            Future.successful(Ok(Json.obj("token" -> session.serialize)).as(JSON))
          }
        }
      }
    }
  }
}
