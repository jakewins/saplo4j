package com.voltvoodoo.saplo4j.async.impl;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.AbstractInternalCallback;
import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.model.SaploCorpus;
import com.voltvoodoo.saplo4j.model.SaploDocument;

public class GetDocumentCallback extends AbstractInternalCallback {
	
	public SaploException exception = null;
	public SaploDocument document = null;
	
	protected SaploCallback<SaploDocument> userCallback;
	
	//
	// CONSTRUCTORS
	//
	
	public GetDocumentCallback() {}
	public GetDocumentCallback(SaploCallback<SaploDocument> userCallback) {
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
		// {"id":1,"result":{"headline":"Document 1","corpusId":191,"publishUrl":"","articleId":1}}
		JSONObject jsonData = (JSONObject)result.get("result");
		this.document = new SaploDocument(new SaploDocument.Id((Long) jsonData.get("articleId")), 
				                          new SaploCorpus.Id((Long) jsonData.get("corpusId")),
				                          (String)jsonData.get("headline"),
				                          (String)jsonData.get("publishUrl"));
		
		if(userCallback != null) {
			userCallback.onSuccess(this.document);
		}
	}
}