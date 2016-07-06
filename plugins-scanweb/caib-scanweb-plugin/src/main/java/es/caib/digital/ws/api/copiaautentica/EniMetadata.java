
package es.caib.digital.ws.api.copiaautentica;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for eniMetadata complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="eniMetadata">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.digital.caib.es/}eniBaseVO">
 *       &lt;sequence>
 *         &lt;element name="estadoElaboracion" type="{http://service.ws.digital.caib.es/}eniEstadoElaboracion" minOccurs="0"/>
 *         &lt;element name="fechaCaptura" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="identificador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="organo" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="origenCiudadanoAdministracion" type="{http://service.ws.digital.caib.es/}eniEnumOrigenCreacion" minOccurs="0"/>
 *         &lt;element name="tipoDocumental" type="{http://service.ws.digital.caib.es/}eniEnumTipoDocumental" minOccurs="0"/>
 *         &lt;element name="versionNTI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eniMetadata", propOrder = {
    "estadoElaboracion",
    "fechaCaptura",
    "identificador",
    "organo",
    "origenCiudadanoAdministracion",
    "tipoDocumental",
    "versionNTI"
})
public class EniMetadata
    extends EniBaseVO
{

    protected EniEstadoElaboracion estadoElaboracion;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Timestamp fechaCaptura;
    protected String identificador;
    @XmlElement(nillable = true)
    protected List<String> organo;
    protected EniEnumOrigenCreacion origenCiudadanoAdministracion;
    protected EniEnumTipoDocumental tipoDocumental;
    protected String versionNTI;

    /**
     * Gets the value of the estadoElaboracion property.
     * 
     * @return
     *     possible object is
     *     {@link EniEstadoElaboracion }
     *     
     */
    public EniEstadoElaboracion getEstadoElaboracion() {
        return estadoElaboracion;
    }

    /**
     * Sets the value of the estadoElaboracion property.
     * 
     * @param value
     *     allowed object is
     *     {@link EniEstadoElaboracion }
     *     
     */
    public void setEstadoElaboracion(EniEstadoElaboracion value) {
        this.estadoElaboracion = value;
    }

    /**
     * Gets the value of the fechaCaptura property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Timestamp getFechaCaptura() {
        return fechaCaptura;
    }

    /**
     * Sets the value of the fechaCaptura property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaCaptura(Timestamp value) {
        this.fechaCaptura = value;
    }

    /**
     * Gets the value of the identificador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Sets the value of the identificador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificador(String value) {
        this.identificador = value;
    }

    /**
     * Gets the value of the organo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the organo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrgano().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOrgano() {
        if (organo == null) {
            organo = new ArrayList<String>();
        }
        return this.organo;
    }

    /**
     * Gets the value of the origenCiudadanoAdministracion property.
     * 
     * @return
     *     possible object is
     *     {@link EniEnumOrigenCreacion }
     *     
     */
    public EniEnumOrigenCreacion getOrigenCiudadanoAdministracion() {
        return origenCiudadanoAdministracion;
    }

    /**
     * Sets the value of the origenCiudadanoAdministracion property.
     * 
     * @param value
     *     allowed object is
     *     {@link EniEnumOrigenCreacion }
     *     
     */
    public void setOrigenCiudadanoAdministracion(EniEnumOrigenCreacion value) {
        this.origenCiudadanoAdministracion = value;
    }

    /**
     * Gets the value of the tipoDocumental property.
     * 
     * @return
     *     possible object is
     *     {@link EniEnumTipoDocumental }
     *     
     */
    public EniEnumTipoDocumental getTipoDocumental() {
        return tipoDocumental;
    }

    /**
     * Sets the value of the tipoDocumental property.
     * 
     * @param value
     *     allowed object is
     *     {@link EniEnumTipoDocumental }
     *     
     */
    public void setTipoDocumental(EniEnumTipoDocumental value) {
        this.tipoDocumental = value;
    }

    /**
     * Gets the value of the versionNTI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionNTI() {
        return versionNTI;
    }

    /**
     * Sets the value of the versionNTI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionNTI(String value) {
        this.versionNTI = value;
    }

}
