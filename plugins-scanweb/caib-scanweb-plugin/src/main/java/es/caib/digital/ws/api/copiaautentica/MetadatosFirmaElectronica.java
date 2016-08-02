
package es.caib.digital.ws.api.copiaautentica;

import java.sql.Timestamp;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for metadatosFirmaElectronica complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="metadatosFirmaElectronica">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.digital.caib.es/}eniFirma">
 *       &lt;sequence>
 *         &lt;element name="accionFirmanteSobreDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="asunto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="emisorCertificado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaFirma" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="idVersionPoliticaFirma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreCompletoFirmante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rolFirmante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoFirmaOriginal" type="{http://service.ws.digital.caib.es/}tipoFirmaOriginal" minOccurs="0"/>
 *         &lt;element name="valida" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metadatosFirmaElectronica", propOrder = {
    "accionFirmanteSobreDocumento",
    "asunto",
    "emisorCertificado",
    "fechaFirma",
    "idVersionPoliticaFirma",
    "nombreCompletoFirmante",
    "numeroSerie",
    "rolFirmante",
    "tipoFirmaOriginal",
    "valida"
})
public class MetadatosFirmaElectronica
    extends EniFirma
{

    protected String accionFirmanteSobreDocumento;
    protected String asunto;
    protected String emisorCertificado;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Timestamp fechaFirma;
    protected String idVersionPoliticaFirma;
    protected String nombreCompletoFirmante;
    protected String numeroSerie;
    protected String rolFirmante;
    protected TipoFirmaOriginal tipoFirmaOriginal;
    protected boolean valida;

    /**
     * Gets the value of the accionFirmanteSobreDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccionFirmanteSobreDocumento() {
        return accionFirmanteSobreDocumento;
    }

    /**
     * Sets the value of the accionFirmanteSobreDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccionFirmanteSobreDocumento(String value) {
        this.accionFirmanteSobreDocumento = value;
    }

    /**
     * Gets the value of the asunto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Sets the value of the asunto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAsunto(String value) {
        this.asunto = value;
    }

    /**
     * Gets the value of the emisorCertificado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmisorCertificado() {
        return emisorCertificado;
    }

    /**
     * Sets the value of the emisorCertificado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmisorCertificado(String value) {
        this.emisorCertificado = value;
    }

    /**
     * Gets the value of the fechaFirma property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Timestamp getFechaFirma() {
        return fechaFirma;
    }

    /**
     * Sets the value of the fechaFirma property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaFirma(Timestamp value) {
        this.fechaFirma = value;
    }

    /**
     * Gets the value of the idVersionPoliticaFirma property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdVersionPoliticaFirma() {
        return idVersionPoliticaFirma;
    }

    /**
     * Sets the value of the idVersionPoliticaFirma property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdVersionPoliticaFirma(String value) {
        this.idVersionPoliticaFirma = value;
    }

    /**
     * Gets the value of the nombreCompletoFirmante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreCompletoFirmante() {
        return nombreCompletoFirmante;
    }

    /**
     * Sets the value of the nombreCompletoFirmante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreCompletoFirmante(String value) {
        this.nombreCompletoFirmante = value;
    }

    /**
     * Gets the value of the numeroSerie property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroSerie() {
        return numeroSerie;
    }

    /**
     * Sets the value of the numeroSerie property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroSerie(String value) {
        this.numeroSerie = value;
    }

    /**
     * Gets the value of the rolFirmante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRolFirmante() {
        return rolFirmante;
    }

    /**
     * Sets the value of the rolFirmante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRolFirmante(String value) {
        this.rolFirmante = value;
    }

    /**
     * Gets the value of the tipoFirmaOriginal property.
     * 
     * @return
     *     possible object is
     *     {@link TipoFirmaOriginal }
     *     
     */
    public TipoFirmaOriginal getTipoFirmaOriginal() {
        return tipoFirmaOriginal;
    }

    /**
     * Sets the value of the tipoFirmaOriginal property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoFirmaOriginal }
     *     
     */
    public void setTipoFirmaOriginal(TipoFirmaOriginal value) {
        this.tipoFirmaOriginal = value;
    }

    /**
     * Gets the value of the valida property.
     * 
     */
    public boolean isValida() {
        return valida;
    }

    /**
     * Sets the value of the valida property.
     * 
     */
    public void setValida(boolean value) {
        this.valida = value;
    }

}
