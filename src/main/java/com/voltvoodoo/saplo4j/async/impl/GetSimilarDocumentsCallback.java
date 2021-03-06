package com.voltvoodoo.saplo4j.async.impl;

import static com.voltvoodoo.saplo4j.utils.JsonUtils.jsonParams;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.AbstractInternalCallback;
import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.model.SaploCorpus;
import com.voltvoodoo.saplo4j.model.SaploDocument;
import com.voltvoodoo.saplo4j.model.SaploSimilarity;

public class GetSimilarDocumentsCallback extends AbstractInternalCallback {

	public SaploException exception = null;
	public ArrayList<SaploSimilarity> similarDocuments = new ArrayList<SaploSimilarity>();

	protected SaploCallback<List<SaploSimilarity>> userCallback;
	protected SaploDocument.Id sourceDocumentId;
	protected SaploCorpus.Id sourceCorpusId;
	protected List<SaploCorpus.Id> searchIn;

	//
	// CONSTRUCTORS
	//

	public GetSimilarDocumentsCallback(SaploDocument.Id sourceDocumentId,
			SaploCorpus.Id sourceCorpusId) {
		this.sourceCorpusId = sourceCorpusId;
		this.sourceDocumentId = sourceDocumentId;
	}

	public GetSimilarDocumentsCallback(SaploDocument.Id sourceDocumentId,
			SaploCorpus.Id sourceCorpusId,
			SaploCallback<List<SaploSimilarity>> userCallback) {
		this.userCallback = userCallback;
		this.sourceCorpusId = sourceCorpusId;
		this.sourceDocumentId = sourceDocumentId;
	}

	//
	// PUBLIC
	//

	@SuppressWarnings("unchecked")
	public void onFailedResponse(SaploException exception) {
		if (exception.getErrorCode() == 1501) {
			// No results found, trigger successful response with empty result
			// XXX: Triggering an error on empty results is behavior that is due
			// to change in the next iteration of the Saplo API 	
			JSONObject emulatedResponse = new JSONObject();
			emulatedResponse.put("result", jsonParams());
			this.onSuccessfulResponse(emulatedResponse);
		} else {
			this.exception = exception;

			if (userCallback != null) {
				userCallback.onFailure(exception);
			}
		}
	}

	public void onSuccessfulResponse(JSONObject result) {
		// [{"resultCorpusId":191,"resultPublishUrl":"","resultArticleId":3,"matchId":1,"resultHeadline":"Document 3","resultValue":0.6713}]
		
		JSONArray jsonDocuments = (JSONArray) result.get("result");
		JSONObject jsonDocument;

		for (Object document : jsonDocuments) {
			jsonDocument = (JSONObject) document;

			Object resultValueObject = jsonDocument.get("resultValue");

			double resultValue;
			if (resultValueObject instanceof Long) {
				resultValue = ((Long) resultValueObject).doubleValue();
			} else {
				resultValue = (Double) resultValueObject;
			}

			similarDocuments.add(new SaploSimilarity(new SaploSimilarity.Id(
					(Long) jsonDocument.get("matchId")), sourceDocumentId,
					sourceCorpusId, new SaploDocument.Id((Long) jsonDocument
							.get("resultArticleId")), new SaploCorpus.Id(
							(Long) jsonDocument.get("resultCorpusId")),
					resultValue));
		}

		if (userCallback != null) {
			userCallback.onSuccess(this.similarDocuments);
		}
	}
}