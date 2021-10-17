package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.Status;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestCategoryTest {

    public Status createTestStatus() {
        Status status = new Status();
        status.setId(1);
        status.setStatusName("new");
        return status;
    }

    public Role createTestRole() {
        Role role = new Role();
        role.setRoleName("admin");
        role.setId(1);
        return role;
    }

    public List<User> createTestUsers(Integer count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setLogin("Test " + i);
            user.setPsw(123 + i);
            user.setRole(createTestRole());
            user.setLastName("Ivanov " + i);
            user.setFirstName("Ivan " + i);
            users.add(user);
        }
        return users;
    }

    public List<Task> createTestTasks(Integer count) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Task task = new Task();
            task.setTitle("Task test " + i);
            task.setStatus(createTestStatus());
            task.setInfo("Task test info " + i);
            task.setDedline(LocalDate.of(2001, 1, 1));

            tasks.add(task);
        }
        return tasks;
    }
}
