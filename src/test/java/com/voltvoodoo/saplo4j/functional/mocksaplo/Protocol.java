package com.voltvoodoo.saplo4j.functional.mocksaplo;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * From simpleframeworks test suites.
 */
public enum Protocol {
	HTTP("http", 9123), HTTPS("https", 9124);

	private final String scheme;
	private final int port;

	private Protocol(String scheme, int port) {
		this.scheme = scheme;
		this.port = port;
	}

	public String getScheme() {
		return scheme;
	}

	public SocketAddress getAddress() {
		return new InetSocketAddress(port);
	}

	public int getPort() {
		return port;
	}

	public String getTarget() {
		return scheme + "://localhost:" + port + "/";
	}
}