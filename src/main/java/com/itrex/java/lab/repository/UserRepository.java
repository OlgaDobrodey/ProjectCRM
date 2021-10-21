package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {

    List<User> selectAll() throws CRMProjectRepositoryException;
    User selectById(Integer id) throws CRMProjectRepositoryException;
    List<Task> selectAllTasksByUser(User user) throws CRMProjectRepositoryException;
    void printCrossTable() throws CRMProjectRepositoryException;
    User add(User user) throws CRMProjectRepositoryException;
    List<User> addAll(List<User> users) throws CRMProjectRepositoryException;
    void addTaskByUser(Task task, User user) throws CRMProjectRepositoryException; //add task for user
    User update(User user, Integer id) throws CRMProjectRepositoryException;  //update user by id

    //update All users, who have old role on default role
    List<User> updateRoleOnDefaultByUsers(Role Role, Role defaultRole) throws CRMProjectRepositoryException;

    boolean remove(User user) throws CRMProjectRepositoryException;
    void removeAllTasksByUser(User user) throws CRMProjectRepositoryException;

    /**
     * remove a task from the list of tasks for the user
     *
     * @param task
     * @param user
     * @return true - if delete task by user; false - if pair user task was not found
     * @throws SQLException
     */
    boolean removeTaskByUser(Task task, User user) throws CRMProjectRepositoryException;
}







