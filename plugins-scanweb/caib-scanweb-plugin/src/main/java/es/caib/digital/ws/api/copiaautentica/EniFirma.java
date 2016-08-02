
package es.caib.digital.ws.api.copiaautentica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eniFirma complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="eniFirma">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.digital.caib.es/}eniBaseVO">
 *       &lt;sequence>
 *         &lt;element name="contenidoFirma" type="{http://service.ws.digital.caib.es/}eniContenidoFirma" minOccurs="0"/>
 *         &lt;element name="ref" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoFirma" type="{http://service.ws.digital.caib.es/}eniEnumTipoFirma" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eniFirma", propOrder = {
    "contenidoFirma",
    "ref",
    "tipoFirma"
})
@XmlSeeAlso({
    MetadatosFirmaElectronica.class
})
public class EniFirma
    extends EniBaseVO
{

    protected EniContenidoFirma contenidoFirma;
    protected String ref;
    protected EniEnumTipoFirma tipoFirma;

    /**
     * Gets the value of the contenidoFirma property.
     * 
     * @return
     *     possible object is
     *     {@link EniContenidoFirma }
     *     
     */
    public EniContenidoFirma getContenidoFirma() {
        return contenidoFirma;
    }

    /**
     * Sets the value of the contenidoFirma property.
     * 
     * @param value
     *     allowed object is
     *     {@link EniContenidoFirma }
     *     
     */
    public void setContenidoFirma(EniContenidoFirma value) {
        this.contenidoFirma = value;
    }

    /**
     * Gets the value of the ref property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRef(String value) {
        this.ref = value;
    }

    /**
     * Gets the value of the tipoFirma property.
     * 
     * @return
     *     possible object is
     *     {@link EniEnumTipoFirma }
     *     
     */
    public EniEnumTipoFirma getTipoFirma() {
        return tipoFirma;
    }

    /**
     * Sets the value of the tipoFirma property.
     * 
     * @param value
     *     allowed object is
     *     {@link EniEnumTipoFirma }
     *     
     */
    public void setTipoFirma(EniEnumTipoFirma value) {
        this.tipoFirma = value;
    }

}
