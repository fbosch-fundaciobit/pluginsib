package org.fundaciobit.plugins.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;



import java.util.Map;
import java.util.logging.Logger;

import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AttributeNameEnumeration;
import sun.security.x509.CertAttrSet;
import sun.security.x509.DNSName;
import sun.security.x509.Extension;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNameInterface;
import sun.security.x509.GeneralNames;
import sun.security.x509.IPAddressName;
import sun.security.x509.OIDName;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.RFC822Name;
import sun.security.x509.URIName;
import sun.security.x509.X500Name;

/**
 *
 * @author anadal
 *
 */
@SuppressWarnings("restriction")
public class CertificateUtils {

  private final static Logger log = Logger.getLogger(CertificateUtils.class.getName());

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


  /**
   * 
   * @param filePath
   * @param passwordks
   * @return
   * @throws KeyStoreException
   * @throws NoSuchProviderException
   * @throws IOException
   * @throws NoSuchAlgorithmException
   * @throws CertificateException
   * @throws FileNotFoundException
   * @throws Exception
   */
  public static  List<Certificate> readCertificatesOfKeystore(InputStream is,
      String passwordks) throws KeyStoreException, NoSuchProviderException, IOException,
      NoSuchAlgorithmException, CertificateException, FileNotFoundException, Exception {
    KeyStore ks = KeyStore.getInstance("pkcs12", "SunJSSE"); 
    ks.load(is,passwordks.toCharArray()); 
    
    Enumeration<String> aliases = ks.aliases(); 
    String alias = null;
    List<Certificate> certificats = new ArrayList<Certificate>();
    while(aliases.hasMoreElements()) {
      alias = aliases.nextElement();
      //System.out.println(certNumber +  " => Alias = ]" + alias +"[");
      
      //Key key = ks.getKey(alias, passwordcert.toCharArray());
      //System.out.println("key = " + key);
      
      Certificate[] cc = ks.getCertificateChain(alias);
      if (cc != null && cc.length != 0) {
        for (Certificate certificate : cc) {
          certificats.add(certificate); 
        }
        
      }
      
    }
    
    return certificats;
  }
  
  
  
  public static  PublicCertificatePrivateKeyPair readPKCS12(InputStream p12, String p12Password) throws KeyStoreException,
    IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
  
      
    String keyAlias= null;
    KeyStore keystore = KeyStore.getInstance("PKCS12");
    keystore.load(p12, p12Password.toCharArray());
    
    
    Enumeration<String> aliases = keystore.aliases();
    
    while (aliases.hasMoreElements()) {
        keyAlias = (String) aliases.nextElement();
    }
    
    return  new PublicCertificatePrivateKeyPair(
        (X509Certificate) keystore.getCertificate(keyAlias),
        (PrivateKey)keystore.getKey(keyAlias, p12Password.toCharArray())
        );
  
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

      // System.out.println(dn);
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
      // TODO emprar mètode getRDNvalue() per obtenir SERIALNUMBER

      final boolean debug = log.isLoggable(java.util.logging.Level.FINE);

      String value = certificate.getSubjectDN().getName().trim();
      if (debug) {
        log.fine("========================================");
        log.fine("VALUES = ]" + value + "[");
      }

