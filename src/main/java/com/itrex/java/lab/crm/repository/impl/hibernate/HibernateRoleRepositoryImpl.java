package com.itrex.java.lab.crm.repository.impl.hibernate;

import com.itrex.java.lab.crm.entity.Role;
import com.itrex.java.lab.crm.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.crm.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public List<Role> selectAll() throws CRMProjectRepositoryException {
        try {
            return entityManager.createQuery(SELECT_ALL, Role.class).getResultList();
        } catch (Exception e) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL ROLES: ", e);
        }
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public Role selectById(Integer id) throws CRMProjectRepositoryException {
        try {
            return entityManager.find(Role.class, id);
        } catch (Exception e) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ROLE BY ID: ", e);
        }
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public Role add(Role role) throws CRMProjectRepositoryException {
        try {
            entityManager.persist(role);
            return role;
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO ROLE - " + role.getRoleName() + ": ", ex);
        }
    }

    @Override
    @Transactional(rollbackFor = {CRMProjectRepositoryException.class})
    public Role update(Role role) throws CRMProjectRepositoryException {
        try {
            entityManager.merge(role);
            return Optional.ofNullable(entityManager.find(Role.class, role.getId())).orElseThrow(() -> new CRMProjectRepositoryException("ROLE NO FOUND DATA BASE"));
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: UPDATE_ROLE - " + role, ex);
        }
    }

}
