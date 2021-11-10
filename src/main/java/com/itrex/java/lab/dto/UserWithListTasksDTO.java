package com.itrex.java.lab.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * The User class is responsible for the user.
 * task and user are linked in a cross table crm.user_task
 */

@NoArgsConstructor
@Getter
@Setter
public class UserWithListTasksDTO {

    private Integer id;
    private String login;
    private String psw;
    private String firstName;
    private String lastName;
    private RoleDTO role;
    private List<TaskDTO> taskDTOS;

    @Override
    public String toString() {
        return "\nUserWithListTasksDTO{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", psw='" + psw + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                ", taskDTOS=" + taskDTOS +
                '}';
    }
}
