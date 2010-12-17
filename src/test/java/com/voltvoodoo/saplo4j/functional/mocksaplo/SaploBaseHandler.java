package com.voltvoodoo.saplo4j.functional.mocksaplo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.simpleframework.http.Request;

public abstract class SaploBaseHandler extends SimpleHandler {

	public String handle(Request request) throws Exception {
		JSONObject reqData = (JSONObject)JSONValue.parse(request.getContent());
		String method = (String) reqData.get("method");
		JSONArray args = (JSONArray) reqData.get("params");
		Long reqId = (Long) reqData.get("id");
		
		if( method.equals("auth.createSession")) {
			return "{\"result\":\"somesessionid\"}";
		} else {
			return handle(method, args, reqId);
		}
	}
	
	public abstract String handle(String method, JSONArray args, Long requestId) throws Exception;
	
}
