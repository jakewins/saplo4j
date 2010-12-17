package com.voltvoodoo.saplo4j.functional.mocksaplo;

import java.io.OutputStream;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

public abstract class SimpleHandler implements Handler {

	public void handle(Request request, Response response) throws Exception {
		String body = handle(request);
		
		byte[] data = body.getBytes("UTF-8");

		response.setCode(200);
		response.setText("OK");
		response.set("Content-Type", "application/json; charset=UTF-8");
		response.set("Server", "Apache/1.3.27 (Unix) mod_perl/1.27");
		response.set("Content-Length", data.length);

		OutputStream out = response.getOutputStream(256);
		out.write(data);
	}
	
	public abstract String handle(Request req) throws Exception;

}
