
package es.caib.digital.ws.api.caratulas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for caratulaReturnInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="caratulaReturnInfo">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.digital.caib.es/}returnInfo">
 *       &lt;sequence>
 *         &lt;element name="caratula" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="csv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "caratulaReturnInfo", propOrder = {
    "caratula",
    "csv"
})
public class CaratulaReturnInfo
    extends ReturnInfo
{

    protected byte[] caratula;
    protected String csv;

    /**
     * Gets the value of the caratula property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getCaratula() {
        return caratula;
    }

    /**
     * Sets the value of the caratula property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setCaratula(byte[] value) {
        this.caratula = ((byte[]) value);
    }

    /**
     * Gets the value of the csv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCsv() {
        return csv;
    }

    /**
     * Sets the value of the csv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCsv(String value) {
        this.csv = value;
    }

}
