package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.repository.StatusRepository;
import com.itrex.java.lab.repository.TaskRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCTaskRepositoryImpl implements TaskRepository {

    private static final String ID_TASK_COLUMN = "id";
    private static final String TITLE_TASK_COLUMN = "title";
    private static final String STATUS_TASK_COLUMN = "status";
    private static final String DEDLINE_TASK_COLUMN = "dedline";
    private static final String INFO_TASK_COLUMN = "info";

    private static final String SELECT_ALL_QUERY = "SELECT * FROM crm.task";
    private static final String SELECT_TASK_BY_ID_QUERY = "SELECT * FROM crm.task WHERE id = ";
    private static final String INSERT_TASK_QUERY = "INSERT INTO crm.task(title, status, dedline, info) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_TASK_QUERY = "UPDATE crm.task SET title=?, status=?, dedline=?, info=?  WHERE id = ?";
    private static final String DELETE_TASK_QUERTY = "DELETE FROM crm.task WHERE id = ?";

    private DataSource dataSource;

    public JDBCTaskRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Task> selectAll() {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement();
             ResultSet resultSet = stm.executeQuery(SELECT_ALL_QUERY)) {

            while (resultSet.next()) {
                Task task = new Task();
                task = getTask(resultSet, task);
                tasks.add(task);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tasks;
    }

    @Override
    public Task selectById(Integer id) {
        Task task = new Task();
        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement();
             ResultSet resultSet = stm.executeQuery(SELECT_TASK_BY_ID_QUERY + id)) {
            if (resultSet.next()) {
                task = getTask(resultSet, task);
                if (resultSet.next()) {
                    throw new SQLIntegrityConstraintViolationException("Count tasks more one");
                }
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return task;
    }

    @Override
    public Task add(Task task) {
        Task insertTask = new Task();
        try (Connection con = dataSource.getConnection()) {
            insertTask = insert(task, con);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return insertTask;
    }

    @Override
    public List<Task> addAll(List<Task> tasks) {
        List<Task> addAllTask = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            try {
                for (Task task : tasks) {
                    addAllTask.add(insert(task, con));
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
        return addAllTask;
    }

    @Override
    public Task update(Task task, Integer id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_TASK_QUERY)) {
            extracted(task, preparedStatement);

            preparedStatement.setInt(5, id);

            preparedStatement.executeUpdate();
            task.setId(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return task;
    }

    @Override
    public boolean remove(Integer id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_TASK_QUERTY)) {
            preparedStatement.setInt(1, id);

            if (preparedStatement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private Task getTask(ResultSet resultSet, Task task) throws SQLException {

        task.setId(resultSet.getInt(ID_TASK_COLUMN));
        task.setTitle(resultSet.getString(TITLE_TASK_COLUMN));
        StatusRepository statusRepository = new JDBCStatusRepositoryImpl(dataSource);
        task.setStatus(statusRepository.selectById(resultSet.getInt(STATUS_TASK_COLUMN)));
        task.setDedline(resultSet.getDate(DEDLINE_TASK_COLUMN).toLocalDate());
        task.setInfo(resultSet.getString(INFO_TASK_COLUMN));

        return task;
    }

    private Task insert(Task task, Connection con) throws SQLException {
        try (PreparedStatement preparedStatement = con.prepareStatement(INSERT_TASK_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            extracted(task, preparedStatement);

            final int effectiveRows = preparedStatement.executeUpdate();

            if (effectiveRows == 1) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        task.setId(generatedKeys.getInt(ID_TASK_COLUMN));
                    }
                }
            }
        }
        return task;
    }

    private void extracted(Task task, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, task.getTitle());
        preparedStatement.setInt(2, task.getStatus().getId());
        preparedStatement.setDate(3, Date.valueOf(task.getDedline()));
        preparedStatement.setString(4, task.getInfo());
    }
}
