package org.acrobatt.project.services.rawdata;

import org.acrobatt.project.dao.mysql.WeatherDataDAO;
import org.acrobatt.project.dao.mysql.analysis.ComparativeDataDAO;
import org.acrobatt.project.dto.RawDataBody;
import org.acrobatt.project.dto.ValuesAndIndexDataBody;
import org.acrobatt.project.dto.WidgetDataBody;
import org.acrobatt.project.services.WeatherDataCacheService;
import org.acrobatt.project.services.WidgetDataService;
import org.acrobatt.project.services.rawdata.modules.Isolated_WidgetDataModule;
import org.acrobatt.project.services.rawdata.modules.ValuesIndexDataModule;
import org.acrobatt.project.utils.DateUtils;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.HashMapUtils;
import org.acrobatt.project.utils.enums.DataOriginType;
import org.acrobatt.project.utils.enums.DataProcess;
import org.acrobatt.project.utils.struct.TableKey;
import org.acrobatt.project.utils.struct.TableValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class DataHubService {

    private static Logger logger = LogManager.getLogger(DataHubService.class);

    private static WeatherDataDAO wdao = WeatherDataDAO.getInstance();
    private static ComparativeDataDAO cdao = ComparativeDataDAO.getInstance();

    private static DataHubService instance = null;

    private WeatherDataCacheService weatherDataCacheService = WeatherDataCacheService.getInstance();
    private ComparativeDataDAO comparativeDataDAO = ComparativeDataDAO.getInstance();

    public DataHubService() throws IOException { }

    /**
     * Generates an instance of this service
     * @return an instance of the service
     */
    public static DataHubService getInstance() throws IOException {
        if(instance == null) instance = new DataHubService();
        return instance;
    }

    /**
     * Returns the requested data as a JSON payload by pulling it from the database and mapping it to JSON.
     * @param body request body
     * @param process determines the mapping process (depends on the type of data fetched)
     * @param originType determines what type of data must be fetched
     * @return the completed JSON payload
     * @throws ParseException if the provided date cannot be parsed
     * @throws NoResultException if nothing was found in the database
     */
    public JSONObject getStoredData(@NotNull RawDataBody body,
                                    @NotNull DataProcess process,
                                    @NotNull DataOriginType originType) throws ParseException, NoResultException {

        //create field map that will be passed to the DAO
        HashMap<String, Object> fieldMap = new HashMap<>();
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy_HH:mm");

        fieldMap.put("datetime", (body.datetime != null ? fmt.parse(body.datetime) : null));
        fieldMap.put("datetime_to", (body.datetime_to != null ? fmt.parse(body.datetime_to) : null));
        fieldMap.put("api", body.api);
        fieldMap.put("city", body.city);
        fieldMap.put("property", body.property);
        fieldMap.put("delay", body.delay);
        fieldMap.put("is_forecast", body.isforecast);

        DataModule module = DataModuleFactory.getDataModule(process);
        Object data = null;
        switch(originType) {
            case MODULAR:               data = wdao.getModularByCriteria(fieldMap);         break;
            case COMPARATIVE:           data = cdao.getComparativeData(fieldMap);           break;
            case DISTANCE:              data = cdao.getDistanceData(fieldMap);              break;
            case SCORE:                 data = cdao.getRawScoreData(fieldMap);              break;
            case INDEX:                 data = cdao.getRawIndexData(fieldMap);              break;
            case DETAILED_INDEX:        data = cdao.getDetailedIndexData(fieldMap);         break;
        }

        @SuppressWarnings("unchecked")
        JSONObject json = module.convertData((List<Tuple>) data);
        return json;
    }

    /**
     * Returns the requested data as a JSON payload by pulling it from the database and mapping it to JSON. For the widget only.
     * @param body widget request body
     * @return widget data in JSON format
     * @throws NoResultException if no results can be found for the calculations
     */
    public JSONObject getWidgetData(@NotNull WidgetDataBody body) throws NoResultException, IOException {
        //create field map that will be passed to the DAO
        HashMap<String, Object> fieldMap = new HashMap<>();

        fieldMap.put("api", body.api);
        fieldMap.put("city", body.city);
        fieldMap.put("delay", body.delay);

        if(body.property.isEmpty()) fieldMap.put("property", null);
        else fieldMap.put("property", body.property);

        WidgetDataService widgetDataService = WidgetDataService.getInstance();
        Isolated_WidgetDataModule module = Isolated_WidgetDataModule.getInstance();

        HashMap<TableKey<String,Integer>, TableValue<Double,String>> data = widgetDataService.getOptimalData(fieldMap);
        return module.convertData(data, fieldMap);
    }

    public JSONObject getValuesAndIndexes(@NotNull ValuesAndIndexDataBody body){
        //create field map that will be passed to the DAO
        HashMap<String, Object> fieldMap = new HashMap<>();

        fieldMap.put("property", body.property);
        fieldMap.put("city", body.city);
        fieldMap.put("delay", body.delay);
        if(!HashMapUtils.matchesNullValues(fieldMap, Arrays.asList(fieldMap.keySet().toArray()),0)){
            throw new IllegalArgumentException("Paramètre(s) invalide(s), requis: [city,property,delay]");
        }


        ValuesIndexDataModule module = ValuesIndexDataModule.getInstance();

        List<Tuple> values = weatherDataCacheService.getAndStoredNowByCriteria(fieldMap);
        Date date = DateUtils.truncateDateToHour(Date.from(Instant.now()));
        logger.info("last date : "+date);
        fieldMap.put("datetime_to",date);
        Calendar monthAgo = Calendar.getInstance();
        monthAgo.setTime(date);
        monthAgo.add(Calendar.DAY_OF_MONTH,-30);
        fieldMap.put("datetime",monthAgo.getTime());
        List<String> apis = values.stream().map((forecast)->forecast.get(GlobalVariableRegistry.API_ALIAS).toString()).distinct().collect(Collectors.toList());
        List<Integer> delays = values.stream().map((forecast)->(Integer)forecast.get(GlobalVariableRegistry.DELAY_ALIAS)).distinct().collect(Collectors.toList());
        List<String> properties = values.stream().map((forecast) -> forecast.get(GlobalVariableRegistry.PROPERTY_ALIAS).toString()).distinct().collect(Collectors.toList());

        List<Tuple> indexes = new ArrayList<>();
        fieldMap.put("apis",apis);
        for(Integer delay : delays){
            for(String property : properties){
                fieldMap.put("delay",delay);
                fieldMap.put("property",Arrays.asList(property));
                indexes.addAll(comparativeDataDAO.getIndexDataForOptimization(fieldMap));
            }
        }
        return module.convertData(values,indexes);
    }
}
