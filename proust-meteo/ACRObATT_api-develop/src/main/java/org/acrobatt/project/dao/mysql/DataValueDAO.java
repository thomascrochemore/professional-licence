package org.acrobatt.project.dao.mysql;

import org.acrobatt.project.model.mysql.DataValue;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class DataValueDAO {

    private static DataValueDAO instance;
    private SessionFactory sfac = HibernateUtils.getSessionFactory();

    public static DataValueDAO getInstance() {
        if(instance == null) instance = new DataValueDAO();
        return instance;
    }

    public List<DataValue> getAll() {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<DataValue> result = s.createQuery("from DataValue", DataValue.class).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public DataValue getById(Long id) {
        Session s = sfac.openSession();
        s.beginTransaction();
        DataValue result = s.get(DataValue.class, id);
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public DataValue insert(DataValue object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.save(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public DataValue persist(DataValue object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.persist(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public void update(DataValue object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.update(object);
        s.getTransaction().commit();
        s.close();
    }

    public void merge(DataValue object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.merge(object);
        s.getTransaction().commit();
        s.close();
    }

    public void delete(DataValue object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }

    public void deleteAll(){
        Session s = sfac.openSession();
        s.beginTransaction();
        s.createQuery("delete from DataValue").executeUpdate();
        s.getTransaction().commit();
        s.close();
    }
}
