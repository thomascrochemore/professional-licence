package org.acrobatt.project.services;

import org.acrobatt.project.dao.mysql.WeatherDataDAO;
import org.acrobatt.project.dao.mysql.analysis.ComparativeDataDAO;
import org.acrobatt.project.utils.DateUtils;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.HashMapUtils;
import org.acrobatt.project.utils.struct.TableKey;
import org.acrobatt.project.utils.struct.TableValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Tuple;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class WidgetDataService {

    private static WidgetDataService instance = null;

    private static Logger logger = LogManager.getLogger(WidgetDataService.class);

    private WeatherDataDAO weatherDataDAO = WeatherDataDAO.getInstance();
    private ComparativeDataDAO comparativeDataDAO = ComparativeDataDAO.getInstance();
    private WeatherDataCacheService weatherDataCacheService = WeatherDataCacheService.getInstance();

    /**
     * Generates an instance of this service
     * @return an instance of the service
     */
    public static WidgetDataService getInstance() throws IOException {
        if(instance == null){
            instance = new WidgetDataService();
        }
        return instance;
    }

    private WidgetDataService() throws IOException {}

    /**
     * get optimal data from criteria (data pulled from the last month until the current day) :
     * @param criteria : city  --> not optional
     * if delay or property --> table 1 dimension
     * if delay and property --> unique value
     * if not delay and not property --> table 2 dimension
     * @return calculated optimal data
     */
    public HashMap<TableKey<String,Integer>,TableValue<Double,String>> getOptimalData(HashMap<String,Object> criteria){
        Date date = DateUtils.truncateDateToHour(Date.from(Instant.now()));
        logger.info("last date : "+date);
        criteria.put("datetime",date);
        logger.debug("Chosen criteria : "+criteria);
        Long nullArgs = criteria.entrySet().stream().filter(entry -> entry.getValue() == null).count();
        logger.debug(String.format("Null criteria = [%1s]",nullArgs));

        if(!HashMapUtils.hasBetweenNullValues(criteria, Arrays.asList("property", "delay"), 0, 2)
                || criteria.get("city") == null) throw new IllegalArgumentException("Criteria check failed");


        // fetch necessary forecasts data
        List<Tuple> forecasts = weatherDataCacheService.getAndStoredNowByCriteria(criteria);
        logger.debug("forecasts size = "+forecasts.size());
        List<Integer> delays = forecasts.stream().map((forecast)->(Integer)forecast.get(GlobalVariableRegistry.DELAY_ALIAS)).distinct().collect(Collectors.toList());
        List<String> properties = forecasts.stream().map((forecast) -> forecast.get(GlobalVariableRegistry.PROPERTY_ALIAS).toString()).distinct().collect(Collectors.toList());
        // invert date criteria (from a month back to current day)
        criteria.put("datetime_to",criteria.get("datetime"));
        Calendar monthAgo = Calendar.getInstance();
        monthAgo.setTime((Date) criteria.get("datetime"));
        monthAgo.add(Calendar.DAY_OF_MONTH,-30);
        criteria.put("datetime",monthAgo.getTime());

        // fetch necessary index data

        List<Tuple> indexes = new ArrayList<>();
        for(Integer delay : delays){
            for(String property : properties){
                List<String> apis = forecasts.stream().filter((forecast) ->
                    forecast.get(GlobalVariableRegistry.DELAY_ALIAS).equals(delay)
                        && forecast.get(GlobalVariableRegistry.PROPERTY_ALIAS).equals(property)
                ).map((forecast) ->
                        forecast.get(GlobalVariableRegistry.API_ALIAS).toString()
                ).distinct().collect(Collectors.toList());
                if(!apis.isEmpty()) {
                    criteria.put("apis",apis);
                    criteria.put("delay", delay);
                    criteria.put("property", Arrays.asList(property));
                    indexes.addAll(comparativeDataDAO.getIndexDataForOptimization(criteria));
                }
            }
        }
        logger.debug("indexes size = "+indexes.size());
        for(Tuple forecast : forecasts){
                logger.info(forecast.get(GlobalVariableRegistry.API_ALIAS) + " + " + forecast.get(GlobalVariableRegistry.DELAY_ALIAS) + " : " + forecast.get(GlobalVariableRegistry.VALUE_ALIAS));
        }

        // HashMap containing the intermediate values to compute optimal values (upper hand of the division)
        HashMap<TableKey<String,Integer>,Double> summingValues = new HashMap<>();
        HashMap<TableKey<String,Integer>,Double> summingIndexes = new HashMap<>();

        for(Tuple forecast : forecasts){
            TableKey<String,Integer> key = new TableKey<>(forecast.get(GlobalVariableRegistry.PROPERTY_ALIAS).toString(),(Integer) forecast.get(GlobalVariableRegistry.DELAY_ALIAS));
            if(!summingValues.containsKey(key)){
                summingValues.put(key,0.0);
            }
            Double valueForecast = ((Number) forecast.get(GlobalVariableRegistry.VALUE_ALIAS)).doubleValue();
            Double index;
            if(forecast.get(GlobalVariableRegistry.DELAY_ALIAS).equals(0)){
                index = 1.0;
                if(!summingIndexes.containsKey(key)){
                    summingIndexes.put(key,0.0);
                }
                summingIndexes.put(key,summingIndexes.get(key)+index);
            }else {
                index = indexes.stream().filter((tuple) -> tuple.get(GlobalVariableRegistry.PROPERTY_ALIAS).equals(forecast.get(GlobalVariableRegistry.PROPERTY_ALIAS))
                        && tuple.get(GlobalVariableRegistry.DELAY_ALIAS).equals(forecast.get(GlobalVariableRegistry.DELAY_ALIAS))
                        && tuple.get(GlobalVariableRegistry.API_ALIAS).equals(forecast.get(GlobalVariableRegistry.API_ALIAS))
                ).collect(Collectors.summingDouble((tuple) -> ((Number) tuple.get(GlobalVariableRegistry.SP_INDEX_ALIAS)).doubleValue()));
            }
            summingValues.put(key,summingValues.get(key) + valueForecast * index);
        }

        // HashMap containing all the indexes to compute optimal values (lower hand of the division)
        for(Tuple index : indexes){
            TableKey<String,Integer> key = new TableKey<>(index.get(GlobalVariableRegistry.PROPERTY_ALIAS).toString(),(Integer) index.get(GlobalVariableRegistry.DELAY_ALIAS));
            if(!summingIndexes.containsKey(key)){
                summingIndexes.put(key,0.0);
            }
            Double valueIndex = ((Number) index.get(GlobalVariableRegistry.SP_INDEX_ALIAS)).doubleValue();
            summingIndexes.put(key,summingIndexes.get(key) + valueIndex);
        }

        // HashMap containing the property units (for subsequent mapping)
        HashMap<TableKey<String,Integer>,String> units = new HashMap<>();
        for(Tuple forecast : forecasts){
            TableKey<String,Integer> key = new TableKey<>(forecast.get(GlobalVariableRegistry.PROPERTY_ALIAS).toString(), (Integer) forecast.get(GlobalVariableRegistry.DELAY_ALIAS));
            if(!units.containsKey(key)){
                String unit = forecast.get(GlobalVariableRegistry.UNIT_ALIAS).toString();
                units.put(key,unit);
            }
        }

        // HashMap containing the final optimal values
        HashMap<TableKey<String,Integer>,TableValue<Double,String>> optimals = new HashMap<>();
        for(Map.Entry<TableKey<String,Integer>,Double> summingValueKeySet : summingValues.entrySet()){
            Double summingIndex = summingIndexes.get(summingValueKeySet.getKey());
            String unit = units.get(summingValueKeySet.getKey());
            Double summingValue = summingValueKeySet.getValue();
            if(summingIndex != null && summingValue != null && unit != null) {
                optimals.put(summingValueKeySet.getKey(), new TableValue(summingValue / summingIndex, unit));
            }
        }
        criteria.put("datetime",date);

        logger.debug("HashMap size = "+optimals.size());
        for(Map.Entry<TableKey<String,Integer>,TableValue<Double,String>> row : optimals.entrySet()){
            logger.debug("entry = "+ row.getKey().getRowKey() + " - " + row.getKey().getColumnKey() + " - " + row.getValue().getValueValue() + " - " + row.getValue().getUnitValue());
        }
        return optimals;
    }
}
