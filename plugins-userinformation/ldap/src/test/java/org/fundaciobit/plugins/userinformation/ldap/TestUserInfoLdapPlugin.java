package org.fundaciobit.plugins.userinformation.ldap;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;

import org.fundaciobit.plugins.userinformation.UserInfo;
import org.fundaciobit.plugins.userinformation.ldap.LdapUserInformationPlugin;
import org.fundaciobit.plugins.utils.ldap.LDAPConstants;

/**
 * 
 * @author anadal
 *
 */
public class TestUserInfoLdapPlugin {

  
  public static void main(String[] args) {
    try {

      String username, password;
      
      /**  =======  connection.properties =============== */
//         ldap.host_url=ldap://ldap.ibit.org:389
//         ldap.security_principal=<<query_username>>
//         ldap.security_credentials=<<query_password>>
//          
//         test.username=<username>
//         test.password=<password
     

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
      
      
      String[] roles = new String[] { "PFI_ADMIN", "PFI_USER" };

      for (int i = 0; i < roles.length; i++) {
        System.out.println();
        System.out.println( " ------- Users with role " + roles[i] + ") ------- ");
        String[] users = ldap.getUsernamesByRol(roles[i]);
        System.out.println(Arrays.toString(users));        
      }
      
      
      
      
      
      //LDAPUserManager um = ldap.getLDAPUserManager();

      // 1.- Mètode per autenticar amb usuari contrasenya
      System.out.println();
      System.out.println("------------- Authenticate: " + ldap.authenticate(username, password));
      System.out.println();
      System.out.println("------------- Authenticate amb contrasenya erronia: " + ldap.authenticate(username, password + "22"));


      // 2.- LLista de Tots els Usuaris
      String[] all = ldap.getAllUsernames();
      System.out.println();
      System.out.println(" ------------------ ALL USERNAMES (" + all.length + ")");
      for (int i = 0; i < all.length ; i++) {
        System.out.println((i++) + ".- " + all[i]);
        if (i > 10) {
          System.out.println("...");
          break;
        }
      }

      // ========= Altres mètodes ========

      // 3.- Obtenir Usuari

      // 3.1.- Obtenir usuari per Nom
      System.out.println();
      System.out.println(" ------------------ getUserInfoByUserName -----------");
      UserInfo u = ldap.getUserInfoByUserName(username);
      System.out.println("    - Info usuari [ username = " + username + "]: ");
      System.out.println("          + Nom: " + u.getName() + " " + u.getSurname1() + " " + u.getSurname2());
      System.out.println("          + Nom Complet : " + u.getFullName());
      System.out.println("          + NIF: " + u.getAdministrationID());
      System.out.println("          + Email: " + u.getEmail());
      
      

      // 3.2.- Obtenir usuari per NIF
      String nif = u.getAdministrationID();
      u = ldap.getUserInfoByAdministrationID(nif);
      System.out.println();
      System.out.println(" ------------------ getUserInfoByAdministrationID -----------");
      System.out.println("    - Info usuari [ nif ="  + nif + "]: ");
      System.out.println("          + Nom: " + u.getName() + " " + u.getSurname1() + " " + u.getSurname2());
      System.out.println("          + Nom Complet : " + u.getFullName());
      System.out.println("          + NIF: " + u.getAdministrationID());
      System.out.println("          + Email: " + u.getEmail());
       

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
}
