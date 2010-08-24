package com.voltvoodoo.saplo4j;

import static com.voltvoodoo.saplo4j.utils.JsonUtils.jsonParams;

import java.util.ArrayList;
import java.util.List;

import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.async.impl.AddDocumentCallback;
import com.voltvoodoo.saplo4j.async.impl.CreateCorpusCallback;
import com.voltvoodoo.saplo4j.async.impl.GetDocumentCallback;
import com.voltvoodoo.saplo4j.async.impl.GetSimilarDocumentsCallback;
import com.voltvoodoo.saplo4j.async.impl.UpdateDocumentCallback;
import com.voltvoodoo.saplo4j.exception.SaploConnectionException;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.http.SaploConnection;
import com.voltvoodoo.saplo4j.http.SaploRequest;
import com.voltvoodoo.saplo4j.model.Language;
import com.voltvoodoo.saplo4j.model.SaploCorpus;
import com.voltvoodoo.saplo4j.model.SaploDocument;
import com.voltvoodoo.saplo4j.model.SaploSimilarity;

/**
 * Java API for Saplo semantic analysis service.
 * 
 * Almost all of the methods within the API come in two versions, a synchronous
 * one and an asynchronous one. The asynchrounous API is, of course, a magnitude
 * faster when it comes to handling many calls at the same time and is most
 * often to be preferred.
 * 
 * The synchronous API is convenient when you handle few requests and don't want
 * to deal with concurrency problems and creating callbacks.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>, progre55, Fredrik Hörte
 *         <fredrik@saplo.com>,
 * 
 */
public class Saplo {

	private static int MAX_WAIT_SECONDS = 180;

	public static String SAPLO_URL = "http://api.saplo.com/";
	public static String API_RESOURCE = "/rpc/json";

	private SaploConnection saploConnection;

	//
	// CONSTRUCTORS
	//

	public Saplo(String apiKey, String secretKey) throws SaploException {
		saploConnection = new SaploConnection(apiKey, secretKey, SAPLO_URL,
				API_RESOURCE);

		saploConnection.open();
	}

	//
	// CONNECTION MANAGEMENT
	//

	public void close() throws SaploException {
		saploConnection.close();
	}

	//
	// CORPUS MANAGEMENT API
	// Synchronous

