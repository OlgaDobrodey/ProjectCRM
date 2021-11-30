package com.itrex.java.lab.crm.service;

import com.itrex.java.lab.crm.dto.PasswordDTOForChanges;
import com.itrex.java.lab.crm.dto.UserDTO;
import com.itrex.java.lab.crm.dto.UserDtoLoginRoleName;
import com.itrex.java.lab.crm.entity.Role;
import com.itrex.java.lab.crm.entity.Status;
import com.itrex.java.lab.crm.entity.Task;
import com.itrex.java.lab.crm.entity.User;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.crm.utils.ConverterUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.itrex.java.lab.crm.repository.RepositoryTestUtils.*;
import static com.itrex.java.lab.crm.utils.ConverterUtils.convertUserToDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest extends BaseServiceTest {

    @Test
    public void getAll_returnUserDTOTest() {
        //given && when
        List<User> result = createTestUsersWithId(0, 2);
        when(userRepository.findAll()).thenReturn(result);

        //then
        assertFalse(userService.getAll().isEmpty());
        assertEquals(userService.getAll().size(), 2);
        verify(userRepository, Mockito.times(2)).findAll();
    }

    @Test
    void getById_existUserDTOId_returnUserDTOTest() {
        //given
        Integer idUser = 2;
        User user = createTestUsersWithId(1, 1).get(0);
        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));

        //when
        UserDTO actual = userService.getById(idUser);

        //then
        assertEquals(2, actual.getId());
        assertEquals("Test1", actual.getLogin());
        assertEquals("1231", actual.getPsw());
        assertEquals(1, actual.getRoleId());
        assertEquals("IvanovB", actual.getLastName());
        assertEquals("IvanB", actual.getFirstName());
        verify(userRepository).findById(idUser);
    }

    @Test
    void getById_existIDUsereDTONotDataBase_returnNULLTest() {
        //given
        Integer idUser = 28;
        when(userRepository.findById(idUser)).thenReturn(Optional.ofNullable(null));

        //when
        UserDTO actual = userService.getById(idUser);

        //then
        assertNull(actual);
        verify(userRepository).findById(idUser);
    }

    @Test
    void getByLogin_existUserDTOLogin_returnUserDTOLoginRoleNameTest() {
        //given
        User user = createTestUsersWithId(1, 1).get(0);
        when(userRepository.findUserByLogin(user.getLogin())).thenReturn(Optional.of(user));

        //when
        UserDtoLoginRoleName actual = userService.getByLogin(user.getLogin());

        //then
        assertEquals(2, actual.getId());
        assertEquals("Test1", actual.getLogin());
        assertEquals("1231", actual.getPsw());
        assertEquals("ADMIN", actual.getRoleName());
        verify(userRepository).findUserByLogin(user.getLogin());
    }

    @Test
    void getByLogin_existLoginUsereDTONotDataBase_returnNULLTest() {
        //given
        when(userRepository.findUserByLogin(any())).thenReturn(Optional.ofNullable(null));

        //when
        UserDtoLoginRoleName actual = userService.getByLogin(any());

        //then
        assertNull(actual);
        verify(userRepository).findUserByLogin(any());
    }

    @Test
    void getAllUsersByTaskDTO_existTaskDTO_returnListOfUserDTOTest() throws CRMProjectServiceException {
        //given
        Task task = createTestTasks(1).get(0);
        task.setId(1);
        List<User> users = createTestUsers(2);
        users.get(0).setId(1);
        users.get(1).setId(2);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userRepository.findUsersByTasks_id(task.getId())).thenReturn(users);

        //when
        List<UserDTO> actual = userService.getAllTaskUsersByTaskId(task.getId());

        //then
        assertEquals(1, actual.get(0).getId());
        assertEquals("Test0", actual.get(0).getLogin());
        assertEquals("1230", actual.get(0).getPsw());
        assertEquals(1, actual.get(0).getRoleId());
        assertEquals("IvanovA", actual.get(0).getLastName());
        assertEquals("IvanA", actual.get(0).getFirstName());
        assertEquals(2, actual.get(1).getId());
        assertEquals("Test1", actual.get(1).getLogin());
        assertEquals("1231", actual.get(1).getPsw());
        assertEquals(1, actual.get(1).getRoleId());
        assertEquals("IvanovB", actual.get(1).getLastName());
        assertEquals("IvanB", actual.get(1).getFirstName());
        verify(userRepository).findUsersByTasks_id(task.getId());
        verify(taskRepository).findById(task.getId());
    }

    @Test
    void getAllUsersFromRoleId_existRoleId_returnListUsersTest() throws CRMProjectServiceException {
        //given && when
        Role role = Role.builder().users(new ArrayList<>()).build();
        List<User> users = createTestUsersWithId(0, 2);
        List<UserDTO> userDTOS = users.stream().map(ConverterUtils::convertUserToDto).collect(Collectors.toList());
        when(roleRepository.findById(any())).thenReturn(Optional.of(role));
        when(userRepository.findUsersByRole_Id(role.getId())).thenReturn(users);

        //then
        assertEquals(userDTOS, userService.getAllRoleUsersByRoleId(role.getId()));
        verify(roleRepository).findById(any());
        verify(userRepository).findUsersByRole_Id(role.getId());
    }

    @Test
    void getAllUsersFromRoleId_existRoleIdNoDB_shouldThrowServiceExceptionTest() {
        //given && when
        when(roleRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        //then
        assertThrows(CRMProjectServiceException.class, () -> userService.getAllRoleUsersByRoleId(any()));
        verify(roleRepository).findById(any());
    }


    @Test
    void add_validData_existUser_returnUserTest() throws CRMProjectServiceException {
        //given
        User user = createTestUsers(1).get(0);
        User returnUser = createTestUsersWithId(0, 1).get(0);
        when(roleRepository.findById(user.getRole().getId())).thenReturn(Optional.of(user.getRole()));
        when(userRepository.save(user)).thenReturn(returnUser);

        //when
        UserDTO actual = userService.add(convertUserToDto(user));

        //then
        assertEquals(1, actual.getId());
        assertEquals("Test0", actual.getLogin());
        assertEquals("1230", actual.getPsw());
        assertEquals(1, actual.getRoleId());
        assertEquals("IvanovA", actual.getLastName());
        assertEquals("IvanA", actual.getFirstName());
        verify(userRepository).save(user);
        verify(roleRepository).findById(1);
    }

    @Test
    void update_validData_existUser_returnUserTest() throws CRMProjectServiceException {
        //given
        User expected = createTestUsersWithId(0, 1).get(0);
        when(userRepository.findById(1)).thenReturn(Optional.of(expected));
        when(roleRepository.findById(1)).thenReturn(Optional.of(Role.builder().id(1).roleName("ADMIN").build()));
        when(userRepository.save(expected)).thenReturn(expected);

        //when
        UserDTO actual = userService.update(convertUserToDto(expected));

        //then
        assertEquals(1, actual.getId());
        assertEquals("Test0", actual.getLogin());
        assertEquals("1230", actual.getPsw());
        assertEquals(1, actual.getRoleId());
        assertEquals("IvanovA", actual.getLastName());
        assertEquals("IvanA", actual.getFirstName());
        verify(userRepository).save(expected);
        verify(userRepository).findById(any());
        verify(roleRepository).findById(any());
    }

    @Test
    void assignTaskFromUser_existIdTaskAndIdUserTest() throws CRMProjectServiceException {
        //given
        User user = createTestUsersWithId(1, 1).get(0);
        Task task = createTestTasksWithId(1, 1).get(0);
        user.setTasks(new ArrayList<>());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        //when
        userService.assignTaskToUser(task.getId(), user.getId());

        //then
        verify(userRepository).findById(any());
        verify(taskRepository).findById(any());
    }

    @Test
    void update_validData_existUserNotDB_returnNullTest() {
        //given && when
        User expected = createTestUsersWithId(0, 1).get(0);
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(null));

        //then
        assertThrows(CRMProjectServiceException.class, () -> userService.update(convertUserToDto(expected)));
        verify(userRepository).findById(any());
    }

    @Test
    void update_existUser_shouldThrowServiceExceptionTest() {
        //given && when
        User user = createTestUsersWithId(0, 1).get(0);
        when(roleRepository.findById(1)).thenReturn(Optional.ofNullable(null));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        //then
        assertThrows(CRMProjectServiceException.class, () -> userService.update(convertUserToDto(user)));
        verify(userRepository).findById(1);
        verify(roleRepository).findById(1);
    }

    @Test
    void remove_existUserDTO_shouldReturnExistTrueTest() throws CRMProjectServiceException {
        //given
        User user = createTestUsersWithId(1, 2).get(0);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);
        //when
        userService.remove(user.getId());

        //then
        verify(userRepository).delete(user);
        verify(userRepository).findById(any());
    }

    @Test
    void revokeTaskFromUserId_existUserIdAndTaskIdTest() throws CRMProjectServiceException {
        //given
        Task task = createTestTasksWithId(1, 1).get(0);
        User user = createTestUsersWithId(1, 1).get(0);
        user.setTasks(new ArrayList<>(List.of(task)));
        task.setUsers(new ArrayList<>(List.of(user)));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        // when
        userService.revokeTaskFromUser(task.getId(), user.getId());

        //then
        assertEquals(Status.DONE, task.getStatus());
        verify(userRepository).findById(user.getId());
        verify(taskRepository).findById(task.getId());
    }

    @Test
    void revokeTaskFromUserId_existUserIdAndTaskIdHadMoreOneUsersTest() throws CRMProjectServiceException {
        //given
        Task task = createTestTasksWithId(1, 1).get(0);
        List<User> users = createTestUsersWithId(1, 3);
        users.forEach(u -> u.setTasks(new ArrayList<>(List.of(task))));
        task.setUsers(users);
        when(userRepository.findById(users.get(0).getId())).thenReturn(Optional.of(users.get(0)));
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        // when
        userService.revokeTaskFromUser(task.getId(), users.get(0).getId());

        //then
        assertEquals(Status.NEW, task.getStatus());
        verify(userRepository).findById(users.get(0).getId());
        verify(taskRepository).findById(task.getId());
    }

    @Test
    void revokeTaskFromUserId_existUserIdAndTaskIdNoDBTest() {
        //given
        Task task = createTestTasksWithId(1, 1).get(0);
        User user = createTestUsersWithId(1, 1).get(0);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.ofNullable(null));

        // when
        assertThrows(CRMProjectServiceException.class, () -> userService.revokeTaskFromUser(task.getId(), user.getId()));

        //then
        verify(taskRepository).findById(task.getId());
    }

    @Test
    void revokeAllUserTasksByUserId_existIdUserTest() throws CRMProjectServiceException {
        //given
        User user = createTestUsersWithId(1, 2).get(0);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        //when
        userService.revokeAllUserTasksByUserId(2);

        //then
        verify(userRepository).findById(any());
    }

    @Test
    void updateUserPassword_exitPasswordDTOAndUserId_returnUserDTOTest() throws CRMProjectServiceException {
        //given
        User user = createTestUsersWithId(0, 1).get(0);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        PasswordDTOForChanges pdtc = PasswordDTOForChanges.builder()
                .newPassword("12345")
                .oldPassword("1230")
                .repeatNewPassword("12345")
                .build();
        when(userRepository.save(user)).thenReturn(user);

        //when
        UserDTO expected = userService.updateUserPassword(pdtc, user.getId());

        //then
        assertEquals(1, expected.getId());
        assertEquals("Test0", expected.getLogin());
        assertEquals("12345", expected.getPsw());
        assertEquals(1, expected.getRoleId());
        assertEquals("IvanovA", expected.getLastName());
        assertEquals("IvanA", expected.getFirstName());
        verify(userRepository).findById(user.getId());
        verify(userRepository).save(user);
    }

    @Test
    void updateUserPassword_exitPasswordDTOEqualsNullAndUserIdEqualsNull_shouldThrowServiceExceptionTest() {
        //given && when
        User user = createTestUsersWithId(0, 1).get(0);
        Integer userIdNoDB = 5;
        when(userRepository.findById(userIdNoDB)).thenReturn(Optional.ofNullable(null));
        PasswordDTOForChanges pdtcNewPswEmpty = PasswordDTOForChanges.builder()
                .newPassword("")
                .oldPassword("1230")
                .repeatNewPassword("")
                .build();

        PasswordDTOForChanges pdtcOldPswEmpty = PasswordDTOForChanges.builder()
                .newPassword("1235")
                .oldPassword(" ")
                .build();

        PasswordDTOForChanges pdtc = PasswordDTOForChanges.builder()
                .newPassword("12345")
                .oldPassword("1230")
                .repeatNewPassword("12345")
                .build();

        PasswordDTOForChanges pdtcRepeatPasEmpty = PasswordDTOForChanges.builder()
                .newPassword("12345")
                .repeatNewPassword("1234")
                .oldPassword("1230")
                .build();

        //then
        assertThrows(CRMProjectServiceException.class,
                () -> userService.updateUserPassword(pdtcNewPswEmpty, user.getId()));
        assertThrows(CRMProjectServiceException.class,
                () -> userService.updateUserPassword(pdtcOldPswEmpty, user.getId()));
        assertThrows(CRMProjectServiceException.class,
                () -> userService.updateUserPassword(pdtc, userIdNoDB));
        assertThrows(CRMProjectServiceException.class,
                () -> userService.updateUserPassword(pdtcRepeatPasEmpty, user.getId()));
        verify(userRepository).findById(userIdNoDB);
    }

}

