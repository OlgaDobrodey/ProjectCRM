package com.itrex.java.lab.crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrex.java.lab.crm.dto.RoleDTO;
import com.itrex.java.lab.crm.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.itrex.java.lab.crm.controller.ControllerUtilsTest.createRoleDto;
import static com.itrex.java.lab.crm.controller.ControllerUtilsTest.createUserDto;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoleControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void read_whenValidInput_thenReturns200Test() throws Exception {
        //given && when
        List<RoleDTO> roles = List.of(createRoleDto(1));
        when(roleService.getAllRoles()).thenReturn(roles);

        //then
        mockMvc.perform(get("/crm/roles")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(roleService).getAllRoles();
    }

    /* List of Roles not found in Data Base */
    @Test
    void read_whenValidInput_thenReturns404Test() throws Exception {
        //given && when
        when(roleService.getAllRoles()).thenReturn(new ArrayList<>());

        //then
        mockMvc.perform(get("/crm/roles")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());

        verify(roleService).getAllRoles();
    }

    @Test
    void readRoleByRoleId_whenInputValid_thenReturns200Test() throws Exception {
        //given && when
        RoleDTO role = createRoleDto(1);
        when(roleService.getById(role.getId())).thenReturn(role);

        //then
        mockMvc.perform(get("/crm/roles/{id}", role.getId())
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(roleService).getById(role.getId());
    }

    @Test
    void readRoleByRoleId_whenInputValidRoleIdNoDataBase_thenReturns404Test() throws Exception {
        //given && when
        Integer roleId = 1;
        when(roleService.getById(roleId)).thenReturn(null);

        //then
        mockMvc.perform(get("/crm/roles/{id}", roleId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());

        verify(roleService).getById(roleId);
    }

    @Test
    void getAllRoleUsersByRoleId_inputValidPathVariableRoleId_thenReturns200Test() throws Exception {
        //given && when
        Integer roleId = 2;
        List<UserDTO> users = List.of(createUserDto(1), createUserDto(2));

        when(userService.getAllRoleUsersByRoleId(roleId)).thenReturn(users);

        //then
        mockMvc.perform(get("/crm/roles/{id}/users", roleId)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(userService).getAllRoleUsersByRoleId(roleId);
    }

    @Test
    void getAllRoleUsersByRoleId_inputValidPathVariableRoleIdNoDataBase_thenReturns404Test() throws Exception {
        //given && when
        Integer roleId = 2;
        List<UserDTO> users = new ArrayList<>();

        when(userService.getAllRoleUsersByRoleId(roleId)).thenReturn(users);

        //then
        mockMvc.perform(get("/crm/roles/{id}/users", roleId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());

        verify(userService).getAllRoleUsersByRoleId(roleId);
    }

    @Test
    void create_whereInputValidRole_thenReturns201Test() throws Exception {
        //given && when
        RoleDTO roleDTO = createRoleDto(1);
        when(roleService.addRole(roleDTO)).thenReturn(roleDTO);

        //then
        mockMvc.perform(post("/crm/roles")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(roleDTO)))
                .andExpect(status().isCreated());

        verify(roleService).addRole(roleDTO);
    }

    @Test
    void update_whereInputValidRoleAndRoleId_thenReturns200Test() throws Exception {
        //given && when
        RoleDTO roleDTO = createRoleDto(1);
        when(roleService.updateRole(roleDTO)).thenReturn(roleDTO);

        //then
        mockMvc.perform(put("/crm/roles/{id}", roleDTO.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(roleDTO)))
                .andExpect(status().isOk());

        verify(roleService).updateRole(roleDTO);
    }

    @Test
    void update_whereInputValidRoleAndRoleId_thenReturns304Test() throws Exception {
        //given && when
        RoleDTO roleDTO = createRoleDto(1);
        when(roleService.updateRole(roleDTO)).thenReturn(null);

        //then
        mockMvc.perform(put("/crm/roles/{id}", roleDTO.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(roleDTO)))
                .andExpect(status().isNotModified());

        verify(roleService).updateRole(roleDTO);
    }

}