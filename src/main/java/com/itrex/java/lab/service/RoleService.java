package com.itrex.java.lab.service;

import com.itrex.java.lab.dto.RoleDTO;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;

import java.util.List;

public interface RoleService {

    List<RoleDTO> getAllRole() throws CRMProjectServiceException;
    RoleDTO selectById(Integer id) throws CRMProjectServiceException;
    RoleDTO addRole(RoleDTO role) throws CRMProjectServiceException;
    RoleDTO updateRole(Integer id, RoleDTO role) throws CRMProjectServiceException;
    boolean removeRole(RoleDTO role) throws CRMProjectServiceException;



}
