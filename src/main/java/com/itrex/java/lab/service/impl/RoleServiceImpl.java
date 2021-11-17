package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.dto.RoleDTO;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.service.RoleService;
import com.itrex.java.lab.utils.ConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.itrex.java.lab.utils.ConverterUtils.convertRoleToDto;
import static com.itrex.java.lab.utils.ConverterUtils.convertRoleToEntity;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDTO> getAllRole() throws CRMProjectServiceException {
        try {
            return roleRepository.selectAll().stream()
                    .map(ConverterUtils::convertRoleToDto)
                    .collect(Collectors.toList());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: GET ALL ROLE:", ex);
        }
    }

    @Override
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
    public RoleDTO addRole(RoleDTO role) throws CRMProjectServiceException {
        try {
            return convertRoleToDto(roleRepository.add(convertRoleToEntity(role)));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: ADD ROLE:", ex);
        }
    }

    @Override
    @Transactional
    public RoleDTO updateRole(RoleDTO roleDTO) throws CRMProjectServiceException {
        try {
            if (roleRepository.selectById(roleDTO.getId()) == null) {
                throw new CRMProjectServiceException("ERROR " + roleDTO + "NOT FOUND IN DATA BASE");
            }
            return convertRoleToDto(roleRepository.update(convertRoleToEntity(roleDTO)));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: UPDATE ROLE - " + roleDTO.getRoleName() + " : ", ex);
        }
    }

}
