package com.voltvoodoo.saplo4j;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.voltvoodoo.saplo4j.async.SaploCallback;
import com.voltvoodoo.saplo4j.exception.SaploException;
import com.voltvoodoo.saplo4j.model.Language;
import com.voltvoodoo.saplo4j.model.SaploCorpus;
import com.voltvoodoo.saplo4j.model.SaploDocument;
import com.voltvoodoo.saplo4j.model.SaploSimilarity;
import com.voltvoodoo.saplo4j.model.SaploTag;

public class SaploNullArgumentsTest {

	Saplo saplo;

	SaploCallback<SaploDocument.Id> noOpDocCallback = new SaploCallback<SaploDocument.Id>() {

		public void onSuccess(SaploDocument.Id result) {}

		public void onFailure(SaploException exception) {}
	};
	
	SaploCallback<Boolean> noOpBooleanCallback = new SaploCallback<Boolean>() {

		public void onSuccess(Boolean result) {}

		public void onFailure(SaploException exception) {}
	};
	
	SaploCallback<SaploDocument> noOpDocumentCallback = new SaploCallback<SaploDocument>() {

		public void onSuccess(SaploDocument result) {}

		public void onFailure(SaploException exception) {}
	};
	
	SaploCallback<List<SaploSimilarity>> noOpSimilarDocumentsCallback = new SaploCallback<List<SaploSimilarity>>() {

		public void onSuccess(List<SaploSimilarity> result) {}

		public void onFailure(SaploException exception) {}
	};
	
	SaploCallback<SaploSimilarity> noOpSimilarityCallback = new SaploCallback<SaploSimilarity>() {

		public void onSuccess(SaploSimilarity result) {}

		public void onFailure(SaploException exception) {}
	};
	
	SaploCallback<List<SaploTag>> noOpTagsCallback = new SaploCallback<List<SaploTag>>() {

		public void onSuccess(List<SaploTag> result) {}

		public void onFailure(SaploException exception) {}
	};
	
	@Before
	public void resetSaplo() {
		SaploConnection conn = mock(SaploConnection.class);
		saplo = new Saplo(conn);
	}
	
	// NULL CORPUS ID
	
