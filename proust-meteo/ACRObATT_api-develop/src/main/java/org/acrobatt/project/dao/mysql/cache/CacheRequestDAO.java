package org.acrobatt.project.dao.mysql.cache;

import org.acrobatt.project.model.mysql.cache.CacheRequest;
import org.acrobatt.project.model.mysql.cache.WeatherDataCache;
import org.acrobatt.project.utils.DateUtils;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class CacheRequestDAO {
    private static CacheRequestDAO instance = null;


    private SessionFactory sfac = HibernateUtils.getSessionFactory();

    public static CacheRequestDAO getInstance(){
        if(instance == null) instance = new CacheRequestDAO();
        return instance;
    }

    public CacheRequest persist(CacheRequest cacheRequest){
        Session s = sfac.openSession();
        s.beginTransaction();
        s.persist(cacheRequest);
        s.getTransaction().commit();
        s.close();
        return cacheRequest;
    }
    public List<CacheRequest> persistAll(List<CacheRequest> cacheRequestList){
        Session session = sfac.openSession();
        for(CacheRequest cacheRequest : cacheRequestList){
            session.beginTransaction();
            session.persist(cacheRequest);
            session.getTransaction().commit();
        }
        session.close();
        return cacheRequestList;
    }
    public void deleteAll(){
        Session session = sfac.openSession();
        session.beginTransaction();
        session.createQuery("delete from CacheRequest ").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public boolean requestCacheExists(CacheRequest cacheRequest){
        Session s = sfac.openSession();
        s.beginTransaction();
        List<CacheRequest> result = s.createQuery("from CacheRequest cr " +
               "where cr.api.id=:api " +
               "and cr.delay.id=:delay " +
               "and cr.location.id=:location " +
               "and date_format(cr.date_request,'%Y-%m-%d %H:00:00') = date_format(:storedDate,'%Y-%m-%d %H:00:00')",CacheRequest.class)
               .setParameter("api",cacheRequest.getApi().getId())
                .setParameter("location",cacheRequest.getLocation().getId())
                .setParameter("delay",cacheRequest.getDelay().getId())
                .setParameter("storedDate", DateUtils.truncateDateToHour(cacheRequest.getDate_request()))
                .list();
        s.getTransaction().commit();
        s.close();
        return !result.isEmpty();
    }
}