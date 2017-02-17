package controllers

import java.util.{Date, UUID}
import javax.inject._

import dao.{SendLogDAO, UserTokenDAO}
import models.UserToken
import play.api.mvc.{Action, Controller}
import play.api.libs.json._

import scala.concurrent.ExecutionContext.Implicits.global
import utils.LdapAuthService

import scala.util.{Failure, Success}

/**
  * Created by Ruslan Komarov on 15.02.17.
  */
class AuthController @Inject() (userTokenDAO: UserTokenDAO) extends Controller {

  case class Credentials(username: String, password: String)

  implicit val credentialsReads = Json.reads[Credentials]

  def auth() = Action { request =>
    val maybeJson: Option[JsValue] = request.body.asJson

    val credentialsFromJson = maybeJson.map {jsValue =>
      Json.fromJson[Credentials](jsValue)
    } getOrElse {
      BadRequest("Bad request").as(JSON) //TODO change body
    }

    credentialsFromJson match { //TODO change bodies
      case _: JsError => BadRequest("Invalid json").as(JSON)
      case JsSuccess(c: Credentials, _) => {
        LdapAuthService.connect(c.username, c.password) match {
          case Failure(_) => BadRequest("Invalid credentials").as(JSON)
          case Success(r) =>  {
            val userToken = new UserToken(UUID.randomUUID().toString, c.username, new Date())
            val result = userTokenDAO.insert(userToken)
            Ok(r.toString() + " ").as(JSON)
          }
        }
      }
    }

  }
}
