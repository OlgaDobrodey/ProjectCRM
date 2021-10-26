package com.itrex.java.lab.repository.impl.hibernate;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class HibernateUserRepositoryImpl implements UserRepository {

    private static final String LOGIN_USER = "login";
    private static final String PSW_USER = "psw";
    private static final String ROLE_USER = "role";
    private static final String FIRST_NAME_USER = "firstName";
    private static final String LAST_NAME_USER = "lastName";
    private static final String ID_USER = "id";
    private static final String ROLE_DEFAULT_USER = "roleDefault";

    private static final String SELECT_ALL = "from User u";
    private static final String SELECT_CROSS_TABLE = "SELECT * FROM crm.user_task";
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
            List<User> users = session.createQuery(SELECT_ALL, User.class).list();
            return users;
        }
    }

    @Override
    public User selectById(Integer id) throws CRMProjectRepositoryException {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        }
    }

    @Override
    @Transactional
    public List<Task> selectAllTasksByUser(User user) throws CRMProjectRepositoryException {
        try(Session session = sessionFactory.openSession()){

            session.get(User.class, user.getId());
            List<Task> tasks = user.getTasks();
            System.out.println(tasks);

        }

        return null;
    }

    @Override
    public void printCrossTable() throws CRMProjectRepositoryException {
        try (Session session = sessionFactory.openSession()){
            List list = session.createSQLQuery(SELECT_CROSS_TABLE).list();
            System.out.println(list.toString());
        }


    }

    @Override
    public User add(User user) throws CRMProjectRepositoryException {
        try (Session session = sessionFactory.openSession()) {
            session.save(user);
            return user;
        }
    }

    @Override
    public List<User> addAll(List<User> users) throws CRMProjectRepositoryException {
        try (Session session = sessionFactory.openSession()) {
            for (User user : users) {
                session.save(user);
            }
        }
        return users;
    }

    @Override
    public void addTaskByUser(Task task, User user) throws CRMProjectRepositoryException {


    }

    @Override
    public User update(User user, Integer id) throws CRMProjectRepositoryException {
        User userUpdate = null;
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
            userUpdate = session.get(User.class, id);
        }
        return userUpdate;
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
        return null;
    }

    @Override
    public boolean remove(User user) throws CRMProjectRepositoryException {
        return false;
    }

    @Override
    public void removeAllTasksByUser(User user) throws CRMProjectRepositoryException {

    }

    @Override
    public boolean removeTaskByUser(Task task, User user) throws CRMProjectRepositoryException {
        return false;
    }
}
