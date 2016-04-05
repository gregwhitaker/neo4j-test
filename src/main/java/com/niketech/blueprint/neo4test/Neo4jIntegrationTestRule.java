package com.niketech.blueprint.neo4test;

import org.junit.Assert;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.server.WrappingNeoServerBootstrapper;
import org.neo4j.server.configuration.Configurator;
import org.neo4j.server.configuration.ServerConfigurator;
import org.neo4j.test.TestGraphDatabaseFactory;

/**
 * JUnit rule that creates a temporary Neo4j server for integration testing purposes.
 *
 * @author Greg Whitaker
 */
public class Neo4jIntegrationTestRule implements TestRule {
    public static final long DEFAULT_PORT = 7474;
    public static final long DEFAULT_CONNECTION_TIMEOUT = 1000;

    private final WrappingNeoServerBootstrapper bootstrapper;
    private final GraphDatabaseService graph;
    private final long connectionTimeout;

    /**
     * Creates a Neo4j server with default settings.
     */
    public Neo4jIntegrationTestRule() {
        this(DEFAULT_PORT, DEFAULT_CONNECTION_TIMEOUT);
    }

    /**
     * Creates a Neo4j server running on the specified port.
     *
     * @param port port to run Neo4j server on
     */
    public Neo4jIntegrationTestRule(long port) {
        this(port, DEFAULT_CONNECTION_TIMEOUT);
    }

    /**
     * Creates a Neo4j server running on the specified port and with a custom connection timeout.
     *
     * @param port port to run Neo4j server on
     * @param connectionTimeout connection timeout in milliseconds
     */
    public Neo4jIntegrationTestRule(long port, long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        this.graph = new TestGraphDatabaseFactory().newImpermanentDatabase();

        ServerConfigurator configurator = new ServerConfigurator((GraphDatabaseAPI) graph);
        configurator.configuration().addProperty(Configurator.WEBSERVER_PORT_PROPERTY_KEY, port);
        bootstrapper = new WrappingNeoServerBootstrapper((GraphDatabaseAPI) graph, configurator);
    }

    @Override
    public Statement apply(Statement baseStatement, Description description) {
        bootstrapper.start();

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    if (graph.isAvailable(1000)) {
                        baseStatement.evaluate();
                    } else {
                        Assert.fail("Database was shutdown or did not start within the allotted timeout.");
                    }
                } finally {
                    bootstrapper.stop();
                }
            }
        };
    }

    /**
     * @return Neo4j graph
     */
    public GraphDatabaseService getGraph() {
        return graph;
    }
}
