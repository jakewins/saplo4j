package com.voltvoodoo.saplo4j.model;

public class SaploTag {

	/**
	 * Typesafe id.
	 */
	public static class Id extends AbstractId {
		private static final long serialVersionUID = -9195944504752505115L;

		public Id(Long id) {
			super(id);
		}

		public Id() {
			super();
		}
	}

	public enum Type {
		UNKNOWN, TOPIC, PERSON, ORGANIZATION, LOCATION
	}

	protected Id id;
	protected String word;
	protected Type type;
	protected SaploDocument.Id documentId;
	protected SaploCorpus.Id corpusId;

	//
	// CONSTRUCTOR
	//

	public SaploTag() {
	}

	public SaploTag(Id id, SaploCorpus.Id corpusId,
			SaploDocument.Id documentId, String word, Type type) {
		this.id = id;
		this.word = word;
		this.type = type;
		this.corpusId = corpusId;
		this.documentId = documentId;
	}

	//
	// GETTERS
	//

	public Type getType() {
		return this.type;
	}

	public String getTagWord() {
		return this.word;
	}

	public Id getId() {
		return this.id;
	}

	public SaploCorpus.Id getCorpusId() {
		return this.corpusId;
	}

	public SaploDocument.Id getDocumentId() {
		return this.documentId;
	}

}
