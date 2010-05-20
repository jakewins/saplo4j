package com.voltvoodoo.saplo4j.model;

/**
 * Represents a Saplo Corpus, a sort of namespace or container for documents within Saplo. 
 * All documents within the corpus have to be in the same language.
 * 
 * SaploCorpus objects cannot be instantiated, but must be created via {@link com.voltvoodoo.saplo4j.Saplo}.
 * Instances of this class are immutable.
 * 
 * @author Jacob Hansson <jacob@voltvoodoo.com>
 *
 */
public class SaploCorpus {

	/**
	 * Typesafe id.
	 */
	public static class Id extends AbstractId {
		private static final long serialVersionUID = -9195944504752505115L;
		public Id(Long id) { super(id); }
		public Id() { super(); }
	}
	
	protected Id id;
	protected Language language;
	protected String   name;
	protected String   description;
	protected int      lastArticleId;
	
	//
	// CONSTRUCTORS
	//
	
	protected SaploCorpus( Id id, Language language, String name, String description, int lastArticleId ) {
		this.id            = id;
		this.language      = language;
		this.name          = name;
		this.description   = description;
		this.lastArticleId = lastArticleId;
	}
	
	//
	// GETTERS
	//
	
	/**
	 * Get the id of this corpus
	 */
	public Id getId() { return this.id; }
	
	/**
	 * Get the language of this corpus
	 */
	public Language getLanguage() { return this.language; }
	
	/**
	 * Get the name of this corpus
	 */
	public String getName() { return this.name; }
	
	/**
	 * Get the description of this corpus
	 */
	public String getDescription() { return this.description; }
	
	/**
	 * Get the id of the latest article added to this corpus
	 * @return
	 */
	public Integer getLatestArticleAddedId() { return this.lastArticleId; }
	
}
