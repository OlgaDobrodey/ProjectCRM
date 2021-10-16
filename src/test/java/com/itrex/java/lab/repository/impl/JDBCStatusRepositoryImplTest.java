package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Status;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.StatusRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JDBCStatusRepositoryImplTest extends BaseRepositoryTest {

    private final StatusRepository repository;

    public JDBCStatusRepositoryImplTest() {
        super();
        repository = new JDBCStatusRepositoryImpl(getConnectionPool());
    }

    @Test
    public void selectAll_validData_shouldReturnExistStatusTest() {
        //given && when
        final List<Status> result = repository.selectAll();

        //then
        assertFalse(result.isEmpty());
    }

    @Test
    void selectById_validData_receiveInteger_shouldReturnExistStatusTest() {
        //given
        Integer idRole = 2;

        //when
        Status actual = repository.selectById(idRole);
        Status expected = new Status();
        expected.setId(2);
        expected.setStatusName("performed");

        //then
        assertEquals(expected, actual);
    }

    @Test
    void add_validData_receiveStatus_shouldReturnExistStatusTest() {
        //given
        Status status = new Status();
        status.setStatusName("test");
        Status expected = new Status();
        expected.setId(repository.selectAll().size()+1);
        expected.setStatusName("test");

        //when
        Status actual = repository.add(status);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void addAll_validData_receiveStatus_shouldReturnExistStatusTest() {
        //given
        Status test1 = new Status();
        test1.setStatusName("test1");
        Status test2 = new Status();
        test2.setStatusName("test2");

        Integer countSelectAllStatus = repository.selectAll().size();
        Status result1 = new Status();
        result1.setStatusName("test1");
        result1.setId(countSelectAllStatus+1);
        Status  result2 = new Status();
        result2.setStatusName("test2");
        result2.setId(countSelectAllStatus+2);

        //when
        List<Status> expected = List.of( result1,  result2);
        List<Status> actual = repository.addAll(List.of(test1, test2));

        //then
        assertEquals(expected, actual);
    }

    @Test
    void update_validData_receiveStatusAndInteger_shouldReturnExistStatusTest() {
        //given
        Status expected = new Status();
        expected.setStatusName("test");
        Integer testId = 1;

        //when
        Status actual = repository.update(expected, 1);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void remove_validData_receiveInteger_shouldReturnExistBooleanTest() {
        //given
        Integer testId = 1;

        //when
        Boolean actual = repository.remove(testId);

        //then
        assertTrue(actual);
    }
}