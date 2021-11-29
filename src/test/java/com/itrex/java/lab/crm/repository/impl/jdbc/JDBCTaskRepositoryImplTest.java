package com.itrex.java.lab.crm.repository.impl.jdbc;

import com.itrex.java.lab.crm.entity.Status;
import com.itrex.java.lab.crm.entity.Task;
import com.itrex.java.lab.crm.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.crm.repository.RepositoryTestUtils;
import com.itrex.java.lab.crm.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JDBCTaskRepositoryImplTest extends BaseJDBCRepositoryTest {

    @Autowired
    @Qualifier(value = "JDBCTaskRepository")
    private TaskRepository repository;

    @Test
    public void selectAll_validData_returnTaskTest() throws CRMProjectRepositoryException {
        //given && when
        final List<Task> result = repository.selectAll();

        //then
        assertFalse(result.isEmpty());
    }

//    @Test
//    void selectAll_shouldThrowRepositoryExceptionTest() {
//        //given && when
//        cleanDB();    //clean Data Base
//
//        //then
//        assertThrows(CRMProjectRepositoryException.class, () -> repository.selectAll());
//    }

    @Test
    void selectById_validData_existTaskID_returnTaskTest() throws CRMProjectRepositoryException {
        //given
        Integer taskId = 2;

        //when
        Task actual = repository.selectById(taskId);

        //then
        assertEquals(2, actual.getId());
        assertEquals("task title 2", actual.getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.getStatus());
        assertEquals(LocalDate.of(2022, 12, 23), actual.getDeadline());
        assertEquals("task info 2", actual.getInfo());
    }

    @Test
    void selectById_validData_existTaskIdNonDataBase_returnNULLTest() throws CRMProjectRepositoryException {
        //given
        Integer taskId = 28;

        //when
        Task actual = repository.selectById(taskId);

        //then
        assertNull(actual);
    }

//    @Test
//    void selectById_returnThrowRepositoryExceptionTest() {
//        //given && when
//        cleanDB();    //clean Data Base
//        Integer taskId = 2;
//
//        //then
//        assertThrows(CRMProjectRepositoryException.class, () -> repository.selectById(taskId));
//    }

    @Test
    void selectAllTaskByUserId_existUser_returnListOfTaskTest() throws CRMProjectRepositoryException {
        //given
        Integer id = 2;

        //when
        List<Task> actual = repository.selectAllTasksByUserId(id);

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
        assertEquals(Status.DONE, actual.get(2).getStatus());
        assertEquals(LocalDate.of(2012, 10, 23), actual.get(2).getDeadline());
        assertEquals("task info 11", actual.get(2).getInfo());
    }

    @Test
    void add_validData_existTask_returnExistTaskTest() throws CRMProjectRepositoryException {
        //given
        Task task = RepositoryTestUtils.createTestTasks(1).get(0);

        //when
        Task actual = repository.add(task);

        //then
        assertEquals("Task test 0", actual.getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.getStatus());
        assertEquals(LocalDate.of(2001, 1, 1), actual.getDeadline());
        assertEquals("Task test info 0", actual.getInfo());
    }

    @Test
    void add_existCopyTaskWithId2_returnThrowRepositoryExceptionTypeTest() throws CRMProjectRepositoryException {
        //given && when
        Task task = repository.selectById(2);

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.add(task));
    }

    @Test
    void update_validData_existTask_returnTaskTest() throws CRMProjectRepositoryException {
        //given
        Task expected = RepositoryTestUtils.createTestTasksWithId(0, 1).get(0);

        //when
        Task actual = repository.update(expected);

        //then
        assertEquals(1, actual.getId());
        assertEquals("Task test 0", actual.getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.getStatus());
        assertEquals(LocalDate.of(2001, 1, 1), actual.getDeadline());
        assertEquals("Task test info 0", actual.getInfo());
    }

//    @Test
//    void update_existTaskId_returnThrowRepositoryExceptionTest() {
//        //given && when
//        cleanDB();    //clean Data Base
//        Task expected = RepositoryTestUtils.createTestTasks(1).get(0);
//
//        //then
//        assertThrows(CRMProjectRepositoryException.class, () -> repository.update(expected));
//    }

    @Test
    void remove_validData_existTaskID_Test() throws CRMProjectRepositoryException {
        //given&&when
        repository.remove(20);

        //then
        assertEquals(19, repository.selectAll().size());
    }

    @Test
    void remove_validData_existTask_returnFalseTest() {
        //given && when && then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.remove(25));
    }

}