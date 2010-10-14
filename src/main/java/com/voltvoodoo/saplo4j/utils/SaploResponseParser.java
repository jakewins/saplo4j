package com.voltvoodoo.saplo4j.utils;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.exception.SaploAuthException;
import com.voltvoodoo.saplo4j.exception.SaploContextException;
import com.voltvoodoo.saplo4j.exception.SaploCorpusException;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.exception.SaploGeneralException;
import com.voltvoodoo.saplo4j.exception.SaploMatchException;
import com.voltvoodoo.saplo4j.exception.SaploTagException;
import com.voltvoodoo.saplo4j.http.SaploRequest;

public class SaploResponseParser {

	public static JSONObject parseSaploResponse(Object result)
			throws SaploException {
		return parseSaploResponse(result, null);
	}

	public static JSONObject parseSaploResponse(Object response,
			SaploRequest request) throws SaploException {

		// Check for errors
		JSONObject jsonResult = (JSONObject) response;

		if (jsonResult == null) {

			// Server returned non-json response
			throw new SaploGeneralException(-1,
					"Saplo API returned a non-JSON response.", request, null);

		} else if (jsonResult.containsKey("error")) {

			int errorCode = -1;
			String errorMessage = "Error in error message: Saplo did not return an error description.";

			if (((JSONObject) jsonResult.get("error")).containsKey("code")) {
				errorCode = Integer.valueOf(((JSONObject) jsonResult
						.get("error")).get("code").toString());
			}

			if (((JSONObject) jsonResult.get("error")).containsKey("msg")) {
				errorMessage = ((JSONObject) jsonResult.get("error"))
						.get("msg").toString();
			}

			errorMessage = "(" + errorCode + ") " + errorMessage;

			if (errorCode < 1100) {
				throw new SaploGeneralException(errorCode, errorMessage,
						request, null);
			} else if (errorCode < 1200) {
				throw new SaploAuthException(errorCode, errorMessage, request,
						null);
			} else if (errorCode < 1300) {
				throw new SaploCorpusException(errorCode, errorMessage,
						request, null);
			} else if (errorCode < 1400) {
				throw new SaploTagException(errorCode, errorMessage, request,
						null);
			} else if (errorCode < 1500) {
				throw new SaploContextException(errorCode, errorMessage,
						request, null);
			} else if (errorCode < 1600) {
				throw new SaploMatchException(errorCode, errorMessage, request,
						null);
			} else {
				throw new SaploGeneralException(errorCode, errorMessage,
						request, null);
			}

		}

		return jsonResult;
	}
}
