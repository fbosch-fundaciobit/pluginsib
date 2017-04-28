package org.fundaciobit.plugins.validatesignature.api.test;

import junit.framework.Assert;

import org.fundaciobit.plugins.utils.FileUtils;
import org.fundaciobit.plugins.validatesignature.api.IValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.SignatureRequestedInformation;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureConstants;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureRequest;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureResponse;

/**
 * 
 * @author anadal
 *
 */
public abstract class AbstractTestValidateSignature implements ValidateSignatureConstants {

  public abstract IValidateSignaturePlugin instantiatePlugin() throws Exception;

  public static final String[][] FIRMA_DOCUMENT = new String[][] {
      // MOLT IMPORTANT !!!!! NOUS ELEMENTS S'HAN D'AFEGIR AL FINAL !!!!!!

      { "sensefirmar.pdf", null, null, null, null },
      { "autofirma.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_PADES_BASIC,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED },
      { "peticioOK.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_BES,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED },
      { "pades_ltv.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_PADES_LTV,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED },
      { "hola_firmat.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_BES,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED },
      { "duesfirmes.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_BES,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED },
      { "miniapplet_epes_segelltemps_catcert.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_T,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED },
      { "miniapplet_epes_segelltemps_afirma.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_T,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED },
      { "miniapplet_empleat_public.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_EPES,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED },
      { "foto.cades.detached.csig", "foto.jpg", SIGNTYPE_CAdES, SIGNPROFILE_BES,
          SIGNFORMAT_EXPLICIT_DETACHED },
      { "foto.jpg.csig", null, SIGNTYPE_CAdES, SIGNPROFILE_BES,
          SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED },
      { "foto_xades_attached.xml", null, SIGNTYPE_XAdES, SIGNPROFILE_BES,
          SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED },
      { "sample_xml_firmat_xades_attached.xml", null, SIGNTYPE_XAdES, SIGNPROFILE_BES,
          SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED },
      { "orve_firma.csig", "orve_doc.pdf", SIGNTYPE_CAdES, SIGNPROFILE_EPES,
          SIGNFORMAT_EXPLICIT_DETACHED },
      { "pades_epes.pdf", null, SIGNTYPE_PAdES, SIGNPROFILE_EPES,
          SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED },

      // Firmes amb @firma FEDERAT

      // OK
      { "afirma/foto.jpg_cades_detached.csig", "afirma/foto.jpg", SIGNTYPE_CAdES,
          SIGNPROFILE_BES, SIGNFORMAT_EXPLICIT_DETACHED },
      // OK
      { "afirma/foto.jpg_cades_detached_ts.csig", "afirma/foto.jpg", SIGNTYPE_CAdES,
          SIGNPROFILE_T, SIGNFORMAT_EXPLICIT_DETACHED },
      // OK
      { "afirma/foto.jpg_cades_detached_epes.csig", "afirma/foto.jpg", SIGNTYPE_CAdES,
          SIGNPROFILE_EPES, SIGNFORMAT_EXPLICIT_DETACHED },
      // OK
      { "afirma/sample_xades_attached_firmat_ts.xml", null, SIGNTYPE_XAdES, SIGNPROFILE_T,
          SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED },
      // OK
      { "afirma/sample_xades_attached_firmat.xml", null, SIGNTYPE_XAdES, SIGNPROFILE_BES,
          SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED },
          
 
      

  /* ERROR ATTACHED i ES DETACHED pero crec que es la generació de la firma */
  /*
   * { "afirma/foto.jpg_cades_attached.csig", null, SIGNTYPE_CAdES,
   * SIGNPROFILE_BES, SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED },
   * 
   * { "afirma/foto.jpg_cades_attached_ts.csig", null, SIGNTYPE_CAdES,
   * SIGNPROFILE_BES, SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED },
   */

 
    
   { "afirma/sample_xades_detached_firmat.xml", "afirma/sample.xml",
   SIGNTYPE_XAdES, SIGNPROFILE_BES,
   SIGNFORMAT_EXPLICIT_DETACHED },
    { "afirma/sample_xades_detached_epes_firmat.xml", "afirma/sample.xml", SIGNTYPE_XAdES,
      SIGNPROFILE_EPES, SIGNFORMAT_EXPLICIT_DETACHED },
      

  };

  public String[][] getTests() {
    return FIRMA_DOCUMENT;
  }

  public void internalCheckTestBasicResults(SignatureValidationTestResult result, int i)
      throws Exception {

    Assert.assertNotNull("SignatureValidationTestResult es null", result);

    ValidateSignatureResponse response = result.getValidateSignatureResponse();

    Assert.assertEquals("Tipus de firma diferent en test " + i, result.getExpectedSignType(),
        response.getSignType());

    Assert.assertEquals("Format de firma diferent en test " + i,
        result.getExpectedSignFormat(), response.getSignFormat());

    Assert.assertEquals("Perfil de firma diferent en test " + i,
        result.getExpectedSignProfile(), response.getSignProfile());

  };

  public static boolean compareStr(String str1, String str2) {
    return (str1 == null ? str2 == null : str1.equals(str2));
  }

  public SignatureValidationTestResult[] internalTestBasic() throws Exception {

    IValidateSignaturePlugin plugin = instantiatePlugin();

    String[][] tests = getTests();

    SignatureValidationTestResult[] responses = new SignatureValidationTestResult[tests.length];

    for (int i = 0; i < tests.length; i++) {

      // long start = System.currentTimeMillis();
      System.out.println(" ==== TEST[" + i + "] [F=" + tests[i][0] + "][D=" + tests[i][1]
          + "]");

      ClassLoader classLoader = getClass().getClassLoader();

      byte[] signature = FileUtils.toByteArray(classLoader.getResource(
          "signatures/" + tests[i][0]).openStream());

      // FileUtils.readResource(this.getClass(),"signatures/" +
      // tests[i][0]));
      byte[] document;
      if (tests[i][1] == null) {
        document = null;
      } else {
        document = FileUtils.toByteArray(classLoader.getResource("signatures/" + tests[i][1])
            .openStream());
        // FileUtils.toByteArray(FileUtils.readResource(this.getClass(),
        // "signatures/" + tests[i][1]));
      }

      SignatureValidationTestResult result = new SignatureValidationTestResult(tests[i][0],
          tests[i][1], signature, document, tests[i][2], tests[i][3], tests[i][4]);

      responses[i] = result;

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

      internalCheckTestBasicResults(result, i);

      // AbstractValidateSignaturePlugin.printSignatureInfo(vs);

      // Elapsed Time = 4022 ms
      // Elapsed Time = 1501 ms
      // Elapsed Time = 1365 ms
      // Elapsed Time = 1351 ms
      // System.err.println( " Elapsed Time = " + (System.currentTimeMillis()-
      // start) +" ms");

    }

    return responses;

  }

}
