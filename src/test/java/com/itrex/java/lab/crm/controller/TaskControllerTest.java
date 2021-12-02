package com.itrex.java.lab.crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrex.java.lab.crm.config.JwtConfigurer;
import com.itrex.java.lab.crm.dto.TaskDTO;
import com.itrex.java.lab.crm.dto.UserDTO;
import com.itrex.java.lab.crm.entity.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.itrex.java.lab.crm.controller.ControllerUtilsTest.createTaskDto;
import static com.itrex.java.lab.crm.controller.ControllerUtilsTest.createUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    protected JwtConfigurer jwtConfigurer;

    private String EXPECTED_USERS_DTO = "[" + createUserDto(1).toString().replaceFirst("\\n", "").replaceAll("\\'", "") + "]";
    private String EXPECTED_TASKS_DTO = "[" + createTaskDto(1).toString().replaceFirst("\\n", "").replaceAll("\\'", "") + "]";

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void read_whenValidInput_thenReturns200Test() throws Exception {
        //given
        List<TaskDTO> tasks = List.of(createTaskDto(1));
        when(taskService.getAll()).thenReturn(tasks);

        //when
        MvcResult mvcResult = mockMvc.perform(get("/crm/tasks")
                        .contentType("application/json"))
                .andReturn();

        //then
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        List<TaskDTO> result = objectMapper.readValue(response.getContentAsString(), ArrayList.class);
        assertEquals(EXPECTED_TASKS_DTO, result.toString());

        verify(taskService).getAll();
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void read_whenValidInputDBNotResault_thenReturns404Test() throws Exception {
        //given && when
        List<TaskDTO> tasks = new ArrayList<>();
        when(taskService.getAll()).thenReturn(tasks);

        //then
        mockMvc.perform(get("/crm/tasks")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());

        verify(taskService).getAll();
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void readPageableSort_whenInputValidPageable_thenReturn404Test() throws Exception {
        //given && when
        Sort sort = Sort.by(Sort.Direction.ASC, "id")
                .and(Sort.by(Sort.Direction.DESC, "status"));
        Pageable pageable = PageRequest.of(0, 3, sort);
        when(taskService.getAll(pageable)).thenReturn(Page.empty());

        //then
        mockMvc.perform(get("/crm/tasks/page")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "id,asc")
                        .param("sort", "status,desc"))
                .andExpect(status().isNotFound());

        verify(taskService).getAll(pageable);
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void readPageableSort_whenInputValidPageable_thenReturn200Test() throws Exception {
        //given && when
        Sort sort = Sort.by(Sort.Direction.ASC, "id")
                .and(Sort.by(Sort.Direction.DESC, "status"));
        Pageable pageable = PageRequest.of(0, 3, sort);
        Page<TaskDTO> tasks = new PageImpl<>(List.of(createTaskDto(1)));

        when(taskService.getAll(pageable)).thenReturn(tasks);

        //then
        mockMvc.perform(get("/crm/tasks/page")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "id,asc")
                        .param("sort", "status,desc"))
                .andExpect(status().isOk());

        verify(taskService).getAll(pageable);
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void readPageableSort_whenInputValidPageableDefaultParam_thenReturn200Test() throws Exception {
        //given && when
        Pageable pageable = PageRequest.of(0, 4);
        Page<TaskDTO> tasks = new PageImpl<>(List.of(createTaskDto(1)));

        when(taskService.getAll(pageable)).thenReturn(tasks);

        //then
        mockMvc.perform(get("/crm/tasks/page")
                        .param("page", "")
                        .param("size", "")
                        .param("sort", "")
                        .param("sort", ""))
                .andExpect(status().isOk());

        verify(taskService).getAll(pageable);
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void readTaskByIdTask_whenInputValid_thenReturns200Test() throws Exception {
        //given
        TaskDTO taskDTO = createTaskDto(1);
        when(taskService.getById(taskDTO.getId())).thenReturn(taskDTO);

        //when
        MvcResult mvcResult = mockMvc.perform(get("/crm/tasks/{id}", taskDTO.getId())
                        .contentType("application/json"))
                .andReturn();

        //then
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        TaskDTO result = objectMapper.readValue(response.getContentAsString(), TaskDTO.class);
        assertEquals("Task test B", result.getTitle());
        assertEquals(Status.NEW, result.getStatus());
        assertEquals("Task test info B", result.getInfo());

        verify(taskService).getById(taskDTO.getId());
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void readTaskByIdTask_whenInputValidNotBD_thenReturns404Test() throws Exception {
        //given && when
        Integer taskId = 2;
        when(taskService.getById(taskId)).thenReturn(null);

        //then
        mockMvc.perform(get("/crm/tasks/{id}", taskId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(taskService).getById(taskId);
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void readAllTaskUsersByTaskId_whenInputValidPathVariableTaskId_thenReturns200Test() throws Exception {
        //given
        List<UserDTO> users = List.of(createUserDto(1));
        Integer taskId = 1;
        when(userService.getAllTaskUsersByTaskId(taskId)).thenReturn(users);

        //when
        MvcResult mvcResult = mockMvc.perform(get("/crm/tasks/{id}/users", taskId)
                        .contentType("application/json"))
                .andReturn();

        //then
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        List<UserDTO> result = objectMapper.readValue(response.getContentAsString(), ArrayList.class);
        assertEquals(EXPECTED_USERS_DTO, result.toString());

        verify(userService).getAllTaskUsersByTaskId(taskId);
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void readAllTaskUsersByTaskId_whenInputValidPathVariableTaskId_thenReturns404Test() throws Exception {
        //given && when
        List<UserDTO> users = new ArrayList<>();
        Integer taskId = 1;
        when(userService.getAllTaskUsersByTaskId(taskId)).thenReturn(users);

        //then
        mockMvc.perform(get("/crm/tasks/{id}/users", taskId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());

        verify(userService).getAllTaskUsersByTaskId(taskId);
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void create_whereInputValidTask_thenReturns201Test() throws Exception {
        //given
        TaskDTO task = createTaskDto(1);
        task.setId(null);
        when(taskService.add(task)).thenReturn(task);

        //when
        MvcResult mvcResult = mockMvc.perform(post("/crm/tasks")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(task)))
                .andReturn();

        //then
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
        TaskDTO result = objectMapper.readValue(response.getContentAsString(), TaskDTO.class);
        assertEquals("Task test B", result.getTitle());
        assertEquals(Status.NEW, result.getStatus());
        assertEquals("Task test info B", result.getInfo());

        verify(taskService).add(task);
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void update_whereInputValidTaskAndTaskId_thenReturns200Test() throws Exception {
        //given && when
        TaskDTO task = createTaskDto(1);
        when(taskService.update(task)).thenReturn(task);

        //then
        mockMvc.perform(put("/crm/tasks/{id}", task.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk());

        verify(taskService).update(task);
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void update_whereInputValidTaskAndTaskId_thenReturns304Test() throws Exception {
        //given && when
        TaskDTO task = createTaskDto(1);
        when(taskService.update(task)).thenReturn(null);

        //then
        mockMvc.perform(put("/crm/tasks/{id}", task.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isNotModified());

        verify(taskService).update(task);
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void finishTaskByTaskId_whenInputValidPathVariableTaskId_thenReturns200Test() throws Exception {
        //given && when
        Integer taskId = 2;
        doNothing().when(taskService).finishTaskByTaskId(taskId);

        //then
        mockMvc.perform(delete("/crm/tasks/{id}/users", taskId)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(taskService).finishTaskByTaskId(taskId);
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void delete_whenInputValidPathVariableTaskId_thenReturns200Test() throws Exception {
        //given && when
        Integer taskId = 2;
        doNothing().when(taskService).remove(taskId);

        //then
        mockMvc.perform(delete("/crm/tasks/{id}", taskId)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(taskService).remove(taskId);
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void updateStatus_whenInputValidPathVariableTaskIdAndStatus_thenReturns200Test() throws Exception {
        //given && when
        Status status = Status.NEW;
        TaskDTO task = createTaskDto(1);
        when(taskService.changeStatusDTO(status, task.getId())).thenReturn(task);

        //then
        mockMvc.perform(put("/crm/tasks/{id}/{status}", task.getId(), status)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(status)))
                .andExpect(status().isOk());

        verify(taskService).changeStatusDTO(status, task.getId());
    }

    @Test
    @WithMockUser(username = "Petrov", password = "123", roles = "ADMIN")
    void updateStatus_whenInputNoValidPathVariableTaskIdAndStatus_thenReturns200Test() throws Exception {
        //given && when
        Status status = Status.NEW;
        TaskDTO task = createTaskDto(1);
        when(taskService.changeStatusDTO(status, task.getId())).thenReturn(task);

        //then
        mockMvc.perform(put("/crm/tasks/{id}/{status}", task.getId(), status)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(status)))
                .andExpect(status().isOk());

        verify(taskService).changeStatusDTO(status, task.getId());
    }

}