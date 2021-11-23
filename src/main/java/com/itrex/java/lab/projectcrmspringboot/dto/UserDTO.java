package com.itrex.java.lab.projectcrmspringboot.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

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
    @NotBlank
    private String login;
    @NotNull
    @NotBlank
    private String psw;
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;
//    @NotNull
//    @NotBlank
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
