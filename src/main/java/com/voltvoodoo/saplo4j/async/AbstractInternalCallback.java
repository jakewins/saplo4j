package com.voltvoodoo.saplo4j.async;

import static com.voltvoodoo.saplo4j.utils.SaploDataUtils.parseSaploResponse;

import java.util.Date;

import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.exception.SaploGeneralException;

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
public abstract class AbstractInternalCallback implements SaploCallback<Object> {

	protected volatile boolean gotResponse = false;
	protected volatile boolean alive = true;

	//
	// PUBLIC
	//

	public abstract void onSuccessfulResponse(JSONObject response);

	public abstract void onFailedResponse(SaploException exception);

	public void onFailure(SaploException exception) {

		if (alive) {
			onFailedResponse(exception);
		}

		gotResponse = true;
	}

	public void onSuccess(Object result) {

		if (alive) {
			try {
				onSuccessfulResponse(parseSaploResponse(result));
			} catch (SaploException e) {
				onFailedResponse(e);
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
				}

			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
