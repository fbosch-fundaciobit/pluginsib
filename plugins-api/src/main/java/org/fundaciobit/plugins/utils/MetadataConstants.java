package org.fundaciobit.plugins.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
/**
 * 
 * @author anadal
 *
 */
public class MetadataConstants {

  
  
  /**
   * 
   * @author anadal
   *
   */
  public static class MetadataConstant {

    private final String key;

    private final MetadataType type;

    private final String description;

    private Map<String, String> allowedValues;

    private String patternRegEx;

    private Set<String> aliases;

    /**
     * @param key
     * @param type
     * @param description
     */
    public MetadataConstant(String key, MetadataType type, String description) {
      super();
      this.key = key;
      this.type = type;
      this.description = description;
    }

    /**
     * @param key
     * @param type
     * @param allowedValues
     * @param patternRegEx
     * @param description
     * @param aliases
     */
    public MetadataConstant(String key, MetadataType type, String description,
        Map<String, String> allowedValues) {
      super();
      this.key = key;
      this.type = type;
      this.allowedValues = allowedValues;
      this.description = description;
    }
    
    
    /**
     * @param key
     * @param type
     * @param patternRegEx
     * @param description
     */
    public MetadataConstant(String key, MetadataType type, String description,
        String patternRegEx) {
      super();
      this.key = key;
      this.type = type;
      this.patternRegEx = patternRegEx;
      this.description = description;
    }
    
    

    /**
     * @param key
     * @param type
     * @param allowedValues
     * @param patternRegEx
     * @param description
     * @param aliases
     */
    public MetadataConstant(String key, MetadataType type, String description,
        Map<String, String> allowedValues, String patternRegEx, Set<String> aliases) {
      super();
      this.key = key;
      this.type = type;
      this.allowedValues = allowedValues;
      this.patternRegEx = patternRegEx;
      this.description = description;
      this.aliases = aliases;
    }

    public Map<String, String> getAllowedValues() {
      return allowedValues;
    }

    public void setAllowedValues(Map<String, String> allowedValues) {
      this.allowedValues = allowedValues;
    }

    public String getPatternRegEx() {
      return patternRegEx;
    }

    public void setPatternRegEx(String patternRegEx) {
      this.patternRegEx = patternRegEx;
    }

    public String getDescription() {
      return description;
    }

    public Set<String> getAliases() {
      return aliases;
    }

    public void setAliases(Set<String> aliases) {
      this.aliases = aliases;
    }

    public String getKey() {
      return key;
    }

    public MetadataType getType() {
      return type;
    }

  }
  
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  // ---------------------  Tipo eni:docBase --------------------------
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  
  /**
   * Código de la aplicación de trámite que generó el documento.
   */
  public static final String ENI_APP_TRAMITE_DOC = "eni:app_tramite_doc";
  
  
  public static final MetadataConstant _ENI_APP_TRAMITE_DOC = new MetadataConstant(ENI_APP_TRAMITE_DOC,
      MetadataType.STRING, "Código de la aplicación de trámite que generó el documento.");

  
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  // ---------------------  Tipo eni:expediente --------------------------
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  
  /**
   * Código de la aplicación de trámite que generó el expediente.
   */
  public static final String ENI_APP_TRAMITE_EXP = "eni:app_tramite_exp";
  
  
  public static final MetadataConstant _ENI_APP_TRAMITE_EXP = new MetadataConstant(ENI_APP_TRAMITE_EXP,
      MetadataType.STRING, "Código de la aplicación de trámite que generó el expediente.");

  
  
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  // ---------------------  Aspecto eni:firmadoBase --------------------------
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  
  
  /**
   * Valor del Código Seguro de Verificación del documento.
   */
  public static final String ENI_CSV = "eni:csv";
  
  
  public static final MetadataConstant _ENI_CSV = new MetadataConstant(ENI_CSV,
      MetadataType.STRING, "Valor del Código Seguro de Verificación del documento.");

  /**
    Denominación normalizada del tipo de firma. Los posibles valores asignables son los siguientes:
  TF01 - CSV
  TF02 - XAdES internally detached signature");
  TF03 - XAdES enveloped signature.
  TF04 - CAdES detached/explicit signature.
  TF05 - CAdES attached/implicit signature.
  TF06 - PAdES.
  El tipo TF04 será establecido por defecto para documentos firmados, exceptuando los 
  documentos en formato PDF o PDF/A, cuyo tipo será TF06.
   */
  public static final String ENI_TIPO_FIRMA = "eni:tipoFirma";
  
  public static final MetadataConstant _ENI_TIPO_FIRMA = new MetadataConstant(ENI_TIPO_FIRMA,
      MetadataType.STRING, "Denominación normalizada del tipo de firma",
      new HashMap<String, String>(){{
        put("TF01","CSV");
        put("TF02","XAdES internally detached signature");
        put("TF03","XAdES enveloped signature");
        put("TF04","CAdES detached/explicit signature");
        put("TF05","CAdES attached/implicit signature");
        put("TF06","PAdES");
        }});

  /**
   * Perfil empleado en una firma con certificado electrónico. Los posibles valores asignables son los siguientes:
      EPES
      T
      C
      X
      XL
      A
      BASELINE B-Level
      BASELINE LT-Level
      BASELINE LTA-Level
      BASELINE T-Level
      LTV
   */
  public static final String ENI_PERFIL_FIRMA = "eni:perfil_firma";
  
  
  public static final MetadataConstant _ENI_PERFIL_FIRMA = new MetadataConstant(ENI_PERFIL_FIRMA,
      MetadataType.STRING, "Perfil de firma empleado en una firma con certificado electrónico.",
      new HashMap<String, String>(){{
        put("EPES","Para las firmas XADES, CADES y PADES");
        put("T","Para las firmas XADES y CADES");
        put("C","Para las firmas XADES y CADES");
        put("X","Para las firmas XADES y CADES");
        put("XL","Para las firmas XADES y CADES");
        put("A","Para las firmas XADES y CADES");
        put("BASELINE B-Level","Para las firmas XADES, CADES y PADES");
        put("BASELINE LT-Level","Para las firmas XADES y CADES");
        put("BASELINE LTA-Level","Para las firmas XADES y CADES");
        put("BASELINE T-Level","Para las firmas XADES, CADES y PADES");
        put("LTV","Para las firmas PADES");
        }});
  
  
  
