package com.itrex.java.lab.service.impl;

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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.itrex.java.lab.utils.Convert.convertUserToDto;
import static com.itrex.java.lab.utils.Convert.convertUserToEntity;

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
    public List<UserDTO> getAllUsersByTaskDTO(Integer idTask) throws CRMProjectServiceException {
        try {
            return userRepository.selectAllUsersByTask(idTask)
                    .stream().map(Convert::convertUserToDto)
                    .collect(Collectors.toList());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: GET ALL USER BY TASK:", ex);
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
    @Transactional
    public void addTaskByUser(Integer idTask, Integer idUser) throws CRMProjectServiceException {
        try {
            userRepository.selectById(idUser)
                    .getTasks()
                    .add(taskRepository.selectById(idTask));
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
            user.getTasks().remove(task);
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE TASK BY USER:", ex);
        }
    }

    @Override
    @Transactional
    public void removeAllUsersByTask(Integer idTask) throws CRMProjectServiceException {
        try {

            List<User> users = userRepository.selectAllUsersByTask(idTask);
            users.forEach(user -> user.getTasks().removeIf(task -> task.getId() == idTask));

        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE ALL TASK:", ex);
        }
    }
}
