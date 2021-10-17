package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.repository.TestCategoryTest;
import com.itrex.java.lab.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JDBCUserRepositoryImplTest extends BaseRepositoryTest {

    private final UserRepository repository;
    private TestCategoryTest testCategoryTest;
    private RoleRepository roleRepository;

    public JDBCUserRepositoryImplTest() {
        super();
        repository = new JDBCUserRepositoryImpl(getConnectionPool());
    }

    @BeforeAll
    private void createCategory() {
        testCategoryTest = new TestCategoryTest();
        roleRepository = new JDBCRoleRepositoryImpl(getConnectionPool());
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
        expected.setRole(roleRepository.selectById(1));
        expected.setLastName("Ivanov");
        expected.setFirstName("Ivan");

        //then
        assertEquals(expected, actual);
    }

    @Test
    void add_validData_receiveUser_shouldReturnExistUserTest() {
        //given
        User user = testCategoryTest.createTestUsers(1).get(0);

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

    @Test
    void addAll_validData_receiveUser_shouldReturnExistUserTest() {
        //given
        List<User> testUsers = testCategoryTest.createTestUsers(2);
        User test1 = testUsers.get(0);
        User test2 = testUsers.get(1);

        Integer countSelectAllTask = repository.selectAll().size();
        User result1 = new User();
        result1.setLogin(test1.getLogin());
        result1.setPsw(test1.getPsw());
        result1.setRole(test1.getRole());
        result1.setLastName(test1.getLastName());
        result1.setFirstName(test1.getFirstName());

        result1.setId(countSelectAllTask + 1);

        User result2 = new User();
        result2.setLogin(test2.getLogin());
        result2.setPsw(test2.getPsw());
        result2.setRole(test2.getRole());
        result2.setLastName(test2.getLastName());
        result2.setFirstName(test2.getFirstName());

        result2.setId(countSelectAllTask + 2);

        //when
        List<User> expected = List.of(result1, result2);
        List<User> actual = repository.addAll(List.of(test1, test2));

        //then
        assertEquals(expected, actual);
    }

    @Test
    void update_validData_receiveUserAndInteger_shouldReturnExistUserTest() {
        //given
        User expected = testCategoryTest.createTestUsers(1).get(0);
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