      String[] split = value.split(",");
      for (int i = 0; i < split.length; i++) {
        if (debug) {
          log.fine("SPLIT[" + i + "] = ]" + split[i] + "[");
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
        if (cadena == null) {
          nif = null;
        } else {
          int finom = cadena.indexOf(" - NIF ");
          if (finom == -1) {
            nif = null;
          } else {
            int iniciNif = finom + " - NIF ".length();
            nif = cadena.substring(iniciNif);
          }
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

  /*
  public static String getUnitatAdministrativa(X509Certificate cert) {
    return getGenericOID(cert, "OID.2.16.724.1.3.5.3.2.10"); 
  }
  
  /**
   * 
   * @param cert
   * @return
   */
  /*
  public static String getCarrec(X509Certificate cert) {
    return getGenericOID(cert, "OID.2.16.724.1.3.5.3.2.11");
  }
  
  */

  /*
  // TODO S'ha de llegir amb un DerValue (Veure mètode getCertificatePolicyId)

   private static String getGenericOID(X509Certificate cert, String oid) { 
     
    String str = cert.toString();
    String nom = null;
    int posOID = str.indexOf(oid);
    try {
      if (posOID != -1) {
        
        int posComa = str.indexOf(',', posOID);
        int posNewLine = str.indexOf('\n', posOID);
        if (posComa == -1) {
          if (posNewLine == -1) {
            nom = str.substring(posOID + oid.length() + 1, str.length());
          } else {
            nom = str.substring(posOID + oid.length() + 1, posNewLine);
          }
        } else {
          if (posNewLine == -1) {
            nom = str.substring(posOID + oid.length() + 1, posComa);
          } else {
            nom = str.substring(posOID + oid.length() + 1, Math.min(posComa, posNewLine) );
          }
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
    }

    return nom;
    
  }
  */

  /**
   * 
   * @param cert
   * @return
   */
  public static String getSubjectCorrectName(X509Certificate cert) {
    
    final String subjectDNStr = cert.getSubjectDN().toString();
    
    String certName = getCN(subjectDNStr);
    
    // Parche pels certificat DNIe (eliminar FIRMA i AUTENTICACION)
    {
      final String[] dnie = { " (FIRMA)", " (AUTENTICACIÓN)" };
      for (String tipusDNIe : dnie) {
        int pos = certName.indexOf(tipusDNIe);
        if (pos != -1) {
          // Eliminar tipus
          certName = certName.replace(tipusDNIe, "");
          // Posar Nom davant
          pos = certName.lastIndexOf(',');
          if (pos != -1) {
            String nom = certName.substring(pos + 1).trim();
            String llinatges = certName.substring(0, pos).trim();
            return nom + " " + llinatges;
          }
        }
      }
    }
    

    try {
      // OID.1.3.6.1.4.1.5734.1.1=ANTONI, OID.1.3.6.1.4.1.5734.1.2=NADAL,
      // OID.1.3.6.1.4.1.5734.1.3=BENNASAR
      Map<String, String> values = getAlternativeNamesOfExtension(cert, SUBJECT_ALT_NAME_OID);
      
      String nom, llinatge1, llinatge2;
      
      if (!values.isEmpty() && ((nom = values.get("OID.1.3.6.1.4.1.5734.1.1")) != null)
          && ((llinatge1 = values.get("OID.1.3.6.1.4.1.5734.1.2")) != null)
          && ((llinatge2 = values.get("OID.1.3.6.1.4.1.5734.1.3")) != null)) {
        
        
        return nom + " " + llinatge1 + " " + llinatge2;
      }
      
    } catch(Exception e) {
      e.printStackTrace();
    }


    

    // Parche pels certificats FNMT que contenen la paraula NOMBRE al principi i
    // el NIF del Firmant
    if (certName.startsWith("NOMBRE ")) {
      certName = certName.substring("NOMBRE ".length());
    }
    int posNIF = certName.indexOf(" - NIF ");
    if (posNIF != -1) {
      certName = certName.substring(0, posNIF);
    }
    // Parche Certificats d'Administracio Pública de FNMT
    int posDNI = certName.indexOf(" - DNI ");
    if (posDNI != -1) {
      certName = certName.substring(0, posDNI);
    }
   
    // sn
    String llinatges = getRDNvalue("surname", subjectDNStr);

    if (llinatges != null && llinatges.trim().length() != 0) {

      // cercam nom
      String nom = getRDNvalue("givenname", subjectDNStr);

      if (nom == null || nom.trim().length() == 0) {
        nom = getRDNvalue("g", subjectDNStr);
      }

      if (nom != null && nom.trim().length() != 0) {
        String fullName = nom + " " + llinatges;
        
        if (fullName.length() >= certName.length()) {
          return fullName;
        }
      }

    }
    

    return certName;

  }

  /**
   * Obtiene el nombre com&uacute;n (Common Name, CN) del titular de un
   * certificado X.509. Si no se encuentra el CN, se devuelve la unidad
   * organizativa (Organization Unit, OU).
   * 
   * @param c
   *          Certificado X.509 del cual queremos obtener el nombre com&uacute;n
   * @return Nombre com&uacute;n (Common Name, CN) del titular de un certificado
   *         X.509
   */
  public static String getCN(final X509Certificate c) {
    if (c == null) {
      return null;
    }
    return getCN(c.getSubjectX500Principal().toString());
  }

  /**
   * Obtiene el nombre com&uacute;n (Common Name, CN) de un <i>Principal</i>
   * X.400. Si no se encuentra el CN, se devuelve la unidad organizativa
   * (Organization Unit, OU).
   * 
   * @param principal
   *          <i>Principal</i> del cual queremos obtener el nombre com&uacute;n
   * @return Nombre com&uacute;n (Common Name, CN) de un <i>Principal</i> X.400
   */
  public static String getCN(final String principal) {
    if (principal == null) {
      return null;
    }

    String rdn = getRDNvalue("cn", principal); //$NON-NLS-1$
    if (rdn == null) {
      rdn = getRDNvalue("ou", principal); //$NON-NLS-1$
    }

    if (rdn != null) {
      return rdn;
    }

    final int i = principal.indexOf('=');
    if (i != -1) {
      log.fine("No se ha podido obtener el Common Name ni la Organizational Unit, se devolvera el fragmento mas significativo"); //$NON-NLS-1$
      return getRDNvalue(principal.substring(0, i), principal);
    }

    log.fine("Principal no valido, se devolvera la entrada"); //$NON-NLS-1$
    return principal;
  }

  /**
   * Recupera el valor de un RDN de un principal. El valor de retorno no incluye
   * el nombre del RDN, el igual, ni las posibles comillas que envuelvan el
   * valor. La funci&oacute;n no es sensible a la capitalizaci&oacute;n del RDN.
   * Si no se encuentra, se devuelve {@code null}.
   * 
   * @param rdn
   *          RDN que deseamos encontrar.
   * @param principal
   *          Principal del que extraer el RDN
   * @return Valor del RDN indicado o {@code null} si no se encuentra.
   */
  private static String getRDNvalue(final String rdn, final String principal) {

    int offset1 = 0;
    while ((offset1 = principal.toLowerCase().indexOf(rdn.toLowerCase(), offset1)) != -1) {

      if (offset1 > 0 && principal.charAt(offset1 - 1) != ','
          && principal.charAt(offset1 - 1) != ' ') {
        offset1++;
        continue;
      }

      offset1 += rdn.length();
      while (offset1 < principal.length() && principal.charAt(offset1) == ' ') {
        offset1++;
      }

      if (offset1 >= principal.length()) {
        return null;
      }

      if (principal.charAt(offset1) != '=') {
        continue;
      }

      offset1++;
      while (offset1 < principal.length() && principal.charAt(offset1) == ' ') {
        offset1++;
      }

      if (offset1 >= principal.length()) {
        return ""; //$NON-NLS-1$
      }

      int offset2;
      if (principal.charAt(offset1) == ',') {
        return ""; //$NON-NLS-1$
      } else if (principal.charAt(offset1) == '"') {
        offset1++;
        if (offset1 >= principal.length()) {
          return ""; //$NON-NLS-1$
        }

        offset2 = principal.indexOf('"', offset1);
        if (offset2 == offset1) {
          return ""; //$NON-NLS-1$
        } else if (offset2 != -1) {
          return principal.substring(offset1, offset2);
        } else {
          return principal.substring(offset1);
        }
      } else {
        offset2 = principal.indexOf(',', offset1);
        if (offset2 != -1) {
          return principal.substring(offset1, offset2).trim();
        }
        return principal.substring(offset1).trim();
      }
    }

    return null;
  }
  
  
  
  /**
   * Get a certificate policy ID from a certificate policies extension
   *
   * @param cert certificate containing the extension
   * @return String with the certificate policy OID
   * @throws IOException if extension can not be parsed
   */
  //@SuppressWarnings("restriction")
  public static String getCertificatePolicyId(X509Certificate cert) throws Exception {

    String oid = null;
    byte[] extvalue = cert.getExtensionValue("2.5.29.32");
    if (extvalue != null) {

      DerValue val = new DerValue(extvalue);

      try {
        DerValue octed = new DerValue(val.getOctetString());
        DerValue[] secs = octed.getData().getSequence(0);

        String fulloid = secs[0].toString();

        if (fulloid != null && fulloid.startsWith("OID.")) {
          return fulloid.substring(4);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return oid;
  }
  
  
  
  

  
  private static final String SUBJECT_ALT_NAME_OID = "2.5.29.17";
  
  public static String getUnitatAdministrativa(X509Certificate cert) throws Exception {
    
    Map<String, String> map = getAlternativeNamesOfExtension(cert, SUBJECT_ALT_NAME_OID);
        
    return map.get("OID.2.16.724.1.3.5.3.2.10"); 
  }
  
  /**
   * 
   * @param cert
   * @return
   */
  public static String getCarrec(X509Certificate cert) throws Exception {
    Map<String, String> map = getAlternativeNamesOfExtension(cert, SUBJECT_ALT_NAME_OID);
    return map.get("OID.2.16.724.1.3.5.3.2.11");
  }
  
  
  
  public static String[] getEmpresaNIFNom(X509Certificate cert) throws Exception {
    Map<String, String> map = getAlternativeNamesOfExtension(cert, SUBJECT_ALT_NAME_OID);
    String nif = map.get("OID.1.3.6.1.4.1.5734.1.7");
    if (nif == null) {
      return null;
    }
    
    return new String[] { nif, map.get("OID.1.3.6.1.4.1.5734.1.6") };
  }
    
  
  /**
   * This static method is the default implementation of the
   * getSubjectAlternaitveNames method in X509Certificate. A
   * X509Certificate provider generally should overwrite this to
   * provide among other things caching for better performance.
   */
  public static Map<String, String> getAlternativeNamesOfExtension(X509Certificate cert,
      String oid) throws CertificateParsingException {
    try {
      
      Map<String, String> values = new HashMap<String, String>();

      byte[] ext = cert.getExtensionValue(oid);
      if (ext == null) {
        return values;
      }

      DerValue val = new DerValue(ext);
      byte[] data = val.getOctetString();

      SubjectAlternativeNameExtension subjectAltNameExt = new SubjectAlternativeNameExtension(
          Boolean.FALSE, data);

      GeneralNames names;
      Collection<TypeValue> col;
      try {
        names = (GeneralNames) subjectAltNameExt
            .get(SubjectAlternativeNameExtension.SUBJECT_NAME);
        col = makeAltNames(names);
      } catch (IOException ioe) {
        // should not occur
        col = Collections.<TypeValue> emptySet();
      }

     

      for (TypeValue typeValue : col) {

        if (typeValue.getType() == GeneralNameInterface.NAME_DIRECTORY) {

          List<Rdn> rdn = new LdapName(typeValue.getValue()).getRdns();
          for (Rdn rdn2 : rdn) {
            values.put(rdn2.getType(), rdn2.getValue().toString());
          }

        }

      }

      return values;

    } catch (Exception ioe) {
      CertificateParsingException cpe = new CertificateParsingException();
      cpe.initCause(ioe);
      throw cpe;
    }
  }


  
    

  
  /**
   * Converts a GeneralNames structure into an immutable Collection of
   * alternative names (subject or issuer) in the form required by
   * {@link #getSubjectAlternativeNames} or
   * {@link #getIssuerAlternativeNames}.
   *
   * @param names the GeneralNames to be converted
   * @return an immutable Collection of alternative names
   */
  private static Collection<TypeValue> makeAltNames( GeneralNames names) {

      if (names.isEmpty()) {
          return new ArrayList<TypeValue>();
      }
      List<TypeValue> newNames = new ArrayList<TypeValue>();
      for (GeneralName gname : names.names()) {
          int type;
          String value;
          GeneralNameInterface name = gname.getName();
          //List<Object> nameEntry = new ArrayList<Object>(2);
          //nameEntry.add(
          type= Integer.valueOf(name.getType());
          switch (name.getType()) {
          case GeneralNameInterface.NAME_RFC822:
              value = ((RFC822Name) name).getName();
              break;
          case GeneralNameInterface.NAME_DNS:
            value =((DNSName) name).getName();
              break;
          case GeneralNameInterface.NAME_DIRECTORY:
            value =((X500Name) name).getRFC1779Name();  //getRFC2253Name();
              break;
          case GeneralNameInterface.NAME_URI:
            value =((URIName) name).getName();
              break;
          case GeneralNameInterface.NAME_IP:
              try {
                value =((IPAddressName) name).getName();
              } catch (IOException ioe) {
                  // IPAddressName in cert is bogus
                  throw new RuntimeException("IPAddress cannot be parsed",
                      ioe);
              }
              break;
          case GeneralNameInterface.NAME_OID:
            value =((OIDName) name).getOID().toString();
              break;
          default:
              // add DER encoded form
            
             
            
              DerOutputStream derOut = new DerOutputStream();
              try {
                  name.encode(derOut);
              } catch (IOException ioe) {
                  // should not occur since name has already been decoded
                  // from cert (this would indicate a bug in our code)
                  throw new RuntimeException("name cannot be encoded", ioe);
              }
              value = Base64.encode(derOut.toByteArray());
              break;
          }
          newNames.add(new TypeValue(type, value));
      }
      return Collections.unmodifiableCollection(newNames);
  }


  
  
  
  private static class TypeValue {
    
    final int type;
    
    final String value;
    
    
    
      
    /**
       * @param type
       * @param value
       */
      public TypeValue(int type, String value) {
        super();
        this.type = type;
        this.value = value;
      }
  
  
    public int getType() {
      return type;
    }
  
    public String getValue() {
      return value;
    }


  }
  
  
  /**
   * This represents the Subject Alternative Name Extension.
   *
   * This extension, if present, allows the subject to specify multiple
   * alternative names.
   *
   * <p>Extensions are represented as a sequence of the extension identifier
   * (Object Identifier), a boolean flag stating whether the extension is to
   * be treated as being critical and the extension value itself (this is again
   * a DER encoding of the extension value).
   * <p>
   * The ASN.1 syntax for this is:
   * <pre>
   * SubjectAltName ::= GeneralNames
   * GeneralNames ::= SEQUENCE SIZE (1..MAX) OF GeneralName
   * </pre>
   * @author Amit Kapoor
   * @author Hemma Prafullchandra
   * @see Extension
   * @see CertAttrSet
   */
  private static class SubjectAlternativeNameExtension extends Extension
  implements CertAttrSet<String> {
      /**
       * Identifier for this attribute, to be used with the
       * get, set, delete methods of Certificate, x509 type.
       */
      //public static final String IDENT =
      //                     "x509.info.extensions.SubjectAlternativeName";
    
    
      /**
       * Attribute names.
       */
      public static final String NAME = "SubjectAlternativeName";
      public static final String SUBJECT_NAME = "subject_name";

      // private data members
      GeneralNames        names = null;

      // Encode this extension
      private void encodeThis() throws IOException {
          if (names == null || names.isEmpty()) {
              this.extensionValue = null;
              return;
          }
          DerOutputStream os = new DerOutputStream();
          names.encode(os);
          this.extensionValue = os.toByteArray();
      }

      /**
       * Create a SubjectAlternativeNameExtension with the passed GeneralNames.
       * The extension is marked non-critical.
       *
       * @param names the GeneralNames for the subject.
       * @exception IOException on error.
       */
      /*
      public SubjectAlternativeNameExtension(GeneralNames names)
      throws IOException {
          this(Boolean.FALSE, names);
      }
      */

      /**
       * Create a SubjectAlternativeNameExtension with the specified
       * criticality and GeneralNames.
       *
       * @param critical true if the extension is to be treated as critical.
       * @param names the GeneralNames for the subject.
       * @exception IOException on error.
       */
      /*
      public SubjectAlternativeNameExtension(Boolean critical, GeneralNames names)
      throws IOException {
          this.names = names;
          this.extensionId = PKIXExtensions.SubjectAlternativeName_Id;
          this.critical = critical.booleanValue();
          encodeThis();
      }
      */

      /**
       * Create a default SubjectAlternativeNameExtension. The extension
       * is marked non-critical.
       */
      /*
      public SubjectAlternativeNameExtension() {
          extensionId = PKIXExtensions.SubjectAlternativeName_Id;
          critical = false;
          names = new GeneralNames();
      }
      */

      /**
       * Create the extension from the passed DER encoded value.
       *
       * @param critical true if the extension is to be treated as critical.
       * @param value an array of DER encoded bytes of the actual value.
       * @exception ClassCastException if value is not an array of bytes
       * @exception IOException on error.
       */
      public SubjectAlternativeNameExtension(Boolean critical, Object value)
      throws IOException {
          this.extensionId = PKIXExtensions.SubjectAlternativeName_Id;
          this.critical = critical.booleanValue();

          this.extensionValue = (byte[]) value;
          DerValue val = new DerValue(this.extensionValue);
          if (val.data == null) {
              names = new GeneralNames();
              return;
          }

          names = new GeneralNames(val);
      }

      /**
       * Returns a printable representation of the SubjectAlternativeName.
       */
      public String toString() {

          String result = super.toString() + "SubjectAlternativeName [\n";
          if(names == null) {
              result += "  null\n";
          } else {
              for(GeneralName name: names.names()) {
                  result += "  "+name+"\n";
              }
          }
          result += "]\n";
          return result;
      }

      /**
       * Write the extension to the OutputStream.
       *
       * @param out the OutputStream to write the extension to.
       * @exception IOException on encoding errors.
       */
      public void encode(OutputStream out) throws IOException {
          DerOutputStream tmp = new DerOutputStream();
          if (extensionValue == null) {
              extensionId = PKIXExtensions.SubjectAlternativeName_Id;
              critical = false;
              encodeThis();
          }
          super.encode(tmp);
          out.write(tmp.toByteArray());
      }

      /**
       * Set the attribute value.
       */
      public void set(String name, Object obj) throws IOException {
          if (name.equalsIgnoreCase(SUBJECT_NAME)) {
              if (!(obj instanceof GeneralNames)) {
                throw new IOException("Attribute value should be of " +
                                      "type GeneralNames.");
              }
              names = (GeneralNames)obj;
          } else {
            throw new IOException("Attribute name not recognized by " +
                          "CertAttrSet:SubjectAlternativeName.");
          }
          encodeThis();
      }

      /**
       * Get the attribute value.
       */
      public Object get(String name) throws IOException {
          if (name.equalsIgnoreCase(SUBJECT_NAME)) {
              return (names);
          } else {
            throw new IOException("Attribute name not recognized by " +
                          "CertAttrSet:SubjectAlternativeName.");
          }
      }

      /**
       * Delete the attribute value.
       */
      public void delete(String name) throws IOException {
          if (name.equalsIgnoreCase(SUBJECT_NAME)) {
              names = null;
          } else {
            throw new IOException("Attribute name not recognized by " +
                          "CertAttrSet:SubjectAlternativeName.");
          }
          encodeThis();
      }

      /**
       * Return an enumeration of names of attributes existing within this
       * attribute.
       */
      public Enumeration<String> getElements() {
          AttributeNameEnumeration elements = new AttributeNameEnumeration();
          elements.addElement(SUBJECT_NAME);

          return (elements.elements());
      }

      /**
       * Return the name of this attribute.
       */
      public String getName() {
          return (NAME);
      }
  }
  
  

}
