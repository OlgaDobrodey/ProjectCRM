package com.itrex.java.lab.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    @Test
    void toString_Entity_shouldReturnExistStringTest() {
        String expected = "\nTask{id=1, title='test', status=null, dedline=2001-01-01, info='test info'}";
        Task task = new Task();
        task.setId(1);
        task.setTitle("test");
        task.setDedline(LocalDate.of(2001, 1, 1));
        task.setInfo("test info");
        task.setStatus(null);
        assertEquals(expected, task.toString());
    }
}