  /**
   * Fecha en la que fue resellado el documento por última vez. Un valor nulo,
   *  indica que nunca ha sido resellado.
   */
  public static final String ENI_FECHA_SELLADO = "eni:fecha_sellado";
  
  
  public static final MetadataConstant _ENI_FECHA_SELLADO = new MetadataConstant(ENI_FECHA_SELLADO,
      MetadataType.DATE, "Fecha en la que fue resellado el documento por última vez."
       + "Un valor nulo, indica que nunca ha sido resellado");
  
  

  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  // ---------------------  Aspecto eni:interoperable --------------------------
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  
  /**
   * Identificador normalizado de la administración generadora o que captura el documento,
   *  o tramitadora del expediente (DIR3).
   */
  public static final String ENI_ORGANO = "eni:organo";

  
  public static final MetadataConstant _ENI_ORGANO = new MetadataConstant(ENI_ORGANO,
      MetadataType.STRING, "Identificador normalizado de la administración generadora "  + 
        " o que captura el documento, o tramitadora del expediente (DIR3)",
        "[A-Z]\\d{8}");
  
  
  /**
   * Identificador ENI de documento o expediente. Compuesto de la siguiente forma:
   *   - Expedientes: ES_<ORGANO>_<AAAA>_EXP_<ID_ESP>
   *   - Documentos: ES_<ORGANO>_<AAAA>_<ID_ESP>
   */
  public static final String ENI_ID = "eni:id";
  
  public static final MetadataConstant _ENI_ID = new MetadataConstant(ENI_ID,
      MetadataType.STRING, "Identificador ENI de documento o expediente. Compuesto de la siguiente forma:\n"
         + "   - Expedientes: ES_<ORGANO>_<AAAA>_EXP_<ID_ESP>\n"
         + "   - Documentos: ES_<ORGANO>_<AAAA>_<ID_ESP>",
        "(ES_" + _ENI_ORGANO.getPatternRegEx() + "_\\d{4}_EXP_.{30})|(ES_" + _ENI_ORGANO.getPatternRegEx() + "_\\d{4}_.{30})");
  
  
  
  /**
   * Identificador normalizado de la versión de la Norma Técnica de Interoperabilidad.
   * Por defecto, se establecerán los siguientes valores:
   * - Expedientes: http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e
   * - Documentos: http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e
   */
  public static final String ENI_VERSION_NTI = "eni:v_nti";
  
  public static final MetadataConstant _ENI_VERSION_NTI = new MetadataConstant(ENI_VERSION_NTI,
      MetadataType.STRING, "Identificador normalizado de la versión de la Norma Técnica de Interoperabilidad",
      new HashMap<String, String>(){{
        put("http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e","Para expediente");
        put("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e","Para documento");
        }});
  
  
  /**
   * Origen del contenido: Ciudadano o administración. Los posibles valores
   *  asignables son los siguientes:
   *   + 1 (Administración).
   *   + 0 (Ciudadano).
   */
  public static final String ENI_ORIGEN = "eni:origen";
  
  public static final MetadataConstant _ENI_ORIGEN = new MetadataConstant(ENI_ORIGEN,
      MetadataType.STRING, "Origen del contenido: Ciudadano o administración",
      new HashMap<String, String>(){{
        put("1","Administración");
        put("0","Ciudadano");
        }});
  
  /**
   * Identificador normalizado del documento origen al que corresponda la copia,
   *  si este es un documento electrónico. Requerido si estado_elaboracion =EE02 
   *  o = EE03, o = EE04. Formato del identificador descrito en metadato id de 
   *  este aspecto (ES_<ORGANO>_<AAAA>_<ID_ESP>).
   */
  public static final String ENI_ID_ORIGEN = "eni:id_origen";
  
  public static final MetadataConstant _ENI_ID_ORIGEN = new MetadataConstant(ENI_ID_ORIGEN,
      MetadataType.STRING, "Identificador normalizado del documento origen al que "
          + "corresponda la copia, si este es un documento electrónico.Requerido si"
          + " estado_elaboracion =EE02 o = EE03, o = EE04",
        "ES_" + _ENI_ORGANO.getPatternRegEx() + "_\\d{4}_.{30}");
  
  

  
  /**
   * Estado de la situación de elaboración del documento. Los posibles valores asignables son los siguientes:
   *  - EE01 (Original).
   *  - EE02 (Copia electrónica auténtica con cambio de formato).
   *  - EE03 (Copia electrónica auténtica de documento papel )
   *  - EE04 (Copia electrónica parcial auténtica).
   *  - EE99 (Otros)
   */
  public static final String ENI_ESTADO_ELABORACION = "eni:estado_elaboracion";
  
  public static final MetadataConstant _ENI_ESTADO_ELABORACION = new MetadataConstant(ENI_ESTADO_ELABORACION,
      MetadataType.STRING, "Estado de la situación de elaboración del documento.",
      new HashMap<String, String>(){{
        put("EE01","Original");
        put("EE02","Copia electrónica auténtica con cambio de formato");
        put("EE03","Copia electrónica auténtica de documento papel");
        put("EE04","Copia electrónica parcial auténtica");
        put("EE99","Otros");
        }});
  
  /**
   * Tipo de documento ENI. Los posibles valores asignables son los siguientes:
   *  TD01 (Resolución), TD02 (Acuerdo), TD03 (Contrato), TD04 (Convenio),
   *  TD05 (Declaración), TD06 (Comunicación), TD07 (Notificación), 
   *  TD08 (Publicación), TD09 (Acuse de recibo), TD10 (Acta), TD11 (Certificado),
   *  TD12 (Diligencia), TD13 (Informe), TD14 (Solicitud), TD15 (Denuncia),
   *  TD16 (Alegación), TD17 (Recursos), TD18 (Comunicación ciudadano),
   *  TD19 (Factura), TD20 (Otros incautados) y TD99 (Otros).
   */
  public static final String ENI_TIPO_DOCUMENTAL = "eni:tipo_doc_ENI";
  
