package com.voltvoodoo.saplo4j.exception;

/**
 * Exceptions related to the current context.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 *
 */
public class SaploConnectionException extends SaploException {

	/**
	 * Serial No
	 */
	private static final long serialVersionUID = -1700167664444095341L;

	public SaploConnectionException() {
		super(-1, "", null);
	}
	
	public SaploConnectionException(String message) {
		super(-1, message, null);
	}
	
	public SaploConnectionException(String message, Throwable cause) {
		super(-1, message, cause);
	}
	
}