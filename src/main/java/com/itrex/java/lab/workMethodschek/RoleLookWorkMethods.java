package com.itrex.java.lab.workMethodschek;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.repository.impl.hibernate.HibernateRoleRepositoryImpl;
import com.itrex.java.lab.repository.impl.hibernate.HibernateUserRepositoryImpl;
import org.hibernate.SessionFactory;

public class RoleLookWorkMethods {

    public static void chek(SessionFactory sessionFactory) {

        HibernateRoleRepositoryImpl roleRepository = new HibernateRoleRepositoryImpl(sessionFactory);
        UserRepository userRepository = new HibernateUserRepositoryImpl(sessionFactory);
        try {

            System.out.println("removeROLE =" + roleRepository.remove(roleRepository.selectById(1)));

            System.out.println("SELECT ALL " + roleRepository.selectAll());
            System.out.println("SELECT USER " + userRepository.selectAll());
            Role role = new Role();
            role.setRoleName("Olga");
            role.setId(9);

            System.out.println("removeROLE =" + roleRepository.remove(role));

            System.out.println("SELECT ALL " + roleRepository.selectAll());
            System.out.println("SELECT USER " + userRepository.selectAll());
        } catch (CRMProjectRepositoryException e) {
            e.printStackTrace();
        }
    }
}
