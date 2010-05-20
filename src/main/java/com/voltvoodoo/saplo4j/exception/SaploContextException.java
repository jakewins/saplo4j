package com.voltvoodoo.saplo4j.exception;

/**
 * Exceptions related to the current context.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 *
 */
public class SaploContextException extends SaploException {

	/**
	 * Serial No
	 */
	private static final long serialVersionUID = -1700167664444095341L;

	public SaploContextException() {
		super(-1, "", null);
	}
	
	public SaploContextException(int errorCode) {
		super(errorCode, "", null);
	}
	
	public SaploContextException(int errorCode, String message) {
		super(errorCode, message, null);
	}
	
	public SaploContextException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}
	
}