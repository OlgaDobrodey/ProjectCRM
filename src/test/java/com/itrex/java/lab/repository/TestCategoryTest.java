package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.*;
import com.itrex.java.lab.repository.impl.JDBCRoleRepositoryImpl;
import com.itrex.java.lab.repository.impl.JDBCStatusRepositoryImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestCategoryTest extends BaseRepositoryTest {

    public Status createTestStatusInDataBase() {
        return new JDBCStatusRepositoryImpl(getConnectionPool()).selectById(1);
    }

    public Role createTestRoleInDataBase() {
        return new JDBCRoleRepositoryImpl(getConnectionPool()).selectById(1);
    }

    public List<User> createTestUsers(Integer count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setLogin("Test " + i);
            user.setPsw(123 + i);
            user.setRole(createTestRoleInDataBase());
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
            task.setStatus(createTestStatusInDataBase());
            task.setInfo("Task test info " + i);
            task.setDedline(LocalDate.now());

            tasks.add(task);
        }
        return tasks;
    }

    public List<UserTask> createUserTasks(Integer count) {
        List<UserTask> userTasks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            UserTask userTask = new UserTask();
            userTask.setTask(createTestTasks(1).get(0));
            userTask.setUser(createTestUsers(1).get(0));
            userTask.setInfo("User task info " + i);
            userTasks.add(userTask);
        }

        return userTasks;
    }
}
