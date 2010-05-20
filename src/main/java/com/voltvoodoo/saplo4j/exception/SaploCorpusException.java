package com.voltvoodoo.saplo4j.exception;

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
		super(-1, "", null);
	}
	
	public SaploCorpusException(int errorCode) {
		super(errorCode, "", null);
	}
	
	public SaploCorpusException(int errorCode, String message) {
		super(errorCode, message, null);
	}
	
	public SaploCorpusException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}
	
}