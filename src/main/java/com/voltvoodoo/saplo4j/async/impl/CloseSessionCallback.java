package com.voltvoodoo.saplo4j.async.impl;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.AbstractInternalCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;

public class CloseSessionCallback extends AbstractInternalCallback {
	
	public SaploException exception = null;
	
	public void onFailedResponse(SaploException exception) {
		this.exception = exception;
	}

	public void onSuccessfulResponse(JSONObject result) {
		// Om-nom-nom
	}
	
}