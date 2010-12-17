package com.voltvoodoo.saplo4j.http;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;

public class MockChannelHandlerContext implements ChannelHandlerContext {

	public Channel getChannel() {
		// TODO Auto-generated method stub
		return null;
	}

	public ChannelPipeline getPipeline() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public ChannelHandler getHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean canHandleUpstream() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canHandleDownstream() {
		// TODO Auto-generated method stub
		return false;
	}

	public void sendUpstream(ChannelEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void sendDownstream(ChannelEvent e) {
		// TODO Auto-generated method stub
		
	}

	public Object getAttachment() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAttachment(Object attachment) {
		// TODO Auto-generated method stub
		
	}

}
