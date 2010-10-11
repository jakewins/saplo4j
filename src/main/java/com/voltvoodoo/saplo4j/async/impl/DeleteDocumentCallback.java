package com.voltvoodoo.saplo4j.async.impl;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.AbstractInternalCallback;
import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;

public class DeleteDocumentCallback extends AbstractInternalCallback {

	private SaploException exception = null;
	private SaploCallback<Boolean> userCallback = null;
	
	public DeleteDocumentCallback() {
		
	}
	
	public DeleteDocumentCallback(SaploCallback<Boolean> callback) {
		this.userCallback = callback;
	}
	
	public void onSuccessfulResponse(JSONObject response) {
		if(userCallback != null) {
			userCallback.onSuccess(true);
		}
	}


	public void onFailedResponse(SaploException exception) {
		this.setException(exception);
		if(userCallback != null) {
			userCallback.onFailure(exception);
		}
	}


	public void setException(SaploException exception) {
		this.exception = exception;
	}


	public SaploException getException() {
		return exception;
	}

}
