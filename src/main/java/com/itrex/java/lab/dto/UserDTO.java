package com.itrex.java.lab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * The User class is responsible for the user.
 * task and user are linked in a cross table crm.user_task
 */

@AllArgsConstructor
@Data
@Builder
public class UserDTO {

    private Integer id;
    private String login;
    private String psw;
    private String firstName;
    private String lastName;
    private RoleDTO role;

    @Override
    public String toString() {
        return "\nUserDTO{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", psw='" + psw + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                '}';
    }

}
