package org.acrobatt.project.dao.mysql;

import org.acrobatt.project.model.mysql.ApiKey;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class ApiKeyDAO implements IGenericDAO<ApiKey, Long> {

    private static ApiKeyDAO instance;
    private SessionFactory sfac = HibernateUtils.getSessionFactory();

    public static ApiKeyDAO getInstance() {
        if(instance == null) instance = new ApiKeyDAO();
        return instance;
    }

    public List<ApiKey> getAll() {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<ApiKey> result = s.createQuery("from ApiKey", ApiKey.class).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public ApiKey getById(Long id) {
        Session s = sfac.openSession();
        s.beginTransaction();
        ApiKey result = s.get(ApiKey.class, id);
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public ApiKey insert(ApiKey object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.save(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public ApiKey persist(ApiKey object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.persist(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public void update(ApiKey object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.update(object);
        s.getTransaction().commit();
        s.close();
    }

    public void merge(ApiKey object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.merge(object);
        s.getTransaction().commit();
        s.close();
    }

    public void delete(ApiKey object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }
}
