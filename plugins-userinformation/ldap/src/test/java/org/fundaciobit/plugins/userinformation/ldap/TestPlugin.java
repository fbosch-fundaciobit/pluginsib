package org.fundaciobit.plugins.userinformation.ldap;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.fundaciobit.plugins.userinformation.UserInfo;
import org.fundaciobit.plugins.userinformation.ldap.LdapUserInformationPlugin;
import org.fundaciobit.plugins.utils.ldap.LDAPConstants;
import org.fundaciobit.plugins.utils.ldap.LDAPUserManager;

/**
 * 
 * @author anadal
 *
 */
public class TestPlugin {

  
  public static void main(String[] args) {
    try {

      String username, password;

      File f = new File("connection.properties");

      Properties ldapProperties = new Properties();

      if (f.exists()) {

        ldapProperties.load(new FileInputStream(f));

        username = ldapProperties.getProperty("test.username");
        password = ldapProperties.getProperty("test.password");

      } else {

        ldapProperties = new Properties();
        // Host
        ldapProperties.setProperty(LDAPConstants.LDAP_PROVIDERURL, "ldap://ldap.ibit.org:389");
        // Nom de l'usuari LDAP amb permisos de lectura
        String loginUserName = "login_user";
        String loginPassword = "login_password";
        // Usuari i contrasenya (usat per llistar usuaris)
        ldapProperties.setProperty(LDAPConstants.LDAP_SECURITYPRINCIPAL, loginUserName);
        ldapProperties.setProperty(LDAPConstants.LDAP_SECURITYCREDENTIALS, loginPassword);
        // ususari de prova
        username = "anadal";
        password = "password_of_anadal";
      }

      // Tipus Seguretat
      ldapProperties.setProperty(LDAPConstants.LDAP_SECURITYAUTHENTICATION,
          LDAPConstants.LDAP_SECURITYAUTHENTICATION_SIMPLE);
      // Directory on estan tots els usuaris
      ldapProperties.setProperty(LDAPConstants.LDAP_USERSCONTEXTDN,
          "cn=Users,dc=ibitnet,dc=lan");
      // No cercar en subdirectoris
      ldapProperties.setProperty(LDAPConstants.LDAP_SEARCHSCOPE,
          LDAPConstants.LDAP_SEARCHSCOPE_ONELEVEL);

      // SEARCH si no el definim automàticament selecciona tot
      ldapProperties.setProperty(LDAPConstants.LDAP_SEARCHFILTER,
          "(|(memberOf=CN=@PFI_ADMIN,CN=Users,DC=ibitnet,DC=lan)(memberOf=CN=@PFI_USER,CN=Users,DC=ibitnet,DC=lan))"
         // "(&(memberOf=CN=@Oficina,CN=Users,DC=ibitnet,DC=lan)(memberOf=CN=@Negoci,CN=Users,DC=ibitnet,DC=lan))"
          );

      ldapProperties.setProperty(LDAPConstants.PREFIX_ROLE_MATCH_MEMBEROF,"CN=@");
      ldapProperties.setProperty(LDAPConstants.SUFFIX_ROLE_MATCH_MEMBEROF,",CN=Users,DC=ibitnet,DC=lan");


      // Propietats de camps LDAP associats a atributs.
      ldapProperties.setProperty(LDAPConstants.LDAP_USERNAME_ATTRIBUTE, "sAMAccountName");
      ldapProperties.setProperty(LDAPConstants.LDAP_NAME_ATTRIBUTE, "givenName");
      ldapProperties.setProperty(LDAPConstants.LDAP_EMAIL_ATTRIBUTE, "mail");
      ldapProperties.setProperty(LDAPConstants.LDAP_ADMINISTRATIONID_ATTRIBUTE, "postOfficeBox");
      ldapProperties.setProperty(LDAPConstants.LDAP_SURNAME_ATTRIBUTE, "sn");
      ldapProperties.setProperty(LDAPConstants.LDAP_MEMBEROF_ATTRIBUTE, "memberOf");
      ldapProperties.setProperty(LDAPConstants.LDAP_TELEPHONE_ATTRIBUTE, "telephoneNumber");

      for (String attrib :  LDAPConstants.LDAP_PROPERTIES) {
        String value = ldapProperties.getProperty(attrib);
        
        if (value == null) {
          System.err.println("Atribut " + attrib + " val null.");
        } else {
          System.setProperty(LdapUserInformationPlugin.LDAP_BASE_PROPERTIES + attrib, value);
          System.out.println(LdapUserInformationPlugin.LDAP_BASE_PROPERTIES + attrib + "=" + value);
        }
      }
     
      
      LdapUserInformationPlugin ldap = new LdapUserInformationPlugin();
      
      UserInfo userInfo = ldap.getUserInfoByUserName(username);
      if (userInfo != null) {
        System.out.println( " ------- getUserInfoByUserName ------- ");
        System.out.println(userInfo.toString());
        System.out.println();
      }
      
      
      UserInfo ui = ldap.getUserInfoByAdministrationID(userInfo.getAdministrationID());
      
      if (ui != null) {
        System.out.println( " ------- getUserInfoByAdministrationID ------- ");
        System.out.println(ui.toString());
        System.out.println();
      }
      
      
      org.fundaciobit.plugins.userinformation.RolesInfo rolesInfo = ldap.getRolesByUsername(username);
      if (rolesInfo != null) {
        System.out.println( " ------- rolesInfo(" + username + ") ------- ");
        String[] roles = rolesInfo.getRoles();
        
        for (String rol : roles) {
          System.out.println("    - " + rol );
        }
        System.out.println();
      }
      
      
      
      
      
      LDAPUserManager um = ldap.getLDAPUserManager();

      // 1.- Mètode per autenticar amb usuari contrasenya
      System.out.println("Authenticate: " + um.authenticateUser(username, password));
      System.out.println("Authenticate amb contrasenya erronia: " + um.authenticateUser(username, password + "22"));

/*
      // 2.- LLista de Tots els Usuaris
      LDAPUser[] all = um.getUserArray();
      int i = 0;
      System.out.println(" ALL USERS (" + all.length + ")");
      for (LDAPUser u : all) {
        System.out.println((i++) + ".- " + u.getUserName() + " -> " + u.getNom() + " "
            + u.getLlinatges() + "  [" + u.getEmail() + "]");
        if (i > 10) {
          System.out.println("...");
          break;
        }
      }

      // ========= Altres mètodes ========

      // 3.- Obtenir Usuari

      // 3.1.- Obtenir usuari per Nom

      LDAPUser u = um.getUserByUsername(username);
      System.out.println("NIF de l'usuari [" + username + "] és: " + u.getNif());
      String[] members = u.getMemberOf();
      for (int j = 0; j < members.length; j++) {
        System.out.println("     + member: " + members[j]);
      }
      

      // 3.2.- Obtenir usuari per NIF
      LDAPUser u2 = um.getUserByNIF(u.getNif());
      System.out.println("El nom de l'usuari amb NIF [" + u.getNif() + "] és " + u2.getNom()
          + " " + u2.getLlinatges());

      // 4.- Existeix usuari?
      boolean existeix = um.userExists(username);
      System.out.println("L'usuari " + username + " existeix ? " + (existeix ? "SI" : "NO"));

      // 5.- Llista només de usernames
      List<String> usernames = um.getAllUserNames();
      System.out.println(" ALL USERNAMES (" + usernames.size() + ")");
      i = 0;
      for (String un : usernames) {
        System.out.println((i++) + ".- " + un);
        if (i > 10) {
          System.out.println("...");
          break;
        }
      }

      // 6.- Cerca LDAP per filtre
      // NamingEnumeration<SearchResult> h = um.searchLDAP(customFilter);
      */ 
       

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
}
