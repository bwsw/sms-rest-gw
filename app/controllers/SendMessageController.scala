package controllers

import javax.inject.Inject

import com.typesafe.config.ConfigException
import controllers.Secured.UserAction
import dao.SendLogDAO
import play.api.libs.json._
import play.api.mvc.Controller
import models.{Message, SendLogRecord}
import org.joda.time.DateTime
import utils.SmsGateway.SmsGateway

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
  * Created by Ruslan Komarov on 17.02.17.
  */
class SendMessageController @Inject() (sendLogDAO: SendLogDAO) extends Controller {
  def send = UserAction.async(parse.json) { implicit request =>
    request.body.validate[Message] match {
      case _: JsError => Future.successful(BadRequest(Json.obj("error" -> "Invalid json")).as(JSON))
      case JsSuccess(m, _) => {
        SmsGateway.getSmsGateway() map { service =>
          Try(service.sendMessage(m)) match {
            case Success(result) => result.fold(
              error => Future.successful(BadRequest(Json.obj("error" -> error)).as(JSON)),
              result => {
                sendLogDAO.insert(SendLogRecord(request.user.username, m.sender, m.destination, m.text, DateTime.now()))
                Future.successful(Ok(Json.obj("message" -> result)).as(JSON))
              }
            )
            case Failure(e) => e match {
              case _: ConfigException => Future.successful(BadRequest(Json.obj("error" -> "Bad application config: missing SMS gateway setting(s))")).as(JSON))
              case _ => Future.successful(BadRequest(Json.obj("error" -> ("Unknown error: " + e.toString))).as(JSON))
            }
          }
        } getOrElse Future.successful(BadRequest(Json.obj("error" -> "Bad application config: missing [app.ldap] setting")).as(JSON))
      }
    }
  }
}
