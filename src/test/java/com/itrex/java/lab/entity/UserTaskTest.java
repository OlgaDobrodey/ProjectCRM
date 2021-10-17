package com.itrex.java.lab.entity;

import com.itrex.java.lab.repository.TestCategoryTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTaskTest {

    @Test
    void toString_Entity_shouldReturnExistStringTest() {
        String expected = "\nUserTask{user=1, task=1, info='null'}";

        TestCategoryTest testCategoryTest = new TestCategoryTest();
        Task task = testCategoryTest.createTestTasks(1).get(0);
        task.setId(1);
        User user = testCategoryTest.createTestUsers(1).get(0);
        user.setId(1);

        UserTask userTask = new UserTask();
        userTask.setTask(task);
        userTask.setUser(user);

        assertEquals(expected, userTask.toString());
    }
}