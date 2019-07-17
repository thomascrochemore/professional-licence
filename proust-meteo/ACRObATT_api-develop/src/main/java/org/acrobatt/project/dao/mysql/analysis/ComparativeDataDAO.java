package org.acrobatt.project.dao.mysql.analysis;

import org.acrobatt.project.dao.mysql.WeatherDataDAO;
import org.acrobatt.project.model.mysql.Delay;
import org.acrobatt.project.model.mysql.Property;
import org.acrobatt.project.model.mysql.analysis.ComparativeData;
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

public class ComparativeDataDAO {
    private static Logger logger = LogManager.getLogger(ComparativeDataDAO.class);

    private static ComparativeDataDAO instance;
    private SessionFactory sfac = HibernateUtils.getSessionFactory();

    private static WeatherDataDAO wdao = WeatherDataDAO.getInstance();

    public static ComparativeDataDAO getInstance() {
        if(instance == null) instance = new ComparativeDataDAO();
        return instance;
    }

    public List<ComparativeData> getAll() {
        Session s = sfac.openSession();
        s.beginTransaction();
        List<ComparativeData> result = s.createQuery("from ComparativeData", ComparativeData.class).list();
        s.getTransaction().commit();
        s.close();
        return result;
    }
    public ComparativeData getById(Long id) {
        Session s = sfac.openSession();
        s.beginTransaction();
        ComparativeData result = s.get(ComparativeData.class, id);
        s.getTransaction().commit();
        s.close();
        return result;
    }

