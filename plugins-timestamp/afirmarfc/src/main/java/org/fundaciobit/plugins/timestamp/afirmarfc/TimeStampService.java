package org.fundaciobit.plugins.timestamp.afirmarfc;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.tsp.TSPAlgorithms;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.tsp.TimeStampToken;
import org.fundaciobit.plugins.utils.XTrustProvider;

import sun.misc.BASE64Decoder;

/**
 * 
 * @author anadal
 *
 */
@SuppressWarnings("restriction")
public class TimeStampService {

  private final String tsaURL;

  private final String appID;

  private final String locCert;
  private final String passCert;

  private final String m_sPolicyOID;

  private final String locTrust;
  private final String passTrust;

  /**
   * @param tsaURL
   * @param appID
   * @param locCert
   * @param passCert
   * @param m_sPolicyOID
   * @param locTrust
   * @param passTrust
   */
  public TimeStampService(String tsaURL, String appID, String locCert, String passCert,
      String m_sPolicyOID) {
    this(tsaURL, appID, locCert, passCert, m_sPolicyOID, null, null);
  }

  /**
   * @param tsaURL
   * @param appID
   * @param locCert
   * @param passCert
   * @param m_sPolicyOID
   * @param locTrust
   * @param passTrust
   */
  public TimeStampService(String tsaURL, String appID, String locCert, String passCert,
      String m_sPolicyOID, String locTrust, String passTrust) {
    super();
    this.tsaURL = tsaURL;
    this.appID = appID;
    this.locCert = locCert;
    this.passCert = passCert;
    this.m_sPolicyOID = m_sPolicyOID;
    this.locTrust = locTrust;
    this.passTrust = passTrust;
  }

 
  public TimeStampToken requestTimeStampHTTPS(byte[] datos) throws TSPException, IOException,
      AutenticacionException, ConexionException {



    SHA1Digest digest = new SHA1Digest();
    digest.update(datos, 0, datos.length);
    byte[] digestValue = new byte[digest.getDigestSize()];
    digest.doFinal(digestValue, 0);

    TimeStampRequestGenerator reqgen = new TimeStampRequestGenerator();
    reqgen.setCertReq(true);
    
    if ((appID != null) && (!"".equals(appID))) {
      final String oid = "1.3.4.6.1.3.4.6";
      reqgen.addExtension(new ASN1ObjectIdentifier(oid), false,
          new DEROctetString(appID.getBytes()));
    }
    if ((this.m_sPolicyOID != null) && (!this.m_sPolicyOID.equals(""))) {
      reqgen.setReqPolicy(this.m_sPolicyOID);
    }
    
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

    } catch(IOException e) {
      throw new TSPException("Error parseando respuesta:" + e.getMessage(), e);
    }
   
    
  }
  
  
  
  public TimeStampDirectInfo requestTimeStampHTTPSDirect(byte[] request) throws TSPException,
     IOException, AutenticacionException, ConexionException { 
   

    

    URL url = new URL(tsaURL);

    HttpsURLConnection tsaConnection = (HttpsURLConnection) url.openConnection();
    tsaConnection.setHostnameVerifier(new NameVerifier());

    // Si locTrust es null llavors Acceptar qualsevol certificat
    // cridant a utilitat de pluginsIB
    try {

      

        SSLContext ctx = SSLContext.getInstance("SSL");
        
        
        KeyStore ks = KeyStore.getInstance("PKCS12");

        TrustManagerFactory tmf;
        
        if (locTrust != null && passTrust != null) {
          KeyStore cer = KeyStore.getInstance("JKS");
          cer.load(new FileInputStream(locTrust), passTrust.toCharArray());
          tmf = TrustManagerFactory.getInstance("SunX509");
          tmf.init(cer);
        } else {
          tmf = TrustManagerFactory.getInstance(XTrustProvider.install());
        }
        // if ("SI".equals(autenticacionCliente))
        {

          InputStream inKS = new FileInputStream(locCert);
          char[] passphrase = passCert.toCharArray();
          ks.load(inKS, passCert.toCharArray());
          KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
          kmf.init(ks, passphrase);
          ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        }
        // else if ("NO".equals(autenticacionCliente))
        // {
        // ctx.init(null, tmf.getTrustManagers(), null);
        // }
        // else
        // {
        // throw new
        // AutenticacionException("Valor incorrecto para la propiedad: https.autenticacion.cliente");
        // }
        SSLSocketFactory factory = ctx.getSocketFactory();

        tsaConnection.setSSLSocketFactory(factory);
      
    } catch (Exception e) {
      throw new ConexionException(
          "Error estableciendo los paramentros de comunicacion SSL-ClientCert: "
      + e.getMessage(), e);
    }

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
  

  class NameVerifier implements HostnameVerifier {
    NameVerifier() {
    }

    public boolean verify(String hostname, SSLSession session) {
      boolean result = false;
      try {
        ResourceBundle rs = ResourceBundle.getBundle("configuracionCliente");
        String rutaCert = rs.getString("location.trustkeystore");
        InputStream inKS = new FileInputStream(rutaCert);
        KeyStore keyStore = KeyStore.getInstance("JKS");
        String pass = rs.getString("password.trustkeystore");
        keyStore.load(inKS, pass.toCharArray());

        Enumeration<String> aliases = keyStore.aliases();

        Certificate[] certs = session.getPeerCertificates();
        for (int i = 0; i < certs.length; i++) {
          if ((certs[i] instanceof X509Certificate)) {
            while (aliases.hasMoreElements()) {
              String a = (String) aliases.nextElement();
              Certificate certificadoConfianza = keyStore.getCertificate(a);

              X509Certificate c = (X509Certificate) certs[i];
              if (certificadoConfianza.equals(c)) {
                return true;
              }
            }
          }
        }
      } catch (Exception e) {
        result = false;
      }
      return result;
    }
  }

  public byte[] invocarTsa(byte[] request) throws TSPException, IOException,
      AutenticacionException {
    ResourceBundle rs = ResourceBundle.getBundle("configuracionCliente");
    String tsaURL = rs.getString("url.https");

    String autenticacionCliente = rs.getString("https.autenticacion.cliente");

    URL url = new URL(tsaURL);

    HttpsURLConnection tsaConnection = (HttpsURLConnection) url.openConnection();

    tsaConnection.setHostnameVerifier(new NameVerifier());

    String locTrust = rs.getString("location.trustkeystore");
    String passTrust = rs.getString("password.trustkeystore");
    try {
      SSLContext ctx = SSLContext.getInstance("SSL");
      KeyStore ks = KeyStore.getInstance("PKCS12");

      KeyStore cer = KeyStore.getInstance("JKS");
      cer.load(new FileInputStream(locTrust), passTrust.toCharArray());

      TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
      tmf.init(cer);
      if ("SI".equals(autenticacionCliente)) {
        String locCert = rs.getString("https.autenticacion.location.cert");
        String passCert = rs.getString("https.autenticacion.password.cert");
        InputStream inKS = new FileInputStream(locCert);
        char[] passphrase = passCert.toCharArray();
        ks.load(inKS, passCert.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);

        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
      } else if ("NO".equals(autenticacionCliente)) {
        ctx.init(null, tmf.getTrustManagers(), null);
      } else {
        throw new AutenticacionException(
            "Valor incorrecto para la propiedad: https.autenticacion.cliente");
      }
      SSLSocketFactory factory = ctx.getSocketFactory();

      tsaConnection.setSSLSocketFactory(factory);
    } catch (Exception e) {
      e.printStackTrace();
    }
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
    if ((encoding != null) && (encoding.equalsIgnoreCase("base64"))) {
      BASE64Decoder dec = new BASE64Decoder();
      respBytes = dec.decodeBuffer(new String(respBytes));
    }
    return respBytes;
  }
}
