package com.voltvoodoo.saplo4j.async.impl;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.AbstractInternalCallback;
import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.model.SaploDocument;

public class AddDocumentCallback extends AbstractInternalCallback {
	
	public SaploException exception = null;
	public SaploDocument.Id documentId = null;
	
	protected SaploCallback<SaploDocument.Id> userCallback;
	
	//
	// CONSTRUCTORS
	//
	
	public AddDocumentCallback() {}
	public AddDocumentCallback(SaploCallback<SaploDocument.Id> userCallback) {
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
		documentId = new SaploDocument.Id( (Long) ((JSONObject)result.get("result")).get("articleId") );
		
		if(userCallback != null) {
			userCallback.onSuccess(this.documentId);
		}
	}
}