	public SaploCorpus.Id createCorpus(String name, String description,
			Language lang) throws SaploException {

		CreateCorpusCallback cb = new CreateCorpusCallback();
		call("corpus.createCorpus", jsonParams(name, description, lang), cb);

		cb.awaitResponse(MAX_WAIT_SECONDS * 1000);

		if (cb.corpusId != null) {
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

	public SaploDocument.Id addDocument(SaploCorpus.Id corpusId,
			String headline, String body, Language lang) throws SaploException {

		AddDocumentCallback cb = new AddDocumentCallback();
		call("corpus.addArticle",
				jsonParams(corpusId, headline, "", body, "", "", "", lang), cb);
		cb.awaitResponse(MAX_WAIT_SECONDS * 1000);

		if (cb.documentId != null) {
			return cb.documentId;
		} else {
			throw cb.exception;
		}
	}

	public boolean updateDocument(SaploCorpus.Id corpusId, SaploDocument.Id id,
			String headline, String body, Language lang) throws SaploException {
		UpdateDocumentCallback cb = new UpdateDocumentCallback();
		call("corpus.updateArticle",
				jsonParams(corpusId, id, headline, "", body, "", "", "", lang),
				cb);
		cb.awaitResponse(10000);

		if (cb.exception == null) {
			return true;
		} else {
			throw cb.exception;
		}
	}

	public SaploDocument getDocument(SaploCorpus.Id corpusId,
			SaploDocument.Id id) throws SaploException {
		GetDocumentCallback cb = new GetDocumentCallback();
		call("corpus.getArticle", jsonParams(corpusId, id), cb);
		cb.awaitResponse(10000);

		if (cb.document != null) {
			return cb.document;
		} else {
			throw cb.exception;
		}
	}

	//
	// DOCUMENT MANAGEMENT API
	// Async

	public void addDocument(SaploCorpus.Id corpusId, String headline,
			String body, Language lang, SaploCallback<SaploDocument.Id> callback)
			throws SaploException {
		call("corpus.addArticle",
				jsonParams(corpusId, headline, "", body, "", "", "", lang),
				new AddDocumentCallback(callback));
	}

	public void updateDocument(SaploCorpus.Id corpusId, SaploDocument.Id id,
			String headline, String body, Language lang,
			SaploCallback<Boolean> callback) throws SaploException {
		call("corpus.updateArticle",
				jsonParams(corpusId, id, headline, "", body, "", "", "", lang),
				new UpdateDocumentCallback(callback));
	}

	public void getDocument(SaploCorpus.Id corpusId, SaploDocument.Id id,
			SaploCallback<SaploDocument> callback) throws SaploException {
		call("corpus.getArticle", jsonParams(corpusId, id),
				new GetDocumentCallback(callback));
	}

	//
	// MATCH API
	// Synchronous

	public ArrayList<SaploSimilarity> getSimilarDocuments(
			SaploCorpus.Id corpusId, SaploDocument.Id id) throws SaploException {
		List<SaploCorpus.Id> searchIn = new ArrayList<SaploCorpus.Id>();
		searchIn.add(corpusId);
		return getSimilarDocuments(corpusId, id, searchIn);
	}

	public ArrayList<SaploSimilarity> getSimilarDocuments(
			SaploCorpus.Id corpusId, SaploDocument.Id id,
			List<SaploCorpus.Id> searchIn) throws SaploException {

		GetSimilarDocumentsCallback cb = new GetSimilarDocumentsCallback(this,
				id, corpusId, searchIn);
		call("match.getSimilarArticles",
				jsonParams(corpusId, id, searchIn, MAX_WAIT_SECONDS), cb);

		while (cb.exception == null && cb.similarDocuments == null) {
			cb.awaitResponse(MAX_WAIT_SECONDS * 1000);
		}

		if (cb.similarDocuments != null) {
			return cb.similarDocuments;
		} else {
			throw cb.exception;
		}
	}

	//
	// MATCH API
	// Async

	public void getSimilarDocuments(SaploCorpus.Id corpusId,
			SaploDocument.Id id,
			SaploCallback<ArrayList<SaploSimilarity>> callback)
			throws SaploException {
		List<SaploCorpus.Id> searchIn = new ArrayList<SaploCorpus.Id>();
		searchIn.add(corpusId);

		getSimilarDocuments(corpusId, id, searchIn, callback);
	}

	public void getSimilarDocuments(SaploCorpus.Id corpusId,
			SaploDocument.Id id, List<SaploCorpus.Id> searchIn,
			SaploCallback<ArrayList<SaploSimilarity>> callback)
			throws SaploException {
		call("match.getSimilarArticles",
				jsonParams(corpusId, id, searchIn, MAX_WAIT_SECONDS),
				new GetSimilarDocumentsCallback(this, id, corpusId, searchIn,
						callback));
	}

	//
	// TAG API
	// Not implemented yet :(

	//
	// OTHER
	//

	/**
	 * Low-level interface for communicating directly with SAPLO api. This can
	 * be used in case you want to pass optional parameters that are not
	 * included in the high-level methods.
	 * 
	 * @param method
	 *            is the Saplo method name, for example "corpus.addArticle"
	 * @param params
	 *            can be almost anything. This gets converted to JSON. Use the
	 *            static method params ftw.
	 * @param callback
	 *            is a SaploCallback that will get the response JSON object, or
	 *            any exceptions.
	 */
	public void call(String method, Object params,
			SaploCallback<Object> callback) throws SaploConnectionException {

		saploConnection.call(new SaploRequest(method, params, callback));

	}

	//
	// SETTINGS
	//

	/**
	 * This is used to "hide" API request limit reached errors. In essesence,
	 * set this to true and you will not get those error. When a request limit
	 * error is encountered, the request will be retried after an hour.
	 * 
	 * Turning this on will, if you are doing large amounts of requests, lead to
	 * some requests taking up to an hour to complete.
	 * 
	 * Default value for this is false.
	 */
	public Saplo setHideRequestLimit(boolean hideRequestLimit) {
		saploConnection.setHideRequestLimit(hideRequestLimit);
		return this;
	}

	public boolean isHidingRequestLimit() {
		return saploConnection.isHidingRequestLimit();
	}

}