package com.voltvoodoo.saplo4j.http;

import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.async.impl.CloseSessionCallback;
import com.voltvoodoo.saplo4j.async.impl.CreateSessionCallback;
import com.voltvoodoo.saplo4j.exception.SaploConnectionException;
import com.voltvoodoo.saplo4j.exception.SaploException;

public class DefaultSaploConnection implements SaploConnection {

	private static int MAX_WAIT_SECONDS = 180;

	private String apiKey;
	private String secretKey;

	private String sessionId;
	private String apiResource;

	private JsonClient jsonClient;

	private boolean hideRequestLimit = false;

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
	private ConcurrentLinkedQueue<SaploRequest> pendingRequests = new ConcurrentLinkedQueue<SaploRequest>();

	//
	// CONSTRUCTOR
	//

	public DefaultSaploConnection(String apiKey, String secretKey, String saploUrl,
			String apiResource) throws SaploException {

		try {
			this.apiKey = apiKey;
			this.secretKey = secretKey;
			this.apiResource = apiResource;

			this.jsonClient = new JsonClient(saploUrl);

		} catch (URISyntaxException e) {
			throw new SaploConnectionException(
					"Unable to connect to Saplo API, url invalid. See nested exception.",
					e);
		}
	}

	//
	// PUBLIC
	//

	public DefaultSaploConnection open() throws SaploException {

		if (!isOpen()) {
			// Init session
			CreateSessionCallback cb = new CreateSessionCallback();
			initSession(apiKey, secretKey, cb);

			cb.awaitResponse(MAX_WAIT_SECONDS * 1000);

			if (cb.exception != null) {
				throw cb.exception;
			} else {
				this.sessionId = cb.sessionId;
			}
		}
		return this;

	}

	public DefaultSaploConnection close() throws SaploException {
		if (isOpen()) {
			CloseSessionCallback cb = new CloseSessionCallback();

			pendingRequests.clear();
			call(new SaploRequest("auth.killSession", null, cb, this));

			cb.awaitResponse(MAX_WAIT_SECONDS * 1000);

			this.sessionId = null;

		}
		return this;
	}

	public DefaultSaploConnection call(SaploRequest request)
			throws SaploConnectionException {

		if (isOpen()) {

			if (requestLimitReached) {
				pendingRequests.add(request);
			} else {

				SaploCallback<Object> callback = request.getCallback();

				if (hideRequestLimit) {
					callback = new RequestLimitWrapper(request, this);
				}

				jsonClient.post(apiResource + ";jsessionid=" + sessionId,
						request.getRequestData(), callback);
			}

			return this;
		} else {
			throw new SaploConnectionException("The session is closed.");
		}

	}

	public boolean isOpen() {
		return sessionId != null;
	}

	//
	// SETTINGS
	//

	public DefaultSaploConnection setHideRequestLimit(boolean hideRequestLimit) {
		this.hideRequestLimit = hideRequestLimit;
		return this;
	}

	public boolean isHidingRequestLimit() {
		return hideRequestLimit;
	}

	//
	// FRIENDS ONLY
	//

	public void handleRequestLimitReached() {
		if (requestLimitReached == false) {
			requestLimitReached = true;

			try {

				Thread.sleep(1000 * 60 * 60);
				requestLimitReached = false;

				// Perform all queued jobs
				for (SaploRequest req : pendingRequests) {
					call(req);
				}

				pendingRequests.clear();

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (SaploConnectionException e) {
				e.printStackTrace();
			}
		}
	}

	//
	// INTERNALS
	//

	@SuppressWarnings("unchecked")
	protected void initSession(String apiKey, String secretKey,
			SaploCallback<Object> callback) throws SaploConnectionException {
		JSONArray params = new JSONArray();
		params.add(apiKey);
		params.add(secretKey);

		JSONObject request = new JSONObject();
		request.put("method", "auth.createSession");
		request.put("params", params);
		jsonClient.post(apiResource, request, callback);
	}

}
