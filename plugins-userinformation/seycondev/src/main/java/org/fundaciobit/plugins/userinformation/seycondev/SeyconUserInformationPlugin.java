package org.fundaciobit.plugins.userinformation.seycondev;

import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.fundaciobit.plugins.userinformation.IUserInformationPlugin;
import org.fundaciobit.plugins.userinformation.RolesInfo;
import org.fundaciobit.plugins.userinformation.UserInfo;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;


/**
 * 
 * @author anadal
 *
 */
public class SeyconUserInformationPlugin implements IUserInformationPlugin {

  
  public RolesInfo getRolesByUsername(String username) throws Exception {

    if (username == null) {
      throw new NullPointerException("Parameter username is NULL");
    }

    String query = "select UGR_CODGRU from SC_WL_USUGRU where UGR_CODUSU = ?";
    List<String> roles = new ArrayList<String>();
    Connection c = getConnection();
    try {
      PreparedStatement ps = c.prepareStatement(query);
      try {

        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();
        try {
          while (rs.next()) {
            roles.add(rs.getString(1));
          }
        } finally {
          try {
            rs.close();
          } catch (Exception e) {
          }
        }
      } finally {
        try {
          ps.close();
        } catch (Exception e) {
        }
      }

    } finally {
      try {
        c.close();
      } catch (Exception e) {
      }
    }

    

    RolesInfo info = new RolesInfo(username, roles.toArray(new String[roles
        .size()]));

    return info;
  }

  @Override
  public UserInfo getUserInfoByAdministrationID(String nif) throws Exception {
    final boolean paramIsNif = true;
    return getUserInfo(paramIsNif, nif);
  }

  @Override
  public UserInfo getUserInfoByUserName(String username) throws Exception {
    final boolean paramIsNif = false;
    return getUserInfo(paramIsNif, username);
  }
  
  
  
  private UserInfo getUserInfo(boolean paramIsNif, String param) throws Exception {
  
    String paramName = paramIsNif? "nif" : "username";
  
  
    if (param == null) {
      throw new NullPointerException("Parameter " + paramName +" is NULL");
    }

    String field = paramIsNif? "USU_NIF" : "USU_CODI"; 
    
    String where;
    if (paramIsNif) {
      where = "(" + field + " = ?) OR (" + field + " = ? )";
    } else {
      where = field + " = ?";
    }
    
    
    String query = "select USU_CODI, USU_NOM, USU_NIF from SC_WL_USUARI where " + where;

    Connection c = getConnection();
    try {
      PreparedStatement ps = c.prepareStatement(query);
      try {

        if (paramIsNif) {
          ps.setString(1, param.toLowerCase());
          ps.setString(2, param.toUpperCase());
        } else {
          ps.setString(1, param);
        }

        ResultSet rs = ps.executeQuery();
        try {
          if (rs.next()) {
            String username = rs.getString(1);
            String nom = rs.getString(2);
            String dni = rs.getString(3);
            UserInfo info = new UserInfo();
            info.setLanguage("ca");
            info.setName(nom);
            info.setAdministrationID(dni);
            info.setUsername(username);
            return info;
          } else {
            return null;
          }
        } finally {
          try {
            rs.close();
          } catch (Exception e) {
          }
        }
      } finally {
        try {
          ps.close();
        } catch (Exception e) {
        }
      }

    } finally {
      try {
        c.close();
      } catch (Exception e) {
      }
    }

  }

  private static DataSource datasource;

  private Connection getConnection() throws NamingException, SQLException {
    
    if (datasource == null) {
      Context ctx = new InitialContext();
      datasource = (DataSource) ctx.lookup("java:/es.caib.seycon.db.wl");
    }
    
    Connection c = datasource.getConnection();
    return c;
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
