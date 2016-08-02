
package es.caib.digital.ws.api.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for configuracionFirmaEntidad complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="configuracionFirmaEntidad">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.digital.caib.es/}baseVO">
 *       &lt;sequence>
 *         &lt;element name="alias" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="localizacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="passwordCertificado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="razonFirma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "configuracionFirmaEntidad", propOrder = {
    "alias",
    "localizacion",
    "passwordCertificado",
    "razonFirma"
})
public class ConfiguracionFirmaEntidad
    extends BaseVO
{

    protected String alias;
    protected String localizacion;
    protected String passwordCertificado;
    protected String razonFirma;

    /**
     * Gets the value of the alias property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the value of the alias property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlias(String value) {
        this.alias = value;
    }

    /**
     * Gets the value of the localizacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalizacion() {
        return localizacion;
    }

    /**
     * Sets the value of the localizacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalizacion(String value) {
        this.localizacion = value;
    }

    /**
     * Gets the value of the passwordCertificado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasswordCertificado() {
        return passwordCertificado;
    }

    /**
     * Sets the value of the passwordCertificado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasswordCertificado(String value) {
        this.passwordCertificado = value;
    }

    /**
     * Gets the value of the razonFirma property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazonFirma() {
        return razonFirma;
    }

    /**
     * Sets the value of the razonFirma property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazonFirma(String value) {
        this.razonFirma = value;
    }

}
