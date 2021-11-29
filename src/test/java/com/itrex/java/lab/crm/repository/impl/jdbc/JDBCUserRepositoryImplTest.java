package com.itrex.java.lab.crm.repository.impl.jdbc;

import com.itrex.java.lab.crm.entity.User;
import com.itrex.java.lab.crm.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.crm.repository.AbstractUserRepositoryTest;
import com.itrex.java.lab.crm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JDBCUserRepositoryImplTest extends AbstractUserRepositoryTest {

    private UserRepository userRepository;

    @Autowired
    public JDBCUserRepositoryImplTest(@Qualifier(value = "JDBCUserRepository") UserRepository userRepository) {
        super();
        postConstruct(userRepository);
        this.userRepository = userRepository;
    }

    @Test
    void add_existCopyUSERId2_returnThrowRepositoryExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        User user = userRepository.selectById(2);

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> userRepository.add(user));
    }

}
