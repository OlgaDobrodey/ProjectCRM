package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.dto.TaskDTO;
import com.itrex.java.lab.dto.UserDTO;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.service.TaskService;
import com.itrex.java.lab.utils.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.itrex.java.lab.utils.Convert.convertTaskToDto;
import static com.itrex.java.lab.utils.Convert.convertTaskToEntity;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
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
    public List<UserDTO> getAllUsersByTaskDTO(TaskDTO task) throws CRMProjectServiceException {
        try {
            return taskRepository.selectAllUsersByTask(convertTaskToEntity(task))
                    .stream().map(Convert::convertUserToDto)
                    .collect(Collectors.toList());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: GET ALL USER BY TASK:", ex);
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
    public void remove(Integer idTask) throws CRMProjectServiceException {
        try {
            if (getById(idTask) == null) {
                throw new CRMProjectServiceException("ERROR SERVICE: DELETE TASK: TaskDTO NO FOUND DATA BASE");
            }
            removeAllUsersByTask(idTask);
            taskRepository.remove(idTask);
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE TASK:", ex);
        }
    }

    @Override
    @Transactional
    public void removeAllUsersByTask(Integer idTask) throws CRMProjectServiceException {
        try {
            List<User> users = taskRepository.selectAllUsersByTask(taskRepository.selectById(idTask));
            for (User user : users) {
                userRepository.removeTaskByUser(idTask, user.getId());
            }
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE ALL TASK:", ex);
        }
    }
}
