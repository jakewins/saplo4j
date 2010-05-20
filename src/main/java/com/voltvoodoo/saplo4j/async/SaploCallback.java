package com.voltvoodoo.saplo4j.async;

import com.voltvoodoo.saplo4j.exception.SaploException;


/**
 * Callback for the asynchronous match calls.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 *
 */
public interface SaploCallback<T> {

	public void onSuccess(T result);
	public void onFailure (SaploException exception);
	
}
