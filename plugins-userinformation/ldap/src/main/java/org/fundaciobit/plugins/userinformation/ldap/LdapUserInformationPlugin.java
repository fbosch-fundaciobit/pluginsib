package org.fundaciobit.plugins.userinformation.ldap;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import org.fundaciobit.plugins.userinformation.IUserInformationPlugin;
import org.fundaciobit.plugins.userinformation.RolesInfo;
import org.fundaciobit.plugins.userinformation.UserInfo;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;
import org.fundaciobit.plugins.utils.ldap.LDAPConstants;
import org.fundaciobit.plugins.utils.ldap.LDAPUser;
import org.fundaciobit.plugins.utils.ldap.LDAPUserManager;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * 
 * @author anadal
 *
 */
public class LdapUserInformationPlugin extends AbstractPluginProperties
   implements IUserInformationPlugin {
  
  
  protected final Logger log = Logger.getLogger(getClass());
  
  public static final String LDAP_BASE_PROPERTIES = USERINFORMATION_BASE_PROPERTY;

  private LDAPUserManager ldapUserManager = null;

  /**
   * 
   */
  public LdapUserInformationPlugin() {
    super();
  }

  /**
   * @param propertyKeyBase
   * @param properties
   */
  public LdapUserInformationPlugin(String propertyKeyBase, Properties properties) {
    super(propertyKeyBase, properties);
  }

  /**
   * @param propertyKeyBase
   */
  public LdapUserInformationPlugin(String propertyKeyBase) {
    super(propertyKeyBase);
  }

  public LDAPUserManager getLDAPUserManager() {

    if (ldapUserManager == null) {

      Properties ldapProperties = new Properties();

      for (String attrib :  LDAPConstants.LDAP_PROPERTIES) {
        ldapProperties.setProperty(attrib, getProperty(LDAP_BASE_PROPERTIES + attrib));
      }

      ldapUserManager = new LDAPUserManager(ldapProperties);
    }
    return ldapUserManager;
  }

  @Override
  public RolesInfo getRolesByUsername(String username) throws Exception {

    LDAPUserManager ldapManager = getLDAPUserManager();
    
    List<String> roles = ldapManager.getRolesOfUser(username);
    
    if (roles == null) {
      return null;
    } else {
      RolesInfo roleInfo = new RolesInfo(username, roles.toArray(new String[roles.size()]));
      return roleInfo;
    }
  }

  
  public UserInfo getUserInfoByAdministrationID(String nif) throws Exception {
    final boolean paramIsNif = true;
    return getUserInfo(paramIsNif, nif);
  }


  public UserInfo getUserInfoByUserName(String username) throws Exception {
    final boolean paramIsNif = false;
    return getUserInfo(paramIsNif, username);
  }
  
  
  
  private UserInfo getUserInfo(boolean paramIsNif, String param) throws Exception {
  
    
    LDAPUserManager ldapManager = getLDAPUserManager();
    LDAPUser ldapUser;
    if (paramIsNif) {
      ldapUser = ldapManager.getUserByAdministrationID(param);
    } else {
      ldapUser = ldapManager.getUserByUsername(param);
    }
    
    if (ldapUser == null) {
      return null;
    }
    
    UserInfo info = new UserInfo();
    info.setLanguage("ca");
    info.setName(ldapUser.getName());
    info.setSurname1(ldapUser.getSurname());
    info.setAdministrationID(ldapUser.getAdministrationID());
    info.setUsername(ldapUser.getUserName());
    info.setEmail(ldapUser.getEmail());
    info.setPhoneNumber(ldapUser.getTelephoneNumber());
    
    return info;

  }


  @Override
  public boolean authenticate(String username, String password) throws Exception {
    throw new NotImplementedException();
  }


  @Override
  public boolean authenticate(X509Certificate certificate) throws Exception {
    throw new NotImplementedException();
  }


  @Override
  public String[] getAllUsernames() throws Exception {
    throw new NotImplementedException();
  }
  

}
