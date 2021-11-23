package com.itrex.java.lab.crm.repository.impl.hibernate;

import com.itrex.java.lab.crm.entity.Task;
import com.itrex.java.lab.crm.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.crm.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Primary
@Repository
public class HibernateTaskRepositoryImpl implements TaskRepository {

    private static final String USER_ID = "userId";

    private static final String SELECT_ALL = "select t from Task t";
    private static final String SELECT_ALL_TASKS_BY_USER = "select t from User u join u.tasks t where u.id =:userId";

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public List<Task> selectAll() throws CRMProjectRepositoryException {
        try {
            return entityManager.createQuery(SELECT_ALL, Task.class).getResultList();
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL TASK: " + ex);
        }
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public Task selectById(Integer id) throws CRMProjectRepositoryException {
        try {
            return entityManager.find(Task.class, id);
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT TASK BY ID: " + ex);
        }
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public List<Task> selectAllTasksByUserId(Integer id) throws CRMProjectRepositoryException {
        try {

            return entityManager.createQuery(SELECT_ALL_TASKS_BY_USER, Task.class)
                    .setParameter(USER_ID, id)
                    .getResultList();
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL TASK FOR USER: ", ex);
        }
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public Task add(Task task) throws CRMProjectRepositoryException {
        try {
            entityManager.persist(task);
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO TASK- " + task + ": ", ex);
        }
        return task;
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public Task update(Task task) throws CRMProjectRepositoryException {
        try {
            entityManager.merge(task);
            return entityManager.find(Task.class, task.getId());
        } catch (Exception e) {
            throw new CRMProjectRepositoryException("ERROR: UPDATE TASK " + task, e);
        }
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public void remove(Integer taskId) throws CRMProjectRepositoryException {
        try {
            Task task = entityManager.find(Task.class, taskId);
            entityManager.remove(task);
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: REMOVE_TASK - " + taskId + ": ", ex);
        }
    }

}
