package com.itrex.java.lab.service;

import com.itrex.java.lab.dto.RoleDTO;
import com.itrex.java.lab.dto.TaskDTO;
import com.itrex.java.lab.dto.UserDTO;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;

import java.sql.SQLException;
import java.util.List;

public interface UserService {

    List<UserDTO> getAll() throws CRMProjectServiceException;
    UserDTO getById(Integer id) throws CRMProjectServiceException;
    List<TaskDTO> getAllTasksByUser(UserDTO user) throws CRMProjectServiceException;

    void printCrossTable() throws CRMProjectServiceException;

    UserDTO add(UserDTO user) throws CRMProjectServiceException;
    List<UserDTO> addAll(List<UserDTO> users) throws CRMProjectServiceException;
    void addTaskByUser(TaskDTO task, UserDTO user) throws CRMProjectServiceException; //add task for user

    /**
     * update user, if user is not founded in DB return null;
     *
     * @param user -all param's user for change
     * @param id   - user id to change
     * @return changed user
     */
    UserDTO update(UserDTO user, Integer id) throws CRMProjectServiceException;  //update user by id

    //update All users, who have old role on default role
    List<UserDTO> updateRoleOnDefaultByUsers(RoleDTO Role, RoleDTO defaultRole) throws CRMProjectServiceException;

    boolean remove(UserDTO user) throws CRMProjectServiceException;

    /**
     * remove a task from the list of tasks for the user
     *
     * @param task
     * @param user
     * @return true - if delete task by user; false - if pair user task was not found
     * @throws SQLException
     */
    boolean removeTaskByUser(TaskDTO task, UserDTO user) throws CRMProjectServiceException;

    void removeAllTasksByUser(UserDTO user) throws CRMProjectServiceException;
}
