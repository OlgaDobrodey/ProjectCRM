package com.itrex.java.lab.repository.impl.hibernate;

import com.itrex.java.lab.repository.impl.abstractClass.AbstractUserRepositoryTest;

class HibernateUserRepositoryImplTest extends AbstractUserRepositoryTest {

    public HibernateUserRepositoryImplTest() {
        super();
        postConstruct(new HibernateUserRepositoryImpl(getSessionFactory()), new HibernateTaskRepositoryImpl(getSessionFactory()));
    }
}