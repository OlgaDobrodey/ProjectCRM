package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Status;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.TestCategoryTest;
import com.itrex.java.lab.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JDBCTaskRepositoryImplTest extends BaseRepositoryTest {

    private final TaskRepository repository;
    private final UserRepository userRepository;

    public JDBCTaskRepositoryImplTest() {
        super();
        repository = new JDBCTaskRepositoryImpl(getConnectionPool());
        userRepository = new JDBCUserRepositoryImpl(getConnectionPool());
    }

    @Test
    public void selectAll_validData_shouldReturnExistTaskTest() throws SQLException {
        //given && when
        final List<Task> result = repository.selectAll();

        //then
        assertFalse(result.isEmpty());
    }

    @Test
    void selectById_validData_receiveInteger_shouldReturnExistTaskTest() throws SQLException {
        //given
        Integer idRole = 2;

        //when
        Task actual = repository.selectById(idRole);

        //then
        assertEquals(2, actual.getId());
        assertEquals("task title 2", actual.getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.getStatus());
        assertEquals(LocalDate.of(2022, 12, 23), actual.getDeadline());
        assertEquals("task info 2", actual.getInfo());
    }

    @Test
    void selectAllUsersByTask_receiverTask_shouldReturnListOfUser() throws SQLException{
        //given
        Task task = repository.selectById(2);

        //when
        List<User> actual = repository.selectAllUsersByTask(task);

        //then
        assertEquals(1, actual.get(0).getId());
        assertEquals("Petrov", actual.get(0).getLogin());
        assertEquals("123", actual.get(0).getPsw());
        assertEquals("ADMIN", actual.get(0).getRole().getRoleName());
        assertEquals("Petrov", actual.get(0).getLastName());
        assertEquals("Petr", actual.get(0).getFirstName());
        assertEquals(8, actual.get(1).getId());
        assertEquals("Dropalo", actual.get(1).getLogin());
        assertEquals("123", actual.get(1).getPsw());
        assertEquals("USER", actual.get(1).getRole().getRoleName());
        assertEquals("Dropalo", actual.get(1).getLastName());
        assertEquals("Andrey", actual.get(1).getFirstName());

    }

    @Test
    void add_validData_receiveTask_shouldReturnExistTaskTest() throws SQLException {
        //given
        Task task = TestCategoryTest.createTestTasks(1).get(0);

        //when
        Task actual = repository.add(task);

        //then
        assertEquals(21, actual.getId());
        assertEquals("Task test 0", actual.getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.getStatus());
        assertEquals(LocalDate.of(2001, 1, 1), actual.getDeadline());
        assertEquals("Task test info 0", actual.getInfo());
    }

    @Test
    void addAll_validData_receiveTask_shouldReturnExistTaskTest() throws SQLException {
        //given
        List<Task> testTasks = TestCategoryTest.createTestTasks(2);

        //when
        List<Task> actual = repository.addAll(testTasks);

        //then
        assertEquals(21, actual.get(0).getId());
        assertEquals("Task test 0", actual.get(0).getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.get(0).getStatus());
        assertEquals(LocalDate.of(2001, 1, 1), actual.get(0).getDeadline());
        assertEquals("Task test info 0", actual.get(0).getInfo());
        assertEquals(22, actual.get(1).getId());
        assertEquals("Task test 1", actual.get(1).getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.get(1).getStatus());
        assertEquals(LocalDate.of(2001, 1, 1), actual.get(1).getDeadline());
        assertEquals("Task test info 1", actual.get(1).getInfo());
    }

    @Test
    void update_validData_receiveTaskAndInteger_shouldReturnExistTaskTest() {
        //given
        Task expected = TestCategoryTest.createTestTasks(1).get(0);
        Integer testId = 1;

        //when
        Task actual = repository.update(expected, 1);

        //then
        assertEquals(1, actual.getId());
        assertEquals("Task test 0", actual.getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.getStatus());
        assertEquals(LocalDate.of(2001, 1, 1), actual.getDeadline());
        assertEquals("Task test info 0", actual.getInfo());
    }

    @Test
    void removeTask_validData_receiveTask_shouldReturnExistBooleanTest() throws SQLException {
        //given
        Task task = repository.selectById(1);

//        when
        Boolean actual = repository.remove(task);

        //then
        assertTrue(actual);
    }

    @Test
    void removeTaskByUser_validData_receiveUserAndTask_shouldReturnBooleanTest() throws SQLException {
        //given
        User user = userRepository.selectById(1);
        Task taskTrue = repository.selectById(2);   //found on DataBase
        Task taskFalse = repository.selectById(4);  //no found on DataBase

        //when
        Boolean actualTrue= repository.removeUserByTask(taskTrue,user);
        Boolean actualFalse= repository.removeUserByTask(taskFalse,user);

        //then
        assertTrue(actualTrue);
        assertFalse(actualFalse);
    }
}