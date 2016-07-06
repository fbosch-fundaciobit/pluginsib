
package es.caib.digital.ws.api.copiaautentica;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eniEnumTipoDocumental.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="eniEnumTipoDocumental">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="TIPO_DOCUMENTAL_RESOLUCION"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_ACUERDO"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_CONTRATO"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_CONVENIO"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_DECLARACION"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_COMUNICACION"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_NOTIFICACION"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_PUBLICACION"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_ACUSE_RECIBO"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_ACTA"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_CERTIFICADO"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_DILIGENCIA"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_INFORME"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_SOLICITUD"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_DENUNCUA"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_ALEGACION"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_RECURSOS"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_COMUNICACION_CUIDADANO"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_FACTURA"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_OTROS_INCAUTADOS"/>
 *     &lt;enumeration value="TIPO_DOCUMENTAL_OTROS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "eniEnumTipoDocumental")
@XmlEnum
public enum EniEnumTipoDocumental {

    TIPO_DOCUMENTAL_RESOLUCION,
    TIPO_DOCUMENTAL_ACUERDO,
    TIPO_DOCUMENTAL_CONTRATO,
    TIPO_DOCUMENTAL_CONVENIO,
    TIPO_DOCUMENTAL_DECLARACION,
    TIPO_DOCUMENTAL_COMUNICACION,
    TIPO_DOCUMENTAL_NOTIFICACION,
    TIPO_DOCUMENTAL_PUBLICACION,
    TIPO_DOCUMENTAL_ACUSE_RECIBO,
    TIPO_DOCUMENTAL_ACTA,
    TIPO_DOCUMENTAL_CERTIFICADO,
    TIPO_DOCUMENTAL_DILIGENCIA,
    TIPO_DOCUMENTAL_INFORME,
    TIPO_DOCUMENTAL_SOLICITUD,
    TIPO_DOCUMENTAL_DENUNCUA,
    TIPO_DOCUMENTAL_ALEGACION,
    TIPO_DOCUMENTAL_RECURSOS,
    TIPO_DOCUMENTAL_COMUNICACION_CUIDADANO,
    TIPO_DOCUMENTAL_FACTURA,
    TIPO_DOCUMENTAL_OTROS_INCAUTADOS,
    TIPO_DOCUMENTAL_OTROS;

    public String value() {
        return name();
    }

    public static EniEnumTipoDocumental fromValue(String v) {
        return valueOf(v);
    }

}
