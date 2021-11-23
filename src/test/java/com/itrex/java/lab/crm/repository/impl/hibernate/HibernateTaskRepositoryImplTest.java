package com.itrex.java.lab.crm.repository.impl.hibernate;

import com.itrex.java.lab.crm.repository.AbstractTaskRepositoryTest;
import com.itrex.java.lab.crm.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

class HibernateTaskRepositoryImplTest extends AbstractTaskRepositoryTest {

    @Autowired
    public HibernateTaskRepositoryImplTest(TaskRepository taskRepository) {
        super();
        postConstruct(taskRepository);
    }

}