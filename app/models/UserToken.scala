package models


import java.util.Date

/**
  * Created by Ruslan Komarov on 16.02.17.
  */
case class UserToken(token: String, username: String, expirationTime: Date)