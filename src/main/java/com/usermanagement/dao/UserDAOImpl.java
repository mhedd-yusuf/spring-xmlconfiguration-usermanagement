package com.usermanagement.dao;

import com.usermanagement.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    @Transactional
    public User save(User user) {
        getCurrentSession().save(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        User user = getCurrentSession().get(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        Query<User> query = getCurrentSession()
                .createQuery("FROM User u ORDER BY u.createdAt DESC", User.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByStatus(User.UserStatus status) {
        Query<User> query = getCurrentSession()
                .createQuery("FROM User u WHERE u.status = :status ORDER BY u.createdAt DESC",
                        User.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        Query<User> query = getCurrentSession()
                .createQuery("FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        return query.uniqueResultOptional();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        Query<User> query = getCurrentSession()
                .createQuery("FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query.uniqueResultOptional();
    }

    @Override
    @Transactional
    public User update(User user) {
        getCurrentSession().update(user);
        return user;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = getCurrentSession().get(User.class, id);
        if (user != null) {
            getCurrentSession().delete(user);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        Query<Long> query = getCurrentSession()
                .createQuery("SELECT COUNT(u) FROM User u", Long.class);
        return query.getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        Query<Long> query = getCurrentSession()
                .createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username",
                        Long.class);
        query.setParameter("username", username);
        return query.getSingleResult() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        Query<Long> query = getCurrentSession()
                .createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email",
                        Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }
}
