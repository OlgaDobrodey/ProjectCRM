package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.UserTask;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.repository.UserTaskRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCUserTaskRepositoryImpl implements UserTaskRepository {

    private static final String USER_COLUMN = "user_id";
    private static final String TASK_COLUMN = "task_id";
    private static final String INFO_COLUMN = "info";

    private static final String SELECT_ALL_QUERY = "SELECT * FROM crm.user_task";
    private static final String SELECT_USERTASK_BY_USER_AND_TASK = "SELECT * FROM crm.user_task WHERE user_id = %d AND task_id= %d";
    private static final String INSERT_USER_TASK_QUERY = "INSERT INTO crm.user_task(user_id, task_id, info) VALUES (?, ?, ?)";
    private static final String UPDATE_USER_TASK_QUERY = "UPDATE crm.user_task SET user_id=?, task_id=?, info=?  WHERE user_id = ? AND task_id=? ";
    private static final String DELETE_USER_TASK_QUERY = "DELETE FROM crm.user_task WHERE task_id=?";

    private DataSource dataSource;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    public JDBCUserTaskRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.userRepository = new JDBCUserRepositoryImpl(dataSource);
        this.taskRepository = new JDBCTaskRepositoryImpl(dataSource);
    }

    @Override
    public List<UserTask> selectAll() {
        List<UserTask> cross = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement();
             ResultSet resultSet = stm.executeQuery(SELECT_ALL_QUERY)) {

            while (resultSet.next()) {
                UserTask userTask = new UserTask();
                userTask = getUserTask(resultSet, userTask);
                cross.add(userTask);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cross;
    }

    @Override
    public UserTask selectByUserTask(User user, Task task) {
        UserTask userTask = new UserTask();
        String select = String.format(SELECT_USERTASK_BY_USER_AND_TASK, user.getId(), task.getId());
        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement();
             ResultSet resultSet = stm.executeQuery(select)) {
            if (resultSet.next()) {
                userTask = getUserTask(resultSet, userTask);
                if (resultSet.next()) {
                    throw new SQLIntegrityConstraintViolationException("Count userTasks more one");
                }
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return userTask;
    }


    @Override
    public UserTask add(UserTask userTask) {
        UserTask insertUT = new UserTask();
        try (Connection con = dataSource.getConnection()) {
            insertUT = insert(userTask, con);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return insertUT;
    }


    @Override
    public List<UserTask> addAll(List<UserTask> userTasks) {
        List<UserTask> addAllcrossTable = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            try {
                for (UserTask userTask : userTasks) {
                    addAllcrossTable.add(insert(userTask, con));
                }
                con.commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
                con.rollback();
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return addAllcrossTable;
    }


    @Override
    public UserTask update(Task task, User user, UserTask userTask) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_USER_TASK_QUERY)) {
            extracted(userTask, preparedStatement);

            preparedStatement.setInt(4, user.getId());
            preparedStatement.setInt(5, task.getId());

            preparedStatement.executeUpdate();
            userTask.setUser(userTask.getUser());
            userTask.setTask(userTask.getTask());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userTask;
    }

    @Override
    public boolean remove(Task task) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_USER_TASK_QUERY)) {
            preparedStatement.setInt(1, task.getId());

            if (preparedStatement.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private UserTask getUserTask(ResultSet resultSet, UserTask userTask) throws SQLException {

        userTask.setUser(userRepository.selectById(resultSet.getInt(USER_COLUMN)));
        userTask.setTask(taskRepository.selectById(resultSet.getInt(TASK_COLUMN)));
        userTask.setInfo(resultSet.getString(INFO_COLUMN));

        return userTask;
    }

    private UserTask insert(UserTask userTask, Connection con) throws SQLException {
        try (PreparedStatement preparedStatement = con.prepareStatement(INSERT_USER_TASK_QUERY)) {
            extracted(userTask, preparedStatement);
            preparedStatement.executeUpdate();
        }
        return userTask;
    }

    private void extracted(UserTask userTask, PreparedStatement preparedStatement) throws SQLException {

        preparedStatement.setInt(1, userTask.getUser().getId());
        preparedStatement.setInt(2, userTask.getTask().getId());
        preparedStatement.setString(3, userTask.getInfo());
    }
}
