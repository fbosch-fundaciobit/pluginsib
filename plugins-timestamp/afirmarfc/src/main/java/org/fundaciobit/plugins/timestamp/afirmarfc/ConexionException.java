package org.fundaciobit.plugins.timestamp.afirmarfc;

import java.io.Serializable;

/**
 * 
 * @author anadal
 *
 */
public class ConexionException extends Exception implements Serializable {
  private static final long serialVersionUID = 1L;

  public ConexionException() {
  }

  public ConexionException(Throwable cause) {
    super(cause);
  }

  public ConexionException(String mensaje) {
    super(mensaje);
  }

  /**
   * @param message
   * @param cause
   */
  public ConexionException(String message, Throwable cause) {
    super(message, cause);

  }

}
