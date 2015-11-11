package org.fundaciobit.plugins.timestamp.afirmarfc;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampToken;
import org.fundaciobit.plugins.timestamp.api.ITimeStampPlugin;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

/**
 * 
 * @author anadal
 *
 */
public class AfirmaRFCTimeStampPlugin extends AbstractPluginProperties implements
    ITimeStampPlugin {

  protected final Logger log = Logger.getLogger(getClass());

  public static final String AFIRMARFC_BASE_PROPERTIES = TIMESTAMP_BASE_PROPERTY
      + "afirmarfc.";

  public static final String OID_RFC3161 = AFIRMARFC_BASE_PROPERTIES + "oid_rfc3161";
  public static final String APPLICATION_ID = AFIRMARFC_BASE_PROPERTIES + "applicationid";

  public static final String URL_RFC = AFIRMARFC_BASE_PROPERTIES + "url_rfc";

  // String locCert = rs.getString("https.autenticacion.location.cert");
  public static final String AUTH_CERT_PATH = AFIRMARFC_BASE_PROPERTIES + "auth.cert.p12.path";

  // String passCert = rs.getString("https.autenticacion.password.cert");
  public static final String AUTH_CERT_PASSWORD = AFIRMARFC_BASE_PROPERTIES
      + "auth.cert.p12.password";

  // Opcional
  // String locTrust = rs.getString("location.trustkeystore");
  public static final String SERVER_TRUSTKEYSTORE_PATH = AFIRMARFC_BASE_PROPERTIES
      + "server.trustkeystore.path";

  // String passTrust = rs.getString("password.trustkeystore");
  public static final String SERVER_TRUSTKEYSTORE_PASSWORD = AFIRMARFC_BASE_PROPERTIES
      + "server.trustkeystore.password";

  /**
   * 
   */
  public AfirmaRFCTimeStampPlugin() {
    super();
  }

  /**
   * @param propertyKeyBase
   * @param properties
   */
  public AfirmaRFCTimeStampPlugin(String propertyKeyBase, Properties properties) {
    super(propertyKeyBase, properties);
  }

  /**
   * @param propertyKeyBase
   */
  public AfirmaRFCTimeStampPlugin(String propertyKeyBase) {
    super(propertyKeyBase);
  }

  
  public byte[] getTimeStampDirect(byte[] fichero) throws Exception {
    try {
      if (fichero == null) {
        return null;
      }
      byte[] datos = null;
      datos = new byte[fichero.length];
      datos = (byte[]) fichero.clone();

      TimeStampService tss = getTimeStampService();

      return tss.requestTimeStampHTTPSDirect(datos).respBytes;

    } catch (ConnectException cExc) {
      ConexionException ce = new ConexionException(
          "Error de conexion. Compruebe el Host establecido o que el servicio este habilitado");
      throw ce;
    } catch (TSPException tspException) {
      PeticionException pExc = new PeticionException(tspException.getMessage());
      throw pExc;
    } catch (IOException ioExc) {
      AutenticacionException autExc = null;
      if (ioExc.getMessage().contains("402")) {
        autExc = new AutenticacionException("Error en el proceso de autenticación");
        throw autExc;
      }
      if ((ioExc.getMessage().contains("401"))
          || (ioExc.getMessage().contains("handshake_failure"))) {
        autExc = new AutenticacionException("Certificado no válido");
        throw autExc;
      }
      if (ioExc.getMessage().contains("400")) {
        PeticionException pExc = new PeticionException("Petición Incorrecta");
        throw pExc;
      }
      throw new ConexionException("Error de Conexion desconocido:" + ioExc.getMessage(), ioExc);
    }

  }
  
  
  /**
   * 
   */
  public TimeStampToken getTimeStamp(byte[] fichero) throws Exception {

   
    try {
      if (fichero == null) {
        return null;
      }
      byte[] datos = null;
      datos = new byte[fichero.length];
      datos = (byte[]) fichero.clone();

      TimeStampService tss = getTimeStampService();

      return tss.requestTimeStampHTTPS(datos);

    } catch (ConnectException cExc) {
      ConexionException ce = new ConexionException(
          "Error de conexion. Compruebe el Host establecido o que el servicio este habilitado");
      throw ce;
    } catch (TSPException tspException) {
      PeticionException pExc = new PeticionException(tspException.getMessage());
      throw pExc;
    } catch (IOException ioExc) {
      AutenticacionException autExc = null;
      if (ioExc.getMessage().contains("402")) {
        autExc = new AutenticacionException("Error en el proceso de autenticación");
        throw autExc;
      }
      if ((ioExc.getMessage().contains("401"))
          || (ioExc.getMessage().contains("handshake_failure"))) {
        autExc = new AutenticacionException("Certificado no válido");
        throw autExc;
      }
      if (ioExc.getMessage().contains("400")) {
        PeticionException pExc = new PeticionException("Petición Incorrecta");
        throw pExc;
      }
      throw new ConexionException("Error de Conexion desconocido:" + ioExc.getMessage(), ioExc);
    }

  }

  private TimeStampService getTimeStampService() {
    String oid = getProperty(OID_RFC3161);
    

    String appID = null;
    appID = getProperty(APPLICATION_ID);
    

    String tsaURL = getProperty(URL_RFC);


    String locCert = getProperty(AUTH_CERT_PATH);
    String passCert = getProperty(AUTH_CERT_PASSWORD);


    String locTrust = getProperty(SERVER_TRUSTKEYSTORE_PATH);
    String passTrust = getProperty(SERVER_TRUSTKEYSTORE_PASSWORD);

    if (log.isDebugEnabled()) {
      log.debug("OID = " + oid);
      log.debug("APPLICATION_ID = " + appID);
      log.debug("tsaURL = " + tsaURL);
      log.debug("locCert = " + locCert);
      log.debug("passCert = " + passCert);
      log.debug("locTrust = " + locTrust);
      log.debug("passTrust = " + passTrust);
    }

    TimeStampService tss;
    if (locTrust == null && passTrust != null) {
      tss = new TimeStampService(tsaURL, appID, locCert, passCert, oid);
    } else {
      tss = new TimeStampService(tsaURL, appID, locCert, passCert, oid, locTrust, passTrust);
    }
    return tss;
  }


}
