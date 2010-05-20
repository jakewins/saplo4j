package com.voltvoodoo.saplo4j.exception;

/**
 * Abstract parent class for all Saplo Exceptions.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 *
 */
public abstract class SaploException extends Exception {

	/**
	 * Serial No
	 */
	private static final long serialVersionUID = -3924337453676846404L;

	protected int errorCode;
	
	public SaploException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}
	
	//
	// PUBLIC
	//
	
	public int getErrorCode() { return this.errorCode; }
	
}