package org.fundaciobit.plugins.certificate.afirmacxf;

import java.io.StringReader;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;

import org.fundaciobit.plugins.certificate.InformacioCertificat;
import org.fundaciobit.plugins.certificate.ResultatValidacio;

import org.fundaciobit.plugins.certificate.afirmacxf.validarcertificadoapi.InfoCertificadoInfo;
import org.fundaciobit.plugins.certificate.afirmacxf.validarcertificadoapi.InfoCertificadoInfo.Campo;
import org.fundaciobit.plugins.certificate.afirmacxf.validarcertificadoapi.MensajeSalida;
import org.fundaciobit.plugins.certificate.afirmacxf.validarcertificadoapi.MensajeSalida.Respuesta.Excepcion;
import org.fundaciobit.plugins.certificate.afirmacxf.validarcertificadoapi.ResultadoValidacionInfo;
import org.fundaciobit.plugins.certificate.afirmacxf.validarcertificadoapi.Validacion;
import org.fundaciobit.plugins.certificate.afirmacxf.validarcertificadoapi.ValidacionService;

import sun.misc.BASE64Encoder;

/**
 * 
 * @author anadal
 * 
 */
public class ValidaCertificat {

  public static final int MODE_VALIDACIO_SIMPLE = 0;
  public static final int MODE_VALIDACIO_AMB_REVOCACIO = 1;
  public static final int MODE_VALIDACIO_CADENA = 2;

  private static final SimpleDateFormat SDF = new SimpleDateFormat(
      "yyyy-MM-dd EEE HH:mm:ss Z", new Locale("es"));

  protected final Logger log = Logger.getLogger(getClass());

  private final String endPoint;
  private final String aplicacioId;

  private final int modeValidacio;

  private final ClientHandler clientHandler;

  private ValidaCertificat(String endPoint, String aplicacioId, int modeValidacio,
      ClientHandler clientHandler) {
    this.endPoint = endPoint;
    this.aplicacioId = aplicacioId;
    this.modeValidacio = modeValidacio;
    this.clientHandler = clientHandler;
  }

  public ValidaCertificat(String endPoint, String application_id, int modeValidacio,
      String username, String password) {
    this(endPoint, application_id, modeValidacio, new ClientHandlerUsernamePassword(username,
        password));
  }

  public ValidaCertificat(String endPoint, String application_id, int modeValidacio,
      String keystoreLocation, String keystoreType, String keystorePassword,
      String keystoreCertAlias, String keystoreCertPassword) {

    this(endPoint, application_id, modeValidacio, new ClientHandlerCertificate(
        keystoreLocation, keystoreType, keystorePassword, keystoreCertAlias,
        keystoreCertPassword));
  }

  public int getModeValidacio() {
    return modeValidacio;
  }

  public String getEndPoint() {
    return endPoint;
  }

  public String getAplicacioId() {
    return aplicacioId;
  }

  public ResultatValidacio validar(X509Certificate certificate, boolean obtenirDadesCertificat)
      throws Exception {
    return validar(certificate.getEncoded(), obtenirDadesCertificat);
  }

  private MensajeSalida getMensajeSalidaFromXml(String xml) throws Exception {
    JAXBContext context = JAXBContext.newInstance(MensajeSalida.class);
    Unmarshaller unMarshaller = context.createUnmarshaller();
    MensajeSalida ms = (MensajeSalida) unMarshaller.unmarshal(new StringReader(xml));
    return ms;
  }

  public ResultatValidacio validar(byte[] data, boolean obtenirDadesCertificat)
      throws Exception {

    String certificatBase64 = new BASE64Encoder().encode(data);

    String respostaXml = cridarValidarCertificado(certificatBase64, obtenirDadesCertificat,
        modeValidacio);

    log.debug(respostaXml);

    MensajeSalida ms = getMensajeSalidaFromXml(respostaXml);

    
    Excepcion ex = ms.getRespuesta().getExcepcion();
    

    log.info("Exceptcion = " + ex);


    if (ex == null) {
      
      ResultadoValidacionInfo rvi;
      
      
      rvi = ms.getRespuesta().getResultadoProcesamiento().getResultadoValidacion();

      ResultatValidacio resultatValidacio = new ResultatValidacio();

      resultatValidacio.setResultatValidacioCodi(Integer.parseInt(rvi.getResultado()));
      resultatValidacio.setResultatValidacioDescripcio(rvi.getDescripcion());

      if (obtenirDadesCertificat) {
        InfoCertificadoInfo infoCert = ms.getRespuesta().getResultadoProcesamiento()
            .getInfoCertificado();
        resultatValidacio.setInformacioCertificat(getDadesCertificat(infoCert));
      }
      return resultatValidacio;

    } else {

      

      StringBuffer str = new StringBuffer();

      String codigoError = ex.getCodigoError();
      if (codigoError != null) {
        str.append("codigoError: " + codigoError);
      }

      String descripcionError = ex.getDescripcion();
      if (descripcionError != null) {
        if (str.length() != 0) {
          str.append("\n");
        }
        str.append("descripcionError: " + descripcionError);
      }

      String excepcionAsociada = ex.getExcepcionAsociada();
      if (excepcionAsociada != null) {
        if (str.length() != 0) {
          str.append("\n");
        }
        str.append("excepcionAsociada: " + excepcionAsociada);
      }

      throw new Exception(str.toString());
    }

  }

  @SuppressWarnings("unchecked")
  private InformacioCertificat getDadesCertificat(InfoCertificadoInfo infoCert) {
    if (infoCert == null) {
      return null;
    }

    List<Campo> camps = infoCert.getCampo();

    InformacioCertificat dades = new InformacioCertificat();

    for (Campo camp : camps) {

      String key = camp.getIdCampo();
      String value = camp.getValorCampo();
      if ("tipoCertificado".equalsIgnoreCase(key))
        dades.setTipusCertificat(value);
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

  private String cridarValidarCertificado(String certificatBase64,
      boolean obtenirDadesCertificat, int modeValidacio) throws Exception {

    ValidacionService service = new ValidacionService(new java.net.URL(getEndPoint() + "?wsdl"));
    Validacion api = service.getValidarCertificado();

    Map<String, Object> reqContext = ((BindingProvider) api).getRequestContext();
    reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getEndPoint());

    String xmlPeticio = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<mensajeEntrada xmlns=\"http://afirmaws/ws/validacion\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:SchemaLocation=\"https://localhost/afirmaws/xsd/mvalidacion/ws.xsd\">"
        + "<peticion>ValidarCertificado</peticion>" + "<versionMsg>1.0</versionMsg>"
        + "<parametros>" + "<certificado><![CDATA[" + certificatBase64 + "]]></certificado>"
        + "<idAplicacion>" + aplicacioId + "</idAplicacion>" + "<modoValidacion>"
        + modeValidacio + "</modoValidacion>" + "<obtenerInfo>" + obtenirDadesCertificat
        + "</obtenerInfo>" + "</parametros>" + "</mensajeEntrada>";
    log.debug(xmlPeticio);

    this.clientHandler.addSecureHeader(api);

    String xmlResposta = api.validarCertificado(xmlPeticio);
    log.debug(xmlResposta);
    return xmlResposta;

  }

}
