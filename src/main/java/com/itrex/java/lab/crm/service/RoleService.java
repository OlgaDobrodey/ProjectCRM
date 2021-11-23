package com.itrex.java.lab.crm.service;

import com.itrex.java.lab.crm.dto.RoleDTO;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;

import java.util.List;

public interface RoleService {

    List<RoleDTO> getAllRoles() throws CRMProjectServiceException;

    RoleDTO getById(Integer id) throws CRMProjectServiceException;

    RoleDTO addRole(RoleDTO role) throws CRMProjectServiceException;

    RoleDTO updateRole(RoleDTO role) throws CRMProjectServiceException;

}
