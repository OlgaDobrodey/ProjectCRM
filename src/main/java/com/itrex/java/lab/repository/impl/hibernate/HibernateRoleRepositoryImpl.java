package com.itrex.java.lab.repository.impl.hibernate;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.repository.RoleRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

public class HibernateRoleRepositoryImpl implements RoleRepository {

    private static final int DEFAULT_ROLE = 2;
    private static final String ROLE_NAME = "roleName";
    private static final String ID_ROLE = "idRole";
    private static final String SELECT_ALL = "from Role r";
    private static final String UPDATE = "update Role set roleName = :roleName where id = :idRole";


    private final SessionFactory sessionFactory;

    public HibernateRoleRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Role> selectAll() throws CRMProjectRepositoryException {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(SELECT_ALL, Role.class).list();
        } catch (Exception e) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ALL ROLES: ", e);
        }
    }

    @Override
    public Role selectById(Integer id) throws CRMProjectRepositoryException {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Role.class, id);
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: SELECT ROLE BY ID: ", ex);
        }
    }

    @Override
    public Role add(Role role) throws CRMProjectRepositoryException {
        try (Session session = sessionFactory.openSession()) {
            session.save(role);
            return role;
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO ROLE - " + role.getRoleName() + ": ", ex);
        }
    }

    @Override
    public List<Role> addAll(List<Role> roles) throws CRMProjectRepositoryException {

        try (Session session = sessionFactory.openSession()) {
            for (Role role : roles) {
                session.save(role);
            }
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: INSERT INTO THESE ROLES - " + roles + ": ", ex);
        }
        return roles;
    }

    @Override
    public Role update(Role role, Integer id) throws CRMProjectRepositoryException {
        Role roleUpdate = null;
        try (Session session = sessionFactory.openSession()) {
            try {
                session.getTransaction().begin();
                Query query = session.createQuery(UPDATE);
                query.setParameter(ROLE_NAME, role.getRoleName());
                query.setParameter(ID_ROLE, id);
                query.executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new CRMProjectRepositoryException("ERROR: UPDATE ROLE " + role, e);
            }
            roleUpdate = session.get(Role.class, id);
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: UPDATE_ROLE - " + role, ex);
        }
        return roleUpdate;
    }

    @Override
    public boolean remove(Role role) throws CRMProjectRepositoryException {
        return remove(role, selectById(DEFAULT_ROLE));
    }

    @Override
    @Transactional
    public boolean remove(Role role, Role defaultRole) throws CRMProjectRepositoryException {
        if (role.equals(defaultRole)) {
            throw new CRMProjectRepositoryException("ERROR : Role equals default role");
        }
        try (Session session = sessionFactory.openSession()) {
            List<User> users = role.getUsers();
            System.out.println(users);

            session.delete(role);
        } catch (Exception ex) {
            throw new CRMProjectRepositoryException("ERROR: REMOVE_ROLE - " + role + ": ", ex);
        }
        return false;
    }
}
