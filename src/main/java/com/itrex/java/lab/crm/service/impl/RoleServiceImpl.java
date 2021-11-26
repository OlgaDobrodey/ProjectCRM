package com.itrex.java.lab.crm.service.impl;

import com.itrex.java.lab.crm.dto.RoleDTO;
import com.itrex.java.lab.crm.entity.Role;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.crm.repository.impl.data.RoleRepository;
import com.itrex.java.lab.crm.service.RoleService;
import com.itrex.java.lab.crm.utils.ConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public List<RoleDTO> getAllRoles() {

        return roleRepository.findAll().stream()
                .map(ConverterUtils::convertRoleToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO getById(Integer id) {

        return roleRepository.findById(id)
                .map(ConverterUtils::convertRoleToDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public RoleDTO addRole(RoleDTO role) throws CRMProjectServiceException {
        verification(role);

        Role newRole = Role.builder().roleName(role.getRoleName()).build();
        return convertRoleToDto(roleRepository.save(newRole));
    }

    @Override
    @Transactional
    public RoleDTO updateRole(RoleDTO roleDTO) throws CRMProjectServiceException {
        verification(roleDTO);

        Role role = roleRepository.findById(roleDTO.getId()).orElseThrow(() -> new CRMProjectServiceException("ERROR " + roleDTO + "NOT FOUND IN DATA BASE"));

        role.setRoleName(roleDTO.getRoleName());
        return convertRoleToDto(roleRepository.save(role));
    }

    /*
    Проверка роли:
     -не должна быть пустой,
     -не должна быть больше 10 символов,
     -должна состоять только из букв.
     */
    private void verification(RoleDTO role) throws CRMProjectServiceException {
        if (role.getRoleName().isBlank() || !role.getRoleName().matches("[a-zA-z]{1,10}$")) {
            throw new CRMProjectServiceException("No valid role");
        }
        role.setRoleName(role.getRoleName().toUpperCase());
    }

}
