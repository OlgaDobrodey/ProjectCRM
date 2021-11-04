package com.itrex.java.lab.repository.impl.jdbc;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.repository.TaskRepository;
import com.itrex.java.lab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Qualifier("JDBCUserRepository")
public class JDBCUserRepositoryImpl implements UserRepository {

    private static final String ID_USER_COLUMN = "id";
    private static final String LOGIN_USER_COLUMN = "login";
    private static final String PSW_USER_COLUMN = "psw";
    private static final String ROLE_USER_COLUMN = "role_id";
    private static final String FIRST_NAME_USER_COLUMN = "first_name";
    private static final String LAST_NAME_USER_COLUMN = "last_name";
    private static final String CROSS_TABLE_ID_USER = "users_id";
    private static final String CROSS_TABLE_ID_TASK = "tasks_id";

    private static final String SELECT_ALL_QUERY = "SELECT * FROM crm.user";
    private static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM crm.user WHERE id = ";
    private static final String SELECT_CROSS_TABLE = "SELECT * FROM crm.user_task";
    private static final String SELECT_ALL_TASKS_FOR_USER = "SELECT tasks_id FROM crm.user_task WHERE users_id = ";
    private static final String INSERT_TASK_FOR_USER = "INSERT INTO crm.user_task(users_id, tasks_id) VALUES (?, ?)";
    private static final String INSERT_USER_QUERY = "INSERT INTO crm.user(login, psw, role_id, first_name, last_name) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE crm.user SET login=?, psw=?, role_id=?, first_name=?, last_name=?  WHERE id = ?";
    private static final String UPDATE_USER_ON_DEFAULT_ROLE_QUERY = "UPDATE crm.user SET role_id = ? WHERE role_id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM crm.user WHERE id = ?";
    private static final String DELETE_USER_ALL_TASKS_QUERY = "DELETE FROM crm.user_task WHERE users_id = ?";
    private static final String DELETE_TASK_BY_USER = "DELETE FROM crm.user_task WHERE users_id= ? AND tasks_id=?";

    private DataSource dataSource;
    private RoleRepository roleRepository;

