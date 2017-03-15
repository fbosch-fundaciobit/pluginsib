package org.fundaciobit.plugins.validatesignature.api;
/**
 * 
 * @author anadal
 *
 */
public class ValidationStatus {


  public static final int SIGNATURE_VALID = 1;

  public static final int SIGNATURE_ERROR = -1;
  
  public static final int SIGNATURE_INVALID = -2;
  

  protected int status = SIGNATURE_ERROR;

  protected String errorMsg = "Estat no definit";

  protected Throwable errorException;
  
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public Throwable getErrorException() {
    return errorException;
  }

  public void setErrorException(Throwable errorException) {
    this.errorException = errorException;
  }

  
}
