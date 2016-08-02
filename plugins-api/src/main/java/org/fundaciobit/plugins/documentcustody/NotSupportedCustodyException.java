package org.fundaciobit.plugins.documentcustody;

/**
 * 
 * @author anadal
 * 
 */
@Deprecated
public class NotSupportedCustodyException extends Exception {

  /**
   * 
   */
  public NotSupportedCustodyException() {
    super();
  }

  /**
   * @param message
   * @param cause
   */
  public NotSupportedCustodyException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   */
  public NotSupportedCustodyException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public NotSupportedCustodyException(Throwable cause) {
    super(cause);
  }

}
