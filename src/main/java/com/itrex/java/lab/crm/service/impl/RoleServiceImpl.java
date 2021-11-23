package com.itrex.java.lab.crm.service.impl;

import com.itrex.java.lab.crm.dto.RoleDTO;
import com.itrex.java.lab.crm.entity.Role;
import com.itrex.java.lab.crm.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.crm.repository.RoleRepository;
import com.itrex.java.lab.crm.service.RoleService;
import com.itrex.java.lab.crm.utils.ConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.itrex.java.lab.crm.utils.ConverterUtils.convertRoleToDto;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRoles() throws CRMProjectServiceException {
        try {
            return roleRepository.selectAll().stream()
                    .map(ConverterUtils::convertRoleToDto)
                    .collect(Collectors.toList());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: GET ALL ROLE:", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO getById(Integer id) throws CRMProjectServiceException {
        try {
            return Optional.ofNullable(roleRepository.selectById(id))
                    .map(ConverterUtils::convertRoleToDto)
                    .orElse(null);
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: SELECT ROLE BY ID: ", ex);
        }
    }

    @Override
    @Transactional
    public RoleDTO addRole(RoleDTO role) throws CRMProjectServiceException {
        try {
            Role newRole = Role.builder().roleName(role.getRoleName()).build();
            return convertRoleToDto(roleRepository.add(newRole));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: ADD ROLE:", ex);
        }
    }

    @Override
    @Transactional
    public RoleDTO updateRole(RoleDTO roleDTO) throws CRMProjectServiceException {
        try {
            Role role = roleRepository.selectById(roleDTO.getId());
            if (role == null) {
                throw new CRMProjectServiceException("ERROR " + roleDTO + "NOT FOUND IN DATA BASE");
            }
            role.setRoleName(roleDTO.getRoleName());
            return convertRoleToDto(roleRepository.update(role));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: UPDATE ROLE - " + roleDTO.getRoleName() + " : ", ex);
        }
    }

}
