package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Role;

import java.sql.SQLException;
import java.util.List;

public interface RoleRepository {

    List<Role> selectAll() throws SQLException;

    Role selectById(Integer id) throws SQLException;

    Role add(Role role) throws SQLException;

    List<Role> addAll(List<Role> roles) throws SQLException;

    Role update(Role role, Integer id) throws SQLException;

    /**
     * when deleting a role, the role is replaced on default,
     *
     * @param role - role for delete;
     * @return true - if role remove; false -if basa date don't have
     */
    boolean remove(Role role) throws SQLException;

    /**
     * when deleting a role, the role is replaced on default,
     *
     * @param role - role for delete; defaultRole - role to replace a deleted role.
     * @return true - if role remove; false -if basa date don't have
     */
    boolean remove(Role role, Role defaultRole) throws SQLException;
}
