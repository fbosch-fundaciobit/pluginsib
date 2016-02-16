package org.fundaciobit.plugins.utils;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * 
 * @author anadal
 *
 */
public class PublicCertificatePrivateKeyPair {

  protected X509Certificate publicCertificate;

  protected PrivateKey privateKey;

  /**
     * 
     */
  public PublicCertificatePrivateKeyPair() {
    super();
  }

  /**
   * @param publicCertificate
   * @param privateKey
   */
  public PublicCertificatePrivateKeyPair(X509Certificate publicCertificate,
      PrivateKey privateKey) {
    super();
    this.publicCertificate = publicCertificate;
    this.privateKey = privateKey;
  }

  public PrivateKey getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(PrivateKey privateKey) {
    this.privateKey = privateKey;
  }

  public X509Certificate getPublicCertificate() {
    return publicCertificate;
  }

  public void setPublicCertificate(X509Certificate publicCertificate) {
    this.publicCertificate = publicCertificate;
  }

}
