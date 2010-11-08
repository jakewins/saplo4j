package com.voltvoodoo.saplo4j.http;

import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.exception.SaploConnectionException;

/**
 * This is a low-level interface for a request to the Saplo API. You are
 * probably more interested in the primary implementation of this,
 * {@link DefaultSaploRequest}
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public interface SaploRequest {

	public Object getRequestData();

	public SaploCallback<Object> getCallback();

	/**
	 * This trigger the request to saplo. 
	 * 
	 * You can use this to re-do failed requests, for instance by catching a
	 * SaploException, getting the request object (#getRequest), and then
	 * triggering this.
	 * 
	 * @throws SaploConnectionException
	 */
	public void send();
}
