package com.voltvoodoo.saplo4j;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.async.impl.AddDocumentCallback;
import com.voltvoodoo.saplo4j.async.impl.CloseSessionCallback;
import com.voltvoodoo.saplo4j.async.impl.CreateCorpusCallback;
import com.voltvoodoo.saplo4j.async.impl.CreateSessionCallback;
import com.voltvoodoo.saplo4j.async.impl.GetDocumentCallback;
import com.voltvoodoo.saplo4j.async.impl.GetSimilarDocumentsCallback;
import com.voltvoodoo.saplo4j.async.impl.UpdateDocumentCallback;
import com.voltvoodoo.saplo4j.exception.SaploConnectionException;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.http.JsonClient;
import com.voltvoodoo.saplo4j.model.Language;
import com.voltvoodoo.saplo4j.model.SaploCorpus;
import com.voltvoodoo.saplo4j.model.SaploDocument;
import com.voltvoodoo.saplo4j.model.SaploSimilarity;

/**
 * Java API for Saplo semantic analysis service.
 * 
 * XXX: This is under heavy refactoring and development.
 * 
 * Almost all of the methods within the API come in two versions, a synchronous one
 * and an asynchronous one. The asynchrounous API is, of course, a magnitude faster 
 * when it comes to handling many calls at the same time and is most often to be preferred.
 * 
 * The synchronous API is convenient when you handle few requests and don't want to deal
 * with concurrency problems and creating callbacks.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>, progre55, Fredrik Hörte <fredrik@saplo.com>, 
 *
 */
@SuppressWarnings("unchecked")
public class Saplo {
	
	public static String SAPLO_URL = "http://api.saplo.com/";
	public static String API_RESOURCE = "/rpc/json";
	
	protected static int MAX_WAIT_SECONDS = 180; // Maximum time to wait for a response.
	
	protected volatile String sessionId;
	protected JsonClient jsonClient;
	protected int requestCount = 0;
	
	/**
	 * Used to quickly generate JSONArray's used as parameter containers.
	 * @param params
	 * @return
	 */
	public static JSONArray params(Object ... params) {
		JSONArray out = new JSONArray();
		JSONObject current;
		
		for(Object param : params) {
			if(param instanceof ArrayList) { // Hack, Saplo API handles nested lists oddly, this abstracts that away.
				current = new JSONObject();
				current.put("javaClass","java.util.ArrayList");
				current.put("list", param);
				out.add(current);
			} else {
				out.add(param);
			}
		}
		
		return out;
	}
	
	//
	// CONSTRUCTORS
	//
	
	public Saplo(String apiKey, String secretKey) throws SaploException {
		try {
			jsonClient = new JsonClient(SAPLO_URL);
			
			// Init session
			CreateSessionCallback cb = new CreateSessionCallback();
			initSession(apiKey, secretKey, cb);
			
			cb.awaitResponse(MAX_WAIT_SECONDS * 1000);
			
			if(cb.exception != null) {
				throw cb.exception;
			} else {
				this.sessionId = cb.sessionId;
			}
			
		} catch(URISyntaxException e) {
			throw new SaploConnectionException("Unable to connect to Saplo API, url invalid. See nested exception.", e);
		}
	}
	
	//
	// CONNECTION MANAGEMENT
	//
	
	public void close() throws SaploException {
		CloseSessionCallback cb = new CloseSessionCallback();
		call("auth.killSession",null,cb);
		cb.awaitResponse(MAX_WAIT_SECONDS * 1000);
	}

	//
	// CORPUS MANAGEMENT API
	// Synchronous
	
	public SaploCorpus.Id createCorpus(String name, String description, Language lang) throws SaploException {
		
		CreateCorpusCallback cb = new CreateCorpusCallback();
		call("corpus.createCorpus", params(name, description, lang), cb);
		
		cb.awaitResponse(MAX_WAIT_SECONDS * 1000);
		
		if( cb.corpusId != null ) {
			return cb.corpusId;
		} else {
			throw cb.exception;
		}
	}
	
	public SaploCorpus getCorpus(SaploCorpus.Id id) throws SaploException {
		throw new UnsupportedOperationException("Not implemented yet.");
	}
	
	//
	// DOCUMENT MANAGEMENT API
	// Synchronous
	
	public SaploDocument.Id addDocument(SaploCorpus.Id corpusId, String headline, String body, Language lang) throws SaploException {
		
		AddDocumentCallback cb = new AddDocumentCallback();
		call("corpus.addArticle", params(corpusId, headline, "", body, "", "", "", lang), cb);
		cb.awaitResponse(MAX_WAIT_SECONDS * 1000);
		
		if( cb.documentId != null ) {
			return cb.documentId;
		} else {
			throw cb.exception;
		}
	}
	
	public boolean updateDocument(SaploCorpus.Id corpusId, SaploDocument.Id id, String headline, String body, Language lang) throws SaploException {
		UpdateDocumentCallback cb = new UpdateDocumentCallback();
		call("corpus.updateArticle", params(corpusId, id, headline, "", body, "", "", "", lang), cb);
		cb.awaitResponse(10000);
		
		if( cb.exception == null ) {
			return true;
		} else {
			throw cb.exception;
		}
	}
	
