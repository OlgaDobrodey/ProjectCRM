package com.itrex.java.lab.repository.impl.hibernate;

import com.itrex.java.lab.repository.impl.abstractClass.AbstractRoleRepositoryTest;

class HibernateRoleRepositoryImplTest extends AbstractRoleRepositoryTest {

    public HibernateRoleRepositoryImplTest() {
        super();
        postConstruct(new HibernateRoleRepositoryImpl(getSessionFactory()));
    }
}

