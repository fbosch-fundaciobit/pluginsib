
package es.caib.digital.ws.api.copiaautentica;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eniEnumOrigenCreacion.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="eniEnumOrigenCreacion">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ORIGEN_CREACION_CIUDADANO"/>
 *     &lt;enumeration value="ORIGEN_CREACION_ADMINISTRACION"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "eniEnumOrigenCreacion")
@XmlEnum
public enum EniEnumOrigenCreacion {

    ORIGEN_CREACION_CIUDADANO,
    ORIGEN_CREACION_ADMINISTRACION;

    public String value() {
        return name();
    }

    public static EniEnumOrigenCreacion fromValue(String v) {
        return valueOf(v);
    }

}
