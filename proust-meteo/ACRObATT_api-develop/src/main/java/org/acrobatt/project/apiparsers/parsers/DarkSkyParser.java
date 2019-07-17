package org.acrobatt.project.apiparsers.parsers;

import org.acrobatt.project.apiparsers.ApiConst;
import org.acrobatt.project.apiparsers.ApiParser;
import org.acrobatt.project.dao.mysql.*;
import org.acrobatt.project.model.mongo.ApiData;
import org.acrobatt.project.model.mysql.*;
import org.acrobatt.project.utils.DateUtils;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.JSONUtils;
import org.acrobatt.project.utils.UnitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * The parser for the DarkSky API
 */
public class DarkSkyParser  implements ApiParser{

    //logger instance for this class
    private static Logger logger = LogManager.getLogger(DarkSkyParser.class);
    private static DarkSkyParser instance = null;

    ApiDAO apiDAO = ApiDAO.getInstance();
    private PropertyDAO propertyDAO = PropertyDAO.getInstance();
    private LocationDAO locationDAO = LocationDAO.getInstance();
    private CountryDAO countryDAO = CountryDAO.getInstance();
    private DelayDAO delayDAO = DelayDAO.getInstance();
    private TextDataConvertDAO convertDAO = TextDataConvertDAO.getInstance();

    /**
     * Generates an instance of this parser
     * @return an instance of the parser
     */
    public static DarkSkyParser getInstance(){
        if(instance == null){
            instance = new DarkSkyParser();
        }
        return instance;
    }

    @Override
    public WeatherData parseJsonResultToWeatherData(ApiData data) {
        WeatherData genericModel = new WeatherData();

        // Get API
        Api api = apiDAO.getByName(ApiConst.DARK_SKY);

        try {
            Country country = countryDAO.getByName(data.getCountry());
            Location location = locationDAO.getByCityAndCountry(data.getCity(),country);
            Delay delay = delayDAO.getByDuration(0);

            JSONObject rawData = data.getData();
            JSONObject currently = rawData.getJSONObject("currently");

            HashMap<String, Object> values = new HashMap<>(
                    JSONUtils.findValueBootstrap(
                            currently, Arrays.asList("temperature", "pressure", "humidity","icon","windSpeed")
                    )
            );

            // Numerical properties
            Property temperatureProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_TEMPERATURE);
            Property pressureProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_PRESSURE);
            Property humidityProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_HUMIDITY);
            Property windSpeedProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_WIND_SPEED);

            // Text properties
            TextData textConditionData = convertDAO.getForRawApiText(values.get("icon").toString(), api).getModelTextData();
            Property conditionProperty = textConditionData.getProperty();

            DataValue temperatureData = new DataValue(temperatureProperty,  genericModel, UnitUtils.convertFahrenheitToCelsius(((Number) values.get("temperature")).floatValue()));
            DataValue pressureData =    new DataValue(pressureProperty,     genericModel, ((Number) values.get("pressure")).floatValue());
            DataValue humidityData =    new DataValue(humidityProperty,     genericModel, ((Number) values.get("humidity")).floatValue()* 100.0f);
            DataValue windSpeedData =   new DataValue(windSpeedProperty,    genericModel, ((Number) values.get("windSpeed")).floatValue() * 1.60934f);
            DataValue conditionData =   new DataValue(conditionProperty,    genericModel, textConditionData.getIntensity());

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

    @Override
    public WeatherData parseJsonForecastToWeathData(ApiData data) {
        WeatherData genericModel = new WeatherData();

        // Get API
        Api api = apiDAO.getByName(ApiConst.DARK_SKY);

        try {
            Country country = countryDAO.getByName(data.getCountry());
            Location location = locationDAO.getByCityAndCountry(data.getCity(),country);
            Delay delay = delayDAO.getByDuration(data.getDelay());

            JSONObject rawData = data.getData();
            JSONObject hourly = rawData.getJSONObject("hourly");
            JSONArray forecasts = hourly.getJSONArray("data");
            for(int i=0; i < forecasts.length(); i++) {
                JSONObject dayData = forecasts.getJSONObject(i);

                Date currentDate = DateUtils.truncateDateToDay(data.getStoredDate());
                Date computedDate = DateUtils.truncateDateToDay(DateUtils.addDelay(currentDate, data.getDelay() - 1));
                Date forecastDate = DateUtils.truncateDateToDay(new Date(dayData.getLong("time") * 1000));

                if(forecastDate.equals(computedDate)) {
                    // Get properties (field list can be passed as a parameter)
                    HashMap<String, Object> values = new HashMap<>(
                            JSONUtils.findValueBootstrap(
                                    dayData, Arrays.asList("temperature", "humidity","pressure","icon","windSpeed")
                            )
                    );

                    // Numerical properties
                    Property temperatureProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_TEMPERATURE);
                    Property humidityProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_HUMIDITY);
                    Property pressureProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_PRESSURE);
                    Property windSpeedProperty = propertyDAO.getByName(GlobalVariableRegistry.PROPERTY_WIND_SPEED);

                    // Text properties
                    TextData textConditionData = convertDAO.getForRawApiText(values.get("icon").toString(), api).getModelTextData();
                    Property conditionProperty = textConditionData.getProperty();

                    // Create the DataValues
                    DataValue temperatureData = new DataValue(temperatureProperty,  genericModel,   UnitUtils.convertFahrenheitToCelsius(((Number) values.get("temperature")).floatValue()));
                    DataValue humidityData =    new DataValue(humidityProperty,     genericModel,   ((Number) values.get("humidity")).floatValue() * 100.0f);
                    DataValue pressureData =    new DataValue(pressureProperty,     genericModel,   ((Number) values.get("pressure")).floatValue());
                    DataValue windSpeedData =   new DataValue(windSpeedProperty,    genericModel,   ((Number) values.get("windSpeed")).floatValue() * 1.60934f);
                    DataValue conditionData =   new DataValue(conditionProperty,    genericModel,   textConditionData.getIntensity());

                    // Fill WeatherData properties
                    genericModel.setData_api(api);
                    genericModel.setData_location(location);
                    genericModel.setForecast(true);
                    genericModel.setData_delay(delay);
                    genericModel.setInserted_at(data.getStoredDate());

                    genericModel.addData_value(temperatureData);
                    genericModel.addData_value(humidityData);
                    genericModel.addData_value(pressureData);
                    genericModel.addData_value(conditionData);
                    genericModel.addData_value(windSpeedData);

                    return genericModel;
                }
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
            return null;
        }
        return null;
    }
}
