/**
 * 
 */
package es.caib.plugins.arxiu.api;

/**
 * Excepció que es produeix al processar les peticions a l'arxiu.
 *
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -311353047419727743L;

	/**
	 * 
	 */
	public ArxiuException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ArxiuException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ArxiuException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ArxiuException(Throwable cause) {
		super(cause);
	}

}
