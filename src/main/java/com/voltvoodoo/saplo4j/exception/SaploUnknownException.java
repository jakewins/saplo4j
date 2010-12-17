package com.voltvoodoo.saplo4j.exception;

import com.voltvoodoo.saplo4j.http.JsonRequest;

public class SaploUnknownException extends SaploException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 433963326536879067L;

	public SaploUnknownException(String message, JsonRequest request) {
		this(-1, message, request, null);
	}
	
	public SaploUnknownException(int errorCode, String message,
			JsonRequest request, Throwable cause) {
		super(errorCode, message, request, cause);
	}

}
