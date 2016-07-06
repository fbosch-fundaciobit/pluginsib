
package es.caib.digital.ws.api.copiaautentica;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eniFirmaConCertificado complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="eniFirmaConCertificado">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.digital.caib.es/}eniBaseVO">
 *       &lt;sequence>
 *         &lt;element name="firmaBase64" type="{http://www.w3.org/2001/XMLSchema}byte" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="referenciaFirma" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eniFirmaConCertificado", propOrder = {
    "firmaBase64",
    "referenciaFirma"
})
public class EniFirmaConCertificado
    extends EniBaseVO
{

    @XmlElement(nillable = true)
    protected List<Byte> firmaBase64;
    protected Object referenciaFirma;

    /**
     * Gets the value of the firmaBase64 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the firmaBase64 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFirmaBase64().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Byte }
     * 
     * 
     */
    public List<Byte> getFirmaBase64() {
        if (firmaBase64 == null) {
            firmaBase64 = new ArrayList<Byte>();
        }
        return this.firmaBase64;
    }

    /**
     * Gets the value of the referenciaFirma property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getReferenciaFirma() {
        return referenciaFirma;
    }

    /**
     * Sets the value of the referenciaFirma property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setReferenciaFirma(Object value) {
        this.referenciaFirma = value;
    }

}