  public static final MetadataConstant _ENI_TIPO_DOCUMENTAL = new MetadataConstant(
      ENI_TIPO_DOCUMENTAL, MetadataType.STRING, "Tipo de documento ENI",
      new HashMap<String, String>(){{
        put("TD01", "Resolución");
        put("TD02", "Acuerdo");
        put("TD03", "Contrato");
        put("TD04", "Convenio");
        put("TD05", "Declaración");
        put("TD06", "Comunicación");
        put("TD07", "Notificación");
        put("TD08", "Publicación");
        put("TD09", "Acuse de recibo");
        put("TD10", "Acta");
        put("TD11", "Certificado");
        put("TD12", "Diligencia");
        put("TD13", "Informe");
        put("TD14", "Solicitud");
        put("TD15", "Denuncia");
        put("TD16", "Alegación");
        put("TD17", "Recursos");
        put("TD18", "Comunicación ciudadano");
        put("TD19", "Factura");
        put("TD20", "Otros incautados");
        put("TD99", "Otros");
        }});
  
  /**
   * Fecha de captura del documento o apertura del expediente en el sistema.
   * Si no es informada por el sistema de información, se establece la fecha
   * de creación del nodo en el SGD.
   */
  public static final String ENI_FECHA_INICIO = "eni:fecha_inicio";
  
  public static final MetadataConstant _ENI_FECHA_INICIO = new MetadataConstant(ENI_FECHA_INICIO,
      MetadataType.DATE, "Fecha de captura del documento o apertura del expediente en el sistema."
     + "Si no es informada por el sistema de información, se establece la fecha"
     + "de creación del nodo en el SGD");

  
  /**
   * Identificador único codificado que determina una categoría en el Cuadro de
   * Clasificación de CAIB.
   */
  public static final String ENI_COD_CLASIFICACION = "eni:cod_clasificacion";
  
  
  public static final MetadataConstant _ENI_COD_CLASIFICACION = new MetadataConstant(
      ENI_COD_CLASIFICACION,   MetadataType.STRING, "Identificador único codificado "
          + "que determina una categoría en el Cuadro de Clasificación de CAIB.");
  
  /**
   * Denominación del formato lógico del fichero contenido en el documento electrónico.
   * Requerido solo para documentos en formato electrónico (“soporte” =Digital).
   */
  public static final String ENI_NOMBRE_FORMATO = "eni:nombre_formato";
  
  public static final MetadataConstant _ENI_NOMBRE_FORMATO = new MetadataConstant(
      ENI_NOMBRE_FORMATO,   MetadataType.STRING, "Denominación del formato lógico"
          + " del fichero contenido en el documento electrónico. Requerido solo"
          + " para documentos en formato electrónico (“soporte” =Digital).");
  
  /**
   * Requerido solo para documentos en formato electrónico (“soporte” =Digital).
   */
  public static final String ENI_EXTENSION_FORMATO = "eni:extension_formato";
  
  public static final MetadataConstant _ENI_EXTENSION_FORMATO = new MetadataConstant(
      ENI_EXTENSION_FORMATO,   MetadataType.STRING,
      "Requerido solo para documentos en formato electrónico (“soporte” =Digital)");
  

  
  /**
   * Referencia a la disposición normativa que define la creación y uso del CSV correspondiente.
   */
  public static final String ENI_DEF_CSV = "eni:def_csv";
  
  public static final MetadataConstant _ENI_DEF_CSV = new MetadataConstant(
      ENI_DEF_CSV,   MetadataType.STRING,
      "Referencia a la disposición normativa que define la creación y uso del CSV correspondiente.");
  
  
  /**
   * Medida de la capacidad para capturar los detalles del documento original, 
   * a menudo cuantificada en píxeles por pulgada.
   */
  public static final String ENI_RESOLUCION = "eni:resolución";
  
  public static final MetadataConstant _ENI_RESOLUCION = new MetadataConstant(
      ENI_RESOLUCION,   MetadataType.STRING,
      "Medida de la capacidad para capturar los detalles del documento original, "
      + "a menudo cuantificada en píxeles por pulgada.", "\\s*\\d+\\s*x\\s*\\d+\\s*[a-zA-Z]*\\s*");
  
  
  
  /**
   * Idioma o lengua utilizada en un documento, hablada o usada por un agente al 
   * realizar una actividad. Utilizar formato RFC4646:2006
   */
  public static final String ENI_IDIOMA = "eni:idioma";
  
  public static final MetadataConstant _ENI_IDIOMA = new MetadataConstant(
      ENI_IDIOMA,   MetadataType.STRING,
      "Idioma o lengua utilizada en un documento, hablada o usada por un agente al"
      + " realizar una actividad. Utilizar formato RFC4646:2006", 
      "[a-zA-Z]{1,8}(-[a-zA-Z0-9]{1,8})*");
      //"/^[a-z]{2,3}(?:-[a-zA-Z]{4})?(?:-[A-Z]{2,3})?$/");
  
  
  /**
   * Estado del expediente en el momento de traslado (Abierto, Cerrado, Índice para remisión cerrado).
   * Los posibles valores asignables son los siguientes:
   *    - E01 (Abierto).
   *    - E02 (Cerrado).
   *    - E03 (Índice para remisión cerrado, exclusivo para subexpedientes de intercambio).
   */
  public static final String ENI_ESTADO_EXP = "eni:estado_exp";
  
  public static final MetadataConstant _ENI_ESTADO_EXP = new MetadataConstant(
      ENI_ESTADO_EXP, MetadataType.STRING,
      "Estado del expediente en el momento de traslado (Abierto, Cerrado, Índice para remisión cerrado)",
      new HashMap<String, String>(){{
        put("E01", "Abierto");
        put("E02", "Cerrado");
        put("E03", "Índice para remisión cerrado, exclusivo para subexpedientes de intercambio");
        }});
  

