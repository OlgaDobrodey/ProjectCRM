package com.itrex.java.lab.crm.repository.impl.jdbc;

import com.itrex.java.lab.crm.entity.Task;
import com.itrex.java.lab.crm.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.crm.repository.AbstractTaskRepositoryTest;
import com.itrex.java.lab.crm.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JDBCTaskRepositoryImplTest extends AbstractTaskRepositoryTest {

    private TaskRepository taskRepository;

    @Autowired
    public JDBCTaskRepositoryImplTest(@Qualifier(value = "JDBCTaskRepository") TaskRepository taskRepository) {
        super();
        postConstruct(taskRepository);
        this.taskRepository = taskRepository;
    }


    @Test
    void add_existCopyTaskWithId2_returnThrowRepositoryExceptionTypeTest() throws CRMProjectRepositoryException {
        //given && when
        Task task = taskRepository.selectById(2);

        //then
        assertThrows(CRMProjectRepositoryException.class, () -> taskRepository.add(task));
    }

}