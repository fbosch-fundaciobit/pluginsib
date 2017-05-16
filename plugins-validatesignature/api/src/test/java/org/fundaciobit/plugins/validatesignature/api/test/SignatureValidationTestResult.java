package org.fundaciobit.plugins.validatesignature.api.test;

import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureResponse;

/**
 * 
 * @author anadal
 *
 */
public class SignatureValidationTestResult {

  final String signatureName;

  final String documentName;

  final byte[] signature;

  final byte[] document;

  final String expectedSignType;

  final String expectedSignProfile;

  final String expectedSignFormat;

  final boolean expectedError;

  ValidateSignatureResponse validateSignatureResponse;

  Throwable exception;

  /**
   * @param signatureName
   * @param documentName
   * @param signature
   * @param document
   * @param expectedSignType
   * @param expectedSignFormat
   * @param expectedSignProfile
   */
  public SignatureValidationTestResult(String signatureName, String documentName,
      byte[] signature, byte[] document, String expectedSignType, 
      String expectedSignProfile, String expectedSignFormat, boolean expectedError) {
    super();
    this.signatureName = signatureName;
    this.documentName = documentName;
    this.signature = signature;
    this.document = document;
    this.expectedSignType = expectedSignType;
    this.expectedSignFormat = expectedSignFormat;
    this.expectedSignProfile = expectedSignProfile;
    this.expectedError = expectedError;
  }

  public ValidateSignatureResponse getValidateSignatureResponse() {
    return validateSignatureResponse;
  }

  public void setValidateSignatureResponse(ValidateSignatureResponse validateSignatureResponse) {
    this.validateSignatureResponse = validateSignatureResponse;
  }

  public Throwable getException() {
    return exception;
  }

  public void setException(Throwable exception) {
    this.exception = exception;
  }

  public String getSignatureName() {
    return signatureName;
  }

  public String getDocumentName() {
    return documentName;
  }

  public byte[] getSignature() {
    return signature;
  }

  public byte[] getDocument() {
    return document;
  }

  public String getExpectedSignType() {
    return expectedSignType;
  }

  public String getExpectedSignFormat() {
    return expectedSignFormat;
  }

  public String getExpectedSignProfile() {
    return expectedSignProfile;
  }

  public boolean isExpectedError() {
    return expectedError;
  }
  
  

}
