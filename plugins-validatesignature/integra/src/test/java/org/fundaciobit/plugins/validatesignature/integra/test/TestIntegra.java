package org.fundaciobit.plugins.validatesignature.integra.test;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.fundaciobit.plugins.validatesignature.api.IValidateSignaturePlugin;
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
  
  protected Map<String, String[]> currentTests = null;
  
  public Map<String, String[]> getTests() {
    if (currentTests == null) {
      
      Map<String, String[]> tests = new TreeMap<String, String[]>(super.getTests());
      
      // TODO Com solucionar Això !!!!
      // ERROR Com és lògic no és un LTV
      // FIRMA_DOCUMENT[2][3] = ValidateSignatureResponse.SIGNPROFILE_PADES_LTV;
      // tests.get("peticioOK.pdf")[3] = ValidateSignatureResponse.SIGNPROFILE_PADES_LTV;
      
      // WARN És EPES + SegellDetemps 
      //FIRMA_DOCUMENT[6][3] = ValidateSignatureResponse.SIGNPROFILE_EPES;
      //tests.get("miniapplet_epes_segelltemps_catcert.pdf")[3] = ValidateSignatureResponse.SIGNPROFILE_EPES;
      
      // WARN És EPES + SegellDetemps 
      //FIRMA_DOCUMENT[7][3] = ValidateSignatureResponse.SIGNPROFILE_EPES;
      //tests.get("miniapplet_epes_segelltemps_afirma.pdf")[3] = ValidateSignatureResponse.SIGNPROFILE_EPES;
      
      
      // ERROR
      //tests.get("miniapplet_empleat_public.pdf")[3] = ValidateSignatureResponse.SIGNPROFILE_EPES;
      
      
      //FIRMA_DOCUMENT[8][3] = ValidateSignatureResponse.SIGNPROFILE_EPES; // ERROR
      currentTests = tests;
    }
    return currentTests;
  }
  
  
  
  @org.junit.Test
  public void testBasic() throws Exception {
    
    Map<String, SignatureValidationTestResult> results = internalTestBasic(false);
    
    System.out.println();
    System.out.println();
    System.out.flush();
    
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
      new TestIntegra().testBasic();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  

}
