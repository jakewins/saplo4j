package com.voltvoodoo.saplo4j.http;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.RequestAware;
import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.exception.SaploConnectionException;

public class DefaultSaploRequest implements SaploRequest {

	private static int REQUEST_COUNT = 0;

	private String method;
	private Object params = new JSONArray();
	private SaploCallback<Object> callback;
	private SaploConnection connection;

	public DefaultSaploRequest(String method, Object params,
			SaploCallback<Object> callback, SaploConnection connection) {

		this.method = method;
		this.callback = callback;
		this.connection = connection;

		if (callback instanceof RequestAware) {
			((RequestAware) callback).setSaploRequest(this);
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

	public SaploCallback<Object> getCallback() {
		return callback;
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

}
