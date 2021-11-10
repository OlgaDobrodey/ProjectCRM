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

    TaskDTO update(TaskDTO task) throws CRMProjectServiceException;

    void remove(Integer idTask) throws CRMProjectServiceException;

    void removeAllUsersByTask(Integer idTask) throws CRMProjectServiceException;
}
