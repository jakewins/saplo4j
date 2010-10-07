package com.voltvoodoo.saplo4j.async.impl;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.AbstractInternalCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.model.Language;
import com.voltvoodoo.saplo4j.model.SaploCorpus;

public class GetCorpusInfoCallback extends AbstractInternalCallback {

	private SaploException exception = null;
	private SaploCorpus corpus = null;
	private SaploCorpus.Id corpusId = null;
	
	public GetCorpusInfoCallback(SaploCorpus.Id id) {
		this.corpusId = id;
	}
	
	public void onSuccessfulResponse(JSONObject response) {
		
		JSONObject corpusInfo = (JSONObject) response.get("result");
		
		corpus = new SaploCorpus(
				corpusId, 
				new Language((String)corpusInfo.get("lang")),
				(String)corpusInfo.get("corpusName"),
				(String)corpusInfo.get("corpusDescription"),
				(Long)corpusInfo.get("nextArticleId"));
		
	}

	@Override
	public void onFailedResponse(SaploException exception) {
		this.setException(exception);
	}

	public void setException(SaploException exception) {
		this.exception = exception;
	}

	public SaploException getException() {
		return exception;
	}

	public SaploCorpus getCorpus() {
		return corpus;
	}

}