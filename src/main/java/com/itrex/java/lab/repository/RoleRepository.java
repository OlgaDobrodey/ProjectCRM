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
     * @return changed role
     * @throws CRMProjectRepositoryException
     */
    Role update(Role role) throws CRMProjectRepositoryException;
    void removeRole(Integer idRole) throws CRMProjectRepositoryException;
}
