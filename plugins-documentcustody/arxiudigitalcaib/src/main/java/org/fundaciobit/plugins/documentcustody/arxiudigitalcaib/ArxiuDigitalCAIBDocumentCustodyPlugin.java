package org.fundaciobit.plugins.documentcustody.arxiudigitalcaib;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.documentcustody.api.AbstractDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.api.AnnexCustody;
import org.fundaciobit.plugins.documentcustody.api.CustodyException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.api.NotSupportedCustodyException;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;
import org.fundaciobit.plugins.utils.Base64;
import org.fundaciobit.plugins.utils.Metadata;
import org.fundaciobit.plugins.utils.MetadataConstants;
import org.fundaciobit.plugins.utils.MetadataFormatException;
import org.fundaciobit.plugins.utils.XTrustProvider;

import es.caib.arxiudigital.apirest.ApiArchivoDigital;
import es.caib.arxiudigital.apirest.CSGD.entidades.comunes.ResultData;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateDocumentResult;
import es.caib.arxiudigital.apirest.CSGD.entidades.resultados.CreateDraftDocumentResult;
import es.caib.arxiudigital.apirest.constantes.Aspectos;
import es.caib.arxiudigital.apirest.constantes.CodigosResultadoPeticion;
import es.caib.arxiudigital.apirest.constantes.FormatosFichero;
import es.caib.arxiudigital.apirest.constantes.TiposFirma;
import es.caib.arxiudigital.apirest.constantes.TiposObjetoSGD;
import es.caib.arxiudigital.apirest.facade.pojos.CabeceraPeticion;
import es.caib.arxiudigital.apirest.facade.pojos.Directorio;
import es.caib.arxiudigital.apirest.facade.pojos.Documento;
import es.caib.arxiudigital.apirest.facade.pojos.Expediente;
import es.caib.arxiudigital.apirest.facade.pojos.FiltroBusquedaFacilExpedientes;
import es.caib.arxiudigital.apirest.facade.pojos.FirmaDocumento;
import es.caib.arxiudigital.apirest.facade.pojos.Nodo;
import es.caib.arxiudigital.apirest.facade.resultados.Resultado;
import es.caib.arxiudigital.apirest.facade.resultados.ResultadoBusqueda;
import es.caib.arxiudigital.apirest.facade.resultados.ResultadoSimple;
import es.caib.arxiudigital.apirest.utils.UtilidadesFechas;

/**
 * Implementació del plugin de custodia documental que guarda els documents dins
 * de l'Arxiu Digital de la CAIB
 * 
 * @author anadal
 */
