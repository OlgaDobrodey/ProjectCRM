package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.RoleRepository;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class JDBCRoleRepositoryImplTest extends BaseRepositoryTest {

    private final RoleRepository repository;

    public JDBCRoleRepositoryImplTest() {
        super();
        repository = new JDBCRoleRepositoryImpl(getConnectionPool());
    }

    @Test
    public void selectAll_validData_shouldReturnExistRoleTest() throws SQLException {
        //given && when
        List<Role> result = repository.selectAll();

        //then
        assertFalse(result.isEmpty());
    }

    @Test
    void selectById_validData_receiveInteger_shouldReturnExistRoleTest() throws SQLException {
        //given
        Integer idRole = 2;

        //when
        Role actual = repository.selectById(idRole);

        //then
        assertEquals(2, actual.getId());
        assertEquals("USER", actual.getRoleName());
    }

    @Test
    void add_validData_receiveRole_shouldReturnExistRoleTest() throws SQLException {
        //given
        Role role = new Role();
        role.setRoleName("test");

        //when
        Role actual = repository.add(role);

        //then
        assertEquals(4, actual.getId());
        assertEquals("TEST", actual.getRoleName());
    }

    @Test
    void addAll_validData_receiveRole_shouldReturnExistRoleTest() throws SQLException {
        //given
        Role roleTest1 = new Role();
        roleTest1.setRoleName("test1");
        Role roleTest2 = new Role();
        roleTest2.setRoleName("test2");

        //when
        List<Role> actual = repository.addAll(List.of(roleTest1, roleTest2));

        //then
        assertEquals(4, actual.get(0).getId());
        assertEquals("TEST1", actual.get(0).getRoleName());
        assertEquals(5, actual.get(1).getId());
        assertEquals("TEST2", actual.get(1).getRoleName());
    }

    @Test
    void update_validData_receiveRoleAndInteger_shouldReturnExistRoleTest() throws SQLException {
        //given
        Role expected = new Role();
        expected.setRoleName("test");
        Integer testId = 1;

        //when
        Role actual = repository.update(expected, 1);

        //then
        assertEquals(1, actual.getId());
        assertEquals("TEST", actual.getRoleName());
    }

    @Test
    void remove_validData_receiveRole_shouldReturnExistBooleanTest() throws SQLException {
        //given
        Role role = repository.selectById(1);
        Role roleUser = new Role();
        roleUser.setId(8);
        roleUser.setRoleName("NO ADMIN");

        //when
        Boolean actual = repository.remove(role);
        Boolean actualFalse = repository.remove(roleUser);

        //then
        assertTrue(actual);
        assertFalse(actualFalse);
    }

    @Test
    void remove_validData_receiveRoleAndDefault_shouldReturnExistBooleanTest() throws SQLException {
        //given
        Role role = repository.selectById(1);
        Role defaultRole = repository.selectById(3);

        //when
        Boolean actual = repository.remove(role, defaultRole);
        Boolean actualFalse = repository.remove(role,role);

        //then
        assertTrue(actual);
        assertFalse(actualFalse);
    }
}