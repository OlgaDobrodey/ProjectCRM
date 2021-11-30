package com.itrex.java.lab.crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrex.java.lab.crm.dto.PasswordDTOForChanges;
import com.itrex.java.lab.crm.dto.TaskDTO;
import com.itrex.java.lab.crm.dto.UserDTO;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.itrex.java.lab.crm.controller.ControllerUtilsTest.createTaskDto;
import static com.itrex.java.lab.crm.controller.ControllerUtilsTest.createUserDto;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void read_whenValidInput_thenReturns200Test() throws Exception {
        //given && when
        List<UserDTO> users = List.of(createUserDto(1));
        when(userService.getAll()).thenReturn(users);

        //then
        mockMvc.perform(get("/crm/users")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(userService).getAll();
    }

    /* List of Users not found in Data Base */
    @Test
    void read_whenValidInput_thenReturns404Test() throws Exception {
        //given && when
        when(userService.getAll()).thenReturn(new ArrayList<>());

        //then
        mockMvc.perform(get("/crm/users")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());

        verify(userService).getAll();
    }

    @Test
    void readUserByUserId_whenInputValId_thenReturns200Test() throws Exception {
        //given && when
        UserDTO user = createUserDto(1);
        when(userService.getById(user.getId())).thenReturn(user);

        //then
        mockMvc.perform(get("/crm/users/{id}", user.getId())
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(userService).getById(user.getId());
    }

    @Test
    void readUserByUserId_whenInputValIdNoBD_thenReturns404Test() throws Exception {
        //given && when
        Integer userId = 1;
        when(userService.getById(userId)).thenReturn(null);

        //then
        mockMvc.perform(get("/crm/users/{id}", userId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());

        verify(userService).getById(userId);
    }

    @Test
    void readAllUserTasksByUserId_whenInputValidPathVariableUserId_thenReturns200Test() throws Exception {
        //given && when
        List<TaskDTO> tasks = List.of(createTaskDto(1));
        Integer userId = 1;
        when(taskService.getAllUserTasksByUserId(userId)).thenReturn(tasks);

        //then
        mockMvc.perform(get("/crm/users/{id}/tasks", userId)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(taskService).getAllUserTasksByUserId(userId);
    }

    @Test
    void readAllUserTasksByUserId_whenInputValidPathVariableUserId_thenReturns404Test() throws Exception {
        //given && when
        Integer userId = 1;
        when(taskService.getAllUserTasksByUserId(userId)).thenReturn(new ArrayList<>());

        //then
        mockMvc.perform(get("/crm/users/{id}/tasks", userId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());

        verify(taskService).getAllUserTasksByUserId(userId);
    }

    @Test
    void create_whereInputValidUser_thenReturns201Test() throws Exception {
        //given && when
        UserDTO user = createUserDto(1);
        when(userService.add(user)).thenReturn(user);

        //then
        mockMvc.perform(post("/crm/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        verify(userService).add(user);
    }

    @Test
    void update_whereInputValidUserAndUserId_thenReturns200Test() throws Exception {
        //given && when
        UserDTO user = createUserDto(1);
        when(userService.update(user)).thenReturn(user);

        //then
        mockMvc.perform(put("/crm/users/{id}", user.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        verify(userService).update(user);
    }

    @Test
    void update_whereInputValidUserAndUserId_thenReturns404Test() throws Exception {
        //given && when
        UserDTO user = createUserDto(1);
        when(userService.update(user)).thenThrow(CRMProjectServiceException.class);

        //then
        mockMvc.perform(put("/crm/users/{id}", user.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());

        verify(userService).update(user);
    }

    @Test
    void updateUserPassword_whenValidInputPathVarUserIdReqBodyPasswordDTOForChanges_thenReturns200Test() throws Exception {
        //given && when
        PasswordDTOForChanges pdtofc = PasswordDTOForChanges
                .builder()
                .oldPassword("123")
                .newPassword("1234")
                .repeatNewPassword("1234")
                .build();
        UserDTO user = createUserDto(1);
        user.setPsw(pdtofc.getNewPassword());
        when(userService.updateUserPassword(pdtofc, user.getId())).thenReturn(user);

        //then
        mockMvc.perform(put("/crm/users/{id}/password", user.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(pdtofc)))
                .andExpect(status().isOk());

        verify(userService).updateUserPassword(pdtofc, user.getId());
    }

    @Test
    void updateUserPassword_whenValidInputPathVarUserIdReqBodyPasswordDTOForChanges_thenReturns404Test() throws Exception {
        //given && when
        PasswordDTOForChanges pdtofc = PasswordDTOForChanges
                .builder()
                .oldPassword("123")
                .newPassword("1234")
                .repeatNewPassword("1234")
                .build();
        Integer userId = 2;
        when(userService.updateUserPassword(pdtofc, userId)).thenThrow(CRMProjectServiceException.class);

        //then
        mockMvc.perform(put("/crm/users/{id}/password", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(pdtofc)))
                .andExpect(status().isNotFound());

        verify(userService).updateUserPassword(pdtofc, userId);
    }

    @Test
    void assignTaskToUser_whenValidInputPathUserIdAndTaskId_return200Test() throws Exception {
        //given && when
        Integer userId = 1;
        Integer taskId = 2;
        doNothing().when(userService).assignTaskToUser(taskId, userId);

        //then
        mockMvc.perform(put("/crm/users/{userId}/tasks/{taskId}", userId, taskId)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(userService).assignTaskToUser(taskId, userId);
    }

    @Test
    void assignTaskToUser_whenValidInputPathUserIdAndTaskId_return404Test() throws Exception {
        //given && when
        Integer userId = 1;
        Integer taskId = 2;
        doThrow(CRMProjectServiceException.class).when(userService).assignTaskToUser(taskId, userId);

        //then
        mockMvc.perform(put("/crm/users/{userId}/tasks/{taskId}", userId, taskId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());

        verify(userService).assignTaskToUser(taskId, userId);
    }

    @Test
    void delete_whenInputValidPathVariableUsersId_thenReturns200Test() throws Exception {
        //given && when
        Integer userId = 2;
        doNothing().when(userService).remove(userId);

        //then
        mockMvc.perform(delete("/crm/users/{id}", userId)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(userService).remove(userId);
    }

    @Test
    void delete_whenInputValidPathVariableUsersId_thenReturns404Test() throws Exception {
        //given && when
        Integer userId = 2;
        doThrow(CRMProjectServiceException.class).when(userService).remove(userId);

        //then
        mockMvc.perform(delete("/crm/users/{id}", userId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());

        verify(userService).remove(userId);
    }

    @Test
    void revoke_whenInputValidPathVariableUsersId_thenReturns200Test() throws Exception {
        //given && when
        Integer userId = 2;
        Integer taskId = 3;
        doNothing().when(userService).revokeTaskFromUser(taskId, userId);

        //then
        mockMvc.perform(delete("/crm/users/{userId}/tasks/{taskId}", userId, taskId)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(userService).revokeTaskFromUser(taskId, userId);
    }

    @Test
    void revoke_whenInputValidPathVariableUsersId_thenReturns304Test() throws Exception {
        //given && when
        Integer userId = 2;
        Integer taskId = 3;
        doThrow(CRMProjectServiceException.class).when(userService).revokeTaskFromUser(taskId, userId);

        //then
        mockMvc.perform(delete("/crm/users/{userId}/tasks/{taskId}", userId, taskId)
                        .contentType("application/json"))
                .andExpect(status().isNotModified());

        verify(userService).revokeTaskFromUser(taskId, userId);
    }

    @Test
    void revokeAllUserTasksByUserId_whenInputValidPathVariableUsersId_thenReturns200Test() throws Exception {
        //given && when
        Integer userId = 2;
        doNothing().when(userService).revokeAllUserTasksByUserId(userId);

        //then
        mockMvc.perform(delete("/crm/users/{userId}/tasks", userId)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(userService).revokeAllUserTasksByUserId(userId);
    }

    @Test
    void revokeAllUserTasksByUserId_whenInputValidPathVariableUsersId_thenReturns304Test() throws Exception {
        //given && when
        Integer userId = 2;
        doThrow(CRMProjectServiceException.class).when(userService).revokeAllUserTasksByUserId(userId);

        //then
        mockMvc.perform(delete("/crm/users/{userId}/tasks", userId)
                        .contentType("application/json"))
                .andExpect(status().isNotModified());

        verify(userService).revokeAllUserTasksByUserId(userId);
    }

}