package com.itrex.java.lab;

import com.itrex.java.lab.config.CRMContextConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Runner {

    public static void main(String[] args) {
        System.out.println("===================START APP======================");
        ApplicationContext ctx = new AnnotationConfigApplicationContext(CRMContextConfiguration.class);
        System.out.println("================CLOSE APP===================");
    }

}
