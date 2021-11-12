package com.itrex.java.lab;

import com.itrex.java.lab.config.CRMContextConfiguration;
import com.itrex.java.lab.dto.RoleDTO;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class Runner {

    public static void main(String[] args) throws CRMProjectServiceException {
        try {
            System.out.println("===================START APP======================");
            ApplicationContext context = new AnnotationConfigApplicationContext(CRMContextConfiguration.class);
            RoleService bean = context.getBean(RoleService.class);
            Flyway flyway = context.getBean(Flyway.class);
            flyway.clean();
            RoleDTO roleDTO = bean.getById(10);
            System.out.println(bean.getAllRole());
        } catch (Exception e) {
            log.info("THE END..");
        }
        System.out.println("================CLOSE APP===================");
    }
}

