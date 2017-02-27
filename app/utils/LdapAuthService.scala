package utils

import com.unboundid.ldap.sdk.LDAPConnection

import scala.util.Try

/**
  * Created by Ruslan Komarov on 15.02.17.
  */
object LdapAuthService {
  def authenticate(username: String, password: String) = {
    import com.typesafe.config.ConfigFactory
    Try {
      val host = ConfigFactory.load().getString("app.ldap.host")
      val port = ConfigFactory.load().getInt("app.ldap.port")

      new LDAPConnection(host, port).bind(username, password)
    }
  }
}
