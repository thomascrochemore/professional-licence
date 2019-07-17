package org.acrobatt.project.dao.mysql;

import org.acrobatt.project.model.mysql.Delay;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class DelayDAO implements IGenericDAO<Delay, Long> {

    private static DelayDAO instance;
    private SessionFactory sfac = HibernateUtils.getSessionFactory();

    public static DelayDAO getInstance() {
        if(instance == null) instance = new DelayDAO();
        return instance;
    }

    public List<Delay> getAll() {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<Delay> result = s.createQuery("from Delay", Delay.class).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Delay getById(Long id) {
        Session s = sfac.openSession();
        s.beginTransaction();
        Delay result = s.get(Delay.class, id);
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Delay getByDuration(Integer dur) {
        Session s = sfac.openSession();
        s.beginTransaction();
        Delay result = s.createQuery("from Delay where value=:dur",Delay.class)
                .setParameter("dur", dur)
                .uniqueResult();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Delay getByMaxValue() {
        Session s = sfac.openSession();
        s.beginTransaction();
        Delay result = s.createQuery("from Delay order by value desc",Delay.class)
                .setMaxResults(1)
                .uniqueResult();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Delay insert(Delay object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.save(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public Delay persist(Delay object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.persist(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public void update(Delay object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.update(object);
        s.getTransaction().commit();
        s.close();
    }

    public void merge(Delay object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.merge(object);
        s.getTransaction().commit();
        s.close();
    }

    public void delete(Delay object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }
}
