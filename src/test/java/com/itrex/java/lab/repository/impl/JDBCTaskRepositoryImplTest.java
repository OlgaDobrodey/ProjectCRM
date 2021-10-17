package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Status;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.TaskRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JDBCTaskRepositoryImplTest extends BaseRepositoryTest {

    private final TaskRepository repository;

    public JDBCTaskRepositoryImplTest() {
        super();
        repository = new JDBCTaskRepositoryImpl(getConnectionPool());
    }

    @Test
    public void selectAll_validData_shouldReturnExistTaskTest() {
        //given && when
        final List<Task> result = repository.selectAll();

        //then
        assertFalse(result.isEmpty());
    }

    @Test
    void selectById_validData_receiveInteger_shouldReturnExistTaskTest() {
        //given
        Integer idRole = 2;

        //when
        Task actual = repository.selectById(idRole);
        Task expected = new Task();
        expected.setId(2);
        expected.setTitle("Prepare for implementation");

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        expected.setDedline(LocalDate.parse("2022-12-23", format));

        Status status = new Status();
        status.setId(1);
        status.setStatusName("new");

        expected.setStatus(status);
        expected.setInfo("Identify production LPARs.");

        //then
        assertEquals(expected, actual);
    }

    @Test
    void add_validData_receiveTask_shouldReturnExistTaskTest() {
        //given
        Task task = new Task();
        task.setTitle("test");

        Status status = new Status();
        status.setId(1);
        status.setStatusName("new");
        task.setStatus(status);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        task.setDedline(LocalDate.parse("2022-12-23", format));

        task.setInfo("info task");

        Task expected = new Task();
        expected.setId(repository.selectAll().size() + 1);
        expected.setTitle(task.getTitle());
        expected.setDedline(task.getDedline());
        expected.setStatus(task.getStatus());
        expected.setInfo(task.getInfo());

        //when
        Task actual = repository.add(task);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void addAll_validData_receiveTask_shouldReturnExistTaskTest() {
        //given
        Task test1 = new Task();
        test1.setTitle("test");

        Status status = new Status();
        status.setId(1);
        status.setStatusName("new");
        test1.setStatus(status);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        test1.setDedline(LocalDate.parse("2022-12-23", format));

        test1.setInfo("info task");

        Task test2 = new Task();
        test2.setTitle("test2");
        test2.setStatus(status);
        test2.setDedline(LocalDate.parse("2022-12-23", format));
        test2.setInfo("info task");

        Integer countSelectAllTask = repository.selectAll().size();
        Task result1 = new Task();
        result1.setTitle(test1.getTitle());
        result1.setDedline(test1.getDedline());
        result1.setStatus(test1.getStatus());
        result1.setInfo(test1.getInfo());

        result1.setId(countSelectAllTask + 1);

        Task result2 = new Task();
        result2.setTitle(test2.getTitle());
        result2.setDedline(test2.getDedline());
        result2.setStatus(test2.getStatus());
        result2.setInfo(test2.getInfo());

        result2.setId(countSelectAllTask + 2);

        //when
        List<Task> expected = List.of(result1, result2);
        List<Task> actual = repository.addAll(List.of(test1, test2));

        //then
        assertEquals(expected, actual);
    }

    @Test
    void update_validData_receiveTaskAndInteger_shouldReturnExistTaskTest() {
        //given
        Task expected = new Task();
        expected.setTitle("test");

        Status status = new Status();
        status.setId(1);
        status.setStatusName("new");
        expected.setStatus(status);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        expected.setDedline(LocalDate.parse("2022-12-23", format));

        expected.setInfo("info task");
        Integer testId = 1;

        //when
        Task actual = repository.update(expected, 1);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void removeTask_validData_receiveInteger_shouldReturnExistBooleanTest() {
        //given
        Integer testId = 1;

        //when
        Boolean actual = repository.remove(testId);

        //then
        assertTrue(actual);
    }
}