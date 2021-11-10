package com.itrex.java.lab.config;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static com.itrex.java.lab.properties.Properties.*;

@Configuration
@ComponentScan("com.itrex.java.lab")
@EnableTransactionManagement
public class CRMContextConfiguration {

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return Flyway.configure()
                .dataSource(H2_URL, H2_USER, H2_PSW)
                .locations(H2_LOCATIONS)
                .schemas(H2_SCHEMA)
                .load();
    }

    @Bean
    @DependsOn("flyway")
    public SessionFactory sessionFactory() {
        return new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
    }

    @Bean
    @DependsOn("flyway")
    public JdbcConnectionPool jdbcConnectionPool() {
        return JdbcConnectionPool.create(H2_URL, H2_USER, H2_PSW);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(jdbcConnectionPool());
    }
}
