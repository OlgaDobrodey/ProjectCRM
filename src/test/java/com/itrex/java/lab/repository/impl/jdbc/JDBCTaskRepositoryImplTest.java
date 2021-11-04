package com.itrex.java.lab.repository.impl.jdbc;

import com.itrex.java.lab.repository.AbstractTaskRepositoryTest;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class JDBCTaskRepositoryImplTest extends AbstractTaskRepositoryTest {

    @Autowired
    public JDBCTaskRepositoryImplTest(@Qualifier(value = "JDBCTaskRepository") TaskRepository taskRepository,
                                      @Qualifier(value = "JDBCUserRepository") UserRepository userRepository) {
        super();
        postConstruct(taskRepository, userRepository);
    }
}