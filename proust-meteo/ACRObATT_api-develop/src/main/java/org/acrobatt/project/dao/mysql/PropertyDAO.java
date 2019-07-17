package org.acrobatt.project.dao.mysql;

import org.acrobatt.project.model.mysql.Property;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class PropertyDAO implements IGenericDAO<Property, Long> {

    private static PropertyDAO instance;
    private SessionFactory sfac = HibernateUtils.getSessionFactory();

    public static PropertyDAO getInstance() {
        if(instance == null) instance = new PropertyDAO();
        return instance;
    }

    public List<Property> getAll() {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<Property> result = s.createQuery("from Property", Property.class).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Property getById(Long id) {
        Session s = sfac.openSession();
        s.beginTransaction();
        Property result = s.get(Property.class, id);
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Property getByName(String name) {
        Session s = sfac.openSession();
        s.beginTransaction();
        Property result = s.createQuery("from Property where name=:name", Property.class)
                .setParameter("name", name)
                .uniqueResult();
        s.getTransaction().commit();
        s.close();
        return result;
    }
    public List<Property> getByNames(List<String> names){
        Session s = sfac.openSession();
        s.beginTransaction();
        List<Property> result = s.createQuery("from Property where name in :names", Property.class)
                .setParameter("names", names)
                .list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Property insert(Property object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.save(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public Property persist(Property object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.persist(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public void update(Property object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.update(object);
        s.getTransaction().commit();
        s.close();
    }

    public void merge(Property object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.merge(object);
        s.getTransaction().commit();
        s.close();
    }

    public void delete(Property object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }
}
