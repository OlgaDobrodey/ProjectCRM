package com.itrex.java.lab.crm.repository.impl.data;

import com.itrex.java.lab.crm.entity.User;
import com.itrex.java.lab.crm.repository.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DataUserRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findUserByLogin_validData_existUserLogin_returnUserTest() {
        //given
        String login = "Ivanov";

        //when
        Optional<User> actual = userRepository.findUserByLogin(login);

        //then
        assertEquals(2, actual.get().getId());
        assertEquals("Ivanov", actual.get().getLogin());
        assertEquals("$2a$10$RIlu14GNv2Ahm6Ayj0cJOOPKI9liARHYFKk6ctESUEGdpmbJUz0iS", actual.get().getPsw());
        assertEquals("ADMIN", actual.get().getRole().getRoleName());
        assertEquals("Ivanov", actual.get().getLastName());
        assertEquals("Ivan", actual.get().getFirstName());
    }

    @Test
    void findUserByLogin_validData_existLoginUserNotDataBase_returnNULLTest() {
        //given
        String login = "IvanovTest";

        //when
        Optional<User> actual = userRepository.findUserByLogin(login);

        //then
        assertNull(actual.orElse(null));
    }

    @Test
    void findUsersByTasks_id_existTaskId_returnListOfUsersTest() {
        //given
        Integer idTask = 2;

        //when
        List<User> actual = userRepository.findUsersByTasks_id(idTask);

        //then
        assertEquals(1, actual.get(0).getId());
        assertEquals("Petrov", actual.get(0).getLogin());
        assertEquals("$2a$10$RIlu14GNv2Ahm6Ayj0cJOOPKI9liARHYFKk6ctESUEGdpmbJUz0iS", actual.get(0).getPsw());
        assertEquals("ADMIN", actual.get(0).getRole().getRoleName());
        assertEquals("Petrov", actual.get(0).getLastName());
        assertEquals("Petr", actual.get(0).getFirstName());
        assertEquals(8, actual.get(1).getId());
        assertEquals("Dropalo", actual.get(1).getLogin());
        assertEquals("$2a$10$RIlu14GNv2Ahm6Ayj0cJOOPKI9liARHYFKk6ctESUEGdpmbJUz0iS", actual.get(1).getPsw());
        assertEquals("USER", actual.get(1).getRole().getRoleName());
        assertEquals("Dropalo", actual.get(1).getLastName());
        assertEquals("Andrey", actual.get(1).getFirstName());
    }

    @Test
    void findUsersByTasks_id_existTaskIdNoDataBase_returnNull() {
        //given
        Integer idTask = 99;

        //when
        List<User> actual = userRepository.findUsersByTasks_id(idTask);

        //then
        assertTrue(actual.isEmpty());
    }

    @Test
    void findUsersByRole_Id_existRoleId_returnListOfUsersTest() {
        //given
        Integer roleId = 1;

        //when
        List<User> users = userRepository.findUsersByRole_Id(roleId);

        //then
        assertEquals(3, users.size());
        assertEquals(1, users.get(0).getId());
        assertEquals(2, users.get(1).getId());
        assertEquals(3, users.get(2).getId());
    }

    @Test
    void findUsersByRole_Id_existRoleIdNoDB_returnEmptyListTest() {
        //given
        Integer roleId = 5;

        //when
        List<User> users = userRepository.findUsersByRole_Id(roleId);

        //then
        assertEquals(0, users.size());
    }

}