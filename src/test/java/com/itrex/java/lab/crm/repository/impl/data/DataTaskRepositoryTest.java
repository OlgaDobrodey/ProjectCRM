package com.itrex.java.lab.crm.repository.impl.data;

import com.itrex.java.lab.crm.entity.Status;
import com.itrex.java.lab.crm.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class DataTaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void findTasksByUsers_Id_existUserId_returnListOfTasks() {
        //given
        Integer id = 2;

        //when
        List<Task> actual = taskRepository.findTasksByUsers_Id(id);

        //then
        assertEquals(3, actual.size());
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
    void findTasksByUsers_Id_existUserIdNonDataBase_returnListOfTasks() {
        //given
        Integer id = 99;

        //when
        List<Task> actual = taskRepository.findTasksByUsers_Id(id);

        //then
        assertTrue(actual.isEmpty());
    }

}