package org.fundaciobit.plugins.utils;

import java.util.Properties;
import org.apache.log4j.Logger;


/**
 * 
 * @author anadal
 *
 */
public class PluginsManager {

  protected static Logger log = Logger.getLogger(PluginsManager.class);

  public static Object instancePluginByProperty(String propertyPlugin) {
    return instancePluginByProperty(propertyPlugin, "", null);
  }
  
  
  public static Object instancePluginByClassName(String className) {
    return instancePluginByClassName(className, "", null);
  }
  
  
  public static Object instancePluginByProperty(String propertyPlugin, String basePropertiesKey) {
    return instancePluginByProperty(propertyPlugin, basePropertiesKey, null);
  }

  public static Object instancePluginByClassName(String className, String basePropertiesKey) {
    return instancePluginByClassName(className, basePropertiesKey, null);
  }
  
  

  public static Object instancePluginByProperty(String propertyPlugin,
      String basePropertiesKey, java.util.Properties properties) {
    // Valor de la Clau
    String className = System.getProperty(propertyPlugin.trim());
    if (className == null || className.trim().length() == 0) {
      return null;
    }
    return instancePluginByClassName(className.trim(), basePropertiesKey.trim(), properties);
  }
  

  public static Object instancePluginByClassName(String className,
      String basePropertiesKey, java.util.Properties properties) {
    // Carregant la classe
    log.info("Carregant classe " + className + " ...");
    Class<?> c;
    try {
      c = Class.forName(className.trim());
    } catch (Exception ex) {
      final String msg = "Error carregant la classe " + className 
        + " associada a un plugin:" + ex.getMessage();
      log.error(msg, ex);
      return null;
    }
    return instancePluginByClass(c, basePropertiesKey, properties);
  }

  
  public static Object instancePluginByClass(Class<?> c) {
    return instancePluginByClass(c, "", null);
  }
  
  public static Object instancePluginByClass(Class<?> c, String basePropertiesKey) {
    return instancePluginByClass(c, basePropertiesKey, null);
  }
  

  public static Object instancePluginByClass(Class<?> c,
      String basePropertiesKey,   java.util.Properties properties) {
    // Instanciant la classe
    Object pluginInstance;
    try {
      if (!AbstractPluginProperties.class.isAssignableFrom(c)) {
        if (log.isDebugEnabled()) {
          log.debug("instancePluginByClassName => " + c.getName()
            + " no es AbstractPluginProperties");
        }
        pluginInstance = c.newInstance();
      } else {
        if (properties == null && (basePropertiesKey == null || basePropertiesKey.trim().length() == 0)) {
          log.debug("instancePluginByClassName => Instanciació simple");
          pluginInstance = c.newInstance();
        } else {
          try {
            pluginInstance = c.getConstructor(String.class, Properties.class).newInstance(basePropertiesKey.trim(), properties);
          } catch(NoSuchMethodException nsme) {
            log.error(nsme);
            pluginInstance = c.newInstance();
          }
        }
      }
    } catch (Exception e) {
      final String msg = "Error instanciant la classe " + c.getName() 
        + " associada a un plugin:" + e.getMessage();
      log.error(msg, e);
      return null;
    }
    return pluginInstance;
  }
  
  
}
