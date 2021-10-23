package com.itrex.java.lab.repository.impl.hibernate;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.UserRepository;

import java.util.List;

public class HibernateUserRepositoryImpl implements UserRepository {

    @Override
    public List<User> selectAll() throws CRMProjectRepositoryException {
        return null;
    }

    @Override
    public User selectById(Integer id) throws CRMProjectRepositoryException {
        return null;
    }

    @Override
    public List<Task> selectAllTasksByUser(User user) throws CRMProjectRepositoryException {
        return null;
    }

    @Override
    public void printCrossTable() throws CRMProjectRepositoryException {

    }

    @Override
    public User add(User user) throws CRMProjectRepositoryException {
        return null;
    }

    @Override
    public List<User> addAll(List<User> users) throws CRMProjectRepositoryException {
        return null;
    }

    @Override
    public void addTaskByUser(Task task, User user) throws CRMProjectRepositoryException {

    }

    @Override
    public User update(User user, Integer id) throws CRMProjectRepositoryException {
        return null;
    }

    @Override
    public List<User> updateRoleOnDefaultByUsers(Role Role, Role defaultRole) throws CRMProjectRepositoryException {
        return null;
    }

    @Override
    public boolean remove(User user) throws CRMProjectRepositoryException {
        return false;
    }

    @Override
    public void removeAllTasksByUser(User user) throws CRMProjectRepositoryException {

    }

    @Override
    public boolean removeTaskByUser(Task task, User user) throws CRMProjectRepositoryException {
        return false;
    }
}
