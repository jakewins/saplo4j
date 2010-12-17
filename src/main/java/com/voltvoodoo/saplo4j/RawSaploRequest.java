package com.voltvoodoo.saplo4j;

import com.voltvoodoo.saplo4j.http.JsonCallback;
import com.voltvoodoo.saplo4j.http.JsonRequest;


/**
 * Use this to send raw-text requests to Saplo. Useful for testing and debugging
 * purposes.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public class RawSaploRequest implements JsonRequest {

	private String requestData;
	private JsonCallback callback;
	private SaploConnection connection;

	public RawSaploRequest(String requestData,
			JsonCallback callback, SaploConnection connection) {
		this.requestData = requestData;
		this.callback = callback;
		this.connection = connection;
	}

	public Object getRequestData() {
		return requestData;
	}

	public JsonCallback getCallback() {
		return callback;
	}
	
	public void setCallback(JsonCallback callback) {
		this.callback = callback;
	}

	public void send() {
		connection.call(this);
	}

	public long getRetryInterval() {
		return 1000 * 70;
	}

}
