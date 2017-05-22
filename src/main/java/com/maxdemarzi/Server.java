package com.maxdemarzi;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class Server {

    private static final String STOREDIR = "/Users/maxdemarzi/Projects/example_embedded_neo4j/neo4j/data/graph.db";
    private static final File DB_PATH = new File(STOREDIR);
    private static final String PATHTOCONFIG = "/Users/maxdemarzi/Projects/example_embedded_neo4j/neo4j/conf/";

    private static final GraphDatabaseService graphDb = new GraphDatabaseFactory()
            .newEmbeddedDatabaseBuilder( DB_PATH )
            //.loadPropertiesFromFile(PATHTOCONFIG + "neo4j.conf")
            // or you can set these programmatically
            //.setConfig( GraphDatabaseSettings.pagecache_memory, "512M" )
            //.setConfig( GraphDatabaseSettings.string_block_size, "60" )
            //.setConfig( GraphDatabaseSettings.array_block_size, "300" )
            .newGraphDatabase();

    public static void main(final String[] args) {

        try ( Transaction tx = graphDb.beginTx() )
        {
            // Database operations go here
            Node max = graphDb.createNode(Label.label("User"));
            max.setProperty("name", "Max De Marzi");
            Node neo4j = graphDb.createNode(Label.label("Thing"));
            max.createRelationshipTo(neo4j, RelationshipType.withName("LIKES"));
            tx.success();
        }
    }


    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
}
