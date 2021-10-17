package com.itrex.java.lab.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleTest {

    @Test
    void toString_Entity_shouldReturnExistStringTest() {
        String expected = "\nRole{id=1, roleName='test'}";
        Role role = new Role();
        role.setId(1);
        role.setRoleName("test");
        assertEquals(expected, role.toString());
    }
}