package com.itrex.java.lab.crm.repository.impl.hibernate;

import com.itrex.java.lab.crm.repository.AbstractUserRepositoryTest;
import com.itrex.java.lab.crm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

class HibernateUserRepositoryImplTest extends AbstractUserRepositoryTest {

    @Autowired
    public HibernateUserRepositoryImplTest(UserRepository userRepository) {
        super();
        postConstruct(userRepository);
    }

}