package org.acrobatt.project.dao.mysql.cache;

import org.acrobatt.project.model.mysql.DataValue;
import org.acrobatt.project.model.mysql.cache.DataValueCache;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class DataValueCacheDAO {

    private static DataValueCacheDAO instance;
    private SessionFactory sfac = HibernateUtils.getSessionFactory();

    public static DataValueCacheDAO getInstance() {
        if(instance == null) instance = new DataValueCacheDAO();
        return instance;
    }

    public List<DataValueCache> getAll() {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<DataValueCache> result = s.createQuery("from DataValueCache ", DataValueCache.class).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public DataValueCache getById(Long id) {
        Session s = sfac.openSession();
        s.beginTransaction();
        DataValueCache result = s.get(DataValueCache.class, id);
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public DataValueCache insert(DataValueCache object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.save(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public DataValueCache persist(DataValueCache object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.persist(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public void update(DataValueCache object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.update(object);
        s.getTransaction().commit();
        s.close();
    }

    public void merge(DataValueCache object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.merge(object);
        s.getTransaction().commit();
        s.close();
    }

    public void delete(DataValueCache object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }

    public void deleteAll(){
        Session s = sfac.openSession();
        s.beginTransaction();
        s.createQuery("delete from DataValueCache").executeUpdate();
        s.getTransaction().commit();
        s.close();
    }
}
