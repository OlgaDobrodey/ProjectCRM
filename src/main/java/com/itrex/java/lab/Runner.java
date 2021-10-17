package com.itrex.java.lab;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.repository.impl.JDBCRoleRepositoryImpl;
import com.itrex.java.lab.repository.impl.JDBCTaskRepositoryImpl;
import com.itrex.java.lab.service.FlywayService;
import org.h2.jdbcx.JdbcConnectionPool;

import java.util.List;

import static com.itrex.java.lab.properties.Properties.*;

public class Runner {

    public static void main(String[] args) {
        System.out.println("===================START APP======================");
        System.out.println("================START MIGRATION===================");
        FlywayService flywayService = new FlywayService();
        flywayService.migrate();

        System.out.println("============CREATE CONNECTION POOL================");
        JdbcConnectionPool jdbcConnectionPool = JdbcConnectionPool.create(H2_URL, H2_USER, H2_PSW);

        JDBCTaskRepositoryImpl jdbcTaskRepository = new JDBCTaskRepositoryImpl(jdbcConnectionPool);
        List<Task> tasks = jdbcTaskRepository.selectAll();
        System.out.println(tasks);

        System.out.println("=========CLOSE ALL UNUSED CONNECTIONS=============");
        jdbcConnectionPool.dispose();
        System.out.println("=================SHUT DOWN APP====================");
    }
}
