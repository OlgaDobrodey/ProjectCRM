package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface TaskRepository {

    List<Task> selectAll() throws SQLException;
    Task selectById(Integer id) throws SQLException;
    Task add(Task task) throws SQLException;
    List<Task> addAll(List<Task> tasks) throws SQLException;
    Task update(Task task, Integer id);
    boolean remove(Task task) throws SQLException;

    //select All users for task
    List<User> selectAllUsersByTask(Task task) throws SQLException;

    //add task for user
    void addUserByTask(Task task, User user) throws SQLException;

    //remove a task from the list of tasks for the user
    boolean removeUserByTask(Task task, User user) throws SQLException;

    void removeAllUsersByTask(Task task) throws SQLException;
}
