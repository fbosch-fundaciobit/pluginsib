package org.fundaciobit.pluginsib.validatesignature.testerwebapp.form;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 
 * @author anadal
 * 
 */
public class ValidateSignatureForm {

  protected long pluginID;

  protected String langUI;

  protected CommonsMultipartFile signature;

  protected CommonsMultipartFile document;

  protected boolean returnSignatureTypeFormatProfile = true;

  protected boolean validateCertificateRevocation = false;

  protected boolean returnCertificateInfo = false;

  protected boolean returnValidationChecks = false;

  protected boolean returnCertificates = false;

  protected boolean returnTimeStampInfo = false;

  public ValidateSignatureForm() {
  }

  public String getLangUI() {
    return langUI;
  }

  public void setLangUI(String langUI) {
    this.langUI = langUI;
  }

  public CommonsMultipartFile getSignature() {
    return signature;
  }

  public void setSignature(CommonsMultipartFile signature) {
    this.signature = signature;
  }

  public CommonsMultipartFile getDocument() {
    return document;
  }

  public void setDocument(CommonsMultipartFile document) {
    this.document = document;
  }

  public boolean isReturnSignatureTypeFormatProfile() {
    return returnSignatureTypeFormatProfile;
  }

  public void setReturnSignatureTypeFormatProfile(boolean returnSignatureTypeFormatProfile) {
    this.returnSignatureTypeFormatProfile = returnSignatureTypeFormatProfile;
  }

  public boolean isValidateCertificateRevocation() {
    return validateCertificateRevocation;
  }

  public void setValidateCertificateRevocation(boolean validateCertificateRevocation) {
    this.validateCertificateRevocation = validateCertificateRevocation;
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

  public long getPluginID() {
    return pluginID;
  }

  public void setPluginID(long pluginID) {
    this.pluginID = pluginID;
  }

}
