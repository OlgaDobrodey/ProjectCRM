package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Status;

import java.util.List;

public interface StatusRepository {

    List<Status> selectAll();
    Status selectById(Integer id);
    Status add(Status status);
    List<Status> addAll(List<Status> roles);
    Status update(Status status, Integer id);
    boolean remove(Integer id);
}
