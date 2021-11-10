package com.itrex.java.lab.repository.impl.hibernate;

import com.itrex.java.lab.entity.Task;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.TaskRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

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

    public HibernateTaskRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Task> selectAll() throws CRMProjectRepositoryException {

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(SELECT_ALL, Task.class).list();
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL TASK: " + ex);
        }
    }


    @Override
    public Task selectById(Integer id) throws CRMProjectRepositoryException {

        try (Session session = sessionFactory.openSession()) {
            return session.get(Task.class, id);
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT TASK BY ID: " + ex);
        }
    }

    @Override
    public Task add(Task task) throws CRMProjectRepositoryException {

        try (Session session = sessionFactory.openSession()) {
            session.save(task);
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO TASK- " + task + ": ", ex);
        }
        return task;
    }

    @Override
    public List<Task> addAll(List<Task> tasks) throws CRMProjectRepositoryException {

        try (Session session = sessionFactory.openSession()) {
            for (Task task : tasks) {
                session.save(task);
            }
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO THESE USERS - " + tasks + ": ", ex);
        }
        return tasks;
    }

    @Override
    public Task update(Task task) throws CRMProjectRepositoryException {

        try (Session session = sessionFactory.openSession()) {
            try {
                session.getTransaction().begin();
                Query query = session.createQuery(UPDATE);
                query.setParameter(TITLE_TASK, task.getTitle());
                query.setParameter(STATUS_TASK, task.getStatus());
                query.setParameter(DEADLINE_TASK, task.getDeadline());
                query.setParameter(INFO_TASK, task.getInfo());
                query.setParameter(ID_TASK, task.getId());
                query.executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new CRMProjectRepositoryException("ERROR: UPDATE TASK " + task, e);
            }
            return Optional.ofNullable(session.get(Task.class, task.getId()))
                    .orElseThrow(()->new CRMProjectRepositoryException("TASK NO FOUND DATA BASE"));
        }
    }

    @Override
    public void remove(Integer idTask) throws CRMProjectRepositoryException {

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            Task task = session.get(Task.class, idTask);
            session.delete(task);
            session.getTransaction().commit();
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: REMOVE_TASK - " + idTask + ": ", ex);
        }

    }

    @Override
    public List<User> selectAllUsersByTask(Task task) throws CRMProjectRepositoryException {

        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            List<User> users = new ArrayList<>(session.get(Task.class, task.getId()).getUsers());
            session.getTransaction().commit();
            return users;
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL USERS FOR TASK: ", ex);
        }
    }

}
