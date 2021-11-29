package com.itrex.java.lab.crm.repository.impl.hibernate;

import com.itrex.java.lab.crm.repository.AbstractTaskRepositoryTest;
import com.itrex.java.lab.crm.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

class HibernateTaskRepositoryImplTest extends AbstractTaskRepositoryTest {

    @Autowired
    public HibernateTaskRepositoryImplTest(@Qualifier(value = "HibernateTaskRepository")TaskRepository taskRepository) {
        super();
        postConstruct(taskRepository);
    }

}