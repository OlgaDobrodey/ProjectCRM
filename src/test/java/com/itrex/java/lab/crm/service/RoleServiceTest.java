package com.itrex.java.lab.crm.service;

import com.itrex.java.lab.crm.dto.RoleDTO;
import com.itrex.java.lab.crm.entity.Role;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.crm.repository.RepositoryTestUtils;
import com.itrex.java.lab.crm.repository.impl.data.RoleRepository;
import com.itrex.java.lab.crm.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static com.itrex.java.lab.crm.utils.ConverterUtils.convertRoleToDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleServiceImpl roleService;
    @Mock
    private RoleRepository roleRepository;

    @Test
    void getAllRole_returnRoleDTOTest() {
        //given && when
        Mockito.when(roleRepository.findAll()).thenReturn(new ArrayList<>());

        //then
        assertEquals(roleService.getAllRoles().size(), 0);
        Mockito.verify(roleRepository).findAll();
    }

    @Test
    void getById_existIDRoleDTO_returnUSERRoleTest() {
        //given
        Integer idRole = 2;
        Role role = new Role();
        role.setId(2);
        role.setRoleName("USER");
        Mockito.when(roleRepository.findById(2)).thenReturn(Optional.of(role));

        //when
        RoleDTO actual = roleService.getById(idRole);

        //then
        Mockito.verify(roleRepository, Mockito.times(1)).findById(any());
        assertEquals(2, actual.getId());
        assertEquals("USER", actual.getRoleName());
    }

    @Test
    void getById_existIDRoleDTONotDataBase_returnNullTest() {
        //given
        Integer roleId = 28;
        Optional<Role> role = Optional.ofNullable(null);
        Mockito.when(roleRepository.findById(roleId)).thenReturn(role);
        //when
        RoleDTO actual = roleService.getById(roleId);

        //then
        Mockito.verify(roleRepository, Mockito.times(1)).findById(any());
        assertNull(actual);
    }

    @Test
    void addRole_existRoleDTO_returnRoleDTOTest() throws CRMProjectServiceException {
        //given && when
        Role testRole = RepositoryTestUtils.createTestRole(1).get(0);
        Mockito.when(roleRepository.save(testRole)).thenReturn(testRole);

        //then
        assertEquals("TESTA", roleService.addRole(convertRoleToDto(testRole)).getRoleName());
        Mockito.verify(roleRepository, Mockito.times(1)).save(any());
    }

    @Test
    void updateRole_existRoleDTO_returnRoleTest() throws CRMProjectServiceException {
        //given
        Role expected = RepositoryTestUtils.createTestRole(1).get(0);
        Integer testId = 1;
        expected.setId(testId);
        Mockito.when(roleRepository.findById(1)).thenReturn(Optional.of(expected));
        Mockito.when(roleRepository.save(expected)).thenReturn(expected);

        //when
        RoleDTO actual = roleService.updateRole(convertRoleToDto(expected));

        //then
        Mockito.verify(roleRepository, Mockito.times(1)).save(any());
        Mockito.verify(roleRepository).findById(any());
        assertEquals(1, actual.getId());
        assertEquals("TESTA", actual.getRoleName());
    }

    @Test
    void updateRole_existRoleDTONonDB_returnCRMProjectServiceExceptionTest(){
        //given && when
        Role expected = RepositoryTestUtils.createTestRole(1).get(0);
        expected.setId(99);
        Mockito.when(roleRepository.findById(expected.getId())).thenReturn(Optional.ofNullable(null));

        //when
        assertThrows(CRMProjectServiceException.class, () -> roleService.updateRole(convertRoleToDto(expected)));

        //then
        Mockito.verify(roleRepository, Mockito.times(1)).findById(any());
    }

}