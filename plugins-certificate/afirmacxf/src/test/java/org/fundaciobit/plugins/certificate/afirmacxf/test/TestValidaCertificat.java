package org.fundaciobit.plugins.certificate.afirmacxf.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.fundaciobit.plugins.certificate.ResultatValidacio;
import org.fundaciobit.plugins.certificate.afirmacxf.ValidaCertificat;

/**
 * 
 * @author anadal
 * 
 */
public class TestValidaCertificat {

  public static void main(String[] args) {
    String endPoint = "http://des-afirma.redsara.es/afirmaws/services/ValidarCertificado";
    testCertWithCertificateLogin(endPoint);
    testCertWithUsrPwdLogin(endPoint);
  }

  public static void testCertWithCertificateLogin(String endPoint) {
    try {
      

      String keystoreLocation = "keystore.jks"; // <= Add here

      System.out.println("FILE EXISTS = " + new File(keystoreLocation).exists());
      // .jks for type "JKS",
      // .p12 or .pfx for type "PKCS12"

      String keystoreType = "jks";
      String keystorePassword = ""; // <= Add here
      String keystoreCertAlias = ""; // <= Add here
      String keystoreCertPassword = ""; // <= Add here

      String aplicacioId = ""; // <= Add here

      File base = new File("C:\\Documents and Settings\\anadal\\Escritorio\\certificats\\");
      File pkcs7 = new File(base, "anadal_caib.cer");
      byte[] data = getBytesFromFile(pkcs7);

      int modeValidacio = ValidaCertificat.MODE_VALIDACIO_AMB_REVOCACIO;
      // int modeValidacio = ValidaCertificat.MODE_VALIDACIO_CADENA;
      // int modeValidacio = ValidaCertificat.MODE_VALIDACIO_SIMPLE;

      ValidaCertificat vc = new ValidaCertificat(endPoint, aplicacioId, modeValidacio,
          keystoreLocation, keystoreType, keystorePassword, keystoreCertAlias,
          keystoreCertPassword);

      boolean obtenirDadesCertificat = false;
      ResultatValidacio rv = vc.validar(data, obtenirDadesCertificat);

      System.out.println(" =============== OK ================");
      System.out.println(rv.toString());

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void testCertWithUsrPwdLogin(String endPoint) {
    try {
      String username = ""; // <= Add here
      String password = ""; // <= Add here
      String aplicacioId = ""; // <= Add here

      File base = new File("C:\\Documents and Settings\\anadal\\Escritorio\\certificats\\");
      File pkcs7 = new File(base, "anadal_caib.cer");
      byte[] data = getBytesFromFile(pkcs7);

      int modeValidacio = ValidaCertificat.MODE_VALIDACIO_AMB_REVOCACIO;
      // int modeValidacio = ValidaCertificat.MODE_VALIDACIO_CADENA;
      // int modeValidacio = ValidaCertificat.MODE_VALIDACIO_SIMPLE;

      ValidaCertificat vc = new ValidaCertificat(endPoint, aplicacioId, modeValidacio,
          username, password);

      boolean obtenirDadesCertificat = false;
      ResultatValidacio rv = vc.validar(data, obtenirDadesCertificat);

      System.out.println(" =============== OK ================");
      System.out.println(rv.toString());

    } catch (Exception e) {

      System.out.println(e.getMessage());

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

}
