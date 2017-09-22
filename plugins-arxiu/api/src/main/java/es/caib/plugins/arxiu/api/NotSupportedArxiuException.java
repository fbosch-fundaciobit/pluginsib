package es.caib.plugins.arxiu.api;

/**
 * 
 * @author Limit
 * 
 */
public class NotSupportedArxiuException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 317046898533465444L;

	/**
	 * 
	 */
	public NotSupportedArxiuException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NotSupportedArxiuException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public NotSupportedArxiuException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NotSupportedArxiuException(Throwable cause) {
		super(cause);
	}

}
