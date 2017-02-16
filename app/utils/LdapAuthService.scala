package utils

import com.unboundid.ldap.sdk.LDAPConnection

import scala.util.Try

/**
  * Created by Ruslan Komarov on 15.02.17.
  */
object LdapAuthService {

  def connect (username: String, password: String) = {
    Try {
      val ldap: LDAPConnection = new LDAPConnection("ldap.com", 389)
      ldap.bind(username, password)
    }
  }
}
