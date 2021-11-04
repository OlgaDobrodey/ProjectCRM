package com.itrex.java.lab;

import com.itrex.java.lab.config.MyApplicationContextConfiguration;
import com.itrex.java.lab.service.FlywayService;
import com.itrex.java.lab.workMethodschek.RoleLookWorkMethods;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Runner {

    public static void main(String[] args) {
        System.out.println("===================START APP======================");
        ApplicationContext ctx = new AnnotationConfigApplicationContext(MyApplicationContextConfiguration.class);

        System.out.println("================START MIGRATION===================");
        FlywayService flywayService = ctx.getBean(FlywayService.class);
        flywayService.migrate();

        //visual representation of how methods work

          RoleLookWorkMethods.chek(ctx);
//          UserLookWorkMethods.chek(ctx);
//          TaskLookWorkMethods.chek(ctx);

    }
}
