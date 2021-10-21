package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.repository.UtillCategory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JDBCRoleRepositoryImplTest extends BaseRepositoryTest {

    private final RoleRepository repository;

    public JDBCRoleRepositoryImplTest() {
        super();
        repository = new JDBCRoleRepositoryImpl(getConnectionPool());
    }

    @Test
    public void selectAll_validData_shouldReturnExistRoleTest() throws CRMProjectRepositoryException {
        //given && when
        List<Role> result = repository.selectAll();

        //then
        assertFalse(result.isEmpty());
    }

    @Test
    void selectAll_validData_shouldReturnCRMProjectRepositoryExceptionType() {
        //given && when
        cleanDB();    //clean Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> {
            repository.selectAll();
        });
    }

    @Test
    void selectById_validData_receiveIDRole_shouldReturnExistUSERRoleTest() throws CRMProjectRepositoryException {
        //given
        Integer idRole = 2;

        //when
        Role actual = repository.selectById(idRole);

        //then
        assertEquals(2, actual.getId());
        assertEquals("USER", actual.getRoleName());
    }

    @Test
    void selectById_validData_receiveIDRoleNotDataBase_shouldReturnNULLTest() throws CRMProjectRepositoryException {
        //given
        Integer idRole = 28;

        //when
        Role actual = repository.selectById(idRole);

        //then
        assertNull(actual);
    }

    @Test
    void selectById_validData_shouldReturnCRMProjectRepositoryExceptionType() {
        //given && when
        cleanDB();    //clean Data Base
        Integer idRole = 2;

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> {
            repository.selectById(2);
        });
    }

    @Test
    void add_validData_receiveRole_shouldReturnExistRoleTest() throws CRMProjectRepositoryException {
        //given
        Role role = UtillCategory.createTestRole(1).get(0);

        //when
        Role actual = repository.add(role);

        //then
        assertEquals(4, actual.getId());
        assertEquals("TEST 0", actual.getRoleName());
    }

    @Test
    void add_validData_receiveCopyRoleUSER_shouldReturnCRMProjectRepositoryExceptionTypeTest() throws CRMProjectRepositoryException {
        //given && when
        Role role = repository.selectById(2);  //There is Role "User" in Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> {
            repository.add(role);
        });
    }

    @Test
    void addAll_validData_receiveRoles_shouldReturnExistRoleTest() throws CRMProjectRepositoryException {
        //given
        List<Role> roles = UtillCategory.createTestRole(2);

        //when
        List<Role> actual = repository.addAll(roles);

        //then
        assertEquals(4, actual.get(0).getId());
        assertEquals("TEST 0", actual.get(0).getRoleName());
        assertEquals(5, actual.get(1).getId());
        assertEquals("TEST 1", actual.get(1).getRoleName());
    }

    @Test
    void addAll_validData_receiveCopyRoleUSER_shouldReturnCRMProjectRepositoryExceptionTypeTest() throws CRMProjectRepositoryException {
        //given && when
        Role test1 = UtillCategory.createTestRole(1).get(0);
        Role test2 = repository.selectById(2);  //There is Role "User" in Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> {
            repository.addAll(List.of(test1, test2));
        });
    }

    @Test
    void update_validData_receiveRoleAndIdRole_shouldReturnExistRoleTest() throws CRMProjectRepositoryException {
        //given
        Role expected = UtillCategory.createTestRole(1).get(0);
        Integer testId = 1;

        //when
        Role actual = repository.update(expected, testId);

        //then
        assertEquals(1, actual.getId());
        assertEquals("TEST 0", actual.getRoleName());
    }

    @Test
    void update_validData_receiveRoleAndIdRoleNotDB_shouldReturnNullTest() throws CRMProjectRepositoryException {
        //given && when
        Role expected = UtillCategory.createTestRole(1).get(0);
        Integer idNonDataBase = 99;

        //when
        Role actual = repository.update(expected, idNonDataBase);

        //then
        assertNull(actual);
    }

    @Test
    void update_validData__receiveIdRole_shouldReturnCRMProjectRepositoryExceptionType() {
        //given && when
        cleanDB();    //clean Data Base
        Role expected = UtillCategory.createTestRole(1).get(0);
        Integer idRole = 1;

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> {
            repository.update(expected, 2);
        });
    }

    @Test
    void remove_validData_receiveRole_shouldReturnExistBooleanTest() throws CRMProjectRepositoryException {
        //given
        Role role = repository.selectById(1);
        Role roleNonDB = UtillCategory.createTestRole(1).get(0);              //There is no such role in the database
        roleNonDB.setId(8);

        //when
        Boolean actual = repository.remove(role);
        Boolean actualFalse = repository.remove(roleNonDB);

        //then
        assertTrue(actual);
        assertFalse(actualFalse);
    }

    @Test
    void remove_validData_receiveRoleUSER_shouldReturnCRMProjectRepositoryExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        Role expected = repository.selectById(2); //expected == default role "USER"

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.remove(expected));
    }

    @Test
    void remove_validData_receiveRoleAndDefault_shouldReturnExistBooleanTest() throws CRMProjectRepositoryException {
        //given
        Role role = repository.selectById(1);
        Role defaultRole = repository.selectById(3);
        Role falseRole = UtillCategory.createTestRole(1).get(0);
        falseRole.setId(25);

        //when
        Boolean actual = repository.remove(role, defaultRole);
        Boolean actualFalse = repository.remove(falseRole, role);

        //then
        assertTrue(actual);
        assertFalse(actualFalse);
    }

    @Test
    void remove_validData_receiveRoleUSEREqualsRoleDefault_shouldReturnCRMProjectRepositoryExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        Role expected = repository.selectById(2); //expected == default role "USER"

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.remove(expected, expected));
    }

    @Test
    void remove_validData_receiveRole_shouldReturnCRMProjectRepositoryExceptionType() {
        //given && when
        cleanDB();    //clean Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> {
            repository.remove(repository.selectById(1));
        });
    }

    @Test
    void remove_validData_receiveRoleAndDefaultRole_shouldReturnCRMProjectRepositoryExceptionType() {
        //given && when
        cleanDB();    //clean Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> {
            repository.remove(repository.selectById(1), repository.selectById(3));
        });
    }
}