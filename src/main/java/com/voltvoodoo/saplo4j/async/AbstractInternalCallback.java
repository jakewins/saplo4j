package com.voltvoodoo.saplo4j.async;

import java.util.Date;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.exception.SaploAuthException;
import com.voltvoodoo.saplo4j.exception.SaploContextException;
import com.voltvoodoo.saplo4j.exception.SaploCorpusException;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.exception.SaploGeneralException;
import com.voltvoodoo.saplo4j.exception.SaploMatchException;
import com.voltvoodoo.saplo4j.exception.SaploTagException;

/**
 * Parent for callbacks that are handle "raw" responses from Saplo. This class
 * takes care of error checking and JSON typecasting.
 * 
 * This also provides a "wait" method, {@link #awaitResponse(long)}, that allows you
 * to block the current thread until your callback has been called. This in effect
 * emulates synchronous behavior.
 *  
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 *
 */
public abstract class AbstractInternalCallback implements SaploCallback<Object> {

	protected volatile boolean gotResponse = false;
	protected volatile boolean alive = true;
	
	//
	// PUBLIC
	//
	
	public abstract void onSuccessfulResponse(JSONObject response);
	public abstract void onFailedResponse(SaploException exception);
	
	public void onFailure(SaploException exception) {
		
		if( alive ) {
			onFailedResponse(exception);
		}
		
		gotResponse = true;
	}

	public void onSuccess(Object result) {

		if( alive ) {
			
//			System.out.println("RESPONSE: " + result);
			
			// Check for errors
			JSONObject jsonResult = (JSONObject)result;
			
			if( jsonResult == null) {
				
				// Server returned non-json response
				onFailedResponse(new SaploGeneralException("Saplo API returned a non-JSON response."));
				
			} else if(jsonResult.containsKey("error")) {
				
				int errorCode = -1;
				String errorMessage = "Error in error message: Saplo did not return an error description.";
				
				if( ((JSONObject)jsonResult.get("error")).containsKey("code") ) {
					errorCode = Integer.valueOf(((JSONObject)jsonResult.get("error")).get("code").toString());
				}
				
				if( ((JSONObject)jsonResult.get("error")).containsKey("msg") ) {
					errorMessage = ((JSONObject)jsonResult.get("error")).get("msg").toString();
				}
				
				errorMessage = "(" + errorCode + ") " + errorMessage;
				
				SaploException exception;
				if ( errorCode < 1100 ) {
					exception = new SaploGeneralException(errorCode, errorMessage);
				} else if ( errorCode < 1200 ) {
					exception = new SaploAuthException(errorCode, errorMessage);
				} else if ( errorCode < 1300 ) {
					exception = new SaploCorpusException(errorCode, errorMessage);
				} else if ( errorCode < 1400 ) {
					exception = new SaploTagException(errorCode, errorMessage);
				} else if ( errorCode < 1500 ) {
					exception = new SaploContextException(errorCode, errorMessage);
				} else if ( errorCode < 1600 ) {
					exception = new SaploMatchException(errorCode, errorMessage);
				} else {
					exception = new SaploGeneralException(errorCode, errorMessage);
				}
				
				onFailedResponse(exception);
				
			} else {
				onSuccessfulResponse(jsonResult);
			}
		}
		
		gotResponse = true;
	}
	
	public void awaitResponse(long timeout) throws SaploGeneralException {
		Date current, end = new Date();
		end.setTime(end.getTime() + timeout);
		
		// Lock up thread until response, interrupt or timeout
		while( ! gotResponse) {
			try {
				Thread.sleep(20);
				
				current = new Date();
				if ( current.after( end )) {
					this.alive = false; // Make sure the wrapped callback is not called later
					throw new SaploGeneralException("Waiting for saplo to respond timed out (current timeout is " + timeout + "ms).");
				}
				
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
