package com.voltvoodoo.saplo4j.exception;

import com.voltvoodoo.saplo4j.http.JsonRequest;

/**
 * Exceptions related to Saplo Corpuses.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public class SaploCorpusException extends SaploException {

	/**
	 * Serial No
	 */
	private static final long serialVersionUID = 3586412273704645167L;

	public SaploCorpusException() {
		super(-1, "", null, null);
	}

	public SaploCorpusException(int errorCode) {
		super(errorCode, "", null, null);
	}

	public SaploCorpusException(int errorCode, String message) {
		super(errorCode, message, null, null);
	}

	public SaploCorpusException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, null, cause);
	}

	public SaploCorpusException(int errorCode, String message,
			JsonRequest request, Throwable cause) {
		super(errorCode, message, request, cause);
	}

}