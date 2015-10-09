package org.fundaciobit.plugins.timestamp.afirmarfc;

import java.io.Serializable;
/**
 * 
 * @author anadal
 *
 */
public class PeticionException extends Exception implements Serializable {
  private static final long serialVersionUID = 1L;

  public PeticionException() {
  }

  public PeticionException(Throwable cause) {
    super(cause);
  }

  public PeticionException(String mensaje) {
    super(mensaje);
  }
}
