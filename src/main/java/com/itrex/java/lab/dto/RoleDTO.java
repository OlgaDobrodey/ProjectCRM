package com.itrex.java.lab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The role class is responsible for the user's role.
 * role's name is only roleName.toUpperCase();
 * Default role = Role {id = 2, roleName = "User"}
 * If the this.role is removed, this.role is changed to default role in all users
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleDTO {

    private Integer id;
    private String roleName;

    public Integer getId() {
        return this.id;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "\nRoleDTO{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}

