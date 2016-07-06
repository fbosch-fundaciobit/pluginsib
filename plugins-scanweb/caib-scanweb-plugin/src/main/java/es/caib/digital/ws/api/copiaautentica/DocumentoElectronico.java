
package es.caib.digital.ws.api.copiaautentica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for documentoElectronico complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="documentoElectronico">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.digital.caib.es/}baseVO">
 *       &lt;sequence>
 *         &lt;element name="datos" type="{http://service.ws.digital.caib.es/}datosDocumento" minOccurs="0"/>
 *         &lt;element name="firma" type="{http://service.ws.digital.caib.es/}firmaElectronica" minOccurs="0"/>
 *         &lt;element name="metadatos" type="{http://service.ws.digital.caib.es/}metadatosDocumentoElectronico" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "documentoElectronico", propOrder = {
    "datos",
    "firma",
    "metadatos"
})
public class DocumentoElectronico
    extends BaseVO
{

    protected DatosDocumento datos;
    protected FirmaElectronica firma;
    protected MetadatosDocumentoElectronico metadatos;

    /**
     * Gets the value of the datos property.
     * 
     * @return
     *     possible object is
     *     {@link DatosDocumento }
     *     
     */
    public DatosDocumento getDatos() {
        return datos;
    }

    /**
     * Sets the value of the datos property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatosDocumento }
     *     
     */
    public void setDatos(DatosDocumento value) {
        this.datos = value;
    }

    /**
     * Gets the value of the firma property.
     * 
     * @return
     *     possible object is
     *     {@link FirmaElectronica }
     *     
     */
    public FirmaElectronica getFirma() {
        return firma;
    }

    /**
     * Sets the value of the firma property.
     * 
     * @param value
     *     allowed object is
     *     {@link FirmaElectronica }
     *     
     */
    public void setFirma(FirmaElectronica value) {
        this.firma = value;
    }

    /**
     * Gets the value of the metadatos property.
     * 
     * @return
     *     possible object is
     *     {@link MetadatosDocumentoElectronico }
     *     
     */
    public MetadatosDocumentoElectronico getMetadatos() {
        return metadatos;
    }

    /**
     * Sets the value of the metadatos property.
     * 
     * @param value
     *     allowed object is
     *     {@link MetadatosDocumentoElectronico }
     *     
     */
    public void setMetadatos(MetadatosDocumentoElectronico value) {
        this.metadatos = value;
    }

}
