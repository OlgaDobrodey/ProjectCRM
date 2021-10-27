package com.itrex.java.lab;

import com.itrex.java.lab.service.FlywayService;
import com.itrex.java.lab.util.HibernateUtil;
import org.hibernate.SessionFactory;

public class Runner {

    public static void main(String[] args) {
        System.out.println("===================START APP======================");
        System.out.println("================START MIGRATION===================");
        FlywayService flywayService = new FlywayService();
        flywayService.migrate();

        try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        ) {

            //visual representation of how methods work

//          RoleLookWorkMethods.chek(sessionFactory);
//          UserLookWorkMethods.chek(sessionFactory);
//          TaskLookWorkMethods.chek(sessionFactory);

        }
    }
}
