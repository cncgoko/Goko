package org.goko.core.common.exception;

/**
 * Technical exception
 * @author PsyKo
 *
 */
public class GkTechnicalException extends GkException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1125645831362340170L;

	/**
	 * Constructor
	 * @param t Throwable
	 */
	public GkTechnicalException(Throwable t) {
		super(t);		
	}
	
	/**
	 * Constructor
	 * @param message the message
	 */
	public GkTechnicalException(String message) {
		super(message);		
	}

}
