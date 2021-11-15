package com.itrex.java.lab.service;

import com.itrex.java.lab.dto.TaskDTO;
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

import static com.itrex.java.lab.repository.RepositoryTestUtils.createTestTasksWithId;
import static com.itrex.java.lab.repository.RepositoryTestUtils.createTestUsersWithId;
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
    @Mock
    private UserService userService;


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
    void getAllTaskDTOByUserDTO_existUserDTO_returnListOfTaskDTOTest()
            throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        List<Task> tasks = createTestTasksWithId(0, 2);
        Integer idUser = 2;
        when(taskRepository.selectAllTasksByUser(idUser)).thenReturn(tasks);

        //when
        List<TaskDTO> actual = taskService.getAllTasksByUser(idUser);

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
        verify(taskRepository).selectAllTasksByUser(idUser);
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
        Task expected = createTestTasksWithId(0, 1).get(0);
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
        Task expected = createTestTasksWithId(98, 1).get(0);
        when(taskRepository.selectById(expected.getId())).thenReturn(null);

        //then
        assertThrows(CRMProjectServiceException.class, () -> taskService.update(convertTaskToDto(expected)));
        verify(taskRepository).selectById(expected.getId());
    }

    @Test
    void removeAllTasksByUser_existIdUserTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        User user = createTestUsersWithId(1, 2).get(0);
        when(userRepository.selectById(user.getId())).thenReturn(user);

        //when
        taskService.removeAllTasksByUser(2);

        //then
        verify(userRepository).selectById(any());
    }

    @Test
    void remove_existTaskDTO_Test() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Task task = RepositoryTestUtils.createTestTasksWithId(0, 1).get(0);
        doNothing().when(userService).removeAllUsersByTask(task.getId());
        doNothing().when(taskRepository).remove(task.getId());

        //when
        taskService.remove(1);

        //then
        verify(userService).removeAllUsersByTask(task.getId());
        verify(taskRepository).remove(task.getId());
    }

    @Test
    void remove_existTaskDTO_shouldReturnThrowServiceExceptionTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given && when
        //given
        Task task = RepositoryTestUtils.createTestTasksWithId(0, 1).get(0);
        doNothing().when(userService).removeAllUsersByTask(task.getId());
        doThrow(CRMProjectRepositoryException.class).when(taskRepository).remove(task.getId());

        //then
        assertThrows(CRMProjectServiceException.class, () -> taskService.remove(1));
        verify(userService).removeAllUsersByTask(task.getId());
        verify(taskRepository).remove(task.getId());
    }
}
