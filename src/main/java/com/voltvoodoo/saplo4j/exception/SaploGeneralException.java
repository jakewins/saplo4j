package com.voltvoodoo.saplo4j.exception;

/**
 * General errors, a bit of this and a bit of that :)
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 *
 */
public class SaploGeneralException extends SaploException {

	/**
	 * Serial No
	 */
	private static final long serialVersionUID = -8939893668147304902L;

	public SaploGeneralException() {
		super(-1, "", null);
	}
	
	public SaploGeneralException(int errorCode) {
		super(errorCode, "", null);
	}
	
	public SaploGeneralException(String message) {
		super(-1, message, null);
	}
	
	public SaploGeneralException(String message, Throwable cause) {
		super(-1, message, cause);
	}
	
	public SaploGeneralException(int errorCode, String message) {
		super(errorCode, message, null);
	}
	
	public SaploGeneralException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}
	
}