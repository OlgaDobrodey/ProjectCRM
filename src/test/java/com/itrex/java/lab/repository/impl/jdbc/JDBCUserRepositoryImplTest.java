package com.itrex.java.lab.repository.impl.jdbc;

import com.itrex.java.lab.repository.AbstractUserRepositoryTest;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;

public class JDBCUserRepositoryImplTest extends AbstractUserRepositoryTest {

    public JDBCUserRepositoryImplTest(@Qualifier(value = "JDBCTaskRepository") TaskRepository taskRepository,
                                      @Qualifier(value = "JDBCUserRepository") UserRepository userRepository) {
        super();
        postConstruct(userRepository,taskRepository);
    }
}
