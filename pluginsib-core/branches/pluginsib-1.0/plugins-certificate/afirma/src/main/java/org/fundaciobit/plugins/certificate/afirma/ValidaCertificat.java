package org.fundaciobit.plugins.certificate.afirma;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.Base64;
import org.apache.log4j.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.fundaciobit.plugins.certificate.InformacioCertificat;
import org.fundaciobit.plugins.certificate.ResultatValidacio;



/**
 * 
 * @author anadal
 *
 */
public class ValidaCertificat {

  public static final int MODE_VALIDACIO_SIMPLE = 0;
  public static final int MODE_VALIDACIO_AMB_REVOCACIO = 1;
  public static final int MODE_VALIDACIO_CADENA = 2;
  
  
  private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd EEE HH:mm:ss Z", new Locale("es"));

  protected final Logger log = Logger.getLogger(getClass());
  
  private final String baseUrl;
  private final String aplicacioId;

  private final ClientHandler clientHandler;



  private ValidaCertificat(
      String baseUrl,
      String aplicacioId,
      ClientHandler clientHandler) {
    this.baseUrl = baseUrl;
    this.aplicacioId = aplicacioId;
    this.clientHandler = clientHandler;
  }



  public ValidaCertificat(String base_url, String application_id, String username,
      String password) {
    this(base_url, application_id, new ClientHandlerUsernamePassword(username, password));
  }
  
  
  public ValidaCertificat(String base_url, String application_id,
      String keystoreLocation,
      String keystoreType,
      String keystorePassword,
      String keystoreCertAlias,
      String keystoreCertPassword) {
    
    this(base_url, application_id, 
        new ClientHandlerCertificate(keystoreLocation, keystoreType,
            keystorePassword, keystoreCertAlias, keystoreCertPassword));
  }
  
  

  public String getBaseUrl() {
    return baseUrl;
  }

  public String getAplicacioId() {
    return aplicacioId;
  }
  
  
  public ResultatValidacio validar(X509Certificate certificate, boolean obtenirDadesCertificat,
      int modeValidacio) throws Exception {
    return validar(certificate.getEncoded(), obtenirDadesCertificat, modeValidacio);
  }
  
  
  
  public ResultatValidacio validar(byte[] data, boolean obtenirDadesCertificat,
      int modeValidacioInt ) throws Exception, CheckCertificateException {

    String certificatBase64 = Base64.encode(data);

      String modeValidacio = String.valueOf(modeValidacioInt);
      String respostaXml = cridarValidarCertificado(
          certificatBase64,
          obtenirDadesCertificat, modeValidacio);
    
      System.out.println(respostaXml);
      
      //ValidarCertificatResponse resposta = new ValidarCertificatResponse();
      Document document = obtenirDocumentXmlResposta(respostaXml);
      String root = "/mensajeSalida/respuesta/ResultadoProcesamiento/ResultadoValidacion/";
      Node resultado = document.selectSingleNode(root + "resultado");
      if (resultado != null) {
        
        ResultatValidacio resultatValidacio = new ResultatValidacio();
        
        resultatValidacio.setResultatValidacioCodi(Integer.parseInt(resultado.getText()));
        Node descripcio = document.selectSingleNode(root + "descripcion");
        if (descripcio != null) {
          resultatValidacio.setResultatValidacioDescripcio(descripcio.getText());
        }
        
        //resposta.setEstat(ValidarSignaturaResponse.ESTAT_OK);
        //resposta.setValid(true);
        //Node descripcion = document.selectSingleNode("/mensajeSalida/respuesta/ResultadoProcesamiento/ResultadoValidacion/descripcion");
        
        if (obtenirDadesCertificat) {
          Node infoCertificado = document.selectSingleNode("/mensajeSalida/respuesta/ResultadoProcesamiento/InfoCertificado");
          resultatValidacio.setInformacioCertificat(getDadesCertificat(infoCertificado));
        }
        return resultatValidacio;

      } else {

        Node codigoError = document.selectSingleNode("/mensajeSalida/respuesta/Excepcion/codigoError");
        Node descripcionError = document.selectSingleNode("/mensajeSalida/respuesta/Excepcion/descripcion");
        Node excepcionAsociada = document.selectSingleNode("/mensajeSalida/respuesta/Excepcion/excepcionAsociada");
        throw new CheckCertificateException(codigoError == null? "" : codigoError.getText(),
            descripcionError == null? null : descripcionError.getText(),
            excepcionAsociada == null? null : excepcionAsociada.getText());
      }      
    
  }




