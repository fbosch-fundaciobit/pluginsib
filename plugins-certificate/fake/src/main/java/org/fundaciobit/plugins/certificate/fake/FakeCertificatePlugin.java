package org.fundaciobit.plugins.certificate.fake;

import java.util.Properties;


import java.io.StringReader;
import java.security.cert.X509Certificate;



import org.apache.log4j.Logger;
import org.fundaciobit.plugins.certificate.ICertificatePlugin;
import org.fundaciobit.plugins.certificate.InformacioCertificat;
import org.fundaciobit.plugins.certificate.ResultatValidacio;
import org.fundaciobit.plugins.utils.CertificateUtils;

/**
 *
 * @author anadal
 *
 */
public class FakeCertificatePlugin implements ICertificatePlugin {

  protected final Logger log = Logger.getLogger(getClass());



  @Override
  public ResultatValidacio getInfoCertificate(X509Certificate cert) throws Exception {

    ResultatValidacio result = new ResultatValidacio();
    
    String error = checkCertificate(cert);
    
    if (error == null) {
      result.setResultatValidacioCodi(0); // OK
      result.setResultatValidacioDescripcio("OK");
    } else {
      result.setResultatValidacioCodi(-1);
      result.setResultatValidacioDescripcio(error);
    }

    
    Properties propIssuer = new Properties();
    propIssuer.load(new StringReader(cert.getIssuerDN().getName().replaceAll(",", "\n")));
    // TODO
    /*
    System.out.println("----- ISSUER -----------");
    propIssuer.list(System.out);
    */
    
    
    Properties propSubject = new Properties();
    propSubject.load(new StringReader(cert.getSubjectDN().getName().replaceAll(",", "\n")));
    // TODO
    /*
    System.out.println("----- SUBJECT -----------");
    propSubject.list(System.out);
    */
    


    InformacioCertificat info = new InformacioCertificat();
    info.setClassificacio(InformacioCertificat.CLASSIFICACIO_DESCONEGUDA);
    info.setDataNaixement(null);
    info.setEmail(propSubject.getProperty("EMAILADDRESS"));
    
    info.setEmissorID(cert.getIssuerDN().getName());
    info.setEmissorOrganitzacio(propIssuer.getProperty("O"));
    
    log.info("\n\n  NIF = " + CertificateUtils.getDNI(cert) + "\n\n");
    
    info.setNifResponsable(CertificateUtils.getDNI(cert));
    info.setNomCompletResponsable(propSubject.getProperty("CN"));
    info.setNomResponsable(propSubject.getProperty("GIVENNAME"));
    
    
    
    {
      String llinatges = propSubject.getProperty("SURNAME");
      if (llinatges != null) {
        String[] llinatgesSplit =  llinatges.split(" ");
        if (llinatgesSplit.length == 2) {
          info.setPrimerLlinatgeResponsable(llinatgesSplit[0]);
          info.setSegonLlinatgeResponsable(llinatgesSplit[1]);
        } else {
          info.setPrimerLlinatgeResponsable(llinatges);
        }        
      }
    }
    info.setNumeroSerie(cert.getSerialNumber());
    info.setPais(propSubject.getProperty("C"));
    
    
    
    info.setPolitica(null);
    info.setPoliticaID(CertificateUtils.getCertificatePolicyId(cert));
    info.setPoliticaVersio(null);
    info.setRaoSocial(null);
    info.setSubject(cert.getSubjectDN().getName());
    info.setTipusCertificat(null);
    info.setUnitatOrganitzativa(propSubject.getProperty("OU"));
    info.setUnitatOrganitzativaNifCif(propSubject.getProperty("OID.1.3.6.1.4.1.17326.30.3"));
    info.setUsCertificat(null);
    info.setUsCertificatExtensio(null);
    
    info.setValidDesDe(cert.getNotBefore());
    info.setValidFins(cert.getNotAfter());

    
    result.setInformacioCertificat(info);
 
    return result;
    
  }
  

  @Override
  public String checkCertificate(X509Certificate cert) throws Exception {

    try {
      
      long now = System.currentTimeMillis();
      
      java.util.Date from = cert.getNotBefore();
      System.out.println("From: " + from);
      if (now < from.getTime()) {
        return "La data d´inici del certificat es posterior a la data actual";
      }

      java.util.Date to = cert.getNotAfter();
      System.out.println("To: " + to);
      if (now > to.getTime()) {
        return "El certificat ha caducat";
      }

      return null;
   
    } catch (Exception e) {

      log.error("Error no controlat cridant a @firma " + e.getMessage(), e);

      return e.getMessage();
    }

  }


}
