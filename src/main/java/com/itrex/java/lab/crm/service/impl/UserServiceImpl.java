package com.itrex.java.lab.crm.service.impl;

import com.itrex.java.lab.crm.dto.PasswordDTOForChanges;
import com.itrex.java.lab.crm.dto.UserDTO;
import com.itrex.java.lab.crm.entity.Role;
import com.itrex.java.lab.crm.entity.Status;
import com.itrex.java.lab.crm.entity.Task;
import com.itrex.java.lab.crm.entity.User;
import com.itrex.java.lab.crm.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.crm.repository.impl.data.RoleRepository;
import com.itrex.java.lab.crm.repository.impl.data.TaskRepository;
import com.itrex.java.lab.crm.repository.impl.data.UserRepository;
import com.itrex.java.lab.crm.service.UserService;
import com.itrex.java.lab.crm.utils.ConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.itrex.java.lab.crm.utils.ConverterUtils.convertUserToDto;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAll() {

        return userRepository
                .findAll()
                .stream()
                .map(ConverterUtils::convertUserToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getById(Integer id) {

        return userRepository
                .findById(id)
                .map(ConverterUtils::convertUserToDto)
                .orElse(null);
    }

    @Override
    public UserDTO getByLogin(String login) {

        return userRepository.findUserByLogin(login)
                .map(ConverterUtils::convertUserToDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllTaskUsersByTaskId(Integer taskId) throws CRMProjectServiceException {

        taskRepository.findById(taskId).orElseThrow(() -> new CRMProjectServiceException("TASK NO FOUND DATA BASE"));

        return userRepository.findUsersByTasks_id(taskId)
                .stream().map(ConverterUtils::convertUserToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllRoleUsersByRoleId(Integer roleId) throws CRMProjectServiceException {

        roleRepository.findById(roleId).orElseThrow(() -> new CRMProjectServiceException("ERROR SERVICE: NO ROLE FOUND WITH ID"));

        return userRepository.findUsersByRole_Id(roleId)
                .stream()
                .map(ConverterUtils::convertUserToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO add(UserDTO userDTO) throws CRMProjectServiceException {
        verification(userDTO);

        User user = User.builder()
                .login(userDTO.getLogin())
                .psw(userDTO.getPsw())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .role(roleRepository.findById(userDTO.getRoleId()).get())
                .build();
        return convertUserToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void assignTaskToUser(Integer taskId, Integer userId) throws CRMProjectServiceException {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new CRMProjectServiceException("TASK NO FOUND DATA BASE"));
        User user = userRepository.findById(userId).orElseThrow(() -> new CRMProjectServiceException("USER NO FOUND DATA BASE"));
        user.getTasks().add(task);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDTO update(UserDTO userDTO) throws CRMProjectServiceException {
        verification(userDTO);

        User user = userRepository.findById(userDTO.getId()).orElseThrow(() -> new CRMProjectServiceException("USER NO FOUND DATA BASE"));
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new CRMProjectServiceException("ERROR SERVICE: UPDATE USER: USER BY ID "
                        + userDTO.getId() + " NO FOUND DATA BASE"));

        user.setLogin(userDTO.getLogin());
        user.setLastName(userDTO.getLastName());
        user.setFirstName(userDTO.getFirstName());
        user.setRole(role);
        return convertUserToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDTO updateUserPassword(PasswordDTOForChanges passwordDTO, Integer userId) throws CRMProjectServiceException {

        if (!passwordDTO.getNewPassword().equals(passwordDTO.getRepeatNewPassword())) {
            throw new CRMProjectServiceException("ERROR SERVICE updateUserPassword: newPassword not equals repeatNewPassword ");
        }

        if (!verificationPassword(passwordDTO.getNewPassword())) {
            throw new CRMProjectServiceException("ERROR SERVICE updateUserPassword: newPassword is not valid");
        }

        if (!verificationPassword(passwordDTO.getOldPassword())) {
            throw new CRMProjectServiceException("ERROR SERVICE updateUserPassword: oldPassword is valid");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new CRMProjectServiceException("USER NO FOUND DATA BASE"));

        if (!user.getPsw().equals(passwordDTO.getOldPassword())) {
            throw new CRMProjectServiceException("ERROR SERVICE updateUserPassword: oldPassword is not equals password user by Id" + userId);
        }
        user.setPsw(passwordDTO.getNewPassword());
        return convertUserToDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public void remove(Integer userId) throws CRMProjectServiceException {

        User user = userRepository.findById(userId).orElseThrow(() -> new CRMProjectServiceException("USER NO FOUND DATA BASE"));
        userRepository.delete(user);
    }

    @Transactional
    @Override
    public void revokeTaskFromUser(Integer taskId, Integer userId) throws CRMProjectServiceException {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new CRMProjectServiceException("TASK NO FOUND DATA BASE"));

        User user = userRepository.findById(userId).orElseThrow(() -> new CRMProjectServiceException("USER NO FOUND DATA BASE"));

        List<User> users = task.getUsers();
        if (users.size() == 1) {
            if (users.get(0).getTasks().get(0).getId().equals(taskId))
                task.setStatus(Status.DONE);
        }

        user.getTasks().remove(task);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void revokeAllUserTasksByUserId(Integer userId) throws CRMProjectServiceException {

        User user = userRepository.findById(userId).orElseThrow(() -> new CRMProjectServiceException("USER NO FOUND DATA BASE"));
        user.setTasks(new ArrayList<>());
        userRepository.save(user);
    }

    private Boolean verificationPassword(String password) {
        return !(password.isBlank() || !password.matches("[a-zA-z0-9]{2,255}$"));
    }

    /*
    Проверка пользователя:
     -логин не пустой, длина от 5 до 50 символов(латинские буквы, цифры, нижнее подчеркивание),
     -пароль не пустой, длина от 2 до 255 символов(латинские буквы и цифры),
     -имя не пустое, длина от 2 до 50 символов(латинские буквы),
     -фамилия не пустая, длина от 2 до 50 символов(латинские буквы),
     -роль проверяется на уровне JSON parse.
     */
    private void verification(UserDTO user) throws CRMProjectServiceException {

        if (user.getLogin().isBlank() || !user.getLogin().matches("[a-zA-z0-9_]{5,50}$")) {
            throw new CRMProjectServiceException("No valid login");
        }
        if (!verificationPassword(user.getPsw())) {
            throw new CRMProjectServiceException("No valid password");
        }
        if (user.getFirstName().isBlank() || !user.getFirstName().matches("[a-zA-z]{2,50}$")) {
            throw new CRMProjectServiceException("No valid First Name");
        }
        if (user.getLastName().isBlank() || !user.getLastName().matches("[a-zA-z]{2,50}$")) {
            throw new CRMProjectServiceException("No valid Last Name");
        }
    }

}
