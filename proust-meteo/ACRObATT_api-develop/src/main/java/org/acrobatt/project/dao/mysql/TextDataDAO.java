package org.acrobatt.project.dao.mysql;

import org.acrobatt.project.model.mysql.Property;
import org.acrobatt.project.model.mysql.TextData;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class TextDataDAO implements IGenericDAO<TextData, Long>{

    private static TextDataDAO instance;
    private SessionFactory sfac = HibernateUtils.getSessionFactory();

    public static TextDataDAO getInstance() {
        if(instance == null) instance = new TextDataDAO();
        return instance;
    }

    public List<TextData> getAll() {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<TextData> result = s.createQuery("from TextData", TextData.class).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public TextData getById(Long id) {
        Session s = sfac.openSession();
        s.beginTransaction();
        TextData result = s.get(TextData.class, id);
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public TextData getByText(String text) {
        Session s = sfac.openSession();
        s.beginTransaction();
        TextData result = s.createQuery("from TextData where text=:text",TextData.class)
                .setParameter("text", text)
                .uniqueResult();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public TextData getByTextAndProperty(String text, Property property) {
        Session s = sfac.openSession();
        s.beginTransaction();
        TextData result = s.createQuery("from TextData where text=:text and property=:prop",TextData.class)
                .setParameter("text", text)
                .setParameter("prop", property)
                .uniqueResult();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public TextData insert(TextData object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.save(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public TextData persist(TextData object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.persist(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public void update(TextData object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.update(object);
        s.getTransaction().commit();
        s.close();
    }

    public void merge(TextData object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.merge(object);
        s.getTransaction().commit();
        s.close();
    }

    public void delete(TextData object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }
}
