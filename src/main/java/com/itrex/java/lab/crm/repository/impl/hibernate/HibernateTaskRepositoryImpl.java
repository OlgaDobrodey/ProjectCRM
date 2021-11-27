package com.itrex.java.lab.crm.repository.impl.hibernate;

import com.itrex.java.lab.crm.entity.Task;
import com.itrex.java.lab.crm.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.crm.repository.TaskRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Deprecated
@Repository
@Qualifier("HibernateTaskRepository")
public class HibernateTaskRepositoryImpl implements TaskRepository {

    private static final String USER_ID = "userId";

    private static final String SELECT_ALL = "select t from Task t";
    private static final String SELECT_ALL_TASKS_BY_USER = "select t from User u join u.tasks t where u.id =:userId";

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Task> selectAll() throws CRMProjectRepositoryException {
        return entityManager.createQuery(SELECT_ALL, Task.class).getResultList();
    }

    @Override

    public Task selectById(Integer id) throws CRMProjectRepositoryException {
        return entityManager.find(Task.class, id);
    }

    @Override
    public List<Task> selectAllTasksByUserId(Integer id) throws CRMProjectRepositoryException {

        return entityManager.createQuery(SELECT_ALL_TASKS_BY_USER, Task.class)
                .setParameter(USER_ID, id)
                .getResultList();
    }

    @Override
    public Task add(Task task) throws CRMProjectRepositoryException {
        Session session = entityManager.unwrap(Session.class);
        Integer taskId = (Integer) session.save(task);

        return session.get(Task.class, taskId);
    }

    @Override
    public Task update(Task task) {

        entityManager.merge(task);
        return entityManager.find(Task.class, task.getId());
    }

    @Override
    public void remove(Integer taskId) throws CRMProjectRepositoryException {
        try {
            Task task = entityManager.find(Task.class, taskId);
            entityManager.remove(task);
        }catch (Exception e){
            throw new CRMProjectRepositoryException("Error remove task",e);
        }
    }

}