  @SuppressWarnings("unchecked")
  private InformacioCertificat getDadesCertificat(
      Node infoCertificado)  {
    if (infoCertificado == null)
      return null;
    InformacioCertificat dades = new InformacioCertificat();
    List<Node> llistaIds = infoCertificado.selectNodes("Campo/idCampo");
    List<Node> llistaValors = infoCertificado.selectNodes("Campo/valorCampo");
    for (int i = 0; i < llistaIds.size(); i++) {
      String key = llistaIds.get(i).getText();
      String value = llistaValors.get(i).getText();
      if ("tipoCertificado".equalsIgnoreCase(key))
        dades.setTipusCertificat (value);
      if ("subject".equalsIgnoreCase(key))
        dades.setSubject(value);
      if ("NombreApellidosResponsable".equalsIgnoreCase(key))
        dades.setNomCompletResponsable(value);
      if ("nombreResponsable".equalsIgnoreCase(key))
        dades.setNomResponsable(value);
      if ("primerApellidoResponsable".equalsIgnoreCase(key))
        dades.setPrimerLlinatgeResponsable(value);
      if ("segundoApellidoResponsable".equalsIgnoreCase(key))
        dades.setSegonLlinatgeResponsable(value);
      if ("NIFResponsable".equalsIgnoreCase(key))
        dades.setNifResponsable(value);
      if ("idEmisor".equalsIgnoreCase(key))
        dades.setEmissorID(value);
      if ("NIF-CIF".equalsIgnoreCase(key))
        dades.setUnitatOrganitzativaNifCif(value);
      if ("email".equalsIgnoreCase(key))
        dades.setEmail(value);
      if ("fechaNacimiento".equalsIgnoreCase(key))
        dades.setDataNaixement(value);
      if ("razonSocial".equalsIgnoreCase(key))
        dades.setRaoSocial(value);
      if ("clasificacion".equalsIgnoreCase(key))
        dades.setClassificacio(Integer.parseInt(value));
      if ("numeroSerie".equalsIgnoreCase(key))
        dades.setNumeroSerie(new BigInteger(value));
      if ("usoCertificado".equalsIgnoreCase(key))
        dades.setUsCertificat(value);
      if ("extensionUsoCertificado".equalsIgnoreCase(key))
        dades.setUsCertificatExtensio(value);
      if ("validoHasta".equalsIgnoreCase(key)) {
        try {
          dades.setValidFins(SDF.parse(value));
        } catch (ParseException e) {
          log.error("Error desconegut parsejant la data de final " + value, e);
        }
      }
      if ("validoDesde".equalsIgnoreCase(key)) {
        try {
          dades.setValidDesDe(SDF.parse(value));
        } catch (ParseException e) {
          log.error("Error desconegut parsejant la data d'inici " + value, e);
        }
      }

      if ("politica".equalsIgnoreCase(key))
        dades.setPolitica(value);
      
      if ("versionPolitica".equalsIgnoreCase(key))
        dades.setPoliticaVersio(value); 
      
      if ("idPolitica".equalsIgnoreCase(key))
        dades.setPoliticaID(value); 
      
      if ("OrganizacionEmisora".equalsIgnoreCase(key))
        dades.setEmissorOrganitzacio(value); 
      
      if ("pais".equalsIgnoreCase(key))
        dades.setPais(value); 
      
      if ("unidadOrganizativa".equalsIgnoreCase(key))
        dades.setUnitatOrganitzativa(value); 

    }
    return dades;
  }

  private Document obtenirDocumentXmlResposta(String xmlResposta) throws DocumentException  {
    // Llevam tots els atributs de mensajeSalida
    int tall1 = xmlResposta.indexOf("mensajeSalida") + "mensajeSalida".length();
    int tall2 = xmlResposta.indexOf(">", tall1);
    String xmlCorrecte = xmlResposta.substring(0, tall1) + xmlResposta.substring(tall2);
    return DocumentHelper.parseText(xmlCorrecte);
  }

  private String cridarValidarCertificado(
      String certificatBase64,
      boolean obtenirDadesCertificat,
      String modeValidacio) throws Exception {
    String xmlPeticio = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
      "<mensajeEntrada xmlns=\"http://afirmaws/ws/validacion\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:SchemaLocation=\"https://localhost/afirmaws/xsd/mvalidacion/ws.xsd\">" +
      "<peticion>ValidarCertificado</peticion>" +
      "<versionMsg>1.0</versionMsg>" +
      "<parametros>" +
      "<certificado><![CDATA[" + certificatBase64 + "]]></certificado>" +
      "<idAplicacion>" + aplicacioId + "</idAplicacion>" +
      ((modeValidacio != null) ? "<modoValidacion>" + modeValidacio + "</modoValidacion>" : "") +
      "<obtenerInfo>" + obtenirDadesCertificat + "</obtenerInfo>" +
      "</parametros>" +
      "</mensajeEntrada>";
    log.debug(xmlPeticio);
    Service service = new Service();
    Call call = (Call)service.createCall();
    call.setTargetEndpointAddress(baseUrl + "/ValidarCertificado");
    call.setOperationName(new QName("http://soapinterop.org/", "ValidarCertificado"));
    call.setClientHandlers(this.clientHandler, null);
    call.setReturnType(org.apache.axis.Constants.XSD_STRING);
    call.addParameter("ValidarCertificadoRequest", org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
    String xmlResposta = (String)call.invoke(new Object[]{xmlPeticio});
    log.debug(xmlResposta);
    return xmlResposta;
  }
      

}
