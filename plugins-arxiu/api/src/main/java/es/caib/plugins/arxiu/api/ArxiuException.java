package es.caib.plugins.arxiu.api;
/**
 * 
 * @author Limit
 *
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
