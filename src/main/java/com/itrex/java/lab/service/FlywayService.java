package com.itrex.java.lab.service;

import org.flywaydb.core.Flyway;

import static com.itrex.java.lab.properties.Properties.*;

public class FlywayService {

    private Flyway flyway;

    public FlywayService() {
        inti();
    }

    public void migrate() {
        flyway.migrate();
    }

    public void clean() {
        flyway.clean();
    }

    private void inti() {
        flyway = Flyway.configure()
                .dataSource(URL, USER, PSW)
                .locations(LOCATIONS)
                .schemas(SCHEMA)
                .load();
    }
}
