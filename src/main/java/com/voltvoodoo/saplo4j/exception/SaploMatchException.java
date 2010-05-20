package com.voltvoodoo.saplo4j.exception;

/**
 * Exceptions related to Saplo Matching.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 *
 */
public class SaploMatchException extends SaploException {

	/**
	 * Serial No
	 */
	private static final long serialVersionUID = -4453049061568772297L;

	public SaploMatchException() {
		super(-1, "", null);
	}
	
	public SaploMatchException(int errorCode) {
		super(errorCode, "", null);
	}
	
	public SaploMatchException(int errorCode, String message) {
		super(errorCode, message, null);
	}
	
	public SaploMatchException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}
	
}