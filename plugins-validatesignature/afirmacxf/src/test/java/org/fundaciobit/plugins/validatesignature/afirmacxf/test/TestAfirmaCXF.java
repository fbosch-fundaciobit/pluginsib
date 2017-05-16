package org.fundaciobit.plugins.validatesignature.afirmacxf.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.fundaciobit.plugins.validatesignature.afirmacxf.AfirmaCxfValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.IValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.test.AbstractTestValidateSignature;
import org.fundaciobit.plugins.validatesignature.api.test.SignatureValidationTestResult;

/**
 * 
 * @author anadal
 *
 */
public class TestAfirmaCXF extends AbstractTestValidateSignature {

  public IValidateSignaturePlugin instantiatePlugin() throws Exception {
    Properties pluginProperties = new Properties();
    pluginProperties.load(new FileInputStream(new File("./config/plugin.properties")));

    String propertyKeyBase = "org.fundaciobit.exemple.base.";

    IValidateSignaturePlugin plugin;
    plugin = new AfirmaCxfValidateSignaturePlugin(propertyKeyBase, pluginProperties);
    return plugin;
  }

  protected Map<String, String[]> currentTests = null;

  public Map<String, String[]> getTests() {
    if (currentTests == null) {
      // TODO XYZ ZZZ Com solucionar Això !!!!

      Map<String, String[]> tests = new TreeMap<String, String[]>(super.getTests());

      // Error de firma no vàlida: El formato de la firma no es válido
 
      
       String[] FIRMA_DOCUMENT = tests.get("afirma/sample_xades_detached_firmat.xml");
       FIRMA_DOCUMENT[5] = "ERROR";
      

      // Error de firma no vàlida: El formato de la firma no es válido
      FIRMA_DOCUMENT = tests.get("afirma/sample_xades_detached_epes_firmat.xml");
      FIRMA_DOCUMENT[5] = "ERROR";

      currentTests = tests;
      
      //currentTests = new TreeMap<String, String[]>();
      //currentTests.put("afirma/sample_xades_detached_epes_firmat.xml",  tests.get("afirma/sample_xades_detached_epes_firmat.xml"));
      
      
    }
    return currentTests;
  }

  @org.junit.Test
  public void testBasic() throws Exception {

    Map<String, SignatureValidationTestResult> results = internalTestBasic(false);

    System.out.println();
    System.out.println();
    System.out.flush();
    System.err.flush();

    for (String key : results.keySet()) {
      String msg = internalCheckTestBasicResults(results.get(key), key, false);

      if (msg == null) {
        System.out.println("TEST[" + key + "] => OK");
        System.out.flush();
      } else {
        System.err.println("TEST[" + key + "] => ERROR : " + msg);
        System.err.flush();
      }

    }

  }

  public static void main(String[] args) {

    try {
      new TestAfirmaCXF().testBasic();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
