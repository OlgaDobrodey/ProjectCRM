package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.dto.RoleDTO;
import com.itrex.java.lab.dto.TaskDTO;
import com.itrex.java.lab.dto.UserDTO;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.service.UserService;
import com.itrex.java.lab.utils.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.itrex.java.lab.utils.Convert.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
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
            User user = userRepository.selectById(id);
            return user != null ? convertUserToDto(user) : null;
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
            userRepository.selectAll().forEach(user -> {
                        try {
                            userRepository
                                    .selectAllTasksByUser(user)
                                    .forEach(task -> System.out.printf("%d - %s : %d - %s\n",
                                            user.getId(), user.getLogin(), task.getId(), task.getTitle()));
                        } catch (CRMProjectRepositoryException e) {
                            e.printStackTrace();
                        }
                    }
            );
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
        try {
            userRepository.addTaskByUser(convertTaskToEntity(task), convertUserToEntity(user));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: ADD TASK BY USER:", ex);
        }
    }

    @Override
    public UserDTO update(UserDTO userDTO) throws CRMProjectServiceException {
        try {
            if (userRepository.selectById(userDTO.getId()) == null) {
                throw new CRMProjectServiceException("ERROR SERVICE: UPDATE USER: USER BY ID "
                        + userDTO.getId() + " NO FOUND DATA BASE");
            }
            return convertUserToDto(userRepository.update(convertUserToEntity(userDTO)));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: UPDATE USER:", ex);
        }
    }

    @Override
    @Transactional(propagation= Propagation.NESTED)
    public List<UserDTO> updateRoleOnDefaultByUsers(RoleDTO role, RoleDTO defaultRole) throws CRMProjectServiceException {
        try {
            return userRepository.updateRoleOnDefaultByUsers(convertRoleToEntity(role), convertRoleToEntity(defaultRole))
                    .stream().map(Convert::convertUserToDto)
                    .collect(Collectors.toList());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: UPDATE ROLE ON DEFAULT ROLE FOR USERS:", ex);
        }
    }

    @Transactional
    @Override
    public void remove(Integer idUser) throws CRMProjectServiceException {
        try {
            if (userRepository.selectById(idUser) == null) {
                throw new CRMProjectServiceException("ERROR SERVICE: DELETE USER: no found Data BASE");
            }
            userRepository.remove(idUser);
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE USER:", ex);
        }
    }

    @Transactional
    @Override
    public void removeTaskByUser(Integer idTask, Integer idUser) throws CRMProjectServiceException {
        try {
            Task task = taskRepository.selectById(idTask);
            User user = userRepository.selectById(idUser);
            if (user == null || task == null) {
                throw new CRMProjectServiceException("ERROR SERVICE: DELETE TASK BY USER:");
            }
            userRepository.removeTaskByUser(idTask, idUser);
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE TASK BY USER:", ex);
        }
    }

    @Override
    public void removeAllTasksByUser(Integer idUser) throws CRMProjectServiceException {
        try {
            userRepository.removeAllTasksByUser(idUser);
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE ALL TASKS BY USER:", ex);
        }
    }
}
