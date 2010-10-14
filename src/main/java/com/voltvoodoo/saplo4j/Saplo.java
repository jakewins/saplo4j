package com.voltvoodoo.saplo4j;

import static com.voltvoodoo.saplo4j.utils.JsonUtils.jsonParams;

import java.util.ArrayList;
import java.util.List;

import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.async.impl.AddDocumentCallback;
import com.voltvoodoo.saplo4j.async.impl.CreateCorpusCallback;
import com.voltvoodoo.saplo4j.async.impl.DeleteCorpusCallback;
import com.voltvoodoo.saplo4j.async.impl.DeleteDocumentCallback;
import com.voltvoodoo.saplo4j.async.impl.DeleteMatchCallback;
import com.voltvoodoo.saplo4j.async.impl.GetCorpusIdsCallback;
import com.voltvoodoo.saplo4j.async.impl.GetCorpusInfoCallback;
import com.voltvoodoo.saplo4j.async.impl.GetDocumentCallback;
import com.voltvoodoo.saplo4j.async.impl.GetEntityTagsCallback;
import com.voltvoodoo.saplo4j.async.impl.GetMatchCallback;
import com.voltvoodoo.saplo4j.async.impl.GetSimilarDocumentsCallback;
import com.voltvoodoo.saplo4j.async.impl.UpdateDocumentCallback;
import com.voltvoodoo.saplo4j.exception.SaploConnectionException;
import com.voltvoodoo.saplo4j.http.SaploConnection;
import com.voltvoodoo.saplo4j.http.SaploRequest;
import com.voltvoodoo.saplo4j.model.Language;
import com.voltvoodoo.saplo4j.model.SaploCorpus;
import com.voltvoodoo.saplo4j.model.SaploDocument;
import com.voltvoodoo.saplo4j.model.SaploSimilarity;
import com.voltvoodoo.saplo4j.model.SaploTag;