  /**
   * Tamaño lógico del documento o expediente (para este último, calculado como
   *  el número de unidades que lo componen).
   */
  public static final String ENI_TAMANO_LOGICO = "eni:tamano_logico";
  
  public static final MetadataConstant _ENI_TAMANO_LOGICO = new MetadataConstant(
      ENI_TAMANO_LOGICO, MetadataType.INTEGER, "Tamaño lógico del documento o expediente"
       + " (para este último, calculado como el número de unidades que lo componen)");
  

  /**
   * Lista de identificadores de interesados en el expediente.
   */
  public static final String ENI_INTERESADOS_EXP = "eni:interesados_exp";
  
  public static final MetadataConstant _ENI_INTERESADOS_EXP = new MetadataConstant(
      ENI_INTERESADOS_EXP, MetadataType.STRING, 
      "Lista de identificadores de interesados en el expediente.");
  

  /**
   * Información adicional sobre el documento o expediente.
   */
  public static final String ENI_DESCRIPCION = "eni:descripcion";
  
  public static final MetadataConstant _ENI_DESCRIPCION = new MetadataConstant(
      ENI_DESCRIPCION, MetadataType.STRING, 
      "Información adicional sobre el documento o expediente.");

  /**
   * Palabra clave que describe el contenido del documento o de la regulación.
   */
  public static final String ENI_TERMINO_PUNTO_ACCESO = "eni:termino_punto_acceso";
  
  public static final MetadataConstant _ENI_TERMINO_PUNTO_ACCESO = new MetadataConstant(
      ENI_TERMINO_PUNTO_ACCESO, MetadataType.STRING, 
      "Palabra clave que describe el contenido del documento o de la regulación");

  /**
   * Identificador asignado a una palabra clave dentro de un esquema de puntos de acceso.
   */
  public static final String ENI_ID_PUNTO_ACCESO = "eni:id_punto_acceso";
  
  public static final MetadataConstant _ENI_ID_PUNTO_ACCESO = new MetadataConstant(
      ENI_ID_PUNTO_ACCESO, MetadataType.STRING, 
      "Identificador asignado a una palabra clave dentro de un esquema de puntos de acceso");

  /**
   * Referencia al esquema del que el punto de acceso se ha extraído. Condicionado al uso
   * de taxonomías de las que se extrae los metadatos “Término punto de acceso”
   *  o “ID de punto de acceso”.
   */
  public static final String ENI_ESQUEMA_PUNTO_ACCESO = "eni:esquema_punto_acceso";
  
  public static final MetadataConstant _ENI_ESQUEMA_PUNTO_ACCESO = new MetadataConstant(
      ENI_ESQUEMA_PUNTO_ACCESO, MetadataType.STRING, 
      "Referencia al esquema del que el punto de acceso se ha extraído. Condicionado al uso "
       + " de taxonomías de las que se extrae los metadatos “Término punto de acceso”"
       + " o “ID de punto de acceso”");


  /**
   * Objeto físico donde se almacena un expediente o documento. Los posibles valores
   *  asignables son los siguientes:
   *   - Digital
   *   - CD-ROM
   *   - DVD
   *   - Disco duro externo
   *   - Memoria USB
   *   - Caja
   *   - Otros
   *  Si no es informado por el sistema de información, se establece el valor “Digital”.
   */
  public static final String ENI_SOPORTE = "eni:soporte";
  
  public static final MetadataConstant _ENI_SOPORTE = new MetadataConstant(
      ENI_SOPORTE, MetadataType.STRING,
      "Objeto físico donde se almacena un expediente o documento",
      new HashMap<String, String>(){{
        put("Digital","Digital");
        put("CD-ROM","CD-ROM");
        put("DVD","DVD");
        put("Disco duro externo","Disco duro externo");
        put("Memoria USB","Memoria USB");
        put("Caja","Caja");
        put("Otros","Otros");
        }});

  /**
   * Localización física del expediente en el archivo central (Número de instalación
   *  o caja remitida al archivo general)
   */
  public static final String ENI_LOC_ARCHIVO_CENTRAL = "eni:loc_archivo_central";
  
  public static final MetadataConstant _ENI_LOC_ARCHIVO_CENTRAL = new MetadataConstant(
      ENI_LOC_ARCHIVO_CENTRAL, MetadataType.STRING, 
      "Localización física del expediente en el archivo central (Número de instalación "
      + " o caja remitida al archivo general)");
  

  /**
   * Localización física del expediente en el archivo general (Número de instalación
   *  o caja en el archivo general).
   */
  public static final String ENI_LOC_ARCHIVO_GENERAL = "eni:loc_archivo_general";

  public static final MetadataConstant _ENI_LOC_ARCHIVO_GENERAL = new MetadataConstant(
      ENI_LOC_ARCHIVO_GENERAL, MetadataType.STRING, 
      "Localización física del expediente en el archivo central (Número de instalación "
      + " o caja remitida al archivo general)");

  /**
   * Elemento de medida utilizado para registrar las dimensiones de un documento físico
   *  o el tamaño o duración lógica de un documento digital.
   */
  public static final String ENI_UNIDADES= "eni:unidades";
  
  public static final MetadataConstant _ENI_UNIDADES = new MetadataConstant(
      ENI_UNIDADES, MetadataType.STRING, 
      "Elemento de medida utilizado para registrar las dimensiones de un documento físico"
      + " o el tamaño o duración lógica de un documento digital");

  /**
   * Tipo documental específico establecido por los procedimientos del Govern.
   */
  public static final String ENI_SUBTIPO_DOC = "eni:subtipo_doc";
  
  public static final MetadataConstant _ENI_SUBTIPO_DOC = new MetadataConstant(
      ENI_SUBTIPO_DOC, MetadataType.STRING, 
      "Tipo documental específico establecido por los procedimientos del Govern.");


