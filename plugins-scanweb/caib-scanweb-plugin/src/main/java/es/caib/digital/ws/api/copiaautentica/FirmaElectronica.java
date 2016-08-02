
package es.caib.digital.ws.api.copiaautentica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for firmaElectronica complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="firmaElectronica">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.digital.caib.es/}baseVO">
 *       &lt;sequence>
 *         &lt;element name="bytesFirma" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "firmaElectronica", propOrder = {
    "bytesFirma"
})
public class FirmaElectronica
    extends BaseVO
{

    protected byte[] bytesFirma;

    /**
     * Gets the value of the bytesFirma property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBytesFirma() {
        return bytesFirma;
    }

    /**
     * Sets the value of the bytesFirma property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBytesFirma(byte[] value) {
        this.bytesFirma = ((byte[]) value);
    }

}
