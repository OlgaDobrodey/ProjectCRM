package com.itrex.java.lab.crm.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDtoLoginRoleName {

    private Integer id;
    @NotNull
    private String login;
    @NotNull
    private String psw;
    @NotNull
    private String roleName;

    @Override
    public String toString() {
        return "\nUserDTORoleName{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", psw='" + psw + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }

}

