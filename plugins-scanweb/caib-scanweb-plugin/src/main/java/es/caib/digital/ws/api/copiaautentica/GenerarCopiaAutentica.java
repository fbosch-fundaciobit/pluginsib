
package es.caib.digital.ws.api.copiaautentica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for generarCopiaAutentica complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="generarCopiaAutentica">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DocumentoElectronicoWSParam" type="{http://service.ws.digital.caib.es/}documentoElectronico" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generarCopiaAutentica", propOrder = {
    "documentoElectronicoWSParam"
})
public class GenerarCopiaAutentica {

    @XmlElement(name = "DocumentoElectronicoWSParam")
    protected DocumentoElectronico documentoElectronicoWSParam;

    /**
     * Gets the value of the documentoElectronicoWSParam property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentoElectronico }
     *     
     */
    public DocumentoElectronico getDocumentoElectronicoWSParam() {
        return documentoElectronicoWSParam;
    }

    /**
     * Sets the value of the documentoElectronicoWSParam property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentoElectronico }
     *     
     */
    public void setDocumentoElectronicoWSParam(DocumentoElectronico value) {
        this.documentoElectronicoWSParam = value;
    }

}
