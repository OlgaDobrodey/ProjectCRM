package com.itrex.java.lab.repository.impl.jdbc;

import com.itrex.java.lab.repository.impl.abstractClass.AbstractRoleRepositoryTest;

public class JDBCRoleRepositoryImplTest extends AbstractRoleRepositoryTest {

    public JDBCRoleRepositoryImplTest() {
        super();
        postConstruct(new JDBCRoleRepositoryImpl(getConnectionPool()));
    }
}