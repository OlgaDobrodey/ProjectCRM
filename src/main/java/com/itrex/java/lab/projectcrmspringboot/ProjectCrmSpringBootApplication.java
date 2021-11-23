package com.itrex.java.lab.projectcrmspringboot;

import com.itrex.java.lab.projectcrmspringboot.repository.TaskRepository;
import com.itrex.java.lab.projectcrmspringboot.repository.UserRepository;
import com.itrex.java.lab.projectcrmspringboot.service.TaskService;
import com.itrex.java.lab.projectcrmspringboot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@SpringBootApplication
@EnableAspectJAutoProxy
public class ProjectCrmSpringBootApplication implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskService taskService;
    @Autowired
    UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(ProjectCrmSpringBootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("===================START APP======================");
        System.out.println(userService.getAll());
         System.out.println("=================SHUT DOWN APP====================");
    }

}
