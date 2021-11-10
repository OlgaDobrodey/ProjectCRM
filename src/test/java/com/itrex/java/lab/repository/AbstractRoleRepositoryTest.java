package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractRoleRepositoryTest extends BaseRepositoryTest {

    private RoleRepository repository;

    public AbstractRoleRepositoryTest() {
        super();
    }

    public void postConstruct(RoleRepository repository) {
        this.repository = repository;
    }

    @Test
    public void selectAll_validData_returnRoleTest() throws CRMProjectRepositoryException {
        //given && when
        List<Role> result = repository.selectAll();

        //then
        assertFalse(result.isEmpty());
    }

    @Test
    void selectAll_shouldThrowRepositoryExceptionTest() {
        //given && when
        cleanDB();    //clean Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.selectAll());
    }

    @Test
    void selectById_validData_existIDRole_returnUSERRoleTest() throws CRMProjectRepositoryException {
        //given
        Integer idRole = 2;

        //when
        Role actual = repository.selectById(idRole);

        //then
        assertEquals(2, actual.getId());
        assertEquals("USER", actual.getRoleName());
    }

    @Test
    void selectById_validData_existIDRoleNotDataBase_returnNullTest() throws CRMProjectRepositoryException {
        //given
        Integer idRole = 28;

        //when
        Role actual = repository.selectById(idRole);

        //then
        assertNull(actual);
    }

    @Test
    void selectById_existRoleId_shouldThrowRepositoryExceptionTest() {
        //given && when
        cleanDB();    //clean Data Base
        Integer idRole = 2;

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.selectById(2));
    }

    @Test
    void add_validData_existRole_returnRoleTest() throws CRMProjectRepositoryException {
        //given
        Role role = RepositoryTestUtils.createTestRole(1).get(0);

        //when
        Role actual = repository.add(role);

        //then
        assertEquals(5, actual.getId());
        assertEquals("TEST 0", actual.getRoleName());
    }

    @Test
    void add_existRole_shouldThrowRepositoryExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        Role role = repository.selectById(2);  //There is Role "User" in Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.add(role));
    }

    @Test
    void addAll_validData_existRoles_returnRoleTest() throws CRMProjectRepositoryException {
        //given
        List<Role> roles = RepositoryTestUtils.createTestRole(2);

        //when
        List<Role> actual = repository.addAll(roles);

        //then
        assertEquals(5, actual.get(0).getId());
        assertEquals("TEST 0", actual.get(0).getRoleName());
        assertEquals(6, actual.get(1).getId());
        assertEquals("TEST 1", actual.get(1).getRoleName());
    }

    @Test
    void addAll_existCopyRoleNameUSER_shouldThrowRepositoryExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        Role test1 = RepositoryTestUtils.createTestRole(1).get(0);
        Role test2 = repository.selectById(2);  //There is Role "User" in Data Base

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.addAll(List.of(test1, test2)));
    }

    @Test
    void update_validData_existRoleAndIdRole_returnRoleTest() throws CRMProjectRepositoryException {
        //given
        Role expected = RepositoryTestUtils.createTestRole(1).get(0);
        expected.setId(1);

        //when
        Role actual = repository.update(expected);

        //then
        assertEquals(1, actual.getId());
        assertEquals("TEST 0", actual.getRoleName());
    }

    @Test
    void update_validData_existRoleAndIdRoleNonDB_shouldCRMProjectRepositoryExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        Role expected = RepositoryTestUtils.createTestRole(1).get(0);
        expected.setId(99);

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.update(expected));
    }

    @Test
    void update_existIdRole_shouldThrowRepositoryExceptionTest() {
        //given && when
        cleanDB();    //clean Data Base
        Role expected = RepositoryTestUtils.createTestRole(1).get(0);
        expected.setId(1);

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.update(expected));
    }

    @Test
    void removeRole_validData_existRoleIdTest() throws CRMProjectRepositoryException {
        //given
        List<Role> rolesGiven = repository.selectAll();
        Role roleForDelete = repository.selectById(4);

        //when
        repository.removeRole(4);
        List<Role> rolesWhen = repository.selectAll();

        //then
        assertFalse(rolesWhen.contains(roleForDelete));
        assertEquals(roleForDelete.getRoleName(), rolesGiven.get(3).getRoleName());
    }

    void removeRole_validData_existRoleId_shouldCRMProjectRepositoryExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        Integer idRoleNoFoundDB = 89;

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.removeRole(idRoleNoFoundDB));

    }
}
