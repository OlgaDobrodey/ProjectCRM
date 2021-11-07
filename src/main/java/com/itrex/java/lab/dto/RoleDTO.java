package com.itrex.java.lab.dto;

import com.itrex.java.lab.entity.Role;

/**
 * The role class is responsible for the user's role.
 * role's name is only roleName.toUpperCase();
 * Default role = Role {id = 2, roleName = "User"}
 * If the this.role is removed, this.role is changed to default role in all users
 */

public class RoleDTO {

    private Integer id;
    private String roleName;

    public RoleDTO(Role role){
        this.id = role.getId();
        this.roleName = role.getRoleName();
    }

    public RoleDTO(Integer id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public RoleDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName.toUpperCase();
    }

    @Override
    public String toString() {
        return "\nRoleDTO{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}

