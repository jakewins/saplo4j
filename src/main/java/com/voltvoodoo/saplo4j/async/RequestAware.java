package com.voltvoodoo.saplo4j.async;

import com.voltvoodoo.saplo4j.http.SaploRequest;

/**
 * Interface for callbacks that want to have the Saplo request object made
 * available to them.
 * 
 * Creating a SaploRequest with a callback that implements this interface will
 * give that callback access to the request object.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public interface RequestAware {

	void setSaploRequest(SaploRequest req);

	SaploRequest getSaploRequest();

}
