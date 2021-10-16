package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Role;

import java.util.List;

public interface RoleRepository {

    List<Role> selectAll();
    Role selectById(Integer id);
    Role add(Role role);
    List<Role> addAll(List<Role> roles);
    Role update(Role role, Integer id);
    boolean remove(Integer id);
}
