Saplo4j
=======

This is a Java client for the Saplo API. It supports most saplo API functions, with a notable exception of the tagging library.

Most long-running methods support both synchronous and asynchronous behaviour. The asynchrounous version of the API allows for several thousand calls to be made simultaneously.

Quickstart
----------

    // Connect
    Saplo saplo = new Saplo("myapikey","myapisecret");
    
    // Create a corpus to put documents in
    SaploCorpus.Id corpusId = saplo.createCorpus("My corpus", "A fancy corpus I made.", Language.ENGLISH);
    
    // Put some documents in the corpus
    SaploDocument.Id firstDocId = saplo.addDocument(corpusId, "The first document", "Body text", Language.ENGLISH);
    SaploDocument.Id secondDocId = saplo.addDocument(corpusId, "The second document", "Body text", Language.ENGLISH);
    SaploDocument.Id thirdDocId = saplo.addDocument(corpusId, "The third document", "Body text", Language.ENGLISH);
	
    // Do some semantic magic
    List<SaploSimilarity> similarToFirstDoc = saplo.getSimilarDocuments(corpusId, firstDocId);
    List<SaploTag> keywordsInFirstDoc = saplo.getTags(corpusId, firstDocId);
    
    // Clean up
    saplo.close();
    

Use as maven dependency
-----------------------

There is currently only the snapshot version available in public maven repos. To use it, add the sonatype repository to your POM:

	<repositories>
		<repository>
			<id>oss.sonatype.org</id>
			<url>https://oss.sonatype.org/content/groups/public</url>
			<snapshots>
				<enabled>true</enabled>
			</snapshots>
		</repository>
	</repositories>
	
And add the latest snapshot as a dependency:

    <dependencies>
        <dependency>
			<groupId>com.voltvoodoo</groupId>
			<artifactId>saplo4j</artifactId>
			<version>0.2-SNAPSHOT</version>
			<type>jar</type>
			<scope>compile</scope>
		</dependency>
    </dependencies>

Build trunk version
------------------

Create jar file:

    mvn package

Install into local repo

    mvn install