public class ArxiuDigitalCAIBDocumentCustodyPlugin extends AbstractPluginProperties implements
    IDocumentCustodyPlugin {

  protected final Logger log = Logger.getLogger(getClass());

  protected static final char SEPARATOR_EXPEDIENT_CARPETA = '#';

  public static final String FORMATO_UTF8 = "UTF-8";

  public static final String NOM_DOCUMENT_ELECTRONIC = "DOCUMENT_ELECTRONIC_CAIB";

  public static final String NOM_INFO_DOCUMENT_ELECTRONIC = "INFO_DOCUMENT_ELECTRONIC_CAIB.properties";

  public static final String NOM_METADADES = "METADADES.properties";

  public static final String FILE_INFO_CSV = "csv";

  public static final String FILE_INFO_NOM = "nom";
  public static final String FILE_INFO_TAMANY = "tamany";
  public static final String FILE_INFO_MIME = "mime";

  public static final String FILE_INFO_SIGNATURE_TYPE = "signtype";

  /**
   * true if data contains original document (attached). false if data does not
   * contain original document (dettached). null can not obtain this information
   * or is not necessary
   */
  public static final String FILE_INFO_SIGNATURE_ATTACHED_DOCUMENT = "attachedDocument";

  public static final String FILE_INFO_BASE_DOCUMENT = "document.";

  public static final String FILE_INFO_BASE_FIRMA = "firma.";

  public static final String ARXIUDIGITALCAIB_PROPERTY_BASE = DOCUMENTCUSTODY_BASE_PROPERTY
      + "arxiudigitalcaib.";
  
  public static final Map<String, TiposFirma> TIPOFIRMA_SIMPLE = new HashMap<String, TiposFirma>();
  
  public static final Map<String, TiposFirma> TIPOFIRMA_COMPLEXE = new HashMap<String, TiposFirma>();
  
  public static final Map<String, FormatosFichero> FORMATS_BY_EXTENSION = new HashMap<String, FormatosFichero>();
  
  
  static {
    
    TIPOFIRMA_SIMPLE.put(SignatureCustody.PADES_SIGNATURE, TiposFirma.PADES);

    /**
     * true if signature contains original document (attached).
     * false if signature does not contain original document (dettached).
     * null can not obtain this information or is not necessary
     */
    
    TIPOFIRMA_COMPLEXE.put(SignatureCustody.CADES_SIGNATURE + "_null" ,  TiposFirma.CADES_ATTACHED);
    TIPOFIRMA_COMPLEXE.put(SignatureCustody.CADES_SIGNATURE + "_true" ,  TiposFirma.CADES_ATTACHED);
    TIPOFIRMA_COMPLEXE.put(SignatureCustody.CADES_SIGNATURE + "_false" ,  TiposFirma.CADES_DETACHED);
    
    TIPOFIRMA_COMPLEXE.put(SignatureCustody.XADES_SIGNATURE + "_true" , TiposFirma.XADES_ENVELOPED);

    
    FORMATS_BY_EXTENSION.put("wfs", FormatosFichero.WFS);
    FORMATS_BY_EXTENSION.put("wms", FormatosFichero.WMS);
    FORMATS_BY_EXTENSION.put("gzip", FormatosFichero.GZIP);
    FORMATS_BY_EXTENSION.put("zip", FormatosFichero.ZIP);
    FORMATS_BY_EXTENSION.put("avi", FormatosFichero.AVI);
    FORMATS_BY_EXTENSION.put("mp4a", FormatosFichero.MP4A);
    FORMATS_BY_EXTENSION.put("csv", FormatosFichero.CSV);
    FORMATS_BY_EXTENSION.put("html", FormatosFichero.HTML);
    FORMATS_BY_EXTENSION.put("htm", FormatosFichero.HTML);
    FORMATS_BY_EXTENSION.put("css", FormatosFichero.CSS);
    FORMATS_BY_EXTENSION.put("jpeg", FormatosFichero.JPEG);
    FORMATS_BY_EXTENSION.put("jpg", FormatosFichero.JPEG);
    FORMATS_BY_EXTENSION.put("mhtml", FormatosFichero.MHTML);
    FORMATS_BY_EXTENSION.put("oasis12", FormatosFichero.OASIS12);
    FORMATS_BY_EXTENSION.put("soxml", FormatosFichero.SOXML);
    FORMATS_BY_EXTENSION.put("pdf", FormatosFichero.PDF);
    //FORMATS_BY_EXTENSION.put("pdfa", FormatosFichero.PDFA);
    FORMATS_BY_EXTENSION.put("png", FormatosFichero.PNG);
    FORMATS_BY_EXTENSION.put("rtf", FormatosFichero.RTF);
    FORMATS_BY_EXTENSION.put("svg", FormatosFichero.SVG);
    FORMATS_BY_EXTENSION.put("tiff", FormatosFichero.TIFF);
    FORMATS_BY_EXTENSION.put("txt", FormatosFichero.TXT);
    FORMATS_BY_EXTENSION.put("xhtml", FormatosFichero.XHTML);
    FORMATS_BY_EXTENSION.put("mp3", FormatosFichero.MP3);
    FORMATS_BY_EXTENSION.put("ogg", FormatosFichero.OGG);
    FORMATS_BY_EXTENSION.put("mp4v", FormatosFichero.MP4V);
    FORMATS_BY_EXTENSION.put("webm", FormatosFichero.WEBM);
    
    
  }


  // ----------------------------------------------------------------------------
  // ---------------------------- CONSTRUCTORS -----------------------------
  // ----------------------------------------------------------------------------

  /**
   * 
   */
  public ArxiuDigitalCAIBDocumentCustodyPlugin() {
    super();
  }

  /**
   * @param propertyKeyBase
   * @param properties
   */
  public ArxiuDigitalCAIBDocumentCustodyPlugin(String propertyKeyBase, Properties properties) {
    super(propertyKeyBase, properties);
  }

  /**
   * @param propertyKeyBase
   */
  public ArxiuDigitalCAIBDocumentCustodyPlugin(String propertyKeyBase) {
    super(propertyKeyBase);
  }

  // ----------------------------------------------------------------------------
  // ---------------------------- P R O P I E T A T S -------------------------
  // ----------------------------------------------------------------------------

  private String getPropertyBase() {
    return ARXIUDIGITALCAIB_PROPERTY_BASE;
  }

  public String getPropertyNomExpedientEL() throws Exception {
    return getPropertyRequired(ARXIUDIGITALCAIB_PROPERTY_BASE + "nom_expedient_EL");
  }

  public String getPropertyNomCarpetaEL() throws Exception {
    return getProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "nom_carpeta_EL");
  }

  public String getPropertySerieDocumentalEL() throws Exception {
    return getPropertyRequired(ARXIUDIGITALCAIB_PROPERTY_BASE + "serie_documental_EL");
  }
  
  public boolean isPropertyCreateDraft() {
    String valueStr = getProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "createDraft");
    if (valueStr == null || valueStr.trim().length() == 0) {
      return true;
    }
    
    if ("false".equals(valueStr.toLowerCase())) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Separats per comma
   * 
   * @return
   * @throws Exception
   */
  public String getPropertyOrgansEL() throws Exception {
    return getPropertyRequired(ARXIUDIGITALCAIB_PROPERTY_BASE + "organs_EL");
  }

  public String getPropertyCodiProcedimentEL() throws Exception {
    return getPropertyRequired(ARXIUDIGITALCAIB_PROPERTY_BASE + "codi_procediment_EL");
  }

  public String getPropertyDataCreacioEL() throws Exception {
    return getProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "data_creacio_EL");
  }

  public String getPropertyEstatElaboracioEL() throws Exception {
    return getPropertyRequired(ARXIUDIGITALCAIB_PROPERTY_BASE + "estat_elaboracio_EL");
  }

  public String getPropertyTipusDocumentalEL() throws Exception {
    return getPropertyRequired(ARXIUDIGITALCAIB_PROPERTY_BASE + "tipus_documental_EL");
  }

  /**
   * ADMINISTRACION ("1"), CIUDADANO ("0");
   * 
   * @return
   * @throws Exception
   */
  public String getPropertyOrigenDocumentEL() throws Exception {
    return getPropertyRequired(ARXIUDIGITALCAIB_PROPERTY_BASE + "origen_document_EL");
  }

  public String getPropertyURL() throws Exception {
    return getPropertyRequired(ARXIUDIGITALCAIB_PROPERTY_BASE + "connexio.url");
  }

  public String getPropertyCodiAplicacio() throws Exception {
    return getPropertyRequired(ARXIUDIGITALCAIB_PROPERTY_BASE + "connexio.codi_aplicacio");
  }

  public String getPropertyOrganitzacio() throws Exception {
    return getPropertyRequired(ARXIUDIGITALCAIB_PROPERTY_BASE + "connexio.organitzacio");
  }

  public String getPropertyLoginUsername() throws Exception {
    return getPropertyRequired(ARXIUDIGITALCAIB_PROPERTY_BASE + "connexio.login.username");
  }

  public String getPropertyLoginPassword() throws Exception {
    return getPropertyRequired(ARXIUDIGITALCAIB_PROPERTY_BASE + "connexio.login.password");
  }

  // Ciutada - Nom.  Opcional
  public String getPropertySolicitantNomEL() throws Exception {
    return getProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "connexio.solicitant.nom_EL");
  }

  // Ciutada - NIF. Opcional
  public String getPropertySolicitantIdentificadorAdministratiuEL() throws Exception {
    return getProperty(ARXIUDIGITALCAIB_PROPERTY_BASE
        + "connexio.solicitant.identificador_administratiu_EL");
  }

  // Funcionari o client de l'aplicació - Username. Opcional
  public String getPropertyUsuariUsernameEL() throws Exception {
    return getProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "connexio.usuari.username_EL");
  }

  // Funcionari o client de l'aplicació - NIF. Opcional
  public String getPropertyUsuariIdentificadorAdministratiuEL() throws Exception {
    return getProperty(ARXIUDIGITALCAIB_PROPERTY_BASE
        + "connexio.usuari.identificador_administratiu_EL");
  }

  // Nom de Procediment (opcional)
  public String getPropertyNomProcedimentEL() throws Exception {
    return getProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "connexio.nom_procediment_EL");
  }

  public boolean isPropertyIgnoreServerCeriticates() throws Exception {
    String ignoreStr = getProperty(ARXIUDIGITALCAIB_PROPERTY_BASE
        + "connexio.ignore_server_certificates");
    if (ignoreStr == null || ignoreStr.trim().length() == 0) {
      return false;
    } else {
      return "true".equals(ignoreStr.trim());
    }
  }
  
  private Boolean isDebugValue = null;
  
  public boolean isDebug() {
    if (isDebugValue == null) {
      isDebugValue = log.isDebugEnabled() || "true".equals(getProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "debug"));
    }
    return isDebugValue;
  }
  

  // ----------------------------------------------------------------------------
  // ----------------------- METODES PRINCIPALS -------------------------------
  // ----------------------------------------------------------------------------

  @Override
  public String reserveCustodyID(Map<String, Object> custodyParameters)
      throws CustodyException {
    // Crearem una estructura
    // EXPEDIENT => CARPETA => DOCUMENTS
    final boolean debug = isDebug();
    try {
      final String nomExpedient = processEL(getPropertyNomExpedientEL(), custodyParameters);
      final String nomCarpeta = processELNullIfEmpty(getPropertyNomCarpetaEL(),
          custodyParameters);

      if (debug) {
        log.info("NomExpedient: " + nomExpedient);
        log.info("NomCarpeta: " + nomCarpeta);
      }

      ApiArchivoDigital api = getApiArxiu(custodyParameters);

      FiltroBusquedaFacilExpedientes filtrosRequeridos = new FiltroBusquedaFacilExpedientes();
      filtrosRequeridos.setName(nomExpedient);
      filtrosRequeridos.setAppName(getPropertyCodiAplicacio());      
      String serieDocumental = processEL(getPropertySerieDocumentalEL(), custodyParameters);
      filtrosRequeridos.setDocSeries(serieDocumental);
            
      ResultadoBusqueda<Expediente> res = api.busquedaFacilExpedientes(filtrosRequeridos,
          null, 1);
      String carpetaID = null;

      if (hiHaErrorEnCerca(res.getCodigoResultado())) {
        throw new CustodyException("Error Consultant si Expedient " + nomExpedient
            + " existeix: " + res.getCodigoResultado() + "-" + res.getMsjResultado());
      }
      


      List<Expediente> llista2 = res.getListaResultado();
      
      
      Expediente expedientCercat = null;
      if (llista2.size() == 0) {
        expedientCercat = null;
      } else {
        // TODO XYZ ZZZ la cerca es fa del nom parescut al fitper, per exemple si cerques
        // "Registre_20" et pot trobar Registre_20, Registre_200, Registre_202, ... 
        int countTrobats = 0;
        for (Expediente expediente : llista2) {
          if (nomExpedient.equals(expediente.getName())) {
            countTrobats++;
            if (countTrobats > 1) {
              log.error(" S'ha trobat coincidencia multiple " + expediente.getName() + " (" 
                + expediente.getId() + ") per la cerca de nomExpedient " + nomExpedient + ")");
            } else { 
              expedientCercat = expediente;
            }
          }
        }
        
        if(countTrobats == 0) {
          expedientCercat=null;
        } else if (countTrobats == 1) {
          // expedientCercat ja conté el valor
        } else if (countTrobats > 1) {
            // Hi ha multiple instancies que s'ajusten. No se quin triar
            String msg = "S'ha trobat coincidencia multiple " + expedientCercat.getName() + " (" 
                + expedientCercat.getId() + ") per la cerca de nomExpedient " + nomExpedient 
                + "). Veure logs per la resta de coincidències.";
            log.error(msg);
            throw new CustodyException(msg);
        }
      }

      
      
      
      //log.info("Creacio Expedient::Llistat.SIZE: " + llista.size());
      String expedientID;
      if (expedientCercat == null) {
        // Cream l'expedient
        Map<String, Object> llistaMetadades = getMetadadesPerExpedient(custodyParameters);

        Expediente miExpediente = new Expediente();

        miExpediente.expedienteParaCrear(true);
        miExpediente.setName(nomExpedient);
        miExpediente.setMetadataCollection(llistaMetadades);

        Resultado<Expediente> resultado = api.crearExpediente(miExpediente, true);

        if (hiHaError(resultado.getCodigoResultado())) {
          throw new CustodyException("Error creant expedient " + nomExpedient + ": "
              + res.getCodigoResultado() + "-" + res.getMsjResultado());
        }

        expedientID = resultado.getElementoDevuelto().getId();

        if (debug) {
          log.info("Creacio Expedient:: CREAT: " + expedientID);
        }

      } else {
        

          
        expedientID = expedientCercat.getId();

        if (debug) {
          log.info("Creacio Expedient:: JA EXISTEIX: " + expedientID);
        }

        if (nomCarpeta != null) {

          List<Nodo> nodos = expedientCercat.getChilds();
          if (nodos == null || nodos.size() == 0) {
            if (debug) {
              log.info("Creacio Expedient:: Fills Expedients:  NO EN TE !!!!!");
            }
          } else {
            for (Nodo nodo : nodos) {
              // TiposObjetoSGD.DIRECTORIO
              if (debug) {
                log.info("Creacio Expedient:: Fills Expedients: " + nodo.getName()
                  + " [" + nodo.getType().getValue() + "]");
              }
              if (nomCarpeta.equals(nodo.getName())
                  && TiposObjetoSGD.DIRECTORIO.equals(nodo.getType())) {
                carpetaID = nodo.getId();
                break;
              }

            }
          }
        }

      }


      // (3) === CREAM CARPETA

      // (3.1) Si carpeta es null llavors no fa falta crear la carpeta

      String custodyID;

      if (nomCarpeta == null) {

        custodyID = expedientID;
      } else {

        if (carpetaID == null) {

          Resultado<Directorio> carpeta = api.crearDirectorio(nomCarpeta, expedientID);

          if (hiHaError(carpeta.getCodigoResultado())) {
            throw new CustodyException("Error creant carpeta " + nomCarpeta + ": "
                + carpeta.getCodigoResultado() + "-" + carpeta.getMsjResultado());
          }

          Directorio directorio = carpeta.getElementoDevuelto();

          carpetaID = directorio.getId();
          if (debug) {
            log.info("Creacio Carpeta:: CREAT: " + carpetaID);
          }

        } else {
          // TODO Això és correcte ????? Hauria de ser únic per cada petició
          // TODO fer proves llançant error aquí
          if (debug) {
            log.info("Creacio Carpeta:: JA EXISTEIX: " + carpetaID);
          }
        }

        // Retornam Expedient#Carpeta
        custodyID = expedientID + SEPARATOR_EXPEDIENT_CARPETA + carpetaID;
      }

      // Afegir CSV al document de informació per a que després, ho pugui
      // recuperar durant l'alta del document/firma
      String csv = api.generarCSV();
      if (debug) {
        log.info("CODI SEGUR GENERAT = " + csv);
      }
      Properties properties = new Properties();
      properties.setProperty(FILE_INFO_CSV, csv);

      createSimpleDoc(api, NOM_INFO_DOCUMENT_ELECTRONIC, properties, custodyID, null);

      // Crear fitxer buit de METADADES
      createSimpleDoc(api, NOM_METADADES, new Properties(), custodyID, null);

      return custodyID;

    } catch (CustodyException ce) {
      throw ce;
    } catch (Exception e) {

      throw new CustodyException("Error reservant CustodyID: " + e.getMessage(), e);

    }

  }

  @Override
  public String getValidationUrl(String custodyID, Map<String, Object> custodyParameters)
      throws CustodyException {
    final String baseUrl = null;
    String baseUrlEL = getProperty(getPropertyBase() + "baseurl_EL");

    String hashPassword = getProperty(getPropertyBase()
        + AbstractDocumentCustodyPlugin.ABSTRACT_HASH_PASSWORD, "");

    // Valid values MD2, MD5, SHA,SHA-256,SHA-384,SHA-512
    String hashAlgorithm = getProperty(getPropertyBase()
        + AbstractDocumentCustodyPlugin.ABSTRACT_HASH_ALGORITHM, "MD5");

    Map<String, Object> custodyParametersExtended = new HashMap<String, Object>();

    // Recuperam el CSV
    custodyParametersExtended.put("csv", getCSV(custodyID, custodyParameters));
    custodyParametersExtended.putAll(custodyParameters);

    return AbstractDocumentCustodyPlugin.getValidationUrlStatic(custodyID,
        custodyParametersExtended, baseUrl, baseUrlEL, hashAlgorithm, hashPassword, log);
  }

  @Override
  public String getSpecialValue(String custodyID, Map<String, Object> custodyParameters)
      throws CustodyException {
    String specialValue = getProperty(getPropertyBase() + "specialValue_EL");
    if (specialValue == null || specialValue.trim().length() == 0) {
      return custodyID;
    } else {

      Map<String, Object> custodyParametersExtended = new HashMap<String, Object>();

      // Recuperam el CSV
      custodyParametersExtended.put("csv", getCSV(custodyID, custodyParameters));
      custodyParametersExtended.putAll(custodyParameters);

      return AbstractDocumentCustodyPlugin.processExpressionLanguage(specialValue,
          custodyParameters);
    }
  }

  @Override
  public void deleteCustody(String custodyID) throws CustodyException,
      NotSupportedCustodyException {
    ExpedientCarpeta ec = decodeCustodyID(custodyID);

    try {
      ApiArchivoDigital api = getApiArxiu(null);
      if (ec.carpetaID == null) {
        api.eliminarExpediente(ec.expedientID);
      } else {
        ResultadoSimple rs = api.eliminarDirectorio(ec.carpetaID);

        if (hiHaError(rs.getCodigoResultado())) {

          log.warn("Error Esborrant Carpeta " + ec.carpetaID + ": "
              + rs.getCodigoResultado() + "-" + rs.getMsjResultado());

        }

        // TODO  Eliminar expedient quan aquest no tengui fills ?????
      }
    } catch (Exception e) {
      throw new CustodyException("Error esborrant expedient amb uuid " + ec.expedientID + ": "
          + e.getMessage(), e);
    }

  }

  @Override
  public boolean supportsDeleteCustody() {
    return true;
  }

  @Override
  public void saveAll(String custodyID, Map<String, Object> custodyParameters,
      DocumentCustody documentCustody, SignatureCustody signatureCustody, Metadata[] metadata)
      throws CustodyException, NotSupportedCustodyException, MetadataFormatException {

    final boolean overWriteDocument = true;
    final boolean overWriteSignature = true;
    final boolean overWriteMetadata = true;
    createUpdateAll(custodyID, custodyParameters, documentCustody, overWriteDocument,
        signatureCustody, overWriteSignature, metadata, overWriteMetadata);
  }

  protected void createUpdateAll(String custodyID, Map<String, Object> custodyParameters,
      DocumentCustody documentCustody, boolean overWriteDocument,
      SignatureCustody signatureCustody, boolean overWriteSignature, Metadata[] metadatas,
      boolean overWriteMetadatas) throws CustodyException {
    final boolean debug = isDebug();
    try {

      // Miram si el Document Electrònic Existeix

      ExpedientCarpeta ec = decodeCustodyID(custodyID);

      ApiArchivoDigital apiArxiu = getApiArxiu(custodyParameters);

      // Cercam si hi ha un document
      Resultado<Expediente> expedient = apiArxiu.obtenerExpediente(ec.expedientID);
      // TODO Falta Checks
      Map<String, Nodo> nodosByName;
      nodosByName = getNodosByCustodyID(apiArxiu, ec, expedient);

      String documentElectronicID = null;
      Nodo nodo = nodosByName.get(NOM_DOCUMENT_ELECTRONIC);
      if (nodo != null && TiposObjetoSGD.DOCUMENTO.equals(nodo.getType())) {
        documentElectronicID = nodo.getId();
      }

      // Recuperar INFO DOCUMENTS (Sempre ha d'existir ja que es crea en
      // reserveCustodyID()
      Nodo nodoInfo = nodosByName.get(NOM_INFO_DOCUMENT_ELECTRONIC);

      Properties infoDocSign = readPropertiesFromSimpleDoc(apiArxiu, nodoInfo.getId());

      // =================================== INICIALITZACIO
      Documento doc;
      Map<String, Object> metadataCollection;
      if (documentElectronicID == null) {

        // CREAR DOCUMENT

        doc = new Documento();
        // String idNuevoDocumento = SENSE_VALOR;

        // Configuració paràmetres document
        List<Aspectos> aspects = new ArrayList<Aspectos>();
        aspects.add(Aspectos.INTEROPERABLE);
        aspects.add(Aspectos.TRANSFERIBLE);
        doc.setId(null);
        doc.setName(NOM_DOCUMENT_ELECTRONIC);
        doc.setAspects(aspects);

        doc.setEncoding(FORMATO_UTF8);

        // **** metadades per document i per signature
        metadataCollection = getMetadadesPerDocument(custodyParameters, signatureCustody);

        // EL CSV s'ha generat en la reserva i s'ha de guardat en 
        // el fitxer de INFO. S'ha de recuperar del fitxer de Informacio.
        String csv = infoDocSign.getProperty(FILE_INFO_CSV);

        if (debug) {
          log.info("CODI SEGUR RECUPERAT = " + csv);
        }
        metadataCollection.put(MetadataConstants.ENI_CSV, csv);
        // TODO XYZ ZZZ  QUE és AIXÒ !!!!!
        // metadataCollection.put(MetadatosDocumento.DEF_CSV, "Sense definir");

        doc.setMetadataCollection(metadataCollection);

      } else {

        // ACTUALITZAR DOCUMENT
        final boolean retrieveContent = true;
        // TODO Comprovar resultat operació
        doc = apiArxiu.obtenerDocumento(documentElectronicID, retrieveContent)
            .getElementoDevuelto();

        metadataCollection = doc.getMetadataCollection();

        if (custodyParameters != null) {
          Map<String, Object> newMetadataCollection = getMetadadesPerDocument(
              custodyParameters, signatureCustody);

          metadataCollection.putAll(newMetadataCollection);

        }

      }

      // ======================== CODI COMU

      if (debug) {
        for (String key : metadataCollection.keySet()) {
          log.info("Metadata Document [" + key + "] => " + metadataCollection.get(key));
        }
      }

      if (overWriteDocument) {
        if (documentCustody == null) {

          doc.setContent(null);
          doc.setMimetype(null);

          infoDocSign.remove(FILE_INFO_BASE_DOCUMENT + FILE_INFO_NOM);
          infoDocSign.remove(FILE_INFO_BASE_DOCUMENT + FILE_INFO_TAMANY);
          infoDocSign.remove(FILE_INFO_BASE_DOCUMENT + FILE_INFO_MIME);

        } else {
          byte[] data = documentCustody.getData();
          String contenidoDocumento = Base64.encode(data);
          doc.setContent(contenidoDocumento);
          String mimetype = documentCustody.getMime(); 
          doc.setMimetype(mimetype);

          infoDocSign.setProperty(FILE_INFO_BASE_DOCUMENT + FILE_INFO_NOM,
              documentCustody.getName());
          infoDocSign.setProperty(FILE_INFO_BASE_DOCUMENT + FILE_INFO_TAMANY,
              String.valueOf(data.length));
          if (mimetype == null) {
            throw new CustodyException("No s'ha definit el mime type del DocumentCustody");
          }
          infoDocSign.setProperty(FILE_INFO_BASE_DOCUMENT + FILE_INFO_MIME, mimetype);
        }
      }

      if (overWriteSignature) {
        if (signatureCustody == null) {
          doc.setListaFirmas(new ArrayList<FirmaDocumento>());

          infoDocSign.remove(FILE_INFO_BASE_FIRMA + FILE_INFO_NOM);
          infoDocSign.remove(FILE_INFO_BASE_FIRMA + FILE_INFO_TAMANY);
          infoDocSign.remove(FILE_INFO_BASE_FIRMA + FILE_INFO_MIME);

          infoDocSign.remove(FILE_INFO_BASE_FIRMA + FILE_INFO_SIGNATURE_ATTACHED_DOCUMENT);
          infoDocSign.remove(FILE_INFO_BASE_FIRMA + FILE_INFO_SIGNATURE_TYPE);

        } else {

          FirmaDocumento firma = new FirmaDocumento();
          final byte[] data = signatureCustody.getData();
          firma.setContent(Base64.encode(data));
          firma.setEncoding(FORMATO_UTF8);

          String mimeType = signatureCustody.getMime();
          
          firma.setMimetype(mimeType);

          doc.setListaFirmas(Arrays.asList(firma));

          infoDocSign.setProperty(FILE_INFO_BASE_FIRMA + FILE_INFO_NOM,
              signatureCustody.getName());
          infoDocSign.setProperty(FILE_INFO_BASE_FIRMA + FILE_INFO_TAMANY,
              String.valueOf(data.length));
          if (mimeType == null) {
            throw new CustodyException("No s'ha definit el mime type del SignatureCustody");
          }
          infoDocSign.setProperty(FILE_INFO_BASE_FIRMA + FILE_INFO_MIME, mimeType);

          if (signatureCustody.getAttachedDocument() == null) {
            infoDocSign.remove(FILE_INFO_BASE_FIRMA + FILE_INFO_SIGNATURE_ATTACHED_DOCUMENT);
          } else {
            infoDocSign.setProperty(FILE_INFO_BASE_FIRMA
                + FILE_INFO_SIGNATURE_ATTACHED_DOCUMENT,
                String.valueOf(signatureCustody.getAttachedDocument()));
          }
          infoDocSign.setProperty(FILE_INFO_BASE_FIRMA + FILE_INFO_SIGNATURE_TYPE,
              signatureCustody.getSignatureType());
        }
      }

      // ============================= GUARDAR o ACTUALITZAR
      if (documentElectronicID == null) {

        // CREAR DOCUMENT

        // Lo creamos en el Archivo

        String uuidDestino = (ec.carpetaID == null) ? ec.expedientID : ec.carpetaID;
        final boolean retrieveContent = false;

        final ResultData rd;
        final String uuid;
        if (isPropertyCreateDraft()) {
          
          CreateDraftDocumentResult result = apiArxiu.crearDraftDocument(uuidDestino, doc,
              retrieveContent);
          
          rd = result.getCreateDraftDocumentResult().getResult();
          
          final String errorCodi = rd.getCode();
          if (hiHaError(errorCodi)) {
            throw new CustodyException("Error creant Document Draft amb id " + custodyID + "("
                + errorCodi + " - " + rd.getDescription() + ")");
          }
          
          uuid = result.getCreateDraftDocumentResult().getResParam().getId();
          
        } else {
           CreateDocumentResult result = apiArxiu.crearDocumento(uuidDestino, doc,
               retrieveContent);
           rd = result.getCreateDocumentResult().getResult();
           
           final String errorCodi = rd.getCode();
           if (hiHaError(errorCodi)) {
             throw new CustodyException("Error creant Document Draft amb id " + custodyID + "("
                 + errorCodi + " - " + rd.getDescription() + ")");
           }
           
           uuid = result.getCreateDocumentResult().getResParam().getId();
        }

        
        

        if (debug) {
          log.info("Creat nou Document amb ID = " + uuid);
        }

      } else {

        // ACTUALITZAR DOCUMENT
        if (debug) {
          log.info("ACTUALITZAR DOCUMENT AMB UUID = " + documentElectronicID);
        }

        doc.setId(documentElectronicID);
        doc.getAspects().remove(Aspectos.BORRADOR);
        ResultadoSimple rs = apiArxiu.actualizarDocumento(doc);

        if (hiHaError(rs.getCodigoResultado())) {
          throw new CustodyException("Error actualitzant Document amb id " + custodyID
              + "(" + rs.getCodigoResultado() + " - " + rs.getMsjResultado() + ")");
        }

      }

      // Guardar Informació del Documents:
      createSimpleDoc(apiArxiu, NOM_INFO_DOCUMENT_ELECTRONIC, infoDocSign, custodyID,
          nodoInfo.getId());

      // Guardar/actualitzar metadades
      if (overWriteMetadatas) {
        updateMetadata(custodyID, metadatas, custodyParameters);
      }

      if (debug) {
        Map<String, Nodo> nodosByName2 = getNodosByCustodyID(apiArxiu, ec, expedient);
        for (String key : nodosByName2.keySet()) {
          log.info("FITXERS A LA CARPETA[" + key + "] = "
              + nodosByName2.get(key).getId());
        }
      }

    } catch (CustodyException e) {
      throw e;
    } catch (Exception e) {
      throw new CustodyException("Error creant o actualitzant document electronic "
          + custodyID + ": " + e.getMessage(), e);
    }
  }

  // ----------------------------------------------------------------------------
  // ---------------------------- D O C U M E N T ------------------------------
  // ----------------------------------------------------------------------------

  @Override
  public void saveDocument(String custodyID, Map<String, Object> custodyParameters,
      DocumentCustody documentCustody) throws CustodyException, NotSupportedCustodyException {

    final boolean overWriteDocument = true;
    final boolean overWriteSignature = false;
    final boolean overWriteMetadatas = false;
    final SignatureCustody signatureCustody = null;
    final Metadata[] metadatas = null;
    createUpdateAll(custodyID, custodyParameters, documentCustody, overWriteDocument,
        signatureCustody, overWriteSignature, metadatas, overWriteMetadatas);

  }

  @Override
  public void deleteDocument(String custodyID) throws CustodyException,
      NotSupportedCustodyException {
    final boolean overWriteDocument = true;
    final boolean overWriteSignature = false;
    final boolean overWriteMetadatas = false;
    final SignatureCustody signatureCustody = null;
    final DocumentCustody documentCustody = null;
    final Metadata[] metadatas = null;
    // No s'utilitza
    final Map<String, Object> custodyParameters = null;
    createUpdateAll(custodyID, custodyParameters, documentCustody, overWriteDocument,
        signatureCustody, overWriteSignature, metadatas, overWriteMetadatas);

  }

  @Override
  public byte[] getDocument(String custodyID) throws CustodyException {

    try {
      ApiArchivoDigital apiArxiu = getApiArxiu(null);

      Map<String, Nodo> nodos = getNodosByCustodyID(apiArxiu, custodyID);

      Nodo nodo = nodos.get(NOM_DOCUMENT_ELECTRONIC);

      String uuid = nodo.getId();

      Resultado<Documento> documento = apiArxiu.obtenerDocumento(uuid, true);

      // TODO validar codis d'error
      Documento doc = documento.getElementoDevuelto();
      String dataB64 = doc.getContent();

      if (dataB64 == null) {
        return null;
      } else {
        return Base64.decode(dataB64);
      }

    } catch (CustodyException ce) {
      throw ce;
    } catch (Exception e) {
      throw new CustodyException(
          "Error intentant obtenir contingut del Document amb custodyID " + custodyID + ": "
              + e.getMessage(), e);
    }
  }

  @Override
  public DocumentCustody getDocumentInfo(String custodyID) throws CustodyException {

    DocumentCustody doc = getDocumentInfoOnly(custodyID);

    if (doc == null) {
      return null;
    }

    doc.setData(getDocument(custodyID));

    return doc;

  }

  @Override
  public DocumentCustody getDocumentInfoOnly(String custodyID) throws CustodyException {

    try {
      ApiArchivoDigital apiArxiu = getApiArxiu(null);

      Map<String, Nodo> nodos = getNodosByCustodyID(apiArxiu, custodyID);

      Nodo nodo = nodos.get(NOM_INFO_DOCUMENT_ELECTRONIC);

      String uuid = nodo.getId();

      Properties prop = readPropertiesFromSimpleDoc(apiArxiu, uuid);

      String nom = prop.getProperty(FILE_INFO_BASE_DOCUMENT + FILE_INFO_NOM);

      if (nom == null) {
        return null;
      }

      DocumentCustody doc = new DocumentCustody();
      doc.setName(nom);
      doc.setLength(Long.parseLong(prop
          .getProperty(FILE_INFO_BASE_DOCUMENT + FILE_INFO_TAMANY)));
      doc.setMime(prop.getProperty(FILE_INFO_BASE_DOCUMENT + FILE_INFO_MIME));

      return doc;

    } catch (Exception e) {
      throw new CustodyException(
          "Error intentant obtenir contingut del Document amb custodyID " + custodyID + ": "
              + e.getMessage(), e);
    }

  }

  @Override
  public boolean supportsDeleteDocument() {
    return isPropertyCreateDraft();
  }

  // ----------------------------------------------------------------------------
  // ---------------------------- S I G N A T U R E ----------------------------
  // ----------------------------------------------------------------------------

  @Override
  public void saveSignature(String custodyID, Map<String, Object> custodyParameters,
      SignatureCustody signatureCustody) throws CustodyException, NotSupportedCustodyException {

    final boolean overWriteDocument = false;
    final boolean overWriteSignature = true;
    final boolean overWriteMetadatas = false;
    final DocumentCustody documentCustody = null;
    final Metadata[] metadatas = null;
    createUpdateAll(custodyID, custodyParameters, documentCustody, overWriteDocument,
        signatureCustody, overWriteSignature, metadatas, overWriteMetadatas);

  }

  @Override
  public byte[] getSignature(String custodyID) throws CustodyException {
    
    final boolean debug = isDebug();
    
    try {
      ApiArchivoDigital apiArxiu = getApiArxiu(null);

      Map<String, Nodo> nodos = getNodosByCustodyID(apiArxiu, custodyID);

      Nodo nodo = nodos.get(NOM_DOCUMENT_ELECTRONIC);

      String uuid = nodo.getId();

      Resultado<Documento> documento = apiArxiu.obtenerDocumento(uuid, true);

      // TODO  Check Values
      Documento doc = documento.getElementoDevuelto();

      List<FirmaDocumento> listFirmes = doc.getListaFirmas();

      if (listFirmes == null || listFirmes.size() == 0) {
        return null;
      }

      FirmaDocumento firma = listFirmes.get(0); // TODO Quina firma recollir: la
                                                // darrera ????

      String dataB64 = firma.getContent();

      if (debug) {
        log.info("firma.getMimetype() = " + firma.getMimetype());
        log.info("firma.getEncoding() = " + firma.getEncoding());
        log.info("firma.getBinarytype().getValue() = " + firma.getBinarytype().getValue());
      }

      if (dataB64 == null) {
        if (debug) {
          log.info("firma.getContent() = NULL");
        }
        return null;
      } else {
        if (debug) {
          log.info("firma.getContent().length = " + dataB64.length());
        }
        return Base64.decode(dataB64);
      }

    } catch (Exception e) {
      throw new CustodyException(
          "Error intentant obtenir contingut de la Firma amb custodyID " + custodyID + ": "
              + e.getMessage(), e);
    }
  }

  @Override
  public SignatureCustody getSignatureInfo(String custodyID) throws CustodyException {
    SignatureCustody sign = getSignatureInfoOnly(custodyID);

    if (sign == null) {
      return null;
    }

    sign.setData(getSignature(custodyID));

    // TODO  Check if data is null

    return sign;
  }

  @Override
  public SignatureCustody getSignatureInfoOnly(String custodyID) throws CustodyException {
    try {
      ApiArchivoDigital apiArxiu = getApiArxiu(null);

      Map<String, Nodo> nodos = getNodosByCustodyID(apiArxiu, custodyID);

      Nodo nodo = nodos.get(NOM_INFO_DOCUMENT_ELECTRONIC);

      String uuid = nodo.getId();

      Properties prop = readPropertiesFromSimpleDoc(apiArxiu, uuid);

      // TODO  Check Values
      String nom = prop.getProperty(FILE_INFO_BASE_FIRMA + FILE_INFO_NOM);

      if (nom == null) {
        return null;
      }

      SignatureCustody sign = new SignatureCustody();
      sign.setName(nom);
      sign.setLength(Long.parseLong(prop.getProperty(FILE_INFO_BASE_FIRMA + FILE_INFO_TAMANY)));
      sign.setMime(prop.getProperty(FILE_INFO_BASE_FIRMA + FILE_INFO_MIME));

      String attached = prop.getProperty(FILE_INFO_BASE_FIRMA
          + FILE_INFO_SIGNATURE_ATTACHED_DOCUMENT);
      if (attached == null) {
        sign.setAttachedDocument(null);
      } else {
        sign.setAttachedDocument(Boolean.parseBoolean(attached));
      }

      sign.setSignatureType(prop.getProperty(FILE_INFO_BASE_FIRMA + FILE_INFO_SIGNATURE_TYPE));

      return sign;

    } catch (Exception e) {
      throw new CustodyException(
          "Error intentant obtenir contingut del Document amb custodyID " + custodyID + ": "
              + e.getMessage(), e);
    }

  }

  @Override
  public void deleteSignature(String custodyID) throws CustodyException,
      NotSupportedCustodyException {
    final boolean overWriteDocument = false;
    final boolean overWriteSignature = true;
    final boolean overWriteMetadatas = false;
    final SignatureCustody signatureCustody = null;
    final DocumentCustody documentCustody = null;
    final Metadata[] metadatas = null;
    // No s'utilitza
    final Map<String, Object> custodyParameters = null;
    createUpdateAll(custodyID, custodyParameters, documentCustody, overWriteDocument,
        signatureCustody, overWriteSignature, metadatas, overWriteMetadatas);

  }

  @Override
  public boolean supportsDeleteSignature() {
    return true;
  }


  @Override
  public String[] getSupportedSignatureTypes() {

    return SignatureCustody.ALL_TYPES_OF_SIGNATURES;

  }

  @Override
  public Boolean supportsAutomaticRefreshSignature() {
    // TODO Es presuposa que si
    return true;
  }

  // ----------------------------------------------------------------------------
  // ---------------------------- M E T A D A T A
  // -------------------------------
  // ----------------------------------------------------------------------------

  @Override
  public boolean supportsMetadata() {
    return true;
  }

  @Override
  public void addMetadata(String custodyID, final Metadata metadata,
      Map<String, Object> custodyParameters) throws CustodyException,
      NotSupportedCustodyException, MetadataFormatException {

    // TODO S'ha de gestionar multipes valors associats a un única clau
    updateMetadata(custodyID, metadata, custodyParameters);
  }

  @Override
  public void addMetadata(String custodyID, final Metadata[] metadataList,
      Map<String, Object> custodyParameters) throws CustodyException,
      NotSupportedCustodyException, MetadataFormatException {
    // TODO S'ha de gestionar multipes valors associats a un única clau
    updateMetadata(custodyID, metadataList, custodyParameters);
  }

  @Override
  public void updateMetadata(String custodyID, final Metadata metadata,
      Map<String, Object> custodyParameters) throws CustodyException,
      NotSupportedCustodyException, MetadataFormatException {

    ApiArchivoDigital apiArxiu = getApiArxiu(custodyParameters);

    new MetadataAction(apiArxiu, custodyID) {
      @Override
      public boolean modificarMetadades(Properties metadades) throws Exception {
        metadades.setProperty(metadata.getKey(), metadata.getValue());
        return true;
      }
    }.doAction();

  }

  @Override
  public void updateMetadata(String custodyID, final Metadata[] metadataList,
      Map<String, Object> custodyParameters) throws CustodyException,
      NotSupportedCustodyException, MetadataFormatException {

    ApiArchivoDigital apiArxiu = getApiArxiu(custodyParameters);

    new MetadataAction(apiArxiu, custodyID) {

      @Override
      public boolean modificarMetadades(Properties metadades) throws Exception {
        if (metadataList != null && metadataList.length != 0) {
          for (Metadata meta : metadataList) {
            metadades.setProperty(meta.getKey(), meta.getValue());
          }
          return true;
        } else {
          return false;
        }
      }
    }.doAction();

  }

  @Override
  public Map<String, List<Metadata>> getAllMetadata(String custodyID) throws CustodyException,
      NotSupportedCustodyException {

    // TODO S'ha de gestionar multipes valors associats a un única clau
    ApiArchivoDigital apiArxiu = getApiArxiu(null);
    Properties properties = new MetadataAction(apiArxiu, custodyID) {
      @Override
      public boolean modificarMetadades(Properties metadades) throws Exception {
        return false;
      }
    }.doAction();

    Map<String, List<Metadata>> result = new HashMap<String, List<Metadata>>();

    for (Object keyObj : properties.keySet()) {
      String key = (String) keyObj;
      List<Metadata> list = new ArrayList<Metadata>();
      list.add(new Metadata(key, properties.getProperty(key)));
      result.put(key, list);
    }
    return result;
  }

  @Override
  public List<Metadata> getMetadata(String custodyID, String key) throws CustodyException,
      NotSupportedCustodyException {
    // TODO S'ha de gestionar multipes valors associats a un única clau
    ApiArchivoDigital apiArxiu = getApiArxiu(null);
    Properties properties = new MetadataAction(apiArxiu, custodyID) {
      @Override
      public boolean modificarMetadades(Properties metadades) throws Exception {
        return false;
      }
    }.doAction();

    List<Metadata> list = new ArrayList<Metadata>();
    String value = properties.getProperty(key);

    if (value != null) {
      list.add(new Metadata(key, value));
    }

    return list;
  }

  @Override
  public Metadata getOnlyOneMetadata(String custodyID, String key) throws CustodyException,
      NotSupportedCustodyException {
    List<Metadata> list = getMetadata(custodyID, key);
    if (list.size() != 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  @Override
  public void deleteAllMetadata(String custodyID) throws CustodyException {
    ApiArchivoDigital apiArxiu = getApiArxiu(null);
    new MetadataAction(apiArxiu, custodyID) {
      @Override
      public boolean modificarMetadades(Properties metadades) throws Exception {
        metadades.clear();
        return true;
      }
    }.doAction();

  }

  @Override
  public List<Metadata> deleteMetadata(String custodyID, final String key)
      throws CustodyException {

    ApiArchivoDigital apiArxiu = getApiArxiu(null);

    MetadataAction m = new MetadataAction(apiArxiu, custodyID) {
      @Override
      public boolean modificarMetadades(Properties metadades) throws Exception {
        Object obj = metadades.remove(key);
        if (obj == null) {
          return false;
        } else {
          this.outputParameter = obj;
          return true;
        }
      }
    };
    m.doAction();
    List<Metadata> list = new ArrayList<Metadata>();
    if (m.outputParameter != null) {
      list.add(new Metadata(key, (String) m.outputParameter));
    }

    return list;
  }

  @Override
  public List<Metadata> deleteMetadata(String custodyID, final String[] keys)
      throws CustodyException {

    ApiArchivoDigital apiArxiu = getApiArxiu(null);

    MetadataAction m = new MetadataAction(apiArxiu, custodyID) {
      @Override
      public boolean modificarMetadades(Properties metadades) throws Exception {

        List<Metadata> list = new ArrayList<Metadata>();
        for (String key : keys) {
          Object obj = metadades.remove(key);
          if (obj != null) {
            list.add(new Metadata(key, (String) obj));
          }
        }

        this.outputParameter = list;
        if (list.isEmpty()) {
          return false;
        } else {
          return true;
        }
      }
    };
    m.doAction();

    return (List<Metadata>) m.outputParameter;

  }

  @Override
  public boolean supportsDeleteMetadata() {
    return true;
  }

  // ----------------------------------------------------------------------------
  // ---------------------------- A N N E X -------------------------------
  // ----------------------------------------------------------------------------
  @Override
  public boolean supportsAnnexes() {
    return false;
  }

  @Override
  public boolean supportsDeleteAnnex() {
    return false;
  }

  @Override
  public String addAnnex(String custodyID, AnnexCustody annex, Map<String, Object> parameters)
      throws CustodyException, NotSupportedCustodyException {
    throw new NotSupportedCustodyException();
  }

  @Override
  public void deleteAnnex(String custodyID, String annexID) throws CustodyException,
      NotSupportedCustodyException {
    throw new NotSupportedCustodyException();

  }

  @Override
  public void deleteAllAnnexes(String custodyID) throws CustodyException,
      NotSupportedCustodyException {
    throw new NotSupportedCustodyException();

  }

  @Override
  public byte[] getAnnex(String custodyID, String annexID) throws CustodyException {
    throw new CustodyException("No es suporten Annexes");
  }

  @Override
  public AnnexCustody getAnnexInfo(String custodyID, String annexID) throws CustodyException {
    throw new CustodyException("No es suporten Annexes");
  }

  @Override
  public AnnexCustody getAnnexInfoOnly(String custodyID, String annexID)
      throws CustodyException {
    throw new CustodyException("No es suporten Annexes");
  }

  @Override
  public List<String> getAllAnnexes(String custodyID) throws CustodyException {
    throw new CustodyException("No es suporten Annexes");
  }

  // ----------------------------------------------------------------------------
  // ---------------------------- U T I L S -------------------------------
  // ----------------------------------------------------------------------------

  protected String processEL(String template, Map<String, Object> custodyParameters)
      throws CustodyException {
    return AbstractDocumentCustodyPlugin
        .processExpressionLanguage(template, custodyParameters);
  }

  protected String processELNullIfEmpty(String template, Map<String, Object> custodyParameters)
      throws CustodyException {
    if (template == null) {
      return null;
    }
    String tmp = AbstractDocumentCustodyPlugin.processExpressionLanguage(template,
        custodyParameters);

    if (tmp == null || tmp.trim().length() == 0) {
      return null;
    }

    return tmp;
  }

  protected boolean hiHaError(String code) {
    return !CodigosResultadoPeticion.PETICION_CORRECTA.equals(code);
  }

  protected boolean hiHaErrorEnCerca(String code) {
    return !CodigosResultadoPeticion.PETICION_CORRECTA.equals(code)
        && !CodigosResultadoPeticion.LISTA_VACIA.equals(code);
  }

  protected Map<String, Object> getMetadadesPerExpedient(Map<String, Object> custodyParameters)
      throws CustodyException, Exception {

    Map<String, Object> llistaMetadades = getMetadadesComuns(custodyParameters);

    // MetadatosExpediente.IDENTIFICADOR_PROCEDIMIENTO
    String codigoProcedimiento = processEL(getPropertyCodiProcedimentEL(), custodyParameters);
    llistaMetadades.put(MetadataConstants.ENI_ID_TRAMITE, codigoProcedimiento);

    // MetadatosExpediente.CODIGO_APLICACION_TRAMITE
    String codigoAplicacion = getPropertyCodiAplicacio();
    llistaMetadades.put(MetadataConstants.ENI_APP_TRAMITE_EXP, codigoAplicacion);

    List<Metadata> additionalMetas;
    additionalMetas = recollectAutomaticMetadatas(custodyParameters, "expedient");

    if (additionalMetas != null) {
      final boolean debug = isDebug(); 
      for (Metadata metadata : additionalMetas) {
        if (debug) {
          log.info("MetasExpedient::Afegint metadata addicional [" + metadata.getKey()
            + "] => " + metadata.getValue());
        }
        llistaMetadades.put(metadata.getKey(), metadata.getValue());
      }
    }

    return llistaMetadades;
  }

  protected Map<String, Object> getMetadadesPerDocument(Map<String, Object> custodyParameters,
     SignatureCustody signatureCustody) throws CustodyException, Exception {

    Map<String, Object> llistaMetadades = getMetadadesComuns(custodyParameters);

    List<Metadata> additionalMetas;
    additionalMetas = recollectAutomaticMetadatas(custodyParameters, "documentelectronic");

    if (additionalMetas.size() != 0) {
      for (Metadata metadata : additionalMetas) {
        llistaMetadades.put(metadata.getKey(), metadata.getValue());
      }
    }

    // MetadatosDocumento.CODIGO_APLICACION_TRAMITE
    String codigoAplicacion = getPropertyCodiAplicacio();
    llistaMetadades.put(MetadataConstants.ENI_APP_TRAMITE_DOC, codigoAplicacion);

    // MetadatosDocumento.ESTADO_ELABORACION : EE01,EE02, EE03, EE04, EE99
    String estadoElaboracion = processEL(getPropertyEstatElaboracioEL(), custodyParameters);
    llistaMetadades.put(MetadataConstants.ENI_ESTADO_ELABORACION, estadoElaboracion);

    // (MetadatosDocumento.TIPO_DOC_ENI: TD01, TD02, TD03, ...
    String tipoDocEni = processEL(getPropertyTipusDocumentalEL(), custodyParameters);
    llistaMetadades.put(MetadataConstants.ENI_TIPO_DOCUMENTAL, tipoDocEni);

   
    if (signatureCustody != null) {
      String fileName = signatureCustody.getName();
    
      int i = fileName.lastIndexOf('.');
      String extensio = null;
      if (i != -1) {
        extensio = fileName.substring(i+1).toLowerCase();        
      }
      
      if (extensio != null) {
        
        llistaMetadades.put(MetadataConstants.ENI_EXTENSION_FORMATO, extensio);
        
        
        FormatosFichero format = FORMATS_BY_EXTENSION.get(extensio);
        
        if (format != null) {
          llistaMetadades.put(MetadataConstants.ENI_NOMBRE_FORMATO, format);  
        }

      }
 
      //  Afegir propietats associades al tipus de firma 
      String tipusFirma = signatureCustody.getSignatureType();
      TiposFirma tipo = TIPOFIRMA_SIMPLE.get(tipusFirma);
      if (tipo == null) {
        tipo = TIPOFIRMA_COMPLEXE.get(tipusFirma + "_" + signatureCustody.getAttachedDocument());
      }
      if (tipo != null) {
        llistaMetadades.put(MetadataConstants.ENI_TIPO_FIRMA, tipo.getValue()); 
      }
      
      // TODO falta afegir propietats associades al Perfil de Firma
      // Veure classe PerfilesFirma
      // metadataCollection.put(MetadatosDocumento.PERFIL_FIRMA, ????)
      
      
      
    }

    return llistaMetadades;

  }

  protected Map<String, Object> getMetadadesComuns(Map<String, Object> custodyParameters)
      throws CustodyException, Exception {

    Map<String, Object> llistaMetadades = new HashMap<String, Object>();

    // MetadatosExpediente.CODIGO_CLASIFICACION
    String serieDocumental = processEL(getPropertySerieDocumentalEL(), custodyParameters);
    llistaMetadades.put(MetadataConstants.ENI_COD_CLASIFICACION, serieDocumental);
    // MetadatosExpediente.ORGANO
    String organos = processEL(getPropertyOrgansEL(), custodyParameters);
    final List<String> listaOrganos = Arrays.asList(organos.split(","));
    llistaMetadades.put(MetadataConstants.ENI_ORGANO, listaOrganos);
    // MetadatosExpediente.ORIGEN
    String origenDocument = processEL(getPropertyOrigenDocumentEL(), custodyParameters);
    llistaMetadades.put(MetadataConstants.ENI_ORIGEN, origenDocument);

    // MetadatosExpediente.FECHA_INICIO
    // NOTA: Amb aquest canvi pots fer que es guardi en una altre data
    // Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse("04/04/2017");
    // String fechaCreacio = UtilidadesFechas.convertirDeDate_A_ISO8601(fecha);
    String templateDataCreacio = getPropertyDataCreacioEL();
    String dataCreacio;
    if (templateDataCreacio == null) {
      dataCreacio = UtilidadesFechas.fechaActualEnISO8601();      
    } else {
      dataCreacio = processEL(templateDataCreacio, custodyParameters);
    }
    if (isDebug()) {
      log.info("DataCreacio: " + dataCreacio);
    }
    llistaMetadades.put(MetadataConstants.ENI_FECHA_INICIO, dataCreacio);

    return llistaMetadades;
  }

  protected ApiArchivoDigital apiArxiuCache = null;

  public ApiArchivoDigital getApiArxiu(Map<String, Object> custodyParameters)
      throws CustodyException {

    try {

      if (apiArxiuCache == null) {
        // CabeceraPeticion cabecera = UtilCabeceras.generarCabeceraMock();

        CabeceraPeticion cabecera = new CabeceraPeticion();
        // intern api
        cabecera.setServiceVersion(ApiArchivoDigital.VERSION_SERVICIO);
        // aplicacio
        cabecera.setCodiAplicacion(getPropertyCodiAplicacio());
        cabecera.setUsuarioSeguridad(getPropertyLoginUsername());
        cabecera.setPasswordSeguridad(getPropertyLoginPassword());
        cabecera.setOrganizacion(getPropertyOrganitzacio());

        boolean debug = isDebug(); 
        
        // info login
        // + Ciutadà
        String ciutadaNom =  processOptionalProperty("SolicitantNom", 
            getPropertySolicitantNomEL(), custodyParameters);
        if (debug) {
          log.info(" LOGIN [ciutadaNom] = " + ciutadaNom);
        }
        
        cabecera.setNombreSolicitante(ciutadaNom);
        String ciutadaNIF = processOptionalProperty("SolicitantIdentificadorAdministratiu",
                getPropertySolicitantIdentificadorAdministratiuEL(), custodyParameters);
        cabecera.setDocumentoSolicitante(ciutadaNIF);
        if (debug) {
          log.info(" LOGIN [ciutadaNIF] = " + ciutadaNIF);
        }

        // + Funcionari o usuari de l'aplicació
        String funcionariUsername = processOptionalProperty("UsuariUsername", 
            getPropertyUsuariUsernameEL(), custodyParameters);
        cabecera.setNombreUsuario(funcionariUsername);
        if (debug) {
          log.info(" LOGIN [funcionariUsername] = " + funcionariUsername);
        }
        
        String funcionariNIF = processOptionalProperty("UsuariIdentificadorAdministratiu",
            getPropertyUsuariIdentificadorAdministratiuEL(), custodyParameters);
        cabecera.setDocumentoUsuario(funcionariNIF);
        if (debug) {
          log.info(" LOGIN [funcionariNIF] = " + funcionariNIF);
        }

        //  + info peticio
        cabecera.setNombreProcedimiento(
            processOptionalProperty("NomProcediment", getPropertyNomProcedimentEL(), custodyParameters));
      

        if (isPropertyIgnoreServerCeriticates()) {
          XTrustProvider.install();
        }

        // String urlBase = DatosConexion.ENDPOINT_DEV;

        ApiArchivoDigital apiArxiu = new ApiArchivoDigital(getPropertyURL(), cabecera);
        
        // TODO 
        apiArxiu.setTrazas(false);

        apiArxiuCache = apiArxiu;

      }

      return apiArxiuCache;
    } catch (CustodyException ce) {
      throw ce;
    } catch (Exception e) {
      throw new CustodyException("Error instanciant l'API d'Arxiu Electrònic: "
          + e.getMessage(), e);
    }
  }
  
  
  /**
   * 
   * @param propertyValue
   * @param custodyParameters
   * @return
   */
  private String processOptionalProperty(String titol, String propertyValue, Map<String, Object> custodyParameters) {
    
    if (propertyValue == null || propertyValue.trim().length() == 0) {
      return propertyValue;
    }
    if ( custodyParameters == null) {
      custodyParameters = new HashMap<String, Object>();
    }
    
    try {
      return AbstractDocumentCustodyPlugin.processExpressionLanguage(propertyValue, custodyParameters);
    } catch (CustodyException e) {
      log.warn("S'ha intentat processar la propietat titulada " + titol + " amb valor "
          + propertyValue + " però s'ha produït un error al fer el processament EL: "
          + e.getMessage(), e );
      return null;
    }
    
    
  }
  
  

  public ExpedientCarpeta decodeCustodyID(String custodyID) {

    if (custodyID == null) {
      return null;
    }

    int pos = custodyID.indexOf(SEPARATOR_EXPEDIENT_CARPETA);

    if (pos == -1) {
      return new ExpedientCarpeta(custodyID, null);
    } else {
      return new ExpedientCarpeta(custodyID.substring(0, pos), custodyID.substring(pos + 1));
    }

  }

  /**
   * 
   * @param custodyParameters
   * @param ignoreErrors
   * @param prefix
   *          Por valer expedient, document, firma
   * @return
   * @throws CustodyException
   */
  protected List<Metadata> recollectAutomaticMetadatas(Map<String, Object> custodyParameters,
      String prefix) throws CustodyException {
    final boolean ignoreErrors = false;

    String propertyBase = getPropertyBase() + prefix + ".";

    final boolean debug = isDebug();
    
    if (debug) {
       log.info("recollectAutomaticMetadatas::Metadata property Base: " + propertyBase);
       log.info("recollectAutomaticMetadatas::" + propertyBase + "automaticmetadata_items = "
          + getProperty(propertyBase + "automaticmetadata_items"));
    }
    List<Metadata> list = AbstractDocumentCustodyPlugin.recollectAutomaticMetadatas(this,
        custodyParameters, getPropertyBase() + prefix + ".", ignoreErrors);

    if (list == null) {
      if (debug) {
        log.info("recollectAutomaticMetadatas::LIST NULL: " + propertyBase);
      }
      return new ArrayList<Metadata>();
    } else {
      if (debug) {
        log.info("recollectAutomaticMetadatas::LIST: " + propertyBase);
      }
      return list;
    }

  }

  public void createSimpleDoc(ApiArchivoDigital api, String nom, Properties properties,
      String custodyID, String uuidDoc) throws IOException, CustodyException {

    StringWriter writer = new StringWriter();
    properties.list(new PrintWriter(writer));
    createSimpleDoc(api, nom, writer.getBuffer().toString(), custodyID, uuidDoc);

  }

  public void createSimpleDoc(ApiArchivoDigital api, String nom, String content,
      String custodyID, String uuidDoc) throws IOException, CustodyException {
    Documento documento = new Documento();

    documento.setContent(Base64.encode(content));
    documento.setName(nom);
    documento.setType(TiposObjetoSGD.DOCUMENTO);
    documento.setEncoding(ArxiuDigitalCAIBDocumentCustodyPlugin.FORMATO_UTF8);

    List<Aspectos> aspects = new ArrayList<Aspectos>();
    aspects.add(Aspectos.INTEROPERABLE);
    aspects.add(Aspectos.TRANSFERIBLE);
    documento.setAspects(aspects);
    documento.setMimetype("text/plain");

    Map<String, Object> llistaMetadades = new HashMap<String, Object>();

    documento.setMetadataCollection(llistaMetadades);

    if (uuidDoc == null) {
      ExpedientCarpeta ec = decodeCustodyID(custodyID);
      String uuidParent = (ec.carpetaID == null) ? ec.expedientID : ec.carpetaID;
      CreateDraftDocumentResult result = api.crearDraftDocument(uuidParent, documento, false);
      String codi = result.getCreateDraftDocumentResult().getResult().getCode();
      if (hiHaError(codi)) {
        throw new CustodyException("Error creant DocumentSimple " + custodyID + "(" + codi
            + " - " + result.getCreateDraftDocumentResult().getResult().getDescription() + ")");
      }

    } else {
      documento.setId(uuidDoc);
      ResultadoSimple rs = api.actualizarDocumento(documento);
      if (hiHaError(rs.getCodigoResultado())) {
        throw new CustodyException("Error actualitzant DocumentSimple " + custodyID + "("
            + rs.getCodigoResultado() + " - " + rs.getMsjResultado() + ")");
      }
    }

  }

  protected Properties readPropertiesFromSimpleDoc(ApiArchivoDigital api, String uuidDoc)
      throws IOException {
    byte[] data = readSimpleDoc(api, uuidDoc);

    if (data == null) {
      return null;
    } else {
      Properties prop = new Properties();

      prop.load(new ByteArrayInputStream(data));

      return prop;
    }

  }

  protected byte[] readSimpleDoc(ApiArchivoDigital api, String uuidDoc) throws IOException {

    final boolean retrieveContent = true;
    Resultado<Documento> resultat = api.obtenerDocumento(uuidDoc, retrieveContent);
    // TODO Check errors
    Documento doc = resultat.getElementoDevuelto();
    if (doc == null) {
      return null;
    } else {
      return Base64.decode(doc.getContent());
    }
  }

  protected Properties byteArray2Properties(byte[] propertiesData) throws Exception {

    Properties prop = new Properties();

    prop.load(new ByteArrayInputStream(propertiesData));

    return prop;

  }

  protected Map<String, Nodo> getNodosByCustodyID(ApiArchivoDigital apiArxiu, String custodyID)
      throws IOException, CustodyException {

    ExpedientCarpeta ec = decodeCustodyID(custodyID);

    // Cercam si hi ha un document
    List<Nodo> nodos;
    if (ec.carpetaID == null) {
      Resultado<Expediente> expedient = apiArxiu.obtenerExpediente(ec.expedientID);
      nodos = expedient.getElementoDevuelto().getChilds();
    } else {
      // log.info("XYZ Llegim Carpeta amb uuid " + ec.carpetaID);
      Resultado<Directorio> dir = apiArxiu.obtenerDirectorio(ec.carpetaID);
      // log.info("XYZ Llegim Carpeta : codi resultat " + dir.getCodigoResultado());
      if (hiHaError(dir.getCodigoResultado())) {
        throw new CustodyException("Error intentant obtenir inforació de la carpeta amb uuid "
           + ec.carpetaID + ": " + dir.getCodigoResultado() + "-" + dir.getMsjResultado());
      }

      nodos = dir.getElementoDevuelto().getChilds();
    }
    Map<String, Nodo> nodosByName;
    nodosByName = new HashMap<String, Nodo>();
    if (nodos != null) {
      for (Nodo nodo : nodos) {
        nodosByName.put(nodo.getName(), nodo);
      }
    }

    return nodosByName;

  }

  protected Map<String, Nodo> getNodosByCustodyID(ApiArchivoDigital apiArxiu,
      ExpedientCarpeta ec, Resultado<Expediente> expedient) throws IOException, CustodyException {
    Map<String, Nodo> nodosByName;
    {
      List<Nodo> nodos;
      if (ec.carpetaID == null) {
        nodos = expedient.getElementoDevuelto().getChilds();
      } else {
        //log.info("Llegim Carpeta amb uuid " + ec.carpetaID);
        Resultado<Directorio> dir = apiArxiu.obtenerDirectorio(ec.carpetaID);

        //log.info("Llegim Carpeta : codi resultat " + dir.getCodigoResultado());
        if (hiHaError(dir.getCodigoResultado())) {
          throw new CustodyException("Error intentant obtenir inforació de la carpeta amb uuid "
             + ec.carpetaID + ": " + dir.getCodigoResultado() + "-" + dir.getMsjResultado());
        }

        nodos = dir.getElementoDevuelto().getChilds();
      }
      nodosByName = new HashMap<String, Nodo>();
      if (nodos != null) {
        for (Nodo nodo : nodos) {
          nodosByName.put(nodo.getName(), nodo);
        }
      }
    }
    return nodosByName;
  }

  protected String getCSV(String custodyID, Map<String, Object> custodyParameters)
      throws CustodyException {
    ApiArchivoDigital apiArxiu = getApiArxiu(custodyParameters);

    try {
      Map<String, Nodo> nodos = getNodosByCustodyID(apiArxiu, custodyID);

      Nodo nodo = nodos.get(NOM_INFO_DOCUMENT_ELECTRONIC);

      String uuid = nodo.getId();

      Properties prop = readPropertiesFromSimpleDoc(apiArxiu, uuid);

      return prop.getProperty(FILE_INFO_CSV);

    } catch (Exception e) {
      throw new CustodyException("Error intentant obtenir el CSV de la custòdia amb id "
          + custodyID + ": " + e.getMessage(), e);
    }

  }

  /**
   * 
   * @author anadal
   *
   */
  public abstract class MetadataAction {

    protected final ApiArchivoDigital apiArxiu;

    protected final String custodyID;

    public Object outputParameter = null;

    /**
     * @param apiArxiu
     * @param custodyID
     */
    public MetadataAction(ApiArchivoDigital apiArxiu, String custodyID) {
      super();
      this.apiArxiu = apiArxiu;
      this.custodyID = custodyID;
    }

    public Properties doAction() throws CustodyException {

      try {
        Map<String, Nodo> nodosByName = getNodosByCustodyID(apiArxiu, custodyID);
        Nodo nodoMetas = nodosByName.get(NOM_METADADES);
        Properties metadades = readPropertiesFromSimpleDoc(apiArxiu, nodoMetas.getId());

        // Afegir metadades ENI i GDIB
        Nodo nodoDocElec = nodosByName.get(NOM_DOCUMENT_ELECTRONIC);
        Map<String, Object> metadataDocs = null;
        if (nodoDocElec != null) {
          Resultado<Documento> res = apiArxiu.obtenerDocumento(nodoDocElec.getId(), false);
          metadataDocs = res.getElementoDevuelto().getMetadataCollection();

          for (String key : metadataDocs.keySet()) {
            Object value = metadataDocs.get(key);
            metadades.setProperty(key, String.valueOf(value));
          }

        }

        // metadatas.setProperty(metadata.getKey(), metadata.getValue());
        if (modificarMetadades(metadades)) {

          Properties metadadesSenseEni = new Properties();
          metadadesSenseEni.putAll(metadades);

          // Eliminam les propietats ENI i GDIB del document
          if (metadataDocs != null) {
            for (String key : metadataDocs.keySet()) {
              // Object value = metadataDocs.get(key);
              metadadesSenseEni.remove(key);
            }
          }

          // Eliminam les propietats ENI i GDIB afegides manualment
          // List<String> keysEniGdibToDelete = new ArrayList<String>();

          for (Object k : new ArrayList<Object>(metadadesSenseEni.keySet())) {
            String key = (String) k;
            if (key.startsWith("eni:") || key.startsWith("gdib:")) {
              log.warn("La metadada " + key + " no serà actualitzada !!!");
              // keysEniGdibToDelete.add(key);
              metadadesSenseEni.remove(k);
            }
          }

          // TODO Falta actualitzar metadades del document

          // Actualitzam metadades
          createSimpleDoc(apiArxiu, NOM_METADADES, metadadesSenseEni, custodyID,
              nodoMetas.getId());

        }

        return metadades;

      } catch (CustodyException ce) {
        throw ce;
      } catch (Exception e) {
        throw new CustodyException("Error desconegut: " + e.getMessage(), e);
      }
    }

    public abstract boolean modificarMetadades(Properties metadades) throws Exception;

  }

}
