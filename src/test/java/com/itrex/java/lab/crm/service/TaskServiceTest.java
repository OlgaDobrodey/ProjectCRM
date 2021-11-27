package com.itrex.java.lab.crm.service;

import com.itrex.java.lab.crm.dto.TaskDTO;
import com.itrex.java.lab.crm.entity.Status;
import com.itrex.java.lab.crm.entity.Task;
import com.itrex.java.lab.crm.entity.User;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.crm.repository.RepositoryTestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.itrex.java.lab.crm.repository.RepositoryTestUtils.createTestTasksWithId;
import static com.itrex.java.lab.crm.repository.RepositoryTestUtils.createTestUsersWithId;
import static com.itrex.java.lab.crm.utils.ConverterUtils.convertTaskToDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest extends BaseServiceTest {

    @Test
    public void getAll_returnTaskDTOTest() {
        //given && when
        final List<Task> result = RepositoryTestUtils.createTestTasks(2);
        result.get(0).setId(1);
        result.get(1).setId(2);
        when(taskRepository.findAll()).thenReturn(result);

        //then
        assertFalse(taskService.getAll().isEmpty());
        verify(taskRepository).findAll();
    }

    @Test
    void getById_existTaskDTOID_returnTaskDTOTest() {
        //given
        Integer roleId = 2;
        Task task = RepositoryTestUtils.createTestTasks(1).get(0);
        task.setId(2);
        when(taskRepository.findById(roleId)).thenReturn(Optional.of(task));

        //when
        TaskDTO actual = taskService.getById(roleId);

        //then
        assertEquals(2, actual.getId());
        assertEquals("Task test 0", actual.getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.getStatus());
        assertEquals(LocalDate.of(2001, 1, 1), actual.getDeadline());
        assertEquals("Task test info 0", actual.getInfo());
        verify(taskRepository).findById(roleId);
    }

    @Test
    void getById__existIDTaskDTONonDataBase_returnNULLTest() {
        //given
        Integer roleId = 28;
        when(taskRepository.findById(roleId)).thenReturn(Optional.ofNullable(null));
        //when
        TaskDTO actual = taskService.getById(roleId);

        //then
        verify(taskRepository, Mockito.times(1)).findById(roleId);
        assertNull(actual);
    }

    @Test
    void getAllTaskDTOByUserDTO_existUserDTO_returnListOfTaskDTOTest() {
        //given
        List<Task> tasks = createTestTasksWithId(0, 2);
        Integer id = 2;
        when(taskRepository.findTasksByUsers_Id(id)).thenReturn(tasks);

        //when
        List<TaskDTO> actual = taskService.getAllUserTasksByUserId(id);

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
        verify(taskRepository).findTasksByUsers_Id(id);
    }

    @Test
    void add_existTaskDTO_returnExistTaskDTOTest() throws CRMProjectServiceException {
        //given
        Task returnTask = createTestTasksWithId(0, 1).get(0);
        Task task = RepositoryTestUtils.createTestTasks(1).get(0);
        when(taskRepository.save(task)).thenReturn(returnTask);

        //when
        TaskDTO actual = taskService.add(convertTaskToDto(returnTask));

        //then
        assertEquals(1, actual.getId());
        assertEquals("Task test 0", actual.getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.getStatus());
        assertEquals(LocalDate.of(2001, 1, 1), actual.getDeadline());
        assertEquals("Task test info 0", actual.getInfo());
        verify(taskRepository).save(task);
    }

    @Test
    void update_existTaskDTO_returnTaskDTOTest() throws CRMProjectServiceException {
        //given
        Task expected = createTestTasksWithId(0, 1).get(0);
        when(taskRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        when(taskRepository.save(expected)).thenReturn(expected);

        //when
        TaskDTO actual = taskService.update(convertTaskToDto(expected));

        //then
        assertEquals(1, actual.getId());
        assertEquals("Task test 0", actual.getTitle(), "assert Title");
        assertEquals(Status.NEW, actual.getStatus());
        assertEquals(LocalDate.of(2001, 1, 1), actual.getDeadline());
        assertEquals("Task test info 0", actual.getInfo());
        verify(taskRepository).save(expected);
        verify(taskRepository).findById(expected.getId());
    }

    @Test
    void update_existTaskDTONotDB_returnThrowServiceExceptionTestTest() {
        //given && when
        Task expected = createTestTasksWithId(98, 1).get(0);
        when(taskRepository.findById(expected.getId())).thenReturn(Optional.ofNullable(null));

        //then
        assertThrows(CRMProjectServiceException.class, () -> taskService.update(convertTaskToDto(expected)));
        verify(taskRepository).findById(expected.getId());
    }

    @Test
    void finishTaskByTaskId_existIdTaskTest() throws CRMProjectServiceException {
        //given
        User user = createTestUsersWithId(1, 1).get(0);
        Task task = createTestTasksWithId(1, 1).get(0);
        task.setUsers(List.of(user));
        user.setTasks(new ArrayList<>(List.of(task)));
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        //when
        taskService.finishTaskByTaskId(task.getId());
        assertEquals(Status.DONE, task.getStatus());

        //then
        verify(taskRepository).findById(task.getId());
    }

    @Test
    void remove_existTaskDTO_Test() throws CRMProjectServiceException {
        //given
        User user = createTestUsersWithId(1, 1).get(0);
        Task task = createTestTasksWithId(1, 1).get(0);
        task.setUsers(List.of(user));
        user.setTasks(new ArrayList<>(List.of(task)));
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        //when
        taskService.remove(task.getId());

        //then
        verify(taskRepository).findById(task.getId());
        verify(taskRepository).delete(task);
    }

    @Test
    void remove_existTaskDTO_shouldReturnThrowServiceExceptionTest() {
        //given && when
        User user = createTestUsersWithId(1, 1).get(0);
        Task task = createTestTasksWithId(1, 1).get(0);
        task.setUsers(List.of(user));
        user.setTasks(new ArrayList<>(List.of(task)));
        when(taskRepository.findById(task.getId())).thenReturn(Optional.ofNullable(null));

        //then
        assertThrows(CRMProjectServiceException.class, () -> taskService.remove(task.getId()));
        verify(taskRepository).findById(task.getId());
    }

    @Test
    void changePasswordDTO_existStatusAndTaskIdWithoutUsers_returnTaskDTO() throws CRMProjectServiceException {
        //given
        Task task = createTestTasksWithId(0, 1).get(0);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        //when
        TaskDTO taskDTO = taskService.changeStatusDTO(Status.NEW, task.getId());

        //then
        assertEquals(Status.NEW, taskDTO.getStatus());
        verify(taskRepository).findById(task.getId());
        verify(taskRepository).save(task);
    }

    @Test
    void changePasswordDTO_existStatusAndTaskId_returnTaskDTO() throws CRMProjectServiceException {
        //given
        Task task = createTestTasksWithId(0, 1).get(0);
        List<User> users = createTestUsersWithId(0, 2);
        task.setUsers(users);
        users.forEach(user -> user.setTasks(new ArrayList<>(List.of(task))));
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        //when
        TaskDTO taskDTOProgress = taskService.changeStatusDTO(Status.PROGRESS, task.getId());
        TaskDTO taskDTONew = taskService.changeStatusDTO(Status.NEW, task.getId());
        TaskDTO taskDTODone = taskService.changeStatusDTO(Status.DONE, task.getId());

        //then
        assertEquals(Status.PROGRESS, taskDTOProgress.getStatus());
        assertEquals(Status.NEW, taskDTONew.getStatus());
        assertEquals(Status.DONE, taskDTODone.getStatus());

        verify(taskRepository, times(3)).findById(task.getId());
        verify(taskRepository, times(3)).save(task);
    }

}
