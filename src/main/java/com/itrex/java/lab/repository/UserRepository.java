package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;

import java.util.List;

public interface UserRepository {

    List<User> selectAll() throws CRMProjectRepositoryException;
    User selectById(Integer id) throws CRMProjectRepositoryException;
    List<Task> selectAllTasksByUser(User user) throws CRMProjectRepositoryException;
    User add(User user) throws CRMProjectRepositoryException;
    List<User> addAll(List<User> users) throws CRMProjectRepositoryException;
    void addTaskByUser(Task task, User user) throws CRMProjectRepositoryException; //add task for user
    User update(User user) throws CRMProjectRepositoryException;  //update user by id
    List<User> updateRoleOnDefaultByUsers(Role Role, Role defaultRole) throws CRMProjectRepositoryException;
    void remove(Integer idUser) throws CRMProjectRepositoryException;
    void removeAllTasksByUser(Integer idUser) throws CRMProjectRepositoryException;
    void removeTaskByUser(Integer idTask, Integer idUser) throws CRMProjectRepositoryException;
}







