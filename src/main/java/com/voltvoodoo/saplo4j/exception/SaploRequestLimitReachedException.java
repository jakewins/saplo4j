package com.voltvoodoo.saplo4j.exception;

import com.voltvoodoo.saplo4j.http.JsonRequest;

/**
 * Triggered when the max API call limit is reached. This may be maximum for the
 * hour, or for some undefined larger time period. Use
 * {@link #isReferringToHourlyLimit()} to determine which one we are dealing
 * with.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public class SaploRequestLimitReachedException extends SaploException {

	/**
	 * Serial no
	 */
	private static final long serialVersionUID = 6000262401921505267L;

	public SaploRequestLimitReachedException(int errorCode, String message) {
		super(errorCode, message, null, null);
	}
	
	public SaploRequestLimitReachedException(String message,
			JsonRequest request) {
		super(-1, message, request, null);
	}

	public SaploRequestLimitReachedException(int errorCode, String message,
			JsonRequest request, Throwable cause) {
		super(errorCode, message, request, cause);
	}

	public boolean isReferringToHourlyLimit() {
		return getErrorCode() == 1008;
	}

}
