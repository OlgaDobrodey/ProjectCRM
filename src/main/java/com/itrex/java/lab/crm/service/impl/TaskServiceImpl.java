package com.itrex.java.lab.crm.service.impl;

import com.itrex.java.lab.crm.dto.TaskDTO;
import com.itrex.java.lab.crm.entity.Status;
import com.itrex.java.lab.crm.entity.Task;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.crm.repository.impl.data.TaskRepository;
import com.itrex.java.lab.crm.service.TaskService;
import com.itrex.java.lab.crm.utils.ConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.itrex.java.lab.crm.utils.ConverterUtils.convertTaskToDto;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getAll() {

        return taskRepository.findAll().stream()
                .map(ConverterUtils::convertTaskToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO getById(Integer id) {

        return taskRepository.findById(id)
                .map(ConverterUtils::convertTaskToDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getAllUserTasksByUserId(Integer userId) {

        return taskRepository.findTasksByUsers_Id(userId).stream()
                .map(ConverterUtils::convertTaskToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskDTO add(TaskDTO task) throws CRMProjectServiceException {
        verification(task);

        Task addTask = Task.builder()
                .title(task.getTitle())
                .status(Status.NEW)
                .deadline(task.getDeadline())
                .info(task.getInfo())
                .build();
        return convertTaskToDto(taskRepository.save(addTask));
    }

    @Override
    @Transactional
    public TaskDTO update(TaskDTO task) throws CRMProjectServiceException {
        verification(task);

        Task update = taskRepository.findById(task.getId()).orElseThrow(() -> new CRMProjectServiceException("TASK NO FOUND DATA BASE"));

        update.setTitle(task.getTitle());
        update.setStatus(task.getStatus());
        update.setDeadline(task.getDeadline());
        update.setInfo(task.getInfo());
        return convertTaskToDto(taskRepository.save(update));
    }

    @Override
    @Transactional
    public void finishTaskByTaskId(Integer taskId) throws CRMProjectServiceException {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new CRMProjectServiceException("TASK NO FOUND DATA BASE"));
        task.setStatus(Status.DONE);

        task.getUsers().forEach(user -> user.getTasks().removeIf(t -> t.getId().equals(taskId)));
        taskRepository.save(task);
    }

    @Override
    @Transactional
    public void remove(Integer taskId) throws CRMProjectServiceException {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new CRMProjectServiceException("TASK NO FOUND DATA BASE"));
        task.setStatus(Status.DONE);

        task.getUsers().forEach(user -> user.getTasks().removeIf(t -> t.getId().equals(taskId)));
        taskRepository.save(task);
        taskRepository.delete(task);
    }

    @Override
    @Transactional
    public TaskDTO changeStatusDTO(Status status, Integer taskId) throws CRMProjectServiceException {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new CRMProjectServiceException("TASK NO FOUND DATA BASE"));

        if (status == Status.PROGRESS && CollectionUtils.isEmpty(task.getUsers())) {
            throw new CRMProjectServiceException("Wrong status: " + status);
        } else if (status != Status.PROGRESS && !CollectionUtils.isEmpty(task.getUsers())) {
            task.getUsers().forEach(user -> user.getTasks().removeIf(t -> t.getId().equals(taskId)));
        }
        task.setStatus(status);
        return convertTaskToDto(taskRepository.save(task));
    }

    /*
   Проверка таски:
   -title должен быть от 2 до 250 сиволов,
   -не должен быть isBlank;
   Статус, дата проверяются JSON parse.
   дата и инфо могут быть равны = null
      */
    private void verification(TaskDTO task) throws CRMProjectServiceException {

        if (task.getTitle().isBlank() || !task.getTitle().matches(".{2,250}$")) {
            throw new CRMProjectServiceException("No valid task title");
        }
        if (!task.getTitle().matches(".{1,250}$")){
            throw new CRMProjectServiceException("No valid task info");
        }
    }

}
