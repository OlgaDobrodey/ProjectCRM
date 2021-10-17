package com.itrex.java.lab;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.repository.impl.JDBCRoleRepositoryImpl;
import com.itrex.java.lab.repository.impl.JDBCUserRepositoryImpl;
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

        System.out.println("=================ROLE=============");
        RoleRepository roleRepository = new JDBCRoleRepositoryImpl(jdbcConnectionPool);
        System.out.println("SELECT_ALL" + roleRepository.selectAll());
        System.out.println("SELECT_BY_ID" + roleRepository.selectById(1));

        Role role = new Role();
        role.setRoleName("doctor");

        System.out.println("ADD" + roleRepository.add(role));
        System.out.println("SELECT_ALL" + roleRepository.selectAll());

        Role role2 = new Role();
        role2.setRoleName("actor");
        Role role3 = new Role();
        role3.setRoleName("patient");

        System.out.println("ADD_ALL" + roleRepository.addAll(List.of(role2, role3)));
        System.out.println("SELECT_ALL" + roleRepository.selectAll());

        Role role4 = new Role();
        role4.setRoleName("USER");

        System.out.println("UPDATE" + roleRepository.update(role4, 2));
        System.out.println("SELECT_ALL" + roleRepository.selectAll());

        System.out.println("REMOVE" + roleRepository.remove(1));
        System.out.println("SELECT_ALL" + roleRepository.selectAll());

        UserRepository userRepository = new JDBCUserRepositoryImpl(jdbcConnectionPool);
        System.out.println("SELECT_ALL_USERS" + userRepository.selectAll() + "\n");

        System.out.println("=========CLOSE ALL UNUSED CONNECTIONS=============");
        jdbcConnectionPool.dispose();
        System.out.println("=================SHUT DOWN APP====================");
    }
}
