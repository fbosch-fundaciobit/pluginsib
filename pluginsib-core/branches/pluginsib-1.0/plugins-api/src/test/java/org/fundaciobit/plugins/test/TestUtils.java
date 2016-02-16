package org.fundaciobit.plugins.test;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * 
 * @author anadal
 *
 */
public abstract class TestUtils {

  

  private static Properties testProperties = new Properties();
  
  static {
    // Propietats
    try {
      testProperties.load(new FileInputStream("test.properties"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
 
  public static String getProperty(String name) {
    return testProperties.getProperty(name);
  }

 
  
}
