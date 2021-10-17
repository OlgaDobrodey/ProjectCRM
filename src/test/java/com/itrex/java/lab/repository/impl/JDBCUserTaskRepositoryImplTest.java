package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.UserTask;
import com.itrex.java.lab.repository.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

//
//    List<UserTask> selectAll();
//    //  UserTask selectByUserAndTask(Task task, User user);
//    UserTask add(UserTask userTask);
//    List<UserTask> addAll(List<UserTask> userTasks);
//    UserTask update(Task task, User user, UserTask userTask);
//    boolean remove(Task task);

    @Test
    public void selectAll_validData_shouldReturnExistUserTaskTest() {
        //given && when
        final List<UserTask> result = repository.selectAll();

        //then
        assertFalse(result.isEmpty());
    }

    //    @Test
//    void add_validData_receiveUser_shouldReturnExistUserTest() {
//        //given
//        User user = new User();
//        user.setLogin("Test");
//        user.setPsw(123);
//
//        Role role = new Role();
//        role.setId(1);
//        role.setRoleName("user");
//        user.setRole(role);
//
//        user.setLastName("Ivanov");
//        user.setFirstName("Ivan");
//
//        User expected = new User();
//        expected.setId(repository.selectAll().size() + 1);
//        expected.setLogin(user.getLogin());
//        expected.setPsw(user.getPsw());
//        expected.setRole(user.getRole());
//        expected.setFirstName(user.getFirstName());
//        expected.setLastName(user.getLastName());
//
//        //when
//        User actual = repository.add(user);
//
//        //then
//        assertEquals(expected, actual);
//    }
    @Test
    void update_validData_receiveUserTaskAndUserAndTask_shouldReturnExistUserTest() {
        //given
        UserTask expected = testCategoryTest.createUserTasks(1).get(0);

        User user = userRepository.selectById(1);
        Task task = taskRepository.selectById(2);

        //when
        UserTask actual = repository.update(task, user, expected);

        //then
        assertEquals(expected, actual);
    }

//    @Test
//    void removeUser_validData_receiveInteger_shouldReturnExistBooleanTest() {
//        //given
//        testCategoryTest.createTestTasks(1).
//
//        //when
//        Boolean actual = repository.remove(repository);
//
//        //then
//        assertTrue(actual);
//    }
}
