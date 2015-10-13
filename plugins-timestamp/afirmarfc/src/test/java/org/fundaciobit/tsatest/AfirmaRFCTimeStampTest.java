package org.fundaciobit.tsatest;

import java.io.FileInputStream;
import java.util.Properties;

import org.bouncycastle.tsp.TimeStampToken;
import org.fundaciobit.plugins.timestamp.afirmarfc.AfirmaRFCTimeStampPlugin;
import org.fundaciobit.plugins.timestamp.api.ITimeStampPlugin;
import org.fundaciobit.plugins.utils.Base64;
import org.fundaciobit.plugins.utils.PluginsManager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * @author anadal
 *
 */
public class AfirmaRFCTimeStampTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName
   *          name of the test case
   */
  public AfirmaRFCTimeStampTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(AfirmaRFCTimeStampTest.class);
  }

  /**
   * Rigourous Test :-)
   */
  public void testApp() {
    assertTrue(true);
  }

  public static void main(String[] args) {

    try {
      System.out.println(AfirmaRFCTimeStampPlugin.class.getCanonicalName());

      final String packageBase = "es.caib.example.";

      Properties afirmaRfcProperties = new Properties();

      afirmaRfcProperties.load(new FileInputStream("test.properties"));
      
      System.out.println("Properties: " + afirmaRfcProperties.toString());

      ITimeStampPlugin afirmaRFCTimeStampPlugin;
      afirmaRFCTimeStampPlugin = (ITimeStampPlugin) PluginsManager.instancePluginByClass(
          AfirmaRFCTimeStampPlugin.class, packageBase, afirmaRfcProperties);

      byte[] fichero = new String("hola").getBytes();

      System.out.println("*** INICIO RFC3161+HTTPS (Port 8443) ***");
      TimeStampToken tst3 = afirmaRFCTimeStampPlugin.getTimeStamp(fichero);
      if (tst3 != null) {
        System.out.println("Sello obtenido:");
        System.out.println(new String(Base64.encode(tst3.getEncoded())));

      } else {
        System.out.println("Error desconocido. Respuesta vacia.");
      }
      System.out.println("*** FIN RFC3161+HTTPS (Port 8443) ***");

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
