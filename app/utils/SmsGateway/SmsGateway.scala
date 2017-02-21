package utils.SmsGateway

import models.Message

/**
  * Created by Ruslan Komarov on 17.02.17.
  */
trait SmsGateway {
  def sendMessage(message: Message): String
}
