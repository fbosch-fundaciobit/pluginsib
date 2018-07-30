package org.fundaciobit.pluginsib.validatesignature.testerwebapp.controller;

import java.io.InputStream;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.validatesignature.api.SignatureRequestedInformation;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureRequest;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureResponse;
import org.fundaciobit.plugins.validatesignature.api.ValidationStatus;
import org.fundaciobit.plugins.validatesignature.tester.ejb.ValidateSignatureLocal;
import org.fundaciobit.plugins.validatesignature.tester.utils.Plugin;
import org.fundaciobit.pluginsib.validatesignature.testerwebapp.form.ValidateSignatureForm;
import org.fundaciobit.pluginsib.validatesignature.testerwebapp.form.ValidateSignatureValidator;
import org.fundaciobit.pluginsib.validatesignature.testerwebapp.utils.HtmlUtils;
import org.fundaciobit.pluginsib.validatesignature.testerwebapp.utils.InfoGlobal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author anadal
 *
 */
@Controller
@RequestMapping(value = ValidateSignatureController.CONTEXTWEB)
@SessionAttributes(types = { org.fundaciobit.pluginsib.validatesignature.testerwebapp.form.ValidateSignatureForm.class })
public class ValidateSignatureController {

  /** Logger for this class and subclasses */
  protected final Logger log = Logger.getLogger(getClass());

  public static final String CONTEXTWEB = "/common/validatesignature";
  
  

  public static final String SESSION_INFO = "VALIDATESIGNATURESESSION";
  
  
  @EJB(mappedName = ValidateSignatureLocal.JNDI_NAME)
  protected ValidateSignatureLocal validateSignatureEjb;

  //XYZ ZZZ public static final String PDF_MIME_TYPE = "application/pdf";

  @Autowired
  protected ValidateSignatureValidator validateSignatureValidator;

  /**
   * 
   */
  public ValidateSignatureController() {
    super();
  }
  
  
  @ModelAttribute("plugins")
  public List<Plugin> getPlugins() throws Exception {
    return validateSignatureEjb.getPlugins();
  }


  @RequestMapping(value = "/form", method = RequestMethod.GET)
  public ModelAndView validateSignatureGet(HttpServletRequest request) throws Exception {

    request.removeAttribute(SESSION_INFO);

    ValidateSignatureForm form = new ValidateSignatureForm();
   
    ModelAndView mav = new ModelAndView("validateSignatureForm");
    mav.addObject(form);

    return mav;

  }
  

  @RequestMapping(value = "/form", method = RequestMethod.POST)
  public ModelAndView validateSignaturePost(HttpServletRequest request, HttpServletResponse response,
      @ModelAttribute ValidateSignatureForm form, BindingResult result) throws Exception {

    validateSignatureValidator.validate(form, result);

    if (result.hasErrors()) {

      log.warn("Validador té errors !!!!");

      ModelAndView mav = new ModelAndView("validateSignatureForm");
      mav.addObject(form);
      return mav;
    }

    CommonsMultipartFile document = form.getDocument();
    CommonsMultipartFile signature = form.getSignature();

    final String langUI = form.getLangUI();

    try {

        // Document
      
        byte[] documentData = processFile(document);
        
        byte[] signatureData = processFile(signature);
       
        // Es servidor
        ValidateSignatureRequest validationRequest = new ValidateSignatureRequest();

        validationRequest.setLanguage(langUI);
        validationRequest.setSignatureData(signatureData);
        validationRequest.setSignedDocumentData(documentData);

        SignatureRequestedInformation sri = new SignatureRequestedInformation();
        sri.setReturnSignatureTypeFormatProfile(form.isReturnSignatureTypeFormatProfile());
        sri.setReturnCertificateInfo(form.isReturnCertificateInfo()); 
        sri.setReturnValidationChecks(form.isReturnValidationChecks()); 
        sri.setValidateCertificateRevocation(form.isValidateCertificateRevocation()); 
        sri.setReturnCertificates(form.isReturnCertificates()); 
        sri.setReturnTimeStampInfo(form.isReturnTimeStampInfo()); 

        validationRequest.setSignatureRequestedInformation(sri);
        
        // XYZ ZZZ
        //IValidateSignaturePlugin plugin= instantiatePlugin();
        
//        if (!plugin.filter(validationRequest)) {
//          throw new Exception("XYZ ZZZ El plugin no suporta el format de firma"
//              + " o alguan de la informació requerida.");
//        }
        

        ValidateSignatureResponse valResponse = validateSignatureEjb.validate(validationRequest, form.getPluginID());
        
        
        // XYZ ZZZ
        System.out.println("LLEGENDA: SIGNATURE_VALID = 1 | SIGNATURE_ERROR = -1 | SIGNATURE_INVALID = -2");
        
        System.out.println(" ESTAT VALIDACIO: " + valResponse.getValidationStatus().getStatus());
        
        
        System.out.println(" SIGN TYPE: " + valResponse.getSignType());
        System.out.println(" SIGN FORMAT: " + valResponse.getSignFormat());
        System.out.println(" SIGN PROFILE: " + valResponse.getSignProfile());




        return finalValidacioDeFirmaServer(request, response, validationRequest, valResponse);

      
    
      
    } catch (Throwable e) {

      String msg = "Error desconegut processant entrada de dades o inicialitzant la validació de la firma: "
          + e.getMessage();

      log.error(msg, e);

      org.fundaciobit.pluginsib.validatesignature.testerwebapp.utils.HtmlUtils.saveMessageError(request, msg);

      ModelAndView mav = new ModelAndView("validateSignatureForm");
      mav.addObject(form);

      return mav;
    }

  }


  
  

