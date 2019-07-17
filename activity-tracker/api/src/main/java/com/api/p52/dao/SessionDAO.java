package com.api.p52.dao;

import com.api.p52.hibernate.CrudDAO;
import com.api.p52.model.Activity;
import com.api.p52.model.Session;
import com.api.p52.model.User;
import com.sun.jersey.spi.resource.Singleton;

import java.util.List;

@Singleton
public class SessionDAO extends CrudDAO<Session,Long> {

    public SessionDAO(){
        super(Session.class,Long.class);
    }

    public List<Session> findByUserId(Long userId){
        return findBy("user.id",userId);
    }
    public  List<Session> findByUser(User user){
        return findByUserId(user.getId());
    }
    public List<Session> findByActivityId(Long activityId){
        return findBy("activity.id",activityId);
    }
    public List<Session> findByActivity(Activity activity){
        return  findByActivityId(activity.getId());
    }
    public List<Session> findByUserIdAndActivityId(Long userId,Long activityId){
        org.hibernate.Session session = sf().openSession();
        session.beginTransaction();
        List<Session> sessions = (List<Session>) session.createQuery(
                "select s from com.api.p52.model.Session s  where s.user.id = :u and s.activity.id = :a")
                .setParameter("u",userId)
                .setParameter("a",activityId)
                .list();
        session.getTransaction().commit();
        session.close();
        return sessions;
    }
}
