package org.acrobatt.project.apiparsers.parsers;

import org.acrobatt.project.apiparsers.ApiConst;
import org.acrobatt.project.apiparsers.ApiParser;
import org.acrobatt.project.dao.mysql.*;
import org.acrobatt.project.model.mongo.ApiData;
import org.acrobatt.project.model.mysql.*;
import org.acrobatt.project.utils.DateUtils;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.JSONUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * The parser for the Apixu API
 */
public class ApixuParser implements ApiParser{

    //logger instance for this class
    private static Logger logger = LogManager.getLogger(ApixuParser.class);
    private static ApixuParser instance = null;

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
    public static  ApixuParser getInstance(){
        if(instance == null){
            instance = new ApixuParser();
        }
        return instance;
    }

    private ApixuParser(){}

    @SuppressWarnings("unchecked")
    public WeatherData parseJsonResultToWeatherData(ApiData data) {
        WeatherData genericModel = new WeatherData();

        // Get API
        Api api = apiDAO.getByName(ApiConst.APIXU);

        try {
            // Divide raw data in parseable blocks
            JSONObject rawData = data.getData();
            JSONObject currentBlock = rawData.getJSONObject("current");
            JSONObject conditionBlock = currentBlock.getJSONObject("condition");

            // Get country
            Country country = countryDAO.getByName(data.getCountry());
            Location location = locationDAO.getByCityAndCountry(data.getCity(), country);

            // Get properties (field list can be passed as a parameter)
            HashMap<String, Object> values = new HashMap<>(
                    JSONUtils.findValueBootstrap(
                            currentBlock, Arrays.asList("temp_c", "pressure_mb", "humidity","wind_kph")
                    )
            );

            // Numerical properties
            Property temperatureProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_TEMPERATURE);
            Property pressureProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_PRESSURE);
            Property humidityProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_HUMIDITY);
            Property windSpeedProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_WIND_SPEED);

            // Text properties
            TextData textConditionData = convertDAO.getForRawApiText(conditionBlock.getString("text"), api).getModelTextData();
            Property conditionProperty = textConditionData.getProperty();

            // Create the DataValues
            DataValue temperatureData = new DataValue(temperatureProperty,  genericModel, ((Number) values.get("temp_c")).floatValue());
            DataValue pressureData =    new DataValue(pressureProperty,     genericModel, ((Number) values.get("pressure_mb")).floatValue());
            DataValue humidityData =    new DataValue(humidityProperty,     genericModel, ((Number) values.get("humidity")).floatValue());
            DataValue windSpeedData =   new DataValue(windSpeedProperty,    genericModel, ((Number) values.get("wind_kph")).floatValue());
            DataValue conditionData =   new DataValue(conditionProperty,    genericModel, textConditionData.getIntensity());
            Delay delay = delayDAO.getByDuration(data.getDelay());

            // Fill WeatherData properties
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

        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
        return genericModel;
    }

    public WeatherData parseJsonForecastToWeathData(ApiData data) {
        WeatherData genericModel = new WeatherData();

        // Get API
        Api api = apiDAO.getByName(ApiConst.APIXU);

        try {
            // Divide raw data in parseable blocks
            JSONObject rawData = data.getData();
            JSONObject forecastBlock = rawData.getJSONObject("forecast");

            JSONArray forecastDays = forecastBlock.getJSONArray("forecastday");
            for(int i = 0; i < forecastDays.length(); i++) {
                JSONObject dayData = forecastDays.getJSONObject(i);

                Date currentDate = DateUtils.truncateDateToDay(data.getStoredDate());
                Date computedDate = DateUtils.truncateDateToDay(DateUtils.addDelay(currentDate, data.getDelay() - 1));
                Date forecastDate = DateUtils.truncateDateToDay(new Date(dayData.getLong("date_epoch") * 1000));

                if(forecastDate.equals(computedDate)) {
                    JSONObject day = dayData.getJSONObject("day");
                    JSONObject conditionBlock = day.getJSONObject("condition");

                    // Get country
                    Country country = countryDAO.getByName(data.getCountry());
                    Location location = locationDAO.getByCityAndCountry(data.getCity(),country);
                    // Get properties (field list can be passed as a parameter)
                    HashMap<String, Object> values = new HashMap<>(
                            JSONUtils.findValueBootstrap(
                                    day, Arrays.asList("avgtemp_c", "avghumidity","maxwind_kph")
                            )
                    );

                    // Numerical properties
                    Property temperatureProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_TEMPERATURE);
                    Property humidityProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_HUMIDITY);
                    Property windSpeedProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_WIND_SPEED);

                    // Text properties
                    TextData textConditionData = convertDAO.getForRawApiText(conditionBlock.getString("text"), api).getModelTextData();
                    Property conditionProperty = textConditionData.getProperty();

                    // Create the DataValues
                    DataValue temperatureData = new DataValue(temperatureProperty,  genericModel,   ((Number) values.get("avgtemp_c")).floatValue());
                    DataValue humidityData =    new DataValue(humidityProperty,     genericModel,   ((Number) values.get("avghumidity")).floatValue());
                    DataValue windSpeedData =   new DataValue(windSpeedProperty,    genericModel,   ((Number) values.get("maxwind_kph")).floatValue());
                    DataValue conditionData =   new DataValue(conditionProperty,    genericModel,   textConditionData.getIntensity());
                    Delay delay = delayDAO.getByDuration(data.getDelay());

                    // Fill WeatherData properties
                    genericModel.setData_api(api);
                    genericModel.setData_location(location);
                    genericModel.setForecast(true);
                    genericModel.setData_delay(delay);
                    genericModel.setInserted_at(data.getStoredDate());

                    genericModel.addData_value(temperatureData);
                    genericModel.addData_value(humidityData);
                    genericModel.addData_value(conditionData);
                    genericModel.addData_value(windSpeedData);
                }
            }

        } catch(Exception e) {
            logger.error(e.getMessage());
            return null;
        }
        return genericModel;
    }
}