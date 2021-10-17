package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.UserTask;
import com.itrex.java.lab.repository.UserTaskRepository;

import javax.sql.DataSource;
import java.util.List;

public class JDBCUserTaskRepositoryImpl implements UserTaskRepository {

    private DataSource dataSource;

    public JDBCUserTaskRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<UserTask> selectAll() {
        return null;
    }

    @Override
    public UserTask selectByUserAndTask(Task task, User user) {
        return null;
    }

    @Override
    public UserTask add(UserTask userTask) {
        return null;
    }

    @Override
    public List<UserTask> addAll(List<UserTask> userTasks) {
        return null;
    }

    @Override
    public UserTask update(Task task, User user, UserTask userTask) {
        return null;
    }

    @Override
    public boolean remove(Task task, User user) {
        return false;
    }
}
