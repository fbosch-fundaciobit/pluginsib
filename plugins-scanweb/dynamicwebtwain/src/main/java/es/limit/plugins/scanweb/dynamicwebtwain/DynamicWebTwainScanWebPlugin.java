package es.limit.plugins.scanweb.dynamicwebtwain;

import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.scanweb.IScanWebPlugin;
import org.fundaciobit.plugins.scanweb.ScanWebResource;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

/**
 * 
 * @author LIMIT 
 * 
 */
public class DynamicWebTwainScanWebPlugin extends AbstractPluginProperties implements
    IScanWebPlugin {

  protected final Logger log = Logger.getLogger(getClass());

  private static final String PROPERTY_BASE = SCANWEB_BASE_PROPERTY + "dynamicwebtwain.";

  /**
 * 
 */
  public DynamicWebTwainScanWebPlugin() {
    super();
  }
  
  
  private String getDynamicWebTwainProperty(String name) {
    return getProperty(PROPERTY_BASE + name);
  }
  

  /**
   * @param propertyKeyBase
   * @param properties
   */
  public DynamicWebTwainScanWebPlugin(String propertyKeyBase, Properties properties) {
    super(propertyKeyBase, properties);
  }

  /**
   * @param propertyKeyBase
   */
  public DynamicWebTwainScanWebPlugin(String propertyKeyBase) {
    super(propertyKeyBase);
  }

  public String getName(Locale locale) throws Exception {
    return "DynamicWebTwain";
  }

  public String getHeaderJSP(HttpServletRequest request) throws Exception {
    // TODO
    return ""; 
  }

  public String getCoreJSP(HttpServletRequest request) throws Exception {
    // TODO
    return "";
  }

  public ScanWebResource getResource(HttpServletRequest request, String resourcename)
      throws Exception {
    // TODO
    return null;
  }

}