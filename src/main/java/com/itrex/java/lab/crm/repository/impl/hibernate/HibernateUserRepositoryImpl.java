package com.itrex.java.lab.crm.repository.impl.hibernate;

import com.itrex.java.lab.crm.entity.User;
import com.itrex.java.lab.crm.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.crm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public List<User> selectAll() throws CRMProjectRepositoryException {

        try {
            return entityManager.createQuery(SELECT_ALL, User.class).getResultList();
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL USERS: ", ex);
        }
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public User selectById(Integer id) throws CRMProjectRepositoryException {

        try {
            return entityManager.find(User.class, id);
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT User BY ID: " + ex);
        }
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public User selectByLogin(String login) throws CRMProjectRepositoryException {
        User user = null;
        try {
            user = entityManager.createQuery(SELECT_USER_LOGIN, User.class)
                    .setParameter(LOGIN, login).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT User BY Login: " + ex);
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public List<User> selectAllUsersByTaskId(Integer taskId) throws CRMProjectRepositoryException {
        try {
            return entityManager.createQuery(SELECT_ALL_USERS_BY_TASK, User.class)
                    .setParameter(TASK_ID, taskId)
                    .getResultList();
        } catch (Exception e) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL USERS FOR TASK BY ID" + taskId + ": ", e);
        }
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public List<User> selectAllUsersByRoleId(Integer roleId) throws CRMProjectRepositoryException {
        try {
            return entityManager.createQuery(SELECT_ALL_USERS_BY_ROLE, User.class)
                    .setParameter(ROLE_ID, roleId)
                    .getResultList();
        } catch (Exception e) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL USERS FOR ROLE BY ID" + roleId + ": ", e);
        }
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public User add(User user) throws CRMProjectRepositoryException {

        try {
            entityManager.persist(user);
            return user;
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO USER - " + user + ": ", ex);
        }
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public User update(User user) throws CRMProjectRepositoryException {

        try {
            entityManager.merge(user);
            return entityManager.find(User.class, user.getId());
        } catch (Exception e) {
            throw new CRMProjectRepositoryException("ERROR: UPDATE TASK " + user, e);
        }
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public void remove(Integer userId) throws CRMProjectRepositoryException {
        try {
            entityManager.remove(entityManager.find(User.class, userId));
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: REMOVE_USER - " + userId + ": ", ex);
        }
    }

}
