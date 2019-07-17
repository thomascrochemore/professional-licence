package org.acrobatt.project.dao.mysql.cache;

import org.acrobatt.project.dao.mysql.IGenericDAO;
import org.acrobatt.project.model.mysql.*;
import org.acrobatt.project.model.mysql.cache.DataValueCache;
import org.acrobatt.project.model.mysql.cache.WeatherDataCache;
import org.acrobatt.project.utils.DateUtils;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.*;

public class WeatherDataCacheDAO implements IGenericDAO<WeatherDataCache, Long> {

    private static Logger logger = LogManager.getLogger(WeatherDataCacheDAO.class);

    private static WeatherDataCacheDAO instance;
    private SessionFactory sfac = HibernateUtils.getSessionFactory();

    private DataValueCacheDAO dataValueCacheDAO = DataValueCacheDAO.getInstance();

    public static WeatherDataCacheDAO getInstance() {
        if(instance == null) instance = new WeatherDataCacheDAO();
        return instance;
    }

    public List<WeatherDataCache> getAll() {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<WeatherDataCache> result = s.createQuery("from WeatherDataCache", WeatherDataCache.class).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }
    public Optional<WeatherDataCache> getByApiAndLocationAndDelayAndInsertedAt(Api api,Location location,Delay delay,Date storedDate){
        Session s = sfac.openSession();
        s.beginTransaction();
        List<WeatherDataCache> result = s.createQuery("from WeatherDataCache wdc " +
                "left join fetch wdc.data_values " +
                "left join fetch wdc.data_api " +
                "left join fetch wdc.data_delay " +
                "left join fetch wdc.data_location " +
                "where wdc.data_api.id=:api and wdc.data_location.id=:location and wdc.data_delay.id = :delay " +
                "and date_format(wdc.inserted_at,'%Y-%m-%d %H:00:00') = date_format(:storedDate,'%Y-%m-%d %H:00:00')" , WeatherDataCache.class)
                .setParameter("api",api.getId())
                .setParameter("location",location.getId())
                .setParameter("delay",delay.getId())
                .setParameter("storedDate", DateUtils.truncateDateToHour(storedDate))
                .list();
        s.getTransaction().commit();
        s.close();
        return result.isEmpty() ? Optional.empty() : Optional.ofNullable(result.get(0));
    }
    public WeatherDataCache getById(Long id) {
        Session s = sfac.openSession();
        s.beginTransaction();
        WeatherDataCache result = s.get(WeatherDataCache.class, id);
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public WeatherDataCache insert(WeatherDataCache object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.save(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public WeatherDataCache persist(WeatherDataCache object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.persist(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public List<WeatherDataCache> persistAll(List<WeatherDataCache> weatherDataList){
        Session s = sfac.openSession();
        //s.beginTransaction();
        for(WeatherDataCache weatherData : weatherDataList){
            s.beginTransaction();
            s.persist(weatherData);
            s.getTransaction().commit();
        }
        //s.getTransaction().commit();
        s.close();
        return weatherDataList;
    }

    public void update(WeatherDataCache object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.update(object);
        s.getTransaction().commit();
        s.close();
    }

    public void merge(WeatherDataCache object) {
        Session s = sfac.openSession();
        s.merge(object);
        s.getTransaction().commit();
        s.close();
    }

    public void delete(WeatherDataCache object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }

    public void deleteAll(){
        dataValueCacheDAO.deleteAll();
        Session s = sfac.openSession();
        s.beginTransaction();
        s.createQuery("delete from WeatherDataCache").executeUpdate();
        s.getTransaction().commit();
        s.close();
    }

    /**
     * Fetches rawdata inside the database by using the JPA Criteria API
     * @param criteria criteria hashMap
     * @return a list of JPA Tuples
     * @throws IllegalArgumentException if one or more args are invalid
     */
    public List<Tuple> getValuesByCriteria(HashMap<String, Object> criteria) throws IllegalArgumentException {
        logger.debug("Chosen criteria : "+criteria);


        //open session
        try(Session s = sfac.openSession()) {

            //create criteria builder
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();

            //get and join necessary tables
            final Root<WeatherDataCache> root_wdata = query.from(WeatherDataCache.class);
            final Join<WeatherDataCache, Api> wdataJoinApi = root_wdata.join("data_api", JoinType.LEFT);
            final Join<WeatherDataCache, Delay> wdataJoinDelay = root_wdata.join("data_delay", JoinType.LEFT);
            final Join<WeatherDataCache, Location> wdataJoinLocation = root_wdata.join("data_location", JoinType.LEFT);
            final Join<WeatherData, DataValueCache> wdataValue = root_wdata.join("data_values", JoinType.LEFT);
            final Join<DataValueCache, Property> wdataJoinProperty = wdataValue.join("property", JoinType.LEFT);
            //create predicate list to build the query
            List<Predicate> criteriaList = new ArrayList<>();
            List<Selection> selectList = new ArrayList<>();

            //delay predicate (set forecast to true if delay is not zero
            //compute new date with specified delay
            if (criteria.get("delay") != null) {
                criteriaList.add(builder.equal(wdataJoinDelay.get("value"), criteria.get("delay")));
                if ((Integer) criteria.get("delay") != 0) criteria.replace("is_forecast", true);
            }
            if (criteria.get("is_forecast") != null) {
                criteriaList.add(builder.equal(root_wdata.get("isForecast"), criteria.get("is_forecast")));
            }
            if (criteria.get("city") != null) {
                criteriaList.add(builder.equal(wdataJoinLocation.get("city"), criteria.get("city")));
            }
            if (criteria.get("property") != null) {
                criteriaList.add(wdataJoinProperty.get("name").in(criteria.get("property")));
            }
            if (criteria.get("datetime") != null) {
                criteriaList.add(builder.equal(
                        builder.function("date_format", String.class, root_wdata.get("inserted_at"), builder.literal("%Y-%m-%d %H:00:00")).as(java.util.Date.class),
                        DateUtils.truncateDateToHour((Date) criteria.get("datetime")))
                );
            }
            if (criteria.get("api") != null) {
                criteriaList.add(builder.equal(wdataJoinApi.get("name"), criteria.get("api")));
            }
            selectList.add(wdataJoinApi.get("name").alias(GlobalVariableRegistry.API_ALIAS));
            selectList.add(wdataJoinProperty.get("name").alias(GlobalVariableRegistry.PROPERTY_ALIAS));
            selectList.add(wdataJoinProperty.get("unit").alias(GlobalVariableRegistry.UNIT_ALIAS));
            selectList.add(wdataJoinLocation.get("city").alias(GlobalVariableRegistry.CITY_ALIAS));
            selectList.add(wdataJoinDelay.get("value").alias(GlobalVariableRegistry.DELAY_ALIAS));
            selectList.add(root_wdata.get("inserted_at").alias(GlobalVariableRegistry.DATETIME_ALIAS));
            selectList.add(wdataValue.get("value_decimal").alias(GlobalVariableRegistry.VALUE_ALIAS));
            //==== END MODULAR SELECTION =============================================================================================//

            query.select(builder.tuple(selectList.toArray(new Selection[0]))).distinct(true);
            query.where(builder.and(criteriaList.toArray(new Predicate[0])));
            query.orderBy(builder.asc(root_wdata.get("inserted_at")));

            Query<Tuple> getData = s.createQuery(query);
            logger.debug("Generated SQL : " + getData.getQueryString());
            return getData.getResultList();
        } finally {
            sfac.getCurrentSession().close();
        }
    }
}

