package org.acrobatt.project.dao.mysql;

import org.acrobatt.project.model.mysql.ApiURL;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class ApiURLDAO implements IGenericDAO<ApiURL, Long> {

    private static ApiURLDAO instance;
    private SessionFactory sfac = HibernateUtils.getSessionFactory();

    public static ApiURLDAO getInstance() {
        if(instance == null) instance = new ApiURLDAO();
        return instance;
    }

    public List<ApiURL> getAll() {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<ApiURL> result = s.createQuery("from ApiURL", ApiURL.class).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public ApiURL getById(Long id) {
        Session s = sfac.openSession();
        s.beginTransaction();
        ApiURL result = s.get(ApiURL.class, id);
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public ApiURL insert(ApiURL object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.save(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public ApiURL persist(ApiURL object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.persist(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public void update(ApiURL object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.update(object);
        s.getTransaction().commit();
        s.close();
    }

    public void merge(ApiURL object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.merge(object);
        s.getTransaction().commit();
        s.close();
    }

    public void delete(ApiURL object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }
}
