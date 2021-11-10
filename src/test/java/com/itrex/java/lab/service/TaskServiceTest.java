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
import com.itrex.java.lab.repository.UserRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskServiceImpl taskService;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;

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
    void update_existTaskDTO_returnTaskDTOTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Task expected = RepositoryTestUtils.createTestTasksWithId(0, 1).get(0);
        when(taskRepository.selectById(expected.getId())).thenReturn(expected);
        when(taskRepository.update(expected)).thenReturn(expected);

        //when
        TaskDTO actual = taskService.update(convertTaskToDto(expected));

        //then
        assertEquals(1, actual.getId());
        assertEquals("Task test 0", actual.getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.getStatus());
        assertEquals(LocalDate.of(2001, 1, 1), actual.getDeadline());
        assertEquals("Task test info 0", actual.getInfo());
        verify(taskRepository).update(expected);
        verify(taskRepository).selectById(expected.getId());
    }

    @Test
    void update_existTaskDTONotDB_returnThrowServiceExceptionTestTest() throws CRMProjectRepositoryException {
        //given && when
        Task expected = RepositoryTestUtils.createTestTasksWithId(98, 1).get(0);
        when(taskRepository.selectById(expected.getId())).thenReturn(null);

        //then
        assertThrows(CRMProjectServiceException.class, () -> taskService.update(convertTaskToDto(expected)));
        verify(taskRepository).selectById(expected.getId());
    }

    @Test
    void remove_existTaskDTO_Test() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Task task = RepositoryTestUtils.createTestTasksWithId(0, 1).get(0);
        List <User> users = RepositoryTestUtils.createTestUsersWithId(0,1);
        when(taskRepository.selectById(1)).thenReturn(task);
        when(taskRepository.selectAllUsersByTask(task)).thenReturn(users);
        doNothing().when(userRepository).removeTaskByUser(task.getId(),users.get(0).getId());
        doNothing().when(taskRepository).remove(1);

        //when
        taskService.remove(1);

        //then
        verify(taskRepository).remove(1);
        verify(taskRepository,times(2)).selectById(1);
        verify(userRepository).removeTaskByUser(task.getId(),users.get(0).getId());
        verify(taskRepository).selectAllUsersByTask(task);
    }

    @Test
    void remove_existTaskDTO_shouldReturnThrowServiceExceptionTest() throws CRMProjectRepositoryException {
        //given && when
        //given
        Task task = RepositoryTestUtils.createTestTasksWithId(0, 1).get(0);
        List <User> users = RepositoryTestUtils.createTestUsersWithId(0,1);
        when(taskRepository.selectById(1)).thenReturn(task);
        when(taskRepository.selectAllUsersByTask(task)).thenReturn(users);
        doNothing().when(userRepository).removeTaskByUser(task.getId(),users.get(0).getId());
        doThrow(CRMProjectRepositoryException.class).when(taskRepository).remove(1);    //clean Data Base

        //then
        assertThrows(CRMProjectServiceException.class, () -> taskService.remove(1));
        verify(taskRepository).remove(1);
        verify(taskRepository,times(2)).selectById(1);
        verify(userRepository).removeTaskByUser(task.getId(),users.get(0).getId());
        verify(taskRepository).selectAllUsersByTask(task);


    }
}