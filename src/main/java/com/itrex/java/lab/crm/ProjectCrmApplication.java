package com.itrex.java.lab.crm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@SpringBootApplication
@EnableAspectJAutoProxy
public class ProjectCrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectCrmApplication.class, args);
    }

}
