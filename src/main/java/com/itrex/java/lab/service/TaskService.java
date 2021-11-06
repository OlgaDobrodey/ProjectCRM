package com.itrex.java.lab.service;


import com.itrex.java.lab.dto.TaskDTO;
import com.itrex.java.lab.dto.UserDTO;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;

import java.util.List;

public interface TaskService {

    List<TaskDTO> getAll() throws CRMProjectServiceException;
    TaskDTO getById(Integer id) throws CRMProjectServiceException;
    //select All users for task
    List<UserDTO> getAllUsersByTaskDTO(TaskDTO task) throws CRMProjectServiceException;

    TaskDTO add(TaskDTO task) throws CRMProjectServiceException;
    List<TaskDTO> addAll(List<TaskDTO> tasks) throws CRMProjectServiceException;
    //add task for user
    void addUserByTask(TaskDTO task, UserDTO user) throws CRMProjectServiceException;

    TaskDTO update(TaskDTO task, Integer id) throws CRMProjectServiceException;

    boolean remove(TaskDTO task) throws CRMProjectServiceException;
    boolean removeUserByTask(TaskDTO task, UserDTO user) throws CRMProjectServiceException;
    boolean removeAllUsersByTask(TaskDTO task) throws CRMProjectServiceException;
}
