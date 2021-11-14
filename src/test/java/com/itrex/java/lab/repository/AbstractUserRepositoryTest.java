package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Status;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractUserRepositoryTest extends BaseRepositoryTest {

    private UserRepository repository;
    private TaskRepository taskRepository;

    public AbstractUserRepositoryTest() {
        super();
    }

    public void postConstruct(UserRepository repository, TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
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
        assertThrows(CRMProjectRepositoryException.class, () -> repository.selectAll());
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
        assertThrows(CRMProjectRepositoryException.class, () -> repository.selectById(idUser));
    }

    @Test
    void selectAllTaskByUser_existUser_returnListOfTaskTest() throws CRMProjectRepositoryException {
        //given
        Integer idUser = 2;

        //when
        List<Task> actual = repository.selectAllTasksByUser(idUser);

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
        User user = RepositoryTestUtils.createTestUsers(1).get(0);

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
        assertThrows(CRMProjectRepositoryException.class, () -> repository.add(user));
    }

    @Test
    void addAll_validData_existUser_returnUserTest() throws CRMProjectRepositoryException {
        //given
        List<User> testUsers = RepositoryTestUtils.createTestUsers(2);

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
        User test1 = RepositoryTestUtils.createTestUsers(1).get(0);
        User test2 = repository.selectById(2);  //There is Role "User" in Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.addAll(List.of(test1, test2)));
    }

    @Test
    void addTaskByUser_validData_existUserAndTaskTest() throws CRMProjectRepositoryException {
        //given
        Integer idUser = 2;
        Task task = taskRepository.selectById(6);

        //when
        repository.addTaskByUser(task.getId(), idUser);

        //then
        assertTrue(repository.selectAllTasksByUser(idUser).contains(task));
    }

    @Test
    void update_validData_existUser_returnUserTest() throws CRMProjectRepositoryException {
        //given
        User expected = RepositoryTestUtils.createTestUsers(1).get(0);
        expected.setId(1);

        //when
        User actual = repository.update(expected);

        //then
        assertEquals(1, actual.getId());
        assertEquals("Test 0", actual.getLogin());
        assertEquals("1230", actual.getPsw());
        assertEquals("ADMIN", actual.getRole().getRoleName());
        assertEquals("Ivanov 0", actual.getLastName());
        assertEquals("Ivan 0", actual.getFirstName());
    }

    @Test
    void update_validData_existUserNotDB_shouldThrowRepositoryExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        User expected = RepositoryTestUtils.createTestUsers(1).get(0);
        expected.setId(99);

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.update(expected));
    }

    @Test
    void update_existIdUser_shouldThrowRepositoryExceptionTest() {
        //given && when
        cleanDB();    //clean Data Base
        User expected = RepositoryTestUtils.createTestUsers(1).get(0);
        Integer id = 1;

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.update(expected));
    }

    @Test
    void remove_validData_existUser_Test() throws CRMProjectRepositoryException {
        //given
        User user = repository.selectById(1);

        //when
        repository.remove(1);

        //then
        assertEquals(9, repository.selectAll().size());
    }

    @Test
    void removeTaskByUser_validData_existUserIdAndTaskIdTest() throws CRMProjectRepositoryException {
        //given
        User user = repository.selectById(1);
        Task taskTrue = taskRepository.selectById(2);
        List<Task> allTaskByUser = repository.selectAllTasksByUser(user.getId());

        //when
        repository.removeTaskByUser(taskTrue.getId(), user.getId());

        //then
        assertEquals(repository.selectAllTasksByUser(user.getId()).size() + 1, allTaskByUser.size());
    }

    @Test
    void removeTaskByUser_validData_existUserIDAndTaskIDNonDB_shouldReturnFalseTest() throws CRMProjectRepositoryException {
        //given && when && then
        assertThrows(CRMProjectRepositoryException.class,
                () -> repository.removeTaskByUser(2,repository.selectAll().size()+1));

    }

    @Test
    void remove_existUserId_shouldThrowRepositoryExceptionTest() {
        //given && when
        cleanDB();    //clean Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.remove(1));
    }

    @Test
    void remove_existUserIdNoDataBase_shouldThrowRepositoryExceptionTest() {
        //given && when
        int idUserNoDB = 18;

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.remove(idUserNoDB));
    }

    @Test
    void removeAllTasksByUser_existUserIDTest_() throws CRMProjectRepositoryException {

        //given
        Integer idUser = 2;

        //when
        repository.removeAllTasksByUser(idUser);

        //then
        assertEquals(0, repository.selectAllTasksByUser(idUser).size());
    }
}

