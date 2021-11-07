package com.itrex.java.lab.service;

import com.itrex.java.lab.dto.TaskDTO;
import com.itrex.java.lab.dto.UserDTO;
import com.itrex.java.lab.entity.Status;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.itrex.java.lab.repository.RepositoryTestUtils.createTestTasks;
import static com.itrex.java.lab.repository.RepositoryTestUtils.createTestUsers;
import static com.itrex.java.lab.utils.Convert.convertTaskToDto;
import static com.itrex.java.lab.utils.Convert.convertUserToDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Test
    public void getAll_returnUserDTOTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given && when
        List<User> result = createTestUsers(2);
        result.get(0).setId(1);
        result.get(1).setId(2);
        when(userRepository.selectAll()).thenReturn(result);

        //then
        assertFalse(userService.getAll().isEmpty());
        assertEquals(userService.getAll().size(), 2);
        verify(userRepository, Mockito.times(2)).selectAll();
    }

    @Test
    void getAll_shouldThrowServiceExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        when(userRepository.selectAll()).thenThrow(CRMProjectRepositoryException.class);

        //then
        assertThrows(CRMProjectServiceException.class, () -> userService.getAll());
        verify(userRepository).selectAll();
    }

    @Test
    void getById_existUserDTOId_returnUserDTOTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Integer idUser = 2;
        User user = createTestUsers(1).get(0);
        user.setId(idUser);
        when(userRepository.selectById(idUser)).thenReturn(user);

        //when
        UserDTO actual = userService.getById(idUser);

        //then
        assertEquals(2, actual.getId());
        assertEquals("Test 0", actual.getLogin());
        assertEquals("1230", actual.getPsw());
        assertEquals("ADMIN", actual.getRole().getRoleName());
        assertEquals("Ivanov 0", actual.getLastName());
        assertEquals("Ivan 0", actual.getFirstName());
        verify(userRepository).selectById(idUser);
    }

    @Test
    void getById_existIDUsereDTONotDataBase_returnNULLTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Integer idUser = 28;
        when(userRepository.selectById(idUser)).thenReturn(null);

        //when
        UserDTO actual = userService.getById(idUser);

        //then
        assertNull(actual);
        verify(userRepository).selectById(idUser);
    }

    @Test
    void getById_shouldThrowServiceExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        Integer idUser = 2;
        when(userRepository.selectById(idUser)).thenThrow(CRMProjectRepositoryException.class);

        //then
        assertThrows(CRMProjectServiceException.class, () -> userService.getById(idUser));
        verify(userRepository).selectById(idUser);
    }

    @Test
    void getAllTaskDTOByUserDTO_existUserDTO_returnListOfTaskDTOTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        List<Task> tasks = createTestTasks(2);
        tasks.get(0).setId(1);
        tasks.get(1).setId(2);
        User user = createTestUsers(1).get(0);
        user.setId(1);
        when(userRepository.selectAllTasksByUser(user)).thenReturn(tasks);

        //when
        List<TaskDTO> actual = userService.getAllTasksByUser(convertUserToDto(user));

        //then
        assertEquals(1, actual.get(0).getId());
        assertEquals("Task test 0", actual.get(0).getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.get(0).getStatus());
        assertEquals(LocalDate.of(2001, 1, 1), actual.get(0).getDeadline());
        assertEquals("Task test info 0", actual.get(0).getInfo());
        assertEquals(2, actual.get(1).getId());
        assertEquals("Task test 1", actual.get(1).getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.get(1).getStatus());
        assertEquals(LocalDate.of(2001, 1, 1), actual.get(1).getDeadline());
        assertEquals("Task test info 1", actual.get(1).getInfo());
        verify(userRepository).selectAllTasksByUser(user);
    }

    @Test
    void add_validData_existUser_returnUserTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        User user = createTestUsers(1).get(0);
        user.setId(11);
        when(userRepository.add(user)).thenReturn(user);

        //when
        UserDTO actual = userService.add(convertUserToDto(user));

        //then
        assertEquals(11, actual.getId());
        assertEquals("Test 0", actual.getLogin());
        assertEquals("1230", actual.getPsw());
        assertEquals("ADMIN", actual.getRole().getRoleName());
        assertEquals("Ivanov 0", actual.getLastName());
        assertEquals("Ivan 0", actual.getFirstName());
        verify(userRepository).add(user);
    }

    @Test
    void add_existCopyUSERId2_returnThrowServiceExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        User user = createTestUsers(1).get(0);
        when(userRepository.add(user)).thenThrow(CRMProjectRepositoryException.class);
        //There is Role "User" in Data Base

        //then
        assertThrows(CRMProjectServiceException.class, () -> userService.add(convertUserToDto(user)));
        verify(userRepository).add(user);
    }

    @Test
    void addAll_existUserDTO_returnUserDTOTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        List<User> testUsers = createTestUsers(2);
        testUsers.get(0).setId(11);
        testUsers.get(1).setId(12);
        when(userRepository.addAll(testUsers)).thenReturn(testUsers);

        //when
        List<UserDTO> actual = userService.addAll(testUsers.stream().
                map(user -> convertUserToDto(user))
                .collect(Collectors.toList()));

        //then
        assertEquals(11, actual.get(0).getId());
        assertEquals("Test 0", actual.get(0).getLogin());
        assertEquals("1230", actual.get(0).getPsw());
        assertEquals("ADMIN", actual.get(0).getRole().getRoleName());
        assertEquals("Ivanov 0", actual.get(0).getLastName());
        assertEquals("Ivan 0", actual.get(0).getFirstName());
        assertEquals(12, actual.get(1).getId());
        assertEquals("Test 1", actual.get(1).getLogin());
        assertEquals("1231", actual.get(1).getPsw());
        assertEquals("ADMIN", actual.get(1).getRole().getRoleName());
        assertEquals("Ivanov 1", actual.get(1).getLastName());
        assertEquals("Ivan 1", actual.get(1).getFirstName());
        verify(userRepository).addAll(testUsers);
    }

    @Test
    void addAll_existCopyUserById2_shouldThrowServiceExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        when(userRepository.addAll(new ArrayList<>())).thenThrow(CRMProjectRepositoryException.class);

        //then
        assertThrows(CRMProjectServiceException.class, () -> userService.addAll(new ArrayList<>()));
        verify(userRepository).addAll(new ArrayList<>());
    }


    @Test
    void update_validData_existUserAndInteger_returnUserTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        User expected = createTestUsers(1).get(0);
        expected.setId(1);
        Integer testId = 1;
        when(userRepository.update(expected, testId)).thenReturn(expected);

        //when
        UserDTO actual = userService.update(convertUserToDto(expected), 1);

        //then
        assertEquals(1, actual.getId());
        assertEquals("Test 0", actual.getLogin());
        assertEquals("1230", actual.getPsw());
        assertEquals("ADMIN", actual.getRole().getRoleName());
        assertEquals("Ivanov 0", actual.getLastName());
        assertEquals("Ivan 0", actual.getFirstName());
        verify(userRepository).update(expected, 1);
    }

    @Test
    void update_validData_existUserAndIdUserNotDB_returnNullTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given && when
        User expected = createTestUsers(1).get(0);
        expected.setId(1);
        Integer testId = 1;
        when(userRepository.update(expected, testId)).thenReturn(null);

        //when
        UserDTO actual = userService.update(convertUserToDto(expected), testId);

        //then
        assertNull(actual);
    }

    @Test
    void update_existIdUser_shouldThrowServiceExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        Integer testId = 1;
        User user = createTestUsers(1).get(0);
        user.setId(1);
        when(userRepository.update(user, testId)).thenThrow(CRMProjectRepositoryException.class);

        //then
        assertThrows(CRMProjectServiceException.class, () -> userService.update(convertUserToDto(user), testId));
        verify(userRepository).update(any(),anyInt());
    }

    @Test
    void remove_existUserDTO_shouldReturnExistTrueTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        User user = createTestUsers(1).get(0);
        user.setId(2);
        when(userRepository.remove(user)).thenReturn(true);
        //when
        Boolean actual = userService.remove(convertUserToDto(user));

        //then
        assertTrue(actual);
        verify(userRepository).remove(user);
    }

    @Test
    void removeTaskByUser_validData_existUserAndTask_shouldReturnTrueTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given && when
        User user = createTestUsers(1).get(0);
        user.setId(2);
        Task taskTrue = createTestTasks(1).get(0);
        taskTrue.setId(2);
        when(userRepository.removeTaskByUser(taskTrue, user)).thenReturn(true);

        //then
        assertTrue(userService.removeTaskByUser(convertTaskToDto(taskTrue), convertUserToDto(user)));
        verify(userRepository).removeTaskByUser(taskTrue, user);
    }

    @Test
    void removeTaskByUser_validData_existUserAndTaskNonDB_shouldReturnFalseTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given && when
        User user = createTestUsers(1).get(0);
        user.setId(2);
        Task taskFalse = createTestTasks(1).get(0);
        taskFalse.setId(2);
        when(userRepository.removeTaskByUser(taskFalse, user)).thenReturn(false);

        //then
        assertFalse(userService.removeTaskByUser(convertTaskToDto(taskFalse), convertUserToDto(user)));
        verify(userRepository).removeTaskByUser(taskFalse, user);
    }

    @Test
    void remove_existUser_shouldThrowServiceExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        User user = createTestUsers(1).get(0);
        user.setId(2);
        when(userRepository.remove(user)).thenThrow(CRMProjectRepositoryException.class);    //clean Data Base

        //then
        assertThrows(CRMProjectServiceException.class, () -> userService.remove(convertUserToDto(user)));
        verify(userRepository).remove(user);
    }
}

