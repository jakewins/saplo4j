package com.voltvoodoo.saplo4j.model;

/**
 * Data object describing the similarity between two documents. 
 * 
 * This class cannot be instantiated, but must be created via {@link com.voltvoodoo.saplo4j.Saplo}.
 * Instances of this class are immutable.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 *
 */
public class SaploSimilarity {
	
	protected SaploDocument.Id sourceDocumentId;
	protected SaploDocument.Id matchedDocumentId;
	protected SaploCorpus.Id sourceCorpusId;
	protected SaploCorpus.Id matchedCorpusId;
	protected Double similarity;
	
	//
	// CONSTRUCTORS
	//
	
	public SaploSimilarity( SaploDocument.Id sourceDocumentId,
			                SaploCorpus.Id sourceCorpusId,
			                SaploDocument.Id matchedDocumentId,
			                SaploCorpus.Id matchedCorpusId,
			                Double similarity) {
		this.sourceDocumentId  = sourceDocumentId;
		this.sourceCorpusId    = sourceCorpusId;
		this.matchedDocumentId = matchedDocumentId;
		this.matchedCorpusId   = matchedCorpusId;
		this.similarity        = similarity;
	}
	
	//
	// GETTERS
	//
	
	public SaploDocument.Id getSourceDocumentId()  { return this.sourceDocumentId; }
	public SaploDocument.Id getMatchedDocumentId() { return this.matchedDocumentId; }
	public SaploCorpus.Id getSourceCorpusId()      { return this.sourceCorpusId; }
	public SaploCorpus.Id getMatchedCorpusId()     { return this.matchedCorpusId; }
	
	public double getSimilarity() { return this.similarity; }
	
}
