package com.voltvoodoo.saplo4j.http;

import org.json.simple.JSONObject;

/**
 * A saplo callback that counts the number of times success and failure are called.
 */
public class CountingCallback implements JsonCallback {

	public int successCalls = 0;
	public int failureCalls = 0;
	
	public void onSuccess(JSONObject result) {
		successCalls++;
	}

	public void onFailure(Exception exception) {
		failureCalls++;
	}

}
