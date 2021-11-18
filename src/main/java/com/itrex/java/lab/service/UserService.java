package com.itrex.java.lab.service;

import com.itrex.java.lab.dto.PasswordDTOForChanges;
import com.itrex.java.lab.dto.UserDTO;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;

import java.util.List;

public interface UserService {

    List<UserDTO> getAll() throws CRMProjectServiceException;
    UserDTO getById(Integer id) throws CRMProjectServiceException;
    List<UserDTO> getAllUsersFromTaskId(Integer taskId) throws CRMProjectServiceException;
    List<UserDTO> getAllUsersFromRoleId(Integer roleId) throws CRMProjectRepositoryException, CRMProjectServiceException;

    UserDTO add(UserDTO user) throws CRMProjectServiceException;
    void assignTaskFromUser(Integer taskId, Integer userId) throws CRMProjectServiceException; //add task for user

    UserDTO update(UserDTO user) throws CRMProjectServiceException; //update user everything except the psw
    UserDTO updateUserPassword(PasswordDTOForChanges passwordDTO, Integer userId) throws CRMProjectRepositoryException, CRMProjectServiceException;

    void remove(Integer userId) throws CRMProjectServiceException;
    void revokeTaskFromUser(Integer taskId, Integer userId) throws CRMProjectServiceException;
    void revokeAllUsersFromTaskId(Integer taskId) throws CRMProjectServiceException; //delete all users for task

}
