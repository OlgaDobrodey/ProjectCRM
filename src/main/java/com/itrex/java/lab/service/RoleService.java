package com.itrex.java.lab.service;

import com.itrex.java.lab.dto.RoleDTO;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;

import java.util.List;

public interface RoleService {

    List<RoleDTO> getAllRole() throws CRMProjectServiceException;
    RoleDTO getById(Integer id) throws CRMProjectServiceException;
    RoleDTO addRole(RoleDTO role) throws CRMProjectServiceException;
    RoleDTO updateRole(RoleDTO role) throws CRMProjectServiceException;
    void removeRole(Integer idRole) throws CRMProjectServiceException;

    /**
     * when deleting a roleDTO, the roleDTO is replaced on defaultDTO,
     * Default roleDTO = RoleDTO {id = 2, roleName = "User"}
     * @param idRoleDTO - id roleDTO for delete; idDefaultRoleDTO - roleDTO to replace a deleted roleDTO.
     * @return true - if role remove; false -if basa date don't have
     */
   void replaceOnDefaultRoleAndRemoveRole(Integer idRoleDTO, Integer idDefaultRoleDTO) throws CRMProjectServiceException;



}
