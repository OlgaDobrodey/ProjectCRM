package com.itrex.java.lab.repository.impl.jdbc;

import com.itrex.java.lab.repository.impl.abstractClass.AbstractTaskRepositoryTest;

public class JDBCTaskRepositoryImplTest extends AbstractTaskRepositoryTest {

    public JDBCTaskRepositoryImplTest() {
        super();
        postConstruct(new JDBCTaskRepositoryImpl(getConnectionPool()), new JDBCUserRepositoryImpl(getConnectionPool()));
    }
}