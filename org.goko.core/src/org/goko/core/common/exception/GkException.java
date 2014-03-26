package org.goko.core.common.exception;

/**
 * An abstract exception 
 * @author PsyKo
 *
 */
public abstract class GkException extends Exception{
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 5871683610064009629L;

	/**
	 * Constructor
	 */
	public GkException(String message){
		super(message);
	}
	
	/**
	 * Constructor
	 */
	public GkException(Throwable t){
		super(t);
	}
}