  /**
   *  Identificador único del procedimiento administrativo con el que se relaciona
   *  el expediente. Este identificador puede corresponderse, bien con el 
   *  identificador del Sistema de Información Administrativa (SIA), bien con un
   *  identificador propio con el siguiente formato:
   *         <ORGANO>_PRO_<ID_PROC>
   *  Donde,
   *    - ORGANO: código DIR3 de organismo/unidad.
   *    - ID_PROC: código alfanumérico que identifica unívocamente al procedimiento 
   *       dentro de los propios de la administración (Longitud: 30 caracteres).
   */
  public static final String ENI_ID_TRAMITE = "eni:id_tramite";

  // TODO FALTA el format SIA
  public static final MetadataConstant _ENI_ID_TRAMITE = new MetadataConstant(ENI_ID_TRAMITE,
      MetadataType.STRING, "Identificador único del procedimiento administrativo con el que se relaciona"
     + " el expediente. Este identificador puede corresponderse, bien con el "
     + " identificador del Sistema de Información Administrativa (SIA), bien con un"
     + " identificador propio con el siguiente formato: <ORGANO>_PRO_<ID_PROC>",
        "(" + _ENI_ORGANO.getPatternRegEx() + "_PRO_.{30})|(.+)");
  
  
  


  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  // ---------------------  Aspecto eni:registrable --------------------------
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------

  /**
   * Tipo de asiento registral. Los posibles valores asignables son los siguientes:
   *    - 0 (Registro de entrada).
   *    - 1 (Registro de salida).
   */
  public static final String ENI_TIPO_ASIENTO_REGISTRAL = "eni:tipo_asiento_registral";
  
  public static final MetadataConstant _ENI_TIPO_ASIENTO_REGISTRAL = new MetadataConstant(
      ENI_TIPO_ASIENTO_REGISTRAL, MetadataType.INTEGER,
      "Tipo de asiento registral",
      new HashMap<String, String>(){{
        put("0","Registro de entrada");
        put("1","Registro de salida");
        }});
  


  /**
   * Código de la Entidad Registral de origen o de destino del documento.
   */
  public static final String ENI_CODIGO_OFICINA_REGISTRO = "eni:codigo_oficina_registro";
  
  public static final MetadataConstant _ENI_CODIGO_OFICINA_REGISTRO = new MetadataConstant(
      ENI_CODIGO_OFICINA_REGISTRO,
      MetadataType.STRING, " Código de la Entidad Registral de origen o de destino del documento.");


  /**
   * Fecha y hora de entrada del documento en la entidad registral de origen o de destino.
   */
  public static final String ENI_FECHA_ASIENTO_REGISTRAL = "eni:fecha_asiento_registral";

  public static final MetadataConstant _ENI_FECHA_ASIENTO_REGISTRAL = new MetadataConstant(
      ENI_FECHA_ASIENTO_REGISTRAL, MetadataType.DATE,
      "Fecha y hora de entrada del documento en la entidad registral de origen o de destino.");


  /**
   * Número de registro del documento en el registro general de la entidad de origen o de destino.
   */
  public static final String ENI_NUMERO_ASIENTO_REGISTRAL = "eni:numero_asiento_registral";

  public static final MetadataConstant _ENI_NUMERO_ASIENTO_REGISTRAL = new MetadataConstant(
      ENI_NUMERO_ASIENTO_REGISTRAL, MetadataType.STRING,
      "Número de registro del documento en el registro general de la entidad de origen o de destino.");



  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  // ---------------------  Aspecto eni:transferible --------------------------
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  

  
  
  /**
   * Determina el estado de la fase semi-activa e histórica en la que se encuentra un expediente. Los posibles valores asignables son los siguientes:
   *     - Preingreso.
   *     - Ingresado.
   *     - Pendiente de eliminación total.
   *     - Pendiente de eliminación parcial.
   *     - Pendiente de transferencia.
   *     - Eliminado.
   *     - Transferido.
   *     - Enviado.
   */
  public static final String ENI_ESTADO_ARCHIVO = "eni:estado_archivo";
  
  public static final MetadataConstant _ENI_ESTADO_ARCHIVO = new MetadataConstant(
      ENI_ESTADO_ARCHIVO, MetadataType.STRING,
      "Tipo de asiento registral",
      new HashMap<String, String>(){{
        put("Preingreso","Preingreso");
        put("Ingresado","Ingresado");
        put("Pendiente de eliminación total","Pendiente de eliminación total");
        put("Pendiente de eliminación parcial","Pendiente de eliminación parcial");
        put("Pendiente de transferencia","Pendiente de transferencia");
        put("Eliminado","Eliminado");
        put("Transferido","Transferido");
        put("Enviado","Enviado");
        }});
  


  /**
   * Tipo de entidad que se está describiendo. Los posibles valores asignables son los siguientes:
   *    - Serie
   *    - Expediente.
   *    - Documento simple.
   */
  public static final String ENI_CATEGORIA = "eni:categoria";
  
  public static final MetadataConstant _ENI_CATEGORIA = new MetadataConstant(
      ENI_CATEGORIA, MetadataType.STRING,
      "Tipo de entidad que se está describiendo",
      new HashMap<String, String>(){{
        put("Serie","Serie");
        put("Expediente","Expediente");
        put("Documento simple","Documento simple");
     }});


  /**
   * Clasificación de sensibilidad LOPD. Los posibles valores asignables son los siguientes:
   *  - Básico
   *  - Medio
   *  - Alto
   *  En caso de no estar definido el nivel de seguridad LOPD para la serie documental
   *  o tipo documental del documento o expediente, se establece por defecto el nivel básico.
   */
  public static final String ENI_LOPD = "eni:lopd";
  
  public static final MetadataConstant _ENI_LOPD = new MetadataConstant(
      ENI_LOPD, MetadataType.STRING,
      "Clasificación de sensibilidad LOPD",
      new HashMap<String, String>(){{
        put("Básico","Básico");
        put("Medio","Medio");
        put("Alto","Alto");
     }});


  /**
   * Nivel de confidencialidad de la información contenida en el documento acorde con
   *  el ENS. Los posibles valores asignables son los siguientes:
   *  - Bajo.
   *  - Medio.
   *  - Alto.
   *  En caso de no estar definido el nivel de confidencialidad, según ENS, para 
   *  la serie documental o tipo documental del documento o expediente, se establece
   *   por defecto el nivel bajo.
   */
  public static final String ENI_CONFIDENCIALIDAD = "eni:confidencialidad";
  
