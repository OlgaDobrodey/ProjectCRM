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

import static com.itrex.java.lab.repository.RepositoryTestUtils.createTestTasksWithId;
import static com.itrex.java.lab.repository.RepositoryTestUtils.createTestUsersWithId;
import static com.itrex.java.lab.utils.ConverterUtils.convertTaskToDto;
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
        Integer roleId = 2;
        Task task = RepositoryTestUtils.createTestTasks(1).get(0);
        task.setId(2);
        when(taskRepository.selectById(roleId)).thenReturn(task);

        //when
        TaskDTO actual = taskService.getById(roleId);

        //then
        assertEquals(2, actual.getId());
        assertEquals("Task test 0", actual.getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.getStatus());
        assertEquals(LocalDate.of(2001, 1, 1), actual.getDeadline());
        assertEquals("Task test info 0", actual.getInfo());
        verify(taskRepository).selectById(roleId);
    }

    @Test
    void getById__existIDTaskDTONonDataBase_returnNULLTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Integer roleId = 28;
        when(taskRepository.selectById(roleId)).thenReturn(null);
        //when
        TaskDTO actual = taskService.getById(roleId);

        //then
        verify(taskRepository, Mockito.times(1)).selectById(roleId);
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
        Integer id = 2;
        when(taskRepository.selectAllTasksByUserId(id)).thenReturn(tasks);

        //when
        List<TaskDTO> actual = taskService.getAllTasksByUserId(id);

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
        verify(taskRepository).selectAllTasksByUserId(id);
    }

    @Test
    void add_existTaskDTO_returnExistTaskDTOTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Task returnTask = RepositoryTestUtils.createTestTasksWithId(0, 1).get(0);
        Task task = RepositoryTestUtils.createTestTasks(1).get(0);
        when(taskRepository.add(task)).thenReturn(returnTask);

        //when
        TaskDTO actual = taskService.add(convertTaskToDto(returnTask));

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
        taskService.revokeAllTasksFromUserId(2);

        //then
        verify(userRepository).selectById(any());
    }

    @Test
    void remove_existTaskDTO_Test() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Task task = RepositoryTestUtils.createTestTasksWithId(0, 1).get(0);
        doNothing().when(userService).revokeAllUsersFromTaskId(task.getId());
        doNothing().when(taskRepository).remove(task.getId());

        //when
        taskService.remove(1);

        //then
        verify(userService).revokeAllUsersFromTaskId(task.getId());
        verify(taskRepository).remove(task.getId());
    }

    @Test
    void remove_existTaskDTO_shouldReturnThrowServiceExceptionTest() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given && when
        Task task = RepositoryTestUtils.createTestTasksWithId(0, 1).get(0);
        doNothing().when(userService).revokeAllUsersFromTaskId(task.getId());
        doThrow(CRMProjectRepositoryException.class).when(taskRepository).remove(task.getId());

        //then
        assertThrows(CRMProjectServiceException.class, () -> taskService.remove(1));
        verify(userService).revokeAllUsersFromTaskId(task.getId());
        verify(taskRepository).remove(task.getId());
    }

    @Test
    void changeStatusForTaskId_existStatusAndTaskIdWithoutUsers_returnTaskDTO() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Task task = createTestTasksWithId(0, 1).get(0);
        when(taskRepository.selectById(task.getId())).thenReturn(task);

        //when
        TaskDTO taskDTO = taskService.changeStatusForTaskId(Status.NEW, task.getId());

        //then
        assertEquals(Status.NEW, taskDTO.getStatus());
        verify(taskRepository).selectById(task.getId());
    }

    @Test
    void changeStatusForTaskId_existStatusAndTaskId_returnTaskDTO() throws CRMProjectRepositoryException, CRMProjectServiceException {
        //given
        Task task = createTestTasksWithId(0, 1).get(0);
        List<User> users = createTestUsersWithId(0, 2);
        task.setUsers(users);
        when(taskRepository.selectById(task.getId())).thenReturn(task);

        //when
        TaskDTO taskDTOProgress = taskService.changeStatusForTaskId(Status.PROGRESS, task.getId());
        TaskDTO taskDTONew = taskService.changeStatusForTaskId(Status.NEW, task.getId());
        TaskDTO taskDTODone = taskService.changeStatusForTaskId(Status.DONE, task.getId());


        //then
        assertEquals(Status.PROGRESS, taskDTOProgress.getStatus());
        assertEquals(Status.NEW, taskDTONew.getStatus());
        assertEquals(Status.DONE, taskDTODone.getStatus());

        verify(taskRepository,times(3)).selectById(task.getId());
    }

}
