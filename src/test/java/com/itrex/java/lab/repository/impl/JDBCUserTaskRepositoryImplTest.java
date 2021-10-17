package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.UserTask;
import com.itrex.java.lab.repository.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JDBCUserTaskRepositoryImplTest extends BaseRepositoryTest {

    private TestCategoryTest testCategoryTest;
    private final UserTaskRepository repository;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    public JDBCUserTaskRepositoryImplTest() {
        super();
        repository = new JDBCUserTaskRepositoryImpl(getConnectionPool());
    }

    @BeforeAll
    private void createCategory() {
        testCategoryTest = new TestCategoryTest();
        userRepository = new JDBCUserRepositoryImpl(getConnectionPool());
        taskRepository = new JDBCTaskRepositoryImpl(getConnectionPool());
    }

    @Test
    public void selectAll_validData_shouldReturnExistUserTaskTest() {
        //given && when
        final List<UserTask> result = repository.selectAll();

        //then
        assertFalse(result.isEmpty());
    }

    @Test
    void selectByUserTask_validData_receiveUserAndTask_shouldReturnExistUserTaskTest() {
        //given
        User user = userRepository.selectById(2);
        Task task = taskRepository.selectById(5);

        //when
        UserTask actual = repository.selectByUserTask(user,task);
        UserTask expected = new UserTask();
        expected.setUser(user);
        expected.setTask(task);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void add_validData_receiveUser_shouldReturnExistUserTest() {
        //given
        User user = userRepository.add(testCategoryTest.createTestUsers(1).get(0));
        Task task = taskRepository.add(testCategoryTest.createTestTasks(1).get(0));
        UserTask userTask = new UserTask();
        userTask.setUser(user);
        userTask.setTask(task);
        userTask.setInfo("info test");

        UserTask expected = new UserTask();
        expected.setTask(userTask.getTask());
        expected.setUser(userTask.getUser());
        expected.setInfo(userTask.getInfo());

        //when
        UserTask actual = repository.add(userTask);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void addAll_validData_receiveUser_shouldReturnExistUserTest() {
        //given
        List<Task> tasks = testCategoryTest.createTestTasks(2);
        taskRepository.addAll(tasks);
        List<User> users = testCategoryTest.createTestUsers(1);
        User testUser = userRepository.add(users.get(0));

        UserTask test1 = new UserTask();
        test1.setTask(tasks.get(0));
        test1.setUser(testUser);
        test1.setInfo("test1");

        UserTask test2 = new UserTask();
        test2.setTask(tasks.get(1));
        test2.setUser(testUser);
        test2.setInfo("test2");

        UserTask result1 = new UserTask();
        result1.setTask(test1.getTask());
        result1.setUser(test1.getUser());
        result1.setInfo(test1.getInfo());

        UserTask result2 = new UserTask();
        result2.setTask(test2.getTask());
        result2.setUser(test2.getUser());
        result2.setInfo(test2.getInfo());

        //when
        List<UserTask> expected = List.of(result1, result2);
        List<UserTask> actual = repository.addAll(List.of(test1, test2));

        //then
        assertEquals(expected, actual);
    }

    @Test
    void update_validData_receiveUserTaskAndUserAndTask_shouldReturnExistUserTest() {
        //given
        User user = userRepository.selectById(1);
        Task task = taskRepository.selectById(2);

        UserTask expected = new UserTask();
        expected.setUser(userRepository.selectById(3));
        expected.setTask(taskRepository.selectById(2));
        expected.setInfo("info test");

        //when
        UserTask actual = repository.update(task, user, expected);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void removeUser_validData_receiveInteger_shouldReturnExistBooleanTest() {
        //given
        Task testTasks = taskRepository.selectById(1);

        //when
        Boolean actual = repository.remove(testTasks);

        //then
        assertTrue(actual);
    }
}
