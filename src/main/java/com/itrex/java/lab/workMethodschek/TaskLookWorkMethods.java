package com.itrex.java.lab.workMethodschek;

import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class TaskLookWorkMethods {

    public static void chek(ApplicationContext ctx) {

        TaskRepository taskRepository = ctx.getBean(TaskRepository.class);
        UserRepository userRepository = ctx.getBean(UserRepository.class);
        try {

            System.out.println("SELECT ALL " + taskRepository.selectAll());
            System.out.println("SELECT ID " + taskRepository.selectById(28));

            List<Task> tasks = UtillCategory.createTestTasks(4);
            System.out.println("ADD TASK " + taskRepository.add(tasks.get(0)));
            System.out.println("ADD TASKS " + taskRepository.addAll(List.of(tasks.get(1), tasks.get(2))));
            System.out.println("UPDATE " + taskRepository.update(tasks.get(3), 4));

            System.out.println("SELECT ALL " + taskRepository.selectAll());
            System.out.println("SELECT ALL USERS BY TASK " + taskRepository.selectAllUsersByTask(taskRepository.selectById(2)));

            System.out.println("REMOVE TASK+ " + taskRepository.remove(taskRepository.selectById(7)));
            System.out.println("SELECT ALL " + taskRepository.selectAll());
            System.out.println("SELECT 4 USER " + userRepository.selectAllTasksByUser(userRepository.selectById(4)));
            System.out.println("SELECT 10 USER " + userRepository.selectAllTasksByUser(userRepository.selectById(10)));
        } catch (CRMProjectRepositoryException e) {
            e.printStackTrace();
        }
    }
}
