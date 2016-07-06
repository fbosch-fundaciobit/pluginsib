
package es.caib.digital.ws.api.caratulas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for returnInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="returnInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="wsState" type="{http://service.ws.digital.caib.es/}wsState" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "returnInfo", propOrder = {
    "wsState"
})
@XmlSeeAlso({
    CaratulaReturnInfo.class
})
public class ReturnInfo {

    protected WsState wsState;

    /**
     * Gets the value of the wsState property.
     * 
     * @return
     *     possible object is
     *     {@link WsState }
     *     
     */
    public WsState getWsState() {
        return wsState;
    }

    /**
     * Sets the value of the wsState property.
     * 
     * @param value
     *     allowed object is
     *     {@link WsState }
     *     
     */
    public void setWsState(WsState value) {
        this.wsState = value;
    }

}
