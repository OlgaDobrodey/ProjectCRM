package com.itrex.java.lab.crm.service;

import com.itrex.java.lab.crm.dto.RoleDTO;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;

import java.util.List;

public interface RoleService {

    List<RoleDTO> getAllRoles();

    RoleDTO getById(Integer id);

    RoleDTO addRole(RoleDTO role);

    RoleDTO updateRole(RoleDTO role) throws CRMProjectServiceException;

}
