package com.itrex.java.lab.crm.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class UserDTOLogin {

    @NotNull
    private String login;
    @NotNull
    private String psw;

}
