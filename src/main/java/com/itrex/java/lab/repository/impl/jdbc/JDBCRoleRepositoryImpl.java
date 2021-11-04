package com.itrex.java.lab.repository.impl.jdbc;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Qualifier("JDBCRoleRepository")
public class JDBCRoleRepositoryImpl implements RoleRepository {

    private static final int DEFAULT_ROLE = 2; //User
    private static final String ID_ROLE_COLUMN = "id";
    private static final String NAME_ROLE_COLUMN = "role_name";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM crm.role";
    private static final String SELECT_ROLE_BY_ID_QUERY = "SELECT * FROM crm.role WHERE id = ";
    private static final String INSERT_ROLE_QUERY = "INSERT INTO crm.role(role_name) VALUES (?)";
    private static final String UPDATE_ROLE_QUERY = "UPDATE crm.role SET role_name = ? WHERE id = ?";
    private static final String DELETE_ROLE_QUERTY = "DELETE FROM crm.role WHERE id = ?";

    private final DataSource dataSource;
    private UserRepository userRepository;

    public JDBCRoleRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Role> selectAll() throws CRMProjectRepositoryException {
        List<Role> roles = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement();
             ResultSet resultSet = stm.executeQuery(SELECT_ALL_QUERY)) {
            while (resultSet.next()) {
                Role role = getRole(resultSet);
                roles.add(role);
            }
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL ROLES: ", ex);
        }
        return roles;
    }

    @Override
    public Role selectById(Integer id) throws CRMProjectRepositoryException {
        Role role = null;
        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement();
             ResultSet resultSet = stm.executeQuery(SELECT_ROLE_BY_ID_QUERY + id)) {
            if (resultSet.next()) {
                role = getRole(resultSet);
                if (resultSet.next()) {
                    throw new SQLIntegrityConstraintViolationException("ERROR: SELECT ROLE BY ID: Count roles more one");
                }
            }
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ROLE BY ID: ", ex);
        }
        return role;
    }

    @Override
    public Role add(Role role) throws CRMProjectRepositoryException {
        List<Role> roles = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(INSERT_ROLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            roles.add(role);
            insert(roles, preparedStatement);
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO ROLE - " + role.getRoleName() + ": ", ex);
        }
        return roles.get(0);
    }

    @Override
    public List<Role> addAll(List<Role> roles) throws CRMProjectRepositoryException {
        StringBuilder insertBuild = new StringBuilder(INSERT_ROLE_QUERY);
        for (int i = 1; i < roles.size(); i++) {
            insertBuild.append(", ").append("(?)");
        }
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(insertBuild.toString(), Statement.RETURN_GENERATED_KEYS)) {
            insert(roles, preparedStatement);
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO THESE ROLES - " + roles + ": ", ex);
        }
        return roles;
    }

    @Override
    public Role update(Role role, Integer id) throws CRMProjectRepositoryException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_ROLE_QUERY)) {

            preparedStatement.setString(1, role.getRoleName());
            preparedStatement.setInt(2, id);
            if (preparedStatement.executeUpdate() > 0) {
                role.setId(id);
            } else role = null;
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: UPDATE_ROLE - " + role, ex);
        }
        return role;
    }

    @Override
    public boolean remove(Role role) throws CRMProjectRepositoryException {
        return remove(role, selectById(DEFAULT_ROLE));
    }

    @Override
    public boolean remove(Role role, Role defaultRole) throws CRMProjectRepositoryException {
        if (role.equals(defaultRole)) {
            throw new CRMProjectRepositoryException("ERROR : Role equals default role");
        }
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try {
                userRepository = new JDBCUserRepositoryImpl(dataSource);
                userRepository.updateRoleOnDefaultByUsers(role, defaultRole);
                try (PreparedStatement preparedStatement = conn.prepareStatement(DELETE_ROLE_QUERTY)) {
                    preparedStatement.setInt(1, role.getId());
                    int a = preparedStatement.executeUpdate();
                    if (a == 1) {
                        return true;
                    }
                }
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw new CRMProjectRepositoryException("TRANSACTION ROLLBACK: ", ex);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: REMOVE_ROLE - " + role + ": ", ex);
        }
        return false;
    }

    private void insert(List<Role> roles, PreparedStatement preparedStatement) throws SQLException {
        for (int i = 1; i <= roles.size(); i++) {
            preparedStatement.setString(i, roles.get(i - 1).getRoleName());
        }
        int effectiveRows = preparedStatement.executeUpdate();
        if (effectiveRows == roles.size()) {
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                for (int i = 0; i < effectiveRows; i++) {
                    if (generatedKeys.next()) {
                        roles.get(i).setId(generatedKeys.getInt(ID_ROLE_COLUMN));
                    }
                }
            }
        }
    }

    private Role getRole(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getInt(ID_ROLE_COLUMN));
        role.setRoleName(resultSet.getString(NAME_ROLE_COLUMN));
        return role;
    }
}
