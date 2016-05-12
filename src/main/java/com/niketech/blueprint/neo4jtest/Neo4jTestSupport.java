package com.niketech.blueprint.neo4jtest;

import org.junit.After;
import org.junit.Before;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.util.Neo4jRunner;
import org.neo4j.driver.v1.util.Neo4jSettings;

public abstract class Neo4jTestSupport {
    private Neo4jRunner server;
    private Driver driver;

    /**
     * Starts a local Neo4j instance.
     *
     * @throws Exception
     */
    @Before
    public void setup() throws Exception {
        server = Neo4jRunner.getOrCreateGlobalRunner();
        server.ensureRunning(Neo4jSettings.DEFAULT);
        driver = server.driver();

        if (graphPopulator() != null) {
            graphPopulator().populate(driver);
        }

        populateGraph();
    }

    /**
     * Stops the local Neo4j instance.
     *
     * @throws Exception
     */
    @After
    public void teardown() throws Exception {
        server.stop();
    }

    /**
     * Override this method if you wish to provide a {@link GraphPopulator} implementation to
     * initialize data within your local Neo4j test database.
     *
     * @return a {@link GraphPopulator} implementation
     */
    protected GraphPopulator graphPopulator() {
        return null;
    }

    /**
     * Override this method if you wish to provide the logic for populating data within your
     * local Neo4j instance, but do not want to create a full {@link GraphPopulator} implementation.
     *
     * If override this method and supply a {@link GraphPopulator} implementation via the <code>graphPopulator</code> method
     * the graph populator will be executed first and then the logic within this method will be executed.
     */
    protected void populateGraph() {
        //Noop
    }

    /**
     * @return Neo4j driver connected to the local Neo4j database started by this test class
     */
    public Driver driver() {
        return driver;
    }
}
