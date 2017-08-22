package es.caib.plugins.arxiu.api;

/**
 * 
 * @author Limit
 * 
 */
public class NotSupportedArxiveException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 317046898533465444L;

	/**
	 * 
	 */
	public NotSupportedArxiveException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NotSupportedArxiveException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public NotSupportedArxiveException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NotSupportedArxiveException(Throwable cause) {
		super(cause);
	}

}
