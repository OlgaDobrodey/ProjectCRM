package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {

    List<User> selectAll() throws SQLException;
    User selectById(Integer id) throws SQLException;
    List<Task> selectAllTasksByUser(User user) throws SQLException;
    void printCrossTable() throws SQLException;
    User add(User user) throws SQLException;
    List<User> addAll(List<User> users) throws SQLException;
    void addTaskByUser(Task task, User user) throws SQLException; //add task for user
    User update(User user, Integer id) throws SQLException;  //update user by id

    //update All users, who have old role on default role
    List<User> updateRoleOnDefaultByUsers(Role Role, Role defaultRole) throws SQLException;

    boolean remove(User user) throws SQLException;
    void removeAllTasksByUser(User user) throws SQLException;

    /**
     * remove a task from the list of tasks for the user
     *
     * @param task
     * @param user
     * @return true - if delete task by user; false - if pair user task was not found
     * @throws SQLException
     */
    boolean removeTaskByUser(Task task, User user) throws SQLException;
}







