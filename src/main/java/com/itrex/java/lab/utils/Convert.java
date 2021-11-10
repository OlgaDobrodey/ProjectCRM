package com.itrex.java.lab.utils;

import com.itrex.java.lab.dto.RoleDTO;
import com.itrex.java.lab.dto.TaskDTO;
import com.itrex.java.lab.dto.UserDTO;
import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;

public class Convert {

    public static RoleDTO convertRoleToDto(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setRoleName(role.getRoleName());
        return roleDTO;
    }


    public static Role convertRoleToEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setRoleName(roleDTO.getRoleName());
        role.setId(roleDTO.getId());
        return role;
    }

    public static UserDTO convertUserToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setLogin(user.getLogin());
        userDTO.setPsw(user.getPsw());
        userDTO.setRole(convertRoleToDto(user.getRole()));
        userDTO.setLastName(user.getLastName());
        userDTO.setFirstName(user.getFirstName());
        return userDTO;
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
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setInfo(task.getInfo());
        taskDTO.setDeadline(task.getDeadline());
        return taskDTO;
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
