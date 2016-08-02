
package es.caib.digital.ws.api.entidades;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for metaDato complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="metaDato">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.digital.caib.es/}baseVO">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idEntidad" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="labelMetaDato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="longitud" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="nombreMetaDato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orden" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="tipoMetaDato" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="valorDefault" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valoresMetaDatos" type="{http://service.ws.digital.caib.es/}valorMetaDato" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metaDato", propOrder = {
    "id",
    "idEntidad",
    "labelMetaDato",
    "longitud",
    "nombreMetaDato",
    "orden",
    "tipoMetaDato",
    "valorDefault",
    "valoresMetaDatos"
})
public class MetaDato
    extends BaseVO
{

    protected String id;
    protected Integer idEntidad;
    protected String labelMetaDato;
    protected Integer longitud;
    protected String nombreMetaDato;
    protected Integer orden;
    protected Integer tipoMetaDato;
    protected String valorDefault;
    @XmlElement(nillable = true)
    protected List<ValorMetaDato> valoresMetaDatos;

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
     * Gets the value of the idEntidad property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdEntidad() {
        return idEntidad;
    }

    /**
     * Sets the value of the idEntidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdEntidad(Integer value) {
        this.idEntidad = value;
    }

    /**
     * Gets the value of the labelMetaDato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabelMetaDato() {
        return labelMetaDato;
    }

    /**
     * Sets the value of the labelMetaDato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabelMetaDato(String value) {
        this.labelMetaDato = value;
    }

    /**
     * Gets the value of the longitud property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLongitud() {
        return longitud;
    }

    /**
     * Sets the value of the longitud property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLongitud(Integer value) {
        this.longitud = value;
    }

    /**
     * Gets the value of the nombreMetaDato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreMetaDato() {
        return nombreMetaDato;
    }

    /**
     * Sets the value of the nombreMetaDato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreMetaDato(String value) {
        this.nombreMetaDato = value;
    }

    /**
     * Gets the value of the orden property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOrden() {
        return orden;
    }

    /**
     * Sets the value of the orden property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOrden(Integer value) {
        this.orden = value;
    }

    /**
     * Gets the value of the tipoMetaDato property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTipoMetaDato() {
        return tipoMetaDato;
    }

    /**
     * Sets the value of the tipoMetaDato property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTipoMetaDato(Integer value) {
        this.tipoMetaDato = value;
    }

    /**
     * Gets the value of the valorDefault property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValorDefault() {
        return valorDefault;
    }

    /**
     * Sets the value of the valorDefault property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValorDefault(String value) {
        this.valorDefault = value;
    }

    /**
     * Gets the value of the valoresMetaDatos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the valoresMetaDatos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValoresMetaDatos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValorMetaDato }
     * 
     * 
     */
    public List<ValorMetaDato> getValoresMetaDatos() {
        if (valoresMetaDatos == null) {
            valoresMetaDatos = new ArrayList<ValorMetaDato>();
        }
        return this.valoresMetaDatos;
    }

}
