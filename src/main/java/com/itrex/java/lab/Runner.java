package com.itrex.java.lab;

import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.impl.hibernate.HibernateTaskRepositoryImpl;
import com.itrex.java.lab.service.FlywayService;
import com.itrex.java.lab.util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.util.List;

public class Runner {

    public static void main(String[] args) {
        System.out.println("===================START APP======================");
        System.out.println("================START MIGRATION===================");
        FlywayService flywayService = new FlywayService();
        flywayService.migrate();

        System.out.println("============CREATE CONNECTION POOL================");
        try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        ) {
            HibernateTaskRepositoryImpl taskRepository = new HibernateTaskRepositoryImpl(sessionFactory);
            System.out.println("SELECT ALL "+ taskRepository.selectAll());
            System.out.println("SELECT ID "+ taskRepository.selectById(5));

            List<Task> tasks = UtillCategory.createTestTasks(4);
            System.out.println("ADD TASK " + taskRepository.add(tasks.get(0)));
            System.out.println("ADD TASKS "+ taskRepository.addAll(List.of(tasks.get(1),tasks.get(2))));
            System.out.println("UPDATE "+ taskRepository.update(tasks.get(3),4));
            System.out.println("SELECT ALL "+ taskRepository.selectAll());
        } catch (CRMProjectRepositoryException e) {
            e.printStackTrace();
        }
    }
}
