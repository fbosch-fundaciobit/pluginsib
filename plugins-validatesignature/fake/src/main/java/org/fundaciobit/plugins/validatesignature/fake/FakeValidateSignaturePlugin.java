package org.fundaciobit.plugins.validatesignature.fake;

import java.util.Properties;

import org.fundaciobit.plugins.validatesignature.api.AbstractValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.IValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.SignatureInfo;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureRequest;
import org.fundaciobit.plugins.validatesignature.api.ValidationStatus;

/**
 *
 * @author anadal
 *
 */
public class FakeValidateSignaturePlugin extends AbstractValidateSignaturePlugin implements
    IValidateSignaturePlugin {

  /**
   * 
   */
  public FakeValidateSignaturePlugin() {
    super();
  }

  /**
   * @param propertyKeyBase
   * @param properties
   */
  public FakeValidateSignaturePlugin(String propertyKeyBase, Properties properties) {
    super(propertyKeyBase, properties);
  }

  /**
   * @param propertyKeyBase
   */
  public FakeValidateSignaturePlugin(String propertyKeyBase) {
    super(propertyKeyBase);
  }

  @Override
  public SignatureInfo validateSignature(ValidateSignatureRequest validationRequest)
      throws Exception {

    SignatureInfo si = new SignatureInfo();

    si.setFormat(UNKNOWN);

    ValidationStatus validationStatus = new ValidationStatus();

    validationStatus.setStatus(ValidationStatus.SIGNATURE_VALID);

    si.setValidationStatus(validationStatus);

    return si;
  }
}
