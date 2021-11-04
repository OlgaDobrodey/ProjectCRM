package com.itrex.java.lab.workMethodschek;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.repository.UserRepository;
import org.h2.jdbcx.JdbcConnectionPool;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

public class RoleLookWorkMethods {

    public static void chek(ApplicationContext ctx) {

        try (SessionFactory sessionFactory = ctx.getBean(SessionFactory.class)) {
            JdbcConnectionPool jdbcConnectionPool = ctx.getBean(JdbcConnectionPool.class);

            RoleRepository roleRepository = ctx.getBean(RoleRepository.class);
            UserRepository userRepository = ctx.getBean(UserRepository.class);
            try {

                System.out.println("removeROLE =" + roleRepository.remove(roleRepository.selectById(1)));

                System.out.println("SELECT ROLE ALL " + roleRepository.selectAll());
                System.out.println("SELECT USER " + userRepository.selectAll());
                Role role = new Role();
                role.setRoleName("Olga");
                role.setId(9);

                System.out.println("removeROLE =" + roleRepository.remove(role));

                System.out.println("SELECT ALL " + roleRepository.selectAll());
                System.out.println("SELECT ALL USER " + userRepository.selectAll());
            } catch (CRMProjectRepositoryException e) {
                e.printStackTrace();
            }
        }
    }
}
