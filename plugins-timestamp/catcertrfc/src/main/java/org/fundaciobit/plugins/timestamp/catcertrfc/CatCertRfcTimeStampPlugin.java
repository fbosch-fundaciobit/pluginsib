package org.fundaciobit.plugins.timestamp.catcertrfc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Properties;


import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.tsp.TSPAlgorithms;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.tsp.TimeStampToken;
import org.fundaciobit.plugins.timestamp.api.ITimeStampPlugin;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

import sun.misc.BASE64Decoder;

/**
 * CatCert
 * 
 * @author anadal
 *
 */
@SuppressWarnings("restriction")
public class CatCertRfcTimeStampPlugin extends AbstractPluginProperties implements
    ITimeStampPlugin {

  protected final Logger log = Logger.getLogger(getClass());

  public static final String CATCERTRFC_BASE_PROPERTIES = TIMESTAMP_BASE_PROPERTY
      + "catcertrfc.";

  public static final String URL = CATCERTRFC_BASE_PROPERTIES + "url";

  /**
   * @param propertyKeyBase
   * @param properties
   */
  public CatCertRfcTimeStampPlugin(String propertyKeyBase, Properties properties) {
    super(propertyKeyBase, properties);

  }

  /**
   * @param propertyKeyBase
   */
  public CatCertRfcTimeStampPlugin(String propertyKeyBase) {
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

      return requestTimeStampHTTPSDirect(datos).respBytes;

    } catch (ConnectException cExc) {
      Exception ce = new Exception(
          "Error de conexion. Compruebe el Host establecido o que el servicio este habilitado");
      throw ce;
    } catch (IOException ioExc) {

      if (ioExc.getMessage().contains("402")) {
        throw new Exception("Error en el proceso de autenticación: " + ioExc.getMessage(),
            ioExc);
      }
      if ((ioExc.getMessage().contains("401"))
          || (ioExc.getMessage().contains("handshake_failure"))) {
        throw new Exception("Certificado no válido");
      }
      if (ioExc.getMessage().contains("400")) {
        throw new Exception("Petición Incorrecta");
      }
      throw new Exception("Error de Conexion desconocido:" + ioExc.getMessage(), ioExc);
    }
  }

  public TimeStampToken getTimeStamp(byte[] datos) throws Exception {

    SHA1Digest digest = new SHA1Digest();
    digest.update(datos, 0, datos.length);
    byte[] digestValue = new byte[digest.getDigestSize()];
    digest.doFinal(digestValue, 0);

    TimeStampRequestGenerator reqgen = new TimeStampRequestGenerator();
    reqgen.setCertReq(true);

    TimeStampRequest req = reqgen.generate(TSPAlgorithms.SHA1, digestValue);
    byte[] request = req.getEncoded();

    TimeStampDirectInfo info = requestTimeStampHTTPSDirect(request);

    byte[] respBytes = info.respBytes;

    String encoding = info.encoding;
    if ((encoding != null) && (encoding.equalsIgnoreCase("base64"))) {
      BASE64Decoder dec = new BASE64Decoder();
      respBytes = dec.decodeBuffer(new String(respBytes));
    }
    return componerSalida(respBytes);
  }

  private TimeStampToken componerSalida(byte[] res) throws TSPException {

    try {
      ASN1InputStream in = null;

      in = new ASN1InputStream(res);

      DERSequence asn = (DERSequence) in.readObject();

      DERSequence info = (DERSequence) asn.getObjectAt(0);
      DERInteger status = (DERInteger) info.getObjectAt(0);
      if ((status.getValue().intValue() != 0) && (status.getValue().intValue() != 1)) {
        String tspExMsg = "Timestamp server error. ";
        if ((DERSequence) info.getObjectAt(1) != null) {
          tspExMsg = tspExMsg + new String(((DERSequence) info.getObjectAt(1)).getEncoded());
        }
        in.close();
        throw new TSPException(tspExMsg);
      }
      in.close();
      try {
        return new TimeStampToken(new CMSSignedData(asn.getObjectAt(1).getDERObject()
            .getDEREncoded()));
      } catch (TSPException e) {
        throw e;
      } catch (Exception e) {
        throw new TSPException(e.getMessage(), e);
      }

    } catch (IOException e) {
      throw new TSPException("Error parseando respuesta:" + e.getMessage(), e);
    }

  }

  public TimeStampDirectInfo requestTimeStampHTTPSDirect(byte[] request) throws TSPException,
      IOException, Exception {

    String tsaURL = getProperty(URL);

    URL url = new URL(tsaURL);

    HttpURLConnection tsaConnection = (HttpURLConnection) url.openConnection();
    
    // Si locTrust es null llavors Acceptar qualsevol certificat
    // cridant a utilitat de pluginsIB

    tsaConnection.setDoInput(true);
    tsaConnection.setDoOutput(true);
    tsaConnection.setUseCaches(false);
    tsaConnection.setRequestProperty("Content-Type", "application/timestamp-query");
    tsaConnection.setAllowUserInteraction(false);
    tsaConnection.setRequestProperty("Content-Transfer-Encoding", "binary");

    OutputStream out = tsaConnection.getOutputStream();
    out.write(request);
    out.close();

    InputStream inp = tsaConnection.getInputStream();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int bytesRead = 0;
    while ((bytesRead = inp.read(buffer, 0, buffer.length)) >= 0) {
      baos.write(buffer, 0, bytesRead);
    }
    byte[] respBytes = baos.toByteArray();

    String encoding = tsaConnection.getContentEncoding();

    return new TimeStampDirectInfo(respBytes, encoding);

  }

  public class TimeStampDirectInfo {

    public final byte[] respBytes;

    public final String encoding;

    /**
     * @param respBytes
     * @param encoding
     */
    public TimeStampDirectInfo(byte[] respBytes, String encoding) {
      super();
      this.respBytes = respBytes;
      this.encoding = encoding;
    }

  }

 

}
