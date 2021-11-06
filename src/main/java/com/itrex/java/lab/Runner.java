package com.itrex.java.lab;

import com.itrex.java.lab.config.CRMContextConfiguration;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.workMethodschek.RoleLookWorkMethods;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Runner {

    public static void main(String[] args) throws CRMProjectServiceException {
        System.out.println("===================START APP======================");
        ApplicationContext ctx = new AnnotationConfigApplicationContext(CRMContextConfiguration.class);

        RoleLookWorkMethods.chek(ctx);


        System.out.println("================CLOSE APP===================");
    }

}
