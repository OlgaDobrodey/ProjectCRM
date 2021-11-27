package com.itrex.java.lab.crm.repository.impl.hibernate;

import com.itrex.java.lab.crm.entity.Role;
import com.itrex.java.lab.crm.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.crm.repository.RoleRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Deprecated
@Repository
@Qualifier("HibernateRoleRepository")
public class HibernateRoleRepositoryImpl implements RoleRepository {

    private static final String SELECT_ALL = "select r from Role r";

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Role> selectAll() {
        return entityManager.createQuery(SELECT_ALL, Role.class).getResultList();
    }

    @Override
    public Role selectById(Integer id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public Role add(Role role) {
        Session session = entityManager.unwrap(Session.class);
        Integer roleId = (Integer) session.save(role);

        return session.get(Role.class, roleId);
    }

    @Override
    public Role update(Role role) throws CRMProjectRepositoryException {
        try {
            entityManager.merge(role);
            return Optional.ofNullable(entityManager.find(Role.class, role.getId())).orElseThrow(() -> new CRMProjectRepositoryException("ROLE NO FOUND DATA BASE"));
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: UPDATE_ROLE - " + role, ex);
        }
    }

}
