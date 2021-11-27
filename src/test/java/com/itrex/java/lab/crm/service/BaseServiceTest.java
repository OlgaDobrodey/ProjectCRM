package com.itrex.java.lab.crm.service;

import com.itrex.java.lab.crm.repository.impl.data.RoleRepository;
import com.itrex.java.lab.crm.repository.impl.data.TaskRepository;
import com.itrex.java.lab.crm.repository.impl.data.UserRepository;
import com.itrex.java.lab.crm.service.impl.RoleServiceImpl;
import com.itrex.java.lab.crm.service.impl.TaskServiceImpl;
import com.itrex.java.lab.crm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class BaseServiceTest {

    @InjectMocks
    protected RoleServiceImpl roleService;
    @InjectMocks
    protected TaskServiceImpl taskService;
    @InjectMocks
    protected UserServiceImpl userService;
    @Mock
    protected RoleRepository roleRepository;
    @Mock
    protected TaskRepository taskRepository;
    @Mock
    protected UserRepository userRepository;

}