  public static final MetadataConstant _ENI_CONFIDENCIALIDAD = new MetadataConstant(
      ENI_CONFIDENCIALIDAD, MetadataType.STRING,
      "Nivel de confidencialidad de la información contenida en el documento acorde con"
      + " el Esquema Nacional de Seguridad.",
      new HashMap<String, String>(){{
        put("Bajo","Bajo");
        put("Medio","Medio");
        put("Alto","Alto");
     }});


  /**
   * El documento se rige por el régimen general de libre acceso o sujeto a alguna
   * de las limitaciones recogidas en la legislación o normativa de aplicación.
   *  Los posibles valores asignables son los siguientes:
   *  - Libre.
   *  - Limitado.
   * En caso de no estar definido el tipo de acceso para la serie documental o tipo
   * documental del documento o expediente, se establece por defecto el tipo libre.
   */
  public static final String ENI_TIPO_ACCESO = "eni:tipo_acceso";
  
  public static final MetadataConstant _ENI_TIPO_ACCESO = new MetadataConstant(
      ENI_TIPO_ACCESO, MetadataType.STRING,
      "El documento se rige por el régimen general de libre acceso o sujeto a alguna"
      + "de las limitaciones recogidas en la legislación o normativa de aplicación",
      new HashMap<String, String>(){{
        put("Libre","Libre");
        put("Parcialmente restringido","Parcialmente restringido");
        put("Restringido","Restringido");
     }});
  

  /**
   * Asigna una codificación a la causa de limitación de acceso que facilita las
   * consiguientes acciones automáticas precisas sobre el documento,
   * siempre que tipo_acceso=”Limitado”.
   * Los posibles valores asignables son los siguientes:
   *  - A (La seguridad nacional).
   *  - B (La defensa).
   *  - C (Las relaciones exteriores).
   *  - D (La seguridad pública).
   *  - E (La prevención, investigación y sanción de los ilícitos penales, administrativos o disciplinarios).
   *  - F (La igualdad de las partes en los procesos judiciales y la tutela judicial efectiva).
   *  - G (Las funciones administrativas de vigilancia, inspección y control).
   *  - H (Los intereses económicos y comerciales).
   *  - I (La política económica y monetaria).
   *  - J (El secreto profesional y la propiedad intelectual e industrial).
   *  - K (La garantía de la confidencialidad o el secreto requerido en procesos de toma de decisión).
   *  - L (La protección del medio ambiente).
   *  - M (Otros).
   */
  public static final String ENI_CODIGO_CAUSA_LIMITACION = "eni:código_causa_limitacion";

  public static final MetadataConstant _ENI_CODIGO_CAUSA_LIMITACION = new MetadataConstant(
      ENI_CODIGO_CAUSA_LIMITACION, MetadataType.STRING,
       "Asigna una codificación a la causa de limitación de acceso que facilita las"
       + " consiguientes acciones automáticas precisas sobre el documento,"
       + " siempre que tipo_acceso=Limitado.",
      new HashMap<String, String>(){{
        put("A", "La seguridad nacional");
        put("B", "La defensa");
        put("C", "Las relaciones exteriores");
        put("D", "La seguridad pública");
        put("E", "La prevención, investigación y sanción de los ilícitos penales, administrativos o disciplinarios");
        put("F", "La igualdad de las partes en los procesos judiciales y la tutela judicial efectiva");
        put("G", "Las funciones administrativas de vigilancia, inspección y control");
        put("H", "Los intereses económicos y comerciales");
        put("I", "La política económica y monetaria");
        put("J", "El secreto profesional y la propiedad intelectual e industrial");
        put("K", "La garantía de la confidencialidad o el secreto requerido en procesos de toma de decisión");
        put("L", "La protección del medio ambiente");
        put("M", "Otros");
     }});
  

  
  /**
   * Referencia a la ley o norma específica que afecta al expediente o documento en 
   * cuanto a su régimen de acceso. usar cuando Tipo de acceso sea Restringido o Parcialmente restringido.
   */
  public static final String ENI_NORMATIVA = "eni:normativa";
  
  public static final MetadataConstant _ENI_NORMATIVA = new MetadataConstant(
      ENI_NORMATIVA, MetadataType.STRING,
      "Referencia a la ley o norma específica que afecta al expediente o documento en"
    + " cuanto a su régimen de acceso. usar cuando Tipo de acceso sea Restringido o Parcialmente restringido");


  /**
   * Fase de archivo correspondiente al ciclo de vida del expediente que se transfiere.
   *  Los posibles valores asignables son los siguientes:
   *  - Archivo activo (DM).
   *  - Archivo semiactivo (RM).
   *  - Archivo histórico (RM).
   */
  public static final String ENI_FASE_ARCHIVO = "eni:fase_archivo";
  
  public static final MetadataConstant _ENI_FASE_ARCHIVO = new MetadataConstant(
      ENI_FASE_ARCHIVO, MetadataType.STRING,
      "Fase de archivo correspondiente al ciclo de vida del expediente que se transfiere.",
      new HashMap<String, String>(){{
        put("Archivo activo","Archivo activo (DM)");
        put("Archivo semiactivo","Archivo semiactivo (RM)");
        put("Archivo histórico","Archivo histórico (RM)");
     }});


  /**
   * Fecha en que se considera finalizado el expediente.
   */
  public static final String ENI_FECHA_FIN_EXP = "eni:fecha_fin_exp";

  public static final MetadataConstant _ENI_FECHA_FIN_EXP = new MetadataConstant(ENI_FECHA_FIN_EXP,
      MetadataType.DATE, "Fecha en que se considera finalizado el expediente");
  

  /**
   * Indica bajo qué condiciones un expediente o documento de acceso libre es reutilizable.
   *   Usado si tipo_acceso= “Libre”.
   */
  public static final String ENI_COND_REUTILIZACION = "eni:cond_reutilizacion";

