package utils.SmsGateway
import java.util

import com.plivo.helper.api.client.RestAPI
import com.typesafe.config.ConfigFactory
import models.Message
import play.api.http.Status


/**
  * Created by Ruslan Komarov on 17.02.17.
  */
class PlivoSmsGateway extends SmsGateway {
  override def sendMessage(message: Message): Either[String, String] = {
    val auth_id = ConfigFactory.load().getString("plivo.auth.id")
    val auth_token = ConfigFactory.load().getString("plivo.auth.token")

    val restAPI = new RestAPI(auth_id, auth_token, "v1")

    val params = new util.LinkedHashMap[String, String]
    params.put("src", message.sender)
    params.put("dst", message.destination)
    params.put("text", message.text)

    val response = restAPI.sendMessage(params)

    if (response.serverCode == Status.ACCEPTED) Right(response.message)
    else Left(response.error)
  }
}
