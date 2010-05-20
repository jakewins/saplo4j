package com.voltvoodoo.saplo4j.async.impl;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.AbstractInternalCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.model.SaploCorpus;

/**
 * Callback used when creating corpuses.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 *
 */
public class CreateCorpusCallback extends AbstractInternalCallback {
	
	public SaploException exception = null;
	public SaploCorpus.Id corpusId  = null;
	
	public void onFailedResponse(SaploException exception) {
		this.exception = exception;
	}

	public void onSuccessfulResponse(JSONObject result) {
		this.corpusId = new SaploCorpus.Id( (Long) ((JSONObject)result.get("result")).get("corpusId") );
	}
	
}