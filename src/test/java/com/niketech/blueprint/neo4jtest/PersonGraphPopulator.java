package com.niketech.blueprint.neo4jtest;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Values;

public class PersonGraphPopulator implements GraphPopulator {

    private static final String CREATE_PERSON_CQL = "CREATE (p:Person {name:{name}})";

    private static final String CREATE_FRIENDSHIP_CQL = "MATCH (p1:Person {name:{p1Name}}), (p2:Person {name:{p2Name}}) " +
                                                        "CREATE (p1)-[:FRIEND]->(p2)";

    @Override
    public void populate(Driver driver) {
        try (Transaction tx = driver.session().beginTransaction()) {
            tx.run(CREATE_PERSON_CQL, Values.parameters("name", "Bob"));
            tx.run(CREATE_PERSON_CQL, Values.parameters("name", "Fred"));
            tx.run(CREATE_PERSON_CQL, Values.parameters("name", "Carl"));
            tx.run(CREATE_PERSON_CQL, Values.parameters("name", "Dan"));

            // Bob is friends with Fred
            tx.run(CREATE_FRIENDSHIP_CQL, Values.parameters("p1Name", "Bob",
                                                            "p2Name", "Fred"));
            // Fred is friends with Carl
            tx.run(CREATE_FRIENDSHIP_CQL, Values.parameters("p1Name", "Fred",
                    "p2Name", "Carl"));

            // Carl is friends with Dan
            tx.run(CREATE_FRIENDSHIP_CQL, Values.parameters("p1Name", "Carl",
                    "p2Name", "Dan"));

            // Dan is friends with Fred
            tx.run(CREATE_FRIENDSHIP_CQL, Values.parameters("p1Name", "Dan",
                    "p2Name", "Fred"));

            tx.success();
        }
    }
}
