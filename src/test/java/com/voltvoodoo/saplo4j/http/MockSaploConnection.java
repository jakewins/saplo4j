package com.voltvoodoo.saplo4j.http;

public class MockSaploConnection implements SaploConnection {

	public SaploConnection open() {
		return this;
	}

	public SaploConnection close() {
		return this;
	}

	public SaploConnection call(SaploRequest request) {
		request.getCallback().onSuccess(true);
		return this;
	}

	public boolean isOpen() {
		return true;
	}

	public SaploConnection setHideRequestLimit(boolean hideRequestLimit) {
		return this;
	}

	public boolean isHidingRequestLimit() {
		return false;
	}

	public void handleRequestLimitReached() {
		
	}

}
