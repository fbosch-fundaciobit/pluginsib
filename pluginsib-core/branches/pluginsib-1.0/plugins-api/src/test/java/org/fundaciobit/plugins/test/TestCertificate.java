package org.fundaciobit.plugins.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.utils.CertificateUtils;


/**
 * 
 * @author anadal
 *
 */
public class TestCertificate extends TestUtils {

  public static Logger log = Logger.getLogger(TestCertificate.class);

  public static boolean debug = false;

  public static String[] getCertificates() {
    String certsStr = getProperty("certificates");
    if (certsStr == null || certsStr.trim().length() == 0) {
      return new String[0];
    } else {
      return certsStr.split(",");
    }

  }

  public void testCertificate() {

    String[] certs = getCertificates();

    for (String certNumber : certs) {
      System.out.println(" ---- " + certNumber + " -------");

      String certInfo = getProperty("cert." + certNumber);

      String[] fields = certInfo.split("\\|");

      String filePath = fields[0];
      String passwordks = fields[1];
      String type = fields[2];

      // String passwordcert = passwordks;

      // System.out.println("filePath: " + filePath);
      // System.out.println("passwordks: " + passwordks);

      // Stirng alias
      // Stirng password
      try {

        // System.out.println("XXXXXXXXX = " + KeyStore.getDefaultType());

        X509Certificate certificate1;

        if ("pkcs12".equals(type)) {

          List<Certificate> cc = CertificateUtils.readCertificatesOfKeystore(
              new FileInputStream(new File(filePath)), passwordks);
          if (cc == null || cc.size() == 0) {
            throw new Exception("Certificat " + certNumber + " Esta buit.");
          }
          certificate1 = (X509Certificate) cc.get(0);

        } else {
          InputStream certstream = new FileInputStream(new File(filePath));
          certificate1 = CertificateUtils.decodeCertificate(certstream);
          // TODO
          /*
           * CertificateFactory cf = CertificateFactory.getInstance("X.509");
           * 
           * certificate1 = (X509Certificate)
           * cf.generateCertificate(certstream);
           */

        }
        if (debug == true) {
          System.out.println("Certificate: " + certificate1);

          System.out.println("Subject Name DN: " + certificate1.getSubjectDN().getName());
          System.out.println("Subject Name: " + CertificateUtils.getCN(certificate1));
          System.out.println("Emissior Name DN: " + certificate1.getIssuerDN().toString());
          System.out.println("Emissior Name: "
              + CertificateUtils.getCN(certificate1.getIssuerDN().toString()));
          System.out.println();
        }

        //
        System.out.println("Subject getSimpleName: "
            + CertificateUtils.getSubjectCorrectName(certificate1));
        System.out.println("Subject NIF: " + CertificateUtils.getDNI(certificate1));
        String unitatAdministrativa = CertificateUtils.getUnitatAdministrativa(certificate1);
        if (unitatAdministrativa != null) {
          System.out.println("Unitat Administrativa: " + unitatAdministrativa);
        }
        String carrec = CertificateUtils.getCarrec(certificate1);
        if (carrec != null) {
          System.out.println("Carrec: " + carrec);
        }
        System.out.println("Emissor: "
            + CertificateUtils.getCN(certificate1.getIssuerDN().toString()));
        
        
        String[] infoEmpresa = CertificateUtils.getEmpresaNIFNom(certificate1);
        if (infoEmpresa != null) {
          System.out.println("Empresa-NIF: " + infoEmpresa[0]);
          System.out.println("Empresa-Nom: " + infoEmpresa[1]);
        }

      } catch (Exception e) {
        System.out.println("ERROR [" + certNumber + "]");
        e.printStackTrace();
      }

    }

  }
  
  
  

  
  
  
  public static void main(String[] args) {

    new TestCertificate().testCertificate();

  }
  
  

}
