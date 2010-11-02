package com.voltvoodoo.saplo4j.async.impl;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.AbstractInternalCallback;
import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;

public class DeleteSimilarityCallback extends AbstractInternalCallback {

	public SaploException exception = null;
	public Boolean result = null;

	protected SaploCallback<Boolean> userCallback;

	//
	// CONSTRUCTORS
	//

	public DeleteSimilarityCallback() {
	}

	public DeleteSimilarityCallback(SaploCallback<Boolean> userCallback) {
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
		this.result = true;
		if (userCallback != null) {
			userCallback.onSuccess(this.result);
		}
	}

}