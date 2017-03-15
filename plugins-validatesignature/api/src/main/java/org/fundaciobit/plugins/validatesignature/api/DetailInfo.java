package org.fundaciobit.plugins.validatesignature.api;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author anadal
 *
 */
public class DetailInfo {

  public static final String SIGN_ALGORITHM_SHA1 = "SHA-1";
  public static final String SIGN_ALGORITHM_SHA256 = "SHA-256";
  public static final String SIGN_ALGORITHM_SHA384 = "SHA-384";
  public static final String SIGN_ALGORITHM_SHA512 = "SHA-512";

  protected String algorithm;

  protected String digestValue;
  
  protected Date signDate;

  /**
   * 
   */
  protected List<Check> validChecks;

  protected List<Check> invalidChecks;

  protected List<Check> indeterminateChecks;

  /**
   * En firmes EPES retorna l'identificaor de la política de firma
   */
  protected String policyIdentifier;

  protected CertificateInfo certificateInfo;
  
  
  protected byte[][] certificateChain;
  
  
  protected TimeStampInfo timeStampInfo;
  
  

  public CertificateInfo getCertificateInfo() {
    return certificateInfo;
  }

  public void setCertificateInfo(CertificateInfo certificateInfo) {
    this.certificateInfo = certificateInfo;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public String getDigestValue() {
    return digestValue;
  }

  public void setDigestValue(String digestValue) {
    this.digestValue = digestValue;
  }

  public String getPolicyIdentifier() {
    return policyIdentifier;
  }

  public void setPolicyIdentifier(String policyIdentifier) {
    this.policyIdentifier = policyIdentifier;
  }

  public List<Check> getValidChecks() {
    return validChecks;
  }

  public void setValidChecks(List<Check> validChecks) {
    this.validChecks = validChecks;
  }

  public List<Check> getInvalidChecks() {
    return invalidChecks;
  }

  public void setInvalidChecks(List<Check> invalidChecks) {
    this.invalidChecks = invalidChecks;
  }

  public List<Check> getIndeterminateChecks() {
    return indeterminateChecks;
  }

  public void setIndeterminateChecks(List<Check> indeterminateChecks) {
    this.indeterminateChecks = indeterminateChecks;
  }



  public Date getSignDate() {
    return signDate;
  }

  public void setSignDate(Date signDate) {
    this.signDate = signDate;
  }

  public byte[][] getCertificateChain() {
    return certificateChain;
  }

  public void setCertificateChain(byte[][] certificateChain) {
    this.certificateChain = certificateChain;
  }

  public TimeStampInfo getTimeStampInfo() {
    return timeStampInfo;
  }

  public void setTimeStampInfo(TimeStampInfo timeStampInfo) {
    this.timeStampInfo = timeStampInfo;
  }


}
