
package es.caib.digital.ws.api.copiaautentica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eniContenidoFirma complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="eniContenidoFirma">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.digital.caib.es/}eniBaseVO">
 *       &lt;sequence>
 *         &lt;element name="csv" type="{http://service.ws.digital.caib.es/}eniContenidoFirmaCSV" minOccurs="0"/>
 *         &lt;element name="firmaConCertificado" type="{http://service.ws.digital.caib.es/}eniFirmaConCertificado" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eniContenidoFirma", propOrder = {
    "csv",
    "firmaConCertificado"
})
public class EniContenidoFirma
    extends EniBaseVO
{

    protected EniContenidoFirmaCSV csv;
    protected EniFirmaConCertificado firmaConCertificado;

    /**
     * Gets the value of the csv property.
     * 
     * @return
     *     possible object is
     *     {@link EniContenidoFirmaCSV }
     *     
     */
    public EniContenidoFirmaCSV getCsv() {
        return csv;
    }

    /**
     * Sets the value of the csv property.
     * 
     * @param value
     *     allowed object is
     *     {@link EniContenidoFirmaCSV }
     *     
     */
    public void setCsv(EniContenidoFirmaCSV value) {
        this.csv = value;
    }

    /**
     * Gets the value of the firmaConCertificado property.
     * 
     * @return
     *     possible object is
     *     {@link EniFirmaConCertificado }
     *     
     */
    public EniFirmaConCertificado getFirmaConCertificado() {
        return firmaConCertificado;
    }

    /**
     * Sets the value of the firmaConCertificado property.
     * 
     * @param value
     *     allowed object is
     *     {@link EniFirmaConCertificado }
     *     
     */
    public void setFirmaConCertificado(EniFirmaConCertificado value) {
        this.firmaConCertificado = value;
    }

}
