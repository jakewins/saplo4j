package com.voltvoodoo.saplo4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.exception.SaploConnectionException;
import com.voltvoodoo.saplo4j.http.JsonCallback;
import com.voltvoodoo.saplo4j.http.JsonRequest;
import com.voltvoodoo.saplo4j.http.RequestAware;

public class SaploRequest implements JsonRequest {

	private static int REQUEST_COUNT = 0;

	private String method;
	private Object params = new JSONArray();
	private JsonCallback callback;
	private SaploConnection connection;
	private long retryInterval;
	
	public SaploRequest(String method, Object params,
			JsonCallback callback, SaploConnection connection) {
		this(method, params, callback, connection, 1000 * 70);
	}
	
	public SaploRequest(String method, Object params,
			JsonCallback callback, SaploConnection connection, long retryInterval) {

		this.method = method;
		this.callback = callback;
		this.connection = connection;
		this.retryInterval = retryInterval;

		if (callback instanceof RequestAware) {
			((RequestAware) callback).setJsonRequest(this);
		}

		if (params != null) {
			this.params = params;
		}

	}

	@SuppressWarnings("unchecked")
	public JSONObject getRequestData() {
		JSONObject request = new JSONObject();

		request.put("method", method);
		request.put("params", params);
		request.put("id", ++REQUEST_COUNT);

		return request;
	}

	public JsonCallback getCallback() {
		return callback;
	}
	
	public void setCallback(JsonCallback callback) {
		this.callback = callback;
	}

	/**
	 * This will replay the request with the same saplo connection it was
	 * created with. This is used internally to handle network problems and
	 * other exceptions that can be recovered.
	 * 
	 * It can also be used externally, for instance by catching a
	 * SaploException, getting the request object (#getRequest), and then
	 * triggering this to redo the request.
	 * 
	 * @throws SaploConnectionException
	 */
	public void send() throws SaploConnectionException {
		connection.call(this);
	}

	/**
	 * This request will be re-sent after this amount of time.
	 */
	public long getRetryInterval() {
		return retryInterval;
	}
	
	public void setRetryInterval(long retryInterval) {
		this.retryInterval = retryInterval;
	}

}
