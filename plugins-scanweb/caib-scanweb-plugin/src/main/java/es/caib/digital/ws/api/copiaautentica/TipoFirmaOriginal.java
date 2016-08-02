
package es.caib.digital.ws.api.copiaautentica;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoFirmaOriginal.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoFirmaOriginal">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SIN_FIRMA"/>
 *     &lt;enumeration value="XADESDetached"/>
 *     &lt;enumeration value="XADESEnveloped"/>
 *     &lt;enumeration value="CADESDetached"/>
 *     &lt;enumeration value="CADESAttached"/>
 *     &lt;enumeration value="PADES"/>
 *     &lt;enumeration value="CMSPKCS_7"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipoFirmaOriginal")
@XmlEnum
public enum TipoFirmaOriginal {

    SIN_FIRMA("SIN_FIRMA"),
    @XmlEnumValue("XADESDetached")
    XADES_DETACHED("XADESDetached"),
    @XmlEnumValue("XADESEnveloped")
    XADES_ENVELOPED("XADESEnveloped"),
    @XmlEnumValue("CADESDetached")
    CADES_DETACHED("CADESDetached"),
    @XmlEnumValue("CADESAttached")
    CADES_ATTACHED("CADESAttached"),
    PADES("PADES"),
    CMSPKCS_7("CMSPKCS_7");
    private final String value;

    TipoFirmaOriginal(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TipoFirmaOriginal fromValue(String v) {
        for (TipoFirmaOriginal c: TipoFirmaOriginal.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
