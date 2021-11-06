package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.dto.RoleDTO;
import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.service.RoleService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.itrex.java.lab.utils.Convector.convertRoleToDto;
import static com.itrex.java.lab.utils.Convector.convertRoleToEntity;

@Service
public class RoleServiceImpl implements RoleService {

    private static final int DEFAULT_ROLE = 2;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleServiceImpl(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<RoleDTO> getAllRole() throws CRMProjectServiceException {
        try {
            return roleRepository.selectAll().stream()
                    .map(role -> convertRoleToDto(role))
                    .collect(Collectors.toList());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: GET ALL ROLE:", ex);
        }
    }

    @Override
    public RoleDTO selectById(Integer id) throws CRMProjectServiceException {
        try {
            return convertRoleToDto(roleRepository.selectById(id));
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
    public RoleDTO updateRole(Integer id, RoleDTO roleDTO) throws CRMProjectServiceException {
        try {
            Role role = convertRoleToEntity(roleDTO);
            return convertRoleToDto(roleRepository.update(role, id));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: UPDATE ROLE - " + roleDTO.getRoleName() + " : ", ex);
        }
    }

    @Override
    @Transactional
    public boolean removeRole(RoleDTO roleDTO) throws CRMProjectServiceException {
        if (roleDTO.getRoleName() == null) {
            return false;
        }
        if (roleDTO.getId() == DEFAULT_ROLE) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE ROLE - " + roleDTO.getRoleName() + " equals DEFAULT_ROLE ");
        }
        try {
            Role role = convertRoleToEntity(roleDTO);
            userRepository.updateRoleOnDefaultByUsers(role, convertRoleToEntity(selectById(DEFAULT_ROLE)));
            return roleRepository.removeRole(role);
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE ROLE(CHANGE ON DEFAULT ROLE) - " + roleDTO.getRoleName() + " : ", ex);
        }
    }

}
