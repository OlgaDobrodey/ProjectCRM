package com.itrex.java.lab.crm.controller;

import com.itrex.java.lab.crm.dto.RoleDTO;
import com.itrex.java.lab.crm.dto.TaskDTO;
import com.itrex.java.lab.crm.dto.UserDTO;
import com.itrex.java.lab.crm.entity.Status;

import java.time.LocalDate;

public class ControllerUtilsTest {

    public static UserDTO createUserDto(Integer userId) {
        return UserDTO.builder()
                .id(userId)
                .login("TestLogin" + (char) ('@' + (userId + 1)))
                .psw("123")
                .firstName("TestFirstName" + (char) ('@' + (userId + 1)))
                .lastName("TestLastName" + (char) ('@' + (userId + 1)))
                .roleId(1)
                .build();
    }

    public static TaskDTO createTaskDto(Integer taskId) {
        return TaskDTO.builder()
                .id(taskId)
                .title("Task test " + (char)('@' + (taskId+ 1)))
                .status(Status.NEW)
                .info("Task test info " + (char)('@' + (taskId+ 1)))
                .deadline(LocalDate.of(2001, 1, 1))
                .build();
    }

    public static RoleDTO createRoleDto(Integer roleId) {
        return RoleDTO.builder()
                .id(roleId)
                .roleName("TestRole" + (char) ('@' + (roleId + 1)))
                .build();
    }

}
