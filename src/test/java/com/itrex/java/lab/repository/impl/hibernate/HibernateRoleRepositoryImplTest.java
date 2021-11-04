package com.itrex.java.lab.repository.impl.hibernate;

import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.repository.impl.abstractClass.AbstractRoleRepositoryTest;
import org.springframework.context.ApplicationContext;

class HibernateRoleRepositoryImplTest extends AbstractRoleRepositoryTest {

    public HibernateRoleRepositoryImplTest(ApplicationContext ctx) {
        super();
        postConstruct(ctx.getBean(RoleRepository.class));
    }
}

