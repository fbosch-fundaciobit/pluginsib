
package es.caib.digital.ws.api.copiaautentica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eniBaseVO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="eniBaseVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eniBaseVO")
@XmlSeeAlso({
    EniEstadoElaboracion.class,
    EniFirmaConCertificado.class,
    EniMetadata.class,
    EniContenidoFirmaCSV.class,
    EniContenidoFirma.class,
    EniFirma.class
})
public class EniBaseVO {


}
