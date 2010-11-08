package com.voltvoodoo.saplo4j.http;


public interface SaploConnection {

	public SaploConnection open();

	public SaploConnection call(SaploRequest request);
	
	public SaploConnection setHideRequestLimit(boolean hideRequestLimit);

	public boolean isHidingRequestLimit();

	public void handleRequestLimitReached();

}
