package org.fundaciobit.plugins.scanweb;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.fundaciobit.plugins.IPlugin;

/**
 * 
 * @author anadal
 * 
 */
public interface IScanWebPlugin extends IPlugin {

  public static final String SCANWEB_BASE_PROPERTY = IPLUGIN_BASE_PROPERTIES  + "scanweb.";

  public String getName(Locale locale) throws Exception;
  
  public void controllerGET(HttpServletRequest request, String docID) throws Exception;
  
  public void controllerPOST(HttpServletRequest request, String docID) throws Exception;
 

  public String getHeaderJSP(HttpServletRequest request, String docID) throws Exception;

  public String getCoreJSP(HttpServletRequest request, String docID) throws Exception;

  public ScanWebResource getResource(HttpServletRequest request, String resourcename, String docID)
      throws Exception;
  
  public int getMinHeight(HttpServletRequest request, String docID) throws Exception;

}