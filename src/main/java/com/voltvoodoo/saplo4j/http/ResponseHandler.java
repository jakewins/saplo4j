package com.voltvoodoo.saplo4j.http;


import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;
import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.exception.SaploConnectionException;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.exception.SaploInternalException;
import com.voltvoodoo.saplo4j.exception.SaploStillProcessingException;
import com.voltvoodoo.saplo4j.exception.SaploUnknownException;
import com.voltvoodoo.saplo4j.utils.SaploResponseParser;

/**
 * Asynchronous response handler. Each request gets it's own instance of this class,
 * in order to connect the correct request callback with the correct response.
 * 
 * This recieves an HTTP response or error. It assumes the response contains JSON data,
 * attempts to decode it as such and send the response onwards.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 *
 */
public class ResponseHandler extends IdleStateAwareChannelHandler {

	private static final int MAX_RETRIES = 5;
	
	private JsonCallback callback;
	private JsonRequest request;
	private SaploResponseParser responseParser;
	
	private boolean requestSent = false;
	private int retries = 0;
	private Logger log = Logger.getLogger(getClass());
	
	public ResponseHandler(JsonRequest req) {
		this(req, new SaploResponseParser());
	}
	
	public ResponseHandler(JsonRequest req, SaploResponseParser responseParser) {
		this.callback = req.getCallback();
		this.request = req;
		this.responseParser = responseParser;
	}
	
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {

		HttpResponse response = (HttpResponse) e.getMessage();
		ChannelBuffer content = response.getContent();
		
		if (content.readable()) {
			
			try {

				JSONObject responseData = responseParser.parseSaploResponse(content.toString("UTF-8"), request);
				close(ctx);
				callback.onSuccess(responseData);
			
			} catch (SaploStillProcessingException ex) {
				
				close(ctx);
				request.send();
			
			} catch (SaploInternalException ex) {
				
				close(ctx);
				attemptToSalvageFailedRequest(ex);
			
			} catch (SaploUnknownException ex ) {
				
				close(ctx);
				attemptToSalvageFailedRequest(ex);
				
 			} catch (SaploException ex) {
				
 				close(ctx);
				callback.onFailure(ex);
				
			} catch (Exception ex) {
				
				close(ctx);
				attemptToSalvageFailedRequest(new SaploUnknownException(-1, "Unknown error while parsing response, see nested.", request, ex));
				
			}
			
		} else {
			
			close(ctx);
			attemptToSalvageFailedRequest(new SaploConnectionException("Got unreadable HTTP response body.", request, null));
			
		}
	}

	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		close(ctx);
		attemptToSalvageFailedRequest(new SaploConnectionException(
				"Low-level network exception, see nested.", request, e.getCause()));
		
	}
	
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception {
		if( requestSent && request.getRetryInterval() != 0 && e.getLastActivityTimeMillis() > request.getRetryInterval() ) {
        	log.info("Request taking too long to complete. Closing connection and retrying request.");
        	close(ctx);
        	request.send();
        }
    }
	
	public void setRequestSent() {
		requestSent = true;
	}
	
	private void attemptToSalvageFailedRequest(SaploException e) {
		if(shouldRetryFailedRequest()) {
			
			log.info("Saplo request failed, retrying it for the " + retries + " time.");
			
			try {
				Thread.sleep(500);
				request.send();
			} catch(InterruptedException intEx) {
				callback.onFailure(e);
			}
			
		} else {
			callback.onFailure(e);
		}
	}
	
	private synchronized boolean shouldRetryFailedRequest() {
		return (++retries) <= MAX_RETRIES; 
	}
	
	private void close(ChannelHandlerContext ctx) {
		if(ctx.getChannel() != null && ctx.getChannel().isConnected()) {
			ctx.getChannel().close();
		}
	}
}