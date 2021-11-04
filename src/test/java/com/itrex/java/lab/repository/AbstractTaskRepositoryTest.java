package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Status;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractTaskRepositoryTest extends BaseRepositoryTest {

    private TaskRepository repository;
    private UserRepository userRepository;

    public AbstractTaskRepositoryTest() {
        super();
    }

    public void postConstruct(TaskRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Test
    public void selectAll_validData_returnTaskTest() throws CRMProjectRepositoryException {
        //given && when
        final List<Task> result = repository.selectAll();

        //then
        assertFalse(result.isEmpty());
    }

    @Test
    void selectAll_shouldThrowRepositoryExceptionTest() {
        //given && when
        cleanDB();    //clean Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.selectAll());
    }

    @Test
    void selectById_validData_existTaskID_returnTaskTest() throws CRMProjectRepositoryException {
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
    void selectById_validData_existIDTaskNonDataBase_returnNULLTest() throws CRMProjectRepositoryException {
        //given
        Integer idTask = 28;

        //when
        Task actual = repository.selectById(idTask);

        //then
        assertNull(actual);
    }

    @Test
    void selectById_returnThrowRepositoryExceptionTest() {
        //given && when
        cleanDB();    //clean Data Base
        Integer idTask = 2;

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.selectById(idTask));
    }

    @Test
    void selectAllUsersByTask_existTask_returnListOfUserTest() throws CRMProjectRepositoryException {
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
    void add_validData_existTask_returnExistTaskTest() throws CRMProjectRepositoryException {
        //given
        Task task = RepositoryTestUtils.createTestTasks(1).get(0);

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
    void add_existCopyTaskWithId2_returnThrowRepositoryExceptionTypeTest() throws CRMProjectRepositoryException {
        //given && when
        Task task = repository.selectById(2);  //There is Role "User" in Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.add(task));
    }

    @Test
    void addAll_validData_existTask_returnTaskTest() throws CRMProjectRepositoryException {
        //given
        List<Task> testTasks = RepositoryTestUtils.createTestTasks(2);

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
    void addAll_existCopyTaskById2_returnThrowRepositoryExceptionTypeTest() throws CRMProjectRepositoryException {
        //given && when
        Task test1 = RepositoryTestUtils.createTestTasks(1).get(0);
        Task test2 = repository.selectById(2);  //There is Role "User" in Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.addAll(List.of(test1, test2)));
    }

    @Test
    void update_validData_existTaskAndInteger_returnTaskTest() throws CRMProjectRepositoryException {
        //given
        Task expected = RepositoryTestUtils.createTestTasks(1).get(0);
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
    void update_validData_existTaskAndIdTaskNotDB_returnNullTest() throws CRMProjectRepositoryException {
        //given && when
        Task expected = RepositoryTestUtils.createTestTasks(1).get(0);
        Integer idNonDataBase = 99;

        //when
        Task actual = repository.update(expected, idNonDataBase);

        //then
        assertNull(actual);
    }

    @Test
    void update_existIdTask_returnThrowRepositoryExceptionTest() {
        //given && when
        cleanDB();    //clean Data Base
        Task expected = RepositoryTestUtils.createTestTasks(1).get(0);
        Integer id = 1;

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.update(expected, id));
    }

    @Test
    void remove_validData_existTask_returnTRUETest() throws CRMProjectRepositoryException {
        //given
        Task task = repository.selectById(1);

        //when
        boolean actual = repository.remove(task);

        //then
        assertTrue(actual);
    }

    @Test
    void remove_validData_existTask_returnFalseTest() throws CRMProjectRepositoryException {
        //given
        Task task = RepositoryTestUtils.createTestTasks(1).get(0);
        task.setId(25);

        //when
        boolean actual = repository.remove(task);

        //then
        assertFalse(actual);
    }

    @Test
    void removeTaskByUser_existUserAndTaskNonDB_shouldReturnTrueTest() throws CRMProjectRepositoryException {
        //given
        User user = userRepository.selectById(1);
        Task taskTrue = repository.selectById(2);   //found on DataBase

        //when
        boolean actualTrue = repository.removeUserByTask(taskTrue, user);

        //then
        assertTrue(actualTrue);
    }

    @Test
    void removeTaskByUser_existUserAndTask_shouldReturnFalseTest() throws CRMProjectRepositoryException {
        //given
        User user = userRepository.selectById(1);
        Task taskFalse = repository.selectById(4);  //no found on DataBase

        //when
        boolean actualFalse = repository.removeUserByTask(taskFalse, user);

        //then
        assertFalse(actualFalse);
    }

    @Test
    void remove_existTask_shouldReturnThrowRepositoryExceptionTest() {
        //given && when
        cleanDB();    //clean Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.remove(repository.selectById(1)));
    }
}
