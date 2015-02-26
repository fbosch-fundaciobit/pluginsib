package org.fundaciobit.plugins.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * 
 * @author anadal
 * 
 */
public class CertificateUtils {
  
  private final static Logger log = Logger.getLogger(CertificateUtils.class);

  /**
   * Codifica la informacion almacenada en un objeto X509Certificate
   * 
   * @param certificate
   *          Objeto X509Certificate con la informacion de un certificado de
   *          usuario
   * 
   * @return Array de bytes con la informacion codificada
   * 
   */
  public static byte[] encodeCertificate(X509Certificate certificate) throws Exception {
    byte[] result = null;
    result = certificate.getEncoded();

    return result;
  }

  /**
   * Obtiene el objeto X509Certificate a partir de los datos codificados de un
   * certificado
   * 
   * @param data
   *          Array de bytes con la informacion codificada de un certificado
   * 
   * @return Objeto X509Certificate
   * 
   */
  public static X509Certificate decodeCertificate(InputStream is) throws Exception {

    X509Certificate result = null;
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    result = (X509Certificate) cf.generateCertificate(is);
    is.close();
    return result;

  }

  // TODO Moure a Test
  public static void main(String[] args) {

    try {
      File f = new File("anadal_caib.cer");

      X509Certificate cert = decodeCertificate(new FileInputStream(f));

      System.out.println("DNI: |" + getDNI(cert) + "|");

    } catch (Exception e) {
      // TODO: handle exception
    }

  }

  /**
   * Recupera el commonName a partir de los datos del certificado
   * 
   * @param certificate
   *          Objeto X509Certificate
   * @return String con el commonName del usuario
   */
  public static String getCommonName(X509Certificate certificate) {

    String cn = null;

    if (certificate != null) {

      String dn = certificate.getSubjectDN().getName().trim();

      //System.out.println(dn);
      int index = dn.indexOf("CN=");
      if (index != -1) {
        cn = dn.substring(index + 3);
      }

      // Checking DNIe CN
      if (dn.indexOf("SERIALNUMBER") != -1) {
        cn = getCnDNIe(cn);

      } else {
        // If we have more things that we don't need
        index = cn.indexOf(",");
        if (index != -1) {
          cn = cn.substring(0, index);
        }

        // If it is a company
        int indexCIF = cn.indexOf("- CIF ");
        if (indexCIF != -1) {
          // Some certs has a commonName like "Name Surname -
          // CIF:0000000T - NOMBRE Surname name - NIF 00000000T"
          // We will crop ' - CIF:' text and delete ' - NOMBRE Surname name -
          // NIF 00000000T'.
          String cif = cn.substring(indexCIF + 6);
          cif = cif.substring(0, cif.indexOf(" "));
          cn = cn.substring(0, indexCIF) + cif;
          // Some certs (FNMT) has a commonName like "ENTIDAD EntityName
          // - CIF 0000000T"
          cn = cn.replace("  ", " ");
          cn = cn.replaceFirst("ENTIDAD ", "");
        } else {
          // Some certs has a commonName like "Name Surname -
          // NIF:0000000T"
          // We will crop ' - NIF:' text.
          cn = cn.replaceFirst("- NIF", "");
          cn = cn.replaceFirst(":", "");
          // Some certs (FNMT) has a commonName like "NOMBRE Surnames name
          // - NIF 0000000T"
          cn = cn.replace("  ", " ");
          cn = cn.replaceFirst("NOMBRE ", "");
        }
      }
    }

    return cn;
  }

  /**
   * Recupera el DNI a partir de los datos del certificado
   * 
   * @param certificate
   *          Objeto X509Certificate
   * @return String con el DNI del usuario
   */
  public static String getDNI(X509Certificate certificate) {

    String nif;

    if (certificate != null) {

      HashMap<String, String> map = new HashMap<String, String>();
      // TODO fer split emprant "(?<!\\),"
      
      final boolean debug = log.isDebugEnabled();
      
      String value =  certificate.getSubjectDN().getName().trim();
      if (debug) {
        log.debug("========================================");
        log.debug("VALUES = ]" + value + "[");
      }
      
      String[] split =value.split(",");
      for (int i = 0; i < split.length; i++) {
        if (debug) {
          log.debug("SPLIT[" + i + "] = ]" + split[i] + "[");
        }
        if (split[i].indexOf('=') != -1) {
          String[] split2 = split[i].split("=");
          if (split2.length == 2) {
            map.put(split2[0].trim(), split2[1].trim());
          }
        }
      }
      nif = map.get("SERIALNUMBER");
      if (nif == null) {
        // Per certificats tipus FNMT
        String cadena = map.get("CN");
        int finom = cadena.indexOf(" - NIF ");
        if (finom == -1) {
          nif = null;
        } else {
          int iniciNif = finom + " - NIF ".length();
          nif = cadena.substring(iniciNif);
        }
      }
    } else {
      nif = null;
    }

    return nif;

    /*
     * String dn = certificate.getSubjectDN().getName().trim();
     * 
     * 
     * 
     * int index = dn.indexOf("SERIALNUMBER"); if (index != -1) { index =
     * dn.indexOf('=',index); int index2 = dn.indexOf(',', index);
     * 
     * nif = dn.substring(index + 1, index2).trim();
     * 
     * }
     * 
     * }
     */

  }

  /***************************************************************************
   * METODOS PRIVADOS
   **************************************************************************/

  /**
   * Devuelve el campo CN pero utilizando el DNI electronico
   */

  private static String getCnDNIe(String cn) {

    String surnames = "";
    String name = "";
    String dni = "";

    // DNIe DN its suposed to be like
    // CN="SURNAME SURNAME, NAME (AUTENTICACIÃ“N)", GIVENNAME=NAME,
    // SURNAME=SURNAME, SERIALNUMBER=XXXXXXXXN, C=ES

    // At this point we have in cn String "SURNAME SURNAME, NAME
    // (AUTENTICACIÃ“N)", GIVENNAME=NAME, SURNAME=SURNAME,
    // SERIALNUMBER=XXXXXXXXN, C=ES
    int pos = cn.indexOf(",");
    if (pos != -1) {
      surnames = cn.substring(1, pos);
    }

    pos = cn.indexOf("GIVENNAME=");
    if (pos != -1) {
      cn = cn.substring(pos + 10, cn.length());
    }

    pos = cn.indexOf(",");
    if (pos != -1) {
      name = cn.substring(0, pos);
    }

    pos = cn.indexOf("SERIALNUMBER=");
    if (pos != -1) {
      cn = cn.substring(pos + 13, cn.length());
    }

    pos = cn.indexOf(",");
    if (pos == -1) {
      pos = cn.length();
    }

    dni = cn.substring(0, pos);
    cn = name + " " + surnames + " " + dni;

    return cn;
  }

}
