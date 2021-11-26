package com.itrex.java.lab.crm.repository.impl.hibernate;

import com.itrex.java.lab.crm.repository.AbstractUserRepositoryTest;
import com.itrex.java.lab.crm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

class HibernateUserRepositoryImplTest extends AbstractUserRepositoryTest {

    @Autowired
    public HibernateUserRepositoryImplTest(@Qualifier("HibernateUserRepository")UserRepository userRepository) {
        super();
        postConstruct(userRepository);
    }

}