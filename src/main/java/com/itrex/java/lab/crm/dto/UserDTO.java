package com.itrex.java.lab.crm.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The User class is responsible for the user.
 * task and user are linked in a cross table crm.user_task
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO {

    private Integer id;
    @NotNull
    private String login;
    @NotNull
    private String psw;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private Integer roleId;

    @Override
    public String toString() {
        return "\nUserDTO{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", psw='" + psw + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + roleId +
                '}';
    }

}
