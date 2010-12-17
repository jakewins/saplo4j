package com.voltvoodoo.saplo4j.functional.mocksaplo;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

/**
 * From simpleframeworks test suites.
 */
public interface Handler {

	public void handle(Request request, Response response) throws Exception;
	
}
