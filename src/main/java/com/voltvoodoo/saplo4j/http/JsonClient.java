package com.voltvoodoo.saplo4j.http;

import com.voltvoodoo.saplo4j.exception.SaploConnectionException;

public interface JsonClient {
	
	public void get(String resource, JsonRequest saploRequest);
	
	public void post(String resource, JsonRequest saploRequest) throws SaploConnectionException;
	
	public void put(String resource, JsonRequest saploRequest);
	
	public void delete(String resource, JsonRequest saploRequest);
	
}
