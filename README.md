Neo4j Test Utilities
===

Library of utilities to use when testing code that uses a Neo4j database.

This library provides the following abstract unit test support classes that you can use:

* Neo4jTestSupport - Creates a local Neo4j database for unit testing.
* Neo4jSpringTestSupport - Creates a local Neo4j database for unit testing and supports Spring for dependency injection.

##Populating the Graph
If you need to populate the graph with test data you can create a `GraphPopulator` instance and configure that in
your test support class or you can override the `populateGraph` method and provide inline code to populate the graph
with test data.

##Testing with Spring
If you are using Spring as your dependency injection framework you can create Spring-enabled unit tests for Neo4j
simply by extending the `Neo4jSpringTestSupport` class.

    @ContextConfiguration(classes = {Neo4jSpringTestSupportGraphPopulatorTest.SpringConfig.class})
    public class Neo4jSpringTestSupportGraphPopulatorTest extends Neo4jSpringTestSupport {
    
        @Autowired
        private String someTestValue;
    
        @Override
        protected GraphPopulator graphPopulator() {
            return new PersonGraphPopulator();
        }
    
        @Test
        public void graphShouldContainFourPersonNodes() {
            StatementResult result = driver().session().run("MATCH (p:Person) RETURN COUNT (p) AS cnt");
            assertEquals(4, result.next().get("cnt").asInt());
        }
    
        @Test
        public void bobShouldBeFriendsWithFred() {
            StatementResult result = driver().session().run("MATCH (p1:Person {name:{p1Name}})-[:FRIEND]->(p2:Person {name:{p2Name}})",
                    Values.parameters("p1Name", "Bob",
                                      "p2Name", "Fred"));
            assertTrue(true);
        }
    
        @Configuration
        static class SpringConfig {
    
            @Bean
            public String someTestValue() {
                return "test";
            }
        }
    }
