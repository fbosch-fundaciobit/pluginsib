
package es.caib.digital.ws.api.copiaautentica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for informacionDocumento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="informacionDocumento">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.digital.caib.es/}baseVO">
 *       &lt;sequence>
 *         &lt;element name="idDocGestorDocumental" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idDocTemporal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idEntidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idTipoFormatoDocEntrada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="urlDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "informacionDocumento", propOrder = {
    "idDocGestorDocumental",
    "idDocTemporal",
    "idEntidad",
    "idTipoFormatoDocEntrada",
    "urlDocumento"
})
public class InformacionDocumento
    extends BaseVO
{

    protected String idDocGestorDocumental;
    protected String idDocTemporal;
    protected String idEntidad;
    protected String idTipoFormatoDocEntrada;
    protected String urlDocumento;

    /**
     * Gets the value of the idDocGestorDocumental property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDocGestorDocumental() {
        return idDocGestorDocumental;
    }

    /**
     * Sets the value of the idDocGestorDocumental property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDocGestorDocumental(String value) {
        this.idDocGestorDocumental = value;
    }

    /**
     * Gets the value of the idDocTemporal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDocTemporal() {
        return idDocTemporal;
    }

    /**
     * Sets the value of the idDocTemporal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDocTemporal(String value) {
        this.idDocTemporal = value;
    }

    /**
     * Gets the value of the idEntidad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdEntidad() {
        return idEntidad;
    }

    /**
     * Sets the value of the idEntidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdEntidad(String value) {
        this.idEntidad = value;
    }

    /**
     * Gets the value of the idTipoFormatoDocEntrada property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTipoFormatoDocEntrada() {
        return idTipoFormatoDocEntrada;
    }

    /**
     * Sets the value of the idTipoFormatoDocEntrada property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTipoFormatoDocEntrada(String value) {
        this.idTipoFormatoDocEntrada = value;
    }

    /**
     * Gets the value of the urlDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlDocumento() {
        return urlDocumento;
    }

    /**
     * Sets the value of the urlDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlDocumento(String value) {
        this.urlDocumento = value;
    }

}
