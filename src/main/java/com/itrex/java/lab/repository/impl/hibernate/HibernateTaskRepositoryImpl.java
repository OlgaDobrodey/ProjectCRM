package com.itrex.java.lab.repository.impl.hibernate;

import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.TaskRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
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

    @Autowired
    public HibernateTaskRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public List<Task> selectAll() throws CRMProjectRepositoryException {

        try {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery(SELECT_ALL, Task.class).list();
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL TASK: " + ex);
        }
    }


    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public Task selectById(Integer id) throws CRMProjectRepositoryException {

        try {
            Session session = sessionFactory.getCurrentSession();
            return session.get(Task.class, id);
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT TASK BY ID: " + ex);
        }
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public Task add(Task task) throws CRMProjectRepositoryException {

        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(task);
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO TASK- " + task + ": ", ex);
        }
        return task;
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public List<Task> addAll(List<Task> tasks) throws CRMProjectRepositoryException {

        try {
            Session session = sessionFactory.getCurrentSession();
            for (Task task : tasks) {
                session.save(task);
            }
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO THESE USERS - " + tasks + ": ", ex);
        }
        return tasks;
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public Task update(Task task) throws CRMProjectRepositoryException {

        try {
            Session session = sessionFactory.getCurrentSession();

            Query query = session.createQuery(UPDATE);
            query.setParameter(TITLE_TASK, task.getTitle());
            query.setParameter(STATUS_TASK, task.getStatus());
            query.setParameter(DEADLINE_TASK, task.getDeadline());
            query.setParameter(INFO_TASK, task.getInfo());
            query.setParameter(ID_TASK, task.getId());
            query.executeUpdate();

            return Optional.ofNullable(session.get(Task.class, task.getId()))
                    .orElseThrow(() -> new CRMProjectRepositoryException("TASK NO FOUND DATA BASE"));
        } catch (Exception e) {
            throw new CRMProjectRepositoryException("ERROR: UPDATE TASK " + task, e);
        }
    }


    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public void remove(Integer idTask) throws CRMProjectRepositoryException {

        try {
            Session session = sessionFactory.getCurrentSession();

            Task task = session.get(Task.class, idTask);
            session.delete(task);
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: REMOVE_TASK - " + idTask + ": ", ex);
        }
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public List<User> selectAllUsersByTask(Integer idTask) throws CRMProjectRepositoryException {

        try {
            Session session = sessionFactory.getCurrentSession();

            return new ArrayList<>(session.get(Task.class, idTask).getUsers());
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL USERS FOR TASK: ", ex);
        }
    }
}
