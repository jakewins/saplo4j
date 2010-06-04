package com.voltvoodoo.saplo4j.async.impl;

import java.util.ArrayList;
import java.util.Date;

import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.exception.SaploGeneralException;


/**
 * A saplo callback implementation that triggers a separate method when its "onSuccess"
 * method has been called a certain number of times. This is good when you make a 
 * set amount of calls, and want to trigger an event when all of them are finished.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 *
 */
public abstract class SaploCountDownCallback<T> implements SaploCallback<T> {

	protected ArrayList<SaploException> exceptions = new ArrayList<SaploException>();
	protected volatile int callCountDown;
	
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
		if( exception != null ) {
			this.exceptions.add(exception);
		}
		
		this.onEachFailure(exception);
		this.called();
	}
	
	/**
	 * Check if there have been any exceptions caught.
	 */
	public boolean hasExceptions() {
		return this.exceptions.size() > 0;
	}
	
	/**
	 * @return the list of exceptions
	 */
	public ArrayList<SaploException> getExceptions() {
		return this.exceptions;
	}
	
	/**
	 * Wait until the number of calls to onSuccess and/or onFailure defined by the numCalls have been made.
	 * @param timeout
	 * @throws SaploGeneralException
	 */
	public void awaitAllFinish(long timeout) throws SaploGeneralException {
		Date current, end = new Date();
		end.setTime(end.getTime() + timeout);
		
		// Lock up thread until response, interrupt or timeout
		while( callCountDown > 0 ) {
			try {
				Thread.sleep(20);
				
				current = new Date();
				if ( current.after( end )) {
					throw new SaploGeneralException("Waiting for all saplo callbacks to finish timed out (current timeout is " + timeout + "ms).");
				}
				
			} catch (InterruptedException e) {
				break;
			}
		}
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
