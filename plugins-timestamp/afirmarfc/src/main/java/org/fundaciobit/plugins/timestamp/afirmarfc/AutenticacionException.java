package org.fundaciobit.plugins.timestamp.afirmarfc;

import java.io.Serializable;
/**
 * 
 * @author anadal
 *
 */
public class AutenticacionException extends Exception implements Serializable {
  private static final long serialVersionUID = 1L;

  public AutenticacionException() {
  }

  public AutenticacionException(Throwable cause) {
    super(cause);
  }

  public AutenticacionException(String mensaje) {
    super(mensaje);
  }
}
