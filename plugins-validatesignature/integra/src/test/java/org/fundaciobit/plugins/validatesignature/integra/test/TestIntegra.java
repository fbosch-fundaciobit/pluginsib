package org.fundaciobit.plugins.validatesignature.integra.test;

import java.util.Properties;

import org.fundaciobit.plugins.validatesignature.api.IValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureResponse;
import org.fundaciobit.plugins.validatesignature.api.test.AbstractTestValidateSignature;
import org.fundaciobit.plugins.validatesignature.api.test.SignatureValidationTestResult;
import org.fundaciobit.plugins.validatesignature.integra.IntegraValidateSignaturePlugin;

/**
 * 
 * @author anadal
 *
 */
public class TestIntegra extends AbstractTestValidateSignature {

  public IValidateSignaturePlugin instantiatePlugin() throws Exception {
    Properties pluginProperties = new Properties();

    String propertyKeyBase = "org.fundaciobit.exemple.base.";

    IValidateSignaturePlugin plugin;
    plugin = new IntegraValidateSignaturePlugin(propertyKeyBase, pluginProperties);
    return plugin;
  }
  
  boolean modificat = false;
  
  public String[][] getTests() {
    if (!modificat) {
      // TODO Com solucionar Això !!!!
      // ERROR Com és lògic no és un LTV
      FIRMA_DOCUMENT[2][3] = ValidateSignatureResponse.SIGNPROFILE_PADES_LTV;
      // WARN És EPES + SegellDetemps 
      FIRMA_DOCUMENT[6][3] = ValidateSignatureResponse.SIGNPROFILE_EPES;
      // WARN És EPES + SegellDetemps 
      FIRMA_DOCUMENT[7][3] = ValidateSignatureResponse.SIGNPROFILE_EPES;
      
      FIRMA_DOCUMENT[8][3] = ValidateSignatureResponse.SIGNPROFILE_EPES; // ERROR
      modificat = true;
    }
    return FIRMA_DOCUMENT;
  }
  
  
  
  @org.junit.Test
  public void testBasic() throws Exception {
    
    // SignatureValidationTestResult[] results = 
    internalTestBasic();
  }
  
  
  
  public static void main(String[] args) {
    
    try {
      new TestIntegra().testBasic();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  

}
