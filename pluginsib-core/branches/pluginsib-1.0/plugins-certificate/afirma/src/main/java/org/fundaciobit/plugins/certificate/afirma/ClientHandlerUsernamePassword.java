package org.fundaciobit.plugins.certificate.afirma;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis.soap.MessageFactoryImpl;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.WSSecUsernameToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author anadal
 *
 */
public class ClientHandlerUsernamePassword extends ClientHandler {

  /**
   * 
   */
  private static final long serialVersionUID = -2810301885896179645L;
  private final String username;
  private final String password;
  

  /**
   * @param username
   * @param password
   */
  public ClientHandlerUsernamePassword(String username, String password) {
    super();
    this.username = username;
    this.password = password;
  }





  @Override
  public SOAPMessage getWSSecurityToken(Document soapEnvelopeRequest) throws Exception {
    // Inserción del tag wsse:Security y userNameToken
    WSSecHeader wsSecHeader = new WSSecHeader(null,false);
    WSSecUsernameToken wsSecUsernameToken = new WSSecUsernameToken();
    wsSecUsernameToken.setPasswordType("PasswordDigest");
    wsSecUsernameToken.setUserInfo(username, password);
    wsSecHeader.insertSecurityHeader(soapEnvelopeRequest);
    wsSecUsernameToken.prepare(soapEnvelopeRequest);
    // Añadimos una marca de tiempo inidicando la fecha de creación del tag
    wsSecUsernameToken.addCreated();
    wsSecUsernameToken.addNonce();
    // Modificación de la petición
    Document secSOAPReqDoc = wsSecUsernameToken.build(soapEnvelopeRequest, wsSecHeader);
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

}
