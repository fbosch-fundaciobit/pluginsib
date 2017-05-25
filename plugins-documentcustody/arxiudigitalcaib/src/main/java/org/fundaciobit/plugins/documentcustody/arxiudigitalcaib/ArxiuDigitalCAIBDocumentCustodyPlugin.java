package org.fundaciobit.plugins.documentcustody.arxiudigitalcaib;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

  public static final String NOM_CSV_DOCUMENT_ELECTRONIC = ".CVS_DOCUMENT_ELECTRONIC_CAIB.txt";

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

  public static final String FILE_INFO_BASE_METADATA = "metadata.";

  public static final String ARXIUDIGITALCAIB_PROPERTY_BASE = DOCUMENTCUSTODY_BASE_PROPERTY
      + "arxiudigitalcaib.";

  public static final Map<String, TiposFirma> TIPOFIRMA_SIMPLE = new HashMap<String, TiposFirma>();

  public static final Map<String, TiposFirma> TIPOFIRMA_COMPLEXE = new HashMap<String, TiposFirma>();

  public static final Map<String, FormatosFichero> FORMATS_BY_EXTENSION = new HashMap<String, FormatosFichero>();

  static {

    TIPOFIRMA_SIMPLE.put(SignatureCustody.PADES_SIGNATURE, TiposFirma.PADES);

    /**
     * true if signature contains original document (attached). false if
     * signature does not contain original document (dettached). null can not
     * obtain this information or is not necessary
     */

    TIPOFIRMA_COMPLEXE.put(SignatureCustody.CADES_SIGNATURE + "_null",
        TiposFirma.CADES_ATTACHED);
    TIPOFIRMA_COMPLEXE.put(SignatureCustody.CADES_SIGNATURE + "_true",
        TiposFirma.CADES_ATTACHED);
    TIPOFIRMA_COMPLEXE.put(SignatureCustody.CADES_SIGNATURE + "_false",
        TiposFirma.CADES_DETACHED);

    TIPOFIRMA_COMPLEXE.put(SignatureCustody.XADES_SIGNATURE + "_true",
        TiposFirma.XADES_ENVELOPED);

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
    // FORMATS_BY_EXTENSION.put("pdfa", FormatosFichero.PDFA);
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
  
  
  public boolean isPropertyTancarExpedient() {
    String valueStr = getProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "tancarExpedient");
    if (valueStr != null && "true".equals(valueStr.toLowerCase())) {
      return true;
    } else {
      return false;
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
  
  public String getPropertyPerfilFirmaEL() throws Exception {
    return getPropertyRequired(ARXIUDIGITALCAIB_PROPERTY_BASE + "perfil_firma_EL");
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

  // Ciutada - Nom. Opcional
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
      isDebugValue = log.isDebugEnabled()
          || "true".equals(getProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "debug"));
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
    // EXPEDIENT => CARPETA => DOCUMENT o
    // EXPEDIENT => DOCUMENT 
    final boolean debug = isDebug();
    try {
      final String nomExpedient = processEL(getPropertyNomExpedientEL(), custodyParameters);
      final String nomCarpeta = processELNullIfEmpty(getPropertyNomCarpetaEL(),
          custodyParameters);

      if (debug) {
        log.info("NomExpedient: " + nomExpedient);
        log.info("NomCarpeta: " + nomCarpeta);
      }

      //String uuidCSVFile = null;
      //String carpetaID = null;

      ApiArchivoDigital api = getApiArxiu(custodyParameters);
      
      // Només miram si existeix l'expedient si tenim CARPETES
      Expediente expedientCercat = null;
      if (nomCarpeta != null) {

        FiltroBusquedaFacilExpedientes filtrosRequeridos = new FiltroBusquedaFacilExpedientes();
        filtrosRequeridos.setName(nomExpedient);
        filtrosRequeridos.setAppName(getPropertyCodiAplicacio());
        String serieDocumental = processEL(getPropertySerieDocumentalEL(), custodyParameters);
        filtrosRequeridos.setDocSeries(serieDocumental);
  
        ResultadoBusqueda<Expediente> res = api.busquedaFacilExpedientes(filtrosRequeridos,
            null, 1);
        
  
        if (hiHaErrorEnCerca(res.getCodigoResultado())) {
          throw new CustodyException("Error Consultant si Expedient " + nomExpedient
              + " existeix: " + res.getCodigoResultado() + "-" + res.getMsjResultado());
        }
  
        List<Expediente> llista2 = res.getListaResultado();
  
        
        if (llista2.size() == 0) {
          expedientCercat = null;
        } else {
          // TODO XYZ ZZZ la cerca es fa del nom parescut al fitper, per exemple
          // si cerques
          // "Registre_20" et pot trobar Registre_20, Registre_200, Registre_202,
          // ...
          int countTrobats = 0;
          for (Expediente expediente : llista2) {
            if (nomExpedient.equals(expediente.getName())) {
              countTrobats++;
              if (countTrobats > 1) {
                log.error(" S'ha trobat coincidencia multiple " + expediente.getName() + " ("
                    + expediente.getId() + ") per la cerca de nomExpedient " + nomExpedient
                    + ")");
              } else {
                expedientCercat = expediente;
              }
            }
          }
  
          if (countTrobats == 0) {
            expedientCercat = null;
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
      }

      // log.info("Creacio Expedient::Llistat.SIZE: " + llista.size());
      String expedientID;
      if (expedientCercat == null) {
        // Cream l'expedient
        Map<String, Object> llistaMetadades = getMetadadesPerExpedient(custodyParameters);

        Expediente miExpediente = new Expediente();

        miExpediente.expedienteParaCrear(true);
        miExpediente.setName(nomExpedient);
        miExpediente.setMetadataCollection(llistaMetadades);

        Resultado<Expediente> resultado = api.crearExpediente(miExpediente, true);

        String codErr = resultado.getCodigoResultado();
        if (hiHaError(codErr)) {
          
          if ("COD_021".equals(codErr) 
              && resultado.getMsjResultado().startsWith("Duplicate child name not allowed")) {
            throw new CustodyException("Error creant expedient " + nomExpedient + ": "
               + "Ja existeix un expedient amb aquest nom (la propietat de "
               + "configuracio nom_expedient_EL ha de generar noms únics)");
          } else {
            throw new CustodyException("Error creant expedient " + nomExpedient + ": "
              + resultado.getCodigoResultado() + "-" + resultado.getMsjResultado());
          }
        }

        expedientID = resultado.getElementoDevuelto().getId();

        if (debug) {
          log.info("Creacio Expedient:: CREAT: " + expedientID);
        }

      } else {

        // Expedient Ja Existeix

        expedientID = expedientCercat.getId();

        if (debug) {
          log.info("Creacio Expedient:: JA EXISTEIX: " + expedientID);
        }

        if (nomCarpeta != null) {

          List<Nodo> nodos = expedientCercat.getChilds();
          if (nodos == null || nodos.size() == 0) {
            if (debug) {
              log.info("Creacio Expedient:: Fills Expedients:  NO EN TE (OK)!!!!!");
            }
          } else {
            for (Nodo nodo : nodos) {
              // TiposObjetoSGD.DIRECTORIO
              if (debug) {
                log.info("Creacio Expedient:: Fills Expedients: " + nodo.getName() + " ["
                    + nodo.getType().getValue() + "]");
              }
              if (nomCarpeta.equals(nodo.getName())
                  && TiposObjetoSGD.DIRECTORIO.equals(nodo.getType())) {
                
                throw new CustodyException("S'ha intentat crear una carpeta  " + nomCarpeta 
                    + " en l'expedient " + nomExpedient + " però aquesta carpeta ja existeix."
                    +" La propietat de configuracio nom_carpeta_EL ha de generar"
                    + " noms únics per expedient.");
                
                // XYZ ZZZ carpetaID = nodo.getId();
              }
              /*
              if (NOM_CSV_DOCUMENT_ELECTRONIC.equals(nodo.getName())
                  && TiposObjetoSGD.DOCUMENTO.equals(nodo.getType())) {
                uuidCSVFile = nodo.getId();
              }
              */
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

          //if (carpetaID != null)   {
           
          //}

          Resultado<Directorio> carpeta = api.crearDirectorio(nomCarpeta, expedientID);

          if (hiHaError(carpeta.getCodigoResultado())) {
            throw new CustodyException("Error creant carpeta " + nomCarpeta + ": "
                + carpeta.getCodigoResultado() + "-" + carpeta.getMsjResultado());
          }

          Directorio directorio = carpeta.getElementoDevuelto();

          String carpetaID = directorio.getId();
          if (debug) {
            log.info("Creacio Carpeta:: CREAT: " + carpetaID);
          }
/*
        } 
        
        else {
          // TODO Això és correcte ????? Hauria de ser únic per cada petició
          // TODO fer proves llançant error aquí
          if (debug) {
            log.info("Creacio Carpeta:: JA EXISTEIX: " + carpetaID);
          }

          Resultado<Directorio> carpeta = api.obtenerDirectorio(carpetaID);

          // TODO XYZ ZZZ Checks

          List<Nodo> nodos = carpeta.getElementoDevuelto().getChilds();
          if (nodos == null || nodos.size() == 0) {
            if (debug) {
              log.info("Creacio Carpeta:: NO EN TE FITXERS !!!!!");
            }
          } else {
            for (Nodo nodo : nodos) {
              // TiposObjetoSGD.DIRECTORIO
              if (debug) {
                log.info("Creacio Carpeta:: Fill Carpeta: " + nodo.getName() + " ["
                    + nodo.getType().getValue() + "]");
              }

              if (NOM_CSV_DOCUMENT_ELECTRONIC.equals(nodo.getName())
                  && TiposObjetoSGD.DOCUMENTO.equals(nodo.getType())) {
                uuidCSVFile = nodo.getId();
              }
            }
          }
           
        }
      */

        // Retornam Expedient#Carpeta
        custodyID = expedientID + SEPARATOR_EXPEDIENT_CARPETA + carpetaID;
      }

      // Afegir CSV al document de informació per a que després, ho pugui
      // recuperar durant l'alta del document/firma
      String csv = api.generarCSV();
      if (debug) {
        log.info("CODI SEGUR GENERAT = " + csv);
      }

      // Cream fitxer draft temporal amb informació del CSV
      // XYZ ZZZ
      String uuidCSVFile = null;
      createSimpleDocCSV(api, NOM_CSV_DOCUMENT_ELECTRONIC, csv, custodyID, uuidCSVFile);

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
          log.warn("Error Esborrant Carpeta " + ec.carpetaID + ": " + rs.getCodigoResultado()
              + "-" + rs.getMsjResultado());
        }

        // TODO Eliminar expedient quan aquest no tengui fills ?????
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

    createUpdateAll(custodyID, custodyParameters, documentCustody, signatureCustody, metadata);
  }

  protected void createUpdateAll(String custodyID, Map<String, Object> custodyParameters,
      DocumentCustody documentCustody, SignatureCustody signatureCustody, Metadata[] metadatas)
      throws CustodyException {

    // boolean overWriteDocument, boolean overWriteSignature, boolean
    // overWriteMetadatas

    final boolean debug = isDebug();
    
    // CHECKS 
    final String nomFitxerDocument; 
    if (documentCustody != null) {
      if (signatureCustody == null) {
        
        
        if (!isPropertyCreateDraft()) {
          //   TODO TRADUIR
          throw new CustodyException("No podem guardar un document pla en mode NO BORRADOR (NO DRAFT)");
        }
      } 
      // ONLY FILE && DETACHED
      nomFitxerDocument  = documentCustody.getName();
    } else {
      // ATTACHED
      nomFitxerDocument = signatureCustody.getName();
    }
    
    // CHECK
    if (NOM_CSV_DOCUMENT_ELECTRONIC.equals(nomFitxerDocument)) {
      // TODO
      throw new CustodyException("El nom de fitxer " + NOM_CSV_DOCUMENT_ELECTRONIC 
          + " no està permés. Canvii el nom del fitxer i torni a intentar-ho");
    }
    

    if (isPropertyTancarExpedient() && isPropertyCreateDraft()) {
      // Son Incompatibles
      throw new CustodyException("La combinació de crear borradors (creatDraft) i de "
          + "tancar expedient (tancarExpedient) són incompatibles."); 
    }
    
    
    try {

      // 1.- Miram si el Document Electrònic Existeix
      ExpedientCarpeta ec = decodeCustodyID(custodyID);

      ApiArchivoDigital apiArxiu = getApiArxiu(custodyParameters);

      // 1.1.- Cercam si hi ha l'expedient
      Resultado<Expediente> expedient = apiArxiu.obtenerExpediente(ec.expedientID);

      if (hiHaError(expedient.getCodigoResultado())) {
        // Checks
        throw new CustodyException("Error llegint expedient amb uuid " + ec.expedientID + "("
            + expedient.getCodigoResultado() + ":" + expedient.getMsjResultado() + ")",
            new Exception());
      }

      // 1.2.- Cercam Fitxers dins la Carpeta (si en té) o directament dins
      // l'expedient
      Map<String, Nodo> nodosByName;
      nodosByName = getNodosByCustodyID(apiArxiu, ec, expedient);

      // 1.3.- Cercam o el document o el fitxer CSV
      String documentElectronicID = null;
      String csv = null;
      Nodo nodoCSV = null;
      {
        FilesInfo filesInfo = getFileInfo(ec, nodosByName);

        if (filesInfo.nodoDocumentElectronic != null) {
          documentElectronicID = filesInfo.nodoDocumentElectronic.getId();
        } else {
          csv = readSimpleDocCSV(apiArxiu, filesInfo.nodoCSV.getId());
          nodoCSV = filesInfo.nodoCSV;
        }
      }

      // =================================== INICIALITZACIO
      Properties metasAndInfo;
      Documento documento;

      final Map<String, Object> generatedMetadataCollection = getMetadadesPerDocument(
          custodyParameters, signatureCustody);

      Map<String, Object> metadataCollection;
      if (documentElectronicID == null) {

        // CREAR DOCUMENT-ElECTRONIC
        metasAndInfo = new Properties();

        documento = new Documento();
        // String idNuevoDocumento = SENSE_VALOR;

        // Configuració paràmetres document
        List<Aspectos> aspects = new ArrayList<Aspectos>();
        aspects.add(Aspectos.INTEROPERABLE);
        aspects.add(Aspectos.TRANSFERIBLE);
        
        
        documento.setId(null);
        documento.setAspects(aspects);

        documento.setEncoding(FORMATO_UTF8);

        metadataCollection = new HashMap<String, Object>();
        metadataCollection.putAll(generatedMetadataCollection);

        // EL CSV s'ha generat en la reserva i s'ha de guardat en
        // el fitxer de INFO. S'ha de recuperar del fitxer de Informacio.
        if (debug) {
          log.info("CODI SEGUR RECUPERAT = " + csv);
        }
        metadataCollection.put(MetadataConstants.ENI_CSV, csv);
        // TODO XYZ ZZZ QUE és AIXÒ !!!!!
        // metadataCollection.put(MetadatosDocumento.DEF_CSV, "Sense definir");

        documento.setMetadataCollection(metadataCollection);

      } else {

        // ACTUALITZAR DOCUMENT-ELECTRONIC

        // Per que hem de treure tot el document. Amb la informació de
        // eni:description n'hi ha suficient ?
        // No ja que per ara hem de suportar el saveDocument i el saveSignature,
        // cosa
        // que fa que s'hagin d'actualitzar signatura i document respectivament.
        final boolean retrieveContent = true;

        Resultado<Documento> docRes = apiArxiu.obtenerDocumento(documentElectronicID,
            retrieveContent);

        // Comprovar resultat operació
        if (hiHaError(docRes.getCodigoResultado())) {
          throw new CustodyException("Error llegint document amb uuid " + documentElectronicID
              + "(" + docRes.getCodigoResultado() + ":" + docRes.getMsjResultado() + ")",
              new Exception());
        }

        documento = docRes.getElementoDevuelto();

        metadataCollection = documento.getMetadataCollection();

        String metasAndInfoStr = (String) metadataCollection
            .get(MetadataConstants.ENI_DESCRIPCION);

        metasAndInfo = stringToProperties(metasAndInfoStr);

        // XYZ ZZZ if (custodyParameters != null)
        // Actualitzam amb les metadades amb les metadades generades
        metadataCollection.putAll(generatedMetadataCollection);

      }

      // ======================== CODI COMU
      if (debug) {
        for (String key : metadataCollection.keySet()) {
          log.info("Metadata Document [" + key + "] => " + metadataCollection.get(key));
        }
      }

      
      if (documentCustody == null) {

        metasAndInfo.remove(FILE_INFO_BASE_DOCUMENT + FILE_INFO_NOM);
        metasAndInfo.remove(FILE_INFO_BASE_DOCUMENT + FILE_INFO_TAMANY);
        metasAndInfo.remove(FILE_INFO_BASE_DOCUMENT + FILE_INFO_MIME);

      } else {
        byte[] data = documentCustody.getData();
        String mimetype = documentCustody.getMime();

        metasAndInfo.setProperty(FILE_INFO_BASE_DOCUMENT + FILE_INFO_NOM,
            documentCustody.getName());
        metasAndInfo.setProperty(FILE_INFO_BASE_DOCUMENT + FILE_INFO_TAMANY,
            String.valueOf(data.length));
        if (mimetype == null) {
          throw new CustodyException("No s'ha definit el mime type del DocumentCustody");
        }
        metasAndInfo.setProperty(FILE_INFO_BASE_DOCUMENT + FILE_INFO_MIME, mimetype);
      }
      

      
      if (signatureCustody == null) {

        metasAndInfo.remove(FILE_INFO_BASE_FIRMA + FILE_INFO_NOM);
        metasAndInfo.remove(FILE_INFO_BASE_FIRMA + FILE_INFO_TAMANY);
        metasAndInfo.remove(FILE_INFO_BASE_FIRMA + FILE_INFO_MIME);

        metasAndInfo.remove(FILE_INFO_BASE_FIRMA + FILE_INFO_SIGNATURE_ATTACHED_DOCUMENT);
        metasAndInfo.remove(FILE_INFO_BASE_FIRMA + FILE_INFO_SIGNATURE_TYPE);

      } else {

        final byte[] data = signatureCustody.getData();
        String mimeType = signatureCustody.getMime();
        
        documento.getAspects().add(Aspectos.FIRMADO);
        
        metasAndInfo.setProperty(FILE_INFO_BASE_FIRMA + FILE_INFO_NOM,
            signatureCustody.getName());
        metasAndInfo.setProperty(FILE_INFO_BASE_FIRMA + FILE_INFO_TAMANY,
            String.valueOf(data.length));
        if (mimeType == null) {
          throw new CustodyException("No s'ha definit el mime type del SignatureCustody");
        }
        metasAndInfo.setProperty(FILE_INFO_BASE_FIRMA + FILE_INFO_MIME, mimeType);

        if (signatureCustody.getAttachedDocument() == null) {
          metasAndInfo.remove(FILE_INFO_BASE_FIRMA + FILE_INFO_SIGNATURE_ATTACHED_DOCUMENT);
        } else {
          metasAndInfo.setProperty(FILE_INFO_BASE_FIRMA
              + FILE_INFO_SIGNATURE_ATTACHED_DOCUMENT,
              String.valueOf(signatureCustody.getAttachedDocument()));
        }
        metasAndInfo.setProperty(FILE_INFO_BASE_FIRMA + FILE_INFO_SIGNATURE_TYPE,
            signatureCustody.getSignatureType());
      }
      

      // Guardar/actualitzar metadades
      if (metadatas != null) {

        for (Metadata metadata : metadatas) {

          String key = metadata.getKey();
          if (key.startsWith("eni:") || key.startsWith("gdib:")) {
            // Metadades eni: o gdib: es guarden com Metadata del Document
            // NOTA: No s'actualitzaran les metadades generades
            if (generatedMetadataCollection.containsKey(key) || MetadataConstants.ENI_DESCRIPCION.equals(key) ) {
              // Es generada i no la podem sobreescriure
              log.warn("La metadada [" + key + "] no es pot guardar ja que és generada pel Sistema");
            } else {
              metadataCollection.put(key, metadata.getValue());
            }
          } else {
            // Processar metadades no eni o gdib: es guarden dins
            // "eni:description"
            metasAndInfo.put(FILE_INFO_BASE_METADATA + key, metadata.getValue());
          }
        }
      }

      // Ara es guarden info del document i signatura més les metadades
      documento.getMetadataCollection().put(MetadataConstants.ENI_DESCRIPCION,
          URLEncoder.encode(propertiesToString(metasAndInfo), "UTF-8"));

      // ============================= MUNTAR DOCUMENTO

      // Muntar Documento segons especificació de l'arxiu Digital CAIB
      // Si la firma electrónica es implícita (tipos de firma: “TF02”, “TF03”,
      // “TF05” y “TF06”), es decir, contenido del documento y firma electrónica
      // coinciden, no es requerido aportar la firma electrónica del documento,
      // bastaría con informar el contenido del mismo.

      // Muntar document segons l'anterior
      
      documento.setName(nomFitxerDocument);
      
      if (documentCustody != null && signatureCustody != null) {
        // DETACHED

        // FILE
        byte[] dataFile = documentCustody.getData();
        documento.setContent(Base64.encode(dataFile));
        documento.setMimetype(documentCustody.getMime());

        // SIGNATURE
        FirmaDocumento firma = new FirmaDocumento();
        final byte[] dataSign = signatureCustody.getData();
        firma.setContent(Base64.encode(dataSign));
        firma.setEncoding(FORMATO_UTF8);
        firma.setMimetype(signatureCustody.getMime());

        documento.setListaFirmas(Arrays.asList(firma));

      } else {

        // ONLY FILE && ATTACHED

        // FILE if ONLY FILE or SIGNATURE if ATTACHED
        byte[] dataFile;
        String mime;

        if (signatureCustody != null) {
          dataFile = signatureCustody.getData();
          mime = signatureCustody.getMime();
        } else {
          dataFile = documentCustody.getData();
          mime = documentCustody.getMime();
        }

        documento.setContent(Base64.encode(dataFile));
        documento.setMimetype(mime);

        documento.setListaFirmas(new ArrayList<FirmaDocumento>());

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

          CreateDraftDocumentResult result = apiArxiu.crearDraftDocument(uuidDestino,
              documento, retrieveContent);

          rd = result.getCreateDraftDocumentResult().getResult();

          final String errorCodi = rd.getCode();
          if (hiHaError(errorCodi)) {
            throw new CustodyException("Error creant Document Draft amb id " + custodyID + "("
                + errorCodi + " - " + rd.getDescription() + ")");
          }

          uuid = result.getCreateDraftDocumentResult().getResParam().getId();

        } else {
          
          CreateDocumentResult result = apiArxiu.crearDocumento(uuidDestino, documento,
              retrieveContent);
          rd = result.getCreateDocumentResult().getResult();

          final String errorCodi = rd.getCode();
          if (hiHaError(errorCodi)) {
            throw new CustodyException("Error creant Document Final amb id " + custodyID + "("
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

        documento.setId(documentElectronicID);
        documento.getAspects().remove(Aspectos.BORRADOR);

        ResultadoSimple rs = apiArxiu.actualizarDocumento(documento);

        if (hiHaError(rs.getCodigoResultado())) {
          throw new CustodyException("Error actualitzant Document amb id " + custodyID
              + "[uuid:" + documentElectronicID + "]" + "(" + rs.getCodigoResultado() + " - "
              + rs.getMsjResultado() + ")");
        }

      }

      // Esborrar fitxer temporal que conté CSV
      if (nodoCSV != null) {
        ResultadoSimple res = apiArxiu.eliminarDocumento(nodoCSV.getId());
        if (hiHaError(res.getCodigoResultado())) {
          log.warn("Error esborrant fitxer temporal de CSV amb uuid " + nodoCSV.getId() + "("
              + res.getCodigoResultado() + ": " + res.getMsjResultado() + ")", new Exception());
        }
      }

      if (debug) {
        Thread.sleep(1500);
        Map<String, Nodo> nodosByName2 = getNodosByCustodyID(apiArxiu, ec, expedient);
        for (String key : nodosByName2.keySet()) {
          log.info("FITXERS A LA CARPETA[" + key + "] = " + nodosByName2.get(key).getId());
        }
      }
      
      if (isPropertyTancarExpedient()) {
         Resultado<String> res = apiArxiu.cerrarExpediente(ec.expedientID);
         if (hiHaError(res.getCodigoResultado())) {
           // TODO que feim, llaçam excepció o warning
           log.error("No s'ha pogut tancar l'expedient amb uuid " + ec.expedientID + ": " 
             + res.getCodigoResultado() + " - " + res.getMsjResultado(), new Exception() );
         }
      }
      

    } catch (CustodyException e) {
      throw e;
    } catch (Exception e) {
      throw new CustodyException("Error creant o actualitzant document electronic "
          + custodyID + ": " + e.getMessage(), e);
    }
  }

  protected FilesInfo getFileInfo(ExpedientCarpeta ec, Map<String, Nodo> nodosByName)
      throws CustodyException {
    
    // Cercarem entre totes els nodes un que és digui NOM_CSV_DOCUMENT_ELECTRONIC
    if (nodosByName.size() == 0) {
      throw new CustodyException("Aquest Expedient/Carpeta ("
          + ec.toString() + ") no conté cap fitxer.", new Exception());
    }
    
    if (nodosByName.size() != 1) {
      
      StringBuffer nodes = new StringBuffer();
      for (Nodo n : nodosByName.values()) {
        if (nodes.length() != 0) {
          nodes.append(", ");
        }
        nodes.append(n.getName() + "(" + n.getId() + ")");
      }
      
      throw new CustodyException("Aquest Expedient/Carpeta ("
          + ec.toString() + ") conté multiples fitxers: " + nodes.toString(), new Exception());
    }
    
    Nodo nodoCSV = nodosByName.get(NOM_CSV_DOCUMENT_ELECTRONIC);

    if (nodoCSV != null && TiposObjetoSGD.DOCUMENTO.equals(nodoCSV.getType())) {
      return new FilesInfo(null, nodoCSV);
    }
    
    String key = nodosByName.keySet().iterator().next();


    Nodo nodo = nodosByName.get(key); // NOM_DOCUMENT_ELECTRONIC);
    
    if (TiposObjetoSGD.DOCUMENTO.equals(nodo.getType())) {
      return new FilesInfo(nodo, null);
    } else {

      StringBuffer nodes = new StringBuffer();
      for (Nodo n : nodosByName.values()) {
        if (nodes.length() != 0) {
          nodes.append(", ");
        }
        nodes.append(n.getName() + "(" + n.getId() + ")");
      }
      throw new CustodyException("No s'ha trobat cap document electrònoc ni fitxer de csv en "
          + "aquest Expedient/Carpeta (" + ec.toString() + "): " + nodes.toString(),
          new Exception());
    }
  }

  // ----------------------------------------------------------------------------
  // ---------------------------- D O C U M E N T ------------------------------
  // ----------------------------------------------------------------------------

  @Override
  @Deprecated
  public void saveDocument(String custodyID, Map<String, Object> custodyParameters,
      DocumentCustody documentCustody) throws CustodyException, NotSupportedCustodyException {

    throw new NotSupportedCustodyException();

    /*
     * 
     * final boolean overWriteDocument = true; final boolean overWriteSignature
     * = false; final boolean overWriteMetadatas = false; final SignatureCustody
     * signatureCustody = null; final Metadata[] metadatas = null;
     * createUpdateAll(custodyID, custodyParameters, documentCustody,
     * overWriteDocument, signatureCustody, overWriteSignature, metadatas,
     * overWriteMetadatas);
     */
  }

  @Override
  @Deprecated
  public void deleteDocument(String custodyID) throws CustodyException,
      NotSupportedCustodyException {

    throw new NotSupportedCustodyException();

    /*
     * 
     * final boolean overWriteDocument = true; final boolean overWriteSignature
     * = false; final boolean overWriteMetadatas = false; final SignatureCustody
     * signatureCustody = null; final DocumentCustody documentCustody = null;
     * final Metadata[] metadatas = null; // No s'utilitza final Map<String,
     * Object> custodyParameters = null; createUpdateAll(custodyID,
     * custodyParameters, documentCustody, overWriteDocument, signatureCustody,
     * overWriteSignature, metadatas, overWriteMetadatas);
     */
  }

  @Override
  public byte[] getDocument(String custodyID) throws CustodyException {

    final boolean retrieveData = true;
    DocumentCustody dc = internalGetDocument(custodyID, retrieveData);
    return (dc == null) ? null : dc.getData();

  }

  @Override
  public DocumentCustody getDocumentInfo(String custodyID) throws CustodyException {

    final boolean retrieveData = true;
    return internalGetDocument(custodyID, retrieveData);

  }

  @Override
  public DocumentCustody getDocumentInfoOnly(String custodyID) throws CustodyException {
    final boolean retrieveData = false;
    return internalGetDocument(custodyID, retrieveData);
  }

  protected DocumentCustody internalGetDocument(String custodyID, boolean retrieveContent)
      throws CustodyException {

    try {
  
      ApiArchivoDigital apiArxiu = getApiArxiu(null);

      Map<String, Nodo> nodos = getNodosByCustodyID(apiArxiu, custodyID);
      
      FilesInfo fileInfo = getFileInfo(decodeCustodyID(custodyID), nodos);
      
      Nodo nodo = fileInfo.nodoDocumentElectronic;
      
      // No s'ha guardat cap document electrònic
      if (nodo == null) {
        return null;
      }


      FullInfoDocumentElectronic fullInfo;
      fullInfo = getFullInfoOfDocumentElectronic(apiArxiu, nodo.getId(), retrieveContent);

      Properties p = fullInfo.infoAndMetas;

      String nom = p.getProperty(FILE_INFO_BASE_DOCUMENT + FILE_INFO_NOM);

      if (nom == null) {
        return null;
      }

      DocumentCustody doc = new DocumentCustody();
      doc.setName(nom);
      doc.setLength(Long.parseLong(p.getProperty(FILE_INFO_BASE_DOCUMENT + FILE_INFO_TAMANY)));
      doc.setMime(p.getProperty(FILE_INFO_BASE_DOCUMENT + FILE_INFO_MIME));

      if (retrieveContent) {
        Documento documento = fullInfo.documento;
        byte[] data = Base64.decode(documento.getContent());
        doc.setData(data);
        doc.setLength(data.length);
      }

      return doc;

    } catch (IOException e) {
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
  @Deprecated
  public void saveSignature(String custodyID, Map<String, Object> custodyParameters,
      SignatureCustody signatureCustody) throws CustodyException, NotSupportedCustodyException {

    throw new NotSupportedCustodyException();

    /*
     * final boolean overWriteDocument = false; final boolean overWriteSignature
     * = true; final boolean overWriteMetadatas = false; final DocumentCustody
     * documentCustody = null; final Metadata[] metadatas = null;
     * createUpdateAll(custodyID, custodyParameters, documentCustody,
     * overWriteDocument, signatureCustody, overWriteSignature, metadatas,
     * overWriteMetadatas);
     */

  }

  @Override
  public byte[] getSignature(String custodyID) throws CustodyException {

    boolean retrieveContent = true;
    SignatureCustody sc = internalGetSignature(custodyID, retrieveContent);

    return (sc == null) ? null : sc.getData();

  }

  @Override
  public SignatureCustody getSignatureInfo(String custodyID) throws CustodyException {

    boolean retrieveContent = true;
    return internalGetSignature(custodyID, retrieveContent);
  }

  @Override
  public SignatureCustody getSignatureInfoOnly(String custodyID) throws CustodyException {
    boolean retrieveContent = false;
    return internalGetSignature(custodyID, retrieveContent);

  }

  public SignatureCustody internalGetSignature(String custodyID, boolean retrieveContent)
      throws CustodyException {
    try {
      ApiArchivoDigital apiArxiu = getApiArxiu(null);

      Map<String, Nodo> nodosByName = getNodosByCustodyID(apiArxiu, custodyID);

      FilesInfo filesInfo = getFileInfo(decodeCustodyID(custodyID), nodosByName);
      
      Nodo nodo = filesInfo.nodoDocumentElectronic; // NOM_DOCUMENT_ELECTRONIC);

      if (nodo == null) {
        return null;
      }

      String uuid = nodo.getId();

      FullInfoDocumentElectronic fullInfo;
      fullInfo = getFullInfoOfDocumentElectronic(apiArxiu, uuid, retrieveContent);

      // Check Values

      Properties p = fullInfo.infoAndMetas;

      String nom = p.getProperty(FILE_INFO_BASE_FIRMA + FILE_INFO_NOM);

      if (nom == null) {
        return null;
      }

      SignatureCustody sign = new SignatureCustody();
      sign.setName(nom);
      sign.setLength(Long.parseLong(p.getProperty(FILE_INFO_BASE_FIRMA + FILE_INFO_TAMANY)));
      sign.setMime(p.getProperty(FILE_INFO_BASE_FIRMA + FILE_INFO_MIME));

      String attached = p.getProperty(FILE_INFO_BASE_FIRMA
          + FILE_INFO_SIGNATURE_ATTACHED_DOCUMENT);
      if (attached == null) {
        sign.setAttachedDocument(null);
      } else {
        sign.setAttachedDocument(Boolean.parseBoolean(attached));
      }

      sign.setSignatureType(p.getProperty(FILE_INFO_BASE_FIRMA + FILE_INFO_SIGNATURE_TYPE));

      if (retrieveContent) {
        //
        String nomFile = p.getProperty(FILE_INFO_BASE_DOCUMENT + FILE_INFO_NOM);
        byte[] data;
        if (nomFile == null) {
          // ATTACHED => Dins content trobarem la firma
          data = Base64.decode(fullInfo.documento.getContent());
          sign.setData(data);
        } else {
          // DETACHED => Dins ListaFirmas trobarem la firma
          FirmaDocumento firma = fullInfo.documento.getListaFirmas().get(0);
          data = Base64.decode(firma.getContent());
          sign.setData(data);
        }
        sign.setLength(data.length);
      }

      return sign;

    } catch (IOException e) {
      throw new CustodyException(
          "Error intentant obtenir contingut del Document amb custodyID " + custodyID + ": "
              + e.getMessage(), e);
    }

  }

  @Override
  @Deprecated
  public void deleteSignature(String custodyID) throws CustodyException,
      NotSupportedCustodyException {

    throw new NotSupportedCustodyException();

    /*
     * final boolean overWriteDocument = false; final boolean overWriteSignature
     * = true; final boolean overWriteMetadatas = false; final SignatureCustody
     * signatureCustody = null; final DocumentCustody documentCustody = null;
     * final Metadata[] metadatas = null; // No s'utilitza final Map<String,
     * Object> custodyParameters = null; createUpdateAll(custodyID,
     * custodyParameters, documentCustody, overWriteDocument, signatureCustody,
     * overWriteSignature, metadatas, overWriteMetadatas);
     */

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
    return false;
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

    updateMetadata(custodyID, new Metadata[] { metadata }, custodyParameters);

  }

  @Override
  public void updateMetadata(String custodyID, final Metadata[] metadatas,
      Map<String, Object> custodyParameters) throws CustodyException,
      NotSupportedCustodyException, MetadataFormatException {

    if (true) {
      throw new NotSupportedCustodyException(); // XYZ ZZZ
    }

    /*
     * ApiArchivoDigital apiArxiu = getApiArxiu(custodyParameters);
     * 
     * apiArxiu.obtenerDocumento(arg0, arg1)
     * 
     * Map<String, List<Metadata>> map = internalGetAllMetadatas(custodyID,
     * custodyParameters);
     * 
     * // mesclam metadades de APIDocumentCustody for (Metadata metadata :
     * metadataList) { String key = metadata.getKey(); if
     * (key.startsWith("eni:") || key.startsWith("gdib:")) { if () }
     */
    /*
     * DocumentCustody documentCustody = null; SignatureCustody signatureCustody
     * = null;
     * 
     * final boolean overWriteDocument = false; final boolean overWriteSignature
     * = false; final boolean overWriteMetadatas = true;
     * 
     * 
     * createUpdateAll(custodyID, custodyParameters, documentCustody,
     * overWriteDocument, signatureCustody, overWriteSignature, metadatas,
     * overWriteMetadatas);
     */

    /*
     * new MetadataAction(apiArxiu, custodyID) {
     * 
     * @Override public boolean modificarMetadades(Properties metadades) throws
     * Exception { if (metadataList != null && metadataList.length != 0) { for
     * (Metadata meta : metadataList) { metadades.setProperty(meta.getKey(),
     * meta.getValue()); } return true; } else { return false; } } }.doAction();
     */

  }

  @Override
  public Map<String, List<Metadata>> getAllMetadata(String custodyID) throws CustodyException,
      NotSupportedCustodyException {

    Map<String, List<Metadata>> map = internalGetAllMetadatas(custodyID, null);

    return map;

    /*
     * // TODO S'ha de gestionar multipes valors associats a un única clau
     * ApiArchivoDigital apiArxiu = getApiArxiu(null); Properties properties =
     * new MetadataAction(apiArxiu, custodyID) {
     * 
     * @Override public boolean modificarMetadades(Properties metadades) throws
     * Exception { return false; } }.doAction();
     * 
     * Map<String, List<Metadata>> result = new HashMap<String,
     * List<Metadata>>();
     * 
     * for (Object keyObj : properties.keySet()) { String key = (String) keyObj;
     * List<Metadata> list = new ArrayList<Metadata>(); list.add(new
     * Metadata(key, properties.getProperty(key))); result.put(key, list); }
     * return result;
     */
  }

  /**
   * Si el document existeix llegim les metadades
   * 
   * @return
   */
  protected Map<String, List<Metadata>> internalGetAllMetadatas(String custodyID,
      Map<String, Object> custodyParameters) throws CustodyException {
    ApiArchivoDigital apiArxiu = getApiArxiu(custodyParameters);

    try {
      Map<String, Nodo> nodosByName = getNodosByCustodyID(apiArxiu, custodyID);

      ExpedientCarpeta ec = decodeCustodyID(custodyID);

      FilesInfo filesInfo = getFileInfo(ec, nodosByName);

      Map<String, List<Metadata>> map = new HashMap<String, List<Metadata>>();

      if (filesInfo.nodoDocumentElectronic != null) {
        // String uuid = filesInfo.nodoDocumentElectronic.getId();

        // Properties prop = getPropertiesFromCAIBDocument_(apiArxiu, uuid);

        // Afegir metadades ENI i GDIB
        Nodo nodoDocElec = filesInfo.nodoDocumentElectronic;
        Map<String, Object> metadataDocs = null;

        Resultado<Documento> res = apiArxiu.obtenerDocumento(nodoDocElec.getId(), false);
        metadataDocs = res.getElementoDevuelto().getMetadataCollection();

        String descInfo = (String) metadataDocs.get(MetadataConstants.ENI_DESCRIPCION);

        Properties prop = stringToProperties(descInfo);

        // Només extreure Metadades
        for (Entry<Object, Object> entry : prop.entrySet()) {
          String key = (String) entry.getKey();

          if (key.startsWith(FILE_INFO_BASE_METADATA)) {
            key = key.substring(FILE_INFO_BASE_METADATA.length() + 1);

            log.info("NEW KEY = " + key);

            map.put(key, Arrays.asList(new Metadata(key, (String) entry.getValue())));

          }

        }

        // metadades del document Electrònic

        for (String key : metadataDocs.keySet()) {
          if (!key.equals(MetadataConstants.ENI_DESCRIPCION)) {
            Object value = metadataDocs.get(key);

            map.put(key, Arrays.asList(new Metadata(key, String.valueOf(value))));
          }
        }

      } else {
        String csv = null;
        csv = readSimpleDocCSV(apiArxiu, filesInfo.nodoCSV.getId());

        final String key = MetadataConstants.ENI_CSV;

        map.put(key, Arrays.asList(new Metadata(key, csv)));

      }

      return map;

    } catch (CustodyException ce) {
      throw ce;
    } catch (Exception e) {
      throw new CustodyException("Error intentant obtenir les metadades de " + custodyID
          + ": " + e.getMessage(), e);
    }

  }

  @Override
  public List<Metadata> getMetadata(String custodyID, String key) throws CustodyException,
      NotSupportedCustodyException {

    Map<String, List<Metadata>> map = internalGetAllMetadatas(custodyID, null);

    return map.get(key);

    // TODO S'ha de gestionar multipes valors associats a un única clau
    /*
     * ApiArchivoDigital apiArxiu = getApiArxiu(null); Properties properties =
     * new MetadataAction(apiArxiu, custodyID) {
     * 
     * @Override public boolean modificarMetadades(Properties metadades) throws
     * Exception { return false; } }.doAction();
     * 
     * List<Metadata> list = new ArrayList<Metadata>(); String value =
     * properties.getProperty(key);
     * 
     * if (value != null) { list.add(new Metadata(key, value)); }
     * 
     * return list;
     */
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

    /*
     * ApiArchivoDigital apiArxiu = getApiArxiu(null); new
     * MetadataAction(apiArxiu, custodyID) {
     * 
     * @Override public boolean modificarMetadades(Properties metadades) throws
     * Exception { metadades.clear(); return true; } }.doAction();
     */

  }

  @Override
  public List<Metadata> deleteMetadata(String custodyID, final String key)
      throws CustodyException {

    throw new CustodyException("NotSupported"); // XYZ ZZZ

    /*
     * ApiArchivoDigital apiArxiu = getApiArxiu(null);
     * 
     * MetadataAction m = new MetadataAction(apiArxiu, custodyID) {
     * 
     * @Override public boolean modificarMetadades(Properties metadades) throws
     * Exception { Object obj = metadades.remove(key); if (obj == null) { return
     * false; } else { this.outputParameter = obj; return true; } } };
     * m.doAction(); List<Metadata> list = new ArrayList<Metadata>(); if
     * (m.outputParameter != null) { list.add(new Metadata(key, (String)
     * m.outputParameter)); }
     * 
     * return list;
     */
  }

  @Override
  public List<Metadata> deleteMetadata(String custodyID, final String[] keys)
      throws CustodyException {

    throw new CustodyException("NotSupported"); // XYZ ZZZ

    /*
     * ApiArchivoDigital apiArxiu = getApiArxiu(null);
     * 
     * MetadataAction m = new MetadataAction(apiArxiu, custodyID) {
     * 
     * @Override public boolean modificarMetadades(Properties metadades) throws
     * Exception {
     * 
     * List<Metadata> list = new ArrayList<Metadata>(); for (String key : keys)
     * { Object obj = metadades.remove(key); if (obj != null) { list.add(new
     * Metadata(key, (String) obj)); } }
     * 
     * this.outputParameter = list; if (list.isEmpty()) { return false; } else {
     * return true; } } }; m.doAction();
     * 
     * return (List<Metadata>) m.outputParameter;
     */
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
    
    final boolean debug = isDebug();

    // MetadatosDocumento.CODIGO_APLICACION_TRAMITE
    String codigoAplicacion = getPropertyCodiAplicacio();
    llistaMetadades.put(MetadataConstants.ENI_APP_TRAMITE_DOC, codigoAplicacion);

    // MetadatosDocumento.ESTADO_ELABORACION : EE01,EE02, EE03, EE04, EE99
    String estadoElaboracion = processEL(getPropertyEstatElaboracioEL(), custodyParameters);
    if(debug) {
      log.info("getMetadadesPerDocument()::ESTADO_ELABORACION = ]" + estadoElaboracion + "[");
    }
    llistaMetadades.put(MetadataConstants.ENI_ESTADO_ELABORACION, estadoElaboracion);

    // (MetadatosDocumento.TIPO_DOC_ENI: TD01, TD02, TD03, ...
    String tipoDocEni = processEL(getPropertyTipusDocumentalEL(), custodyParameters);
    if(debug) {
      log.info("getMetadadesPerDocument()::TIPO_DOC_ENI = ]" + tipoDocEni + "[");
    }
    llistaMetadades.put(MetadataConstants.ENI_TIPO_DOCUMENTAL, tipoDocEni);

    if (signatureCustody != null) {
      String fileName = signatureCustody.getName();

      int i = fileName.lastIndexOf('.');
      String extensio = null;
      if (i != -1) {
        extensio = fileName.substring(i + 1).toLowerCase();
      }

      if (extensio != null) {
        if(debug) {
          log.info("getMetadadesPerDocument()::ENI_EXTENSION_FORMATO = ]" + extensio + "[");
        }
        llistaMetadades.put(MetadataConstants.ENI_EXTENSION_FORMATO, extensio);

        FormatosFichero format = FORMATS_BY_EXTENSION.get(extensio);

        if (format != null) {
          if(debug) {
            log.info("getMetadadesPerDocument()::ENI_NOMBRE_FORMATO = ]" + format + "[");
          }
          llistaMetadades.put(MetadataConstants.ENI_NOMBRE_FORMATO, format);
        }

      }

      // Afegir propietats associades al tipus de firma
      String tipusFirma = signatureCustody.getSignatureType();
      TiposFirma tipo = TIPOFIRMA_SIMPLE.get(tipusFirma);
      if (tipo == null) {
        tipo = TIPOFIRMA_COMPLEXE.get(tipusFirma + "_"
            + signatureCustody.getAttachedDocument());
      }
      if (tipo != null) {
        if(debug) {
          log.info("getMetadadesPerDocument()::ENI_TIPO_FIRMA = ]" + tipo.getValue() + "[");
        }
        llistaMetadades.put(MetadataConstants.ENI_TIPO_FIRMA, tipo.getValue());
      }

      // Afegir propietats associades al Perfil de Firma
      // (BASELINE B-Level,  EPES, T, C, X, XL, A, LTV,  BASELINE LT-Level,
      // BASELINE LTA-Level, BASELINE T-LevelLTV)
      String perfilFirma = processEL(getPropertyPerfilFirmaEL(), custodyParameters);
      if (debug) {
        log.info("getMetadadesPerDocument()::PERFIL FIRMA = ]" + perfilFirma + "[");
      }
      llistaMetadades.put(MetadataConstants.ENI_PERFIL_FIRMA, perfilFirma);

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
        String ciutadaNom = processOptionalProperty("SolicitantNom",
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

        // + info peticio
        cabecera.setNombreProcedimiento(processOptionalProperty("NomProcediment",
            getPropertyNomProcedimentEL(), custodyParameters));

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
  private String processOptionalProperty(String titol, String propertyValue,
      Map<String, Object> custodyParameters) {

    if (propertyValue == null || propertyValue.trim().length() == 0) {
      return propertyValue;
    }
    if (custodyParameters == null) {
      custodyParameters = new HashMap<String, Object>();
    }

    try {
      return AbstractDocumentCustodyPlugin.processExpressionLanguage(propertyValue,
          custodyParameters);
    } catch (CustodyException e) {
      log.warn(
          "S'ha intentat processar la propietat titulada " + titol + " amb valor "
              + propertyValue + " però s'ha produït un error al fer el processament EL: "
              + e.getMessage(), e);
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


  protected String propertiesToString(Properties prop) throws Exception {
    StringWriter writer = new StringWriter();
    prop.store(writer, "!!! NO MODIFICAR - DO NOT MODIFY !!!");
    return writer.getBuffer().toString();
  }

  protected Properties stringToProperties(String str) throws IOException {
    Properties prop = new Properties();
    if (str == null || str.trim().length() == 0) {
      log.warn("La propietat que conte informació de DocumentCustody, SignatureCustody"
          + " i Metadades val null o està buida", new Exception());

    } else {
      String strOK = URLDecoder.decode(str, "UTF-8");
      prop.load(new StringReader(strOK));
    }
    return prop;
  }

  public void createSimpleDocCSV(ApiArchivoDigital api, String nom, String content,
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


  protected String readSimpleDocCSV(ApiArchivoDigital api, String uuidDoc) throws IOException {

    final boolean retrieveContent = true;
    Resultado<Documento> resultat = api.obtenerDocumento(uuidDoc, retrieveContent);

    if (hiHaError(resultat.getCodigoResultado())) {
      log.error("No s'ha pogut llegir fitxer de CSV amb uuid " + uuidDoc + "("
          + resultat.getCodigoResultado() + ":" + resultat.getMsjResultado() + ")");
      return null;
    }

    // TODO Check errors
    Documento doc = resultat.getElementoDevuelto();
    if (doc == null) {
      return null;
    } else {
      return new String(Base64.decode(doc.getContent()));
    }
  }


  protected Map<String, Nodo> getNodosByCustodyID(ApiArchivoDigital apiArxiu, String custodyID)
      throws IOException, CustodyException {

    ExpedientCarpeta ec = decodeCustodyID(custodyID);

    // Cercam si hi ha un document
    List<Nodo> nodos;
    if (ec.carpetaID == null) {
      Resultado<Expediente> expedient = apiArxiu.obtenerExpediente(ec.expedientID);

      if (hiHaError(expedient.getCodigoResultado())) {
        throw new CustodyException(
            "Error intentant obtenir informació de l'expedient amb uuid " + ec.expedientID
                + ": " + expedient.getCodigoResultado() + "-" + expedient.getMsjResultado());
      }

      nodos = expedient.getElementoDevuelto().getChilds();
    } else {
      // log.info("XYZ Llegim Carpeta amb uuid " + ec.carpetaID);
      Resultado<Directorio> dir = apiArxiu.obtenerDirectorio(ec.carpetaID);
      // log.info("XYZ Llegim Carpeta : codi resultat " +
      // dir.getCodigoResultado());
      if (hiHaError(dir.getCodigoResultado())) {
        throw new CustodyException(
            "Error intentant obtenir informació de la carpeta amb uuid " + ec.carpetaID + ": "
                + dir.getCodigoResultado() + "-" + dir.getMsjResultado());
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
      ExpedientCarpeta ec, Resultado<Expediente> expedient) throws IOException,
      CustodyException {
    Map<String, Nodo> nodosByName;
    {
      List<Nodo> nodos;
      if (ec.carpetaID == null) {
        nodos = expedient.getElementoDevuelto().getChilds();
      } else {
        // log.info("Llegim Carpeta amb uuid " + ec.carpetaID);
        Resultado<Directorio> dir = apiArxiu.obtenerDirectorio(ec.carpetaID);

        // log.info("Llegim Carpeta : codi resultat " +
        // dir.getCodigoResultado());
        if (hiHaError(dir.getCodigoResultado())) {
          throw new CustodyException(
              "Error intentant obtenir inforació de la carpeta amb uuid " + ec.carpetaID
                  + ": " + dir.getCodigoResultado() + "-" + dir.getMsjResultado());
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

    try {
      Map<String, List<Metadata>> metas = internalGetAllMetadatas(custodyID, custodyParameters);

      List<Metadata> list = metas.get(MetadataConstants.ENI_CSV);

      return list.get(0).getValue();

    } catch (CustodyException ce) {
      throw ce;
    } catch (Exception e) {
      throw new CustodyException("Error intentant obtenir el CSV de la custòdia amb id "
          + custodyID + ": " + e.getMessage(), e);
    }

  }

  /*
   * protected Properties getPropertiesFromCAIBDocument_All(ApiArchivoDigital
   * apiArxiu, String uuid) throws Exception, CustodyException { final boolean
   * custodyMetadatas = true; final boolean docElectronicMetadatas = true;
   * 
   * Properties prop = getPropertiesFromCAIBDocument(apiArxiu, uuid,
   * custodyMetadatas, docElectronicMetadatas); return prop; }
   * 
   * 
   * protected Properties getPropertiesFromCAIBDocument_Info(ApiArchivoDigital
   * apiArxiu, String uuid, boolean retrieveData) throws Exception,
   * CustodyException { final boolean custodyMetadatas = true; final boolean
   * docElectronicMetadatas = false;
   * 
   * Properties prop = getPropertiesFromCAIBDocument(apiArxiu, uuid,
   * custodyMetadatas, docElectronicMetadatas); return prop; }
   */

  protected FullInfoDocumentElectronic getFullInfoOfDocumentElectronic(
      ApiArchivoDigital apiArxiu, String uuid, boolean retrieveContent)
      throws CustodyException, IOException {
    Resultado<Documento> doc = apiArxiu.obtenerDocumento(uuid, retrieveContent);

    if (hiHaError(doc.getCodigoResultado())) {
      throw new CustodyException(doc.getCodigoResultado() + ": " + doc.getMsjResultado());
    }

    Properties prop = new Properties();

    // if (custodyMetadatas)
    {

      Map<String, Object> metas = doc.getElementoDevuelto().getMetadataCollection();

      String infoAndMetasStr = (String) metas.get(MetadataConstants.ENI_DESCRIPCION);

      prop = stringToProperties(infoAndMetasStr);

    }

    return new FullInfoDocumentElectronic(prop, doc.getElementoDevuelto());
  }

  protected Properties getMetadadesSenseEni(Properties metadades,
      Map<String, Object> metadataDocs) {
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
    boolean debug = isDebug();

    for (Object k : new ArrayList<Object>(metadadesSenseEni.keySet())) {
      String key = (String) k;
      if (key.startsWith("eni:") || key.startsWith("gdib:")) {
        if (debug) {
          log.warn("La metadada " + key + " no serà actualitzada !!!");
        }
        // keysEniGdibToDelete.add(key);
        metadadesSenseEni.remove(k);
      }
    }
    return metadadesSenseEni;
  }

  /**
   * 
   * @author anadal
   *
   */
  public class FilesInfo {

    public final Nodo nodoDocumentElectronic;

    public final Nodo nodoCSV;

    /**
     * @param nodoDocumentElectronic
     * @param nodoCSV
     */
    public FilesInfo(Nodo nodoDocumentElectronic, Nodo nodoCSV) {
      super();
      this.nodoDocumentElectronic = nodoDocumentElectronic;
      this.nodoCSV = nodoCSV;
    }

  }

  public class FullInfoDocumentElectronic {

    public final Properties infoAndMetas;

    public final Documento documento;

    /**
     * @param infoAndMetas
     * @param documento
     */
    public FullInfoDocumentElectronic(Properties infoAndMetas, Documento documento) {
      super();
      this.infoAndMetas = infoAndMetas;
      this.documento = documento;
    }

  }

  /**
   * 
   * @author anadal
   *
   */
  /*
   * public abstract class MetadataAction {
   * 
   * protected final ApiArchivoDigital apiArxiu;
   * 
   * protected final String custodyID;
   * 
   * public Object outputParameter = null;
   * 
   * 
   * public MetadataAction(ApiArchivoDigital apiArxiu, String custodyID) {
   * super(); this.apiArxiu = apiArxiu; this.custodyID = custodyID; }
   * 
   * public Properties doAction() throws CustodyException {
   * 
   * try { Map<String, Nodo> nodosByName = getNodosByCustodyID(apiArxiu,
   * custodyID); Nodo nodoMetas = nodosByName.get(NOM_METADADES); Properties
   * metadades = readPropertiesFromSimpleDoc(apiArxiu, nodoMetas.getId());
   * 
   * // Afegir metadades ENI i GDIB Nodo nodoDocElec =
   * nodosByName.get(NOM_DOCUMENT_ELECTRONIC); Map<String, Object> metadataDocs
   * = null; if (nodoDocElec != null) { Resultado<Documento> res =
   * apiArxiu.obtenerDocumento(nodoDocElec.getId(), false); metadataDocs =
   * res.getElementoDevuelto().getMetadataCollection();
   * 
   * for (String key : metadataDocs.keySet()) { Object value =
   * metadataDocs.get(key); metadades.setProperty(key, String.valueOf(value)); }
   * 
   * }
   * 
   * // metadatas.setProperty(metadata.getKey(), metadata.getValue()); if
   * (modificarMetadades(metadades)) {
   * 
   * Properties metadadesSenseEni = getMetadadesSenseEni(metadades,
   * metadataDocs);
   * 
   * // TODO Falta actualitzar metadades del document
   * 
   * // Actualitzam metadades createSimpleDoc(apiArxiu, NOM_METADADES,
   * metadadesSenseEni, custodyID, nodoMetas.getId());
   * 
   * }
   * 
   * return metadades;
   * 
   * } catch (CustodyException ce) { throw ce; } catch (Exception e) { throw new
   * CustodyException("Error desconegut: " + e.getMessage(), e); } }
   * 
   * 
   * 
   * public abstract boolean modificarMetadades(Properties metadades) throws
   * Exception;
   * 
   * }
   */

}
