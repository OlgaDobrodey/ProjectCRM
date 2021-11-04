package com.itrex.java.lab.workMethodschek;

import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class UserLookWorkMethods {

    public static void chek(ApplicationContext ctx) {
        UserRepository userRepository = ctx.getBean(UserRepository.class);
        RoleRepository roleRepository = ctx.getBean(RoleRepository.class);
        TaskRepository taskRepository = ctx.getBean(TaskRepository.class);

        try {
            System.out.println("SELECT ID " + userRepository.selectById(1));
            User user = userRepository.selectById(2);

            List<Task> yask = userRepository.selectAllTasksByUser(user);
            System.out.println("1selectAllTasksByUser" + yask);
            userRepository.addTaskByUser(taskRepository.selectById(6), user);
            System.out.println("2selectAllTasksByUser" + userRepository.selectAllTasksByUser(user));

            List<User> users = UtillCategory.createTestUsers(4);
            System.out.println("ADD TASK " + userRepository.add(users.get(0)));
            System.out.println("ADD TASKS " + userRepository.addAll(List.of(users.get(1), users.get(2))));

            System.out.println("UPDATE " + userRepository.update(users.get(3), 4));

            System.out.println("SELECT ALL " + userRepository.selectAll());

            System.out.println("UPDATE ROLE " + userRepository.updateRoleOnDefaultByUsers(roleRepository.selectById(1), roleRepository.selectById(2)));
            System.out.println("SELECT ALL " + userRepository.selectAll());

            System.out.println("REMOVE USER" + userRepository.remove(user));

            System.out.println("SELECT ALL" + userRepository.selectAll());
            System.out.println("TASK $=4" + taskRepository.selectAllUsersByTask(taskRepository.selectById(4)));
            System.out.println("TASK $=5" + taskRepository.selectAllUsersByTask(taskRepository.selectById(5)));
            System.out.println("TASK $=11" + taskRepository.selectAllUsersByTask(taskRepository.selectById(11)));
            userRepository.addTaskByUser(taskRepository.selectById(8), user);
            System.out.println("REMOVE TASK AND USER" + userRepository.removeTaskByUser(taskRepository.selectById(4), user));
            userRepository.printCrossTable();
            System.out.println("ALL TASK" + userRepository.selectAllTasksByUser(user));
        } catch (CRMProjectRepositoryException e) {
            e.printStackTrace();
        }
    }
}

