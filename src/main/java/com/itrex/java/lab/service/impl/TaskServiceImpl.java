package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.dto.TaskDTO;
import com.itrex.java.lab.entity.Status;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.service.TaskService;
import com.itrex.java.lab.service.UserService;
import com.itrex.java.lab.utils.ConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.itrex.java.lab.utils.ConverterUtils.convertTaskToDto;

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
                    .map(ConverterUtils::convertTaskToDto)
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
    public List<TaskDTO> getAllTasksByUserId(Integer userId) throws CRMProjectServiceException {
        try {
            return taskRepository.selectAllTasksByUserId(userId).stream()
                    .map(ConverterUtils::convertTaskToDto)
                    .collect(Collectors.toList());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: SELECT TASK BY USER: ", ex);
        }
    }

    @Override
    public TaskDTO add(TaskDTO task) throws CRMProjectServiceException {
        try {
            Task addTask = Task.builder()
                    .title(task.getTitle())
                    .status(Status.NEW)
                    .deadline(task.getDeadline())
                    .info(task.getInfo())
                    .build();
            return convertTaskToDto(taskRepository.add(addTask));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: ADD TASK:", ex);
        }
    }

    @Override
    public TaskDTO update(TaskDTO task) throws CRMProjectServiceException {
        try {
            Task update = taskRepository.selectById(task.getId());
            if (update == null) {
                throw new CRMProjectServiceException("TASK NO FOUND DATA BASE");
            }
            update.setTitle(task.getTitle());
            update.setStatus(task.getStatus());
            update.setDeadline(task.getDeadline());
            update.setInfo(task.getInfo());
            return convertTaskToDto(taskRepository.update(update));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: UPDATE TASK:", ex);
        }
    }


    @Override
    @Transactional
    public void revokeAllTasksFromUserId(Integer userId) throws CRMProjectServiceException {
        try {
            User user = userRepository.selectById(userId);
            user.setTasks(new ArrayList<>());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE ALL TASKS BY USER:", ex);
        }
    }

    @Override
    @Transactional
    public void remove(Integer taskId) throws CRMProjectServiceException {
        try {
            userService.revokeAllUsersFromTaskId(taskId);
            taskRepository.remove(taskId);
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE TASK:", ex);
        }
    }

}
