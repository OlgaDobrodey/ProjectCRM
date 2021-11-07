package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.dto.TaskDTO;
import com.itrex.java.lab.dto.UserDTO;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.itrex.java.lab.utils.Convert.*;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<TaskDTO> getAll() throws CRMProjectServiceException {
        try {
            return taskRepository.selectAll().stream()
                    .map(task -> convertTaskToDto(task))
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
                    .stream().map(user -> convertUserToDto(user))
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
            List<Task> tasks = tasksDTO.stream().map(taskDTO -> convertTaskToEntity(taskDTO)).collect(Collectors.toList());
            return taskRepository.addAll(tasks)
                    .stream().map(task -> convertTaskToDto(task))
                    .collect(Collectors.toList());
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: ADD ALL TASKs:", ex);
        }
    }

    @Override
    public void addUserByTask(TaskDTO task, UserDTO user) throws CRMProjectServiceException {
        try {
            taskRepository.addUserByTask(convertTaskToEntity(task), convertUserToEntity(user));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: ADD User By TASK:", ex);
        }
    }

    @Override
    public TaskDTO update(TaskDTO task, Integer id) throws CRMProjectServiceException {
        try {
            Task taskEntity = taskRepository.update(convertTaskToEntity(task), id);
            return taskEntity!=null?convertTaskToDto(taskEntity):null;
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: UPDATE TASK:", ex);
        }
    }

    @Override
    public boolean remove(TaskDTO task) throws CRMProjectServiceException {
        try {
            return taskRepository.remove(convertTaskToEntity(task));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE TASK:", ex);
        }
    }

    @Override
    public boolean removeUserByTask(TaskDTO task, UserDTO user) throws CRMProjectServiceException {
        try {
            return taskRepository.removeUserByTask(convertTaskToEntity(task), convertUserToEntity(user));
        } catch (CRMProjectRepositoryException ex) {
            throw new CRMProjectServiceException("ERROR SERVICE: DELETE USER BY TASK:", ex);
        }
    }

    @Override
    public boolean removeAllUsersByTask(TaskDTO task) {
        try {
            taskRepository.removeAllUsersByTask(convertTaskToEntity(task));
            return true;
        } catch (CRMProjectRepositoryException ex) {
            return false;
        }
    }
}
