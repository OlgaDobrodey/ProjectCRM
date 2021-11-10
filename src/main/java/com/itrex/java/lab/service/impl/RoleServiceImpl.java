package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.dto.RoleDTO;
import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.service.RoleService;
import com.itrex.java.lab.utils.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.itrex.java.lab.utils.Convert.convertRoleToDto;
import static com.itrex.java.lab.utils.Convert.convertRoleToEntity;

@Service
public class RoleServiceImpl implements RoleService {

    private static final int DEFAULT_ROLE = 2;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<RoleDTO> getAllRole() throws CRMProjectServiceException {
        try {
            return roleRepository.selectAll().stream()
                    .map(Convert::convertRoleToDto)
                    .collect(Collectors.toList());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: GET ALL ROLE:", ex);
        }
    }

    @Override
    public RoleDTO getById(Integer id) throws CRMProjectServiceException {
        try {
            return Optional.ofNullable(roleRepository.selectById(id))
                    .map(Convert::convertRoleToDto)
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

    @Override
    public void removeRole(Integer idRoleDTO) throws CRMProjectServiceException {
        replaceOnDefaultRoleAndRemoveRole(idRoleDTO, DEFAULT_ROLE);
    }

    @Override
    @Transactional
    public void replaceOnDefaultRoleAndRemoveRole(Integer idRoleDTO, Integer idDefaultRoleDTO) throws CRMProjectServiceException {

        if (idRoleDTO == idDefaultRoleDTO) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE ROLE BY ID = " + idRoleDTO + " equals DEFAULT_ROLE ");
        }
        try {
            Role role = roleRepository.selectById(idRoleDTO);
            if (role == null) {
                throw new CRMProjectServiceException("ERROR SERVICE: DELETE ROLE BY ID = " + idRoleDTO + " NO FOUND DATA DASE");
            }
            userRepository.updateRoleOnDefaultByUsers(role, roleRepository.selectById(idDefaultRoleDTO));
            roleRepository.removeRole(idRoleDTO);
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE ROLE(CHANGE ON DEFAULT ROLE) - " + idRoleDTO + " : ", ex);
        }
    }
}
