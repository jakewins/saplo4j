package com.voltvoodoo.saplo4j.exception;

import com.voltvoodoo.saplo4j.http.SaploRequest;

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
		super(-1, "", null, null);
	}

	public SaploGeneralException(int errorCode) {
		super(errorCode, "", null, null);
	}

	public SaploGeneralException(String message) {
		super(-1, message, null, null);
	}
	
	public SaploGeneralException(String message, SaploRequest request) {
		super(-1, message, request, null);
	}

	public SaploGeneralException(String message, Throwable cause) {
		super(-1, message, null, cause);
	}

	public SaploGeneralException(int errorCode, String message) {
		super(errorCode, message, null, null);
	}

	public SaploGeneralException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, null, cause);
	}

	public SaploGeneralException(int errorCode, String message,
			SaploRequest request, Throwable cause) {
		super(errorCode, message, request, cause);
	}

}