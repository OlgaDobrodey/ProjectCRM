package com.itrex.java.lab;

import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;

public class Runner {

    public static void main(String[] args) throws CRMProjectServiceException, CRMProjectRepositoryException {
        System.out.println("===================START APP======================");
//        ApplicationContext context =  new AnnotationConfigApplicationContext(CRMContextConfiguration.class);
//        RoleLookWorkMethods.chek(context);
        System.out.println("================CLOSE APP===================");
    }
}
