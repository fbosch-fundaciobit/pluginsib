
package es.caib.digital.ws.api.copiaautentica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eniEstadoElaboracion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="eniEstadoElaboracion">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.digital.caib.es/}eniBaseVO">
 *       &lt;sequence>
 *         &lt;element name="identificadorDocumentoOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valorEstadoElaboracion" type="{http://service.ws.digital.caib.es/}eniEnumEstadoElaboracion" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eniEstadoElaboracion", propOrder = {
    "identificadorDocumentoOrigen",
    "valorEstadoElaboracion"
})
public class EniEstadoElaboracion
    extends EniBaseVO
{

    protected String identificadorDocumentoOrigen;
    protected EniEnumEstadoElaboracion valorEstadoElaboracion;

    /**
     * Gets the value of the identificadorDocumentoOrigen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificadorDocumentoOrigen() {
        return identificadorDocumentoOrigen;
    }

    /**
     * Sets the value of the identificadorDocumentoOrigen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificadorDocumentoOrigen(String value) {
        this.identificadorDocumentoOrigen = value;
    }

    /**
     * Gets the value of the valorEstadoElaboracion property.
     * 
     * @return
     *     possible object is
     *     {@link EniEnumEstadoElaboracion }
     *     
     */
    public EniEnumEstadoElaboracion getValorEstadoElaboracion() {
        return valorEstadoElaboracion;
    }

    /**
     * Sets the value of the valorEstadoElaboracion property.
     * 
     * @param value
     *     allowed object is
     *     {@link EniEnumEstadoElaboracion }
     *     
     */
    public void setValorEstadoElaboracion(EniEnumEstadoElaboracion value) {
        this.valorEstadoElaboracion = value;
    }

}
