package com.itrex.java.lab.crm.repository;

import com.itrex.java.lab.crm.entity.Role;
import com.itrex.java.lab.crm.entity.Status;
import com.itrex.java.lab.crm.entity.Task;
import com.itrex.java.lab.crm.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepositoryTestUtils {

    private final static Role role = Role.builder().id(1).roleName("ADMIN").build();

    public static List<Role> createTestRole(Integer count) {
        List<Role> roles = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            roles.add(Role.builder()
                    .roleName("TEST" + (char) ('@' + (i + 1)))
                    .build());
        }
        return roles;
    }

    public static List<Role> createTestRoleWithId(int start, int count) {
        List<Role> roles = new ArrayList<>();
        for (int i = start; i < start + count; i++) {
            roles.add(Role.builder()
                    .id(i + 1)
                    .roleName("TEST" + (char) ('@' + (i + 1)))
                    .build());
        }
        return roles;
    }

    public static List<User> createTestUsers(Integer count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(User.builder()
                    .lastName("Ivanov" + (char) ('@' + (i + 1)))
                    .firstName("Ivan" + (char) ('@' + (i + 1)))
                    .login("Test" + i)
                    .psw("123" + i)
                    .role(role)
                    .build());
        }
        return users;
    }

    public static List<User> createTestUsersWithId(int start, int count) {
        List<User> users = new ArrayList<>();
        for (int i = start; i < start + count; i++) {
            users.add(User.builder()
                    .id(i + 1)
                    .lastName("Ivanov" + (char) ('@' + (i + 1)))
                    .firstName("Ivan" + (char) ('@' + (i + 1)))
                    .login("Test" + i)
                    .psw("123" + i)
                    .role(role)
                    .build());
        }
        return users;
    }

    public static List<Task> createTestTasks(Integer count) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tasks.add(Task.builder()
                    .title("Task test " + i)
                    .status(Status.NEW)
                    .info("Task test info " + i)
                    .deadline(LocalDate.of(2001, 1, 1))
                    .build());
        }
        return tasks;
    }

    public static List<Task> createTestTasksWithId(int start, int count) {
        List<Task> tasks = new ArrayList<>();
        for (int i = start; i < start + count; i++) {
            tasks.add(Task.builder()
                    .id(i + 1)
                    .title("Task test " + i)
                    .status(Status.NEW)
                    .info("Task test info " + i)
                    .deadline(LocalDate.of(2001, 1, 1))
                    .build());
        }
        return tasks;
    }

}
