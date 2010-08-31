package com.voltvoodoo.saplo4j.exception;

import com.voltvoodoo.saplo4j.http.SaploRequest;

/**
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
		super(-1, "", null, null);
	}

	public SaploConnectionException(String message) {
		super(-1, message, null, null);
	}

	public SaploConnectionException(String message, Throwable cause) {
		super(-1, message, null, cause);
	}

	public SaploConnectionException(int errorCode, String message,
			SaploRequest request, Throwable cause) {
		super(errorCode, message, request, cause);
	}

}