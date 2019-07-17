package org.acrobatt.project.dao.mysql;

import org.acrobatt.project.model.mysql.Country;
import org.acrobatt.project.model.mysql.Location;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class LocationDAO implements IGenericDAO<Location, Long> {

    private static LocationDAO instance;
    private SessionFactory sfac = HibernateUtils.getSessionFactory();

    public static LocationDAO getInstance() {
        if(instance == null) instance = new LocationDAO();
        return instance;
    }

    public List<Location> getAll() {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<Location> result = s.createQuery("from Location", Location.class).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Location getById(Long id) {
        Session s = sfac.openSession();
        s.beginTransaction();
        Location result = s.get(Location.class, id);
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public List<Location> getByCity(String city) {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<Location> result = s.createQuery("from Location where lower(city)=lower(:city)",Location.class)
                .setParameter("city", city)
                .list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public List<Location> getByCountry(Country country) {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<Location> result = s.createQuery("from Location where lower(country)=lower(:country)",Location.class)
                .setParameter("country", country)
                .list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Location getByCityAndCountry(String city, Country country){
        Session s = sfac.openSession();
        s.beginTransaction();
        Location result = s.createQuery("from Location where lower(city)=lower(:city) and lower(country)=lower(:country)",Location.class)
                .setParameter("city",city)
                .setParameter("country", country)
                .uniqueResult();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Location getByCoordinates(Float latitude, Float longitude) {
        Session s = sfac.openSession();
        s.beginTransaction();
        Location result = s.createQuery("from Location where latitude=:lat and longitude=:long", Location.class)
                .setParameter("lat", latitude)
                .setParameter("long", longitude)
                .uniqueResult();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public Location insert(Location object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.save(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public Location persist(Location object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.persist(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public void update(Location object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.update(object);
        s.getTransaction().commit();
        s.close();
    }

    public void merge(Location object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.merge(object);
        s.getTransaction().commit();
        s.close();
    }

    public void delete(Location object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }
}