	public SaploDocument getDocument(SaploCorpus.Id corpusId, SaploDocument.Id id) throws SaploException {
		GetDocumentCallback cb = new GetDocumentCallback();
		call("corpus.getArticle", params(corpusId, id), cb);
		cb.awaitResponse(10000);
		
		if( cb.document != null ) {
			return cb.document;
		} else {
			throw cb.exception;
		}
	}
	
	//
	// DOCUMENT MANAGEMENT API
	// Async
	
	public void addDocument(SaploCorpus.Id corpusId, String headline, String body, Language lang, SaploCallback<SaploDocument.Id> callback ) throws SaploException {
		call("corpus.addArticle", params(corpusId, headline, "", body, "", "", "", lang), new AddDocumentCallback(callback));
	}
	
	public void updateDocument(SaploCorpus.Id corpusId, SaploDocument.Id id, String headline, String body, Language lang, SaploCallback<Boolean> callback) throws SaploException {
		call("corpus.updateArticle", params(corpusId, id, headline, "", body, "", "", "", lang), new UpdateDocumentCallback(callback));
	}
	
	public void getDocument(SaploCorpus.Id corpusId, SaploDocument.Id id, SaploCallback<SaploDocument> callback) throws SaploException {
		call("corpus.getArticle", params(corpusId, id), new GetDocumentCallback(callback));
	}
	
	//
	// MATCH API
	// Synchronous
	
	public ArrayList<SaploSimilarity> getSimilarDocuments(SaploCorpus.Id corpusId, SaploDocument.Id id) throws SaploException {
		List<SaploCorpus.Id> searchIn = new ArrayList<SaploCorpus.Id>();
		searchIn.add(corpusId);
		return getSimilarDocuments(corpusId, id, searchIn);
	}
	
	public ArrayList<SaploSimilarity> getSimilarDocuments(SaploCorpus.Id corpusId, SaploDocument.Id id, List<SaploCorpus.Id> searchIn) throws SaploException {
		GetSimilarDocumentsCallback cb = new GetSimilarDocumentsCallback(this, id, corpusId, searchIn);
		call("match.getSimilarArticles", params(corpusId, id, searchIn, MAX_WAIT_SECONDS), cb);
		
		while(cb.exception == null && cb.similarDocuments == null) {
			cb.awaitResponse(MAX_WAIT_SECONDS * 1000);
		}
		
		if( cb.similarDocuments != null ) {
			return cb.similarDocuments;
		} else {
			throw cb.exception;
		}
	}
	
	//
	// MATCH API
	// Async
	
	public void getSimilarDocuments(SaploCorpus.Id corpusId, SaploDocument.Id id, SaploCallback<ArrayList<SaploSimilarity>> callback ) throws SaploException {
		List<SaploCorpus.Id> searchIn = new ArrayList<SaploCorpus.Id>();
		searchIn.add(corpusId);
		
		getSimilarDocuments(corpusId, id, searchIn, callback);
	}
	
	public void getSimilarDocuments(SaploCorpus.Id corpusId, SaploDocument.Id id, List<SaploCorpus.Id> searchIn, SaploCallback<ArrayList<SaploSimilarity>> callback ) throws SaploException {
		call("match.getSimilarArticles", params(corpusId, id, searchIn, MAX_WAIT_SECONDS), new GetSimilarDocumentsCallback(this, id, corpusId, searchIn, callback));
	}
	
	//
	// TAG API
	// Not implemented yet :(
	
	//
	// OTHER
	//
	
	/**
	 * Low-level interface for communicating directly with SAPLO api. This can be used
	 * in case you want to pass optional parameters that are not included in the high-level
	 * methods.
	 * 
	 * @param method is the Saplo method name, for example "corpus.addArticle"
	 * @param params can be almost anything. This gets converted to JSON. Use the static method params ftw.
	 * @param callback is a SaploCallback that will get the response JSON object, or any exceptions.
	 */
	public void call(String method, Object params, SaploCallback<Object> callback ) throws SaploConnectionException {
		JSONObject request = new JSONObject();
		request.put("method", method);
		
		if( params != null) {
			request.put("params", params);
		} else {
			request.put("params", new JSONArray());
		}
		request.put("id", ++requestCount);
		jsonClient.post(API_RESOURCE + ";jsessionid=" + sessionId, request, callback);
	}
	
	//
	// INTERNALS
	//
	
	protected void initSession( String apiKey, String secretKey, SaploCallback<Object> callback ) throws SaploConnectionException {
		JSONArray params = new JSONArray();
		params.add(apiKey);
		params.add(secretKey);
		
		JSONObject request = new JSONObject();
		request.put("method", "auth.createSession");
		request.put("params", params);
		jsonClient.post(API_RESOURCE, request, callback);
	}

}