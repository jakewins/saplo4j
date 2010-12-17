package com.voltvoodoo.saplo4j;

import static com.voltvoodoo.saplo4j.utils.JsonUtils.jsonParams;

import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.voltvoodoo.saplo4j.async.impl.CloseSessionCallback;
import com.voltvoodoo.saplo4j.async.impl.CreateSessionCallback;
import com.voltvoodoo.saplo4j.exception.SaploConnectionException;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.exception.SaploGeneralException;
import com.voltvoodoo.saplo4j.http.JsonClient;
import com.voltvoodoo.saplo4j.http.JsonRequest;
import com.voltvoodoo.saplo4j.http.NettyBasedJsonClient;
import com.voltvoodoo.saplo4j.http.RequestLimitWrapper;

/**
 * A thin layer on top of JsonClient. Handles opening and closing Saplo
 * sessions, as well as recovering requests that fail at the Saplo level, such
 * as abstracting API call limits away.
 * 
 * That means connection reset by peer etc. are not handled here, they are
 * handled by JsonClient.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public class SaploConnection {

	private static int MAX_WAIT_SECONDS = 180;

	private String apiKey;
	private String secretKey;

	private String sessionId;
	private String apiResource;

	private JsonClient jsonClient;

	private boolean hideRequestLimit = true;
	
	private long requestLimitWaitTime = 1000 * 60 * 60;

	/**
	 * If this is set to true, no requests will be sent to the Saplo server.
	 * Instead, requests made will pile up until there are more API requests
	 * available from Saplo.
	 * 
	 * This behavior is enabled by {@link #setHideRequestLimit(boolean)} to
	 * true.
	 */
	private boolean requestLimitReached = false;

	/**
	 * If requests for some reason are halted, new requests will build up in
	 * this linked queue, waiting for the halt to be lifted.
	 */
	private ConcurrentLinkedQueue<JsonRequest> pendingRequests = new ConcurrentLinkedQueue<JsonRequest>();

	private Logger log = Logger.getLogger(getClass());

	//
	// CONSTRUCTOR
	//

	public SaploConnection(String apiKey, String secretKey, String saploUrl, String apiResource) throws SaploException {
		try {
			this.apiKey = apiKey;
			this.secretKey = secretKey;
			this.apiResource = apiResource;

			this.jsonClient = new NettyBasedJsonClient(saploUrl);
		} catch (URISyntaxException e) {
			throw new SaploConnectionException(
					"Unable to connect to Saplo API, url invalid. See nested exception.",
					e);
		}
	}
	
	public SaploConnection(String apiKey, String secretKey, String apiResource, JsonClient jsonClient) throws SaploException {

			this.apiKey = apiKey;
			this.secretKey = secretKey;
			this.apiResource = apiResource;

			this.jsonClient = jsonClient;
	}

	//
	// PUBLIC
	//

	public SaploConnection open() {

		if (!isOpen()) {

			// Init session
			CreateSessionCallback cb = new CreateSessionCallback();
			JsonRequest request = new SaploRequest(
					"auth.createSession", jsonParams(apiKey, secretKey), cb,
					this);

			// We use jsonClient here instead of "call", 
			// since call depends on a session being open.
			jsonClient.post(apiResource,
					request);

			cb.awaitResponse(MAX_WAIT_SECONDS * 1000);

			if (cb.exception != null) {
				throw cb.exception;
			} else {
				this.sessionId = cb.sessionId;
			}
		}
		return this;

	}

	public SaploConnection close() {
		if (isOpen()) {

			CloseSessionCallback cb = new CloseSessionCallback();

			pendingRequests.clear();
			requestLimitReached = false;
			
			call(new SaploRequest("auth.killSession", null, cb, this));

			cb.awaitResponse(MAX_WAIT_SECONDS * 1000);

			this.sessionId = null;

		}
		return this;
	}

	public void call(JsonRequest request) {

		if (isOpen()) {

			if (requestLimitReached) {
				pendingRequests.add(request);
			} else {

				if (hideRequestLimit) {
					request.setCallback(new RequestLimitWrapper(request, this));
				}

				jsonClient.post(apiResource + ";jsessionid=" + sessionId,
						request);
			}

		} else {
			request.getCallback().onFailure(
					new SaploConnectionException("The session is closed."));
		}

	}

	public boolean isOpen() {
		return sessionId != null;
	}

	//
	// SETTINGS
	//

	public SaploConnection setHideRequestLimit(boolean hideRequestLimit) {
		this.hideRequestLimit = hideRequestLimit;
		return this;
	}

	public boolean isHidingRequestLimit() {
		return hideRequestLimit;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * Alert the system that the maximum number of Saplo API calls for this hour
	 * has been reached.
	 * 
	 * The first call to this method each hour will lead to execution halting 
	 * for an hour.
	 * 
	 * If you have a request that has failed due to API call restraints, call this method,
	 * and once it returns, send your request again. If you are the first on to make the call,
	 * this will take an hour to return and then your request will be sent the normal way.
	 * 
	 * If you are not the first one to call this method this hour, this method will return instantly.
	 * When you then send your request it will be added to a queue of requests waiting for the first
	 * call to this method to return.
	 */
	public void handleRequestLimitReached() {
		if (attemptToSetRequestLimitReached()) {
			try {
				int sleepSeconds = Math.round(getRequestLimitWaitTime() / 1000);
				
				log.warn("Max API calls reached, sleeping for " + sleepSeconds + " seconds and will then try again.");
				Thread.sleep(getRequestLimitWaitTime());

				// Get a copy of the pending requests, and clear the original.
				// This is done to allow the request limit to be reached again
				// while we are working through pending requests.
				ConcurrentLinkedQueue<JsonRequest> pendingCopy = pendingRequests;
				pendingRequests = new ConcurrentLinkedQueue<JsonRequest>();

				// Must be done before using call(), otherwise we'd just be
				// adding stuff to the request queue.
				requestLimitReached = false;

				// Perform all queued jobs
				for (JsonRequest req : pendingCopy) {
					call(req);
				}

			} catch (InterruptedException e) {
				
				// Fail all pending requests
				for (JsonRequest req : pendingRequests) {
					req.getCallback().onFailure(new SaploGeneralException("Thread was interrupted while waiting for more API calls to be available, see nested.", req, e));
				}
				
				pendingRequests.clear();
				
			} 
		}
	}

	/**
	 * Set {@link #requestLimitReached} to true, if it was not already.
	 * 
	 * @return true if {@link #requestLimitReached} was changed, false if it was
	 *         already true
	 */
	private synchronized boolean attemptToSetRequestLimitReached() {
		if (requestLimitReached == false) {
			requestLimitReached = true;
			return true;
		} else {
			return false;
		}
	}

	public void setRequestLimitWaitTime(long requestLimitWaitTime) {
		this.requestLimitWaitTime = requestLimitWaitTime;
	}

	public long getRequestLimitWaitTime() {
		return requestLimitWaitTime;
	}
}
