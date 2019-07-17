package org.acrobatt.project.dao.mysql;

import org.acrobatt.project.model.mysql.Api;
import org.acrobatt.project.model.mysql.TextDataConvert;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class TextDataConvertDAO implements IGenericDAO<TextDataConvert,Long> {

    private static TextDataConvertDAO instance;
    private SessionFactory sfac = HibernateUtils.getSessionFactory();

    public static TextDataConvertDAO getInstance() {
        if(instance == null) instance = new TextDataConvertDAO();
        return instance;
    }

    @Override
    public List<TextDataConvert> getAll() {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<TextDataConvert> result = s.createQuery("from TextDataConvert", TextDataConvert.class).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    @Override
    public TextDataConvert getById(Long id) {
        Session s = sfac.openSession();
        s.beginTransaction();
        TextDataConvert result = s.get(TextDataConvert.class, id);
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public TextDataConvert getForRawApiText(String raw, Api api) {
        Session s = sfac.openSession();
        s.beginTransaction();
        TextDataConvert result = s.createQuery("from TextDataConvert where apiTextData=:raw and api=:api", TextDataConvert.class)
                .setParameter("raw", raw)
                .setParameter("api", api)
                .uniqueResult();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    @Override
    public TextDataConvert insert(TextDataConvert object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.save(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    @Override
    public TextDataConvert persist(TextDataConvert object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.persist(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    @Override
    public void update(TextDataConvert object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.update(object);
        s.getTransaction().commit();
        s.close();
    }

    @Override
    public void merge(TextDataConvert object) {
        Session s = sfac.openSession();
        s.merge(object);
        s.getTransaction().commit();
        s.close();
    }

    @Override
    public void delete(TextDataConvert object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }
}
