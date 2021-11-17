package com.itrex.java.lab.utils;

import com.itrex.java.lab.dto.RoleDTO;
import com.itrex.java.lab.dto.TaskDTO;
import com.itrex.java.lab.dto.UserDTO;
import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;

public class ConverterUtils {

    public static RoleDTO convertRoleToDto(Role role) {
        return RoleDTO.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .build();
    }

    public static Role convertRoleToEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setRoleName(roleDTO.getRoleName());
        role.setId(roleDTO.getId());

        return role;
    }

    public static UserDTO convertUserToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .psw(user.getPsw())
                .role(convertRoleToDto(user.getRole()))
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .build();
    }

    public static User convertUserToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setLogin(userDTO.getLogin());
        user.setPsw(userDTO.getPsw());
        user.setRole(convertRoleToEntity(userDTO.getRole()));
        user.setLastName(userDTO.getLastName());
        user.setFirstName(userDTO.getFirstName());

        return user;
    }

    public static TaskDTO convertTaskToDto(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .status(task.getStatus())
                .info(task.getInfo())
                .deadline(task.getDeadline())
                .build();
    }

    public static Task convertTaskToEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setTitle(taskDTO.getTitle());
        task.setStatus(taskDTO.getStatus());
        task.setInfo(taskDTO.getInfo());
        task.setDeadline(taskDTO.getDeadline());

        return task;
    }

}
