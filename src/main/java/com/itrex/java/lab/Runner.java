package com.itrex.java.lab;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.repository.impl.jdbc.JDBCRoleRepositoryImpl;
import com.itrex.java.lab.repository.impl.jdbc.JDBCTaskRepositoryImpl;
import com.itrex.java.lab.repository.impl.jdbc.JDBCUserRepositoryImpl;
import com.itrex.java.lab.service.FlywayService;
import org.h2.jdbcx.JdbcConnectionPool;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import static com.itrex.java.lab.properties.Properties.*;

public class Runner {

    public static void main(String[] args) {
        System.out.println("===================START APP======================");
        System.out.println("================START MIGRATION===================");
        FlywayService flywayService = new FlywayService();
        flywayService.migrate();

        System.out.println("============CREATE CONNECTION POOL================");
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Role role = new Role();
            role.setRoleName("Doctor");
            session.save(role);
            session.getTransaction().commit();
            JdbcConnectionPool jdbcConnectionPool = JdbcConnectionPool.create(H2_URL, H2_USER, H2_PSW);

            System.out.println("=================ROLE=============");

            UserRepository userRepository = new JDBCUserRepositoryImpl(jdbcConnectionPool);
            TaskRepository taskRepository = new JDBCTaskRepositoryImpl(jdbcConnectionPool);
            RoleRepository roleRepository = new JDBCRoleRepositoryImpl(jdbcConnectionPool);

            System.out.println("SELECT ALL"+ roleRepository.selectAll());
        } catch (CRMProjectRepositoryException e) {
            e.printStackTrace();
        }
    }
}
