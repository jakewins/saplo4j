package com.voltvoodoo.saplo4j.async.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.AbstractInternalCallback;
import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.model.SaploCorpus;
import com.voltvoodoo.saplo4j.model.SaploDocument;
import com.voltvoodoo.saplo4j.model.SaploTag;

public class GetTagsCallback extends AbstractInternalCallback {

	public SaploException exception = null;
	public List<SaploTag> result = null;

	protected SaploCallback<List<SaploTag>> userCallback;

	private SaploCorpus.Id corpusId;
	private SaploDocument.Id documentId;

	public static SaploTag.Type getType(long tagId) {
		if (tagId == 2)
			return SaploTag.Type.TOPIC;
		if (tagId == 3)
			return SaploTag.Type.PERSON;
		if (tagId == 4)
			return SaploTag.Type.ORGANIZATION;
		if (tagId == 5)
			return SaploTag.Type.LOCATION;

		return SaploTag.Type.UNKNOWN;
	}

	//
	// CONSTRUCTORS
	//

	public GetTagsCallback() {
	}

	public GetTagsCallback(SaploCorpus.Id corpusId,
			SaploDocument.Id documentId) {
		this(corpusId, documentId, null);
	}

	public GetTagsCallback(SaploCorpus.Id corpusId,
			SaploDocument.Id documentId,
			SaploCallback<List<SaploTag>> userCallback) {
		this.corpusId = corpusId;
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
		JSONArray jsonTags = (JSONArray) result.get("result");
		JSONObject jsonTag;

		this.result = new ArrayList<SaploTag>();

		for (Object rawTag : jsonTags) {
			jsonTag = (JSONObject) rawTag;

			this.result.add(new SaploTag(new SaploTag.Id((Long) jsonTag
					.get("tagId")), corpusId, documentId, (String) jsonTag
					.get("tagWord"),
					getType((Long) jsonTag.get("tagTypeId"))));
		}
		if (userCallback != null) {
			userCallback.onSuccess(this.result);
		}
	}
}