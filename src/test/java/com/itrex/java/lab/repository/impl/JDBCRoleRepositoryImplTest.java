package com.itrex.java.lab.repository.impl;


import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.RoleRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class JDBCRoleRepositoryImplTest extends BaseRepositoryTest {

    private final RoleRepository repository;

    public JDBCRoleRepositoryImplTest() {
        super();
        repository = new JDBCRoleRepositoryImpl(getConnectionPool());
    }

    @Test
    public void selectAll_validData_shouldReturnExistRoleTest() {
        //given && when
        final List<Role> result = repository.selectAll();

        //then
        assertFalse(result.isEmpty());
    }

    @Test
    void selectById_validData_receiveInteger_shouldReturnExistRoleTest() {
        //given
        Integer idRole = 2;

        //when
        Role actual = repository.selectById(idRole);
        Role expected = new Role();
        expected.setId(2);
        expected.setRoleName("user");

        //then
        assertEquals(expected, actual);
    }

    @Test
    void add_validData_receiveRole_shouldReturnExistRoleTest() {
        //given
        Role role = new Role();
        role.setRoleName("test");
        Role expected = new Role();
        expected.setId(3);
        expected.setRoleName("test");

        //when
        Role actual = repository.add(role);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void addAll_validData_receiveRole_shouldReturnExistRoleTest() {
        //given
        Role roleTest1 = new Role();
        roleTest1.setRoleName("test1");
        Role roleTest2 = new Role();
        roleTest2.setRoleName("test2");

        Role roleExpected1 = new Role();
        roleExpected1.setRoleName("test1");
        roleExpected1.setId(3);
        Role roleExpected2 = new Role();
        roleExpected2.setRoleName("test2");
        roleExpected2.setId(4);

        //when
        List<Role> expected = List.of(roleExpected1, roleExpected2);
        List<Role> actual = repository.addAll(List.of(roleTest1, roleTest2));

        //then
        assertEquals(expected, actual);
    }

    @Test
    void update_validData_receiveRoleAndInteger_shouldReturnExistRoleTest() {
        //given
        Role expected = new Role();
        expected.setRoleName("test");
        Integer testId = 1;

        //when
        Role actual = repository.update(expected, 1);

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