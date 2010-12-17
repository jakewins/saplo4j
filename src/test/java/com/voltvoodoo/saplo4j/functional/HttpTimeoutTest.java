package com.voltvoodoo.saplo4j.functional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.json.simple.JSONArray;
import org.junit.Test;

import com.voltvoodoo.saplo4j.Saplo;
import com.voltvoodoo.saplo4j.functional.mocksaplo.SaploBaseHandler;
import com.voltvoodoo.saplo4j.functional.mocksaplo.WebServer;
import com.voltvoodoo.saplo4j.model.SaploCorpus;
import com.voltvoodoo.saplo4j.model.SaploDocument;

public class HttpTimeoutTest {

	protected int callCounter = 0;
	
	private WebServer reallySlowServer = new WebServer(new SaploBaseHandler() {
		public String handle(String method, JSONArray args, Long id) throws Exception {
			callCounter++;
			if( callCounter == 1) {
				Thread.sleep(100000);
			}
			
			return "{\"result\":[]}";
		}
	});
	
	@Test
	public void shouldTimeout() throws InterruptedException {
		callCounter = 0;
		reallySlowServer.start();
		
		Saplo saplo = new Saplo("","",reallySlowServer.getUrl(), "");
		saplo.setRetryInterval(1000);
		saplo.getSimilarDocuments(new SaploCorpus.Id(0l), new SaploDocument.Id(0l));
		
		// Sleep for a while, to ensure no other retries are sent.
		Thread.sleep(3000);
		
		assertThat(callCounter, is(2));
		
		reallySlowServer.stop();
	}
	
}
