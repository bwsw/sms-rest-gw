package com.bwsw.utils

import com.unboundid.ldap.sdk.LDAPConnection

import scala.util.Try

/**
  * Authentication service using LDAP
  */
object LdapAuthService {
  /**
    * Try to authenticate to the LDAP service using parameters
    * @param username username for authentication
    * @param password password for authentication
    * @return Success or Failure (in case of some exception)
    */
  def authenticate(username: String, password: String) = {
    import com.typesafe.config.ConfigFactory
    Try {
      val host = ConfigFactory.load().getString("app.ldap.host")
      val port = ConfigFactory.load().getInt("app.ldap.port")

      new LDAPConnection(host, port).bind(username, password)
    }
  }
}
