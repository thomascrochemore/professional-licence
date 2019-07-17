package org.acrobatt.project.weatherapi;

import org.acrobatt.project.dao.mysql.ApiDAO;
import org.acrobatt.project.dao.mysql.DelayDAO;
import org.acrobatt.project.dao.mysql.LocationDAO;
import org.acrobatt.project.model.mongo.ApiData;
import org.acrobatt.project.model.mysql.Api;
import org.acrobatt.project.model.mysql.Delay;
import org.acrobatt.project.model.mysql.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.message.GZipEncoder;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.IOException;
import java.util.*;

public class WeatherApiService {

    private static Logger logger = LogManager.getLogger(URLParser.class);

    private static WeatherApiService instance;

    private ApiDAO apiDAO = ApiDAO.getInstance();
    private LocationDAO locationDAO = LocationDAO.getInstance();
    private DelayDAO delayDAO = DelayDAO.getInstance();

    /**
     * Generates an instance of this service
     * @return an instance of the service
     */
    public static WeatherApiService getInstance() throws IOException {
        if(instance == null){
            instance = new WeatherApiService();
        }
        return instance;
    }

    private WeatherApiService() throws IOException {}

    /**
     * Switches the key to use to call the API
     * @param keys the list of keys used by the API
     * @param lastIndex the index of the recently used key
     * @return the new index
     */
    private Long keySwitch(Collection<?> keys, Long lastIndex){
        return (lastIndex + 1l) % keys.size();
    }

    /**
     * A Java version of the Python function used to build an URL based on the supplied API, Location, Delay and Date
     * @param api the API
     * @param location the location
     * @param delay the delay
     * @param storedDate the date
     * @return the built URL
     */
    private String urlStrategy(Api api,Location location,Delay delay,Date storedDate){
        String url;
        if(delay.getValue() == 0){
            url = api.getRealtimeUrl().getUrl_ex();
        }else{
            url = api.getForecastUrl().getUrl_ex();
        }
        HashMap<String,String> urlVars = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(storedDate);
        Integer days = delay.getValue() / 24;
        Integer hours = (calendar.get(Calendar.HOUR_OF_DAY) + delay.getValue()) % 24;

        urlVars.put("key",api.getApi_keys().get(api.getLastUsedKey().intValue()).getKey_value());
        urlVars.put("city",location.getCity());
        urlVars.put("hour",hours.toString());
        urlVars.put("days",days.toString());
        urlVars.put("country.code",location.getCountry().getCode());
        urlVars.put("country.name",location.getCountry().getName());
        urlVars.put("latitude",location.getLatitude().toString());
        urlVars.put("longitude",location.getLongitude().toString());
        url = URLParser.parseURL(url,urlVars);
        return url;
    }

    /**
     * Returns all the data at a given date
     * @param storedDate the date
     * @return the list of raw JSON MongoDB objects
     * @throws IOException ???
     */
    public List<ApiData> getAllApiData(Date storedDate) throws IOException, JSONException {
        List<ApiData> apiDataList = new ArrayList<>();
        for(Api api : apiDAO.getAllWithKeysAndUrls()){
            for(Location location : locationDAO.getAll()){
                for(Delay delay : delayDAO.getAll()){
                    ApiData apiData = getApiData(api,location,delay,storedDate);
                    if(apiData != null) {
                        apiDataList.add(apiData);
                    }
                }
            }
        }
        logger.info("ApiData list size = "+ apiDataList.size());
        return apiDataList;
    }

    /**
     * Returns one data element for the given API, Location, Delay and Date
     * @param api the API
     * @param location the location
     * @param delay the delay
     * @param storedDate the date
     * @return the ApiData object
     */
    public ApiData getApiData(Api api,Location location,Delay delay,Date storedDate) throws JSONException {
        ApiData data = null;
        try {
            Client client = ClientBuilder.newClient();
            String url = urlStrategy(api,location,delay,storedDate);
            String res = client.target(url)
                    .register(GZipEncoder.class)
                    .request("application/json")
                    .get().readEntity(String.class);

            JSONObject object = new JSONObject(res);
            data = new ApiData(object,location.getCity(), api.getName(), delay.getValue(), storedDate);
            data.setCountry(location.getCountry().getName());

            Long nextId = keySwitch(api.getApi_keys(), api.getLastUsedKey());
            api.setLastUsedKey(nextId);
            apiDAO.update(api);
        }catch (Throwable e){
            logger.error("Exception occurred:", e);
        }
        return data;
    }

}
