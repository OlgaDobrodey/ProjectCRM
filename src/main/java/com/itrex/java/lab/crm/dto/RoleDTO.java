package com.itrex.java.lab.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * The role class is responsible for the user's role.
 * role's name is only roleName.toUpperCase();
 * Default role = Role {id = 2, roleName = "User"}
 * If the this.role is removed, this.role is changed to default role in all users
 */

@AllArgsConstructor
@Data
@Builder
public class RoleDTO {

    private Integer id;
    @NonNull
    private String roleName;

    @Override
    public String toString() {
        return "\n{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }

}

