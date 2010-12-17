package com.voltvoodoo.saplo4j.http;

import org.json.simple.JSONObject;

public interface JsonCallback {

	public void onSuccess(JSONObject result);

	public void onFailure(Exception exception);
	
}
