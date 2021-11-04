package com.itrex.java.lab.repository.impl.hibernate;

import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.repository.impl.abstractClass.AbstractTaskRepositoryTest;
import org.springframework.context.ApplicationContext;

class HibernateTaskRepositoryImplTest extends AbstractTaskRepositoryTest {

    public HibernateTaskRepositoryImplTest(ApplicationContext ctx) {
        super();
        postConstruct(ctx.getBean(TaskRepository.class), ctx.getBean(UserRepository.class));
    }
}