package com.voltvoodoo.saplo4j.exception;

import com.voltvoodoo.saplo4j.http.JsonRequest;

/**
 * Triggered when a connection is interrupted or times out
 * while processing is still underway.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 *
 */
public class SaploStillProcessingException extends SaploException {

	/**
	 * Serial no
	 */
	private static final long serialVersionUID = 3582968853698750951L;

	public SaploStillProcessingException(String message,
			JsonRequest request) {
		this(-1, message, request, null);
	}
	
	public SaploStillProcessingException(int errorCode, String message,
			JsonRequest request) {
		this(errorCode, message, request, null);
	}
	
	public SaploStillProcessingException(int errorCode, String message,
			JsonRequest request, Throwable cause) {
		super(errorCode, message, request, cause);
	}
	
}
