package com.voltvoodoo.saplo4j.async.impl;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.AbstractInternalCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;

public class CreateSessionCallback extends AbstractInternalCallback {
	
	public SaploException exception = null;
	public String sessionId;
	
	public void onFailedResponse(SaploException exception) {
		this.exception = exception;
	}

	public void onSuccessfulResponse(JSONObject result) {
		this.sessionId = (String)result.get("result");
	}
	
}