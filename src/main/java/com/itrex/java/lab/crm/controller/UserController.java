package com.itrex.java.lab.crm.controller;

import com.itrex.java.lab.crm.dto.LoginUserDTO;
import com.itrex.java.lab.crm.dto.PasswordDTOForChanges;
import com.itrex.java.lab.crm.dto.TaskDTO;
import com.itrex.java.lab.crm.dto.UserDTO;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.crm.service.TaskService;
import com.itrex.java.lab.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController extends BaseController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    /*
    Посмотреть всех пользователей.
    Необходимо для осуществления информирования и контроля состояния бизнес-процессов
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> read() {
        List<UserDTO> users = new ArrayList<>();
        try {
            users = userService.getAll();
        } catch (CRMProjectServiceException e) {
            new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
   Посмотреть конкретного пользователя, необходимо для расшифровки конкретного пользователя, его роли.
   По запросу "/{id}/tasks" -можно посмотреть задачи в его выполнении
    */
    @GetMapping("/users/{id}")
    public ResponseEntity<?> readTaskByIdTask(@PathVariable Integer id) {
        UserDTO readed = null;
        try {
            readed = userService.getById(id);
        } catch (CRMProjectServiceException e) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return readed != null
                ? new ResponseEntity<>(readed, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
   Дополнительная функция, которая дает возможность просмотреть
   все задания данного пользователя
   */
    @GetMapping("/users/{id}/tasks")
    public ResponseEntity<List<TaskDTO>> readAllUserTasksByUserId(@PathVariable Integer id) {
        List<TaskDTO> tasks = new ArrayList<>();
        try {
            tasks = taskService.getAllUserTasksByUserId(id);
        } catch (CRMProjectServiceException e) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return tasks != null && !tasks.isEmpty()
                ? new ResponseEntity<>(tasks, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
    Создание нового пользователя
    */
    @PostMapping("/users")
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
        UserDTO createUser = null;
        try {
            createUser = userService.add(userDTO);
        } catch (CRMProjectServiceException e) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(createUser, HttpStatus.CREATED);
    }

    /*
    Вход в систему с помощью пароля и логина
     */
    @PostMapping("/profile")
    public ResponseEntity<?> signIn(Model model, @RequestBody LoginUserDTO user) {
        try {
            UserDTO verificationUser = userService.getByLogin(user.getLogin());
            if (verificationUser == null || (!verificationUser.getPsw().equals(user.getPsw()))) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            model.addAttribute("userActiv", verificationUser);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CRMProjectServiceException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    /*
   Корректировка задачи, Изменение статуса, внесение пояснений в инфо, корректировка названия
   возможное смещение сроков
    */
    @PutMapping(value = "/users/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") int id, @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        UserDTO updated = null;
        try {
            updated = userService.update(userDTO);
        } catch (CRMProjectServiceException e) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return updated != null
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /*
    Обновление пароля пользователя с данным id, проверека старого
     */
    @PutMapping("/users/{id}/updatePassword")
    public ResponseEntity<?> updateUserPassword(
            @PathVariable(name = "id") int id, @RequestBody PasswordDTOForChanges psw) {
        UserDTO updated = null;
        try {
            updated = userService.updateUserPassword(psw, id);
        } catch (CRMProjectServiceException e) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return updated != null
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /*
    Добавление задачи для пользователя
    */
    @PutMapping(value = "/users/{userId}/tasks/{taskId}")
    public ResponseEntity<?> assignTaskToUser(@PathVariable(name = "userId") int userId,
                                              @PathVariable(name = "taskId") int taskId) {
        try {
            userService.assignTaskToUser(taskId, userId);
        } catch (CRMProjectServiceException e) {
            new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    Удаление юзера из системы, используется при выбытии пользователя из всех бизнес процессах.
    При удалении пользователь удаляется со всех заданий, которые выполнял.
 */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        try {
            userService.remove(id);
        } catch (CRMProjectServiceException e) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    Открепление задачи от пользователя (задача выполнена или переопределиться на другого пользователя)
  */
    @DeleteMapping(value = "/users/{userId}/tasks/{taskId}")
    public ResponseEntity<?> revoke(@PathVariable(name = "userId") int userId,
                                    @PathVariable(name = "taskId") int taskId) {
        try {
            userService.revokeTaskFromUser(taskId, userId);
        } catch (CRMProjectServiceException e) {
            new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    Открепление всех задач пользователя. Возможно при изменении роли/позиции пользователя.
    Ухода пользователя в длительный отпуск
     */
    @DeleteMapping("/users/{userId}/tasks")
    public ResponseEntity<?> revokeAllUserTasksByUserId(@PathVariable(name = "userId") int userId) {
        try {
            userService.revokeAllUserTasksByUserId(userId);
        } catch (CRMProjectServiceException e) {
            new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
