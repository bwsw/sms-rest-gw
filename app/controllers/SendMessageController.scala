package controllers

import javax.inject.Inject

import dao.SendLogDAO
import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import models.Message
import utils.SmsGateway.PlivoSmsGateway

import scala.concurrent.Future

/**
  * Created by Ruslan Komarov on 17.02.17.
  */
class SendMessageController @Inject() (sendLogDAO: SendLogDAO) extends Controller with Secured {
  val smsGw = new PlivoSmsGateway

  def send = Authenticated.async(parse.json) { request =>
    request.body.validate[Message] match {
      case _: JsError => Future.successful(BadRequest(Json.obj("error" -> "Invalid json")).as(JSON))
      case JsSuccess(m, _) => Future.successful(Ok("Ok")) //smsGw.sendMessage(m))
    }
  }
}
