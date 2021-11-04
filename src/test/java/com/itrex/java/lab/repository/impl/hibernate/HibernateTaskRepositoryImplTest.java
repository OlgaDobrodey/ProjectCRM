package com.itrex.java.lab.repository.impl.hibernate;

import com.itrex.java.lab.repository.AbstractTaskRepositoryTest;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

class HibernateTaskRepositoryImplTest extends AbstractTaskRepositoryTest {

    @Autowired
    public HibernateTaskRepositoryImplTest(TaskRepository taskRepository, UserRepository userRepository) {
        super();
        postConstruct(taskRepository, userRepository);
    }
}