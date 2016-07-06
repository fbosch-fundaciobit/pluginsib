package es.caib.digital.ws.api.utils;

import java.net.URL;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;

import es.caib.digital.ws.api.copiaautentica.CopiaAutenticaWSService;
import es.caib.digital.ws.api.copiaautentica.CopiaAutenticaWSServiceService;
import es.caib.digital.ws.api.entidades.EntidadesWSService;
import es.caib.digital.ws.api.entidades.EntidadesWSServiceImplService;


/**
 * 
 * @author anadal
 *
 */
public class DigitalUtils {
  
  protected static Logger log = Logger.getLogger(DigitalUtils.class);

  
  public static final String COPIA_AUTENTICA = "ServicioCopiaAutentica"; 
  
  public static final String ENTIDADES = "ServicioEntidades";

  
  public static void configAddressUserPassword(String usr, String pwd, String endpoint,
      Object api) {

    Map<String, Object> reqContext = ((BindingProvider) api).getRequestContext();
    reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
    reqContext.put(BindingProvider.USERNAME_PROPERTY, usr);
    reqContext.put(BindingProvider.PASSWORD_PROPERTY, pwd);
    
    reqContext.put("disableCNCheck", "true");
  }

  public static CopiaAutenticaWSService getCopiaAutenticaWSServiceApi(String hostport, String usr, String pwd)
      throws Exception {

    final String endPoint = hostport + "/digitalfront/ws/" + DigitalUtils.COPIA_AUTENTICA;
    
    log.info(" XYZ endPoint = " + endPoint);
    
    String fileName = "ServicioCopiaAutentica.wsdl";
    ClassLoader classLoader = DigitalUtils.class.getClassLoader();
    URL wsdlLocation = classLoader.getResource(fileName);
    

    CopiaAutenticaWSServiceService srv = new CopiaAutenticaWSServiceService(wsdlLocation);

    CopiaAutenticaWSService api = srv.getCopiaAutenticaWSServicePort();

    configAddressUserPassword(usr, pwd, endPoint, api);

    return api;

  }
  
  
  public static EntidadesWSService getEntidadesWSServiceApi(String hostPort, String usr, String pwd)
      throws Exception {
    

    final String endPoint = hostPort + "/digitalfront/ws/" + DigitalUtils.ENTIDADES;
    

    String fileName = "ServicioEntidades.wsdl";
    ClassLoader classLoader = DigitalUtils.class.getClassLoader();
    URL wsdlLocation = classLoader.getResource(fileName);

    EntidadesWSServiceImplService srv = new EntidadesWSServiceImplService(wsdlLocation);

    EntidadesWSService api = srv.getEntidadesWSServiceImplPort();

    configAddressUserPassword(usr, pwd, endPoint, api);

    return api;

  }
  
}
