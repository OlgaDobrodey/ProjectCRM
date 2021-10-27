package com.itrex.java.lab.workMethodschek;

import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.repository.impl.hibernate.HibernateTaskRepositoryImpl;
import com.itrex.java.lab.repository.impl.hibernate.HibernateUserRepositoryImpl;
import org.hibernate.SessionFactory;

import java.util.List;

public class TaskLookWorkMethods {

    public static void chek(SessionFactory sessionFactory) {
        TaskRepository taskRepository = new HibernateTaskRepositoryImpl(sessionFactory);
        UserRepository userRepository = new HibernateUserRepositoryImpl(sessionFactory);
        try {

            System.out.println("SELECT ALL "+ taskRepository.selectAll());
            System.out.println("SELECT ID "+ taskRepository.selectById(28));

            List<Task> tasks = UtillCategory.createTestTasks(4);
            System.out.println("ADD TASK " + taskRepository.add(tasks.get(0)));
            System.out.println("ADD TASKS "+ taskRepository.addAll(List.of(tasks.get(1),tasks.get(2))));
            System.out.println("UPDATE "+ taskRepository.update(tasks.get(3),4));

            System.out.println("SELECT ALL "+ taskRepository.selectAll());
            System.out.println("SELECT ALL USERS BY TASK "+ taskRepository.selectAllUsersByTask(taskRepository.selectById(2)));

            System.out.println("REMOVE TASK+ " + taskRepository.remove(taskRepository.selectById(7)));
            System.out.println("SELECT ALL " + taskRepository.selectAll());
            System.out.println("SELECT 4 USER " + userRepository.selectAllTasksByUser(userRepository.selectById(4)));
            System.out.println("SELECT 10 USER " + userRepository.selectAllTasksByUser(userRepository.selectById(10)));
        } catch (CRMProjectRepositoryException e) {
            e.printStackTrace();
        }
    }
}
