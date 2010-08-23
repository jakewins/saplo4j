Saplo4j
=======

This is a Java client for the Saplo API. It supports most saplo API functions, with a notable exception of the tagging library.

Most long-running methods support both synchronous and asynchronous behaviour. The asynchrounous version of the API allows for several thousand calls to be made simultaneously.

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
