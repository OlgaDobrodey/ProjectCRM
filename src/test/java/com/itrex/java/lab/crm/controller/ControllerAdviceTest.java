package com.itrex.java.lab.crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrex.java.lab.crm.config.JwtConfigurer;
import com.itrex.java.lab.crm.entity.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ControllerAdviceTest extends BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected RoleController roleController;
    @MockBean
    protected JwtConfigurer jwtConfigurer;

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void handle() throws Exception {
        //given && when
        Status roleDTO = Status.NEW;

        //then
        mockMvc.perform(post("/crm/roles")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(roleDTO)))
                .andExpect(status().isExpectationFailed());
    }

}