package com.itrex.java.lab.repository.impl.jdbc;

import com.itrex.java.lab.entity.Status;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.repository.UtillCategory;
import org.junit.jupiter.api.Test;

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
    public void selectAll_validData_returnExistUserTest() throws CRMProjectRepositoryException {
        //given && when
        final List<User> result = repository.selectAll();

        //then
        assertFalse(result.isEmpty());
    }

    @Test
    void selectAll_shouldThrowRepositoryExceptionTest() {
        //given && when
        cleanDB();    //clean Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> {
            repository.selectAll();
        });
    }

    @Test
    void selectById_validData_existUserId_returnUserTest() throws CRMProjectRepositoryException {
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
    void selectById_validData_existIDUsereNotDataBase_returnNULLTest() throws CRMProjectRepositoryException {
        //given
        Integer idUser = 28;

        //when
        User actual = repository.selectById(idUser);

        //then
        assertNull(actual);
    }

    @Test
    void selectById_shouldThrowRepositoryExceptionTest() {
        //given && when
        cleanDB();    //clean Data Base
        Integer idUser = 2;

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> {
            repository.selectById(idUser);
        });
    }

    @Test
    void selectAllTaskByUser_existUser_returnListOfTaskTest() throws CRMProjectRepositoryException {
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
    void add_validData_existUser_returnUserTest() throws CRMProjectRepositoryException {
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
    void add_existCopyUSERId2_returnThrowRepositoryExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        User user = repository.selectById(2);  //There is Role "User" in Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> {
            repository.add(user);
        });
    }

    @Test
    void addAll_validData_existUser_returnUserTest() throws CRMProjectRepositoryException {
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
    void addAll_existCopyUserById2_shouldThrowRepositoryExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        User test1 = UtillCategory.createTestUsers(1).get(0);
        User test2 = repository.selectById(2);  //There is Role "User" in Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> {
            repository.addAll(List.of(test1, test2));
        });
    }

    @Test
    void update_validData_existUserAndInteger_returnUserTest() throws CRMProjectRepositoryException {
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
    void update_validData_existUserAndIdUserNotDB_returnNullTest() throws CRMProjectRepositoryException {
        //given && when
        User expected = UtillCategory.createTestUsers(1).get(0);
        Integer idNonDataBase = 99;

        //when
        User actual = repository.update(expected, idNonDataBase);

        //then
        assertNull(actual);
    }

    @Test
    void update_existIdUser_shouldThrowRepositoryExceptionTest() {
        //given && when
        cleanDB();    //clean Data Base
        User expected = UtillCategory.createTestUsers(1).get(0);
        Integer id = 1;

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> {
            repository.update(expected, id);
        });
    }

    @Test
    void remove_validData_existUser_shouldReturnExistTrueTest() throws CRMProjectRepositoryException {
        //given
        User user = repository.selectById(1);

        //when
        Boolean actual = repository.remove(user);

        //then
        assertTrue(true);
    }

    @Test
    void removeTaskByUser_validData_existUserAndTask_shouldReturnTrueTest() throws CRMProjectRepositoryException {
        //given
        User user = repository.selectById(1);
        Task taskTrue = taskRepository.selectById(2);   //found on DataBase

        //when
        Boolean actualTrue = repository.removeTaskByUser(taskTrue, user);

        //then
        assertTrue(actualTrue);
    }

    @Test
    void removeTaskByUser_validData_existUserAndTaskNonDB_shouldReturnFalseTest() throws CRMProjectRepositoryException {
        //given
        User user = repository.selectById(1);
        Task taskFalse = taskRepository.selectById(4);  //no found on DataBase

        //when
        Boolean actualFalse = repository.removeTaskByUser(taskFalse, user);

        //then
        assertFalse(actualFalse);
    }

    @Test
    void remove_existUser_shouldThrowRepositoryExceptionTest() {
        //given && when
        cleanDB();    //clean Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> {
            repository.remove(repository.selectById(1));
        });
    }
}
