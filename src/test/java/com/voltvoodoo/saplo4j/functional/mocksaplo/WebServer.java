package com.voltvoodoo.saplo4j.functional.mocksaplo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.simple.JSONObject;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

/**
 * Based on simpleframeworks test suites.
 */
public class WebServer implements Container {

	private final Handler handler;
	private final Connection connection;
	private final Server server;
	private final AtomicInteger requests;
	private final AtomicInteger failures;
	private final Protocol protocol;

	/**
	 * Create a web server that always returns HTTP 200 and a given JSON
	 * response.
	 * 
	 * @param responseObject
	 * @throws Exception
	 */
	public WebServer(JSONObject responseObject) {
		this(200, responseObject.toJSONString(), "application/json");
	}

	/**
	 * Create a web server that always returns a given response code, a given
	 * response body and a given content type.
	 * 
	 * @param responseCode
	 * @param textResponse
	 * @throws Exception
	 */
	public WebServer(final int responseCode, final String textResponse,
			final String contentType) {
		this(new Handler() {

			public void handle(Request request, Response response)
					throws Exception {

				byte[] data = textResponse.getBytes("UTF-8");

				response.setCode(responseCode);
				response.setText("OK");
				response.set("Content-Type", contentType + "; charset=UTF-8");
				response.set("Server", "Apache/1.3.27 (Unix) mod_perl/1.27");
				response.set("Content-Length", data.length);

				OutputStream out = response.getOutputStream(256);
				out.write(data);
			}

		});
	}

	/**
	 * Create a web server that handles arbitrary requests and creates arbitrary
	 * responses for them.
	 * 
	 * @param handler
	 * @throws Exception
	 */
	public WebServer(Handler handler) {
		try {
			this.server = new ContainerServer(this, 10);
			this.connection = new SocketConnection(server);
			this.requests = new AtomicInteger();
			this.failures = new AtomicInteger();
			this.handler = handler;
			this.protocol = Protocol.HTTP;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public int getRequests() {
		return requests.get();
	}

	public int getFailures() {
		return failures.get();
	}

	public void start() {
		try {
			SocketAddress address = protocol.getAddress();

			if (protocol == Protocol.HTTP) {
				connection.connect(address);
			} else {
				throw new RuntimeException(
						"Https is not yet implemented in the testing framework.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handle(Request req, Response resp) {
		try {
			requests.getAndIncrement();
			handler.handle(req, resp);
		} catch (Exception e) {
			failures.getAndIncrement();
			e.printStackTrace();
		} finally {
			try {
				resp.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		try {
			connection.close();
			server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getUrl() {
		return protocol.getTarget();
	}

}
