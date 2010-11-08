package com.voltvoodoo.saplo4j.async;

import static com.voltvoodoo.saplo4j.utils.SaploResponseParser.parseSaploResponse;

import java.util.Date;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.exception.SaploGeneralException;
import com.voltvoodoo.saplo4j.http.SaploRequest;

/**
 * Parent for callbacks that are handle "raw" responses from Saplo. This class
 * takes care of error checking and JSON typecasting.
 * 
 * This also provides a "wait" method, {@link #awaitResponse(long)}, that allows
 * you to block the current thread until your callback has been called. This in
 * effect emulates synchronous behavior.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public abstract class AbstractInternalCallback implements
		SaploCallback<Object>, RequestAware {

	protected volatile boolean gotResponse = false;
	protected volatile boolean alive = true;
	protected volatile SaploRequest request;

	private boolean interrupted = false;
	
	//
	// PUBLIC
	//

	public abstract void onSuccessfulResponse(JSONObject response);

	public abstract void onFailedResponse(SaploException exception);

	public void onFailure(SaploException exception) {

		if (alive) {
			if (!handleTimeoutExceptions(exception)) {
				onFailedResponse(exception);
			}
		}

		gotResponse = true;
	}

	public void onSuccess(Object response) {

		if (alive) {
			try {
				onSuccessfulResponse(parseSaploResponse(response, request));
			} catch (SaploException e) {
				if (!handleTimeoutExceptions(e)) {
					onFailedResponse(e);
				}
			}
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
					throw new SaploGeneralException(
							"Waiting for saplo to respond timed out (current timeout is "
									+ timeout + "ms).");
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

	public void setSaploRequest(SaploRequest request) {
		this.request = request;
	}

	public SaploRequest getSaploRequest() {
		return this.request;
	}

	//
	// INTERNALS
	//

	private boolean handleTimeoutExceptions(SaploException exception) {
		if (exception.getErrorCode() == 1004
				|| exception.getErrorCode() == 1005) {
			// Saplo is still working, send request again.
			try {
				getSaploRequest().send();
				return true;
			} catch (SaploException e) {

			}
		}

		return false;
	}
}
