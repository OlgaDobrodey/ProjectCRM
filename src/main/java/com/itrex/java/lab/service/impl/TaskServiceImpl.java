package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.dto.TaskDTO;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.service.TaskService;
import com.itrex.java.lab.service.UserService;
import com.itrex.java.lab.utils.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.itrex.java.lab.utils.Convert.convertTaskToDto;
import static com.itrex.java.lab.utils.Convert.convertTaskToEntity;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public List<TaskDTO> getAll() throws CRMProjectServiceException {
        try {
            return taskRepository.selectAll().stream()
                    .map(Convert::convertTaskToDto)
                    .collect(Collectors.toList());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: GET ALL TASK:", ex);
        }
    }

    @Override
    public TaskDTO getById(Integer id) throws CRMProjectServiceException {
        try {
            Task task = taskRepository.selectById(id);
            return task != null ? convertTaskToDto(task) : null;
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: GET BY ID TASK:", ex);
        }
    }

    @Override
    public List<TaskDTO> getAllTasksByUser(Integer idUser) throws CRMProjectServiceException {
        try {
            return taskRepository.selectAllTasksByUser(idUser).stream()
                    .map(task -> convertTaskToDto(task))
                    .collect(Collectors.toList());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: SELECT TASK BY USER: ", ex);
        }
    }

    @Override
    public TaskDTO add(TaskDTO task) throws CRMProjectServiceException {
        try {
            return convertTaskToDto(taskRepository.add(convertTaskToEntity(task)));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: ADD TASK:", ex);
        }
    }

    @Override
    public List<TaskDTO> addAll(List<TaskDTO> tasksDTO) throws CRMProjectServiceException {
        try {
            List<Task> tasks = tasksDTO.stream().map(Convert::convertTaskToEntity).collect(Collectors.toList());
            return taskRepository.addAll(tasks)
                    .stream().map(Convert::convertTaskToDto)
                    .collect(Collectors.toList());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: ADD ALL TASKs:", ex);
        }
    }

    @Override
    public TaskDTO update(TaskDTO task) throws CRMProjectServiceException {
        try {
            if (getById(task.getId()) == null) {
                throw new CRMProjectServiceException("TASK NO FOUND DATA BASE");
            }
            return convertTaskToDto(taskRepository.update(convertTaskToEntity(task)));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: UPDATE TASK:", ex);
        }
    }


    @Override
    @Transactional
    public void removeAllTasksByUser(Integer idUser) throws CRMProjectServiceException {
        try {
            User user = userRepository.selectById(idUser);
            user.setTasks(new ArrayList<>());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE ALL TASKS BY USER:", ex);
        }
    }

    @Override
    @Transactional
    public void remove(Integer idTask) throws CRMProjectServiceException {
        try {
            userService.removeAllUsersByTask(idTask);
            taskRepository.remove(idTask);
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE TASK:", ex);
        }
    }
}
