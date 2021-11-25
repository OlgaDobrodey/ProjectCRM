package com.itrex.java.lab.crm.controller;

import com.itrex.java.lab.crm.dto.RoleDTO;
import com.itrex.java.lab.crm.dto.UserDTO;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.crm.service.RoleService;
import com.itrex.java.lab.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    /*
    Посмотреть все существующие роли.
    Могут посмотреть все пользователи
    */
    @GetMapping("/roles")
    public ResponseEntity<List<RoleDTO>> read() {

        List<RoleDTO> tasks = roleService.getAllRoles();

        return tasks != null && !tasks.isEmpty()
                ? new ResponseEntity<>(tasks, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
    Просмотреть данную роль под id
    */
    @GetMapping("/roles/{id}")
    public ResponseEntity<?> readTaskByIdTask(@PathVariable Integer id) {

        RoleDTO readRole = roleService.getById(id);

        return readRole != null
                ? new ResponseEntity<>(readRole, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
    Посмотреть конкретную роль и всех пользователей данной роли.
     */
    @GetMapping("/roles/{id}/users")
    public ResponseEntity<?> getAllRoleUsersByRoleId(@PathVariable Integer id) {
        List<UserDTO> users = new ArrayList<>();
        try {
            users = userService.getAllRoleUsersByRoleId(id);
        } catch (CRMProjectServiceException e) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
   Создание новой роли, может выполнять только Admin
   Применяется при введении нового вида пользователя,
   пример может быть в будущем добавлен клиент(как наблюдатель выполнения задачи)
    */
    @PostMapping("/roles")
    public ResponseEntity<?> create(@RequestBody RoleDTO roleDTO) {

        RoleDTO created = roleService.addRole(roleDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /*
     Корректировка роли, в связи изменившимися бизнес-процессами. Изменения названия
     */
    @PutMapping(value = "/roles/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") int id, @RequestBody RoleDTO roleDTO) {
        roleDTO.setId(id);
        RoleDTO updated = null;
        try {
            updated = roleService.updateRole(roleDTO);
        } catch (CRMProjectServiceException e) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return updated != null
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

}
