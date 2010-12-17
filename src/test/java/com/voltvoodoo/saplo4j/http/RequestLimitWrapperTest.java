package com.voltvoodoo.saplo4j.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.voltvoodoo.saplo4j.SaploConnection;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.exception.SaploGeneralException;
import com.voltvoodoo.saplo4j.exception.SaploRequestLimitReachedException;

public class RequestLimitWrapperTest {

	@Test
	public void shouldHandleHourlyRequestLimit() {
		
		SaploConnection conn = mock(SaploConnection.class);
		JsonRequest req = mock(JsonRequest.class);
		
		RequestLimitWrapper reqWrapper = new RequestLimitWrapper(req, conn);
		
		reqWrapper.onFailure(new SaploRequestLimitReachedException(1008, ""));
		
		verify(conn).handleRequestLimitReached();
		
	}
	
	@Test
	public void nonRequestLimitExceptionsShouldFallThrough() {
		
		SaploConnection conn = mock(SaploConnection.class);
		JsonRequest req = mock(JsonRequest.class);
		JsonCallback cb = mock(JsonCallback.class);
		
		SaploException ex = new SaploGeneralException(-1, "");
		
		when(req.getCallback()).thenReturn(cb);
		
		RequestLimitWrapper reqWrapper = new RequestLimitWrapper(req, conn);
		reqWrapper.onFailure(ex);
		
		verify(conn, never()).handleRequestLimitReached();
		verify(cb).onFailure(ex);
		
	}
}
