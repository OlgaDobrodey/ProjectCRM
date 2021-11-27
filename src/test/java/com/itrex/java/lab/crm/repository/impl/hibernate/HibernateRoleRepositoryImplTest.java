package com.itrex.java.lab.crm.repository.impl.hibernate;

import com.itrex.java.lab.crm.entity.Role;
import com.itrex.java.lab.crm.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.crm.repository.RepositoryTestUtils;
import com.itrex.java.lab.crm.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HibernateRoleRepositoryImplTest extends BaseHibernateRepositoryTest {

    @Autowired
    @Qualifier(value = "HibernateRoleRepository")
    private RoleRepository roleRepository;

    @Test
    public void selectAll_validData_returnRoleTest() throws CRMProjectRepositoryException {
        //given && when
        List<Role> result = roleRepository.selectAll();

        //then
        assertFalse(result.isEmpty());
    }

    @Test
    void selectById_validData_existIDRole_returnUSERRoleTest() throws CRMProjectRepositoryException {
        //given
        Integer idRole = 2;

        //when
        Role actual = roleRepository.selectById(idRole);

        //then
        assertEquals(2, actual.getId());
        assertEquals("USER", actual.getRoleName());
    }

    @Test
    void selectById_validData_existIDRoleNotDataBase_returnNullTest() throws CRMProjectRepositoryException {
        //given
        Integer idRole = 28;

        //when
        Role actual = roleRepository.selectById(idRole);

        //then
        assertNull(actual);
    }

    @Test
    void add_validData_existRole_returnRoleTest() throws CRMProjectRepositoryException {
        //given
        Role role = RepositoryTestUtils.createTestRole(1).get(0);

        //when
        Role actual = roleRepository.add(role);

        //then
        assertEquals(5,roleRepository.selectAll().size());
    }

    @Test
    void update_validData_existRoleAndIdRole_returnRoleTest() throws CRMProjectRepositoryException {
        //given
        Role expected = RepositoryTestUtils.createTestRole(1).get(0);
        expected.setId(1);

        //when
        Role actual = roleRepository.update(expected);

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
        assertThrows(CRMProjectRepositoryException.class, () -> roleRepository.update(expected));
    }

}

