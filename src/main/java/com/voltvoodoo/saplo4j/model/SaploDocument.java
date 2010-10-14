package com.voltvoodoo.saplo4j.model;

/**
 * A document retrieved through the Saplo API. This class is normally
 * created via {@link com.voltvoodoo.saplo4j.Saplo#addDocument(com.voltvoodoo.saplo4j.model.SaploCorpus.Id, String, String, Language)}
 * 
 * Instances of this class are immutable.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 *
 */
public class SaploDocument {
	
	/**
	 * Typesafe id.
	 */
	public static class Id extends AbstractId {
		private static final long serialVersionUID = 5929037545997882096L;
		public Id(Long id) { super(id); }
		public Id() { super(); }
	}
	
	protected Id id;
	protected SaploCorpus.Id corpusId;
	protected String headline;
	protected String meta;
	
	//
	// CONSTRUCTORS
	//
	
	public SaploDocument(Id id, SaploCorpus.Id corpusId, String headline, String meta) {
		this.id = id;
		this.corpusId = corpusId;
		this.headline = headline;
		this.meta = meta;
	}
	
	//
	// GETTERS
	//
	
	public Id getId() { return this.id; }
	public SaploCorpus.Id getCorpusId() { return this.corpusId; }
	public String getHeadline() { return this.headline; }
	public String getMeta() { return this.meta; }
	
}
