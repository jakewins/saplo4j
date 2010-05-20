package com.voltvoodoo.saplo4j.async.impl;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.AbstractInternalCallback;
import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.model.SaploDocument;

public class UpdateDocumentCallback extends AbstractInternalCallback {
	
	public SaploException exception = null;
	
	protected SaploCallback<Boolean> userCallback;
	
	//
	// CONSTRUCTORS
	//
	
	public UpdateDocumentCallback() {}
	public UpdateDocumentCallback(SaploCallback<Boolean> userCallback) {
		this.userCallback = userCallback;
	}
	
	//
	// PUBLIC
	//
	
	public void onFailedResponse(SaploException exception) {
		this.exception = exception;
		
		if(userCallback != null) {
			userCallback.onFailure(exception);
		}
	}

	public void onSuccessfulResponse(JSONObject result) {
		if(userCallback != null) {
			userCallback.onSuccess(true);
		}
	}
	
}