package com.voltvoodoo.saplo4j.exception;

import com.voltvoodoo.saplo4j.http.JsonRequest;

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
		super(-1, "", null, null);
	}

	public SaploMatchException(int errorCode) {
		super(errorCode, "", null, null);
	}

	public SaploMatchException(int errorCode, String message) {
		super(errorCode, message, null, null);
	}

	public SaploMatchException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, null, cause);
	}

	public SaploMatchException(int errorCode, String message,
			JsonRequest request, Throwable cause) {
		super(errorCode, message, request, cause);
	}

}