package com.voltvoodoo.saplo4j.http;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.Saplo;
import com.voltvoodoo.saplo4j.async.SaploCallback;

/**
 * This is a low-level wrapper for a request to the Saplo API. It contains the
 * data string, url, id and callback, as well as functionality to trigger the
 * request and to replay it if it fails.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public class SaploRequest {

	private static int REQUEST_COUNT = 0;

	private String method;
	private Object params = new JSONArray();
	private Saplo saplo;
	private SaploCallback<Object> callback;

	public SaploRequest(String method, Object params,
			SaploCallback<Object> callback) {

		this.method = method;
		this.callback = callback;

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

		// Send request
		return request;
	}

	public SaploCallback<Object> getCallback() {
		return callback;
	}
}
