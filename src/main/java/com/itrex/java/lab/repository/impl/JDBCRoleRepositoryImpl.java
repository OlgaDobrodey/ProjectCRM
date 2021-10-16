package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.repository.RoleRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCRoleRepositoryImpl implements RoleRepository {

    private static final String ID_ROLE_COLUMN = "id";
    private static final String NAME_ROLE_COLUMN = "role_name";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM crm.role";
    private static final String SELECT_ROLE_BY_ID_QUERY = "SELECT * FROM crm.role WHERE id = ";
    private static final String INSERT_ROLE_QUERY = "INSERT INTO crm.role(role_name) VALUES (?)";
    private static final String UPDATE_ROLE_QUERY = "UPDATE crm.role SET role_name=? WHERE id = ?";
    private static final String DELETE_ROLE_QUERTY = "DELETE FROM crm.role WHERE id = ?";


    private final DataSource dataSource;

    public JDBCRoleRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Role> selectAll() {
        List<Role> roles = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement();
             ResultSet resultSet = stm.executeQuery(SELECT_ALL_QUERY)) {
            while (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getInt(ID_ROLE_COLUMN));
                role.setRoleName(resultSet.getString(NAME_ROLE_COLUMN));

                roles.add(role);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return roles;
    }

    @Override
    public Role selectById(Integer id) {
        Role role = new Role();
        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement();
             ResultSet resultSet = stm.executeQuery(SELECT_ROLE_BY_ID_QUERY + id)) {
            if (resultSet.next()) {
                role.setId(resultSet.getInt(ID_ROLE_COLUMN));
                role.setRoleName(resultSet.getString(NAME_ROLE_COLUMN));
                if (resultSet.next()) {
                    throw new SQLIntegrityConstraintViolationException("Count roles more one");
                }
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return role;
    }

    @Override
    public Role add(Role role) {
        Role insertRole = new Role();
        try (Connection con = dataSource.getConnection()) {
            insertRole = insert(role, con);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return insertRole;
    }

    @Override
    public List<Role> addAll(List<Role> roles) {
        List<Role> addAllRolles = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            try {
                for (Role role : roles) {
                    addAllRolles.add(insert(role, con));
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
        return addAllRolles;
    }

    @Override
    public Role update(Role role, Integer id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_ROLE_QUERY)) {
            preparedStatement.setString(1, role.getRoleName());
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
            role.setId(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return role;
    }

    @Override
    public boolean remove(Integer id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_ROLE_QUERTY)) {
            preparedStatement.setInt(1, id);

            int i = preparedStatement.executeUpdate();
            if(i==1) return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private Role insert(Role role, Connection con) throws SQLException {
        try (PreparedStatement preparedStatement = con.prepareStatement(INSERT_ROLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, role.getRoleName());
            role = setIdForChangeRole(preparedStatement, role);
        }
        return role;
    }

    private Role setIdForChangeRole(PreparedStatement preparedStatement, Role role) throws SQLException {
        final int effectiveRows = preparedStatement.executeUpdate();

        if (effectiveRows == 1) {
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    role.setId(generatedKeys.getInt(ID_ROLE_COLUMN));
                }
            }
        }
        return role;
    }
}
