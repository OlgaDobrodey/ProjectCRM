package com.itrex.java.lab.crm.repository;

import com.itrex.java.lab.crm.entity.Role;
import com.itrex.java.lab.crm.exceptions.CRMProjectRepositoryException;
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
    void add_validData_existRole_returnRoleTest() throws CRMProjectRepositoryException {
        //given
        Role role = RepositoryTestUtils.createTestRole(1).get(0);

        //when
        Role actual = repository.add(role);

        //then
        assertEquals("TESTA", actual.getRoleName());
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
        assertEquals("TESTA", actual.getRoleName());
    }

    @Test
    void update_validData_existRoleAndIdRoleNonDB_shouldCRMProjectRepositoryExceptionTest() {
        //given && when
        Role expected = RepositoryTestUtils.createTestRole(1).get(0);
        expected.setId(99);

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> repository.update(expected));
    }

}
