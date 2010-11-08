package com.voltvoodoo.saplo4j.async.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.AbstractInternalCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;

public class GetAvailableMethodsCallback extends AbstractInternalCallback {
	
	private SaploException exception = null;
	private List<String> methods = null;
	
	public void onSuccessfulResponse(JSONObject response) {
		JSONArray jsonMethods = (JSONArray) response.get("result");
		this.methods = new ArrayList<String>();
		for(Object method : jsonMethods) {
			methods.add((String)method);
		}
	}

	public void onFailedResponse(SaploException exception) {
		this.exception = exception;
	}

	public SaploException getException() {
		return exception;
	}
	
	public List<String> getMethods() {
		return methods;
	}
 
}