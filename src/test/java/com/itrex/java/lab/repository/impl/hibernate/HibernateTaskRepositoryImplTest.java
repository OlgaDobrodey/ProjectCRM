package com.itrex.java.lab.repository.impl.hibernate;

import com.itrex.java.lab.repository.impl.abstractClass.AbstractTaskRepositoryTest;

class HibernateTaskRepositoryImplTest extends AbstractTaskRepositoryTest {

    public HibernateTaskRepositoryImplTest() {
        super();
        postConstruct(new HibernateTaskRepositoryImpl(getSessionFactory()), new HibernateUserRepositoryImpl(getSessionFactory()));
    }
}