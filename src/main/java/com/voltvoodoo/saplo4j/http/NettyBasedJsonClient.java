package com.voltvoodoo.saplo4j.http;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpContentDecompressor;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpRequestEncoder;
import org.jboss.netty.handler.codec.http.HttpResponseDecoder;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.voltvoodoo.saplo4j.exception.SaploConnectionException;

/**
 * A Json RPC client for Saplo. Based on the netty example HttpClient by Andy
 * Taylor and Trustin Lee.
 * 
 * There is plenty of room for optimization here, for instance keeping the
 * connection open between requests.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 * 
 */
public class NettyBasedJsonClient implements JsonClient {
	
	private CookieEncoder cookies = new CookieEncoder(false);
	private ChannelFactory channelFactory;

	private String scheme;
	private String host;

	private int port;
	
	private Timer timer = new HashedWheelTimer();

	//
	// CONSTRUCTORS
	//

	public NettyBasedJsonClient(String url) throws SaploConnectionException,
			URISyntaxException {
		this(new URI(url));
	}

	public NettyBasedJsonClient(URI uri) throws SaploConnectionException {
		// BOOT
		channelFactory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		// INIT URI
		scheme = uri.getScheme() == null ? "http" : uri.getScheme();
		host = uri.getHost() == null ? "localhost" : uri.getHost();

		port = uri.getPort();
		if (port == -1 && scheme.equalsIgnoreCase("http")) {
			port = 80;
		}

		if (!scheme.equalsIgnoreCase("http")) {
			throw new SaploConnectionException(
					"Only http transport is supported, your url is set to use "
							+ scheme + ".");
		}

	}

	//
	// PUBLIC
	//

	public void get(String resource, JsonRequest saploRequest)
			throws SaploConnectionException {
		request(resource, saploRequest, HttpMethod.GET);
	}

	public void post(String resource, JsonRequest saploRequest) throws SaploConnectionException {
		request(resource, saploRequest, HttpMethod.POST);
	}

	public void put(String resource, JsonRequest saploRequest)
			throws SaploConnectionException {
		request(resource, saploRequest, HttpMethod.PUT);
	}

	public void delete(String resource, JsonRequest saploRequest)
			throws SaploConnectionException {
		request(resource, saploRequest, HttpMethod.DELETE);
	}

	//
	// INTERNALS
	//

	protected void request(final String resource, final JsonRequest saploRequest, final HttpMethod method)
			throws SaploConnectionException {

		// CONNECT
		final ResponseHandler responseHandler = new ResponseHandler(saploRequest);
		ClientBootstrap boot = createPipeline(responseHandler);
		ChannelFuture future = boot.connect(new InetSocketAddress(host, port));
		
		// Create a channel, wait until it is established
		future.addListener(new ChannelFutureListener() {

			public void operationComplete(ChannelFuture future)
					throws Exception {

				if (!future.isSuccess()) {
					throw new SaploConnectionException(
							"Connection failed, see nested exception.", future
									.getCause());
				}

				// Prepare the HTTP request
				HttpRequest httpRequest = new DefaultHttpRequest(
						HttpVersion.HTTP_1_1, method, resource);
				httpRequest.setHeader(HttpHeaders.Names.HOST, host);
				httpRequest.setHeader(HttpHeaders.Names.CONNECTION,
						HttpHeaders.Values.CLOSE);
				httpRequest.setHeader(HttpHeaders.Names.ACCEPT_ENCODING,
						HttpHeaders.Values.GZIP);

				if (method == HttpMethod.POST || method == HttpMethod.PUT) {
					if (saploRequest.getRequestData() != null) {
						
						byte[] binaryData = binaryJSON(saploRequest.getRequestData());
						ChannelBuffer buffer = ChannelBuffers
								.buffer(binaryData.length);
						buffer.writeBytes(binaryData);

						httpRequest.setHeader(HttpHeaders.Names.CONTENT_LENGTH,
								String.valueOf(binaryData.length));
						httpRequest.setContent(buffer);
						
					} else {
						httpRequest.setHeader(HttpHeaders.Names.CONTENT_LENGTH, "0");
						httpRequest.setContent(null);
					}
				}

				try {
					httpRequest.setHeader(HttpHeaders.Names.COOKIE,
							cookies.encode());
				} catch (IndexOutOfBoundsException e) {
					// Om-nom-nom
					// We end up here if there are no cookies to encode..
				}

				// Send request
				future.getChannel().write(httpRequest);
				responseHandler.setRequestSent();
			}

		});
	}

	protected ClientBootstrap createPipeline(ResponseHandler responseHandler) {
		ClientBootstrap bootstrap = new ClientBootstrap(channelFactory);
		
		bootstrap.getPipeline().addLast("timeout",    new IdleStateHandler(timer, 1, 0, 0));
		bootstrap.getPipeline().addLast("decoder",    new HttpResponseDecoder());
		bootstrap.getPipeline().addLast("inflater",   new HttpContentDecompressor());
		bootstrap.getPipeline().addLast("aggregator", new HttpChunkAggregator(1048576));
		bootstrap.getPipeline().addLast("encoder",    new HttpRequestEncoder());
		bootstrap.getPipeline().addLast("handler",    responseHandler);

		return bootstrap;
	}

	protected static byte[] binaryJSON(Object data) {
		byte[] binaryData;

		if (data instanceof JSONObject || data instanceof JSONArray) {
			binaryData = data.toString().getBytes();
		} else if ( data instanceof String) {
			binaryData = ((String)data).getBytes();
		} else {
			binaryData = JSONValue.toJSONString(data).getBytes();
		}

		return binaryData;
	}

}