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

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * The parser for the WeatherUnderground API
 */
public class WundergroundParser implements ApiParser{

    //logger instance for this class
    private static Logger logger = LogManager.getLogger(WundergroundParser.class);
    private static WundergroundParser instance = null;

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
    public static WundergroundParser getInstance(){
        if(instance == null){
            instance = new WundergroundParser();
        }
        return instance;
    }

    private WundergroundParser(){}
    public WeatherData parseJsonResultToWeatherData(ApiData data) {
        WeatherData genericModel = new WeatherData();

        // Get API
        Api api = apiDAO.getByName(ApiConst.WUNDERGROUND);

        try {
            JSONObject rawData = data.getData();
            JSONObject observationBlock = rawData.getJSONObject("current_observation");

            // Get country
            Country country = countryDAO.getByName(data.getCountry());
            Location location = locationDAO.getByCityAndCountry(data.getCity(),country);
            Delay delay = delayDAO.getByDuration(data.getDelay());
            // Get properties (field list can be passed as a parameter)
            HashMap<String, Object> values = new HashMap<>(
                    JSONUtils.findValueBootstrap(
                            observationBlock, Arrays.asList("temp_c", "pressure_mb","wind_kph","relative_humidity")
                    )
            );

            // Numerical properties
            Property temperatureProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_TEMPERATURE);
            Property pressureProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_PRESSURE);
            Property humidityProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_HUMIDITY);
            Property windSpeedProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_WIND_SPEED);

            Float humidity = Float.valueOf(values.get("relative_humidity").toString().replace("%",""));
            // Text properties
            TextData textConditionData = convertDAO.getForRawApiText(observationBlock.getString("weather"), api).getModelTextData();
            Property conditionProperty = textConditionData.getProperty();

            // Create the DataValues
            DataValue temperatureData = new DataValue(temperatureProperty,  genericModel, (Float.parseFloat(values.get("temp_c").toString())));
            DataValue pressureData =    new DataValue(pressureProperty,     genericModel, (Float.parseFloat(values.get("pressure_mb").toString())));
            DataValue humidityData =    new DataValue(humidityProperty,  genericModel, humidity);
            DataValue windSpeedData =   new DataValue(windSpeedProperty,    genericModel, (Float.parseFloat(values.get("wind_kph").toString())));
            DataValue conditionData =   new DataValue(conditionProperty,    genericModel, textConditionData.getIntensity());
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

        } catch(Exception e) {
            logger.error(e.getMessage());
            return null;
        }
        return genericModel;
    }

    public WeatherData parseJsonForecastToWeathData(ApiData data) {
        WeatherData genericModel = new WeatherData();

        // Get API
        Api api = apiDAO.getByName(ApiConst.WUNDERGROUND);

        try {
            JSONObject rawData = data.getData();
            JSONObject forecastBlock = rawData.getJSONObject("forecast");
            JSONArray simpleForecasts = forecastBlock.getJSONObject("simpleforecast").getJSONArray("forecastday");

            for(int i = 0; i < simpleForecasts.length(); i++) {
                JSONObject obj = simpleForecasts.getJSONObject(i);
                JSONObject objDateBlock = obj.getJSONObject("date");
                JSONObject objHighBlock = obj.getJSONObject("high");
                JSONObject objLowBlock = obj.getJSONObject("low");

                Date currentDate = DateUtils.truncateDateToDay(data.getStoredDate());
                Date computedDate = DateUtils.truncateDateToDay(DateUtils.addDelay(currentDate, data.getDelay()));
                Date previsionDate = DateUtils.truncateDateToDay(Date.from(Instant.ofEpochSecond(objDateBlock.getInt("epoch"))));

                if(previsionDate.equals(computedDate)) {

                    // Get country
                    Country country = countryDAO.getByName(data.getCountry());
                    Location location = locationDAO.getByCityAndCountry(data.getCity(),country);
                    Delay delay = delayDAO.getByDuration(data.getDelay());

                    // Get properties (field list can be passed as a parameter)
                    HashMap<String, Object> values = new HashMap<>(
                            JSONUtils.findValueBootstrap(
                                    obj, Arrays.asList("avehumidity")
                            )
                    );
                    Float windSpeed = (float) obj.getJSONObject("avewind").getDouble("kph");

                    // Numerical properties
                    Property temperatureProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_TEMPERATURE);
                    Property humidityProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_HUMIDITY);
                    Property windSpeedProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_WIND_SPEED);

                    // Text properties
                    TextData textConditionData = convertDAO.getForRawApiText(obj.getString("conditions"), api).getModelTextData();
                    Property conditionProperty = textConditionData.getProperty();

                    // Create the DataValues
                    DataValue temperatureData = new DataValue(temperatureProperty,  genericModel, (float) (objHighBlock.getDouble("celsius") + objLowBlock.getDouble("celsius")) / 2);
                    DataValue humidityData =    new DataValue(humidityProperty,     genericModel, (Float.parseFloat(values.get("avehumidity").toString())));
                    DataValue windSpeedData =   new DataValue(windSpeedProperty,    genericModel, windSpeed);
                    DataValue conditionData =   new DataValue(conditionProperty,    genericModel, textConditionData.getIntensity());

                    // Fill WeatherData properties
                    genericModel.setData_api(api);
                    genericModel.setData_location(location);
                    genericModel.setForecast(true);
                    genericModel.setData_delay(delay);
                    genericModel.setInserted_at(data.getStoredDate());

                    genericModel.addData_value(temperatureData);
                    genericModel.addData_value(humidityData);
                    genericModel.addData_value(windSpeedData);
                    genericModel.addData_value(conditionData);
                }
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
            return null;
        }
        return genericModel;
    }
}
