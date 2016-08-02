
package es.caib.digital.ws.api.entidades;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for entidad complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="entidad">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.digital.caib.es/}baseVO">
 *       &lt;sequence>
 *         &lt;element name="codigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoOrgano" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="configuracionFirmaEntidad" type="{http://service.ws.digital.caib.es/}configuracionFirmaEntidad" minOccurs="0"/>
 *         &lt;element name="configuracionPlantillaEntidad" type="{http://service.ws.digital.caib.es/}configuracionPlantillaEntidad" minOccurs="0"/>
 *         &lt;element name="esDefault" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaAlta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="metaDatos" type="{http://service.ws.digital.caib.es/}metaDato" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderBy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoIntegracion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "entidad", propOrder = {
    "codigo",
    "codigoOrgano",
    "configuracionFirmaEntidad",
    "configuracionPlantillaEntidad",
    "esDefault",
    "fechaAlta",
    "id",
    "metaDatos",
    "nombre",
    "orderBy",
    "tipoIntegracion"
})
public class Entidad
    extends BaseVO
{

    protected String codigo;
    protected String codigoOrgano;
    protected ConfiguracionFirmaEntidad configuracionFirmaEntidad;
    protected ConfiguracionPlantillaEntidad configuracionPlantillaEntidad;
    protected String esDefault;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaAlta;
    protected String id;
    @XmlElement(nillable = true)
    protected List<MetaDato> metaDatos;
    protected String nombre;
    protected String orderBy;
    protected String tipoIntegracion;

    /**
     * Gets the value of the codigo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Sets the value of the codigo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigo(String value) {
        this.codigo = value;
    }

    /**
     * Gets the value of the codigoOrgano property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoOrgano() {
        return codigoOrgano;
    }

    /**
     * Sets the value of the codigoOrgano property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoOrgano(String value) {
        this.codigoOrgano = value;
    }

    /**
     * Gets the value of the configuracionFirmaEntidad property.
     * 
     * @return
     *     possible object is
     *     {@link ConfiguracionFirmaEntidad }
     *     
     */
    public ConfiguracionFirmaEntidad getConfiguracionFirmaEntidad() {
        return configuracionFirmaEntidad;
    }

    /**
     * Sets the value of the configuracionFirmaEntidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConfiguracionFirmaEntidad }
     *     
     */
    public void setConfiguracionFirmaEntidad(ConfiguracionFirmaEntidad value) {
        this.configuracionFirmaEntidad = value;
    }

    /**
     * Gets the value of the configuracionPlantillaEntidad property.
     * 
     * @return
     *     possible object is
     *     {@link ConfiguracionPlantillaEntidad }
     *     
     */
    public ConfiguracionPlantillaEntidad getConfiguracionPlantillaEntidad() {
        return configuracionPlantillaEntidad;
    }

    /**
     * Sets the value of the configuracionPlantillaEntidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConfiguracionPlantillaEntidad }
     *     
     */
    public void setConfiguracionPlantillaEntidad(ConfiguracionPlantillaEntidad value) {
        this.configuracionPlantillaEntidad = value;
    }

    /**
     * Gets the value of the esDefault property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsDefault() {
        return esDefault;
    }

    /**
     * Sets the value of the esDefault property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsDefault(String value) {
        this.esDefault = value;
    }

    /**
     * Gets the value of the fechaAlta property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaAlta() {
        return fechaAlta;
    }

    /**
     * Sets the value of the fechaAlta property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaAlta(XMLGregorianCalendar value) {
        this.fechaAlta = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the metaDatos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the metaDatos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetaDatos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MetaDato }
     * 
     * 
     */
    public List<MetaDato> getMetaDatos() {
        if (metaDatos == null) {
            metaDatos = new ArrayList<MetaDato>();
        }
        return this.metaDatos;
    }

    /**
     * Gets the value of the nombre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Gets the value of the orderBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Sets the value of the orderBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderBy(String value) {
        this.orderBy = value;
    }

    /**
     * Gets the value of the tipoIntegracion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoIntegracion() {
        return tipoIntegracion;
    }

    /**
     * Sets the value of the tipoIntegracion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoIntegracion(String value) {
        this.tipoIntegracion = value;
    }

}
