package com.api.p52.dao;

import com.api.p52.hibernate.CrudDAO;
import com.api.p52.model.Property;
import com.api.p52.model.Session;
import com.api.p52.model.SessionProperty;
import com.sun.jersey.spi.resource.Singleton;

import java.util.List;

@Singleton
public class SessionPropertyDAO  extends CrudDAO<SessionProperty,Long>{

    public SessionPropertyDAO(){
        super(SessionProperty.class,Long.class);
    }

    public List<SessionProperty> findBySessionId(Long id){
        return findBy("session.id",id);
    }
    public List<SessionProperty> findBySession(Session session){
        return findBySessionId(session.getId());
    }
    public List<SessionProperty> findByPropertyId(Long id){
        return findBy("property.id",id);
    }
    public List<SessionProperty> findByProperty(Property property){
        return findByPropertyId(property.getId());
    }
    public SessionProperty findOneBySessionIdAndPropertyId(Long sessionId,Long propertyId){
        org.hibernate.Session session = sf().openSession();
        session.beginTransaction();
        List<SessionProperty> sessionProperties = (List<SessionProperty>) session.createQuery(
                "select sp from com.api.p52.model.SessionProperty sp where sp.session.id = :s and sp.property.id = :p")
                .setParameter("s",sessionId)
                .setParameter("p",propertyId)
                .list();
        session.getTransaction().commit();
        session.close();
        return sessionProperties.size() == 0 ? null : sessionProperties.get(0);
    }
}
