package com.itrex.java.lab.crm.controller;

import com.itrex.java.lab.crm.dto.PasswordDTOForChanges;
import com.itrex.java.lab.crm.dto.TaskDTO;
import com.itrex.java.lab.crm.dto.UserDTO;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.crm.service.TaskService;
import com.itrex.java.lab.crm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final TaskService taskService;
    private final UserService userService;

    /*
    Посмотреть всех пользователей.
    Необходимо для осуществления информирования и контроля состояния бизнес-процессов
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> read() {

        List<UserDTO> users = userService.getAll();

        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
   Посмотреть конкретного пользователя, необходимо для расшифровки конкретного пользователя, его роли.
   По запросу "/{id}/tasks" -можно посмотреть задачи в его выполнении
    */
    @GetMapping("/users/{id}")
    public ResponseEntity<?> readUserByUserId(@PathVariable Integer id) {
        UserDTO readed = userService.getById(id);

        return readed != null
                ? new ResponseEntity<>(readed, HttpStatus.OK)
                : new ResponseEntity<>("no found user in DB", HttpStatus.NOT_FOUND);
    }

    /*
   Дополнительная функция, которая дает возможность просмотреть
   все задания данного пользователя
   */
    @GetMapping("/users/{id}/tasks")
    public ResponseEntity<List<TaskDTO>> readAllUserTasksByUserId(@PathVariable Integer id) {

        List<TaskDTO> tasks = taskService.getAllUserTasksByUserId(id);

        return tasks != null && !tasks.isEmpty()
                ? new ResponseEntity<>(tasks, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
    Создание нового пользователя
    */
    @PostMapping("/users")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
        UserDTO createUser;
        try {
            createUser = userService.add(userDTO);
        } catch (CRMProjectServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(createUser, HttpStatus.CREATED);
    }

    /*
   Корректировка пользователя, изменение роли
    */
    @PutMapping(value = "/users/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_CONTROLLER"})
    public ResponseEntity<?> update(@PathVariable(name = "id") int id, @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        UserDTO updated;
        try {
            updated = userService.update(userDTO);
        } catch (CRMProjectServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return updated != null
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /*
    Обновление пароля пользователя с данным id, проверека старого
     */
    @PutMapping("/users/{id}/password")
    @Secured({"ROLE_ADMIN", "ROLE_CONTROLLER"})
    public ResponseEntity<?> updateUserPassword(
            @PathVariable(name = "id") int id, @RequestBody PasswordDTOForChanges psw) {
        try {
            UserDTO userDTO = userService.updateUserPassword(psw, id);
            return new ResponseEntity<>(userDTO,HttpStatus.OK);

        } catch (CRMProjectServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /*
    Добавление задачи для пользователя
    */
    @PutMapping(value = "/users/{userId}/tasks/{taskId}")
    @Secured({"ROLE_ADMIN", "ROLE_CONTROLLER"})
    public ResponseEntity<?> assignTaskToUser(@PathVariable(name = "userId") int userId,
                                              @PathVariable(name = "taskId") int taskId) {
        try {
            userService.assignTaskToUser(taskId, userId);
        } catch (CRMProjectServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    Удаление юзера из системы, используется при выбытии пользователя из всех бизнес процессах.
    При удалении пользователь удаляется со всех заданий, которые выполнял.
 */
    @DeleteMapping("/users/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        try {
            userService.remove(id);
        } catch (CRMProjectServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    Открепление задачи от пользователя (задача выполнена или переопределиться на другого пользователя)
  */
    @DeleteMapping(value = "/users/{userId}/tasks/{taskId}")
    @Secured({"ROLE_ADMIN", "ROLE_CONTROLLER"})
    public ResponseEntity<?> revoke(@PathVariable(name = "userId") Integer userId,
                                    @PathVariable(name = "taskId") Integer taskId) {
        try {
            userService.revokeTaskFromUser(taskId, userId);
        } catch (CRMProjectServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    Открепление всех задач пользователя. Возможно при изменении роли/позиции пользователя.
    Ухода пользователя в длительный отпуск
     */
    @DeleteMapping("/users/{userId}/tasks")
    @Secured({"ROLE_ADMIN", "ROLE_CONTROLLER"})
    public ResponseEntity<?> revokeAllUserTasksByUserId(@PathVariable(name = "userId") int userId) {
        try {
            userService.revokeAllUserTasksByUserId(userId);
        } catch (CRMProjectServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
