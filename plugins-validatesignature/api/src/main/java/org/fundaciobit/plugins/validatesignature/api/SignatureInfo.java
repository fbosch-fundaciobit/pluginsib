package org.fundaciobit.plugins.validatesignature.api;

/**
 * 
 * @author anadal
 *
 */
public class SignatureInfo {

  private String format = IValidateSignaturePlugin.UNKNOWN;
  
  
  private ValidationStatus validationStatus = new ValidationStatus();
  
  
  private DetailInfo[] detailInfo = null;

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public ValidationStatus getValidationStatus() {
    return validationStatus;
  }

  public DetailInfo[] getDetailInfo() {
    return detailInfo;
  }

  public void setDetailInfo(DetailInfo[] detailInfo) {
    this.detailInfo = detailInfo;
  }

  public void setValidationStatus(ValidationStatus validationStatus) {
    this.validationStatus = validationStatus;
  }

  
}
