package com.itrex.java.lab.crm.repository.impl.hibernate;

import com.itrex.java.lab.crm.repository.AbstractRoleRepositoryTest;
import com.itrex.java.lab.crm.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

class HibernateRoleRepositoryImplTest extends AbstractRoleRepositoryTest {

    @Autowired
    public HibernateRoleRepositoryImplTest(@Qualifier(value = "HibernateRoleRepository") RoleRepository roleRepository) {
        super();
        postConstruct(roleRepository);
    }

}

