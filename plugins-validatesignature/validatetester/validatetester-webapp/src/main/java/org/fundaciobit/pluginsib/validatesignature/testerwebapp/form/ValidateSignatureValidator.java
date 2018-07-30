package org.fundaciobit.pluginsib.validatesignature.testerwebapp.form;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 
 * @author anadal
 *
 */
@Component
public class ValidateSignatureValidator implements Validator {

  protected final Logger log = Logger.getLogger(getClass());


  public ValidateSignatureValidator() {
    super();
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return ValidateSignatureForm.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {

    CommonsMultipartFile document = ((ValidateSignatureForm) target).getDocument();
    
    CommonsMultipartFile signature = ((ValidateSignatureForm) target).getSignature();

    if ( (document == null || document.isEmpty()) && (signature == null || signature.isEmpty())) {
      errors.rejectValue("document", "genapp.validation.required",
          new String[] { "document" }, null);
      errors.rejectValue("signature", "genapp.validation.required",
          new String[] { "signature" }, null);
    }

  }

}
