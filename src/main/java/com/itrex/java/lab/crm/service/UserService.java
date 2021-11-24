package com.itrex.java.lab.crm.service;

import com.itrex.java.lab.crm.dto.PasswordDTOForChanges;
import com.itrex.java.lab.crm.dto.UserDTO;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;

import java.util.List;

public interface UserService {

    List<UserDTO> getAll() throws CRMProjectServiceException;
    UserDTO getById(Integer id) throws CRMProjectServiceException;
    UserDTO getByLogin(String login) throws CRMProjectServiceException;
    List<UserDTO> getAllTaskUsersByTaskId(Integer taskId) throws CRMProjectServiceException;
    List<UserDTO> getAllRoleUsersByRoleId(Integer roleId) throws CRMProjectServiceException;

    UserDTO add(UserDTO user) throws CRMProjectServiceException;
    void assignTaskToUser(Integer taskId, Integer userId) throws CRMProjectServiceException; //add task for user

    UserDTO update(UserDTO user) throws CRMProjectServiceException; //update user everything except the psw
    UserDTO updateUserPassword(PasswordDTOForChanges passwordDTO, Integer userId) throws CRMProjectServiceException;

    void remove(Integer userId) throws CRMProjectServiceException;
    void revokeTaskFromUser(Integer taskId, Integer userId) throws CRMProjectServiceException;
    void revokeAllUserTasksByUserId(Integer userId) throws CRMProjectServiceException;

}