  protected byte[] processFile(CommonsMultipartFile cmf) throws Exception {

    if (cmf == null || cmf.isEmpty()) {
      return null;
    }

    InputStream is = cmf.getInputStream();    
    byte[] dataFitxer = IOUtils.toByteArray(is);
    is.close();
    
    return dataFitxer;
  }





  public ModelAndView finalValidacioDeFirmaServer(HttpServletRequest request,
      HttpServletResponse response, ValidateSignatureRequest validateRequest,
      ValidateSignatureResponse validateResponse) throws Exception {

    ValidationStatus validationStatus = validateResponse.getValidationStatus();

    int status = validationStatus.getStatus();

    switch (status) {

    case ValidationStatus.SIGNATURE_ERROR: 
    {
      String msg = "Error desconegut durant la validació de la firma: " + validationStatus.getErrorMsg();
      if (validationStatus.getErrorException() != null) {
        log.error(msg, validationStatus.getErrorException());
      }
      
      HtmlUtils.saveMessageError(request, msg);
      return new ModelAndView(new RedirectView("/", true));
    }

    case ValidationStatus.SIGNATURE_INVALID: 
    case ValidationStatus.SIGNATURE_VALID: 
    {

      

//        if (isDebug) {
//          log.debug(" ------ SERVER SIGNID ]" + fssr.getSignID() + "[");
//        }

      InfoGlobal infoGlobal = new InfoGlobal(validateRequest, validateResponse);


      request.getSession().setAttribute(SESSION_INFO, infoGlobal);

      ModelAndView mav = new ModelAndView("validateSignatureFinal");
      mav.addObject("infoGlobal", infoGlobal);
      return mav;

    } // Final Case Firma OK

    default:
      // XYZ ZZZ Traduir
      HtmlUtils.saveMessageError(request, "Estat desconegut (" + status + ")"
          + " despres del procés de validació de firmes");
      return new ModelAndView(new RedirectView("/", true));

    } // Final Switch Global

  }

  /* XYZ ZZZ
  @RequestMapping(value = "/download/original/{signID}", method = RequestMethod.GET)
  public void downloadOriginal(@PathVariable("signID") String signID,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    Map<String, InfoGlobal> peticions = (Map<String, InfoGlobal>) request.getSession()
        .getAttribute("peticions");

    InfoGlobal infoGlobal = peticions.get(signID);

    FirmaSimpleFile fsf = infoGlobal.getPeticio().getFileToSign();

    if (fsf == null) {
      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
      return;
    }

    String filename = fsf.getNom();

    {
      response.setContentType(fsf.getMime());

      response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
      response.setContentLength((int) fsf.getData().length);

      java.io.OutputStream output = response.getOutputStream();

      IOUtils.copy(new ByteArrayInputStream(fsf.getData()), output);

      output.close();
    }
  }

  /**
   * Descàrrega del document firmat
   * 
   * @param id
   * @param response
   * @throws Exception
   
  @RequestMapping(value = "/download/signed/{signID}", method = RequestMethod.GET)
  public void downloadSigned(@PathVariable("signID") String signID,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    Map<String, InfoGlobal> peticions = (Map<String, InfoGlobal>) request.getSession()
        .getAttribute("peticions");

    log.info("downloadSigned:: peticions = " + peticions);

    log.info("downloadSigned:: signID = |" + signID + "|");

    InfoGlobal infoGlobal = peticions.get(signID);

    log.info("downloadSigned:: infoGlobal = |" + infoGlobal + "|");

    FirmaSimpleFile fsf = infoGlobal.getResultat().getSignedFile();

    if (fsf == null) {
      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
      return;
    }

    String filename = fsf.getNom();

    {
      response.setContentType(fsf.getMime());

      response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
      response.setContentLength((int) fsf.getData().length);

      java.io.OutputStream output = response.getOutputStream();

      IOUtils.copy(new ByteArrayInputStream(fsf.getData()), output);

      output.close();
    }



  }
*/
  @InitBinder("validateSignatureForm")
  public void initBinder(WebDataBinder binder) {

    binder.setValidator(this.validateSignatureValidator);

    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

  }
/* XYZ ZZZ
  protected static String getAbsoluteURLBase(HttpServletRequest request) {

    return request.getScheme() + "://" + request.getServerName() + ":"
        + +request.getServerPort() + request.getContextPath();

  }

  public static String getRelativeURLBase(HttpServletRequest request) {
    return request.getContextPath();
  }

  protected static String getAbsoluteControllerBase(HttpServletRequest request,
      String webContext) {
    return getAbsoluteURLBase(request) + webContext;
  }

  public static String getRelativeControllerBase(HttpServletRequest request, String webContext) {
    return getRelativeURLBase(request) + webContext;
  }
*/
}
