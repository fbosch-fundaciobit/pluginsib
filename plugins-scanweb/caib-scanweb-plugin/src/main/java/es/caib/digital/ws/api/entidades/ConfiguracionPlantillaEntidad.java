
package es.caib.digital.ws.api.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for configuracionPlantillaEntidad complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="configuracionPlantillaEntidad">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.digital.caib.es/}baseVO">
 *       &lt;sequence>
 *         &lt;element name="logoEntidad" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="bytesPlantillaPersonalizada" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="nombreFicheroLogo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreFicheroPlantillaTransformacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "configuracionPlantillaEntidad", propOrder = {
    "logoEntidad",
    "bytesPlantillaPersonalizada",
    "nombreFicheroLogo",
    "nombreFicheroPlantillaTransformacion"
})
public class ConfiguracionPlantillaEntidad
    extends BaseVO
{

    protected byte[] logoEntidad;
    protected byte[] bytesPlantillaPersonalizada;
    protected String nombreFicheroLogo;
    protected String nombreFicheroPlantillaTransformacion;

    /**
     * Gets the value of the logoEntidad property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getLogoEntidad() {
        return logoEntidad;
    }

    /**
     * Sets the value of the logoEntidad property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setLogoEntidad(byte[] value) {
        this.logoEntidad = ((byte[]) value);
    }

    /**
     * Gets the value of the bytesPlantillaPersonalizada property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBytesPlantillaPersonalizada() {
        return bytesPlantillaPersonalizada;
    }

    /**
     * Sets the value of the bytesPlantillaPersonalizada property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBytesPlantillaPersonalizada(byte[] value) {
        this.bytesPlantillaPersonalizada = ((byte[]) value);
    }

    /**
     * Gets the value of the nombreFicheroLogo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreFicheroLogo() {
        return nombreFicheroLogo;
    }

    /**
     * Sets the value of the nombreFicheroLogo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreFicheroLogo(String value) {
        this.nombreFicheroLogo = value;
    }

    /**
     * Gets the value of the nombreFicheroPlantillaTransformacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreFicheroPlantillaTransformacion() {
        return nombreFicheroPlantillaTransformacion;
    }

    /**
     * Sets the value of the nombreFicheroPlantillaTransformacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreFicheroPlantillaTransformacion(String value) {
        this.nombreFicheroPlantillaTransformacion = value;
    }

}