  public static final MetadataConstant _ENI_COND_REUTILIZACION = new MetadataConstant(
      ENI_COND_REUTILIZACION,
      MetadataType.DATE,
      "Indica bajo qué condiciones un expediente o documento de acceso libre es reutilizable."
      + "Usado si tipo_acceso= “Libre”");
  

  /**
   * Identifica los diferentes valores primarios de expedientes y documentos
   * especificados en la Tabla de valoración. Los posibles valores asignables
   * son los siguientes:
   *  - Administrativo.
   *  - Fiscal.
   *  - Jurídico.
   *  - Otros.
   * En caso de no estar definido el valor para la serie documental o tipo documental
   * del documento o expediente, se establece por defecto el valor “Administrativo”.
   */
  public static final String ENI_TIPO_VALOR = "eni:tipo_valor";
  
  public static final MetadataConstant _ENI_TIPO_VALOR = new MetadataConstant(
      ENI_TIPO_VALOR, MetadataType.STRING,
      "Identifica los diferentes valores primarios de expedientes y documentos especificados en la Tabla de valoración",
      new HashMap<String, String>(){{
        put("Administrativo","Administrativo");
        put("Fiscal","Fiscal");
        put("Jurídico","Jurídico");
        put("Otros","Otros");
     }});



  /**
   * Determinación del plazo de prescripción de los valores primarios (número de días).
   * Requerido para proceso de resellado, una vez se cumpla el plazo establecido para el
   * valor primario de un documento, este no volverá a ser resellado.
   */
  public static final String ENI_PLAZO = "eni:plazo";
  
  public static final MetadataConstant _ENI_PLAZO = new MetadataConstant(
      ENI_PLAZO, MetadataType.INTEGER,
      "Determinación del plazo de prescripción de los valores primarios (número de días)."
      + " Requerido para proceso de resellado, una vez se cumpla el plazo establecido para el"
      + " valor primario de un documento, este no volverá a ser resellado"
      );


  /**
   * Determinación de la existencia de valores secundarios en los documentos y
   * expedientes cuya consecuencia será la conservación permanente. Los posibles
   *  valores asignables son los siguientes:
   *  - Sí
   *  - No
   *  - Sin cobertura de calificación
   */
  public static final String ENI_VALOR_SECUNDARIO = "eni:valor_secundario";
  
  public static final MetadataConstant _ENI_VALOR_SECUNDARIO = new MetadataConstant(
      ENI_VALOR_SECUNDARIO, MetadataType.STRING,
      "Determinación de la existencia de valores secundarios en los documentos y"
       + "expedientes cuya consecuencia será la conservación permanente",
      new HashMap<String, String>(){{
        put("Sí","Sí");
        put("No","No");
        put("Sin cobertura de calificación","Sin cobertura de calificación");
     }});


  /**
   * Decisión emitida por la autoridad calificadora que debe aplicarse sobre los
   * documentos a lo largo de su ciclo de vida y una vez realizada su valoración.
   * Los posibles valores asignables son los siguientes:
   *  - CP (Conservación permanente).
   *  - EP (Eliminación parcial).
   *  - ET (Eliminación total).
   *  - PD (Pendiente de dictamen).
   * En caso de no estar definida la valoración para la serie documental o tipo
   * documental del documento o expediente, se establece por defecto el valor “PD”.
   */
  public static final String ENI_TIPO_DICTAMEN = "eni:tipo_dictamen";

  public static final MetadataConstant _ENI_TIPO_DICTAMEN = new MetadataConstant(
      ENI_TIPO_DICTAMEN, MetadataType.STRING,
      "Decisión emitida por la autoridad calificadora que debe aplicarse sobre los"
       + "documentos a lo largo de su ciclo de vida y una vez realizada su valoración",
      new HashMap<String, String>(){{
        put("CP", "Conservación permanente");
        put("EP", "Eliminación parcial");
        put("ET", "Eliminación total");
        put("PD", "Pendiente de dictamen");
     }});

  /**
   * Acción concreta que se aplica al documento en base al dictamen adoptado
   * por una autoridad calificadora.
   */
  public static final String ENI_ACCION_DICTAMINADA = "eni:accion_dictaminada";

  public static final MetadataConstant _ENI_ACCION_DICTAMINADA = new MetadataConstant(
      ENI_ACCION_DICTAMINADA, MetadataType.STRING,
      "Acción concreta que se aplica al documento en base al dictamen adoptado"
      + " por una autoridad calificadora");

  /**
   * Plazo en el que se tiene que ejecutar la acción concreta que se aplica al documento.
   * Requerido para proceso de expurgo, si el tipo de dictamen es =”EP” o =”ET”.
   */
  public static final String ENI_PLAZO_ACCION_DICTAMINADA = "eni:plazo_accion_dictaminada";

  public static final MetadataConstant _ENI_PLAZO_ACCION_DICTAMINADA = new MetadataConstant(
      ENI_PLAZO_ACCION_DICTAMINADA, MetadataType.STRING,
      "Plazo en el que se tiene que ejecutar la acción concreta que se aplica al documento."
      + " Requerido para proceso de expurgo, si el tipo de dictamen es =”EP” o =”ET”");

  /**
   * Calificación de un documento como esencial/vital. 
   * En caso de no estar calificada la serie documental o tipo documental del documento
   * o expediente, se establece por defecto el valor FALSE.
   */
  public static final String ENI_DOCUMENTO_VITAL = "eni:documento_vital";
  
  public static final MetadataConstant _ENI_DOCUMENTO_VITAL = new MetadataConstant(
      ENI_DOCUMENTO_VITAL, MetadataType.BOOLEAN,
      "Calificación de un documento como esencial/vital");



  /**
   * Indicador en lenguaje natural que identifica de forma unívoca la clase 
   * asignada dentro de un cuadro de clasificación.
   */
  public static final String ENI_DENOMINACION_CLASE = "eni:denominación_clase";

  public static final MetadataConstant _ENI_DENOMINACION_CLASE = new MetadataConstant(
      ENI_DENOMINACION_CLASE, MetadataType.STRING,
      "Indicador en lenguaje natural que identifica de forma unívoca la clase "
      + "asignada dentro de un cuadro de clasificación.");
  

