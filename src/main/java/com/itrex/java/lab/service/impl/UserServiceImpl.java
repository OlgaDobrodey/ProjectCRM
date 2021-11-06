package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.dto.RoleDTO;
import com.itrex.java.lab.dto.TaskDTO;
import com.itrex.java.lab.dto.UserDTO;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.itrex.java.lab.utils.Convector.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDTO> getAll() throws CRMProjectServiceException {
        try {
            return userRepository.selectAll().stream()
                    .map(user -> convertUserToDto(user))
                    .collect(Collectors.toList());
        } catch (CRMProjectRepositoryException ex) {
           throw new CRMProjectServiceException("ERROR SERVICE: GET ALL USER:", ex);
        }
    }

    @Override
    public UserDTO getById(Integer id) throws CRMProjectServiceException {
        try {
            return convertUserToDto(userRepository.selectById(id));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: SELECT USER BY ID: ", ex);
        }
    }

    @Override
    public List<TaskDTO> getAllTasksByUser(UserDTO user) throws CRMProjectServiceException {
        try {
            return userRepository.selectAllTasksByUser(convertUserToEntity(user)).stream()
                    .map(task -> convertTaskToDto(task))
                    .collect(Collectors.toList());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: SELECT TASK BY USER: ", ex);
        }
    }

    @Override
    public void printCrossTable() throws CRMProjectServiceException {
        try {
            userRepository.printCrossTable();
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: PRINT CROSS TABLE: ", ex);
        }
    }

    @Override
    public UserDTO add(UserDTO user) throws CRMProjectServiceException {
        try {
            return convertUserToDto(userRepository.add(convertUserToEntity(user)));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: ADD USER:", ex);
        }
    }

    @Override
    public List<UserDTO> addAll(List<UserDTO> usersDTO) throws CRMProjectServiceException {
        try {
            List<User> users = usersDTO.stream().map(userDTO -> convertUserToEntity(userDTO)).collect(Collectors.toList());
            return userRepository.addAll(users).stream().map(user -> convertUserToDto(user)).collect(Collectors.toList());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: ADD ALL USER:", ex);
        }
    }

    @Override
    public void addTaskByUser(TaskDTO task, UserDTO user) throws CRMProjectServiceException {
        try{
            userRepository.addTaskByUser(convertTaskToEntity(task),convertUserToEntity(user));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: ADD TASK BY USER:", ex);
        }
    }

    @Override
    public UserDTO update(UserDTO user, Integer id) throws CRMProjectServiceException {
        try {
            return convertUserToDto(userRepository.update(convertUserToEntity(user),id));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: UPDATE USER:", ex);
        }
    }

    @Override
    public List<UserDTO> updateRoleOnDefaultByUsers(RoleDTO role, RoleDTO defaultRole) throws CRMProjectServiceException {
        try {
            return userRepository.updateRoleOnDefaultByUsers(convertRoleToEntity(role),convertRoleToEntity(defaultRole))
                    .stream().map(user -> convertUserToDto(user))
                    .collect(Collectors.toList());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: UPDATE ROLE ON DEFAULT ROLE FOR USERS:", ex);
        }
    }

    @Override
    public boolean remove(UserDTO user) throws CRMProjectServiceException {
        try {
            return userRepository.remove(convertUserToEntity(user));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE USER:", ex);
        }
    }

    @Override
    public boolean removeTaskByUser(TaskDTO task, UserDTO user) throws CRMProjectServiceException {
        try {
            return userRepository.removeTaskByUser(convertTaskToEntity(task),convertUserToEntity(user));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE TASK BY USER:", ex);
        }
    }

    @Override
    public void removeAllTasksByUser(UserDTO user) throws CRMProjectServiceException {
        try {
            userRepository.removeAllTasksByUser(convertUserToEntity(user));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE ALL TASKS BY USER:", ex);
        }
    }
}
