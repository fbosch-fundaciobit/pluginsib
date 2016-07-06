
package es.caib.digital.ws.api.copiaautentica;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eniEnumEstadoElaboracion.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="eniEnumEstadoElaboracion">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ESTADO_ELABORACION_ORIGINAL"/>
 *     &lt;enumeration value="ESTADO_ELABORACION_COPIA_AUTENTICA_CAMBIO_FORMATO"/>
 *     &lt;enumeration value="ESTADO_ELABORACION_COPIA_AUTENTICA_DOCUMENTO_PAPEL"/>
 *     &lt;enumeration value="ESTADO_ELABORACION_COPIA_PARCIAL_AUTENTICA"/>
 *     &lt;enumeration value="ESTADO_ELABORACION_OTROS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "eniEnumEstadoElaboracion")
@XmlEnum
public enum EniEnumEstadoElaboracion {

    ESTADO_ELABORACION_ORIGINAL,
    ESTADO_ELABORACION_COPIA_AUTENTICA_CAMBIO_FORMATO,
    ESTADO_ELABORACION_COPIA_AUTENTICA_DOCUMENTO_PAPEL,
    ESTADO_ELABORACION_COPIA_PARCIAL_AUTENTICA,
    ESTADO_ELABORACION_OTROS;

    public String value() {
        return name();
    }

    public static EniEnumEstadoElaboracion fromValue(String v) {
        return valueOf(v);
    }

}
