package com.api.p52.hibernate;

import com.api.p52.factory.HibernateFactory;
import com.sun.jersey.api.core.InjectParam;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public class CrudDAO<ModelClass,IdClass extends Serializable>{

    private final Class<ModelClass> modelClass;
    private final Class<IdClass> idClass;

    @InjectParam
    private HibernateFactory hibernateFactory;

    public CrudDAO(Class<ModelClass> modelClass,Class<IdClass> idClass) {
        this.modelClass = modelClass;
        this.idClass = idClass;
    }
    protected SessionFactory getSessionFactory()
    {
        return this.hibernateFactory.getSessionFactory();
    }
    protected  SessionFactory sf(){
        return getSessionFactory();
    }
    public ModelClass insert(ModelClass model) {
        Session session = sf().openSession();
        session.beginTransaction();
        session.save(model);
        session.getTransaction().commit();
        session.close();
        return model;
    }

    public Iterable<ModelClass> insertAll(Iterable<ModelClass> models){
        Session session = sf().openSession();
        session.beginTransaction();
        for(ModelClass model : models) {
            session.save(model);
        }
        session.getTransaction().commit();
        session.close();
        return models;
    }
    public ModelClass update(ModelClass model) {
        Session session = sf().openSession();
        session.beginTransaction();
        session.update(model);
        session.getTransaction().commit();
        session.close();
        return model;
    }
    public Iterable<ModelClass> updateAll(Iterable<ModelClass> models){
        Session session = sf().openSession();
        session.beginTransaction();
        for(ModelClass model : models) {
            session.update(model);
        }
        session.getTransaction().commit();
        session.close();
        return models;
    }
    public ModelClass save(ModelClass model) {
        Session session = sf().openSession();
        session.beginTransaction();
        session.saveOrUpdate(model);
        session.getTransaction().commit();
        session.close();
        return model;
    }

    public Iterable<ModelClass> saveAll(Iterable<ModelClass> models){
        Session session = sf().openSession();
        session.beginTransaction();
        for(ModelClass model : models) {
            session.saveOrUpdate(model);
        }
        session.getTransaction().commit();
        session.close();
        return models;
    }

    public void delete(ModelClass model) {
        Session session = sf().openSession();
        session.beginTransaction();
        session.delete(model);
        session.getTransaction().commit();
        session.close();
    }

    public void deleteAll(Iterable<ModelClass> models){
        Session session = sf().openSession();
        session.beginTransaction();
        for(ModelClass model : models) {
            session.delete(model);
        }
        session.getTransaction().commit();
        session.close();
    }

    public ModelClass findOne(IdClass id){
        Session session = sf().openSession();
        session.beginTransaction();
        ModelClass event = (ModelClass) session.get(modelClass.getName(),id);
        session.getTransaction().commit();
        session.close();
        return event;
    }
    public List<ModelClass> findAll(){
        Session session = sf().openSession();
        session.beginTransaction();
        List<ModelClass> models = session.createQuery("select m from "+modelClass.getName()+ " m ").list();
        session.getTransaction().commit();
        session.close();
        return models;
    }

    public List<ModelClass> findBy(String field,Serializable value){
        Session session = sf().openSession();
        session.beginTransaction();
        List<ModelClass> result = (List<ModelClass>) session.createQuery(
                "select m from "+modelClass.getName()+ " m where m."+field.toString()+" = :v")
                .setParameter("v",value)
                .list();
        session.getTransaction().commit();
        session.close();
        return result;
    }
    public ModelClass findOneBy(String field,Serializable value) {
        List<ModelClass> models = findBy(field,value);
        return models.size() == 0 ? null : models.get(0);
    }

}
