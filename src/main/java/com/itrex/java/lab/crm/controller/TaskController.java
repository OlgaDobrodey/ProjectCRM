package com.itrex.java.lab.crm.controller;

import com.itrex.java.lab.crm.dto.TaskDTO;
import com.itrex.java.lab.crm.dto.UserDTO;
import com.itrex.java.lab.crm.entity.Status;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.crm.service.TaskService;
import com.itrex.java.lab.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TaskController extends BaseController{

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    /*
     Посмотреть все существующие задачи/таски.
     Необходимо для Admin и Controlera(для анализа, общего состояния бизнес-процессов)
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> read() {
        List<TaskDTO> tasks = new ArrayList<>();
        try {
            tasks = taskService.getAll();
        } catch (CRMProjectServiceException e) {
            e.getMessage();
        }
        return tasks != null && !tasks.isEmpty()
                ? new ResponseEntity<>(tasks, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
    Посмотреть задачу, необходимо для ознакомления содержимым задачи/task
     */
    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> readTaskByIdTask(@PathVariable Integer id) {
        TaskDTO readTask = null;
        try {
            readTask = taskService.getById(id);
        } catch (CRMProjectServiceException e) {
            e.getMessage();
        }
        return readTask != null
                ? new ResponseEntity<>(readTask, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
    Дополнительная функция, которая дает возможность просмотреть
    всех исполнителей задачи с заданным id
     */
    @GetMapping("/tasks/{id}/users")
    public ResponseEntity<List<UserDTO>> readAllTaskUsersByTaskId(@PathVariable Integer id) {
        List<UserDTO> users = new ArrayList<>();
        try {
            users = userService.getAllTaskUsersByTaskId(id);
        } catch (CRMProjectServiceException e) {
            e.getMessage();
        }
        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
    Создание новой задачи, может выполнять Admin
    (Постановка задачи)
    Task создается всегда со STATUS.NEW
    taskId - формируется автоматически
     */
    @PostMapping("/tasks")
    public ResponseEntity<?> create(@RequestBody TaskDTO taskDTO) {
        TaskDTO createTask = null;
        try {
            createTask = taskService.add(taskDTO);
        } catch (CRMProjectServiceException e) {
            e.getMessage();
        }
        return new ResponseEntity<>(createTask, HttpStatus.CREATED);
    }

    /*
    Корректировка задачи, Изменение статуса, внесение пояснений в инфо, корректировка названия
    возможное смещение сроков
     */
    @PutMapping(value = "/tasks/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") int id, @RequestBody TaskDTO taskDTO) {
        taskDTO.setId(id);
        TaskDTO updated = null;
        try {
            updated = taskService.update(taskDTO);
        } catch (CRMProjectServiceException e) {
            e.getStackTrace();
        }
        return updated != null
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /*
    Открепление всех пользователей от задачи.
    Возможное условие использование: -предварительный этап перед удалением таски;
    - завершение всей работы по заданию, всеми сотрудниками;
    -смена команды.
    Но не удаление таски
     */
    @DeleteMapping("/tasks/{id}/users")
    public ResponseEntity<?> finishTaskByTaskId(@PathVariable(name = "id") int id) {
        try {
            taskService.finishTaskByTaskId(id);
        } catch (CRMProjectServiceException e) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    Удаление задачи: функция используется если задача выполнена или была создана случайно,
     является некорректной и не подлежит изменению.
     Статус переводится в STATUS.DONE также открепляются все пользователи.
     */
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        try {
            taskService.remove(id);
        } catch (CRMProjectServiceException e) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    Изменение значения статуса. Перевод его в одно из состояний: NEW, DONE и PROGRESS
     */
    @PutMapping("/tasks/{id}/{status}")
    public ResponseEntity<?> update(@PathVariable Integer id, @PathVariable(name = "status") Status status) {
        TaskDTO taskStatus = null;
        try {
            taskStatus = taskService.changeStatusDTO(status, id);
        } catch (CRMProjectServiceException e) {
            e.getStackTrace();
        }
        return taskStatus != null
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);//TODO
    }

}
