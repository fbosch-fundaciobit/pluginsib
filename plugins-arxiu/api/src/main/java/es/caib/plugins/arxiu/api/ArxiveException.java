package es.caib.plugins.arxiu.api;
/**
 * 
 * @author Limit
 *
 */
public class ArxiveException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -311353047419727743L;

	/**
	 * 
	 */
	public ArxiveException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ArxiveException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ArxiveException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ArxiveException(Throwable cause) {
		super(cause);
	}

}
