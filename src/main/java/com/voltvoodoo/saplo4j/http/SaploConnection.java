package com.voltvoodoo.saplo4j.http;


public interface SaploConnection {

	public SaploConnection open();

	public SaploConnection close();

	public SaploConnection call(SaploRequest request);

	public boolean isOpen();
	
	public SaploConnection setHideRequestLimit(boolean hideRequestLimit);

	public boolean isHidingRequestLimit();

	public void handleRequestLimitReached();

}
