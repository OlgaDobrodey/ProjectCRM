package com.itrex.java.lab.crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrex.java.lab.crm.config.JwtConfigurer;
import com.itrex.java.lab.crm.dto.RoleDTO;
import com.itrex.java.lab.crm.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.itrex.java.lab.crm.controller.ControllerUtilsTest.createRoleDto;
import static com.itrex.java.lab.crm.controller.ControllerUtilsTest.createUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoleControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtConfigurer jwtConfigurer;

    private String EXPECTED_USERS_DTO = "[" + createUserDto(1).toString().replaceFirst("\\n", "").replaceAll("\\'", "") + "]";
    private String EXPECTED_ROlES_DTO = "[" + createRoleDto(1).toString().replaceFirst("\\n", "").replaceAll("\\'", "") + "]";

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "USER")
    void read_whenValidInput_thenReturns200Test() throws Exception {
        //given && when
        List<RoleDTO> roles = List.of(createRoleDto(1));
        when(roleService.getAllRoles()).thenReturn(roles);

        //then
        MvcResult mvcResult = mockMvc.perform(get("/crm/roles")
                        .contentType("application/json"))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        List<RoleDTO> roleDTOSs = objectMapper.readValue(response.getContentAsString(), ArrayList.class);
        assertEquals(EXPECTED_ROlES_DTO, roleDTOSs.toString());

        verify(roleService).getAllRoles();
    }

    /* List of Roles not found in Data Base */
    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
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
    @WithMockUser(username = "Petrov", password = "123", roles = "USER")
    void readRoleByRoleId_whenInputValid_thenReturns200Test() throws Exception {
        //given
        RoleDTO role = createRoleDto(1);
        when(roleService.getById(role.getId())).thenReturn(role);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/crm/roles/{id}", role.getId())
                        .contentType("application/json"))
                .andReturn();

        //then
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        RoleDTO roleDTOSs = objectMapper.readValue(response.getContentAsString(), RoleDTO.class);
        assertEquals("TestRoleB", roleDTOSs.getRoleName());

        verify(roleService).getById(role.getId());
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "USER")
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
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void getAllRoleUsersByRoleId_inputValidPathVariableRoleId_thenReturns200Test() throws Exception {
        //given
        Integer roleId = 2;
        List<UserDTO> users = List.of(createUserDto(1));

        when(userService.getAllRoleUsersByRoleId(roleId)).thenReturn(users);

        //when
        MvcResult mvcResult = mockMvc.perform(get("/crm/roles/{id}/users", roleId)
                        .contentType("application/json"))
                .andReturn();

        //then
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        List<UserDTO> result = objectMapper.readValue(response.getContentAsString(), ArrayList.class);
        assertEquals(EXPECTED_USERS_DTO, result.toString());

        verify(userService).getAllRoleUsersByRoleId(roleId);
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
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
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void create_whereInputValidRole_thenReturns201Test() throws Exception {
        //given
        RoleDTO roleDTO = createRoleDto(1);
        when(roleService.addRole(roleDTO)).thenReturn(roleDTO);

        //when
        MvcResult mvcResult = mockMvc.perform(post("/crm/roles")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(roleDTO)))
                .andReturn();

        //then
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
        RoleDTO roleDTOSs = objectMapper.readValue(response.getContentAsString(), RoleDTO.class);
        assertEquals("TestRoleB", roleDTOSs.getRoleName());

        verify(roleService).addRole(roleDTO);
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
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
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
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