package com.voltvoodoo.saplo4j.async;

import java.util.Date;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.exception.SaploGeneralException;
import com.voltvoodoo.saplo4j.http.JsonCallback;
import com.voltvoodoo.saplo4j.http.JsonRequest;
import com.voltvoodoo.saplo4j.http.RequestAware;

/**
 * Parent callback that adds support for interrupting (ie. ignoring) the response,
 * as well as halting execution until a response is recieved.
 * 
 * This also provides a "wait" method, {@link #awaitResponse(long)}, that allows
 * you to block the current thread until your callback has been called. This in
 * effect emulates synchronous behavior.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public abstract class AbstractInternalCallback implements
	JsonCallback, RequestAware {

	protected boolean gotResponse = false;
	protected boolean alive = true;
	protected JsonRequest request;

	private boolean interrupted = false;
	
	//
	// PUBLIC
	//

	public abstract void onSuccessfulResponse(JSONObject response);

	public abstract void onFailedResponse(SaploException exception);

	public void onFailure(Exception exception) {

		if (alive) {
			if(exception instanceof SaploException ) {
				onFailedResponse((SaploException)exception);
			} else {
				onFailedResponse(new SaploGeneralException(exception));
			}
		}

		gotResponse = true;
	}

	public void onSuccess(JSONObject response) {
		
		if (alive) {
			onSuccessfulResponse(response);
		}

		gotResponse = true;
	}

	public void awaitResponse(long timeout) throws SaploGeneralException {
		Date current, end = new Date();
		end.setTime(end.getTime() + timeout);

		// Lock up thread until response, interrupt or timeout
		while (!gotResponse) {
			try {
				Thread.sleep(20);

				current = new Date();
				if (current.after(end)) {
					this.alive = false; // Make sure the wrapped callback is not
										// called later
					onFailedResponse(new SaploGeneralException(
							"Waiting for saplo to respond timed out (the timeout set was "
									+ timeout + "ms)."));
					break;
					
				} else if(interrupted) {
					this.alive = false;
					onFailedResponse(new SaploGeneralException("The call was interrupted by something manually calling interrupt() on the callback on our side of the wire.", request));
				}

			} catch (InterruptedException e) {
				onFailedResponse(new SaploGeneralException("The call was interrupted by something manually calling interrupt() on the thread on our side of the wire.", request));
			}
		}
	}
	
	public void interrupt() {
		interrupted = true;
	}

	//
	// REQUEST AWARENESS
	//

	public void setJsonRequest(JsonRequest request) {
		this.request = request;
	}

	public JsonRequest getJsonRequest() {
		return this.request;
	}
}
