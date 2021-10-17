package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JDBCUserRepositoryImpl implements UserRepository {

    private static final String ID_USER_COLUMN = "id";
    private static final String LOGIN_USER_COLUMN = "login";
    private static final String PSW_USER_COLUMN = "psw";
    private static final String ROLE_USER_COLUMN = "role";
    private static final String FIRST_NAME_USER_COLUMN = "first_name";
    private static final String LAST_NAME_USER_COLUMN = "last_name";

    private static final String SELECT_ALL_QUERY = "SELECT * FROM crm.user";
    private static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM crm.user WHERE id = ";
    private static final String INSERT_USER_QUERY = "INSERT INTO crm.user(login, psw, role, first_name, last_name) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE crm.user SET login=?, psw=?, role=?, first_name=?, last_name=?  WHERE id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM crm.user WHERE id = ?";

    private DataSource dataSource;

    public JDBCUserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> selectAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement();
             ResultSet resultSet = stm.executeQuery(SELECT_ALL_QUERY)) {

            while (resultSet.next()) {
                User user = new User();
                user = getUser(resultSet, user);
                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    @Override
    public User selectById(Integer id) {
        User user = new User();
        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement();
             ResultSet resultSet = stm.executeQuery(SELECT_USER_BY_ID_QUERY + id)) {
            if (resultSet.next()) {
                user = getUser(resultSet, user);
                if (resultSet.next()) {
                    throw new SQLIntegrityConstraintViolationException("Count users more one");
                }
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    @Override
    public User add(User user) {
        User insertUser = new User();
        try (Connection con = dataSource.getConnection()) {
            insertUser = insert(user, con);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return insertUser;
    }

    @Override
    public List<User> addAll(List<User> users) {
        List<User> addAllUser = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            try {
                for (User user : users) {
                    addAllUser.add(insert(user, con));
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
        return addAllUser;
    }

    @Override
    public User update(User user, Integer id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_USER_QUERY)) {
            extracted(user, preparedStatement);

            preparedStatement.setInt(6, id);

            preparedStatement.executeUpdate();
            user.setId(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean remove(Integer id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_USER_QUERY)) {
            preparedStatement.setInt(1, id);

            if (preparedStatement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private User getUser(ResultSet resultSet, User user) throws SQLException {

        user.setId(resultSet.getInt(ID_USER_COLUMN));
        user.setLogin(resultSet.getString(LOGIN_USER_COLUMN));
        user.setPsw(resultSet.getInt(PSW_USER_COLUMN));
        RoleRepository roleRepository = new JDBCRoleRepositoryImpl(dataSource);
        user.setRole(roleRepository.selectById(resultSet.getInt(ROLE_USER_COLUMN)));
        user.setFirstName(resultSet.getString(FIRST_NAME_USER_COLUMN));
        user.setLastName(resultSet.getString(LAST_NAME_USER_COLUMN));

        return user;
    }

    private User insert(User user, Connection con) throws SQLException {
        try (PreparedStatement preparedStatement = con.prepareStatement(INSERT_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            extracted(user, preparedStatement);

            final int effectiveRows = preparedStatement.executeUpdate();

            if (effectiveRows == 1) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(ID_USER_COLUMN));
                    }
                }
            }
        }
        return user;
    }

    private void extracted(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setInt(2, user.getPsw());
        preparedStatement.setInt(3, user.getRole().getId());
        preparedStatement.setString(4, user.getFirstName());
        preparedStatement.setString(5, user.getLastName());
    }
}
