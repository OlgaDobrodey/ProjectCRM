package com.itrex.java.lab.repository.impl.hibernate;

import com.itrex.java.lab.repository.AbstractUserRepositoryTest;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

class HibernateUserRepositoryImplTest extends AbstractUserRepositoryTest {

    @Autowired
    public HibernateUserRepositoryImplTest(TaskRepository taskRepository, UserRepository userRepository) {
        super();
        postConstruct(userRepository, taskRepository);
    }
}