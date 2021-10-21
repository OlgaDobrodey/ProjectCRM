package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;

import java.util.List;

public interface RoleRepository {

    List<Role> selectAll() throws CRMProjectRepositoryException;

    Role selectById(Integer id) throws CRMProjectRepositoryException;

    Role add(Role role) throws CRMProjectRepositoryException;

    List<Role> addAll(List<Role> roles) throws CRMProjectRepositoryException;

    Role update(Role role, Integer id) throws CRMProjectRepositoryException;

    /**
     * when deleting a role, the role is replaced on default,
     *
     * @param role - role for delete;
     * @return true - if role remove; false -if basa date don't have
     */
    boolean remove(Role role) throws CRMProjectRepositoryException;

    /**
     * when deleting a role, the role is replaced on default,
     *
     * @param role - role for delete; defaultRole - role to replace a deleted role.
     * @return true - if role remove; false -if basa date don't have
     */
    boolean remove(Role role, Role defaultRole) throws CRMProjectRepositoryException;
}
