package com.voltvoodoo.saplo4j.async.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.AbstractInternalCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.model.SaploCorpus;

public class GetCorpusIdsCallback extends AbstractInternalCallback {

	private SaploException exception = null;
	private List<SaploCorpus.Id> corpusIds = new ArrayList<SaploCorpus.Id>();
	
	public void onSuccessfulResponse(JSONObject response) {
		corpusIds.clear();
		JSONArray corpuses = (JSONArray) response.get("result");
		for(Object rawCorpus : corpuses) {
			corpusIds.add(new SaploCorpus.Id((Long)((JSONObject)rawCorpus).get("corpusId")));
		}
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

	public List<SaploCorpus.Id> getCorpusIds() {
		return corpusIds;
	}

}
