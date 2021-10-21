package com.itrex.java.lab;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.Status;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.repository.impl.JDBCRoleRepositoryImpl;
import com.itrex.java.lab.repository.impl.JDBCTaskRepositoryImpl;
import com.itrex.java.lab.repository.impl.JDBCUserRepositoryImpl;
import com.itrex.java.lab.service.FlywayService;
import org.h2.jdbcx.JdbcConnectionPool;

import static com.itrex.java.lab.properties.Properties.*;

public class Runner {

    public static void main(String[] args) {
        System.out.println("===================START APP======================");
        System.out.println("================START MIGRATION===================");
        FlywayService flywayService = new FlywayService();
        flywayService.migrate();

        System.out.println("============CREATE CONNECTION POOL================");
        JdbcConnectionPool jdbcConnectionPool = JdbcConnectionPool.create(H2_URL, H2_USER, H2_PSW);

        System.out.println("=================ROLE=============");

        UserRepository userRepository = new JDBCUserRepositoryImpl(jdbcConnectionPool);
        TaskRepository taskRepository = new JDBCTaskRepositoryImpl(jdbcConnectionPool);
        RoleRepository roleRepository = new JDBCRoleRepositoryImpl(jdbcConnectionPool);

        try {

            Role role = new Role();
            role.setRoleName("ALLLL");

            System.out.println("UPDATE ROLE " + roleRepository.update(role,991));
            System.out.println("SELECT ALL" + roleRepository.selectAll());
            System.out.println("SELECT ALL : " + taskRepository.selectAll());
            Task task = new Task();
            task.setTitle("title0");
            task.setStatus(Status.NEW);
            task.setInfo("task info1");
            System.out.println("ADD :" + taskRepository.add(task));
            Task task2 = new Task();
            task2.setTitle("title100");
            task2.setStatus(Status.NEW);
            task2.setInfo("task info100");
            Task task3 = new Task();
            task3.setTitle("title012");
            task3.setStatus(Status.NEW);
            task3.setInfo("task info121");

            System.out.println("UPDATE : " + taskRepository.update(task2, 1));
            System.out.println("ALL USERS BY TASK :" + taskRepository.selectAllUsersByTask(taskRepository.selectById(2)));
            taskRepository.addUserByTask(task, userRepository.selectById(2));
            System.out.println("ADD USER BY TASK");
            userRepository.printCrossTable();
            taskRepository.removeUserByTask(task, userRepository.selectById(2));
            userRepository.printCrossTable();
            taskRepository.removeAllUsersByTask(taskRepository.selectById(1));
            userRepository.printCrossTable();
            Task task4 = new Task();
            task4.setId(28);
            task4.setTitle("title012111");
            task4.setStatus(Status.NEW);
            task4.setInfo("task info121");
            boolean a = taskRepository.remove(task4);
            System.out.println("REMOVE " + a);
            System.out.println("ALL TASK" + taskRepository.selectAll());
            userRepository.printCrossTable();

        } catch (CRMProjectRepositoryException e) {
            System.out.println(e);
        }

        System.out.println("=========CLOSE ALL UNUSED CONNECTIONS=============");
        jdbcConnectionPool.dispose();
        System.out.println("=================SHUT DOWN APP====================");
    }
}
