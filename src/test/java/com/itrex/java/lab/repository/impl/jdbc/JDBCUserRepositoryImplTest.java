package com.itrex.java.lab.repository.impl.jdbc;

import com.itrex.java.lab.repository.impl.abstractClass.AbstractUserRepositoryTest;

public class JDBCUserRepositoryImplTest extends AbstractUserRepositoryTest {

    public JDBCUserRepositoryImplTest() {
        super();
        postConstruct(new JDBCUserRepositoryImpl(getConnectionPool()), new JDBCTaskRepositoryImpl(getConnectionPool()));
    }
}
