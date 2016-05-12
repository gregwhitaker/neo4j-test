package com.niketech.blueprint.neo4jtest;

import org.junit.Test;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
