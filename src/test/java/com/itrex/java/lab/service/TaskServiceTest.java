package com.itrex.java.lab.service;

import com.itrex.java.lab.dto.TaskDTO;
import com.itrex.java.lab.dto.UserDTO;
import com.itrex.java.lab.entity.Status;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.repository.RepositoryTestUtils;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.itrex.java.lab.utils.Convert.convertTaskToDto;
import static com.itrex.java.lab.utils.Convert.convertUserToDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskServiceImpl taskService;
    @Mock
    private TaskRepository taskRepository;

    @Test
    public void getAll_returnTaskDTOTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given && when
        final List<Task> result = RepositoryTestUtils.createTestTasks(2);
        result.get(0).setId(1);
        result.get(1).setId(2);
        when(taskRepository.selectAll()).thenReturn(result);

        //then
        assertFalse(taskService.getAll().isEmpty());
        verify(taskRepository).selectAll();
    }

    @Test
    void getAll_shouldThrowServiceExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        when(taskRepository.selectAll()).thenThrow(CRMProjectRepositoryException.class);

        //then
        assertThrows(CRMProjectServiceException.class, () -> taskService.getAll());
        verify(taskRepository).selectAll();
    }

    @Test
    void getById_existTaskDTOID_returnTaskDTOTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Integer idRole = 2;
        Task task = RepositoryTestUtils.createTestTasks(1).get(0);
        task.setId(2);
        when(taskRepository.selectById(idRole)).thenReturn(task);

        //when
        TaskDTO actual = taskService.getById(idRole);

        //then
        assertEquals(2, actual.getId());
        assertEquals("Task test 0", actual.getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.getStatus());
        assertEquals(LocalDate.of(2001, 1, 1), actual.getDeadline());
        assertEquals("Task test info 0", actual.getInfo());
        verify(taskRepository).selectById(idRole);
    }

    @Test
    void getById__existIDTaskDTONonDataBase_returnNULLTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Integer idRole = 28;
        when(taskRepository.selectById(idRole)).thenReturn(null);
        //when
        TaskDTO actual = taskService.getById(idRole);

        //then
        verify(taskRepository, Mockito.times(1)).selectById(idRole);
        assertNull(actual);
    }

    @Test
    void getById_returnThrowServiceExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        when(taskRepository.selectById(any())).thenThrow(CRMProjectRepositoryException.class);

        //then
        assertThrows(CRMProjectServiceException.class, () -> taskService.getById(any()));
        verify(taskRepository).selectById(any());
    }

    @Test
    void getAllUsersByTaskDTO_existTaskDTO_returnListOfUserDTOTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Task task = RepositoryTestUtils.createTestTasks(1).get(0);
        task.setId(1);
        List<User> users = RepositoryTestUtils.createTestUsers(2);
        users.get(0).setId(1);
        users.get(1).setId(2);
        when(taskRepository.selectAllUsersByTask(task)).thenReturn(users);

        //when
        List<UserDTO> actual = taskService.getAllUsersByTaskDTO(convertTaskToDto(task));

        //then
        assertEquals(1, actual.get(0).getId());
        assertEquals("Test 0", actual.get(0).getLogin());
        assertEquals("1230", actual.get(0).getPsw());
        assertEquals("ADMIN", actual.get(0).getRole().getRoleName());
        assertEquals("Ivanov 0", actual.get(0).getLastName());
        assertEquals("Ivan 0", actual.get(0).getFirstName());
        assertEquals(2, actual.get(1).getId());
        assertEquals("Test 1", actual.get(1).getLogin());
        assertEquals("1231", actual.get(1).getPsw());
        assertEquals("ADMIN", actual.get(1).getRole().getRoleName());
        assertEquals("Ivanov 1", actual.get(1).getLastName());
        assertEquals("Ivan 1", actual.get(1).getFirstName());
        verify(taskRepository).selectAllUsersByTask(task);
    }

    @Test
    void add_existTaskDTO_returnExistTaskDTOTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Task task = RepositoryTestUtils.createTestTasks(1).get(0);
        task.setId(1);
        when(taskRepository.add(task)).thenReturn(task);

        //when
        TaskDTO actual = taskService.add(convertTaskToDto(task));

        //then
        assertEquals(1, actual.getId());
        assertEquals("Task test 0", actual.getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.getStatus());
        assertEquals(LocalDate.of(2001, 1, 1), actual.getDeadline());
        assertEquals("Task test info 0", actual.getInfo());
        verify(taskRepository).add(task);
    }

    @Test
    void add_existCopyTaskWithId2_returnThrowServiceExceptionTypeTest() throws CRMProjectRepositoryException {
        //given && when
        Task task = RepositoryTestUtils.createTestTasks(1).get(0);
        when(taskRepository.add(task)).thenThrow(CRMProjectRepositoryException.class);  //There is Role "User" in Data Base

        //then
        assertThrows(CRMProjectServiceException.class, () -> taskService.add(convertTaskToDto(task)));
        verify(taskRepository).add(task);
    }

    @Test
    void addAll_validData_existTaskDTO_returnTaskDTOTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given

        List<Task> tasks = RepositoryTestUtils.createTestTasks(2);
        tasks.get(0).setId(1);
        tasks.get(1).setId(2);
        when(taskRepository.addAll(tasks)).thenReturn(tasks);
        List<TaskDTO> taskDTO = tasks.stream().map(task -> convertTaskToDto(task)).collect(Collectors.toList());

        //when
        List<TaskDTO> actual = taskService.addAll(taskDTO);

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
        verify(taskRepository).addAll(tasks);
    }

    @Test
    void addAll_existCopyTaskDTOById2_returnThrowServiceExceptionTypeTest() throws CRMProjectRepositoryException {
        //given && when
        List<Task> tasks = RepositoryTestUtils.createTestTasks(2);
        when(taskRepository.addAll(tasks)).thenThrow(CRMProjectRepositoryException.class);
        List<TaskDTO> taskDTOS = tasks.stream().map(task -> convertTaskToDto(task)).collect(Collectors.toList());

        //then
        assertThrows(CRMProjectServiceException.class, () -> taskService.addAll(taskDTOS));
        verify(taskRepository).addAll(tasks);
    }

    @Test
    void update_existTaskDTOAndInteger_returnTaskDTOTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Task expected = RepositoryTestUtils.createTestTasks(1).get(0);
        expected.setId(1);
        Integer testId = 1;
        when(taskRepository.update(expected,testId)).thenReturn(expected);

        //when
        TaskDTO actual = taskService.update(convertTaskToDto(expected),1);

        //then
        assertEquals(1, actual.getId());
        assertEquals("Task test 0", actual.getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.getStatus());
        assertEquals(LocalDate.of(2001, 1, 1), actual.getDeadline());
        assertEquals("Task test info 0", actual.getInfo());
        verify(taskRepository).update(expected,testId);
    }

    @Test
    void update_existTaskDTOAndIdTaskDTONotDB_returnNullTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given && when
        Task expected = RepositoryTestUtils.createTestTasks(1).get(0);
        Integer idNonDataBase = 99;
        when(taskRepository.update(expected,idNonDataBase)).thenReturn(null);

        //when
        TaskDTO actual = taskService.update(convertTaskToDto(expected), idNonDataBase);

        //then
        assertNull(actual);
        verify(taskRepository).update(expected,idNonDataBase);
    }

    @Test
    void update_existIdTaskDTO_returnThrowServiceExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        when(taskRepository.update(new Task(),1)).thenThrow(CRMProjectRepositoryException.class);

        //then
        assertThrows(CRMProjectServiceException.class, () -> taskService.update(new TaskDTO(), 1));
        verify(taskRepository).update(new Task(),1);
    }

    @Test
    void remove_existTaskDTO_returnTRUETest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        when(taskRepository.remove(new Task())).thenReturn(true);

        //when
        boolean actual = taskService.remove(new TaskDTO());

        //then
        assertTrue(actual);
        verify(taskRepository).remove(new Task());
    }

    @Test
    void remove_existTaskDTO_returnFalseTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        when(taskRepository.remove(new Task())).thenReturn(false);

        //when
        boolean actual = taskService.remove(new TaskDTO());

        //then
        assertFalse(actual);
        verify(taskRepository).remove(new Task());
    }

    @Test
    void removeTaskDTOByUserDTO_existUserAndTaskNonDB_shouldReturnTrueTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Task task = RepositoryTestUtils.createTestTasks(1).get(0);
        task.setId(1);
        User user = RepositoryTestUtils.createTestUsers(1).get(0);
        user.setId(1);
        when(taskRepository.removeUserByTask(task,user)).thenReturn(true);  //found on DataBase

        //when
        boolean actualTrue = taskService.removeUserByTask(convertTaskToDto(task), convertUserToDto(user));

        //then
        assertTrue(actualTrue);
        verify(taskRepository).removeUserByTask(task,user);
    }

    @Test
    void removeTaskDTOByUserDTO_existUserAndTask_shouldReturnFalseTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Task task = RepositoryTestUtils.createTestTasks(1).get(0);
        task.setId(1);
        User user = RepositoryTestUtils.createTestUsers(1).get(0);
        user.setId(1);
        when(taskRepository.removeUserByTask(task,user)).thenReturn(false);  // no found on DataBase

        //when
        boolean actualFalse = taskService.removeUserByTask(convertTaskToDto(task), convertUserToDto(user));

        //then
        assertFalse(actualFalse);
        verify(taskRepository).removeUserByTask(task,user );
    }

    @Test
    void remove_existTaskDTO_shouldReturnThrowServiceExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        when(taskRepository.remove(new Task())).thenThrow(CRMProjectRepositoryException.class);    //clean Data Base

        //then
        assertThrows(CRMProjectServiceException.class, () -> taskService.remove(new TaskDTO()));
        verify(taskRepository).remove(new Task());
    }
}
