package org.fundaciobit.plugins.certificate.afirma;

import javax.xml.soap.SOAPMessage;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.SOAPPart;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.message.SOAPEnvelope;
import org.w3c.dom.Document;
/**
 * 
 * @author anadal
 *
 */
public abstract class ClientHandler extends BasicHandler {

  /**
   * 
   */
  private static final long serialVersionUID = 1660702682299084863L;


  public abstract SOAPMessage getWSSecurityToken(Document soapEnvelopeRequest) throws Exception;
  

    public void invoke(MessageContext msgContext) throws AxisFault {
      try {
        SOAPMessage msg = msgContext.getCurrentMessage();
        Document doc = ((SOAPEnvelope)msg.getSOAPPart().getEnvelope()).getAsDocument();
        SOAPMessage secMsg = getWSSecurityToken(doc);
        if (secMsg == null) {
          secMsg = msg;
        }
        ((SOAPPart)msgContext.getRequestMessage().getSOAPPart()).setCurrentMessage(
            secMsg.getSOAPPart().getEnvelope(),
            SOAPPart.FORM_SOAPENVELOPE);
      } catch (Exception ex) {
        throw new AxisFault("Error al incorporar les capçaleres WSS al missatge SOAP " + ex.getMessage(), ex);
      }
    }

  
}
