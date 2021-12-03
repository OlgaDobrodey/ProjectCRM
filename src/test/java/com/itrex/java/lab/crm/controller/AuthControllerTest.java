package com.itrex.java.lab.crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrex.java.lab.crm.config.JwtConfigurer;
import com.itrex.java.lab.crm.dto.UserDtoLoginRoleName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class AuthControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtConfigurer jwtConfigurer;
    @MockBean
    protected SecurityContextLogoutHandler securityContextLogoutHandler;

    private String APPLICATION_JSON = "application/json";

    @Test
    void authenticate_existUserAuthenticationDTO_returnResponseEntity200Test() throws Exception {
        //given
        UserDtoLoginRoleName udlrm = UserDtoLoginRoleName.builder()
                .id(1)
                .login("Login")
                .psw("123")
                .roleName("ADMIN")
                .build();
        String token = "token";
        when(userService.getByLogin(udlrm.getLogin())).thenReturn(udlrm);
        when(jwtTokenProvider.createToken(udlrm.getLogin(), udlrm.getRoleName())).thenReturn(token);

        //when
        MvcResult mvcResult = mockMvc.perform(post("/crm/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(udlrm)))
                .andReturn();

        //then
        MockHttpServletResponse response = mvcResult.getResponse();
        Map<Object, Object> result = objectMapper.readValue(response.getContentAsString(), HashMap.class);

        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertEquals(udlrm.getLogin(), result.get("login"));
        assertEquals(token, result.get("token"));

        verify(userService).getByLogin(udlrm.getLogin());
        verify(jwtTokenProvider).createToken(udlrm.getLogin(), udlrm.getRoleName());
    }

    @Test
    void authenticate_existUserAuthenticationDTO_returnResponseEntity404Test() throws Exception {
        //given
        UserDtoLoginRoleName udlrm = UserDtoLoginRoleName.builder()
                .id(1)
                .login("Test")
                .psw("123")
                .roleName("ADMIN")
                .build();

        when(userService.getByLogin(udlrm.getLogin())).thenReturn(null);

        //when
        MvcResult mvcResult = mockMvc.perform(post("/crm/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(udlrm)))
                .andReturn();

        //then
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
        assertEquals("User doesn't exists", response.getContentAsString());

        verify(userService).getByLogin(udlrm.getLogin());
    }

    @Test
    void logout_existHttpServletRequestHttpServletResponse_Test() throws Exception {
        //given && when && then

        mockMvc.perform(post("/crm/logout")
                        .contentType(APPLICATION_JSON))
                .andReturn();
    }

}