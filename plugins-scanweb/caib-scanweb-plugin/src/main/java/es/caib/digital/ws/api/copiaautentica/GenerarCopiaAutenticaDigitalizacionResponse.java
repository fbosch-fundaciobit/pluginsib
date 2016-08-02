
package es.caib.digital.ws.api.copiaautentica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for generarCopiaAutenticaDigitalizacionResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="generarCopiaAutenticaDigitalizacionResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DocumentoElectronicoWSResponse" type="{http://service.ws.digital.caib.es/}documentoElectronico" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generarCopiaAutenticaDigitalizacionResponse", propOrder = {
    "documentoElectronicoWSResponse"
})
public class GenerarCopiaAutenticaDigitalizacionResponse {

    @XmlElement(name = "DocumentoElectronicoWSResponse")
    protected DocumentoElectronico documentoElectronicoWSResponse;

    /**
     * Gets the value of the documentoElectronicoWSResponse property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentoElectronico }
     *     
     */
    public DocumentoElectronico getDocumentoElectronicoWSResponse() {
        return documentoElectronicoWSResponse;
    }

    /**
     * Sets the value of the documentoElectronicoWSResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentoElectronico }
     *     
     */
    public void setDocumentoElectronicoWSResponse(DocumentoElectronico value) {
        this.documentoElectronicoWSResponse = value;
    }

}
