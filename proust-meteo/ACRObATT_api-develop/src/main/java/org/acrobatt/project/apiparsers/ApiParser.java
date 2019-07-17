package org.acrobatt.project.apiparsers;

import org.acrobatt.project.model.mongo.ApiData;
import org.acrobatt.project.model.mysql.WeatherData;

/**
 * The master parsing interface
 */
public interface ApiParser {

    /**
     * Parses a raw real-time JSON object stored in MongoDB and transforms it into the MySQL equivalent
     * @param jsonObject the real-time JSON object
     * @return a parsed WeatherData object
     */
    public WeatherData parseJsonResultToWeatherData(ApiData jsonObject);

    /**
     * Parses a raw forecast JSON object stored in MongoDB and transforms it into the MySQL equivalent
     * @param jsonObject the forecast JSON object
     * @return a parsed WeatherData object
     */
    public WeatherData parseJsonForecastToWeathData(ApiData jsonObject);
}