package com.niketech.blueprint.neo4jtest;

import org.neo4j.driver.v1.Driver;

public interface GraphPopulator {

    void populate(Driver driver);
}
