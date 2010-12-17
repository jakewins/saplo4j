package com.voltvoodoo.saplo4j.http;


/**
 * Interface for callbacks that want to have the JSONRPC request object made
 * available to them.
 * 
 * Creating a JsonRequest with a callback that implements this interface will
 * give that callback access to the request object.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public interface RequestAware {

	void setJsonRequest(JsonRequest req);

	JsonRequest getJsonRequest();

}
