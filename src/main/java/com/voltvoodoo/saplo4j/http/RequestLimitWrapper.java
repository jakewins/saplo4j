package com.voltvoodoo.saplo4j.http;

import static com.voltvoodoo.saplo4j.utils.SaploResponseParser.parseSaploResponse;

import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.exception.SaploConnectionException;
import com.voltvoodoo.saplo4j.exception.SaploException;

/**
 * Wraps a SaploCallback, adding a check that hides request limit problems from
 * the wrapped callback.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public class RequestLimitWrapper implements SaploCallback<Object> {

	private static final int MAX_HOURLY_CALLS_ERROR = 1008;

	private SaploCallback<Object> wrappedCallback;
	private SaploConnection saploConnection;
	private SaploRequest request;

	public RequestLimitWrapper(SaploRequest request,
			SaploConnection saploConnection) {
		this.wrappedCallback = request.getCallback();
		this.saploConnection = saploConnection;
		this.request = request;
	}

	public void onSuccess(Object result) {
		try {
			// We do this to trigger any errors in the response
			parseSaploResponse(result);
		} catch (SaploException e) {
			if (e.getErrorCode() == MAX_HOURLY_CALLS_ERROR) {

				// This will queue up all coming requests, wait for an hour, and
				// then start releasing them again.
				saploConnection.handleRequestLimitReached();

				// Ah, here we are, an hour later. Redo the request.
				try {
					saploConnection.call(request);
				} catch (SaploConnectionException e1) {
					throw new RuntimeException(
							"FATAL: Saplo connection failed when recovering from request limit error, see nested.",
							e1);
				}

				return;
			}
		}

		wrappedCallback.onSuccess(result);
	}

	public void onFailure(SaploException exception) {
		wrappedCallback.onFailure(exception);
	}

}
