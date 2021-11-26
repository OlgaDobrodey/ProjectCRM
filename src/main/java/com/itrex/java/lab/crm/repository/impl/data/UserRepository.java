package com.itrex.java.lab.crm.repository.impl.data;

import com.itrex.java.lab.crm.entity.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Primary
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByLogin(String login);
    List<User> findUsersByTasks_id(Integer id);
    List<User> findUsersByRole_Id(Integer id);

}
