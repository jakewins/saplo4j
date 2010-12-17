package com.voltvoodoo.saplo4j.exception;

import com.voltvoodoo.saplo4j.http.JsonRequest;

/**
 * Triggered when there is an exception in the underlying Saplo engine.
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 *
 */
public class SaploInternalException extends SaploException{

	/**
	 * Serial no
	 */
	private static final long serialVersionUID = 1100648217003884589L;

	public SaploInternalException(int errorCode, String message,
			JsonRequest request) {
		super(errorCode, message, request, null);
	}
	
	public SaploInternalException(int errorCode, String message,
			JsonRequest request, Throwable cause) {
		super(errorCode, message, request, cause);
	}

}
