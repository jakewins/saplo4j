package com.voltvoodoo.saplo4j.http;

import static org.junit.Assert.assertEquals;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ExceptionEvent;
import org.junit.Test;

public class ResponseHandlerTest {

	private class FailingRequest implements JsonRequest {

		public int sendCounter = 0;
		
		private JsonCallback cb;
		private ResponseHandler responseHandler;
		
		public FailingRequest(JsonCallback callback) {
			this.cb = callback;
		}
		
		public void setResponseHandler(ResponseHandler responseHandler) {
			this.responseHandler = responseHandler;
		}
		
		public Object getRequestData() {
			return null;
		}

		public JsonCallback getCallback() {
			return cb;
		}

		
		public void setCallback(JsonCallback callback) {
			this.cb = callback;
		}

		public void send() {
			sendCounter++;
			responseHandler.exceptionCaught(new MockChannelHandlerContext(), new ExceptionEvent() {
				public Channel getChannel() {
					return null;
				}

				public ChannelFuture getFuture() {
					return null;
				}

				public Throwable getCause() {
					return new RuntimeException("Some random problem.");
				}
			});
		}

		public long getRetryInterval() {
			return 1000 * 70;
		}
	}
	
	@Test
	public void networkExceptionsShouldLeadToRetry() {
		
		CountingCallback callback = new CountingCallback();
		
		FailingRequest failingRequest = new FailingRequest(callback);
		ResponseHandler responseHandler = new ResponseHandler(failingRequest);
		
		failingRequest.setResponseHandler(responseHandler);
		failingRequest.send();
		
		assertEquals("A failing request should be retried five times, leading to a total of 6 send attempts.", 6,failingRequest.sendCounter);
		assertEquals("OnSuccess should not have been called.", 0, callback.successCalls);
		assertEquals("OnFailure should only have been called once.", 1, callback.failureCalls);
	}
	
}
