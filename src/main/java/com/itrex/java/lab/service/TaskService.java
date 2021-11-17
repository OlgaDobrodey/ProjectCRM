package com.itrex.java.lab.service;


import com.itrex.java.lab.dto.TaskDTO;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;

import java.util.List;

public interface TaskService {

    List<TaskDTO> getAll() throws CRMProjectServiceException;
    TaskDTO getById(Integer id) throws CRMProjectServiceException;
    List<TaskDTO> getAllTasksByUserId(Integer userId) throws CRMProjectServiceException;
    TaskDTO add(TaskDTO task) throws CRMProjectServiceException;
    TaskDTO update(TaskDTO task) throws CRMProjectServiceException;
    void removeAllTasksByUser(Integer userId) throws CRMProjectServiceException;
    void remove(Integer taskId) throws CRMProjectServiceException;

}
