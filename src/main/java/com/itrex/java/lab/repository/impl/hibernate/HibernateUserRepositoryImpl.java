package com.itrex.java.lab.repository.impl.hibernate;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Primary
@Repository
public class HibernateUserRepositoryImpl implements UserRepository {

    private static final String LOGIN_USER = "login";
    private static final String PSW_USER = "psw";
    private static final String ROLE_USER = "role";
    private static final String FIRST_NAME_USER = "firstName";
    private static final String LAST_NAME_USER = "lastName";
    private static final String ID_USER = "id";
    private static final String ROLE_DEFAULT_USER = "roleDefault";

    private static final String SELECT_ALL = "from User u";
    private static final String UPDATE = "update User set login = :login, psw =:psw, " +
            "role_id =:role, firstName=:firstName, lastName=:lastName where id = :id";
    private static final String UPDATE_USER_ON_DEFAULT_ROLE = "update User u set role_id =:roleDefault where role_id =:role";


    private final SessionFactory sessionFactory;

    public HibernateUserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> selectAll() throws CRMProjectRepositoryException {

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(SELECT_ALL, User.class).list();
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL USERS: ", ex);
        }
    }

    @Override
    public User selectById(Integer id) throws CRMProjectRepositoryException {

        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT TASK BY ID: " + ex);
        }
    }

    @Override
    public List<Task> selectAllTasksByUser(User user) throws CRMProjectRepositoryException {

        try (Session session = sessionFactory.openSession()) {

            User userDB = session.get(User.class, user.getId());
            return new ArrayList<>(userDB.getTasks());
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL TASK FOR USER: ", ex);
        }
    }

    @Override
    public void printCrossTable() throws CRMProjectRepositoryException {
        try (Session session = sessionFactory.openSession()) {
            List<User> users = selectAll();
            for (User user : users) {
               List<Task> tasks = session.get(User.class, user.getId()).getTasks();
                tasks.forEach((task) -> System.out.printf("%d - %s : %d - %s\n", user.getId(), user.getLogin(), task.getId(), task.getTitle()));
            }
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL CROSS TABLE: ", ex);
        }
    }

    @Override
    public User add(User user) throws CRMProjectRepositoryException {

        try (Session session = sessionFactory.openSession()) {
            session.save(user);
            return user;
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO USER - " + user + ": ", ex);
        }
    }

    @Override
    public List<User> addAll(List<User> users) throws CRMProjectRepositoryException {

        try (Session session = sessionFactory.openSession()) {
            for (User user : users) {
                session.save(user);
            }
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO THESE USERS - " + users + ": ", ex);
        }
        return users;
    }

    @Override
    public void addTaskByUser(Task task, User user) throws CRMProjectRepositoryException {

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            User userBD = session.get(User.class, user.getId());
            List<Task> tasks = userBD.getTasks();
            tasks.add(task);
            userBD.setTasks(tasks);
            session.getTransaction().commit();
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO USER AND TASK IN CROSS TABLE - " + user + "\n" + task + ": " + ex);
        }
    }

    @Override
    public User update(User user, Integer id) throws CRMProjectRepositoryException {

        try (Session session = sessionFactory.openSession()) {
            try {
                session.getTransaction().begin();
                Query query = session.createQuery(UPDATE);
                query.setParameter(LOGIN_USER, user.getLogin());
                query.setParameter(PSW_USER, user.getPsw());
                query.setParameter(ROLE_USER, user.getRole());
                query.setParameter(FIRST_NAME_USER, user.getFirstName());
                query.setParameter(LAST_NAME_USER, user.getLastName());
                query.setParameter(ID_USER, id);
                query.executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new CRMProjectRepositoryException("ERROR: UPDATE TASK " + user, e);
            }
            return session.get(User.class, id);
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: UPDATE_USER - " + user + ": ", ex);
        }
    }

    @Override
    public List<User> updateRoleOnDefaultByUsers(Role role, Role defaultRole) throws CRMProjectRepositoryException {

        List<User> users = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            try {
                session.getTransaction().begin();
                int count = session.createQuery(UPDATE_USER_ON_DEFAULT_ROLE)
                        .setParameter(ROLE_DEFAULT_USER, defaultRole)
                        .setParameter(ROLE_USER, role)
                        .executeUpdate();
                for (int i = 1; i <= count; i++) {
                    users.add(session.get(User.class, i));
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new CRMProjectRepositoryException("ERROR: UPDATE User " + users, e);
            }
        }
        return users;
    }

    @Override
    public boolean remove(User user) throws CRMProjectRepositoryException {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.remove(user);
            session.getTransaction().commit();
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: REMOVE_USER - " + user + ": ", ex);
        }
        return true;
    }

    @Override
    public void removeAllTasksByUser(User user) throws CRMProjectRepositoryException {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();

            User userDB = session.get(User.class, user.getId());
            userDB.setTasks(new ArrayList<>());

            session.getTransaction().commit();
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: DELETE_ALL_TASKS_BY_USER - " + user + ": ", ex);
        }
    }

    @Override
    public boolean removeTaskByUser(Task task, User user) throws CRMProjectRepositoryException {
        try (Session session = sessionFactory.openSession()) {

            User userDB = session.get(User.class, user.getId());
            Task taskDB = session.get(Task.class, task.getId());

            if (userDB == null || taskDB == null) {
                return false;
            }

            List<Task> tasks = userDB.getTasks();

            if (tasks.contains(taskDB)) {
                tasks.remove(taskDB);
                return true;
            } else return false;
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: DELETE_TASK_BY_USER - " + user + ": ", ex);
        }
    }
}
