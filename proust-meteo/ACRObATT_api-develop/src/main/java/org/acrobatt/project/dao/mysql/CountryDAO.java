package org.acrobatt.project.dao.mysql;

import org.acrobatt.project.model.mysql.Country;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class CountryDAO implements IGenericDAO<Country, Long> {

    private static CountryDAO instance;
    private SessionFactory sfac = HibernateUtils.getSessionFactory();

    public static CountryDAO getInstance() {
        if(instance == null) instance = new CountryDAO();
        return instance;
    }

    public List<Country> getAll() {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<Country> result = s.createQuery("from Country", Country.class).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Country getById(Long id) {
        Session s = sfac.openSession();
        s.beginTransaction();
        Country result = s.get(Country.class, id);
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Country getByName(String name) {
        Session s = sfac.openSession();
        s.beginTransaction();
        Country result = s.createQuery("from Country where lower(name)=lower(:name)", Country.class)
                .setParameter("name", name)
                .uniqueResult();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Country getByCode(String code) {
        Session s = sfac.openSession();
        s.beginTransaction();
        Country result = s.createQuery("from Country where code=:code", Country.class)
                .setParameter("code", code)
                .uniqueResult();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Country insert(Country object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.save(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public Country persist(Country object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.persist(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public void update(Country object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.update(object);
        s.getTransaction().commit();
        s.close();
    }

    public void merge(Country object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.merge(object);
        s.getTransaction().commit();
        s.close();
    }

    public void delete(Country object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }
}
