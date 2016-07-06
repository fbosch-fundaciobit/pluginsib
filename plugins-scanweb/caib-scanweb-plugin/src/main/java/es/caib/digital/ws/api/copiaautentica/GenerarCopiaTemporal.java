
package es.caib.digital.ws.api.copiaautentica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for generarCopiaTemporal complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="generarCopiaTemporal">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DocumentoElectronicoWS" type="{http://service.ws.digital.caib.es/}documentoElectronico" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generarCopiaTemporal", propOrder = {
    "documentoElectronicoWS"
})
public class GenerarCopiaTemporal {

    @XmlElement(name = "DocumentoElectronicoWS")
    protected DocumentoElectronico documentoElectronicoWS;

    /**
     * Gets the value of the documentoElectronicoWS property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentoElectronico }
     *     
     */
    public DocumentoElectronico getDocumentoElectronicoWS() {
        return documentoElectronicoWS;
    }

    /**
     * Sets the value of the documentoElectronicoWS property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentoElectronico }
     *     
     */
    public void setDocumentoElectronicoWS(DocumentoElectronico value) {
        this.documentoElectronicoWS = value;
    }

}
