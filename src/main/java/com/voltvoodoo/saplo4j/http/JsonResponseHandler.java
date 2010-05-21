package com.voltvoodoo.saplo4j.http;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.json.simple.JSONValue;

import com.voltvoodoo.saplo4j.async.SaploCallback;

@ChannelPipelineCoverage("all")
public class JsonResponseHandler extends SimpleChannelUpstreamHandler {
	
	protected SaploCallback<Object> callback;
	
	public JsonResponseHandler(SaploCallback<Object> callback) {
		this.callback = callback;
	}
	
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    
		HttpResponse response = (HttpResponse) e.getMessage();
		ChannelBuffer content = response.getContent();
		if (content.readable()) {
			Object result = JSONValue.parse(content.toString("UTF-8"));
			if( result == null ) {
				// XXX: Debugging
				System.out.println("FATAL: Unreadable response");
				System.out.println("In UTF-8:");
				System.out.println(content.toString("UTF-8"));
				System.out.println("Raw:");
				System.out.println(content.toString());
			}
			
			callback.onSuccess(result);
		}
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
	}
}