package com.voltvoodoo.saplo4j.exception;

import com.voltvoodoo.saplo4j.http.JsonRequest;

/**
 * Triggered when there are tagging-related problems.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public class SaploTagException extends SaploException {

	/**
	 * Serial No
	 */
	private static final long serialVersionUID = 4208015295079179453L;

	public SaploTagException() {
		super(-1, "", null, null);
	}

	public SaploTagException(int errorCode) {
		super(errorCode, "", null, null);
	}

	public SaploTagException(int errorCode, String message) {
		super(errorCode, message, null, null);
	}

	public SaploTagException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, null, cause);
	}

	public SaploTagException(int errorCode, String message,
			JsonRequest request, Throwable cause) {
		super(errorCode, message, request, cause);
	}

}