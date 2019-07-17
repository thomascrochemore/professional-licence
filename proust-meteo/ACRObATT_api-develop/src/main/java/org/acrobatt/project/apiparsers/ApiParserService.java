package org.acrobatt.project.apiparsers;


import org.acrobatt.project.model.mongo.ApiData;
import org.acrobatt.project.model.mysql.WeatherData;

import java.io.IOException;


public class ApiParserService{

    private static ApiParserService instance;

    private ApiParserFactory apiParserFactory = ApiParserFactory.getInstance();

    /**
     * Generates an instance of this service
     * @return an instance of the service
     */
    public static ApiParserService getInstance(){
        if(instance == null){
            instance = new ApiParserService();
        }
        return instance;
    }

    private ApiParserService(){}

    /**
     * An implementation of the interface function
     * @see ApiParser
     * @param apiData a real-time JSON object
     * @return a parsed WeatherData object
     * @throws IOException if parsing fails
     */
    public WeatherData parseJsonResultToWeatherData(ApiData apiData) throws IOException {
        return apiParserFactory.getApiParser(apiData.getApi()).parseJsonResultToWeatherData(apiData);
    }

    /**
     * An implementation of the interface function
     * @see ApiParser
     * @param apiData a forecast JSON object
     * @return a parsed WeatherData object
     * @throws IOException if parsing fails
     */
    public WeatherData parseJsonForecastToWeathData(ApiData apiData) throws IOException {
        return apiParserFactory.getApiParser(apiData.getApi()).parseJsonForecastToWeathData(apiData);
    }

    /**
     * Checks if a parser exists for the given API name
     * @param name the name
     * @return true if it exists
     */
    public boolean apiParserExists(String name) {
        return apiParserFactory.apiParserExists(name);
    }
}
