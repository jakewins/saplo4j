package com.voltvoodoo.saplo4j.async.impl;

import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;


/**
 * A saplo callback implementation that triggers a separate method when its "onSuccess"
 * method has been called a certain number of times. This is good when you make a 
 * set amount of calls, and want to trigger an event when all of them are finished.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 *
 */
public abstract class SaploCountDownCallback<T> implements SaploCallback<T> {

	protected int callCountDown;
	
	public SaploCountDownCallback(int numCalls) {
		this.callCountDown = numCalls;
	}

	/**
	 * Triggered when the number of calls specified has been reached.
	 */
	public abstract void onAllComplete();
	
	/**
	 * Replacement for onSuccess, override this to handle each result.
	 * @param result
	 */
	public void onEachSuccess(T result) {
		/* NOP */
	}
	
	/**
	 * Replacement for onFailure, override this to handle each failure.
	 * @param exception
	 */
	public void onEachFailure(SaploException exception) {
		/* NOP */
	}
	
	public final void onSuccess(T result) {
		this.onEachSuccess(result);
		this.called();
	}

	public final void onFailure(SaploException exception) {
		this.onEachFailure(exception);
		this.called();
	}
	
	//
	// INTERNALS
	//
	
	private synchronized void called() {
		this.callCountDown--;
		if(this.callCountDown == 0) {
			this.onAllComplete();
		}
	}
	
}