    public JDBCUserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> selectAll() throws CRMProjectRepositoryException {
        List<User> users = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement();
             ResultSet resultSet = stm.executeQuery(SELECT_ALL_QUERY)) {

            while (resultSet.next()) {
                User user = getUser(resultSet);
                users.add(user);
            }
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL USERS: " , ex);
        }
        return users;
    }

    @Override
    public User selectById(Integer id) throws CRMProjectRepositoryException {
        User user = null;
        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement();
             ResultSet resultSet = stm.executeQuery(SELECT_USER_BY_ID_QUERY + id)) {
            if (resultSet.next()) {
                user = getUser(resultSet);
                if (resultSet.next()) {
                    throw new SQLIntegrityConstraintViolationException("Count users more one");
                }
            }
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT USER BY ID: " , ex);
        }
        return user;
    }

    @Override
    public List<Task> selectAllTasksByUser(User user) throws CRMProjectRepositoryException {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement();
             ResultSet resultSet = stm.executeQuery(SELECT_ALL_TASKS_FOR_USER + user.getId())) {
            TaskRepository taskRepository = new JDBCTaskRepositoryImpl(dataSource);
            while (resultSet.next()) {
                Task task = taskRepository.selectById(resultSet.getInt(CROSS_TABLE_ID_TASK));
                tasks.add(task);
            }
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL TASK FOR USER: " , ex);
        }
        return tasks;
    }

    @Override
    public void printCrossTable() throws CRMProjectRepositoryException {
        try (Connection conn = dataSource.getConnection();
             Statement stm = conn.createStatement();
             ResultSet resultSet = stm.executeQuery(SELECT_CROSS_TABLE)) {
            System.out.println(CROSS_TABLE_ID_USER + " : " + CROSS_TABLE_ID_TASK);
            while (resultSet.next()) {
                System.out.printf("%7d:%-7d\n", resultSet.getInt(CROSS_TABLE_ID_USER), resultSet.getInt(CROSS_TABLE_ID_TASK));
            }
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL CROSS TABLE: " , ex);
        }
    }

    @Override
    public User add(User user) throws CRMProjectRepositoryException {
        List<User> users = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(INSERT_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            users.add(user);
            insert(users, preparedStatement);
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO USER - " + user + ": ", ex);
        }
        return users.get(0);
    }

    @Override
    public List<User> addAll(List<User> users) throws CRMProjectRepositoryException {
        StringBuilder insertBuild = new StringBuilder(INSERT_USER_QUERY);
        for (int i = 1; i < users.size(); i++) {
            insertBuild.append(", ").append("(?, ?, ?, ?, ?)");
        }
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(insertBuild.toString(), Statement.RETURN_GENERATED_KEYS)) {
            insert(users, preparedStatement);
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO THESE USERS - " + users + ": " , ex);
        }
        return users;
    }

    @Override
    public void addTaskByUser(Task task, User user) throws CRMProjectRepositoryException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(INSERT_TASK_FOR_USER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, task.getId());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            while (generatedKeys.next()) {
                generatedKeys.getInt(CROSS_TABLE_ID_USER);

                generatedKeys.getInt(CROSS_TABLE_ID_TASK);
            }
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO USER AND TASK IN CROSS TABLE - " + user + "\n" + task + ": " + ex);
        }
    }

    @Override
    public User update(User user, Integer id) throws CRMProjectRepositoryException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_USER_QUERY)) {
            extracted(0, user, preparedStatement);

            preparedStatement.setInt(6, id);

            if (preparedStatement.executeUpdate() > 0) {
                user.setId(id);
            }
            else user = null;
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: UPDATE_USER - " + user + ": " , ex);
        }
        return user;
    }

    @Override
    public List<User> updateRoleOnDefaultByUsers(Role role, Role defaulRole) throws CRMProjectRepositoryException {
        List<User> users = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_USER_ON_DEFAULT_ROLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, defaulRole.getId());
            preparedStatement.setInt(2, role.getId());

            int executeUpdate = preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys();) {
                for (int i = 0; i < executeUpdate; i++) {
                    if (generatedKeys.next()) {
                        users.add(selectById(generatedKeys.getInt(ID_USER_COLUMN)));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: UPDATE_USERS_ON_ROLE - " + role + ": ", ex);
        }
        return users;
    }

    @Override
    public boolean remove(User user) throws CRMProjectRepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try {
                removeAllTasksByUser(user);
                try (PreparedStatement preparedStatement = conn.prepareStatement(DELETE_USER_QUERY)) {
                    preparedStatement.setInt(1, user.getId());
                    int a = preparedStatement.executeUpdate();
                    if (a == 1) {
                        return true;
                    }
                }
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw new SQLException("TRANSACTION ROLLBACK: " + ex);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: REMOVE_USER - " + user + ": " , ex);
        }
        return false;
    }

    @Override
    public boolean removeTaskByUser(Task task, User user) throws CRMProjectRepositoryException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_TASK_BY_USER)) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, task.getId());
            int executeUpdate = preparedStatement.executeUpdate();
            if (executeUpdate == 1) {
                return true;
            }
            return false;
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: DELETE_TASK_BY_USER - " + user + ": " , ex);
        }
    }

    @Override
    public void removeAllTasksByUser(User user) throws CRMProjectRepositoryException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_USER_ALL_TASKS_QUERY)) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new CRMProjectRepositoryException("ERROR: DELETE_ALL_TASKS_BY_USER - " + user + ": " , ex);
        }
    }

    private User getUser(ResultSet resultSet) throws SQLException,CRMProjectRepositoryException {

        User user = new User();
        user.setId(resultSet.getInt(ID_USER_COLUMN));
        user.setLogin(resultSet.getString(LOGIN_USER_COLUMN));
        user.setPsw(resultSet.getString(PSW_USER_COLUMN));
        roleRepository = new JDBCRoleRepositoryImpl(dataSource);
        user.setRole(roleRepository.selectById(resultSet.getInt(ROLE_USER_COLUMN)));
        user.setFirstName(resultSet.getString(FIRST_NAME_USER_COLUMN));
        user.setLastName(resultSet.getString(LAST_NAME_USER_COLUMN));

        return user;
    }

    private void insert(List<User> users, PreparedStatement preparedStatement) throws SQLException {
        for (int i = 0; i < users.size(); i++) {
            extracted(i, users.get(i), preparedStatement);         //
        }
        int effectiveRows = preparedStatement.executeUpdate();
        if (effectiveRows == users.size()) {
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                for (int i = 0; i < effectiveRows; i++) {
                    if (generatedKeys.next()) {
                        users.get(i).setId(generatedKeys.getInt(ID_USER_COLUMN));
                    }
                }
            }
        }
    }

    /**
     * @param counter           - counter, determines which element is added counting from 0
     * @param user              - user
     * @param preparedStatement
     * @throws SQLException
     */
    private void extracted(int counter, User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1 + 5 * counter, user.getLogin());
        preparedStatement.setString(2 + 5 * counter, user.getPsw());
        preparedStatement.setInt(3 + 5 * counter, user.getRole().getId());
        preparedStatement.setString(4 + 5 * counter, user.getFirstName());
        preparedStatement.setString(5 + 5 * counter, user.getLastName());
    }
}
