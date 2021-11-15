package com.itrex.java.lab.service;

import com.itrex.java.lab.dto.UserDTO;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;

import java.util.List;

public interface UserService {

    List<UserDTO> getAll() throws CRMProjectServiceException;
    UserDTO getById(Integer id) throws CRMProjectServiceException;
    List<UserDTO> getAllUsersByTaskDTO(Integer idTask) throws CRMProjectServiceException;

    UserDTO add(UserDTO user) throws CRMProjectServiceException;
    List<UserDTO> addAll(List<UserDTO> users) throws CRMProjectServiceException;
    void addTaskByUser(Integer idTask, Integer idUser) throws CRMProjectServiceException; //add task for user

    UserDTO update(UserDTO user) throws CRMProjectServiceException;  //update user by id

    void remove(Integer idUserDTO) throws CRMProjectServiceException;
    void removeTaskByUser(Integer idTask, Integer idUser) throws CRMProjectServiceException;
    void removeAllUsersByTask(Integer idTask) throws CRMProjectServiceException;
}
