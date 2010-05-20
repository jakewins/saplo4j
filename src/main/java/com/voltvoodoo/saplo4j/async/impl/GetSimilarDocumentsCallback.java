package com.voltvoodoo.saplo4j.async.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.voltvoodoo.saplo4j.Saplo;
import com.voltvoodoo.saplo4j.async.AbstractInternalCallback;
import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.model.SaploCorpus;
import com.voltvoodoo.saplo4j.model.SaploDocument;
import com.voltvoodoo.saplo4j.model.SaploSimilarity;

public class GetSimilarDocumentsCallback extends AbstractInternalCallback {
	
	public SaploException exception = null;
	public ArrayList<SaploSimilarity> similarDocuments = new ArrayList<SaploSimilarity>();
	
	protected SaploCallback<ArrayList<SaploSimilarity>> userCallback;
	protected SaploDocument.Id sourceDocumentId;
	protected SaploCorpus.Id sourceCorpusId;
	protected Saplo saplo;
	protected List<SaploCorpus.Id> searchIn;
	
	//
	// CONSTRUCTORS
	//
	
	public GetSimilarDocumentsCallback(Saplo saplo, SaploDocument.Id sourceDocumentId, SaploCorpus.Id sourceCorpusId, List<SaploCorpus.Id> searchIn) {
		this.saplo = saplo;
		this.sourceCorpusId = sourceCorpusId;
		this.sourceDocumentId = sourceDocumentId;
		this.searchIn = searchIn;
	}
	
	public GetSimilarDocumentsCallback(Saplo saplo, SaploDocument.Id sourceDocumentId, SaploCorpus.Id sourceCorpusId, List<SaploCorpus.Id> searchIn, SaploCallback<ArrayList<SaploSimilarity>> userCallback) {
		this.saplo = saplo;
		this.userCallback = userCallback;
		this.sourceCorpusId = sourceCorpusId;
		this.sourceDocumentId = sourceDocumentId;
		this.searchIn = searchIn;
		
		if(userCallback != null) {
			userCallback.onFailure(exception);
		}
	}
	
	//
	// PUBLIC
	//
	
	public void onFailedResponse(SaploException exception) {
		if ( exception.getErrorCode() == 1004 || exception.getErrorCode() == 1005 ) {
			// Saplo is still working, send request again.
			try {
				this.saplo.call("match.getSimilarArticles", Saplo.params(sourceCorpusId, sourceDocumentId, searchIn), this);
			} catch (SaploException e) {
				this.exception = e;
			}
		} else {
			this.exception = exception;
		}
	}

	public void onSuccessfulResponse(JSONObject result) {
		//[{"resultCorpusId":191,"resultPublishUrl":"","resultArticleId":3,"matchId":1,"resultHeadline":"Document 3","resultValue":0.6713}]
		
		JSONArray jsonDocuments = (JSONArray)result.get("result");
		JSONObject jsonDocument;
		
		for(Object document : jsonDocuments) {
			jsonDocument = (JSONObject) document;
			
			similarDocuments.add(new SaploSimilarity(sourceDocumentId,
	                                                 sourceCorpusId,
	                                                 new SaploDocument.Id( (Long)jsonDocument.get("resultArticleId") ),
	                                                 new SaploCorpus.Id(   (Long)jsonDocument.get("resultCorpusId") ),
	                                                 (Double)jsonDocument.get("resultValue")));
		}
		
		if(userCallback != null) {
			userCallback.onSuccess(this.similarDocuments);
		}
	}
}