package org.acrobatt.project.dao.mysql;

import org.acrobatt.project.model.mysql.*;
import org.acrobatt.project.utils.DateUtils;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.HashMapUtils;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.*;

public class WeatherDataDAO implements IGenericDAO<WeatherData, Long> {

    private static Logger logger = LogManager.getLogger(WeatherDataDAO.class);

    private static WeatherDataDAO instance;
    private SessionFactory sfac = HibernateUtils.getSessionFactory();

    private DataValueDAO dataValueDAO = DataValueDAO.getInstance();

    public static WeatherDataDAO getInstance() {
        if(instance == null) instance = new WeatherDataDAO();
        return instance;
    }

    public List<WeatherData> getAll() {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<WeatherData> result = s.createQuery("from WeatherData", WeatherData.class).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }
    public WeatherData getById(Long id) {
        Session s = sfac.openSession();
        s.beginTransaction();
        WeatherData result = s.get(WeatherData.class, id);
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public List<WeatherData> getByDate(Date date){
        Session s = sfac.openSession();
        s.beginTransaction();
        List<WeatherData> result = s.createQuery("from WeatherData wd " +
                        "left join fetch wd.data_values " +
                        "left join fetch wd.data_location " +
                        "left join fetch  wd.data_delay " +
                        "left join fetch wd.data_api " +
                "where date_format(wd.inserted_at,'%Y-%m-%d %H:00:00') = date_format(:storedDate,'%Y-%m-%d %H:00:00')",
                WeatherData.class)
        .setParameter("storedDate",DateUtils.truncateDateToHour(date)).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public WeatherData insert(WeatherData object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.save(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public WeatherData persist(WeatherData object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.persist(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public List<WeatherData> persistAll(List<WeatherData> weatherDataList){
        Session s = sfac.openSession();
        //s.beginTransaction();
        for(WeatherData weatherData : weatherDataList){
            s.beginTransaction();
            s.persist(weatherData);
            s.getTransaction().commit();
        }
        //s.getTransaction().commit();
        s.close();
        return weatherDataList;
    }

    public void update(WeatherData object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.update(object);
        s.getTransaction().commit();
        s.close();
    }

    public void merge(WeatherData object) {
        Session s = sfac.openSession();
        s.merge(object);
        s.getTransaction().commit();
        s.close();
    }

    public void delete(WeatherData object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }

    public void deleteAll(){
        dataValueDAO.deleteAll();
        Session s = sfac.openSession();
        s.beginTransaction();
        s.createQuery("delete from WeatherData").executeUpdate();
        s.getTransaction().commit();
        s.close();
    }

    //============= JPA QUERIES ==============//

    /**
     * Fetches rawdata inside the database by using the JPA Criteria API
     * @param criteria criteria hashMap
     * @return a list of JPA Tuples
     * @throws IllegalArgumentException if one or more args are invalid
     */
    public List<Tuple> getModularByCriteria(HashMap<String, Object> criteria) throws IllegalArgumentException {
        logger.debug("Chosen criteria : "+criteria);

        Long nullArgs = criteria.entrySet().stream().filter(entry -> entry.getValue() == null).count();
        if(nullArgs != 3 && criteria.get("datetime") == null) throw new IllegalArgumentException();

        if(!HashMapUtils.matchesNullValues(criteria, Arrays.asList("api","city","property"), 2) || criteria.get("datetime") == null)
            throw new IllegalArgumentException("Criteria check failed: invalid number of null values");

        try (Session s = sfac.openSession()) {

            //create criteria builder
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();

            //get and join necessary tables
            final Root<WeatherData> root_wdata = query.from(WeatherData.class);
            final Join<WeatherData, Api> wdataJoinApi = root_wdata.join("data_api",JoinType.LEFT);
            final Join<WeatherData, Location> wdataJoinLocation = root_wdata.join("data_location", JoinType.LEFT);
            final Join<WeatherData, DataValue> wdataValue = root_wdata.join("data_values", JoinType.LEFT);

            //create predicate list to build the query
            List<Predicate> criteriaList = new ArrayList<>();
            List<Selection> selectList = new ArrayList<>();

            //delay predicate (set forecast to true if delay is not zero
            //compute new date with specified delay
            if((Integer) criteria.get("delay") != 0) criteria.replace("is_forecast", true);
            if(criteria.get("is_forecast").equals(true)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime((Date) criteria.get("datetime"));
                cal.add(Calendar.HOUR_OF_DAY, (Integer) criteria.get("delay"));
                criteria.replace("datetime", cal.getTime());
            }

            //we want the date for (date_from + delay), for every case
            Predicate predicate_date = builder.equal(
                    builder.function("date_format", String.class, root_wdata.get("inserted_at"), builder.literal("%Y-%m-%d %H:00:00")).as(java.util.Date.class),
                    DateUtils.truncateDateToHour((Date)criteria.get("datetime")));
            criteriaList.add(predicate_date);

            //forecast predicate
            Predicate predicate_forecast = builder.equal(
                    root_wdata.get("isForecast").as(Boolean.class), criteria.get("is_forecast"));
            criteriaList.add(predicate_forecast);

            //==== MODULAR SELECTION =================================================================================================//

            //if the api is specified
            if(criteria.get("api") != null) {
                Predicate predicate = builder.equal(wdataJoinApi.get("name"), criteria.get("api").toString());
                criteriaList.add(predicate);
            } else selectList.add(wdataJoinApi.get("name").alias(GlobalVariableRegistry.API_ALIAS));

            //if the city is specified
            if(criteria.get("city") != null) {
                Predicate predicate = builder.equal(wdataJoinLocation.get("city"), criteria.get("city").toString());
                criteriaList.add(predicate);
            } else selectList.add(wdataJoinLocation.get("city").alias(GlobalVariableRegistry.CITY_ALIAS));

            //if the property is specified
            if (criteria.get("property") != null) {
                Predicate predicate = builder.equal(wdataValue.get("property").get("name"), criteria.get("property").toString());
                criteriaList.add(predicate);
            } else {
                selectList.add(wdataValue.get("property").get("name").alias(GlobalVariableRegistry.PROPERTY_ALIAS));
            }

            selectList.add(wdataValue.get("property").get("unit").alias(GlobalVariableRegistry.UNIT_ALIAS));
            selectList.add(wdataValue.get("value_decimal").alias(GlobalVariableRegistry.VALUE_ALIAS));

            //==== END MODULAR SELECTION =============================================================================================//

            query.select(builder.tuple(selectList.toArray(new Selection[0]))).distinct(true);
            query.where(builder.and(criteriaList.toArray(new Predicate[0])));
            query.orderBy(builder.asc(root_wdata.get("inserted_at")));

            Query<Tuple> getData = s.createQuery(query);
            logger.debug("Generated SQL : "+getData.getQueryString());
            return getData.getResultList();
        } finally {
            sfac.getCurrentSession().close();
        }
    }

    /**
     * Fetches rawdata inside the database by using the JPA Criteria API
     * @param criteria criteria hashMap
     * @return a list of JPA Tuples
     * @throws IllegalArgumentException if one or more args are invalid
     */
    public List<Tuple> getValuesByCriteria(HashMap<String, Object> criteria) throws IllegalArgumentException {
        logger.debug("Chosen criteria : "+criteria);

        try (Session s = sfac.openSession()) {

            //create criteria builder
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();

            //get and join necessary tables
            final Root<WeatherData> root_wdata = query.from(WeatherData.class);
            final Join<WeatherData, Api> wdataJoinApi = root_wdata.join("data_api",JoinType.LEFT);
            final Join<WeatherData,Delay> wdataJoinDelay = root_wdata.join("data_delay",JoinType.LEFT);
            final Join<WeatherData, Location> wdataJoinLocation = root_wdata.join("data_location", JoinType.LEFT);
            final Join<WeatherData, DataValue> wdataValue = root_wdata.join("data_values", JoinType.LEFT);
            final Join<DataValue,Property> wdataJoinProperty = wdataValue.join("property",JoinType.LEFT);
            //create predicate list to build the query
            List<Predicate> criteriaList = new ArrayList<>();
            List<Selection> selectList = new ArrayList<>();

            //delay predicate (set forecast to true if delay is not zero
            //compute new date with specified delay
            if(criteria.get("delay") != null) {
                criteriaList.add(builder.equal(wdataJoinDelay.get("value"),criteria.get("delay")));
                if ((Integer) criteria.get("delay") != 0) criteria.replace("is_forecast", true);
            }
            if(criteria.get("is_forecast") != null) {
                criteriaList.add(builder.equal(root_wdata.get("isForecast"),criteria.get("is_forecast")));
            }
            if(criteria.get("city") != null){
                criteriaList.add(builder.equal(wdataJoinLocation.get("city"),criteria.get("city")));
            }
            if(criteria.get("property") != null){
                criteriaList.add(wdataJoinProperty.get("name").in(criteria.get("property")));
            }
            if(criteria.get("datetime") != null){
                criteriaList.add(builder.equal(
                        builder.function("date_format", String.class, root_wdata.get("inserted_at"), builder.literal("%Y-%m-%d %H:00:00")).as(java.util.Date.class),
                        DateUtils.truncateDateToHour((Date)criteria.get("datetime")))
                );
            }
            if(criteria.get("api") != null){
                criteriaList.add(builder.equal(wdataJoinApi.get("name"),criteria.get("api")));
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
            logger.debug("Generated SQL : "+getData.getQueryString());
            return getData.getResultList();
        } finally {
            sfac.getCurrentSession().close();
        }
    }

    public Date getLastForecast(){
        try (Session s = sfac.openSession()) {

            //create criteria builder
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();

            //get and join necessary tables
            final Root<WeatherData> root_wdata = query.from(WeatherData.class);

            List<Predicate> criteriaList = new ArrayList<>();
            List<Selection> selectList = new ArrayList<>();
            selectList.add(builder.max(root_wdata.get("inserted_at")).alias(GlobalVariableRegistry.LAST_DATETIME_ALIAS));
            criteriaList.add(builder.equal(root_wdata.get("isForecast"),true));
            query.select(builder.tuple(selectList.toArray(new Selection[0]))).distinct(true);
            query.where(builder.and(criteriaList.toArray(new Predicate[0])));
            Query<Tuple> getData = s.createQuery(query);
            return (Date) getData.getSingleResult().get(GlobalVariableRegistry.LAST_DATETIME_ALIAS);
        } finally {
            sfac.getCurrentSession().close();
        }
    }

    public Date getFirstForecastDate(){
        try (Session s = sfac.openSession()) {

            //create criteria builder
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();

            //get and join necessary tables
            final Root<WeatherData> root_wdata = query.from(WeatherData.class);

            List<Predicate> criteriaList = new ArrayList<>();
            List<Selection> selectList = new ArrayList<>();

            //get minimum forecast date
            selectList.add(builder.min(root_wdata.get("inserted_at")).alias(GlobalVariableRegistry.FIRST_DATETIME_ALIAS));
            criteriaList.add(builder.equal(root_wdata.get("isForecast"),true));

            query.select(builder.tuple(selectList.toArray(new Selection[0]))).distinct(true);
            query.where(builder.and(criteriaList.toArray(new Predicate[0])));
            Query<Tuple> getData = s.createQuery(query);
            return (Date) getData.getSingleResult().get(GlobalVariableRegistry.FIRST_DATETIME_ALIAS);
        } finally {
            sfac.getCurrentSession().close();
        }
    }
}
