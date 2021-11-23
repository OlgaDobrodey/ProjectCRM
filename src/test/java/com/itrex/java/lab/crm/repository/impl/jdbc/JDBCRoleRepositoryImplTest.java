package com.itrex.java.lab.crm.repository.impl.jdbc;

import com.itrex.java.lab.crm.repository.AbstractRoleRepositoryTest;
import com.itrex.java.lab.crm.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class JDBCRoleRepositoryImplTest extends AbstractRoleRepositoryTest {

    @Autowired
    public JDBCRoleRepositoryImplTest(@Qualifier(value = "JDBCRoleRepository") RoleRepository roleRepository) {
        super();
        postConstruct(roleRepository);
    }

}