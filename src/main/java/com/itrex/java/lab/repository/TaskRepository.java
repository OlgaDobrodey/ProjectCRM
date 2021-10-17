package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Task;

import java.util.List;

public interface TaskRepository {

    List<Task> selectAll();
    Task selectById(Integer id);
    Task add(Task task);
    List<Task> addAll(List<Task> tasks);
    Task update(Task task, Integer id);
    boolean remove(Integer id);

}
