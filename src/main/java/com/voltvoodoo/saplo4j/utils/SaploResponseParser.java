package com.voltvoodoo.saplo4j.utils;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.voltvoodoo.saplo4j.exception.SaploAuthException;
import com.voltvoodoo.saplo4j.exception.SaploContextException;
import com.voltvoodoo.saplo4j.exception.SaploCorpusException;
import com.voltvoodoo.saplo4j.exception.SaploGeneralException;
import com.voltvoodoo.saplo4j.exception.SaploInternalException;
import com.voltvoodoo.saplo4j.exception.SaploMatchException;
import com.voltvoodoo.saplo4j.exception.SaploRequestLimitReachedException;
import com.voltvoodoo.saplo4j.exception.SaploStillProcessingException;
import com.voltvoodoo.saplo4j.exception.SaploTagException;
import com.voltvoodoo.saplo4j.exception.SaploUnknownException;
import com.voltvoodoo.saplo4j.http.JsonRequest;

/**
 * Reads saplo JSON responses (or determines they are not JSON), converts errors
 * into thrown exceptions, or returns a JSONObject containing the response.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public class SaploResponseParser {

	public JSONObject parseSaploResponse(String response) {
		return parseSaploResponse(response, null);
	}

	public JSONObject parseSaploResponse(String textResponse,
			JsonRequest request) {

		Object response = JSONValue.parse(textResponse);

		// Check for errors
		if (response instanceof JSONObject) {
			JSONObject jsonResult = (JSONObject) response;

			if (jsonResult.containsKey("error")) {

				int errorCode = -1;
				String errorMessage = "Unknown error, Saplo did not return an error message.";

				if (((JSONObject) jsonResult.get("error")).containsKey("code")) {
					errorCode = Integer.valueOf(((JSONObject) jsonResult
							.get("error")).get("code").toString());
				}

				if (((JSONObject) jsonResult.get("error")).containsKey("msg")) {
					errorMessage = ((JSONObject) jsonResult.get("error")).get(
							"msg").toString();
				}

				errorMessage = "(" + errorCode + ") " + errorMessage;
				if (errorCode == 1003 || errorCode == 1004 || errorCode == 1005) {
					throw new SaploStillProcessingException(errorCode,
							errorMessage, request, null);
				} else if (errorCode == 899) {
					throw new SaploUnknownException(errorCode, errorMessage,
							request, null);
				} else if (errorCode == 1008 || errorCode == 1007) {
					throw new SaploRequestLimitReachedException(errorCode,
							errorMessage, request, null);
				} else if (errorCode == 1010) {
					throw new SaploInternalException(errorCode, errorMessage,
							request, null);
				} else if (errorCode < 1100) {
					throw new SaploGeneralException(errorCode, errorMessage,
							request, null);
				} else if (errorCode < 1200) {
					throw new SaploAuthException(errorCode, errorMessage,
							request, null);
				} else if (errorCode < 1300) {
					throw new SaploCorpusException(errorCode, errorMessage,
							request, null);
				} else if (errorCode < 1400) {
					throw new SaploTagException(errorCode, errorMessage,
							request, null);
				} else if (errorCode < 1500) {
					throw new SaploContextException(errorCode, errorMessage,
							request, null);
				} else if (errorCode < 1600) {
					throw new SaploMatchException(errorCode, errorMessage,
							request, null);
				} else {
					throw new SaploGeneralException(errorCode, errorMessage,
							request, null);
				}

			}

			return jsonResult;
		} else {
			throw new SaploGeneralException("Got non-JSON response.", request);
		}
	}
}
