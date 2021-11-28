package com.itrex.java.lab.crm.service;


import com.itrex.java.lab.crm.dto.TaskDTO;
import com.itrex.java.lab.crm.entity.Status;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    List<TaskDTO> getAll();

    Page<TaskDTO> getAll(Pageable pageable);

    TaskDTO getById(Integer id);

    List<TaskDTO> getAllUserTasksByUserId(Integer userId);

    TaskDTO add(TaskDTO task) throws CRMProjectServiceException;

    TaskDTO update(TaskDTO task) throws CRMProjectServiceException;

    void finishTaskByTaskId(Integer taskId) throws CRMProjectServiceException; //delete all users for task

    void remove(Integer taskId) throws CRMProjectServiceException;

    TaskDTO changeStatusDTO(Status status, Integer taskId) throws CRMProjectServiceException;

}
