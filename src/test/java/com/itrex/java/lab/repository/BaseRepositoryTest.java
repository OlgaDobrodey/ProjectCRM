package com.itrex.java.lab.repository;

import com.itrex.java.lab.config.MyApplicationContextConfiguration;
import com.itrex.java.lab.service.FlywayService;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(MyApplicationContextConfiguration.class)
public abstract class BaseRepositoryTest {

    private FlywayService flywayService;
    private JdbcConnectionPool connectionPool;

    public BaseRepositoryTest() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(MyApplicationContextConfiguration.class);
        flywayService = ctx.getBean(FlywayService.class);
        connectionPool = ctx.getBean(JdbcConnectionPool.class);
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

