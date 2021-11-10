package com.itrex.java.lab.service;

import com.itrex.java.lab.dto.RoleDTO;
import com.itrex.java.lab.dto.TaskDTO;
import com.itrex.java.lab.dto.UserDTO;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;

import java.util.List;

public interface UserService {

    List<UserDTO> getAll() throws CRMProjectServiceException;
    UserDTO getById(Integer id) throws CRMProjectServiceException;
    List<TaskDTO> getAllTasksByUser(UserDTO user) throws CRMProjectServiceException;

    void printCrossTable() throws CRMProjectServiceException;

    UserDTO add(UserDTO user) throws CRMProjectServiceException;
    List<UserDTO> addAll(List<UserDTO> users) throws CRMProjectServiceException;
    void addTaskByUser(TaskDTO task, UserDTO user) throws CRMProjectServiceException; //add task for user

    UserDTO update(UserDTO user) throws CRMProjectServiceException;  //update user by id
    List<UserDTO> updateRoleOnDefaultByUsers(RoleDTO Role, RoleDTO defaultRole) throws CRMProjectServiceException;

   void remove(Integer idUserDTO) throws CRMProjectServiceException;
    void removeTaskByUser(Integer idTask, Integer idUser) throws CRMProjectServiceException;
    void removeAllTasksByUser(Integer IdUser) throws CRMProjectServiceException;
}
