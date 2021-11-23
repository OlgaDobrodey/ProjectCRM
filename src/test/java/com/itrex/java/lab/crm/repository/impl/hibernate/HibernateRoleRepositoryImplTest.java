package com.itrex.java.lab.crm.repository.impl.hibernate;

import com.itrex.java.lab.crm.repository.AbstractRoleRepositoryTest;
import com.itrex.java.lab.crm.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

class HibernateRoleRepositoryImplTest extends AbstractRoleRepositoryTest {

    @Autowired
    public HibernateRoleRepositoryImplTest(RoleRepository roleRepository) {
        super();
        postConstruct(roleRepository);
    }

}

