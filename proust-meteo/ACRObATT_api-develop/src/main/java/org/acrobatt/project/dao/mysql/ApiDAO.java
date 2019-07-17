package org.acrobatt.project.dao.mysql;

import org.acrobatt.project.model.mysql.Api;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class ApiDAO implements IGenericDAO<Api, Long> {

    private static ApiDAO instance;
    private SessionFactory sfac = HibernateUtils.getSessionFactory();

    public static ApiDAO getInstance() {
        if(instance == null) instance = new ApiDAO();
        return instance;
    }

    public List<Api> getAll() {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<Api> result = s.createQuery("from Api", Api.class).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public List<Api> getAllWithKeysAndUrls(){
        Session s = sfac.openSession();
        s.beginTransaction();
        List<Api> result = s.createQuery("select distinct api from Api api " +
                "join fetch api.api_keys " +
                "join fetch api.url_schemes", Api.class).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Api getById(Long id) {
        Session s = sfac.openSession();
        s.beginTransaction();
        Api result = s.get(Api.class, id);
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Api getByName(String name) {
        Session s = sfac.openSession();
        s.beginTransaction();
        Api result = s.createQuery("from Api where name=:name", Api.class)
                .setParameter("name", name)
                .uniqueResult();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Api getByNameWithKeysAndUrl(String name) {
        Session s = sfac.openSession();
        s.beginTransaction();
        Api result = s.createQuery("from Api api " +
                "left join fetch api.api_keys " +
                "left join fetch api.url_schemes " +
                "where name=:name ", Api.class)
                .setParameter("name", name)
                .uniqueResult();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Api insert(Api object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.save(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public Api persist(Api object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.persist(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public void update(Api object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.update(object);
        s.getTransaction().commit();
        s.close();
    }

    public void merge(Api object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.merge(object);
        s.getTransaction().commit();
        s.close();
    }

    public void delete(Api object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }
}
