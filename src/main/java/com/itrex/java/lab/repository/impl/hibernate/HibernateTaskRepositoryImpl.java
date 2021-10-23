package com.itrex.java.lab.repository.impl.hibernate;

import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.TaskRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import java.util.List;

public class HibernateTaskRepositoryImpl implements TaskRepository {

    private static final String TITLE_TASK = "title";
    private static final String STATUS_TASK = "status";
    private static final String DEADLINE_TASK = "deadline";
    private static final String INFO_TASK = "info";
    private static final String ID_TASK = "id";


    private static final String SELECT_ALL = "from Task t";
    private static final String UPDATE = "update Task set title = :title, status =:status, deadline =:deadline," +
            " info=:info where id = :id";

    private final SessionFactory sessionFactory;

    public HibernateTaskRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Task> selectAll() throws CRMProjectRepositoryException {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(SELECT_ALL, Task.class).list();
        }
    }


    @Override
    public Task selectById(Integer id) throws CRMProjectRepositoryException {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Task.class, id);
        }
    }

    @Override
    public Task add(Task task) throws CRMProjectRepositoryException {
        try (Session session = sessionFactory.openSession()) {
            session.save(task);
            return task;
        }
    }

    @Override
    public List<Task> addAll(List<Task> tasks) throws CRMProjectRepositoryException {
        try (Session session = sessionFactory.openSession()) {
            for (Task task : tasks) {
                session.save(task);
            }
        }
        return tasks;
    }

    @Override
    public Task update(Task task, Integer id) throws CRMProjectRepositoryException {
        Task roleUpdate = null;
        try (Session session = sessionFactory.openSession()) {
            try {
                session.getTransaction().begin();
                Query query = session.createQuery(UPDATE);
                query.setParameter(TITLE_TASK, task.getTitle());
                query.setParameter(STATUS_TASK, task.getStatus());
                query.setParameter(DEADLINE_TASK, task.getDeadline());
                query.setParameter(INFO_TASK, task.getInfo());
                query.setParameter(ID_TASK, id);
                query.executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new CRMProjectRepositoryException("ERROR: UPDATE TASK " + task, e);
            }
            roleUpdate = session.get(Task.class, id);
        }
        return roleUpdate;
    }

    @Override
    public boolean remove(Task task) throws CRMProjectRepositoryException {
        return false;
    }

    @Override
    public List<User> selectAllUsersByTask(Task task) throws CRMProjectRepositoryException {
        return null;
    }

    @Override
    public void addUserByTask(Task task, User user) throws CRMProjectRepositoryException {

    }

    @Override
    public boolean removeUserByTask(Task task, User user) throws CRMProjectRepositoryException {
        return false;
    }

    @Override
    public void removeAllUsersByTask(Task task) throws CRMProjectRepositoryException {

    }
}
