package org.fundaciobit.plugins.utils;

import java.util.Properties;
import org.fundaciobit.plugins.IPlugin;

/**
 * 
 * @author anadal
 * 
 */
public abstract class AbstractPluginProperties implements IPlugin {

  public final String propertyKeyBase;

  /**
   * If properties is null then access to System.getProperties()
   */
  public final Properties properties;

  /**
   * @param propertyKeyBase
   */
  public AbstractPluginProperties() {
    this("", null);
  }

  /**
   * @param propertyKeyBase
   */
  public AbstractPluginProperties(String propertyKeyBase) {
    this(propertyKeyBase, null);
  }

  /**
   * @param propertyKeyBase
   * @param properties
   */
  public AbstractPluginProperties(String propertyKeyBase, Properties properties) {
    super();
    this.propertyKeyBase = propertyKeyBase;
    this.properties = properties;
  }

  public final String getProperty(String partialName) {
    return getProperty(partialName, null);
  }

  public final String getProperty(String partialName, String defaultValue) {
    String fullName = getPropertyName(partialName);
    if (this.properties == null) {
      return System.getProperty(fullName, defaultValue);
    } else {
      return this.properties.getProperty(fullName, defaultValue);
    }
  }
  
  public final String getPropertyName(String partialName) {
    return this.propertyKeyBase + partialName;
  }
  
  /**
   * 
   * @param propname
   * @return
   * @throws Exception
   */
  public final String getPropertyRequired(String partialName) throws Exception {
    String value = getProperty(partialName);
    if (value == null) {
      throw new Exception("Property " + getPropertyName(partialName) 
          + " is required but it has not defined in the Properties");
    }
    return value;
  }

}