    public ComparativeData insert(ComparativeData object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.save(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public ComparativeData persist(ComparativeData object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.persist(object);
        s.getTransaction().commit();
        s.close();
        return object;
    }

    public List<ComparativeData> persistAll(List<ComparativeData> weatherDataList){
        Session s = sfac.openSession();
        for(ComparativeData weatherData : weatherDataList){
            s.beginTransaction();
            s.persist(weatherData);
            s.getTransaction().commit();
        }
        s.close();
        return weatherDataList;
    }

    public void update(ComparativeData object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.update(object);
        s.getTransaction().commit();
        s.close();
    }

    public void merge(ComparativeData object) {
        Session s = sfac.openSession();
        s.merge(object);
        s.getTransaction().commit();
        s.close();
    }

    public void delete(ComparativeData object) {
        Session s = sfac.openSession();
        s.beginTransaction();
        s.delete(object);
        s.getTransaction().commit();
        s.close();
    }

    public void deleteAll(){
        Session s = sfac.openSession();
        s.beginTransaction();
        s.createQuery("delete from ComparativeData").executeUpdate();
        s.getTransaction().commit();
        s.close();
    }

    public void backupEntries() {
        Session s = sfac.openSession();
        s.beginTransaction();
        Query query = s.createNativeQuery("CALL stats_proc_backup()");
        query.executeUpdate();
        s.getTransaction().commit();
        s.close();
    }

    public void computeEntries(Date datetime) {
        Session s = sfac.openSession();
        s.beginTransaction();
        Query query = s.createNativeQuery("CALL stats_proc_compute(:dt)")
                .setParameter("dt", datetime);
        query.executeUpdate();
        s.getTransaction().commit();
        s.close();
    }

    //===================================================================================================//

    /**
     * Fetches computed raw data inside the database (comparative between real-time averages and forecasts).
     * This function returns a list of comparisons
     * @param criteria criteria hashMap
     * @return a list of JPA Tuples
     * @throws IllegalArgumentException if one or more args are invalid
     */
    public List<Tuple> getComparativeData(HashMap<String, Object> criteria) throws IllegalArgumentException {
        logger.debug("Chosen criteria : "+criteria);

        if(!HashMapUtils.matchesNullValues(criteria, Arrays.asList(criteria.keySet().toArray()), 1) && criteria.get("api") != null)
            throw new IllegalArgumentException("Criteria check failed: invalid number of null values");

        try (Session s = sfac.openSession()){
            //create criteria builder
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();

            //get and join necessary tables
            final Root<ComparativeData> root_cmpr = query.from(ComparativeData.class);
            final Root<Property> root_prop = query.from(Property.class);

            //forecast predicate
            Predicate predicate = builder.and(
                    builder.equal(root_cmpr.get("location"), criteria.get("city")),
                    builder.equal(root_cmpr.get("delay"), criteria.get("delay")),
                    builder.equal(root_cmpr.get("prop"), criteria.get("property")),
                    builder.equal(root_cmpr.get("prop"), root_prop.get("name")),
                    builder.between(root_cmpr.get("date_req"), (Date) criteria.get("datetime"), (Date) criteria.get("datetime_to"))
            );

            List<Selection> selectList = Arrays.asList(
                    root_cmpr.get("date_req").alias(GlobalVariableRegistry.DATETIME_ALIAS),
                    root_prop.get("unit").alias(GlobalVariableRegistry.UNIT_ALIAS),
                    root_cmpr.get("value_fcst").alias(GlobalVariableRegistry.SP_FORECAST_ALIAS),
                    root_cmpr.get("value_rt").alias(GlobalVariableRegistry.SP_REALTIME_ALIAS));

            query.select(builder.tuple(selectList.toArray(new Selection[0]))).distinct(true);
            query.where(predicate);
            query.orderBy(builder.asc(root_cmpr.get("date_req")));

            Query<Tuple> getData = s.createQuery(query);
            logger.debug(getData.getQueryString());
            return getData.getResultList();
        } finally {
            sfac.getCurrentSession().close();
        }
    }

    /**
     * Fetches raw distance data inside the database (between forecast and real-time)
     * @param criteria criteria hashMap
     * @return a list of JPA Tuples
     * @throws IllegalArgumentException if one or more args are invalid
     */
    public List<Tuple> getDistanceData(HashMap<String, Object> criteria) throws IllegalArgumentException {
        logger.debug("Chosen criteria : "+criteria);
        if(!HashMapUtils.matchesNullValues(criteria, Arrays.asList(criteria.keySet().toArray()), 0))
            throw new IllegalArgumentException("Criteria check failed: invalid number of null values");

        try (Session s = sfac.openSession()) {

            //create criteria builder
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();

            //get and join necessary tables
            final Root<ComparativeData> root_cmpr = query.from(ComparativeData.class);

            //forecast predicate
            Predicate predicate = builder.and(
                    builder.equal(root_cmpr.get("api"), criteria.get("api")),
                    builder.equal(root_cmpr.get("location"), criteria.get("city")),
                    builder.equal(root_cmpr.get("prop"), criteria.get("property")),
                    builder.equal(root_cmpr.get("delay"), criteria.get("delay")),
                    builder.between(root_cmpr.get("date_req"), (Date) criteria.get("datetime"), (Date) criteria.get("datetime_to"))
            );

            List<Selection> selectList = Arrays.asList(
                    root_cmpr.get("date_req").alias(GlobalVariableRegistry.DATETIME_ALIAS),
                    root_cmpr.get("value_fcst").alias(GlobalVariableRegistry.SP_FORECAST_ALIAS),
                    root_cmpr.get("value_rt").alias(GlobalVariableRegistry.SP_REALTIME_ALIAS),
                    root_cmpr.get("distance").alias(GlobalVariableRegistry.SP_DISTANCE_ALIAS));

            query.select(builder.tuple(selectList.toArray(new Selection[0]))).distinct(true);
            query.where(predicate);
            query.orderBy(builder.asc(root_cmpr.get("date_req")));

            Query<Tuple> getData = s.createQuery(query);
            logger.debug(getData.getQueryString());
            return getData.getResultList();
        } finally {
            sfac.getCurrentSession().close();
        }
    }

    /**
     * Fetches raw scoring data, querying the distance
     * @param criteria criteria hashMap
     * @return a list of JPA Tuples
     * @throws IllegalArgumentException if one or more args are invalid
     */
    public List<Tuple> getRawScoreData(HashMap<String, Object> criteria) throws IllegalArgumentException {
        logger.debug("Chosen criteria : "+criteria);
        if (
                !HashMapUtils.hasMaxNullValues(criteria, Arrays.asList("api","city","property", "delay"), 1)
                || (HashMapUtils.matchesNullValues(
                        criteria, Arrays.asList("api","city","property", "delay"), 0)
                        && new Integer(criteria.get("delay").toString()) != 0)
        ) throw new IllegalArgumentException("Criteria check failed: invalid number of null values");

        try (Session s = sfac.openSession()) {

            //create criteria builder
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();

            //get and join necessary tables
            final Root<ComparativeData> root_cmpr = query.from(ComparativeData.class);

            // create lists to build the query
            List<Selection> selectList = new ArrayList<>();
            List<Expression<?>> groupByList = new ArrayList<>();
            List<Predicate> predicateList = new ArrayList<>();


            //==== MODULAR SELECTION =================================================================================================//

            if(criteria.get("api") != null) {
                predicateList.add(builder.equal(root_cmpr.get("api"), criteria.get("api")));
            } else {
                selectList.add(root_cmpr.get("api").alias(GlobalVariableRegistry.API_ALIAS));
                groupByList.add(root_cmpr.get("api"));
            }

            if(criteria.get("city") != null) {
                predicateList.add(builder.equal(root_cmpr.get("location"), criteria.get("city")));
            } else {
                selectList.add(root_cmpr.get("location").alias(GlobalVariableRegistry.CITY_ALIAS));
                groupByList.add(root_cmpr.get("location"));
            }

            if(criteria.get("property") != null){
                predicateList.add(builder.equal(root_cmpr.get("prop"), criteria.get("property")));
            } else {
                selectList.add(root_cmpr.get("prop").alias(GlobalVariableRegistry.PROPERTY_ALIAS));
                groupByList.add(root_cmpr.get("prop"));
            }

            if(new Integer(criteria.get("delay").toString()) != 0){
                predicateList.add(builder.equal(root_cmpr.get("delay"),criteria.get("delay")));
            } else {
                selectList.add(root_cmpr.get("delay").alias(GlobalVariableRegistry.DELAY_ALIAS));
                groupByList.add(root_cmpr.get("delay"));
            }

            // Add (1 / avg(distance)) to the select list (= score)
            selectList.add(builder.coalesce(builder.quot(1,(builder.avg(root_cmpr.get("distance")))),0.01).alias(GlobalVariableRegistry.SP_SCORING_ALIAS));

            // Date interval for the score
            predicateList.add(builder.between(root_cmpr.get("date_req"), (Date) criteria.get("datetime"), (Date) criteria.get("datetime_to")));

            //==== END MODULAR SELECTION =============================================================================================//

            query.select(builder.tuple(selectList.toArray(new Selection[0]))).distinct(true);
            query.where(builder.and(predicateList.toArray(new Predicate[0])));
            query.groupBy(groupByList);
            Query<Tuple> getData = s.createQuery(query);
            logger.debug(getData.getQueryString());
            return getData.getResultList();
        } finally {
            sfac.getCurrentSession().close();
        }
    }

    /**
     * Fetches raw index data, querying the distance
     * @param criteria criteria hashMap
     * @return a list of JPA Tuples
     * @throws IllegalArgumentException if one or more args are invalid
     */
    public List<Tuple> getRawIndexData(HashMap<String, Object> criteria) throws IllegalArgumentException {
        logger.debug("Chosen criteria : "+criteria);
        if (
                !HashMapUtils.hasMaxNullValues(criteria, Arrays.asList("api","city","property", "delay"), 1)
                || (HashMapUtils.matchesNullValues(
                        criteria, Arrays.asList("api","city","property", "delay"), 0)
                        && new Integer(criteria.get("delay").toString()) != 0)
        ) throw new IllegalArgumentException("Criteria check failed: invalid number of null values");

        try (Session s = sfac.openSession()) {

            //search in the entire database until current server date & time
            Calendar current = Calendar.getInstance();
            criteria.put("datetime", DateUtils.truncateDateToHour(wdao.getFirstForecastDate()));
            criteria.put("datetime_to", DateUtils.truncateDateToHour(current.getTime()));

            List<Tuple> scores = getRawScoreData(criteria);
            Double sumScore = scores.stream().mapToDouble((tuple) -> (Double) tuple.get(GlobalVariableRegistry.SP_SCORING_ALIAS)).sum();
            logger.info("Calculated score sum = "+sumScore);

            //create criteria builder
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();

            //get and join necessary tables
            final Root<ComparativeData> root_cmpr = query.from(ComparativeData.class);

            // create lists to build the query
            List<Selection> selectList = new ArrayList<>();
            List<Expression<?>> groupByList = new ArrayList<>();

            Predicate predicate = builder.and(
                    builder.lessThan(root_cmpr.get("date_req"), (Date) criteria.get("datetime_to"))
            );

            //==== MODULAR SELECTION =================================================================================================//

            if(criteria.get("api") != null) {
                predicate = builder.and(predicate, builder.equal(root_cmpr.get("api"), criteria.get("api")));
            } else {
                selectList.add(root_cmpr.get("api").alias(GlobalVariableRegistry.API_ALIAS));
                groupByList.add(root_cmpr.get("api"));
            }

            if(criteria.get("city") != null) {
                predicate = builder.and(predicate, builder.equal(root_cmpr.get("location"), criteria.get("city")));
            } else {
                selectList.add(root_cmpr.get("location").alias(GlobalVariableRegistry.CITY_ALIAS));
                groupByList.add(root_cmpr.get("location"));
            }

            if(criteria.get("property") != null){
                predicate = builder.and(predicate, builder.equal(root_cmpr.get("prop"), criteria.get("property")));
            } else {
                selectList.add(root_cmpr.get("prop").alias(GlobalVariableRegistry.PROPERTY_ALIAS));
                groupByList.add(root_cmpr.get("prop"));
            }

            if(new Integer(criteria.get("delay").toString()) != 0){
                predicate = builder.and(predicate, builder.equal(root_cmpr.get("delay"),criteria.get("delay")));
            } else {
                selectList.add(root_cmpr.get("delay").alias(GlobalVariableRegistry.DELAY_ALIAS));
                groupByList.add(root_cmpr.get("delay"));
            }

            //==== END MODULAR SELECTION =============================================================================================//

            selectList.addAll(Arrays.asList(
                    builder.quot(builder.coalesce(builder.quot(1,(builder.avg(root_cmpr.get("distance")))),0.01),sumScore).alias(GlobalVariableRegistry.SP_INDEX_ALIAS))
            );

            query.select(builder.tuple(selectList.toArray(new Selection[0]))).distinct(true);
            query.where(predicate);
            query.groupBy(groupByList);
            query.orderBy(builder.desc(builder.quot(builder.coalesce(builder.quot(1,(builder.avg(root_cmpr.get("distance")))),0.01),sumScore)));

            Query<Tuple> getData = s.createQuery(query);
            logger.debug(getData.getQueryString());
            return getData.getResultList();
        } finally {
            sfac.getCurrentSession().close();
        }
    }

    /**
     * Fetches detailed index data (with score and score sum), querying the distance
     * @param criteria criteria hashMap
     * @return a list of JPA Tuples
     * @throws IllegalArgumentException if one or more args are invalid
     */
    public List<Tuple> getDetailedIndexData(HashMap<String, Object> criteria) throws IllegalArgumentException {
        logger.debug("Chosen criteria : "+criteria);
        Long nullArgs = criteria.entrySet().stream().filter(entry -> entry.getValue() == null).count();
        logger.debug(String.format("Null criteria = [%1s]",nullArgs));
        if (
                !HashMapUtils.hasMaxNullValues(criteria, Arrays.asList("api","city","property", "delay"), 1)
                        || (HashMapUtils.matchesNullValues(
                        criteria, Arrays.asList("api","city","property", "delay"), 0)
                        && new Integer(criteria.get("delay").toString()) != 0)
                ) throw new IllegalArgumentException("Criteria check failed: invalid number of null values");

        try (Session s = sfac.openSession()) {

            //search in the entire database until current server date & time
            Calendar current = Calendar.getInstance();
            criteria.put("datetime", DateUtils.truncateDateToHour(wdao.getFirstForecastDate()));
            criteria.put("datetime_to", DateUtils.truncateDateToHour(current.getTime()));

            List<Tuple> scores = getRawScoreData(criteria);
            Double sumScore = scores.stream().mapToDouble((tuple) -> (Double) tuple.get(GlobalVariableRegistry.SP_SCORING_ALIAS)).sum();
            logger.info("Calculated score sum = "+sumScore);

            //create criteria builder
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();

            //get and join necessary tables
            final Root<ComparativeData> root_cmpr = query.from(ComparativeData.class);

            // create lists to build the query
            List<Selection> selectList = new ArrayList<>();
            List<Expression<?>> groupByList = new ArrayList<>();

            Predicate predicate = builder.and(
                    builder.lessThan(root_cmpr.get("date_req"), (Date) criteria.get("datetime_to"))
            );

            //==== MODULAR SELECTION =================================================================================================//

            if(criteria.get("api") != null) {
                predicate = builder.and(predicate, builder.equal(root_cmpr.get("api"), criteria.get("api")));
            } else {
                selectList.add(root_cmpr.get("api").alias(GlobalVariableRegistry.API_ALIAS));
                groupByList.add(root_cmpr.get("api"));
            }

            if(criteria.get("city") != null) {
                predicate = builder.and(predicate, builder.equal(root_cmpr.get("location"), criteria.get("city")));
            } else {
                selectList.add(root_cmpr.get("location").alias(GlobalVariableRegistry.CITY_ALIAS));
                groupByList.add(root_cmpr.get("location"));
            }

            if(criteria.get("property") != null){
                predicate = builder.and(predicate, builder.equal(root_cmpr.get("prop"), criteria.get("property")));
            } else {
                selectList.add(root_cmpr.get("prop").alias(GlobalVariableRegistry.PROPERTY_ALIAS));
                groupByList.add(root_cmpr.get("prop"));
            }

            if(new Integer(criteria.get("delay").toString()) != 0){
                predicate = builder.and(predicate, builder.equal(root_cmpr.get("delay"),criteria.get("delay")));
            } else {
                selectList.add(root_cmpr.get("delay").alias(GlobalVariableRegistry.DELAY_ALIAS));
                groupByList.add(root_cmpr.get("delay"));
            }

            // Add (1 / avg(distance)) to the select list (= score)
            selectList.add(builder.coalesce(builder.quot(1,(builder.avg(root_cmpr.get("distance")))),0.01).alias(GlobalVariableRegistry.SP_SCORING_ALIAS));

            //==== END MODULAR SELECTION =============================================================================================//

            selectList.addAll(Arrays.asList(
                    builder.literal(sumScore).alias(GlobalVariableRegistry.SP_SUM_SCORE_ALIAS),
                    builder.quot(builder.coalesce(builder.quot(1,(builder.avg(root_cmpr.get("distance")))),0.01),sumScore).alias(GlobalVariableRegistry.SP_INDEX_ALIAS))
            );

            query.select(builder.tuple(selectList.toArray(new Selection[0]))).distinct(true);
            query.where(predicate);
            query.groupBy(groupByList);
            query.orderBy(builder.desc(builder.quot(builder.coalesce(builder.quot(1,(builder.avg(root_cmpr.get("distance")))),0.01),sumScore)));

            Query<Tuple> getData = s.createQuery(query);
            logger.debug(getData.getQueryString());
            return getData.getResultList();
        } finally {
            sfac.getCurrentSession().close();
        }
    }

    //========== OPTIMIZATION ==============================================================================================

    /**
     * Fetches scoring data, querying the distance (FOR OPTIMIZATION ONLY)
     * This function only returns one comparison
     * @param criteria criteria hashMap
     * @return a list of JPA Tuples
     * @throws IllegalArgumentException if one or more args are invalid
     */
    public List<Tuple> getScoreDataForOptimization(HashMap<String, Object> criteria) throws IllegalArgumentException {
        logger.debug("Chosen criteria : "+criteria);
        Long nullArgs = criteria.entrySet().stream().filter(entry -> entry.getValue() == null).count();
        logger.debug(String.format("Null criteria = [%1s]",nullArgs));
        if(!HashMapUtils.hasBetweenNullValues(criteria, Arrays.asList("city","property","delay"), 0, 3)
                || !HashMapUtils.matchesNullValues(criteria, Arrays.asList("datetime","datetime_to"), 0))
            throw new IllegalArgumentException("Criteria check failed");

        try (Session s = sfac.openSession()) {

            //create criteria builder
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();

            //get and join necessary tables
            final Root<ComparativeData> root_cmpr = query.from(ComparativeData.class);

            Predicate predicate = builder.and(
                    builder.between(root_cmpr.get("date_req"), (Date) criteria.get("datetime"), (Date) criteria.get("datetime_to"))
            );

            if(criteria.get("city") != null) {
                predicate = builder.and(predicate, builder.equal(root_cmpr.get("location"), criteria.get("city")));
            }
            if(criteria.get("property") != null){
                predicate = builder.and(predicate, root_cmpr.get("prop").in(criteria.get("property")));
            }
            if(criteria.get("delay") != null){
                predicate = builder.and(predicate, builder.equal(root_cmpr.get("delay"),criteria.get("delay")));
            }
            if(criteria.get("apis") != null && !((List) criteria.get("apis")).isEmpty()) {
                predicate = builder.and(predicate, root_cmpr.get("api").in(criteria.get("apis")));
            }
            List<Selection> selectList = Arrays.asList(
                    root_cmpr.get("api").alias(GlobalVariableRegistry.API_ALIAS),
                    root_cmpr.get("prop").alias(GlobalVariableRegistry.PROPERTY_ALIAS),
                    root_cmpr.get("delay").alias(GlobalVariableRegistry.DELAY_ALIAS),
                    builder.coalesce(builder.quot(1,(round(builder,builder.avg(root_cmpr.get("distance")),3))),1000).alias(GlobalVariableRegistry.SP_SCORING_ALIAS));

            List<Expression<?>> groupByList = Arrays.asList(
                    root_cmpr.get("api"),
                    root_cmpr.get("prop"),
                    root_cmpr.get("delay")
            );

            query.select(builder.tuple(selectList.toArray(new Selection[0]))).distinct(true);
            query.where(predicate);
            query.groupBy(groupByList);
            Query<Tuple> getData = s.createQuery(query);
            logger.debug(getData.getQueryString());
            return getData.getResultList();
        } finally {
            sfac.getCurrentSession().close();
        }
    }

    // https://stackoverflow.com/questions/23369306/round-to-2-decimal-places-in-hibernate-query-language
    private Expression<? extends Number> round(CriteriaBuilder builder,Expression<? extends Number> path,Integer precision){
        return builder.prod(
                builder.quot(
                    builder.function("floor",path.getJavaType(),
                            builder.sum(
                                    builder.prod(
                                            builder.abs(path),
                                            builder.literal(Math.pow(10,precision))
                                    ),
                                    builder.literal(0.5)
                            )
                    ),
                     builder.literal(Math.pow(10,precision))
                ),
                builder.function("sign",path.getJavaType(),path)
        );
    }

    /**
     * Fetches index data, querying the distance (FOR OPTIMIZATION ONLY)
     * This function only returns one comparison
     * @param criteria criteria hashMap
     * @return a list of JPA Tuples
     * @throws IllegalArgumentException if one or more args are invalid
     */
    public List<Tuple> getIndexDataForOptimization(HashMap<String, Object> criteria) throws IllegalArgumentException {
        logger.debug("Chosen criteria : "+criteria);
        Long nullArgs = criteria.entrySet().stream().filter(entry -> entry.getValue() == null).count();
        logger.debug(String.format("Null criteria = [%1s]",nullArgs));

        if(!HashMapUtils.hasBetweenNullValues(criteria, Arrays.asList("city","property","delay"), 0, 3)
                || !HashMapUtils.matchesNullValues(criteria, Arrays.asList("datetime","datetime_to"), 0))
            throw new IllegalArgumentException("Criteria check failed");

        try (Session s = sfac.openSession()) {

            List<Tuple> scores = getScoreDataForOptimization(criteria);
            Double sumScore = scores.stream().mapToDouble((tuple) -> (Double) tuple.get(GlobalVariableRegistry.SP_SCORING_ALIAS)).sum();
            logger.info("Calculated score sum = "+sumScore);

            //create criteria builder
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();

            //get and join necessary tables
            final Root<ComparativeData> root_cmpr = query.from(ComparativeData.class);

            Predicate predicate = builder.and(
                    builder.between(root_cmpr.get("date_req"), (Date) criteria.get("datetime"), (Date) criteria.get("datetime_to"))
            );

        if(criteria.get("city") != null) {
            predicate = builder.and(predicate, builder.equal(root_cmpr.get("location"), criteria.get("city")));
        }
        if(criteria.get("property") != null){
            predicate = builder.and(predicate, root_cmpr.get("prop").in(criteria.get("property")));
        }
        if(criteria.get("delay") != null){
            predicate = builder.and(predicate, builder.equal(root_cmpr.get("delay"),criteria.get("delay")));
        }
        if(criteria.get("apis") != null && !((List) criteria.get("apis")).isEmpty()){
            predicate = builder.and(predicate,root_cmpr.get("api").in(criteria.get("apis")));
        }

        List<Selection> selectList = Arrays.asList(
                root_cmpr.get("api").alias(GlobalVariableRegistry.API_ALIAS),
                root_cmpr.get("prop").alias(GlobalVariableRegistry.PROPERTY_ALIAS),
                root_cmpr.get("delay").alias(GlobalVariableRegistry.DELAY_ALIAS),
                builder.literal(sumScore).alias(GlobalVariableRegistry.SP_SUM_SCORE_ALIAS),
                builder.quot(builder.coalesce(builder.quot(1,(round(builder,builder.avg(root_cmpr.get("distance")),3))),1000),sumScore).alias(GlobalVariableRegistry.SP_INDEX_ALIAS));

            List<Expression<?>> groupByList = Arrays.asList(
                    root_cmpr.get("api"),
                    root_cmpr.get("prop"),
                    root_cmpr.get("delay")
            );

            query.select(builder.tuple(selectList.toArray(new Selection[0]))).distinct(true);
            query.where(predicate);
            query.groupBy(groupByList);
            Query<Tuple> getData = s.createQuery(query);
            logger.debug(getData.getQueryString());
            return getData.getResultList();
        } finally {
            sfac.getCurrentSession().close();
        }
    }
}
