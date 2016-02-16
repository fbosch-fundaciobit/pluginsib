package org.fundaciobit.plugins.checkcertificate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.fundaciobit.plugins.certificate.ResultatValidacio;
import org.fundaciobit.plugins.certificate.fake.FakeCertificatePlugin;





/**
 * Unit test for simple App.
 */
public class AppTest  {

  public static void main(String[] args) {

      
      File[] toCheck =  new File[] {
          //new File("anadal.cer"),
          //new File("CAMERFIRMA.cer"),
          //new File("catala.cer"),
          //new File("dani-Camerfirma.cer"),
          //new File("c:\\DGIDT.cer")
          new File("c:\\anadal_caib.cer")
      };

      FakeCertificatePlugin fake = new FakeCertificatePlugin();

      for (int i = 0; i < toCheck.length; i++) {
        System.out.println(" ============ " + toCheck[i].getName() + " ===================");
        try {
           X509Certificate cert = decodeCertificate(new FileInputStream(toCheck[i]));
           
           //String result = fake.checkCertificate(cert);
           //
           
           ResultatValidacio res = fake.getInfoCertificate(cert);
           /*
           System.out.println("Resultat = " + (res.getResultatValidacioCodi() == 0 ? "OK" : ("ERROR: " + res.getResultatValidacioDescripcio())));
           
           InformacioCertificat info = res.getInformacioCertificat();
           */
           System.out.println(res.toString());
           
           
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        
      }
    
  }
  
  
  public static X509Certificate decodeCertificate(InputStream is) throws Exception  {

    X509Certificate result = null;
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    result = (X509Certificate) cf.generateCertificate(is);
    is.close();
    return result;

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
