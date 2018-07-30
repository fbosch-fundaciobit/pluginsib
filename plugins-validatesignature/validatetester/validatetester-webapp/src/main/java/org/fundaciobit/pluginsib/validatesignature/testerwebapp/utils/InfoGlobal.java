package org.fundaciobit.pluginsib.validatesignature.testerwebapp.utils;

import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureRequest;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureResponse;

/**
 * 
 * @author anadal
 *
 */
public class InfoGlobal {

  protected final ValidateSignatureRequest validateRequest;
  protected final ValidateSignatureResponse validateResponse;

  /**
   * @param validateRequest
   * @param validateResponse
   */
  public InfoGlobal(ValidateSignatureRequest validateRequest,
      ValidateSignatureResponse validateResponse) {
    super();
    this.validateRequest = validateRequest;
    this.validateResponse = validateResponse;
  }

  public ValidateSignatureRequest getValidateRequest() {
    return validateRequest;
  }

  public ValidateSignatureResponse getValidateResponse() {
    return validateResponse;
  }

}
