package org.acrobatt.project.services;

import org.acrobatt.project.apiparsers.ApiParserService;
import org.acrobatt.project.dao.mysql.ApiDAO;
import org.acrobatt.project.dao.mysql.DelayDAO;
import org.acrobatt.project.dao.mysql.LocationDAO;
import org.acrobatt.project.dao.mysql.PropertyDAO;
import org.acrobatt.project.dao.mysql.cache.CacheRequestDAO;
import org.acrobatt.project.dao.mysql.cache.WeatherDataCacheDAO;
import org.acrobatt.project.model.mongo.ApiData;
import org.acrobatt.project.model.mysql.*;
import org.acrobatt.project.model.mysql.cache.CacheRequest;
import org.acrobatt.project.model.mysql.cache.WeatherDataCache;
import org.acrobatt.project.utils.DateUtils;
import org.acrobatt.project.weatherapi.WeatherApiService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Tuple;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class WeatherDataCacheService {
    private static WeatherDataCacheService instance = null;

    private CacheRequestDAO cacheRequestDAO = CacheRequestDAO.getInstance();
    private WeatherDataCacheDAO weatherDataCacheDAO = WeatherDataCacheDAO.getInstance();
    private WeatherApiService weatherApiService = WeatherApiService.getInstance();
    private ApiParserService apiParserService = ApiParserService.getInstance();
    private LocationDAO locationDAO = LocationDAO.getInstance();
    private ApiDAO apiDAO = ApiDAO.getInstance();
    private DelayDAO delayDAO = DelayDAO.getInstance();

    private Logger logger = LogManager.getLogger(this.getClass());

    public WeatherDataCacheService() throws IOException {
    }

    public static WeatherDataCacheService getInstance() throws IOException {
        if(instance == null) instance = new WeatherDataCacheService();
        return instance;
    }

    public Optional<WeatherDataCache> getAndStoreNowData(Api api,Location location,Delay delay){
        Date now = DateUtils.truncateDateToHour(Date.from(Instant.now()));
        CacheRequest request = new CacheRequest(api,location,delay,now);
        Optional<WeatherDataCache> weatherDataCache = Optional.empty();
        if(!cacheRequestDAO.requestCacheExists(request)){
            try {
                if(apiParserService.apiParserExists(api.getName())){
                    ApiData apiData = weatherApiService.getApiData(api, location, delay, now);
                    WeatherData weatherData = null;
                    switch (apiData.getType()) {
                        case RESULT:
                            weatherData = apiParserService.parseJsonResultToWeatherData(apiData);
                            break;
                        case FORECAST:
                            weatherData = apiParserService.parseJsonForecastToWeathData(apiData);
                            break;
                    }
                    cacheRequestDAO.persist(request);
                    if(weatherData == null || weatherData.isEmpty()){
                        return Optional.empty();
                    }
                    return Optional.of(weatherDataCacheDAO.persist(new WeatherDataCache(weatherData)));
                }
            }catch (Throwable e){
                logger.error(e.getMessage());
                return Optional.empty();
            }
        }
        return weatherDataCacheDAO.getByApiAndLocationAndDelayAndInsertedAt(api,location,delay,now);
    }

    public List<Tuple> getAndStoredNowByCriteria(HashMap<String,Object> criteria){
        criteria.put("datetime",DateUtils.truncateDateToHour(Date.from(Instant.now())));
        List<Api> apis;
        if(criteria.get("api") != null){
            apis = new ArrayList<>();
            apis.add(apiDAO.getByNameWithKeysAndUrl(criteria.get("api").toString()));
        }else{
            apis = apiDAO.getAllWithKeysAndUrls();
        }
        List<Location> locations;

        if(criteria.get("city") != null){
            locations = locationDAO.getByCity(criteria.get("city").toString());
        }else{
            locations = locationDAO.getAll();
        }

        List<Delay> delays;

        if(criteria.get("delay") != null){
            delays = new ArrayList<>();
            delays.add(delayDAO.getByDuration((int) criteria.get("delay")));
        }else{
            delays = delayDAO.getAll();
        }
        List<KeyRequest> requests = new ArrayList<>();
        for(Api api : apis){
            for(Location location  : locations){
                for(Delay delay : delays){
                    requests.add(new KeyRequest(api,location,delay));
                }
            }
        }
        requests.stream().parallel().forEach((req ->
                getAndStoreNowData(req.getApi(),req.getLocation(),req.getDelay())
        ));
        return weatherDataCacheDAO.getValuesByCriteria(criteria);
    }
    private static class KeyRequest
    {
        private Api api;
        private Location location;
        private Delay delay;

        public KeyRequest(Api api,Location location,Delay delay){
            setApi(api);
            setLocation(location);
            setDelay(delay);
        }

        public Api getApi() {
            return api;
        }

        public void setApi(Api api) {
            this.api = api;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public Delay getDelay() {
            return delay;
        }

        public void setDelay(Delay delay) {
            this.delay = delay;
        }
    }
}