	@Test(expected=IllegalArgumentException.class)
	public void addDocumentShouldNotAllowNullCorpusId() {
		saplo.addDocument(null, "", "", Language.ENGLISH);
		fail( "Add document should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncAddDocumentShouldNotAllowNullCorpusId() {
		saplo.addDocument(null, "", "", Language.ENGLISH, noOpDocCallback);
		fail( "Async add document should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addDocumentWithMetaShouldNotAllowNullCorpusId() {
		saplo.addDocument(null, "", "", "", Language.ENGLISH);
		fail( "Add document with meta should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncAddDocumentWithMetaShouldNotAllowNullCorpusId() {
		saplo.addDocument(null, "", "", "", Language.ENGLISH, noOpDocCallback);
		fail( "Async add document with meta should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void deleteCorpusShouldNotAllowNullCorpusId() {
		saplo.deleteCorpus(null);
		fail( "Async add document with meta should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void deleteDocumentShouldNotAllowNullCorpusId() {
		saplo.deleteDocument(null, new SaploDocument.Id(1l));
		fail( "Delete document should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncDeleteDocumentShouldNotAllowNullCorpusId() {
		saplo.deleteDocument(null, new SaploDocument.Id(1l), noOpBooleanCallback);
		fail( "Async delete document should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void deleteSimilarityShouldNotAllowNullCorpusId() {
		saplo.deleteSimilarity(null, new SaploSimilarity.Id(1l), new SaploDocument.Id(1l));
		fail( "Delete similarity should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncDeleteSimilarityShouldNotAllowNullCorpusId() {
		saplo.deleteSimilarity(null, new SaploSimilarity.Id(1l), new SaploDocument.Id(1l), noOpBooleanCallback);
		fail( "Async delete similarity should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getDocumentShouldNotAllowNullCorpusId() {
		saplo.getDocument(null, new SaploDocument.Id(1l));
		fail( "Get document should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncGetDocumentShouldNotAllowNullCorpusId() {
		saplo.getDocument(null, new SaploDocument.Id(1l), noOpDocumentCallback);
		fail( "Async get document should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getSimilarDocumentsShouldNotAllowNullCorpusId() {
		saplo.getSimilarDocuments(null, new SaploDocument.Id(1l));
		fail( "Get similar documents should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncGetSimilarDocumentsShouldNotAllowNullCorpusId() {
		saplo.getSimilarDocuments(null, new SaploDocument.Id(1l), noOpSimilarDocumentsCallback);
		fail( "Async get similar documents should not allow null as corpus id." );
	}

	@Test(expected=IllegalArgumentException.class)
	public void getSimilarityShouldNotAllowNullCorpusId() {
		saplo.getSimilarity(null, new SaploSimilarity.Id(1l), new SaploDocument.Id(1l));
		fail( "Get similarity should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncGetSimilarityShouldNotAllowNullCorpusId() {
		saplo.getSimilarity(null, new SaploSimilarity.Id(1l), new SaploDocument.Id(1l), noOpSimilarityCallback);
		fail( "Async get similarity should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getTagsShouldNotAllowNullCorpusId() {
		saplo.getTags(null, new SaploDocument.Id(1l));
		fail( "Get tags should not allow null corpus as id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncGetTagsShouldNotAllowNullCorpusId() {
		saplo.getTags(null, new SaploDocument.Id(1l), noOpTagsCallback);
		fail( "Async get tags should not allow null corpus as id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void updateDocumentShouldNotAllowNullCorpusId() {
		saplo.updateDocument(null, new SaploDocument.Id(1l), "", "", Language.ENGLISH);
		fail( "Update document should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncUpdateDocumentShouldNotAllowNullCorpusId() {
		saplo.updateDocument(null, new SaploDocument.Id(1l), "", "", Language.ENGLISH, noOpBooleanCallback);
		fail( "Async update document should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void updateDocumentWithMetaShouldNotAllowNullCorpusId() {
		saplo.updateDocument(null, new SaploDocument.Id(1l), "", "", "", Language.ENGLISH);
		fail( "Update document with meta should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncUpdateDocumentWithMetaShouldNotAllowNullCorpusId() {
		saplo.updateDocument(null, new SaploDocument.Id(1l), "", "", "", Language.ENGLISH, noOpBooleanCallback);
		fail( "Async update document with meta should not allow null as corpus id." );
	}
	
	// NULL LANGUAGE
	
	@Test(expected=IllegalArgumentException.class)
	public void createCorpusShouldNotAllowNullLanguage() {
		saplo.createCorpus("","", null);
		fail( "Create document should not allow null as corpus id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addDocumentShouldNotAllowNullLanguage() {
		saplo.addDocument(new SaploCorpus.Id(1l), "", "", null);
		fail( "Add document should not allow null as language." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncAddDocumentShouldNotAllowNullLanguage() {
		saplo.addDocument(new SaploCorpus.Id(1l), "", "", null, noOpDocCallback);
		fail( "Async add document should not allow null as language." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addDocumentWithMetaShouldNotAllowNullLanguage() {
		saplo.addDocument(new SaploCorpus.Id(1l), "", "", "", null);
		fail( "Add document with meta should not allow null as language." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncAddDocumentWithMetaShouldNotAllowNullLanguage() {
		saplo.addDocument(new SaploCorpus.Id(1l), "", "", "", null, noOpDocCallback);
		fail( "Async add document with meta should not allow null as language." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void updateDocumentShouldNotAllowNullLanguage() {
		saplo.updateDocument(new SaploCorpus.Id(1l), new SaploDocument.Id(1l), "", "", null);
		fail( "Update document should not allow null as language." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncUpdateDocumentShouldNotAllowNullLanguage() {
		saplo.updateDocument(new SaploCorpus.Id(1l), new SaploDocument.Id(1l), "", "", null, noOpBooleanCallback);
		fail( "Async update document should not allow null as language." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void updateDocumentWithMetaShouldNotAllowNullLanguage() {
		saplo.updateDocument(new SaploCorpus.Id(1l), new SaploDocument.Id(1l), "", "", "", null);
		fail( "Update document with meta should not allow null as language." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncUpdateDocumentWithMetaShouldNotAllowNullLanguage() {
		saplo.updateDocument(new SaploCorpus.Id(1l), new SaploDocument.Id(1l), "", "", "", null, noOpBooleanCallback);
		fail( "Async update document with meta should not allow null as language." );
	}
	
	// NULL DOCUMENT
	
	@Test(expected=IllegalArgumentException.class)
	public void deleteDocumentShouldNotAllowNullDocumentId() {
		saplo.deleteDocument(new SaploCorpus.Id(1l), null);
		fail( "Delete document should not allow null as document id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncDeleteDocumentShouldNotAllowNullDocumentId() {
		saplo.deleteDocument(new SaploCorpus.Id(1l), null, noOpBooleanCallback);
		fail( "Async delete document should not allow null as document id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getDocumentShouldNotAllowNullDocumentId() {
		saplo.getDocument(new SaploCorpus.Id(1l), null);
		fail( "Get document should not allow null as document id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncGetDocumentShouldNotAllowNullDocumentId() {
		saplo.getDocument(new SaploCorpus.Id(1l), null, noOpDocumentCallback);
		fail( "Async get document should not allow null as document id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void deleteSimilarityShouldNotAllowNullDocumentId() {
		saplo.deleteSimilarity(new SaploCorpus.Id(1l), new SaploSimilarity.Id(1l), null);
		fail( "Delete similarity should not allow null as document id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncDeleteSimilarityShouldNotAllowNullDocumentId() {
		saplo.deleteSimilarity(new SaploCorpus.Id(1l), new SaploSimilarity.Id(1l), null, noOpBooleanCallback);
		fail( "Async delete similarity should not allow null as document id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getSimilarDocumentsShouldNotAllowNullDocumentId() {
		saplo.getSimilarDocuments(new SaploCorpus.Id(1l), null);
		fail( "Get similar documents should not allow null as document id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncGetSimilarDocumentsShouldNotAllowNullDocumentId() {
		saplo.getSimilarDocuments(new SaploCorpus.Id(1l), null, noOpSimilarDocumentsCallback);
		fail( "Async get similar documents should not allow null as document id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getSimilarityShouldNotAllowNullDocumentId() {
		saplo.getSimilarity(new SaploCorpus.Id(1l), new SaploSimilarity.Id(1l), null);
		fail( "Get similarity should not allow null as document id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncGetSimilarityShouldNotAllowNullDocumentId() {
		saplo.getSimilarity(new SaploCorpus.Id(1l), new SaploSimilarity.Id(1l), null, noOpSimilarityCallback);
		fail( "Async get similarity should not allow null as document id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getTagsShouldNotAllowNullDocumentId() {
		saplo.getTags(new SaploCorpus.Id(1l), null);
		fail( "Get tags should not allow null as document id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncGetTagsShouldNotAllowNullDocumentId() {
		saplo.getTags(new SaploCorpus.Id(1l), null, noOpTagsCallback);
		fail( "Async get tags should not allow null as document id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void updateDocumentShouldNotAllowNullDocumentId() {
		saplo.updateDocument(new SaploCorpus.Id(1l), null, "", "", Language.ENGLISH);
		fail( "Update document should not allow null as document id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncUpdateDocumentShouldNotAllowNullDocumentId() {
		saplo.updateDocument(new SaploCorpus.Id(1l), null, "", "", Language.ENGLISH, noOpBooleanCallback);
		fail( "Async update document should not allow null as document id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void updateDocumentWithMetaShouldNotAllowNullDocumentId() {
		saplo.updateDocument(new SaploCorpus.Id(1l), null, "", "", "", Language.ENGLISH);
		fail( "Update document with meta should not allow null as document id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncUpdateDocumentWithMetaShouldNotAllowNullDocumentId() {
		saplo.updateDocument(new SaploCorpus.Id(1l), null, "", "", "", Language.ENGLISH, noOpBooleanCallback);
		fail( "Async update document with meta should not allow null as document id." );
	}
	
	// NULL SIMILARITY
	
	@Test(expected=IllegalArgumentException.class)
	public void deleteSimilarityShouldNotAllowNullSimilarityId() {
		saplo.deleteSimilarity(new SaploCorpus.Id(1l), null, new SaploDocument.Id(1l));
		fail( "Delete similarity should not allow null as similarity id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncDeleteSimilarityShouldNotAllowNullSimilarityId() {
		saplo.deleteSimilarity(new SaploCorpus.Id(1l), null, new SaploDocument.Id(1l), noOpBooleanCallback);
		fail( "Async delete similarity should not allow null as similarity id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getSimilarityShouldNotAllowNullSimilarityId() {
		saplo.getSimilarity(new SaploCorpus.Id(1l), null, new SaploDocument.Id(1l));
		fail( "Get similarity should not allow null as similarity id." );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void asyncGetSimilarityShouldNotAllowNullSimilarityId() {
		saplo.getSimilarity(new SaploCorpus.Id(1l), null, new SaploDocument.Id(1l), noOpSimilarityCallback);
		fail( "Async get similarity should not allow null as similarity id." );
	}
	
	
}
