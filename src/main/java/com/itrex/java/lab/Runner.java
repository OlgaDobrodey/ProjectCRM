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

        System.out.println("============CREATE CONNECTION POOL================");
        try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        ) {
//          RoleRep.chek(sessionFactory);
          UserREpo.chek(sessionFactory);
//            TaskRepo.chek(sessionFactory);

        }
    }
}
