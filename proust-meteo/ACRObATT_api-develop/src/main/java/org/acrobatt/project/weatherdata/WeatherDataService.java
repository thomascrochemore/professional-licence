package org.acrobatt.project.weatherdata;

import org.acrobatt.project.apiparsers.ApiParserService;
import org.acrobatt.project.dao.mongo.ApiDataDAO;
import org.acrobatt.project.dao.mysql.WeatherDataDAO;
import org.acrobatt.project.dao.mysql.analysis.ComparativeDataDAO;
import org.acrobatt.project.dao.mysql.cache.CacheRequestDAO;
import org.acrobatt.project.dao.mysql.cache.WeatherDataCacheDAO;
import org.acrobatt.project.model.mongo.ApiData;
import org.acrobatt.project.model.mysql.WeatherData;
import org.acrobatt.project.model.mysql.cache.CacheRequest;
import org.acrobatt.project.model.mysql.cache.WeatherDataCache;
import org.acrobatt.project.utils.DateUtils;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.acrobatt.project.weatherapi.WeatherApiService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.quartz.Scheduler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class WeatherDataService {

    private static Logger logger = LogManager.getLogger(WeatherDataService.class);

    private static WeatherDataService instance = null;

    private ApiParserService apiParserService = ApiParserService.getInstance();
    private WeatherApiService weatherApiService = WeatherApiService.getInstance();
    private WeatherDataDAO weatherDataDAO = WeatherDataDAO.getInstance();
    private ApiDataDAO apiDataDAO = ApiDataDAO.getInstance();
    private ComparativeDataDAO cfdao = ComparativeDataDAO.getInstance();
    private WeatherDataCacheDAO weatherDataCacheDAO = WeatherDataCacheDAO.getInstance();
    private CacheRequestDAO cacheRequestDAO = CacheRequestDAO.getInstance();
    private Scheduler scheduler;

    /**
     * Generates an instance of this service
     * @return an instance of the service
     */
    public static WeatherDataService getInstance() throws IOException {
        if(instance == null){
            instance = new WeatherDataService();
        }
        return instance;
    }

    private WeatherDataService() throws IOException {
        logger.info("Built service instance");
    }

    /**
     * Backs up all the raw data stored in MongoDB inside the MySQL database
     * @throws IOException if normalization fails
     */
    public void backupApiDataToWeatherData() throws IOException {
        HibernateUtils.refreshSessionFactory();
        //MongoClientFactory.refreshConnection();

        weatherDataDAO.deleteAll();
        int page = 0;
        final int PAGE_SIZE = 100;
        List<ApiData> apiDataList;
        do{
            apiDataList = apiDataDAO.findPage(page++,PAGE_SIZE);
            logger.info("Parsing page size of "+apiDataList.size()+"...");
            for(ApiData apiData : apiDataList) {
                if(apiParserService.apiParserExists(apiData.getApi())) {
                    try {
                        WeatherData weatherData = null;
                        switch (apiData.getType()) {
                            case RESULT:
                                weatherData = apiParserService.parseJsonResultToWeatherData(apiData);
                                break;
                            case FORECAST:
                                weatherData = apiParserService.parseJsonForecastToWeathData(apiData);
                                break;
                        }
                        if (weatherData != null && !weatherData.isEmpty()) {
                            weatherDataDAO.persist(weatherData);
                        }
                    } catch (Throwable e) {
                        // in case of error, add other data
                        logger.error(e.getMessage());
                    }
                }
            }
        }while(!apiDataList.isEmpty());

        //call stored procedure to backup stats_avg_rt data
        logger.info("Backing up data...");
        cfdao.backupEntries();
        logger.info("Done");
        cacheRequestDAO.deleteAll();
        weatherDataCacheDAO.deleteAll();
        Date last = weatherDataDAO.getLastForecast();
        List<WeatherData> weatherDataLast = weatherDataDAO.getByDate(last);
        List<WeatherDataCache> weatherDataCaches = weatherDataLast.stream().filter((weatherData -> !weatherData.isEmpty())).map((WeatherDataCache::new)).collect(Collectors.toList());
        List<CacheRequest> cacheRequests = weatherDataLast.stream().map((wd)->new CacheRequest(wd.getData_api(),wd.getData_location(),wd.getData_delay(),DateUtils.truncateDateToHour(wd.getInserted_at()))).collect(Collectors.toList());
        cacheRequestDAO.persistAll(cacheRequests);
        weatherDataCacheDAO.persistAll(weatherDataCaches);
    }

    /**
     * Computes a new set of raw data (every six hours) inside the MySQL and MongoDB database
     * @throws IOException if normalization fails
     */
    public void computeApisData() throws IOException, JSONException {
        HibernateUtils.refreshSessionFactory();
        List<WeatherData> weatherDataList = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        //now.set(Calendar.HOUR_OF_DAY,6);
        List<ApiData> apiDataList = weatherApiService.getAllApiData(now.getTime());
        apiDataDAO.insertAll(apiDataList);
        for(ApiData apiData : apiDataList){
            try{
                if(apiParserService.apiParserExists(apiData.getApi())) {
                    WeatherData weatherData = null;
                    switch (apiData.getType()) {
                        case RESULT:
                            weatherData = apiParserService.parseJsonResultToWeatherData(apiData);
                            break;
                        case FORECAST:
                            weatherData = apiParserService.parseJsonForecastToWeathData(apiData);
                            break;
                    }
                    if (weatherData != null && !weatherData.isEmpty()) {
                        weatherDataList.add(weatherData);
                    }
                }
            }catch (Throwable e){
                logger.warn(e.getMessage());
            }
        }
        logger.debug("WeatherData list size = " + weatherDataList.size());
        weatherDataDAO.persistAll(weatherDataList);
        List<WeatherDataCache> weatherDataCaches = weatherDataList.stream().filter((weatherData)->!weatherData.isEmpty()).map((WeatherDataCache::new)).collect(Collectors.toList());
        weatherDataCacheDAO.deleteAll();
        cacheRequestDAO.deleteAll();
        List<CacheRequest> cacheRequests = weatherDataList.stream().map((wd)->new CacheRequest(wd.getData_api(),wd.getData_location(),wd.getData_delay(), DateUtils.truncateDateToHour(wd.getInserted_at()))).collect(Collectors.toList());
        cacheRequestDAO.persistAll(cacheRequests);
        weatherDataCacheDAO.persistAll(weatherDataCaches);


        //call stored procedure to backup stats_avg_rt data
        logger.info("Computing data...");
        cfdao.computeEntries(now.getTime());
        logger.info("Done");
    }
}
