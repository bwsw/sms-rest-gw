package com.bwsw.controllers

import com.bwsw.models.User
import com.bwsw.utils.LdapAuthService
import com.typesafe.config.ConfigException
import com.unboundid.ldap.sdk.LDAPException
import pdi.jwt.JwtSession
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Authenticate controller
  */
class AuthController extends Controller {
  case class Credentials(username: String, password: String)
  implicit val credentialsReads = Json.reads[Credentials]

  /**
    * Authenticate method.
    * Receive POST request (/auth) with arguments: username and password; check credentials using LdapAuthService.
    * @return response with token or error with description
    */
  def auth = Action.async(parse.json) { request =>
    request.body.validate[Credentials] match {
      case JsError(_) => Future.successful(BadRequest(Json.obj("error" -> "Invalid json")).as(JSON))
      case JsSuccess(c: Credentials, _) => {
        LdapAuthService.authenticate(c.username, c.password) match {
          case Failure(e) => e match {
            case _: LDAPException => Future.successful(BadRequest(Json.obj("error" -> "LDAP service error")).as(JSON))
            case _: ConfigException => Future.successful(BadRequest(Json.obj("error" -> "Bad application config: missing [app.ldap.(host|port))] setting(s))")).as(JSON))
            case _ => Future.successful(BadRequest(Json.obj("error" -> ("Unknown error: " + e.toString))).as(JSON))
          }
          case Success(r) => {
            val session = JwtSession() + ("user", User(c.username))
            Future.successful(Ok(Json.obj("token" -> session.serialize)).as(JSON))
          }
        }
      }
    }
  }
}
