package com.itrex.java.lab.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatusTest {

    @Test
    void toString_Entity_shouldReturnExistStringTest() {
        String expected = "\nStatus{id=1, statusName='test'}";
        Status status = new Status();
        status.setId(1);
        status.setStatusName("test");
        assertEquals(expected, status.toString());
    }
}