
package es.caib.digital.ws.api.copiaautentica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eniContenidoFirmaCSV complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="eniContenidoFirmaCSV">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.digital.caib.es/}eniBaseVO">
 *       &lt;sequence>
 *         &lt;element name="regulacionGeneracionCSV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valorCSV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eniContenidoFirmaCSV", propOrder = {
    "regulacionGeneracionCSV",
    "valorCSV"
})
public class EniContenidoFirmaCSV
    extends EniBaseVO
{

    protected String regulacionGeneracionCSV;
    protected String valorCSV;

    /**
     * Gets the value of the regulacionGeneracionCSV property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegulacionGeneracionCSV() {
        return regulacionGeneracionCSV;
    }

    /**
     * Sets the value of the regulacionGeneracionCSV property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegulacionGeneracionCSV(String value) {
        this.regulacionGeneracionCSV = value;
    }

    /**
     * Gets the value of the valorCSV property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValorCSV() {
        return valorCSV;
    }

    /**
     * Sets the value of the valorCSV property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValorCSV(String value) {
        this.valorCSV = value;
    }

}
