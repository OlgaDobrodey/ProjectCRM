package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.UserTask;

import java.util.List;

public interface UserTaskRepository {

    List<UserTask> selectAll();
    UserTask selectByUserAndTask(Task task, User user);
    UserTask add(UserTask userTask);
    List<UserTask> addAll(List<UserTask> userTasks);
    UserTask update(Task task, User user, UserTask userTask);
    boolean remove(Task task, User user);
}
