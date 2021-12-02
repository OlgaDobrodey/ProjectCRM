package com.itrex.java.lab.crm.controller;

import com.itrex.java.lab.crm.security.JwtTokenProvider;
import com.itrex.java.lab.crm.service.RoleService;
import com.itrex.java.lab.crm.service.TaskService;
import com.itrex.java.lab.crm.service.UserService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@WebMvcTest
public abstract class BaseControllerTest {

    @MockBean
    protected RoleService roleService;
    @MockBean
    protected UserService userService;
    @MockBean
    protected TaskService taskService;
    @MockBean
    protected JwtTokenProvider jwtTokenProvider;
    @MockBean
    protected AuthenticationManager authenticationManager;
    @MockBean
    private SecurityContextLogoutHandler securityContextLogoutHandler;

}
