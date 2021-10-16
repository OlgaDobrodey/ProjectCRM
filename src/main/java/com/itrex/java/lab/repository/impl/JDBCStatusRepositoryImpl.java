package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Status;
import com.itrex.java.lab.repository.StatusRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCStatusRepositoryImpl implements StatusRepository {

    private static final String ID_STATUS_COLUMN = "id";
    private static final String NAME_STATUS_COLUMN = "status_name";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM crm.status";
    private static final String SELECT_STATUS_BY_ID_QUERY = "SELECT * FROM crm.status WHERE id = ";
    private static final String INSERT_STATUS_QUERY = "INSERT INTO crm.status(status_name) VALUES (?)";
    private static final String UPDATE_STATUS_QUERY = "UPDATE crm.status SET status_name = ? WHERE id = ?";
    private static final String DELETE_STATUS_QUERTY = "DELETE FROM crm.status WHERE id = ?";

    private final DataSource dataSource;

    public JDBCStatusRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Status> selectAll() {
        List<Status> statuses = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement();
             ResultSet resultSet = stm.executeQuery(SELECT_ALL_QUERY)) {
            while (resultSet.next()) {
                Status status = new Status();
                status.setId(resultSet.getInt(ID_STATUS_COLUMN));
                status.setStatusName(resultSet.getString(NAME_STATUS_COLUMN));

                statuses.add(status);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return statuses;
    }

    @Override
    public Status selectById(Integer id) {
        Status status = new Status();
        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement();
             ResultSet resultSet = stm.executeQuery(SELECT_STATUS_BY_ID_QUERY + id)) {
            if (resultSet.next()) {
                status.setId(id);
                status.setStatusName(resultSet.getString(NAME_STATUS_COLUMN));
                if (resultSet.next()) {
                    throw new SQLIntegrityConstraintViolationException("Count roles more one");
                }
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    @Override
    public Status add(Status status) {
        Status insertStatus = new Status();
        try (Connection con = dataSource.getConnection()) {
            insertStatus = insert(status, con);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return insertStatus;
    }

    @Override
    public List<Status> addAll(List<Status> statuses) {
        List<Status> addAllStatus = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            try {
                for (Status status : statuses) {
                    addAllStatus.add(insert(status, con));
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
        return addAllStatus;
    }

    @Override
    public Status update(Status status, Integer id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_STATUS_QUERY)) {
            preparedStatement.setString(1, status.getStatusName());
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
            status.setId(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return status;
    }

    @Override
    public boolean remove(Integer id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_STATUS_QUERTY)) {
            preparedStatement.setInt(1, id);

            if (preparedStatement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private Status insert(Status status, Connection con) throws SQLException {
        try (PreparedStatement preparedStatement = con.prepareStatement(INSERT_STATUS_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, status.getStatusName());
            status = setIdForChangeRole(preparedStatement, status);
        }
        return status;
    }

    private Status setIdForChangeRole(PreparedStatement preparedStatement, Status status) throws SQLException {
        final int effectiveRows = preparedStatement.executeUpdate();

        if (effectiveRows == 1) {
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    status.setId(generatedKeys.getInt(ID_STATUS_COLUMN));
                }
            }
        }
        return status;
    }
}
