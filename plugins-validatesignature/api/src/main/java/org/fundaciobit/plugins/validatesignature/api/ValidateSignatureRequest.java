package org.fundaciobit.plugins.validatesignature.api;

/**
 * 
 * @author anadal
 *
 */
public class ValidateSignatureRequest {

  protected byte[] signatureData;

  protected byte[] signedDocumentData;

  protected boolean validateCertificateRevocation = false;

  protected boolean returnCertificateInfo = false;

  protected boolean returnValidationChecks = false;
  
  protected boolean returnCertificates = false;
  
  protected boolean returnTimeStampInfo = false;

  public byte[] getSignatureData() {
    return signatureData;
  }

  public void setSignatureData(byte[] signatureData) {
    this.signatureData = signatureData;
  }

  public byte[] getSignedDocumentData() {
    return signedDocumentData;
  }

  public void setSignedDocumentData(byte[] signedDocumentData) {
    this.signedDocumentData = signedDocumentData;
  }

  public boolean isReturnCertificateInfo() {
    return returnCertificateInfo;
  }

  public void setReturnCertificateInfo(boolean returnCertificateInfo) {
    this.returnCertificateInfo = returnCertificateInfo;
  }

  public boolean isReturnValidationChecks() {
    return returnValidationChecks;
  }

  public void setReturnValidationChecks(boolean returnValidationChecks) {
    this.returnValidationChecks = returnValidationChecks;
  }

  public boolean isValidateCertificateRevocation() {
    return validateCertificateRevocation;
  }

  public void setValidateCertificateRevocation(boolean validateCertificateRevocation) {
    this.validateCertificateRevocation = validateCertificateRevocation;
  }

  public boolean isReturnCertificates() {
    return returnCertificates;
  }

  public void setReturnCertificates(boolean returnCertificates) {
    this.returnCertificates = returnCertificates;
  }

  public boolean isReturnTimeStampInfo() {
    return returnTimeStampInfo;
  }

  public void setReturnTimeStampInfo(boolean returnTimeStampInfo) {
    this.returnTimeStampInfo = returnTimeStampInfo;
  }

  
}
