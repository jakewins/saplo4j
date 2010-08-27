package com.voltvoodoo.saplo4j.exception;

import com.voltvoodoo.saplo4j.http.SaploRequest;

/**
 * Triggered when API keys are incorrect.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public class SaploAuthException extends SaploException {

	/**
	 * Serial No
	 */
	private static final long serialVersionUID = 8938155926474771017L;

	public SaploAuthException() {
		super(-1, "", null, null);
	}

	public SaploAuthException(int errorCode) {
		super(errorCode, "", null, null);
	}

	public SaploAuthException(int errorCode, String message) {
		super(errorCode, message, null, null);
	}

	public SaploAuthException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, null, cause);
	}

	public SaploAuthException(int errorCode, String message,
			SaploRequest request, Throwable cause) {
		super(errorCode, message, request, cause);
	}

}