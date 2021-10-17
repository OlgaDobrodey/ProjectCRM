package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.Status;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JDBCUserRepositoryImplTest extends BaseRepositoryTest {

    private final UserRepository repository;

    public JDBCUserRepositoryImplTest() {
        super();
        repository = new JDBCUserRepositoryImpl(getConnectionPool());
    }

    @Test
    public void selectAll_validData_shouldReturnExistUserTest() {
        //given && when
        final List<User> result = repository.selectAll();

        //then
        assertFalse(result.isEmpty());
    }

    @Test
    void selectById_validData_receiveInteger_shouldReturnExistUserTest() {
        //given
        Integer idUser = 2;

        //when
        User actual = repository.selectById(idUser);
        User expected = new User();
        expected.setId(2);
        expected.setLogin("Ivanov");
        expected.setPsw(123);

        Role role = new Role();
        role.setId(1);
        role.setRoleName("user");
        expected.setRole(role);

        expected.setLastName("Ivanov");
        expected.setFirstName("Ivan");

        //then
        assertEquals(expected, actual);
    }

    @Test
    void add_validData_receiveUser_shouldReturnExistUserTest() {
        //given
        User user = new User();
        user.setLogin("Test");
        user.setPsw(123);

        Role role = new Role();
        role.setId(1);
        role.setRoleName("user");
        user.setRole(role);

        user.setLastName("Ivanov");
        user.setFirstName("Ivan");


        User expected = new User();
        expected.setId(repository.selectAll().size() + 1);
        expected.setLogin(user.getLogin());
        expected.setPsw(user.getPsw());
        expected.setRole(user.getRole());
        expected.setFirstName(user.getFirstName());
        expected.setLastName(user.getLastName());

        //when
        User actual = repository.add(user);

        //then
        assertEquals(expected, actual);
    }

//    @Test
//    void addAll_validData_receiveUser_shouldReturnExistUserTest() {
//        //given
//        Task test1 = new Task();
//        test1.setTitle("test");
//
//        Status status = new Status();
//        status.setId(1);
//        status.setStatusName("new");
//        test1.setStatus(status);
//
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        test1.setDedline(LocalDate.parse("2022-12-23", format));
//
//        test1.setInfo("info task");
//
//        Task test2 = new Task();
//        test2.setTitle("test2");
//        test2.setStatus(status);
//        test2.setDedline(LocalDate.parse("2022-12-23", format));
//        test2.setInfo("info task");
//
//        Integer countSelectAllTask = repository.selectAll().size();
//        Task result1 = new Task();
//        result1.setTitle(test1.getTitle());
//        result1.setDedline(test1.getDedline());
//        result1.setStatus(test1.getStatus());
//        result1.setInfo(test1.getInfo());
//
//        result1.setId(countSelectAllTask + 1);
//
//        Task result2 = new Task();
//        result2.setTitle(test2.getTitle());
//        result2.setDedline(test2.getDedline());
//        result2.setStatus(test2.getStatus());
//        result2.setInfo(test2.getInfo());
//
//        result2.setId(countSelectAllTask + 2);
//
//        //when
//        List<Task> expected = List.of(result1, result2);
//        List<Task> actual = repository.addAll(List.of(test1, test2));
//
//        //then
//        assertEquals(expected, actual);
//    }

    @Test
    void update_validData_receiveUserAndInteger_shouldReturnExistUserTest() {
        //given
        User expected = new User();
        expected .setLogin("Test");
        expected .setPsw(123);

        Role role = new Role();
        role.setId(1);
        role.setRoleName("user");
        expected.setRole(role);

        expected .setLastName("Ivanov");
        expected .setFirstName("Ivan");
        Integer testId = 1;

        //when
        User actual = repository.update(expected, 1);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void removeUser_validData_receiveInteger_shouldReturnExistBooleanTest() {
        //given
        Integer testId = 1;

        //when
        Boolean actual = repository.remove(testId);

        //then
        assertTrue(actual);
    }
}
