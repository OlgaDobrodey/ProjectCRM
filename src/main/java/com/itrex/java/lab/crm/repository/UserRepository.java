package com.itrex.java.lab.crm.repository;

import com.itrex.java.lab.crm.entity.User;
import com.itrex.java.lab.crm.exceptions.CRMProjectRepositoryException;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {

    List<User> selectAll() throws CRMProjectRepositoryException;

    User selectById(Integer id) throws CRMProjectRepositoryException;
    User selectByLogin(String login) throws CRMProjectRepositoryException;

    List<User> selectAllUsersByTaskId(Integer taskId) throws CRMProjectRepositoryException;
    List<User> selectAllUsersByRoleId(Integer roleId) throws CRMProjectRepositoryException;

    User add(User user) throws CRMProjectRepositoryException;

    User update(User user) throws CRMProjectRepositoryException;  //update user by id

    void remove(Integer userId) throws CRMProjectRepositoryException, SQLException;

}







