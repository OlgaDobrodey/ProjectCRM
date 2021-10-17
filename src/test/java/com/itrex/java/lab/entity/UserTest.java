package com.itrex.java.lab.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void toString_Entity_shouldReturnExistStringTest() {
        String expected = "\nUser{id=null, login='test', psw=123, role=null, firstName='test1', lastName='test2'}";
        User user = new User();
        user.setLogin("test");
        user.setPsw(123);
        user.setFirstName("test1");
        user.setLastName("test2");

        assertEquals(expected, user.toString());
    }
}