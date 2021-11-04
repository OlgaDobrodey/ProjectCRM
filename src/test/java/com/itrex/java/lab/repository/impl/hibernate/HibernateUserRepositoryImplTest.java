package com.itrex.java.lab.repository.impl.hibernate;

import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.repository.impl.abstractClass.AbstractUserRepositoryTest;
import org.springframework.context.ApplicationContext;

class HibernateUserRepositoryImplTest extends AbstractUserRepositoryTest {

    public HibernateUserRepositoryImplTest(ApplicationContext ctx) {
        super();
        postConstruct(ctx.getBean(UserRepository.class), ctx.getBean(TaskRepository.class));
    }
}