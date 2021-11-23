package com.itrex.java.lab.crm;

import com.itrex.java.lab.crm.repository.TaskRepository;
import com.itrex.java.lab.crm.repository.UserRepository;
import com.itrex.java.lab.crm.service.TaskService;
import com.itrex.java.lab.crm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@SpringBootApplication
@EnableAspectJAutoProxy
public class ProjectCrmApplication implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskService taskService;
    @Autowired
    UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(ProjectCrmApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("===================START APP======================");
        System.out.println("=================SHUT DOWN APP====================");
    }

}
