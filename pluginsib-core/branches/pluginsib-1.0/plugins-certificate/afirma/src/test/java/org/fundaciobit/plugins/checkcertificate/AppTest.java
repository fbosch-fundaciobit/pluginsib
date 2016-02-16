package org.fundaciobit.plugins.checkcertificate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.fundaciobit.plugins.certificate.afirma.CheckCertificateException;
import org.fundaciobit.plugins.certificate.afirma.ValidaCertificat;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

  public static void main(String[] args) {

    try {
      
      File parent = new File("C:\\Documents and Settings\\anadal\\Escritorio\\certificats\\");
      
      //File pkcs7 = new File("c:\\DNIe.x509");

      //File pkcs7 = new File("c:\\CAMERFIRMA.cer");

      // File pkcs7 = new File("c:\\anadal.cer");
      //File pkcs7 = new File("c:\\catala.cer");

      // File pkcs7 = new File("c:\\joanpetit.x509");
      
      
      //File pkcs7 = new File("c:\\godaddy.cer");
      
      //File pkcs7 = new File("c:\\dani-Camerfirma.cer");

      File pkcs7 = new File(parent, "DGIDT.cer"); // <=== CHANGE THIS
      
      


      byte[] data = getBytesFromFile(pkcs7);

      String afirma_url_base = "http://des-afirma.redsara.es/afirmaws/services/";
      //String afirma_url_base = "http://afirma.redsara.es/afirmaws/services";

      ValidaCertificat validaCert;

      // ======= USUARI - CONTRASENYA
      String afirma_app_id = ""; // <=== CHANGE THIS
      String afirma_usr = ""; // <=== CHANGE THIS !!!!!
      String afirma_pwd = ""; // <=== CHANGE THIS !!!!!      
      validaCert = new ValidaCertificat(afirma_url_base, afirma_app_id, afirma_usr, afirma_pwd);
      


      // ======= CERTIFICAT
      /*
      String afirma_app_id = ""; // <=== CHANGE THIS

      String keystoreLocation = ""; // <=== CHANGE THIS
      // .jks for type "JKS",
      // .p12 or .pfx for type "PKCS12"
      String keystoreType= "JKS"; // <=== CHANGE THIS
      String keystorePassword = ""; // <=== CHANGE THIS
      String keystoreCertAlias = ""; // <=== CHANGE THIS
      String keystoreCertPassword = ""; // <=== CHANGE THIS

      
      validaCert = new ValidaCertificat(afirma_url_base, afirma_app_id,
          keystoreLocation, keystoreType,
          keystorePassword, keystoreCertAlias, keystoreCertPassword);
      */
      
      try {

        boolean obtenirDadesCertificat = true;
        int modeValidacio = ValidaCertificat.MODE_VALIDACIO_AMB_REVOCACIO;
        //int modeValidacio = ValidaCertificat.MODE_VALIDACIO_CADENA;
        //int modeValidacio = ValidaCertificat.MODE_VALIDACIO_SIMPLE;
    
        org.fundaciobit.plugins.certificate.ResultatValidacio rv = validaCert.validar(data, 
             obtenirDadesCertificat, modeValidacio);
        System.out.println(" =============== OK ================");
    
        
        System.out.println(rv.toString());
        


      } catch(CheckCertificateException checkExc) {
        System.out.println(" ======= Error ============== ");

        System.out.println("      getErrorCodi: " + checkExc.getErrorCodi());
        System.out.println("getErrorDescripcio: " + checkExc.getErrorDescripcio());
        System.out.println("  getErrorExcepcio: " + checkExc.getErrorExcepcio());

       

      }

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  // Returns the contents of the file in a byte array.
  public static byte[] getBytesFromFile(File file) throws IOException {
    InputStream is = new FileInputStream(file);

    // Get the size of the file
    long length = file.length();

    // You cannot create an array using a long type.
    // It needs to be an int type.
    // Before converting to an int type, check
    // to ensure that file is not larger than Integer.MAX_VALUE.
    if (length > Integer.MAX_VALUE) {
      // File is too large
    }

    // Create the byte array to hold the data
    byte[] bytes = new byte[(int) length];

    // Read in the bytes
    int offset = 0;
    int numRead = 0;
    while (offset < bytes.length
        && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
      offset += numRead;
    }

    // Ensure all the bytes have been read in
    if (offset < bytes.length) {
      throw new IOException("Could not completely read file " + file.getName());
    }

    // Close the input stream and return bytes
    is.close();
    return bytes;
  }


  /**
   * Create the test case
   * 
   * @param testName
   *          name of the test case
   */
  public AppTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(AppTest.class);
  }

  /**
   * Rigourous Test :-)
   */
  public void testApp() {
    assertTrue(true);
  }
}
