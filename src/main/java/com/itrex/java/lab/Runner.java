package com.itrex.java.lab;

import com.itrex.java.lab.config.CRMContextConfiguration;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.workMethodschek.UserLookWorkMethods;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Runner {

    public static void main(String[] args) throws CRMProjectServiceException, CRMProjectRepositoryException {
        System.out.println("===================START APP======================");
        ApplicationContext context = new AnnotationConfigApplicationContext(CRMContextConfiguration.class);
        UserLookWorkMethods.chek(context);
//        RoleLookWorkMethods.chek(context);
//        TaskLookWorkMethods.chek(context);
        System.out.println("================CLOSE APP===================");
    }
}
