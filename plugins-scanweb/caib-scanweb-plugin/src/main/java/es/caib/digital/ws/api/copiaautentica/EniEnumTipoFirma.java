
package es.caib.digital.ws.api.copiaautentica;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eniEnumTipoFirma.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="eniEnumTipoFirma">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="TIPO_FIRMA_CSV"/>
 *     &lt;enumeration value="TIPO_FIRMA_XADES_INTERNALLY_DETACHED_SIGNATURE"/>
 *     &lt;enumeration value="TIPO_FIRMA_XADES_ENVELOPED_SIGNATURE"/>
 *     &lt;enumeration value="TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE"/>
 *     &lt;enumeration value="TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNATURE"/>
 *     &lt;enumeration value="TIPO_FIRMA_PADES"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "eniEnumTipoFirma")
@XmlEnum
public enum EniEnumTipoFirma {

    TIPO_FIRMA_CSV,
    TIPO_FIRMA_XADES_INTERNALLY_DETACHED_SIGNATURE,
    TIPO_FIRMA_XADES_ENVELOPED_SIGNATURE,
    TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE,
    TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNATURE,
    TIPO_FIRMA_PADES;

    public String value() {
        return name();
    }

    public static EniEnumTipoFirma fromValue(String v) {
        return valueOf(v);
    }

}