  /**
   * Decisión emitida por la autoridad calificadora que debe aplicarse sobre los
   * documentos a lo largo de su ciclo de vida y una vez realizada su valoración.
   *  Los posibles valores asignables son los siguientes:
   *   - SIA.
   *   - Funcional (Por defecto).
   */
  public static final String ENI_TIPO_CLASIFICACION= "eni:tipo_clasificacion";
  
  public static final MetadataConstant _ENI_TIPO_CLASIFICACION = new MetadataConstant(
      ENI_TIPO_CLASIFICACION, MetadataType.STRING,
      "Decisión emitida por la autoridad calificadora que debe aplicarse sobre los"
      + " documentos a lo largo de su ciclo de vida y una vez realizada su valoración.",
      new HashMap<String, String>(){{
        put("SIA", "SIA");
        put("Funcional", "Funcional");
     }});

  
  
  
  
  
  public static final List<String> getAllMetadatas() {
    
    synchronized (metadataInfoMap) {
      if (metadatasList == null) {
        internalCompute();
      }
    }

    return metadatasList;
    
  }
  
  
  public static final Map<String, MetadataConstant> getAllMetadatasInfo() {
    synchronized (metadataInfoMap) {
      if (metadatasList == null) {
        internalCompute();
      }
    }
    return metadataInfoMap;
  }
  
  
  
  private static List<String> metadatasList = null;
  
  private static final Map<String, MetadataConstant> metadataInfoMap = new HashMap<String, MetadataConstants.MetadataConstant>();
  
  private static void internalCompute() {
    Field[] declaredFields = MetadataConstants.class.getDeclaredFields();

    metadatasList = new ArrayList<String>();
    for (Field field : declaredFields) {
      //System.out.println(field.getName() + " --> " + field.getType());
      if (java.lang.reflect.Modifier.isStatic(field.getModifiers())
          && java.lang.reflect.Modifier.isPublic(field.getModifiers())) {
        // staticFields.add(field);
        String name = field.getName();

        if (name.startsWith("_")) {
          if (MetadataConstant.class.equals(field.getType())) {

            try {
              MetadataConstant mdc = (MetadataConstant) field.get(null);

              metadataInfoMap.put(mdc.getKey(), mdc);

              // TODO ALIASES

            } catch (IllegalArgumentException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (IllegalAccessException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }

          }

        } else {

          String value;
          try {
            value = (String) field.get(null);
            metadatasList.add(value);
          } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

        }
      }
    }

    // trobar no donats d'alta

    Set<String> metasWithInfo = metadataInfoMap.keySet();

    for (String meta : metadatasList) {
      if (!metasWithInfo.contains(meta)) {
        System.err.println("ERROR EN CLAU " + meta);
      }
    }

  }
  
  
  
  /**
   * 
   * @return Retorna un array de resultados segun el indice del metadato:
   *               - true: correcto
   *               - false: incorrecto
   *               - null: clave del metadato desconocida
   */
  public static Boolean[] validate(Metadata[] toValidateArray) {
    
    if (toValidateArray == null) {
      return null;
    }
    
    Map<String, MetadataConstant> allmetas = getAllMetadatasInfo();
    
    Boolean[] result = new Boolean[toValidateArray.length];
    for (int i = 0; i < toValidateArray.length; i++) {
      Metadata meta = toValidateArray[i];
      
      
      String value = meta.getValue(); 
      
      if (value == null) {
        value="";
      } 
      
      String key = meta.getKey();

      MetadataConstant info = allmetas.get(key);
      
      if (info == null) {
        result[i] = null;
      } else {
        Map<String,String> allowed = info.getAllowedValues(); 
        if (allowed != null) {
          result[i] = allowed.containsKey(value.trim());
        } else {
          String regex = info.getPatternRegEx();
          if (regex != null) {
            result[i] =  Pattern.matches(regex, value);
          } else {
            // Mirarem segons el tipus de Metadata
            try {
              Metadata.checkMetadata(meta);
              result[i] = true;
            } catch(Exception e) {
              result[i] = false;
            }
          }
        }
      }
    }
    
    return result;
  }
  
  
  
  
  public static void main(String[] args) {

    
    internalCompute();
    
    System.out.println(" list " + metadatasList.size());
    
    System.out.println(" map  " + metadataInfoMap.size());
    

    //final String regex = _ENI_ID.getPatternRegEx();
    //final String string = "ES_E00003901_2011_MPTAP000000000000000020110512E";
    
    Metadata eniID = new Metadata(ENI_ID, "ES_E00003901_2011_MPTAP000000000000000020110512E");
    
    
    
    
    //final String regex = _ENI_RESOLUCION.getPatternRegEx();
    //final String string = "200 x300ppp ";
    Metadata res = new Metadata(ENI_RESOLUCION, "200 x300ppp ");
    
    Metadata res2 = new Metadata(ENI_RESOLUCION, "200ppp ");
    
    
    //final String regex = _ENI_IDIOMA.getPatternRegEx();
    //final String string = "ca-ES";
    Metadata idioma = new Metadata(ENI_IDIOMA, "ca-ES");
    
    Metadata idioma2 = new Metadata(ENI_IDIOMA, "ca_ES");
    
    Metadata desconeguda = new Metadata("desconeguda", "xxxxxx");
    
    
    Metadata[] metas = new Metadata[] { eniID, res, res2, idioma, idioma2, desconeguda };
    Boolean[] results = validate(metas);
    for (int i = 0; i < metas.length; i++) {
      System.out.println(metas[i].getKey() + "[" + metas[i].getValue() + "] => " + results[i]);
    }
    
    

    
    /*

    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(string);

    
    System.out.println(" MATCH = " + Pattern.matches(regex, string));
    

    
    while (matcher.find()) {
        //System.out.println("Full match: " + matcher.group(0));
        for (int i = 0; i <= matcher.groupCount(); i++) {
            System.out.println("Group " + i + ": " + matcher.group(i));
        }
    }
    */
  }
  
  
}
