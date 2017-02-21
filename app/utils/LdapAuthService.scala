package utils

import com.unboundid.ldap.sdk.{LDAPConnection, SearchRequest, SearchScope}

import scala.util.Try

/**
  * Created by Ruslan Komarov on 15.02.17.
  */
object LdapAuthService {

  lazy val ldapService: LDAPConnection = new LDAPConnection("ldap.com", 389)

  def authenticate(username: String, password: String) = {
    Try {
      ldapService.bind(username, password)
    }
  }
}
