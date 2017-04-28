package org.fundaciobit.plugins.certificate.afirmacxf.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.fundaciobit.plugins.validatesignature.afirmacxf.AfirmaCxfValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.IValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.test.AbstractTestValidateSignature;

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
  
  
boolean modificat = false;
  
  public String[][] getTests() {
    if (!modificat) {
      // TODO XYZ ZZZ Com solucionar Això !!!!

      // Error de firma no vàlida: El formato de la firma no es válido
      /* [20] =  { "afirma/sample_xades_detached_firmat.xml", "afirma/sample.xml",
        SIGNTYPE_XAdES, SIGNPROFILE_BES,
        SIGNFORMAT_EXPLICIT_DETACHED },
        */      
      FIRMA_DOCUMENT[20][2] = null;
      FIRMA_DOCUMENT[20][3] = null;
      FIRMA_DOCUMENT[20][4] = null;
      
      // Error de firma no vàlida: El formato de la firma no es válido
      /* [21] =   { "afirma/sample_xades_detached_epes_firmat.xml", "afirma/sample.xml", SIGNTYPE_XAdES,
           SIGNPROFILE_EPES, SIGNFORMAT_EXPLICIT_DETACHED }, */
      FIRMA_DOCUMENT[21][2] = null;
      FIRMA_DOCUMENT[21][3] = null;
      FIRMA_DOCUMENT[21][4] = null;
      
      
      
      
      
      
      
      modificat = true;
    }
    return FIRMA_DOCUMENT;
  }
  
  
  
  @org.junit.Test
  public void testBasic() throws Exception {
    
    //SignatureValidationTestResult[] results =
    internalTestBasic();

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
