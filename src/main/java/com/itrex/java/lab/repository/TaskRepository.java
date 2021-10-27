package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;

import java.util.List;

public interface TaskRepository {

    List<Task> selectAll() throws CRMProjectRepositoryException;

    Task selectById(Integer id) throws CRMProjectRepositoryException;

    Task add(Task task) throws CRMProjectRepositoryException;

    List<Task> addAll(List<Task> tasks) throws CRMProjectRepositoryException;

    /**
     * update task, if task is not founded in DB return null;
     * @param task -all param's task for change
     * @param id - task id to change
     * @return changed task
     * @throws CRMProjectRepositoryException
     */
    Task update(Task task, Integer id) throws CRMProjectRepositoryException;

    boolean remove(Task task) throws CRMProjectRepositoryException;

    //select All users for task
    List<User> selectAllUsersByTask(Task task) throws CRMProjectRepositoryException;

    //add task for user
    void addUserByTask(Task task, User user) throws CRMProjectRepositoryException;

    //remove a task from the list of tasks for the user
    boolean removeUserByTask(Task task, User user) throws CRMProjectRepositoryException;

    void removeAllUsersByTask(Task task) throws CRMProjectRepositoryException;
}
