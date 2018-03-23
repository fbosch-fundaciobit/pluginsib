package org.fundaciobit.plugins.validatesignature.api.test;

import java.util.Map;
import java.util.TreeMap;

import junit.framework.Assert;

import org.fundaciobit.plugins.utils.FileUtils;
import org.fundaciobit.plugins.validatesignature.api.IValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.SignatureRequestedInformation;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureConstants;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureRequest;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureResponse;
import org.fundaciobit.plugins.validatesignature.api.ValidationStatus;

/**
 * 
 * @author anadal
 *
 */
public abstract class AbstractTestValidateSignature implements ValidateSignatureConstants {

  public abstract IValidateSignaturePlugin instantiatePlugin() throws Exception;
  
  public static final Map<String, String[]> TESTS_VALIDA_FIRMA;
  
  static {
  

    String[][] FIRMA_DOCUMENT = new String[][] {
      // MOLT IMPORTANT !!!!! NOUS ELEMENTS S'HAN D'AFEGIR AL FINAL !!!!!!

      { "sensefirmar.pdf", null, null, null, null , "ERROR" },
      { "autofirma.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_PADES_BASIC,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED , null },
      { "peticioOK.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_BES,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED , null },
      { "pades_ltv.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_PADES_LTV,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED , null },
      { "hola_firmat.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_BES,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED , null },
      { "duesfirmes.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_BES,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED , null },
      { "miniapplet_epes_segelltemps_catcert.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_EPES, //SIGNPROFILE_T,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED , null },
      { "miniapplet_epes_segelltemps_afirma.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_EPES, //SIGNPROFILE_T,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED , null },
      { "miniapplet_empleat_public.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_EPES,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED , null },
      { "foto.cades.detached.csig", "foto.jpg", SIGNTYPE_CAdES, SIGNPROFILE_BES,
          SIGNFORMAT_EXPLICIT_DETACHED , null },
      { "foto.jpg.csig", null, SIGNTYPE_CAdES, SIGNPROFILE_BES,
          SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED , null },
      { "foto_xades_attached.xml", null, SIGNTYPE_XAdES, SIGNPROFILE_BES,
          SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED , null },
      { "sample_xml_firmat_xades_attached.xml", null, SIGNTYPE_XAdES, SIGNPROFILE_BES,
          SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED , null },
      { "orve_firma.csig", "orve_doc.pdf", SIGNTYPE_CAdES, SIGNPROFILE_EPES,
          SIGNFORMAT_EXPLICIT_DETACHED , null },
      { "pades_epes.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_EPES,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED , null },
          
      { "openoffice.odt", null, SIGNTYPE_ODF, null,
            SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED , null },

      // Firmes amb @firma FEDERAT

      // OK
      { "afirma/foto.jpg_cades_detached.csig", "afirma/foto.jpg", SIGNTYPE_CAdES,
          SIGNPROFILE_BES, SIGNFORMAT_EXPLICIT_DETACHED , null },
      // OK
      { "afirma/foto.jpg_cades_detached_ts.csig", "afirma/foto.jpg", SIGNTYPE_CAdES,
          SIGNPROFILE_T, SIGNFORMAT_EXPLICIT_DETACHED , null },
      // OK
      { "afirma/foto.jpg_cades_detached_epes.csig", "afirma/foto.jpg", SIGNTYPE_CAdES,
          SIGNPROFILE_EPES, SIGNFORMAT_EXPLICIT_DETACHED, null },
      // OK
      { "afirma/sample_xades_attached_firmat_ts.xml", null, SIGNTYPE_XAdES, SIGNPROFILE_T,
          SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED , null },
      // OK
      { "afirma/sample_xades_attached_firmat.xml", null, SIGNTYPE_XAdES, SIGNPROFILE_BES,
          SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED , null },
        
  /* ERROR ATTACHED i ES DETACHED pero crec que es la generació de la firma */
  /*
   * { "afirma/foto.jpg_cades_attached.csig", null, SIGNTYPE_CAdES,
   * SIGNPROFILE_BES, SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED , null },
   * 
   * { "afirma/foto.jpg_cades_attached_ts.csig", null, SIGNTYPE_CAdES,
   * SIGNPROFILE_BES, SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED , null },
   */

   { "afirma/sample_xades_detached_firmat.xml", "afirma/sample.xml",
   SIGNTYPE_XAdES, SIGNPROFILE_BES,
   SIGNFORMAT_EXPLICIT_DETACHED , null },
    { "afirma/sample_xades_detached_epes_firmat.xml", "afirma/sample.xml", SIGNTYPE_XAdES,
      SIGNPROFILE_EPES, SIGNFORMAT_EXPLICIT_DETACHED, null },
      

  };
   
    TESTS_VALIDA_FIRMA = new TreeMap<String, String[]>();
    
    
    for (int i = 0; i < FIRMA_DOCUMENT.length; i++) {
      TESTS_VALIDA_FIRMA.put(FIRMA_DOCUMENT[i][0], FIRMA_DOCUMENT[i]);
    }
    
    
  }

  public Map<String, String[]> getTests() {
    return TESTS_VALIDA_FIRMA;
  }

  public String internalCheckTestBasicResults(SignatureValidationTestResult result, String key,
      boolean throwException)
      throws Exception {

    if (throwException) {
      
      Assert.assertNotNull("SignatureValidationTestResult es null", result);

      ValidateSignatureResponse response = result.getValidateSignatureResponse();
      
      int status = response.getValidationStatus().getStatus();
      
      if (result.isExpectedError()) {
        
        if (status == ValidationStatus.SIGNATURE_VALID) {
          throw new Exception("S'esperava un error, però l'execució del test ha funcionat.");
        } else {
          return null; // OK
        }
        
      }
      
      
      if (response.getSignType() == null && response.getSignFormat() == null
          && response.getSignProfile() == null) {
        if (status != ValidationStatus.SIGNATURE_VALID) {
          throw new Exception("La cridada al Validator/Informador de Firmes ha retornat un error: " 
              + response.getValidationStatus().getErrorMsg());
        }
      }
      
  
      Assert.assertEquals("Tipus de firma diferent en test " + key, result.getExpectedSignType(),
          response.getSignType());
  
      Assert.assertEquals("Format de firma diferent en test " + key,
          result.getExpectedSignFormat(), response.getSignFormat());
  
      Assert.assertEquals("Perfil de firma diferent en test " + key,
          result.getExpectedSignProfile(), response.getSignProfile());
      

      
    } else {
      
      if (result == null) {
        return "SignatureValidationTestResult es null";
      }
      
      ValidateSignatureResponse response = result.getValidateSignatureResponse();
      
      if (response == null) {
        return "REsponse Val Null"; // ERROR
      }
      
      if (response.getValidationStatus() == null) {
        return "No s'ah definit getValidationStatus"; // ERROR
      }
      
      
      int status = response.getValidationStatus().getStatus();
      
      if (result.isExpectedError()) {
        
        
      
        if(status == ValidationStatus.SIGNATURE_VALID) {
           return "S'esperava un error, però l'execució del test ha funcionat."; // ERROR
        } else {
           return null; // OK;
        }
        
      }
      
      
      if (response.getSignType() == null && response.getSignFormat() == null
          && response.getSignProfile() == null) {
        if (status != ValidationStatus.SIGNATURE_VALID) {
          return "La cridada al Validator/Informador de Firmes ha retornat un error: " 
            + response.getValidationStatus().getErrorMsg();
        }
      }

      if (!compareStr(result.getExpectedSignType(),
          response.getSignType())) {
        return "Tipus de firma diferent en test " + key + "(Expected:" +result.getExpectedSignType() + " | " +
            response.getSignType() + ")";
      }

  
      if (!compareStr(result.getExpectedSignFormat(), response.getSignFormat())) {
        return "Format de firma diferent en test " + key + "(Expected: " 
          + result.getExpectedSignFormat() + " | "
            + response.getSignFormat() ;
      }

  
      if (!compareStr(result.getExpectedSignProfile(), response.getSignProfile())) {
        return "Perfil de firma diferent en test " + key + "(Expected:" + result.getExpectedSignProfile() + " | " + response.getSignProfile();
      }
      
    }
    
    
    return null;

  };

  public static boolean compareStr(String str1, String str2) {
    return (str1 == null ? str2 == null : str1.equals(str2));
  }

  public Map<String,SignatureValidationTestResult> internalTestBasic(boolean checkResults) throws Exception {

    IValidateSignaturePlugin plugin = instantiatePlugin();

    Map<String, String[]> tests = getTests();
    
   

    Map<String,SignatureValidationTestResult> responses = new TreeMap<String, SignatureValidationTestResult>();

    
    for (String key : tests.keySet()) {
      
      String[] params = tests.get(key);

      // long start = System.currentTimeMillis();
      System.out.println(" ==== TEST[" + key + "] [F=" + params[0] + "][D=" + params[1]
          + "]");

      

      byte[] signature = getResource("signatures/" + params[0]);

      // FileUtils.readResource(this.getClass(),"signatures/" +
      // tests[i][0]));
      byte[] document;
      if (params[1] == null) {
        document = null;
      } else {
        document = getResource("signatures/" + params[1]);
        // FileUtils.toByteArray(FileUtils.readResource(this.getClass(),
        // "signatures/" + tests[i][1]));
      }

      SignatureValidationTestResult result = new SignatureValidationTestResult(params[0],
          params[1], signature, document, params[2], params[3], params[4], 
          (params[5] == null? false: true));

      responses.put(key, result);

      try {

        ValidateSignatureRequest validationRequest = new ValidateSignatureRequest();

        validationRequest.setSignatureData(signature);
        validationRequest.setSignedDocumentData(document);

        SignatureRequestedInformation sri = new SignatureRequestedInformation();
        sri.setReturnSignatureTypeFormatProfile(true);
        sri.setReturnCertificateInfo(false);
        sri.setReturnValidationChecks(false);
        sri.setValidateCertificateRevocation(false);
        sri.setReturnCertificates(false);
        sri.setReturnTimeStampInfo(false);

        validationRequest.setSignatureRequestedInformation(sri);

        ValidateSignatureResponse vs = plugin.validateSignature(validationRequest);

        if (vs == null) {
          throw new Exception(" ValidateSignatureResponse  es null");
        }

        result.setValidateSignatureResponse(vs);

      } catch (Throwable th) {

        th.printStackTrace(System.err);

        result.setException(th);
      }

      if (checkResults) {
        internalCheckTestBasicResults(result, key, true);
      }

    }

    return responses;

  }
  
  
  public byte[] getResource(String path) throws Exception {
    ClassLoader classLoader = getClass().getClassLoader();
    
    byte[] data = FileUtils.toByteArray(classLoader.getResourceAsStream(path));
    return data;
  }

}
