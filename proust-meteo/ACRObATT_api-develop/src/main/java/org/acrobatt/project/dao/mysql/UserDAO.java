package org.acrobatt.project.dao.mysql;

import org.acrobatt.project.model.mysql.User;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDAO {

    private static UserDAO instance;
    private SessionFactory sfac = HibernateUtils.getSessionFactory();

    public static UserDAO getInstance() {
        if(instance == null) instance = new UserDAO();
        return instance;
    }

    public List<User> getAll() {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<User> result = s.createQuery("from User", User.class).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public User getById(Long id) {
        Session s = sfac.openSession();
        s.beginTransaction();
        User result = s.get(User.class, id);
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public User getByUsername(String name) {
        Session s = sfac.openSession();
        s.beginTransaction();
        User result = s.createQuery("from User where username=:name", User.class)
                .setParameter("name", name)
                .uniqueResult();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public User insert(User object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.save(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public User persist(User object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.persist(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public void update(User object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.update(object);
        s.getTransaction().commit();
        s.close();
    }

    public void merge(User object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.merge(object);
        s.getTransaction().commit();
        s.close();
    }

    public void delete(User object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }
}
