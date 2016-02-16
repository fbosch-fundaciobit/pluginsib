package org.fundaciobit.plugins.certificate.afirma;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Properties;

import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis.soap.MessageFactoryImpl;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.WSSecSignature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author anadal
 *
 */
public class ClientHandlerCertificate extends ClientHandler {


  /**
   * 
   */
  private static final long serialVersionUID = 3883745902598584073L;


  private String keystoreLocation;
  private String keystoreType;
  private String keystorePassword;
  private String keystoreCertAlias;
  private String keystoreCertPassword;


  public ClientHandlerCertificate(
      String keystoreLocation,
      String keystoreType,
      String keystorePassword,
      String keystoreCertAlias,
      String keystoreCertPassword) {
    this.keystoreLocation = keystoreLocation;
    this.keystoreType = keystoreType;
    this.keystorePassword = keystorePassword;
    this.keystoreCertAlias = keystoreCertAlias;
    this.keystoreCertPassword = keystoreCertPassword;
  }

  
  

  @Override
  public SOAPMessage getWSSecurityToken(Document soapEnvelopeRequest) throws Exception {
    // Inserción del tag wsse:Security y BinarySecurityToken
    WSSecHeader wsSecHeader = new WSSecHeader(null, false);
    WSSecSignature wsSecSignature = new WSSecSignature();
    @SuppressWarnings("deprecation")
    Crypto crypto = CryptoFactory.getInstance(
        "org.apache.ws.security.components.crypto.Merlin",
        initializateCryptoProperties());
    // Indicación para que inserte el tag BinarySecurityToken
    wsSecSignature.setKeyIdentifierType(WSConstants.BST_DIRECT_REFERENCE);
    wsSecSignature.setUserInfo(keystoreCertAlias, keystoreCertPassword);
    wsSecHeader.insertSecurityHeader(soapEnvelopeRequest);
    wsSecSignature.prepare(soapEnvelopeRequest,crypto,wsSecHeader);
    // Modificación y firma de la petición
    Document secSOAPReqDoc = wsSecSignature.build(soapEnvelopeRequest,crypto,wsSecHeader);
    Element element = secSOAPReqDoc.getDocumentElement();
    // Transformación del elemento DOM a String
    DOMSource source = new DOMSource(element);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    StreamResult streamResult = new StreamResult(baos);
    TransformerFactory.newInstance().newTransformer().transform(source, streamResult);
    // Creación de un nuevo mensaje SOAP a partir del mensaje SOAP securizado formado
    String secSOAPReq = new String(baos.toByteArray());
    SOAPMessage res = new MessageFactoryImpl().createMessage(
        null,
        new ByteArrayInputStream(secSOAPReq.getBytes()));
    return res;
  }
  
  


  private Properties initializateCryptoProperties() {
    Properties props = new Properties();
    props.setProperty(
        "org.apache.ws.security.crypto.provider",
        "org.apache.ws.security.components.crypto.Merlin");
    props.setProperty(
        "org.apache.ws.security.crypto.merlin.keystore.type",
        keystoreType);
    props.setProperty(
        "org.apache.ws.security.crypto.merlin.keystore.password",
        keystorePassword);
    props.setProperty(
        "org.apache.ws.security.crypto.merlin.keystore.alias",
        keystoreCertAlias);
    props.setProperty(
        "org.apache.ws.security.crypto.merlin.alias.password",
        keystoreCertPassword);
    props.setProperty(
        "org.apache.ws.security.crypto.merlin.file",
        keystoreLocation);
    return props;
  }

  
}
