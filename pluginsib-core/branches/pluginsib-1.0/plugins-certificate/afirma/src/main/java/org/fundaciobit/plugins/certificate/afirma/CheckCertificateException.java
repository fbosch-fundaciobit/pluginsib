package org.fundaciobit.plugins.certificate.afirma;
/**
 * 
 * @author anadal
 *
 */
public class CheckCertificateException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 4149066745359282316L;

  protected String errorCodi;
  protected String errorDescripcio;
  protected String errorExcepcio;

  /**
   * @param errorCodi
   * @param errorDescripcio
   * @param errorExcepcio
   */
  public CheckCertificateException(String errorCodi, String errorDescripcio,
      String errorExcepcio) {
    super(errorCodi + ": " + errorDescripcio + "(" +  errorExcepcio + ")");
    this.errorCodi = errorCodi;
    this.errorDescripcio = errorDescripcio;
    this.errorExcepcio = errorExcepcio;
  }
  
  public String getErrorCodi() {
    return errorCodi;
  }
  public void setErrorCodi(String errorCodi) {
    this.errorCodi = errorCodi;
  }
  public String getErrorDescripcio() {
    return errorDescripcio;
  }
  public void setErrorDescripcio(String errorDescripcio) {
    this.errorDescripcio = errorDescripcio;
  }
  public String getErrorExcepcio() {
    return errorExcepcio;
  }
  public void setErrorExcepcio(String errorExcepcio) {
    this.errorExcepcio = errorExcepcio;
  }
  
  
}
