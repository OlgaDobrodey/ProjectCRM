package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Status;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.repository.UtillCategory;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JDBCUserRepositoryImplTest extends BaseRepositoryTest {

    private final UserRepository repository;
    private final TaskRepository taskRepository;

    public JDBCUserRepositoryImplTest() {
        super();
        repository = new JDBCUserRepositoryImpl(getConnectionPool());
        taskRepository = new JDBCTaskRepositoryImpl(getConnectionPool());
    }

    @Test
    public void selectAll_validData_shouldReturnExistUserTest() throws SQLException {
        //given && when
        final List<User> result = repository.selectAll();

        //then
        assertFalse(result.isEmpty());
    }

    @Test
    void selectById_validData_receiveInteger_shouldReturnExistUserTest() throws SQLException {
        //given
        Integer idUser = 2;

        //when
        User actual = repository.selectById(idUser);

        //then
        assertEquals(2, actual.getId());
        assertEquals("Ivanov", actual.getLogin());
        assertEquals("123", actual.getPsw());
        assertEquals("ADMIN", actual.getRole().getRoleName());
        assertEquals("Ivanov", actual.getLastName());
        assertEquals("Ivan", actual.getFirstName());
    }

    @Test
    void selectAllTaskByUser_receiverUser_shouldReturnListOfTask() throws SQLException {
        //given
        User user = repository.selectById(2);

        //when
        List<Task> actual = repository.selectAllTasksByUser(user);

        //then
        assertEquals(3, actual.size());
        assertEquals(5, actual.get(1).getId());
        assertEquals("task title 5", actual.get(1).getTitle(), "assert Title");
        assertEquals(Status.PROGRESS, actual.get(1).getStatus());
        assertEquals(LocalDate.of(2019, 10, 23), actual.get(1).getDeadline());
        assertEquals("task info 5", actual.get(1).getInfo());
        assertEquals(4, actual.get(0).getId());
        assertEquals("task title 4", actual.get(0).getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.get(0).getStatus());
        assertEquals(LocalDate.of(2022, 10, 23), actual.get(0).getDeadline());
        assertEquals("task info 4", actual.get(0).getInfo());
        assertEquals(11, actual.get(2).getId());
        assertEquals("task title 11", actual.get(2).getTitle(), "assert Title");
        assertEquals(Status.DELETED, actual.get(2).getStatus());
        assertEquals(LocalDate.of(2012, 10, 23), actual.get(2).getDeadline());
        assertEquals("task info 11", actual.get(2).getInfo());
    }

    @Test
    void add_validData_receiveUser_shouldReturnExistUserTest() throws SQLException {
        //given
        User user = UtillCategory.createTestUsers(1).get(0);

        //when
        User actual = repository.add(user);

        //then
        assertEquals(11, actual.getId());
        assertEquals("Test 0", actual.getLogin());
        assertEquals("1230", actual.getPsw());
        assertEquals("ADMIN", actual.getRole().getRoleName());
        assertEquals("Ivanov 0", actual.getLastName());
        assertEquals("Ivan 0", actual.getFirstName());
    }

    @Test
    void addAll_validData_receiveUser_shouldReturnExistUserTest() throws SQLException {
        //given
        List<User> testUsers = UtillCategory.createTestUsers(2);

        //when
        List<User> actual = repository.addAll(testUsers);

        //then
        assertEquals(11, actual.get(0).getId());
        assertEquals("Test 0", actual.get(0).getLogin());
        assertEquals("1230", actual.get(0).getPsw());
        assertEquals("ADMIN", actual.get(0).getRole().getRoleName());
        assertEquals("Ivanov 0", actual.get(0).getLastName());
        assertEquals("Ivan 0", actual.get(0).getFirstName());
        assertEquals(12, actual.get(1).getId());
        assertEquals("Test 1", actual.get(1).getLogin());
        assertEquals("1231", actual.get(1).getPsw());
        assertEquals("ADMIN", actual.get(1).getRole().getRoleName());
        assertEquals("Ivanov 1", actual.get(1).getLastName());
        assertEquals("Ivan 1", actual.get(1).getFirstName());
    }

    @Test
    void update_validData_receiveUserAndInteger_shouldReturnExistUserTest() throws SQLException {
        //given
        User expected = UtillCategory.createTestUsers(1).get(0);
        Integer testId = 1;

        //when
        User actual = repository.update(expected, 1);

        //then
        assertEquals(1, actual.getId());
        assertEquals("Test 0", actual.getLogin());
        assertEquals("1230", actual.getPsw());
        assertEquals("ADMIN", actual.getRole().getRoleName());
        assertEquals("Ivanov 0", actual.getLastName());
        assertEquals("Ivan 0", actual.getFirstName());
    }

    @Test
    void remove_validData_receiveUser_shouldReturnExistBooleanTest() throws SQLException {
        //given
        User user = repository.selectById(1);

        //when
        Boolean actual = repository.remove(user);

        //then
        assertTrue(true);
    }

    @Test
    void removeTaskByUser_validData_receiveUserAndTask_shouldReturnBooleanTest() throws SQLException {
        //given
        User user = repository.selectById(1);
        Task taskTrue = taskRepository.selectById(2);   //found on DataBase
        Task taskFalse = taskRepository.selectById(4);  //no found on DataBase

        //when
        Boolean actualTrue = repository.removeTaskByUser(taskTrue, user);
        Boolean actualFalse = repository.removeTaskByUser(taskFalse, user);

        //then
        assertTrue(actualTrue);
        assertFalse(actualFalse);
    }
}