/**
 * Java API for Saplo semantic analysis service.
 * 
 * Almost all of the methods within the API come in two versions, a synchronous
 * one and an asynchronous one. The asynchronous API is, of course, a lot faster
 * when it comes to handling many calls at the same time and is most often to be
 * preferred.
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

	/**
	 * Create a new Saplo client. This instantly creates a new Saplo session.
	 * Don't forgot to call Saplo.close when you are done.
	 * 
	 * @param apiKey
	 *            Your Saplo API key
	 * @param secretKey
	 *            Your Saplo Secret API key
	 */
	public Saplo(String apiKey, String secretKey) {
		saploConnection = new SaploConnection(apiKey, secretKey, SAPLO_URL,
				API_RESOURCE);

		saploConnection.open();
	}

	//
	// CONNECTION MANAGEMENT
	//

	/**
	 * Call this to finish the Saplo session.
	 */
	public void close() {
		saploConnection.close();
	}

	/**
	 * Call this to open the Saplo session. A session is opened by default when
	 * you instantiate this class, so the only use for this is if you have
	 * closed the session and want to open a new one.
	 */
	public void open() {
		saploConnection.open();
	}

	//
	// CORPUS MANAGEMENT API
	//

	/**
	 * Create a new Saplo corpus. A corpus is a container where you put your
	 * documents. When you do similarity matching et cetera, you decide on a
	 * per-corpus level what documents should be included in the results.
	 * 
	 * @param name
	 *            A human-readable name of the corpus
	 * @param description
	 *            A human-readable description of the contents of the corpus.
	 * @param lang
	 *            The language of the documents that will be put in the corpus.
	 * 
	 * @return a globally unique id of the new corpus
	 */
	public SaploCorpus.Id createCorpus(String name, String description,
			Language lang) {

		CreateCorpusCallback cb = new CreateCorpusCallback();
		call("corpus.createCorpus", jsonParams(name, description, lang), cb);

		cb.awaitResponse(MAX_WAIT_SECONDS * 1000);

		if (cb.corpusId != null) {
			return cb.corpusId;
		} else {
			throw cb.exception;
		}
	}

	/**
	 * Remove a corpus
	 * 
	 * @param id
	 *            The id of the corpus to remove
	 * @return
	 */
	public boolean deleteCorpus(SaploCorpus.Id id) {

		DeleteCorpusCallback cb = new DeleteCorpusCallback();
		call("corpus.deleteCorpus", jsonParams(id), cb);

		cb.awaitResponse(MAX_WAIT_SECONDS * 1000);

		if (cb.exception != null) {
			throw cb.exception;
		} else {
			return cb.result;
		}
	}

	/**
	 * Get a list of all your corpuses.
	 * 
	 * @return A list of all your corpuses.
	 */
	public List<SaploCorpus> getAllCorpuses() {

		GetCorpusIdsCallback idCb = new GetCorpusIdsCallback();
		call("corpus.getPermissions", jsonParams(), idCb);

		idCb.awaitResponse(MAX_WAIT_SECONDS * 1000);

		if (idCb.getException() != null) {
			throw idCb.getException();
		}

		ArrayList<SaploCorpus> corpuses = new ArrayList<SaploCorpus>();
		for (SaploCorpus.Id id : idCb.getCorpusIds()) {
			corpuses.add(getCorpus(id));
		}

		return corpuses;
	}

	/**
	 * Get a single corpus given its id. This will give you access to
	 * information like name, description, language and next document id to be
	 * assigned.
	 * 
	 * @param id
	 *            Id of the corpus you want to retrieve.
	 * @return
	 */
	public SaploCorpus getCorpus(SaploCorpus.Id id) {
		GetCorpusInfoCallback cb = new GetCorpusInfoCallback(id);
		call("corpus.getInfo", jsonParams(id), cb);

		cb.awaitResponse(MAX_WAIT_SECONDS * 1000);

		if (cb.getException() != null) {
			throw cb.getException();
		} else {
			return cb.getCorpus();
		}
	}

	//
	// DOCUMENT MANAGEMENT API
	// Synchronous

	/**
	 * @see #addDocument(com.voltvoodoo.saplo4j.model.SaploCorpus.Id, String,
	 *      String, String, Language)
	 */
	public SaploDocument.Id addDocument(SaploCorpus.Id corpusId,
			String headline, String body, Language lang) {
		return addDocument(corpusId, headline, body, "", lang);
	}

	/**
	 * Add a new document (a text) to a given corpus.
	 * 
	 * @param corpusId
	 *            Is the corpus to add the document in
	 * @param headline
	 *            Is a headline for your document
	 * @param body
	 *            Is the main document text
	 * @param meta
	 *            Is an arbitrary String with a maximum size of 1000 chars. Use
	 *            this to store meta data about your document.
	 * @param lang
	 *            Is the language of the text in the document.
	 * @return a corpus-unique document id.
	 */
	public SaploDocument.Id addDocument(SaploCorpus.Id corpusId,
			String headline, String body, String meta, Language lang) {

		AddDocumentCallback cb = new AddDocumentCallback();
		addDocument(corpusId, headline, body, meta, lang, cb);
		cb.awaitResponse(MAX_WAIT_SECONDS * 1000);

		if (cb.documentId != null) {
			return cb.documentId;
		} else {
			throw cb.exception;
		}
	}

	/**
	 * @see #updateDocument(com.voltvoodoo.saplo4j.model.SaploCorpus.Id,
	 *      com.voltvoodoo.saplo4j.model.SaploDocument.Id, String, String,
	 *      String, Language)
	 */
	public boolean updateDocument(SaploCorpus.Id corpusId, SaploDocument.Id id,
			String headline, String body, Language lang) {
		return updateDocument(corpusId, id, headline, body, "", lang);
	}

	/**
	 * Edit the contents of an existing document.
	 * 
	 * @param corpusId
	 *            Is the corpus to add the document in
	 * @param headline
	 *            Is a headline for your document
	 * @param body
	 *            Is the main document text
	 * @param meta
	 *            Is an arbitrary String with a maximum size of 1000 chars. Use
	 *            this to store meta data about your document.
	 * @param lang
	 *            Is the language of the text in the document.
	 */
	public boolean updateDocument(SaploCorpus.Id corpusId, SaploDocument.Id id,
			String headline, String body, String meta, Language lang) {
		UpdateDocumentCallback cb = new UpdateDocumentCallback();
		updateDocument(corpusId, id, headline, body, meta, lang, cb);
		cb.awaitResponse(10000);

		if (cb.exception == null) {
			return true;
		} else {
			throw cb.exception;
		}
	}

	public SaploDocument getDocument(SaploCorpus.Id corpusId,
			SaploDocument.Id id) {
		GetDocumentCallback cb = new GetDocumentCallback();
		getDocument(corpusId, id, cb);
		cb.awaitResponse(10000);

		if (cb.document != null) {
			return cb.document;
		} else {
			throw cb.exception;
		}
	}

	public Boolean deleteDocument(SaploCorpus.Id corpusId, SaploDocument.Id id) {
		DeleteDocumentCallback cb = new DeleteDocumentCallback();

		deleteDocument(corpusId, id, cb);
		cb.awaitResponse(MAX_WAIT_SECONDS * 1000);

		if (cb.getException() == null) {
			return true;
		} else {
			throw cb.getException();
		}
	}

	//
	// DOCUMENT MANAGEMENT API
	// Async

	/**
	 * @see #addDocument(com.voltvoodoo.saplo4j.model.SaploCorpus.Id, String,
	 *      String, String, Language, SaploCallback)
	 */
	public void addDocument(SaploCorpus.Id corpusId, String headline,
			String body, Language lang, SaploCallback<SaploDocument.Id> callback) {
		addDocument(corpusId, headline, body, "", lang, callback);
	}

	/**
	 * Add a new document (a text) to a given corpus.
	 * 
	 * @param corpusId
	 *            Is the corpus to add the document in
	 * @param headline
	 *            Is a headline for your document
	 * @param body
	 *            Is the main document text
	 * @param meta
	 *            Is an arbitrary String with a maximum size of 1000 chars. Use
	 *            this to store meta data about your document.
	 * @param lang
	 *            Is the language of the text in the document.
	 * @param callback
	 *            Will be called with the document id when the operation has
	 *            completed.
	 */
	public void addDocument(SaploCorpus.Id corpusId, String headline,
			String body, String meta, Language lang,
			SaploCallback<SaploDocument.Id> callback) {
		addDocument(corpusId, headline, body, meta, lang, new AddDocumentCallback( callback ));
	}

	/**
	 * @see #updateDocument(com.voltvoodoo.saplo4j.model.SaploCorpus.Id,
	 *      com.voltvoodoo.saplo4j.model.SaploDocument.Id, String, String,
	 *      String, Language, SaploCallback)
	 */
	public void updateDocument(SaploCorpus.Id corpusId, SaploDocument.Id id,
			String headline, String body, Language lang,
			SaploCallback<Boolean> callback) {
		updateDocument(corpusId, id, headline, body, "", lang, callback);
	}

	/**
	 * Edit the contents of an existing document.
	 * 
	 * @param corpusId
	 *            Is the corpus to add the document in
	 * @param headline
	 *            Is a headline for your document
	 * @param body
	 *            Is the main document text
	 * @param meta
	 *            Is an arbitrary String with a maximum size of 1000 chars. Use
	 *            this to store meta data about your document.
	 * @param lang
	 *            Is the language of the text in the document.
	 * @param callback
	 *            Will be called when the edit is complete.
	 */
	public void updateDocument(SaploCorpus.Id corpusId, SaploDocument.Id id,
			String headline, String body, String meta, Language lang,
			SaploCallback<Boolean> callback) {
		updateDocument(corpusId, id, headline, body, meta, lang,
				new UpdateDocumentCallback(callback));
	}

	public void getDocument(SaploCorpus.Id corpusId, SaploDocument.Id id,
			SaploCallback<SaploDocument> callback) {
		getDocument(corpusId, id, new GetDocumentCallback(callback));
	}

	public void deleteDocument(SaploCorpus.Id corpusId, SaploDocument.Id id,
			SaploCallback<Boolean> callback) {
		deleteDocument(corpusId, id, new DeleteDocumentCallback(callback));
	}

	//
	// MATCH API
	// Synchronous

	public ArrayList<SaploSimilarity> getSimilarDocuments(
			SaploCorpus.Id corpusId, SaploDocument.Id id) {

		GetSimilarDocumentsCallback cb = new GetSimilarDocumentsCallback(id,
				corpusId);

		getSimilarDocuments(corpusId, id, cb);

		while (cb.exception == null && cb.similarDocuments == null) {
			cb.awaitResponse(MAX_WAIT_SECONDS * 1000);
		}

		if (cb.similarDocuments != null) {
			return cb.similarDocuments;
		} else {
			throw cb.exception;
		}
	}

	public SaploSimilarity getMatch(SaploCorpus.Id corpusId,
			SaploSimilarity.Id id, SaploDocument.Id documentId) {

		GetMatchCallback cb = new GetMatchCallback(corpusId, id, documentId);
		getMatch(corpusId, id, documentId, cb);

		while (cb.exception == null && cb.similarity == null) {
			cb.awaitResponse(MAX_WAIT_SECONDS * 1000);
		}

		if (cb.similarity != null) {
			return cb.similarity;
		} else {
			throw cb.exception;
		}
	}

	public boolean deleteMatch(SaploCorpus.Id corpusId, SaploSimilarity.Id id,
			SaploDocument.Id documentId) {

		DeleteMatchCallback cb = new DeleteMatchCallback();

		deleteMatch(corpusId, id, documentId, cb);

		while (cb.exception == null && cb.result == null) {
			cb.awaitResponse(MAX_WAIT_SECONDS * 1000);
		}

		if (cb.result != null) {
			return cb.result;
		} else {
			throw cb.exception;
		}

	}

	//
	// MATCH API
	// Async

	public void getSimilarDocuments(SaploCorpus.Id corpusId,
			SaploDocument.Id id,
			SaploCallback<ArrayList<SaploSimilarity>> callback) {

		getSimilarDocuments(corpusId, id, new GetSimilarDocumentsCallback(id,
				corpusId, callback));
	}

	public void getMatch(SaploCorpus.Id corpusId, SaploSimilarity.Id id,
			SaploDocument.Id documentId, SaploCallback<SaploSimilarity> callback) {

		getMatch(corpusId, id, documentId, new GetMatchCallback(corpusId, id,
				documentId, callback));
	}

	public void deleteMatch(SaploCorpus.Id corpusId, SaploSimilarity.Id id,
			SaploDocument.Id documentId, SaploCallback<Boolean> callback) {

		deleteMatch(corpusId, id, documentId, new DeleteMatchCallback(callback));
	}

	//
	// TAG API
	// Synchronous

	public List<SaploTag> getEntityTags(SaploCorpus.Id corpusId,
			SaploDocument.Id documentId) {

		GetEntityTagsCallback cb = new GetEntityTagsCallback(corpusId,
				documentId);

		getEntityTags(corpusId, documentId, cb);

		while (cb.exception == null && cb.result == null) {
			cb.awaitResponse(MAX_WAIT_SECONDS * 1000);
		}

		if (cb.result != null) {
			return cb.result;
		} else {
			throw cb.exception;
		}

	}

	//
	// TAG API
	// Async

	public void getEntityTags(SaploCorpus.Id corpusId,
			SaploDocument.Id documentId, SaploCallback<List<SaploTag>> callback) {

		getEntityTags(corpusId, documentId, new GetEntityTagsCallback(corpusId,
				documentId, callback));

	}

	//
	// OTHER
	//

	/**
	 * Low-level interface for communicating directly with Saplo API. This can
	 * be used in case you want to pass optional parameters that are not
	 * included in the high-level methods. It can also be used to call methods
	 * that are not yet implemented.
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

		call(new SaploRequest(method, params, callback, saploConnection));

	}

	/**
	 * Perform a low-level call to Saplo using a SaploRequest object.
	 * 
	 * This is convenient if you want to redo a request that has failed. Use the
	 * getRequest() method that all SaploException objects have, and simply call
	 * this method with the request. All your callbacks etc will be used as they
	 * were in the first request.
	 * 
	 * @param request
	 * @throws SaploConnectionException
	 */
	public void call(SaploRequest request) throws SaploConnectionException {
		saploConnection.call(request);
	}

	//
	// SETTINGS
	//

	/**
	 * This is used to "hide" API request limit errors. In essence, set this to
	 * true and you will not get those errors. The Saplo4j client will
	 * automatically queue your calls and throttle them so that they do not
	 * overcome the per-hour API limit.
	 * 
	 * WARNING: If you are constantly doing more calls than allowed per hour,
	 * turning this on will lead to memory problems as the queue of calls to
	 * make grows larger.
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

	//
	// DOCUMENT MANAGEMENT API
	// Underlying implementation

	private void addDocument(SaploCorpus.Id corpusId, String headline,
			String body, String meta, Language lang,
			AddDocumentCallback callback) {
		call("corpus.addArticle",
				jsonParams(corpusId, headline, "", body, "", "", "", lang),
				callback);
	}

	private void updateDocument(SaploCorpus.Id corpusId, SaploDocument.Id id,
			String headline, String body, String meta, Language lang,
			UpdateDocumentCallback callback) {
		call("corpus.updateArticle",
				jsonParams(corpusId, id, headline, "", body, "", meta, "", lang),
				callback);
	}

	private void deleteDocument(SaploCorpus.Id corpusId, SaploDocument.Id id,
			DeleteDocumentCallback callback) {
		call("corpus.deleteArticle", jsonParams(corpusId, id), callback);
	}

	private void getDocument(SaploCorpus.Id corpusId, SaploDocument.Id id,
			GetDocumentCallback callback) {
		call("corpus.getArticle", jsonParams(corpusId, id), callback);
	}

	//
	// MATCH API
	// Underlying implementation

	private void getSimilarDocuments(SaploCorpus.Id corpusId,
			SaploDocument.Id id, GetSimilarDocumentsCallback callback) {

		call("match.getSimilarArticles",
				jsonParams(corpusId, id, MAX_WAIT_SECONDS, 50, 0.1, 1.0),
				callback);
	}

	private void getMatch(SaploCorpus.Id corpusId, SaploSimilarity.Id id,
			SaploDocument.Id documentId, GetMatchCallback callback) {

		call("match.getMatch", jsonParams(corpusId, documentId, id), callback);
	}

	private void deleteMatch(SaploCorpus.Id corpusId, SaploSimilarity.Id id,
			SaploDocument.Id documentId, DeleteMatchCallback callback) {

		call("match.getMatch", jsonParams(corpusId, documentId, id), callback);
	}

	//
	// TAG API
	// Underlying implementation

	private void getEntityTags(SaploCorpus.Id corpusId,
			SaploDocument.Id documentId, GetEntityTagsCallback callback) {
		call("tags.getEntityTags",
				jsonParams(corpusId, documentId, MAX_WAIT_SECONDS), callback);
	}

}