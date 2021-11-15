package com.itrex.java.lab;

import com.itrex.java.lab.config.CRMContextConfiguration;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.service.TaskService;
import com.itrex.java.lab.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class Runner {

    public static void main(String[] args) throws CRMProjectServiceException {
        try {
            System.out.println("===================START APP======================");
            ApplicationContext context = new AnnotationConfigApplicationContext(CRMContextConfiguration.class);
            TaskService taskService = context.getBean(TaskService.class);
            UserService userService = context.getBean(UserService.class);

//
//            taskService.remove(3);
//            System.out.println(taskService.getAllTasksByUser(1));
//            System.out.println(taskService.getAllTasksByUser(7));
//            System.out.println(taskService.getAll());

//            taskService.removeAllTasksByUser(2);
//            System.out.println(taskService.getAllTasksByUser(2));

            userService.removeAllUsersByTask(2);
            System.out.println(userService.getAllUsersByTaskDTO(2));
//             System.out.println(taskRepository.selectAllTasksByUser(2));
        } catch (Exception e) {
            log.info("THE END..", e);
        }
        System.out.println("================CLOSE APP===================");
    }
}

