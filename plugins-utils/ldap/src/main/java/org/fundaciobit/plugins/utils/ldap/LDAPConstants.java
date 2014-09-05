package org.fundaciobit.plugins.utils.ldap;
/**
 * Constants.
 * @author anadal
 *
 */
public interface LDAPConstants {

  /**
   * LDAP of Property key name that represents ProviderURL.
   */
  String LDAP_PROVIDERURL =  "ldap.host_url";

  /**
   * LDAP of Property key name that represents SecurityPrincipal.
   */
  String LDAP_SECURITYPRINCIPAL = "ldap.security_principal";
  
  /**
   * LDAP of Property key name that represents SecurityCredentials.
   */
  String LDAP_SECURITYCREDENTIALS = "ldap.security_credentials";

  /**
   * LDAP of Property key name that represents SecurityAuthentication.
   */
  String LDAP_SECURITYAUTHENTICATION = "ldap.security_authentication";

  /**
   * LDAP of Property key name that represents None SecurityAuthentication.
   */
  String LDAP_SECURITYAUTHENTICATION_NONE = "none";

  /**
   * LDAP of Property key name that represents Simple SecurityAuthentication.
   */
  String LDAP_SECURITYAUTHENTICATION_SIMPLE = "simple";


  /**
   * LDAP of Property key name that represents UsersContextDN.
   */
  String LDAP_USERSCONTEXTDN = "ldap.users_context_dn";

  /**
   * LDAP of Property key name that represents SearchScope.
   */
  String LDAP_SEARCHSCOPE = "ldap.search_scope";

  /**
   * LDAP of Property key name that represents SearchScope.
   */
  String LDAP_SEARCHSCOPE_ONELEVEL = "onelevel";

  /**
   * LDAP of Property key name that represents SearchScope.
   */
  String LDAP_SEARCHSCOPE_SUBTREE = "subtree";

  /**
   * LDAP of Property key name that represents SearchFilter.
   */
  String LDAP_SEARCHFILTER = "ldap.search_filter";

  String PREFIX_ROLE_MATCH_MEMBEROF = "ldap.prefix_role_match_memberof";

  String SUFFIX_ROLE_MATCH_MEMBEROF =  "ldap.suffix_role_match_memberof";

  /**
   * LDAP of Property key name that represents UsernameAttribute.
   */
  String LDAP_USERNAME_ATTRIBUTE = "ldap.attribute.username";

  /**
   * LDAP of Property key name that represents EmailAttribute.
   */
  String LDAP_EMAIL_ATTRIBUTE = "ldap.attribute.mail";

  /**
   * LDAP of Property key name that represents Nif Attribute.
   */
  String LDAP_ADMINISTRATIONID_ATTRIBUTE = "ldap.attribute.administration_id";
  
  /**
   * LDAP of Property key name that represents name.
   */
  String LDAP_NAME_ATTRIBUTE =  "ldap.attribute.name";

  /**
   * LDAP of Property key name that represents surname.
   */
  String LDAP_SURNAME_ATTRIBUTE = "ldap.attribute.surname";
  
  String LDAP_MEMBEROF_ATTRIBUTE="ldap.attribute.memberof";

  String LDAP_TELEPHONE_ATTRIBUTE = "ldap.attribute.telephone";


  /**
   * All LDAP properties.
   */
  String[] LDAP_PROPERTIES = new String[] {
    LDAP_PROVIDERURL, LDAP_SECURITYPRINCIPAL, LDAP_SECURITYAUTHENTICATION,
    LDAP_SECURITYCREDENTIALS, LDAP_USERSCONTEXTDN, LDAP_SEARCHSCOPE,
    LDAP_SEARCHFILTER,
    PREFIX_ROLE_MATCH_MEMBEROF,SUFFIX_ROLE_MATCH_MEMBEROF,
    // ATTRIBUTES
    LDAP_USERNAME_ATTRIBUTE, LDAP_EMAIL_ATTRIBUTE, LDAP_ADMINISTRATIONID_ATTRIBUTE,
    LDAP_NAME_ATTRIBUTE, LDAP_SURNAME_ATTRIBUTE, LDAP_TELEPHONE_ATTRIBUTE,
    LDAP_MEMBEROF_ATTRIBUTE
  };

}
