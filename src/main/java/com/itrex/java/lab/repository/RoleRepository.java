package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;

import java.util.List;

public interface RoleRepository {

    /**
     * select All role
     * @return list of role, if role do not in Data Base return empty list
     * @throws CRMProjectRepositoryException
     */
    List<Role> selectAll() throws CRMProjectRepositoryException;

    Role selectById(Integer id) throws CRMProjectRepositoryException;

    Role add(Role role) throws CRMProjectRepositoryException;

    List<Role> addAll(List<Role> roles) throws CRMProjectRepositoryException;

    /**
     * update role, if role is not founded in DB return null;
     * @param role -all param's role for change
     * @param id - role id to change
     * @return changed role
     * @throws CRMProjectRepositoryException
     */
    Role update(Role role, Integer id) throws CRMProjectRepositoryException;

    /**
     * when deleting a role, the role is replaced on default,
     * Default role = Role {id = 2, roleName = "User"}
     * @param role - role for delete;
     * @return true - if role remove; false -if basa date don't have
     */
    boolean remove(Role role) throws CRMProjectRepositoryException;

    /**
     * when deleting a role, the role is replaced on default,
     * Default role = Role {id = 2, roleName = "User"}
     * @param role - role for delete; defaultRole - role to replace a deleted role.
     * @return true - if role remove; false -if basa date don't have
     */
    boolean remove(Role role, Role defaultRole) throws CRMProjectRepositoryException;
}
