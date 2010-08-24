package com.voltvoodoo.saplo4j.utils;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.exception.SaploAuthException;
import com.voltvoodoo.saplo4j.exception.SaploContextException;
import com.voltvoodoo.saplo4j.exception.SaploCorpusException;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.exception.SaploGeneralException;
import com.voltvoodoo.saplo4j.exception.SaploMatchException;
import com.voltvoodoo.saplo4j.exception.SaploTagException;

public class SaploDataUtils {

	public static JSONObject parseSaploResponse(Object result)
			throws SaploException {

		// Check for errors
		JSONObject jsonResult = (JSONObject) result;

		if (jsonResult == null) {

			// Server returned non-json response
			throw new SaploGeneralException(
					"Saplo API returned a non-JSON response.");

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
				throw new SaploGeneralException(errorCode, errorMessage);
			} else if (errorCode < 1200) {
				throw new SaploAuthException(errorCode, errorMessage);
			} else if (errorCode < 1300) {
				throw new SaploCorpusException(errorCode, errorMessage);
			} else if (errorCode < 1400) {
				throw new SaploTagException(errorCode, errorMessage);
			} else if (errorCode < 1500) {
				throw new SaploContextException(errorCode, errorMessage);
			} else if (errorCode < 1600) {
				throw new SaploMatchException(errorCode, errorMessage);
			} else {
				throw new SaploGeneralException(errorCode, errorMessage);
			}

		}

		return jsonResult;
	}
}
