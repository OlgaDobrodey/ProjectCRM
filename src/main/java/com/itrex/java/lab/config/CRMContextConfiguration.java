package com.itrex.java.lab.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

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
    public JdbcConnectionPool jdbcConnectionPool() {
        return JdbcConnectionPool.create(H2_URL, H2_USER, H2_PSW);
    }

    @Bean
    @DependsOn("flyway")
    public DataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("org.h2.Driver");
        cpds.setJdbcUrl("jdbc:h2:mem:CRM;DB_CLOSE_DELAY=-1");
        cpds.setUser("sa");
        cpds.setPassword("");
        return cpds;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() throws PropertyVetoException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.itrex.java.lab.entity");

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        properties.setProperty("format_sql", "true");
        properties.setProperty("show_sql", "true");
        properties.setProperty("hibernate.default_schema", "crm");

        sessionFactory.setHibernateProperties(properties);
        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws PropertyVetoException {
        HibernateTransactionManager transactionManager
                = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
}
