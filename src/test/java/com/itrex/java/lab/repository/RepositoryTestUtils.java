package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.Status;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepositoryTestUtils {

    public static List<Role> createTestRole(Integer count) {
        List<Role> roles = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Role role = new Role();
            role.setRoleName("TEST " + i);
            roles.add(role);
        }
        return roles;
    }

    public static List<User> createTestUsers(Integer count) {
        List<User> users = new ArrayList<>();
        Role role = new Role();
        role.setId(1);
        role.setRoleName("ADMIN");
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setLogin("Test " + i);
            user.setPsw("123" + i);
            user.setRole(role);
            user.setLastName("Ivanov " + i);
            user.setFirstName("Ivan " + i);
            users.add(user);
        }
        return users;
    }

    public static List<User> createTestUsersWithId(int start, int count) {
        List<User> users = new ArrayList<>();
        Role role = new Role();
        role.setId(1);
        role.setRoleName("ADMIN");
        for (int i = start; i < start+count; i++) {
            User user = new User();
            user.setId(i+1);
            user.setLogin("Test " + i);
            user.setPsw("123" + i);
            user.setRole(role);
            user.setLastName("Ivanov " + i);
            user.setFirstName("Ivan " + i);
            users.add(user);
        }
        return users;
    }

    public static List<Task> createTestTasks(Integer count) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Task task = new Task();
            task.setTitle("Task test " + i);
            task.setStatus(Status.NEW);
            task.setInfo("Task test info " + i);
            task.setDeadline(LocalDate.of(2001, 1, 1));

            tasks.add(task);
        }
        return tasks;
    }
    public static List<Task> createTestTasksWithId(int start,int count) {
        List<Task> tasks = new ArrayList<>();
        for (int i = start; i < start+count; i++) {
            Task task = new Task();
            task.setId(i+1);
            task.setTitle("Task test " + i);
            task.setStatus(Status.NEW);
            task.setInfo("Task test info " + i);
            task.setDeadline(LocalDate.of(2001, 1, 1));

            tasks.add(task);
        }
        return tasks;
    }
}
