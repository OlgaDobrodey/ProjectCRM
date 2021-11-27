package com.itrex.java.lab.crm.repository.impl.hibernate;

import com.itrex.java.lab.crm.entity.User;
import com.itrex.java.lab.crm.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.crm.repository.UserRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Deprecated
@Repository
@Qualifier("HibernateUserRepository")
public class HibernateUserRepositoryImpl implements UserRepository {

    private static final String TASK_ID = "taskId";
    private static final String ROLE_ID = "roleId";
    private static final String LOGIN = "login";

    private static final String SELECT_ALL = "select u from User u";
    private static final String SELECT_USER_LOGIN = "select u from User u where u.login =:login";
    private static final String SELECT_ALL_USERS_BY_TASK = "select u from Task t join t.users u where t.id =:taskId";
    private static final String SELECT_ALL_USERS_BY_ROLE = "select u from Role r join r.users u where r.id =:roleId";

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<User> selectAll() throws CRMProjectRepositoryException {

        return entityManager.createQuery(SELECT_ALL, User.class).getResultList();
    }

    @Override
    public User selectById(Integer id) throws CRMProjectRepositoryException {

        return entityManager.find(User.class, id);
    }

    @Override

    public User selectByLogin(String login) {
        User user;
        try {
            user = entityManager.createQuery(SELECT_USER_LOGIN, User.class)
                    .setParameter(LOGIN, login).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return user;
    }

    @Override
    public List<User> selectAllUsersByTaskId(Integer taskId) {

        return entityManager.createQuery(SELECT_ALL_USERS_BY_TASK, User.class)
                .setParameter(TASK_ID, taskId)
                .getResultList();
    }

    @Override
    public List<User> selectAllUsersByRoleId(Integer roleId) throws CRMProjectRepositoryException {

        return entityManager.createQuery(SELECT_ALL_USERS_BY_ROLE, User.class)
                .setParameter(ROLE_ID, roleId)
                .getResultList();
    }

    @Override
    public User add(User user) throws CRMProjectRepositoryException {

        Session session = entityManager.unwrap(Session.class);
        Integer userId = (Integer) session.save(user);

        return session.get(User.class, userId);
    }

    @Override
    public User update(User user) throws CRMProjectRepositoryException {

        entityManager.merge(user);
        return entityManager.find(User.class, user.getId());
    }

    @Override
    public void remove(Integer userId) throws CRMProjectRepositoryException {
        try {
            entityManager.remove(entityManager.find(User.class, userId));
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: REMOVE_USER - " + userId + ": ", ex);
        }
    }

}
