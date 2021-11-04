package com.itrex.java.lab.config;

import com.itrex.java.lab.service.FlywayService;
import org.h2.jdbcx.JdbcConnectionPool;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static com.itrex.java.lab.properties.Properties.*;

@Configuration
@ComponentScan("com.itrex.java.lab.repository")
public class MyApplicationContextConfiguration {

    @Bean
    public SessionFactory sessionFactory() {
        return new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
    }

    @Bean
    @Scope("prototype")
    public FlywayService flywayService() {
        return new FlywayService();
    }

    @Bean
    public JdbcConnectionPool jdbcConnectionPool() {
        return JdbcConnectionPool.create(H2_URL, H2_USER, H2_PSW);
    }
}
