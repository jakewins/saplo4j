package com.voltvoodoo.saplo4j.http;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.SaploConnection;
import com.voltvoodoo.saplo4j.exception.SaploRequestLimitReachedException;


/**
 * Wraps a SaploCallback, adding a check that hides request limit problems from
 * the wrapped callback.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public class RequestLimitWrapper implements JsonCallback {

	private JsonCallback wrappedCallback;
	private SaploConnection saploConnection;
	private JsonRequest request;

	public RequestLimitWrapper(JsonRequest request,
			SaploConnection saploConnection) {
		this.wrappedCallback = request.getCallback();
		this.saploConnection = saploConnection;
		this.request = request;
	}

	public void onSuccess(JSONObject result) {
		wrappedCallback.onSuccess(result);
	}

	public void onFailure(Exception exception) {
		if (exception instanceof SaploRequestLimitReachedException
				&& ((SaploRequestLimitReachedException) exception).isReferringToHourlyLimit()) {

			// This will queue up all coming requests, wait for an hour, and
			// then start releasing them again.
			saploConnection.handleRequestLimitReached();
			
			saploConnection.call(request);

		} else {
			wrappedCallback.onFailure(exception);
		}
	}

}
