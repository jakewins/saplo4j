package com.voltvoodoo.saplo4j.http;

import com.voltvoodoo.saplo4j.async.SaploCallback;

public class RawSaploRequest implements SaploRequest {

	private String requestData;
	private SaploCallback<Object> callback;
	private SaploConnection connection;
	
	public RawSaploRequest(String requestData, SaploCallback<Object> callback, SaploConnection connection) {
		this.requestData = requestData;
		this.callback = callback;
		this.connection = connection;
	}
	
	public Object getRequestData() {
		return requestData;
	}

	public SaploCallback<Object> getCallback() {
		return callback;
	}

	public void send() {
		connection.call(this);
	}

}
