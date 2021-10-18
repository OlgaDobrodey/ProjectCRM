package com.itrex.java.lab.my;

import com.itrex.java.lab.entity.*;
import com.itrex.java.lab.repository.*;
import com.itrex.java.lab.repository.impl.*;
import com.itrex.java.lab.service.FlywayService;
import org.h2.jdbcx.JdbcConnectionPool;

import java.time.LocalDate;
import java.util.List;

import static com.itrex.java.lab.properties.Properties.*;

public class ex {
    public static void main(String[] args) {

        System.out.println("===================START APP======================");
        System.out.println("================START MIGRATION===================");
        FlywayService flywayService = new FlywayService();
        flywayService.migrate();

        System.out.println("============CREATE CONNECTION POOL================");
        JdbcConnectionPool jdbcConnectionPool = JdbcConnectionPool.create(H2_URL, H2_USER, H2_PSW);

        System.out.println("=================USER_TASK=============");
        UserTaskRepository roleRepository = new JDBCUserTaskRepositoryImpl(jdbcConnectionPool);
//        System.out.println("SELECT_ALL" + roleRepository.selectAll());

        UserRepository userRepository = new JDBCUserRepositoryImpl(jdbcConnectionPool);
        User user = userRepository.selectById(1);

        TaskRepository taskRepository = new JDBCTaskRepositoryImpl(jdbcConnectionPool);
        Task task = taskRepository.selectById(3);

//        System.out.println("SELECT_BY_USERTASK" + roleRepository.selectByUserTask(user,task));

        RoleRepository role1 = new JDBCRoleRepositoryImpl(jdbcConnectionPool);
        Role role = role1.selectById(2);

        StatusRepository statusRepository = new JDBCStatusRepositoryImpl(jdbcConnectionPool);
        Status status = statusRepository.selectById(1);

        User user1 = new User();
        user1.setLogin("OLGA");
        user1.setPsw(123);
        user1.setLastName("OLGA");
        user1.setFirstName("OLGA!");
        user1.setRole(role);
        userRepository.add(user1);

        Task task1 = new Task();
        task1.setTitle("QQQQQ");
        task1.setStatus(status);
        task1.setDedline(LocalDate.now());
        task1.setInfo("info");
        taskRepository.add(task1);

        UserTask userTask1 = new UserTask();
        userTask1.setUser(user1);
        userTask1.setTask(task1);
//
//        System.out.println(userTask1);
//
//        System.out.println("ADD" + roleRepository.add(userTask1));
//        System.out.println("SELECT_ALL" + roleRepository.selectAll());
//
        UserTask userTask2 = new UserTask();
        userTask2.setUser(user1);
        userTask2.setTask(task);
//
//        System.out.println(userTask2);

        UserTask userTask3 = new UserTask();
        userTask3.setUser(user);
        userTask3.setTask(task1);

//        System.out.println(userTask3);
//
//        System.out.println("ADD_ALL" + roleRepository.addAll(List.of(userTask2, userTask3)));
//        System.out.println("SELECT_ALL" + roleRepository.selectAll());

        UserTask userTask4 = new UserTask();
        userTask4.setUser(userRepository.selectById(8));
        userTask4.setTask(taskRepository.selectById(4));

        System.out.println("User TASK4 " + userTask4);
//        System.out.println("task1 "+task1);
//        System.out.println("User "+user);
//        System.out.println("UT  "+ roleRepository.selectByUserTask(user,task1));

//
        System.out.println("UPDATE" + roleRepository.update(taskRepository.selectById(3), userRepository.selectById(8), userTask4));
        System.out.println("SELECT_ALL" + roleRepository.selectAll());
//
        System.out.println("REMOVE" + roleRepository.remove(taskRepository.selectById(4)));
        System.out.println("SELECT_ALL" + roleRepository.selectAll());

        System.out.println("SELECT TASK "+ taskRepository.selectAll());
//
//        UserRepository userRepository = new JDBCUserRepositoryImpl(jdbcConnectionPool);
//        System.out.println("SELECT_ALL_USERS" + userRepository.selectAll() + "\n");

        System.out.println("=========CLOSE ALL UNUSED CONNECTIONS=============");
        jdbcConnectionPool.dispose();
        System.out.println("=================SHUT DOWN APP====================");
    }
}
