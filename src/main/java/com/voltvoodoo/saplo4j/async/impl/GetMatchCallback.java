package com.voltvoodoo.saplo4j.async.impl;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.AbstractInternalCallback;
import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.model.SaploCorpus;
import com.voltvoodoo.saplo4j.model.SaploDocument;
import com.voltvoodoo.saplo4j.model.SaploSimilarity;

public class GetMatchCallback extends AbstractInternalCallback {

	public SaploException exception = null;
	public SaploSimilarity similarity = null;

	protected SaploCallback<SaploSimilarity> userCallback;

	private SaploSimilarity.Id id;
	private SaploCorpus.Id corpusId;
	private SaploDocument.Id documentId;

	//
	// CONSTRUCTORS
	//

	public GetMatchCallback() {
	}

	public GetMatchCallback(SaploCorpus.Id corpusId, SaploSimilarity.Id id,
			SaploDocument.Id documentId) {
		this(corpusId, id, documentId, null);
	}

	public GetMatchCallback(SaploCorpus.Id corpusId, SaploSimilarity.Id id,
			SaploDocument.Id documentId,
			SaploCallback<SaploSimilarity> userCallback) {
		this.corpusId = corpusId;
		this.id = id;
		this.documentId = documentId;
		this.userCallback = userCallback;
	}

	//
	// PUBLIC
	//

	public void onFailedResponse(SaploException exception) {
		this.exception = exception;

		if (userCallback != null) {
			userCallback.onFailure(exception);
		}
	}

	public void onSuccessfulResponse(JSONObject result) {
		// {"id":1,"result":{"headline":"Document 1","corpusId":191,"publishUrl":"","articleId":1}}
		JSONObject jsonData = (JSONObject) result.get("result");
		this.similarity = new SaploSimilarity(id, documentId, corpusId,
				new SaploDocument.Id((Long) jsonData.get("resultArticleId")),
				new SaploCorpus.Id((Long) jsonData.get("resultCorpusId")),
				(Double) jsonData.get("resultValue"));

		if (userCallback != null) {
			userCallback.onSuccess(this.similarity);
		}
	}
}