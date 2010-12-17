package com.voltvoodoo.saplo4j.exception;

import com.voltvoodoo.saplo4j.http.JsonRequest;

/**
 * Abstract parent class for all Saplo Exceptions.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public abstract class SaploException extends RuntimeException {

	/**
	 * Serial No
	 */
	private static final long serialVersionUID = -3924337453676846404L;

	protected int errorCode;
	protected JsonRequest request;

	public SaploException(int errorCode, String message, JsonRequest request,
			Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;

		this.request = request;
	}

	//
	// PUBLIC
	//

	public int getErrorCode() {
		return this.errorCode;
	}

	/**
	 * Most of the exceptions caused in direct relation to Saplo API calls will
	 * contain the original request object that caused the problem, for
	 * debugging purposes.
	 * 
	 * If a request is not available, this will be null.
	 */
	public JsonRequest getRequest() {
		return request;
	}

}