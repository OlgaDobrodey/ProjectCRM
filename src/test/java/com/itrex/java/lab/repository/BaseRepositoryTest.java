package com.itrex.java.lab.repository;

import static com.itrex.java.lab.properties.Properties.H2_PSW;
import static com.itrex.java.lab.properties.Properties.H2_URL;
import static com.itrex.java.lab.properties.Properties.H2_USER;

import com.itrex.java.lab.service.FlywayService;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


public abstract class BaseRepositoryTest {

    private final FlywayService flywayService;
    private final JdbcConnectionPool connectionPool;

    public BaseRepositoryTest () {
        flywayService = new FlywayService();
        connectionPool = JdbcConnectionPool.create(H2_URL, H2_USER, H2_PSW);
    }

    @BeforeEach
    public void initDB() {
        flywayService.migrate();
    }

    @AfterEach
    public void cleanDB() {
        flywayService.clean();
    }

    public JdbcConnectionPool getConnectionPool() {
        return connectionPool;
    }

}

