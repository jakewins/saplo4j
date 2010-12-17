package com.voltvoodoo.saplo4j.http;

import com.voltvoodoo.saplo4j.SaploRequest;
import com.voltvoodoo.saplo4j.exception.SaploConnectionException;

/**
 * This is a low-level interface for a request a JSON RPC API. You are
 * probably more interested in the primary implementation of this,
 * {@link SaploRequest}
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public interface JsonRequest {

	public Object getRequestData();

	public JsonCallback getCallback();
	public void setCallback(JsonCallback callback);
	
	/**
	 * If this does not return 0, this should return the time in milliseconds that
	 * should trigger a retry of the request if no response has been given. 
	 * @return
	 */
	public long getRetryInterval();

	/**
	 * Send off this request. 
	 * 
	 * You can use this to re-do failed request.
	 * 
	 * @throws SaploConnectionException
	 */
	public void send();
}
