package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.UserTask;
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
    private static final String INSERT_USER_TASK_QUERY = "INSERT INTO crm.user_task(user_id, task_id, info) VALUES (?, ?, ?)";
    private static final String UPDATE_USER_TASK_QUERY = "UPDATE crm.user_task SET user_id=?, task_id=?, info=?  WHERE user_id = ? AND task_id=? ";
    private static final String DELETE_USER_TASK_QUERY = "DELETE FROM crm.user_task WHERE task_id=?";

    private DataSource dataSource;

    public JDBCUserTaskRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
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
    public UserTask selectByTask(Task task) {
        return null;
    }

    @Override
    public UserTask add(UserTask userTask) {
        UserTask insertUT = new UserTask();
        try (Connection con = dataSource.getConnection()) {
            insertUT = insert(insertUT, con);
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
            userTask.setUser(user);
            userTask.setTask(task);
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
        userTask.setTask(resultSet.getObject(TASK_COLUMN, Task.class));
        userTask.setUser(resultSet.getObject(USER_COLUMN, User.class));
        userTask.setInfo(resultSet.getString(INFO_COLUMN));

        return userTask;
    }

    private UserTask insert(UserTask userTask, Connection con) throws SQLException {
        try (PreparedStatement preparedStatement = con.prepareStatement(INSERT_USER_TASK_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            extracted(userTask, preparedStatement);

            final int effectiveRows = preparedStatement.executeUpdate();

            if (effectiveRows == 1) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
//                        userTask.setId(generatedKeys.getInt(ID_USER_TASK_COLUMN));
                    }
                }
            }
        }
        return userTask;
    }

    private void extracted(UserTask userTask, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, userTask.getUser().getId());
        preparedStatement.setInt(2, userTask.getTask().getId());
        preparedStatement.setString(3, userTask.getInfo());
    }
}
