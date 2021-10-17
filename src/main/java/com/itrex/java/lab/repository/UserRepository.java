package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.User;

import java.util.List;

public interface UserRepository {

    List<User> selectAll();
    User selectById(Integer id);
    User add(User user);
    List<User> addAll(List<User> users);
    User update(User user, Integer id);
    boolean remove(Integer id);
}
