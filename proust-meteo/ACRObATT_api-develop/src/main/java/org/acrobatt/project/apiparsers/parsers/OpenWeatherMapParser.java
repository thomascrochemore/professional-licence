package org.acrobatt.project.apiparsers.parsers;

import org.acrobatt.project.apiparsers.ApiConst;
import org.acrobatt.project.apiparsers.ApiParser;
import org.acrobatt.project.dao.mysql.*;
import org.acrobatt.project.model.mongo.ApiData;
import org.acrobatt.project.model.mysql.*;
import org.acrobatt.project.utils.DateUtils;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.UnitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * The parser for the OpenWeatherMap API
 */
public class OpenWeatherMapParser implements ApiParser{

    //logger instance for this class
    private static Logger logger = LogManager.getLogger(OpenWeatherMapParser.class);
    private static OpenWeatherMapParser instance = null;

    private ApiDAO apiDAO = ApiDAO.getInstance();
    private PropertyDAO propertyDAO = PropertyDAO.getInstance();
    private LocationDAO locationDAO = LocationDAO.getInstance();
    private CountryDAO countryDAO = CountryDAO.getInstance();
    private DelayDAO delayDAO = DelayDAO.getInstance();
    private TextDataConvertDAO convertDAO = TextDataConvertDAO.getInstance();

    /**
     * Generates an instance of this parser
     * @return an instance of the parser
     */
    public static OpenWeatherMapParser getInstance(){
        if(instance == null){
            instance = new OpenWeatherMapParser();
        }
        return instance;
    }

    private OpenWeatherMapParser(){}
    public WeatherData parseJsonResultToWeatherData(ApiData data) {
        Api api = apiDAO.getByName(ApiConst.OPEN_WEATHER_MAP);
        WeatherData genericModel = new WeatherData();

        try {
            JSONObject rawData = data.getData();
            JSONObject firstMain = rawData.getJSONObject("main");
            JSONObject conditions = rawData.getJSONArray("weather").getJSONObject(0);
            JSONObject wind = rawData.getJSONObject("wind");

            Float temperature = UnitUtils.convertKelvinToCelsius((float) firstMain.getDouble("temp"));
            Float pressure = (float) firstMain.getDouble("pressure");
            Float humidity = (float) firstMain.getDouble("humidity");
            Float windSpeed = (float) wind.getDouble("speed") * 3.6f;

            Country country = countryDAO.getByName(data.getCountry());
            Location location = locationDAO.getByCityAndCountry(data.getCity(),country);
            Delay delay = delayDAO.getByDuration(data.getDelay());

            // Numerical properties
            Property temperatureProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_TEMPERATURE);
            Property pressureProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_PRESSURE);
            Property humidityProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_HUMIDITY);
            Property windSpeedProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_WIND_SPEED);

            // Text properties
            TextData textConditionData = convertDAO.getForRawApiText(conditions.getString("description"), api).getModelTextData();
            Property conditionProperty = textConditionData.getProperty();

            DataValue temperatureData = new DataValue(temperatureProperty,  genericModel, temperature);
            DataValue pressureData =    new DataValue(pressureProperty,     genericModel, pressure);
            DataValue humidityData =    new DataValue(humidityProperty,     genericModel, humidity);
            DataValue windSpeedData =   new DataValue(windSpeedProperty,    genericModel, windSpeed);
            DataValue conditionData =   new DataValue(conditionProperty,    genericModel, textConditionData.getIntensity());

            // fill boilerplate WeatherData properties
            genericModel.setData_api(api);
            genericModel.setData_location(location);
            genericModel.setForecast(false);
            genericModel.setData_delay(delay);
            genericModel.setInserted_at(data.getStoredDate());

            genericModel.addData_value(temperatureData);
            genericModel.addData_value(pressureData);
            genericModel.addData_value(humidityData);
            genericModel.addData_value(windSpeedData);
            genericModel.addData_value(conditionData);

        } catch(Exception e) {
            logger.error(e.getMessage());
            return null;
        }
        return genericModel;
    }

    public WeatherData parseJsonForecastToWeathData(ApiData data) {
        Api api = apiDAO.getByName(ApiConst.OPEN_WEATHER_MAP);
        WeatherData genericModel = new WeatherData();
        Date storedDate = DateUtils.truncateDateToHour(data.getStoredDate());

        try {
            JSONObject rawData = data.getData();
            JSONArray list = rawData.getJSONArray("list") ;

            Long storedDateUnixTs = storedDate.getTime() / 1000;
            Long delayUnixTs = (long) ( (data.getDelay()) * 3600);
            Long forecastUnixTs = storedDateUnixTs + delayUnixTs ;

            Country country = countryDAO.getByName(data.getCountry());
            Location location = locationDAO.getByCityAndCountry(data.getCity(),country);
            Delay delay = delayDAO.getByDuration(data.getDelay());

            // fill boilerplate WeatherData properties
            genericModel.setData_api(api);
            genericModel.setData_location(location);
            genericModel.setForecast(true);
            genericModel.setInserted_at(data.getStoredDate());
            genericModel.setData_delay(delay);

            for(int i = 0; i < list.length(); i++) {
                JSONObject fcst = list.getJSONObject(i);
                Date date = DateUtils.truncateDateToHour(new Date(fcst.getLong("dt") * 1000));
                long expectTime = date.getTime() / 1000;
                if(expectTime == forecastUnixTs) {
                    JSONObject mainBlock = fcst.getJSONObject("main");
                    JSONObject conditionBlock = fcst.getJSONArray("weather").getJSONObject(0);
                    JSONObject windblock = fcst.getJSONObject("wind");

                    Float temperature = UnitUtils.convertKelvinToCelsius((float) mainBlock.getDouble("temp"));
                    Float pressure = (float) mainBlock.getDouble("pressure");
                    Float humidity = (float) mainBlock.getDouble("humidity");
                    Float windSpeed = (float) windblock.getDouble("speed") * 3.6f;

                    // Numerical properties
                    Property temperatureProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_TEMPERATURE);
                    Property pressureProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_PRESSURE);
                    Property humidityProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_HUMIDITY);
                    Property windSpeedProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_WIND_SPEED);

                    // Text properties
                    TextData textConditionData = convertDAO.getForRawApiText(conditionBlock.getString("description"), api).getModelTextData();
                    Property conditionProperty = textConditionData.getProperty();

                    DataValue temperatureData = new DataValue(temperatureProperty,  genericModel, temperature);
                    DataValue pressureData =    new DataValue(pressureProperty,     genericModel, pressure);
                    DataValue humidityData =    new DataValue(humidityProperty,     genericModel, humidity);
                    DataValue windSpeedData =   new DataValue(windSpeedProperty,    genericModel, windSpeed);
                    DataValue conditionData =   new DataValue(conditionProperty,    genericModel, textConditionData.getIntensity());

                    genericModel.addData_value(temperatureData);
                    genericModel.addData_value(pressureData);
                    genericModel.addData_value(humidityData);
                    genericModel.addData_value(windSpeedData);
                    genericModel.addData_value(conditionData);
                }
            }
        } catch(JSONException e) {
            logger.error(e.getMessage());
            return null;
        }
        return genericModel;
    }
}
