package com.voltvoodoo.saplo4j.model;

/**
 * A document retrieved through the Saplo API. This class cannot
 * be instantiated, but must be created via {@link com.voltvoodoo.saplo4j.Saplo}
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
	protected String url;
	
	//
	// CONSTRUCTORS
	//
	
	public SaploDocument(Id id, SaploCorpus.Id corpusId, String headline, String url) {
		this.id = id;
		this.corpusId = corpusId;
		this.headline = headline;
		this.url = url;
	}
	
	//
	// GETTERS
	//
	
	public Id getId() { return this.id; }
	public SaploCorpus.Id getCorpusId() { return this.corpusId; }
	public String getHeadline() { return this.headline; }
	public String getUrl() { return this.url; }
	
}
