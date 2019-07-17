package org.acrobatt.project.apiparsers;

import org.acrobatt.project.apiparsers.parsers.DarkSkyParser;
import org.acrobatt.project.model.mongo.ApiData;
import org.acrobatt.project.model.mysql.DataValue;
import org.acrobatt.project.model.mysql.WeatherData;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class DarkSkyParserTest {

    DarkSkyParser darkSkyParser = DarkSkyParser.getInstance();

    @BeforeAll
    public static void initSetupMigration(){
        HibernateUtils.loadConfiguration(new File(GlobalVariableRegistry.HIBERNATE_CFG_TEST_LOCAL));
        HibernateUtils.getSessionFactory();
        /*DBMigrationController migrationController = new DBMigrationController();
        migrationController.migrateSprint3();*/
    }

    @Test
    public  void realTimeTest(){
        try {
            JSONObject apiJson = new JSONObject(REALTIME_JSON);
            ApiData apiData = new ApiData(apiJson,"Helsinki",ApiConst.DARK_SKY,0, Date.from(Instant.now()));
            WeatherData weatherData = darkSkyParser.parseJsonResultToWeatherData(apiData);
            System.out.println("Realtime : ");
            System.out.println("delay : " + weatherData.getData_delay().getValue());
            System.out.println("city : "+weatherData.getData_location().getCity());
            System.out.println("api : "+weatherData.getData_api().getName());
            System.out.println("forecast : " + weatherData.getForecast());
            for(DataValue dataValue : weatherData.getData_values()){
                System.out.println(dataValue.getProperty().getName()+" - decimal : "+dataValue.getValue_decimal());
            }
        } catch(Exception e) {
            System.out.println("Test failed : "+e.getMessage());
        }
    }

    @Test
    public void forecastTest(){
        try {
            JSONObject apiJson = new JSONObject(FORECAST_2018_22_03);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,2018);
            calendar.set(Calendar.MONTH,2);
            calendar.set(Calendar.DAY_OF_MONTH,22);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            ApiData apiData = new ApiData(apiJson,"Strasbourg",ApiConst.DARK_SKY,6,calendar.getTime());
            WeatherData weatherData = darkSkyParser.parseJsonForecastToWeathData(apiData);

            System.out.println("\nForecast :");
            System.out.println("delay : " + weatherData.getData_delay().getValue());
            System.out.println("city : "+weatherData.getData_location().getCity());
            System.out.println("api : "+weatherData.getData_api().getName());
            System.out.println("forecast : " + weatherData.getForecast());
            for(DataValue dataValue : weatherData.getData_values()){
                System.out.println(dataValue.getProperty().getName()+" - decimal : "+dataValue.getValue_decimal());
            }
        } catch(Exception e) {
            System.out.println("Test failed : "+e.getMessage());
        }

    }

    public static final String REALTIME_JSON = "{\n" +
            "        \"hourly\" : {\n" +
            "            \"icon\" : \"partly-cloudy-day\",\n" +
            "            \"data\" : [ \n" +
            "                {\n" +
            "                    \"ozone\" : 435.84,\n" +
            "                    \"windGust\" : 18.81,\n" +
            "                    \"temperature\" : 29.09,\n" +
            "                    \"precipAccumulation\" : 0.012,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.94,\n" +
            "                    \"cloudCover\" : 0.44,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 18.11,\n" +
            "                    \"pressure\" : 1005.23,\n" +
            "                    \"windSpeed\" : 14.37,\n" +
            "                    \"windBearing\" : 216,\n" +
            "                    \"visibility\" : 6.22,\n" +
            "                    \"time\" : 1521673200,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"precipIntensity\" : 0.0013,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 27.62,\n" +
            "                    \"precipProbability\" : 0.07\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 433.44,\n" +
            "                    \"windGust\" : 18.39,\n" +
            "                    \"temperature\" : 27.64,\n" +
            "                    \"precipAccumulation\" : 0.01,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.98,\n" +
            "                    \"cloudCover\" : 0.63,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 17.21,\n" +
            "                    \"pressure\" : 1004.59,\n" +
            "                    \"windSpeed\" : 12.2,\n" +
            "                    \"windBearing\" : 221,\n" +
            "                    \"visibility\" : 6.22,\n" +
            "                    \"time\" : 1521676800,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"precipIntensity\" : 0.001,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 27.16,\n" +
            "                    \"precipProbability\" : 0.06\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 430.99,\n" +
            "                    \"windGust\" : 19.2,\n" +
            "                    \"temperature\" : 27.19,\n" +
            "                    \"precipAccumulation\" : 0.005,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 1,\n" +
            "                    \"cloudCover\" : 0.82,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 17.56,\n" +
            "                    \"pressure\" : 1003.91,\n" +
            "                    \"windSpeed\" : 10.37,\n" +
            "                    \"windBearing\" : 230,\n" +
            "                    \"visibility\" : 6.22,\n" +
            "                    \"time\" : 1521680400,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"precipIntensity\" : 0.0005,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 27.16,\n" +
            "                    \"precipProbability\" : 0.05\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 428.52,\n" +
            "                    \"windGust\" : 20.78,\n" +
            "                    \"temperature\" : 27.47,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 1,\n" +
            "                    \"cloudCover\" : 0.9,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 18.13,\n" +
            "                    \"pressure\" : 1003.21,\n" +
            "                    \"windSpeed\" : 9.98,\n" +
            "                    \"windBearing\" : 236,\n" +
            "                    \"visibility\" : 6.22,\n" +
            "                    \"time\" : 1521684000,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"precipIntensity\" : 0.0001,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 27.45,\n" +
            "                    \"precipProbability\" : 0.05\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 426.73,\n" +
            "                    \"windGust\" : 22.45,\n" +
            "                    \"temperature\" : 27.97,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"dewPoint\" : 27.82,\n" +
            "                    \"humidity\" : 0.99,\n" +
            "                    \"cloudCover\" : 0.9,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 18.47,\n" +
            "                    \"pressure\" : 1002.51,\n" +
            "                    \"windSpeed\" : 10.48,\n" +
            "                    \"time\" : 1521687600,\n" +
            "                    \"windBearing\" : 235,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 425.95,\n" +
            "                    \"windGust\" : 24.07,\n" +
            "                    \"temperature\" : 28.21,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"dewPoint\" : 28.01,\n" +
            "                    \"humidity\" : 0.99,\n" +
            "                    \"cloudCover\" : 0.91,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 18.52,\n" +
            "                    \"pressure\" : 1001.85,\n" +
            "                    \"windSpeed\" : 10.97,\n" +
            "                    \"time\" : 1521691200,\n" +
            "                    \"windBearing\" : 235,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 425.83,\n" +
            "                    \"windGust\" : 25.78,\n" +
            "                    \"temperature\" : 28.74,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"dewPoint\" : 28.25,\n" +
            "                    \"humidity\" : 0.98,\n" +
            "                    \"cloudCover\" : 0.93,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 18.91,\n" +
            "                    \"pressure\" : 1001.17,\n" +
            "                    \"windSpeed\" : 11.53,\n" +
            "                    \"time\" : 1521694800,\n" +
            "                    \"windBearing\" : 235,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 426.1,\n" +
            "                    \"windGust\" : 27.37,\n" +
            "                    \"temperature\" : 29.48,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"dewPoint\" : 28.6,\n" +
            "                    \"humidity\" : 0.96,\n" +
            "                    \"cloudCover\" : 0.9,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 19.54,\n" +
            "                    \"pressure\" : 1000.44,\n" +
            "                    \"windSpeed\" : 12.16,\n" +
            "                    \"time\" : 1521698400,\n" +
            "                    \"windBearing\" : 236,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 426.98,\n" +
            "                    \"windGust\" : 28.94,\n" +
            "                    \"temperature\" : 30.67,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.94,\n" +
            "                    \"cloudCover\" : 0.75,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 20.67,\n" +
            "                    \"pressure\" : 999.58,\n" +
            "                    \"windSpeed\" : 13.02,\n" +
            "                    \"windBearing\" : 239,\n" +
            "                    \"time\" : 1521702000,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.0001,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 29.13,\n" +
            "                    \"precipProbability\" : 0.07\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 428.22,\n" +
            "                    \"windGust\" : 30.37,\n" +
            "                    \"temperature\" : 32.16,\n" +
            "                    \"precipAccumulation\" : 0.005,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.91,\n" +
            "                    \"cloudCover\" : 0.55,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 22.22,\n" +
            "                    \"pressure\" : 998.66,\n" +
            "                    \"windSpeed\" : 13.86,\n" +
            "                    \"windBearing\" : 244,\n" +
            "                    \"time\" : 1521705600,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.0007,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 29.75,\n" +
            "                    \"precipProbability\" : 0.13\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 428.56,\n" +
            "                    \"windGust\" : 31.15,\n" +
            "                    \"temperature\" : 33.4,\n" +
            "                    \"precipAccumulation\" : 0.008,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.88,\n" +
            "                    \"cloudCover\" : 0.44,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 23.53,\n" +
            "                    \"pressure\" : 997.83,\n" +
            "                    \"windSpeed\" : 14.58,\n" +
            "                    \"windBearing\" : 238,\n" +
            "                    \"time\" : 1521709200,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.0011,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 30.2,\n" +
            "                    \"precipProbability\" : 0.17\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 426.61,\n" +
            "                    \"windGust\" : 30.97,\n" +
            "                    \"temperature\" : 34.25,\n" +
            "                    \"precipAccumulation\" : 0.005,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.85,\n" +
            "                    \"cloudCover\" : 0.5,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 25.58,\n" +
            "                    \"pressure\" : 997.06,\n" +
            "                    \"windSpeed\" : 12.09,\n" +
            "                    \"windBearing\" : 215,\n" +
            "                    \"time\" : 1521712800,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.0007,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 30.36,\n" +
            "                    \"precipProbability\" : 0.16\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 423.79,\n" +
            "                    \"windGust\" : 30.13,\n" +
            "                    \"temperature\" : 34.88,\n" +
            "                    \"precipAccumulation\" : 0.007,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.84,\n" +
            "                    \"cloudCover\" : 0.65,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 27.46,\n" +
            "                    \"pressure\" : 996.41,\n" +
            "                    \"windSpeed\" : 9.67,\n" +
            "                    \"windBearing\" : 296,\n" +
            "                    \"time\" : 1521716400,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.0009,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 30.4,\n" +
            "                    \"precipProbability\" : 0.16\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 422.55,\n" +
            "                    \"windGust\" : 29.1,\n" +
            "                    \"temperature\" : 35.34,\n" +
            "                    \"precipAccumulation\" : 0.015,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.82,\n" +
            "                    \"cloudCover\" : 0.78,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 26.74,\n" +
            "                    \"pressure\" : 996.06,\n" +
            "                    \"windSpeed\" : 12.6,\n" +
            "                    \"windBearing\" : 268,\n" +
            "                    \"time\" : 1521720000,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.002,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 30.34,\n" +
            "                    \"precipProbability\" : 0.19\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 424.34,\n" +
            "                    \"windGust\" : 28.08,\n" +
            "                    \"temperature\" : 36.74,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipType\" : \"rain\",\n" +
            "                    \"humidity\" : 0.77,\n" +
            "                    \"cloudCover\" : 0.86,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 28.88,\n" +
            "                    \"pressure\" : 996.21,\n" +
            "                    \"windSpeed\" : 11.65,\n" +
            "                    \"time\" : 1521723600,\n" +
            "                    \"windBearing\" : 249,\n" +
            "                    \"precipIntensity\" : 0.0033,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 30.19,\n" +
            "                    \"precipProbability\" : 0.27\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 427.68,\n" +
            "                    \"windGust\" : 26.89,\n" +
            "                    \"temperature\" : 36.16,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipType\" : \"rain\",\n" +
            "                    \"humidity\" : 0.78,\n" +
            "                    \"cloudCover\" : 0.92,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 28.28,\n" +
            "                    \"pressure\" : 996.68,\n" +
            "                    \"windSpeed\" : 11.34,\n" +
            "                    \"time\" : 1521727200,\n" +
            "                    \"windBearing\" : 296,\n" +
            "                    \"precipIntensity\" : 0.0069,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 29.96,\n" +
            "                    \"precipProbability\" : 0.33\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 430.87,\n" +
            "                    \"windGust\" : 25.6,\n" +
            "                    \"temperature\" : 35.27,\n" +
            "                    \"precipAccumulation\" : 0.073,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.8,\n" +
            "                    \"cloudCover\" : 0.95,\n" +
            "                    \"summary\" : \"Overcast\",\n" +
            "                    \"apparentTemperature\" : 26.53,\n" +
            "                    \"pressure\" : 997.28,\n" +
            "                    \"windSpeed\" : 12.93,\n" +
            "                    \"windBearing\" : 272,\n" +
            "                    \"time\" : 1521730800,\n" +
            "                    \"icon\" : \"cloudy\",\n" +
            "                    \"precipIntensity\" : 0.0096,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 29.66,\n" +
            "                    \"precipProbability\" : 0.34\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 433.64,\n" +
            "                    \"windGust\" : 24.05,\n" +
            "                    \"temperature\" : 34.46,\n" +
            "                    \"precipAccumulation\" : 0.049,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.81,\n" +
            "                    \"cloudCover\" : 0.98,\n" +
            "                    \"summary\" : \"Overcast\",\n" +
            "                    \"apparentTemperature\" : 28.83,\n" +
            "                    \"pressure\" : 998.02,\n" +
            "                    \"windSpeed\" : 6.46,\n" +
            "                    \"windBearing\" : 233,\n" +
            "                    \"time\" : 1521734400,\n" +
            "                    \"icon\" : \"cloudy\",\n" +
            "                    \"precipIntensity\" : 0.0065,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 29.36,\n" +
            "                    \"precipProbability\" : 0.28\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 436.22,\n" +
            "                    \"windGust\" : 22.42,\n" +
            "                    \"temperature\" : 33.17,\n" +
            "                    \"precipAccumulation\" : 0.017,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.85,\n" +
            "                    \"cloudCover\" : 0.99,\n" +
            "                    \"summary\" : \"Overcast\",\n" +
            "                    \"apparentTemperature\" : 26.62,\n" +
            "                    \"pressure\" : 998.89,\n" +
            "                    \"windSpeed\" : 7.44,\n" +
            "                    \"windBearing\" : 334,\n" +
            "                    \"time\" : 1521738000,\n" +
            "                    \"icon\" : \"cloudy\",\n" +
            "                    \"precipIntensity\" : 0.0023,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 29.03,\n" +
            "                    \"precipProbability\" : 0.18\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 438.08,\n" +
            "                    \"windGust\" : 21.11,\n" +
            "                    \"temperature\" : 32.01,\n" +
            "                    \"precipAccumulation\" : 0.004,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.86,\n" +
            "                    \"cloudCover\" : 0.99,\n" +
            "                    \"summary\" : \"Overcast\",\n" +
            "                    \"apparentTemperature\" : 23.94,\n" +
            "                    \"pressure\" : 999.63,\n" +
            "                    \"windSpeed\" : 9.62,\n" +
            "                    \"windBearing\" : 304,\n" +
            "                    \"time\" : 1521741600,\n" +
            "                    \"icon\" : \"cloudy\",\n" +
            "                    \"precipIntensity\" : 0.0006,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 28.4,\n" +
            "                    \"precipProbability\" : 0.1\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 438.8,\n" +
            "                    \"windGust\" : 20.41,\n" +
            "                    \"temperature\" : 30.88,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.86,\n" +
            "                    \"cloudCover\" : 0.93,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 23.02,\n" +
            "                    \"pressure\" : 1000.17,\n" +
            "                    \"windSpeed\" : 8.78,\n" +
            "                    \"windBearing\" : 300,\n" +
            "                    \"time\" : 1521745200,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"precipIntensity\" : 0.0002,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 27.28,\n" +
            "                    \"precipProbability\" : 0.05\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 438.73,\n" +
            "                    \"windGust\" : 20.06,\n" +
            "                    \"temperature\" : 29.61,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"dewPoint\" : 25.86,\n" +
            "                    \"humidity\" : 0.86,\n" +
            "                    \"cloudCover\" : 0.85,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 21.41,\n" +
            "                    \"pressure\" : 1000.56,\n" +
            "                    \"windSpeed\" : 8.85,\n" +
            "                    \"time\" : 1521748800,\n" +
            "                    \"windBearing\" : 308,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 438.82,\n" +
            "                    \"windGust\" : 19.6,\n" +
            "                    \"temperature\" : 28.07,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"dewPoint\" : 24.54,\n" +
            "                    \"humidity\" : 0.86,\n" +
            "                    \"cloudCover\" : 0.77,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 19.62,\n" +
            "                    \"pressure\" : 1000.83,\n" +
            "                    \"windSpeed\" : 8.7,\n" +
            "                    \"time\" : 1521752400,\n" +
            "                    \"windBearing\" : 315,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 439.64,\n" +
            "                    \"windGust\" : 18.83,\n" +
            "                    \"temperature\" : 26.47,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"dewPoint\" : 23.46,\n" +
            "                    \"humidity\" : 0.88,\n" +
            "                    \"cloudCover\" : 0.72,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 17.9,\n" +
            "                    \"pressure\" : 1000.95,\n" +
            "                    \"windSpeed\" : 8.32,\n" +
            "                    \"time\" : 1521756000,\n" +
            "                    \"windBearing\" : 315,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 440.53,\n" +
            "                    \"windGust\" : 17.97,\n" +
            "                    \"temperature\" : 25.16,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"dewPoint\" : 22.41,\n" +
            "                    \"humidity\" : 0.89,\n" +
            "                    \"cloudCover\" : 0.68,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 16.52,\n" +
            "                    \"pressure\" : 1000.94,\n" +
            "                    \"windSpeed\" : 7.99,\n" +
            "                    \"time\" : 1521759600,\n" +
            "                    \"windBearing\" : 312,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 441.08,\n" +
            "                    \"windGust\" : 17.13,\n" +
            "                    \"temperature\" : 24.45,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"dewPoint\" : 21.4,\n" +
            "                    \"humidity\" : 0.88,\n" +
            "                    \"cloudCover\" : 0.59,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 15.8,\n" +
            "                    \"pressure\" : 1000.97,\n" +
            "                    \"windSpeed\" : 7.79,\n" +
            "                    \"time\" : 1521763200,\n" +
            "                    \"windBearing\" : 311,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 440.82,\n" +
            "                    \"windGust\" : 16.07,\n" +
            "                    \"temperature\" : 24.1,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"dewPoint\" : 20.25,\n" +
            "                    \"humidity\" : 0.85,\n" +
            "                    \"cloudCover\" : 0.4,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 15.35,\n" +
            "                    \"pressure\" : 1001.08,\n" +
            "                    \"windSpeed\" : 7.83,\n" +
            "                    \"time\" : 1521766800,\n" +
            "                    \"windBearing\" : 315,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 440.05,\n" +
            "                    \"windGust\" : 15.02,\n" +
            "                    \"temperature\" : 23.69,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.83,\n" +
            "                    \"cloudCover\" : 0.17,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 14.74,\n" +
            "                    \"pressure\" : 1001.21,\n" +
            "                    \"windSpeed\" : 7.99,\n" +
            "                    \"windBearing\" : 320,\n" +
            "                    \"time\" : 1521770400,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"precipIntensity\" : 0.0001,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 19.16,\n" +
            "                    \"precipProbability\" : 0.02\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 439.61,\n" +
            "                    \"windGust\" : 14.71,\n" +
            "                    \"temperature\" : 22.76,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.84,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 13.51,\n" +
            "                    \"pressure\" : 1001.37,\n" +
            "                    \"windSpeed\" : 8.12,\n" +
            "                    \"windBearing\" : 324,\n" +
            "                    \"time\" : 1521774000,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"precipIntensity\" : 0.0001,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 18.55,\n" +
            "                    \"precipProbability\" : 0.02\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 439.72,\n" +
            "                    \"windGust\" : 15.91,\n" +
            "                    \"temperature\" : 21.94,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.87,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 12.43,\n" +
            "                    \"pressure\" : 1001.58,\n" +
            "                    \"windSpeed\" : 8.22,\n" +
            "                    \"windBearing\" : 327,\n" +
            "                    \"time\" : 1521777600,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"precipIntensity\" : 0.0001,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 18.68,\n" +
            "                    \"precipProbability\" : 0.02\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 440.11,\n" +
            "                    \"windGust\" : 17.85,\n" +
            "                    \"temperature\" : 21.42,\n" +
            "                    \"icon\" : \"clear-day\",\n" +
            "                    \"dewPoint\" : 19.26,\n" +
            "                    \"humidity\" : 0.91,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 11.75,\n" +
            "                    \"pressure\" : 1001.8,\n" +
            "                    \"windSpeed\" : 8.29,\n" +
            "                    \"time\" : 1521781200,\n" +
            "                    \"windBearing\" : 330,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 440.11,\n" +
            "                    \"windGust\" : 18.89,\n" +
            "                    \"temperature\" : 21.86,\n" +
            "                    \"icon\" : \"clear-day\",\n" +
            "                    \"dewPoint\" : 20.1,\n" +
            "                    \"humidity\" : 0.93,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 12.37,\n" +
            "                    \"pressure\" : 1002.01,\n" +
            "                    \"windSpeed\" : 8.17,\n" +
            "                    \"time\" : 1521784800,\n" +
            "                    \"windBearing\" : 332,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 439.47,\n" +
            "                    \"windGust\" : 17.99,\n" +
            "                    \"temperature\" : 23.44,\n" +
            "                    \"icon\" : \"clear-day\",\n" +
            "                    \"dewPoint\" : 21.28,\n" +
            "                    \"humidity\" : 0.91,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 14.69,\n" +
            "                    \"pressure\" : 1002.18,\n" +
            "                    \"windSpeed\" : 7.64,\n" +
            "                    \"time\" : 1521788400,\n" +
            "                    \"windBearing\" : 333,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 438.61,\n" +
            "                    \"windGust\" : 16.22,\n" +
            "                    \"temperature\" : 25.76,\n" +
            "                    \"icon\" : \"clear-day\",\n" +
            "                    \"dewPoint\" : 22.67,\n" +
            "                    \"humidity\" : 0.88,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 18.03,\n" +
            "                    \"pressure\" : 1002.33,\n" +
            "                    \"windSpeed\" : 6.93,\n" +
            "                    \"time\" : 1521792000,\n" +
            "                    \"windBearing\" : 333,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 437.77,\n" +
            "                    \"windGust\" : 15.06,\n" +
            "                    \"temperature\" : 27.61,\n" +
            "                    \"icon\" : \"clear-day\",\n" +
            "                    \"dewPoint\" : 23.59,\n" +
            "                    \"humidity\" : 0.85,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 20.48,\n" +
            "                    \"pressure\" : 1002.51,\n" +
            "                    \"windSpeed\" : 6.64,\n" +
            "                    \"time\" : 1521795600,\n" +
            "                    \"windBearing\" : 331,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 437.49,\n" +
            "                    \"windGust\" : 15.34,\n" +
            "                    \"temperature\" : 29.03,\n" +
            "                    \"icon\" : \"clear-day\",\n" +
            "                    \"dewPoint\" : 23.88,\n" +
            "                    \"humidity\" : 0.81,\n" +
            "                    \"cloudCover\" : 0.17,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 21.8,\n" +
            "                    \"pressure\" : 1002.67,\n" +
            "                    \"windSpeed\" : 7.17,\n" +
            "                    \"time\" : 1521799200,\n" +
            "                    \"windBearing\" : 325,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 437.27,\n" +
            "                    \"windGust\" : 16.28,\n" +
            "                    \"temperature\" : 30.19,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.77,\n" +
            "                    \"cloudCover\" : 0.41,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 22.59,\n" +
            "                    \"pressure\" : 1002.84,\n" +
            "                    \"windSpeed\" : 8.09,\n" +
            "                    \"windBearing\" : 317,\n" +
            "                    \"time\" : 1521802800,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.0001,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 23.75,\n" +
            "                    \"precipProbability\" : 0.04\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 436.74,\n" +
            "                    \"windGust\" : 16.83,\n" +
            "                    \"temperature\" : 31.36,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.71,\n" +
            "                    \"cloudCover\" : 0.54,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 23.7,\n" +
            "                    \"pressure\" : 1003.16,\n" +
            "                    \"windSpeed\" : 8.61,\n" +
            "                    \"windBearing\" : 313,\n" +
            "                    \"time\" : 1521806400,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.0001,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 23.13,\n" +
            "                    \"precipProbability\" : 0.05\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 435.6,\n" +
            "                    \"windGust\" : 16.5,\n" +
            "                    \"temperature\" : 32.31,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.65,\n" +
            "                    \"cloudCover\" : 0.47,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 25.05,\n" +
            "                    \"pressure\" : 1003.74,\n" +
            "                    \"windSpeed\" : 8.29,\n" +
            "                    \"windBearing\" : 316,\n" +
            "                    \"time\" : 1521810000,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.0001,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 21.71,\n" +
            "                    \"precipProbability\" : 0.05\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 434.2,\n" +
            "                    \"windGust\" : 15.8,\n" +
            "                    \"temperature\" : 32.68,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"dewPoint\" : 19.72,\n" +
            "                    \"humidity\" : 0.58,\n" +
            "                    \"cloudCover\" : 0.29,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 25.93,\n" +
            "                    \"pressure\" : 1004.47,\n" +
            "                    \"windSpeed\" : 7.58,\n" +
            "                    \"time\" : 1521813600,\n" +
            "                    \"windBearing\" : 321,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 432.87,\n" +
            "                    \"windGust\" : 15.26,\n" +
            "                    \"temperature\" : 32.13,\n" +
            "                    \"icon\" : \"clear-day\",\n" +
            "                    \"dewPoint\" : 18.01,\n" +
            "                    \"humidity\" : 0.55,\n" +
            "                    \"cloudCover\" : 0.14,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 25.63,\n" +
            "                    \"pressure\" : 1005.1,\n" +
            "                    \"windSpeed\" : 7.04,\n" +
            "                    \"time\" : 1521817200,\n" +
            "                    \"windBearing\" : 328,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 432.01,\n" +
            "                    \"windGust\" : 15.28,\n" +
            "                    \"temperature\" : 30.75,\n" +
            "                    \"icon\" : \"clear-day\",\n" +
            "                    \"dewPoint\" : 17.01,\n" +
            "                    \"humidity\" : 0.56,\n" +
            "                    \"cloudCover\" : 0.07,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 24.12,\n" +
            "                    \"pressure\" : 1005.57,\n" +
            "                    \"windSpeed\" : 6.82,\n" +
            "                    \"time\" : 1521820800,\n" +
            "                    \"windBearing\" : 338,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 431.26,\n" +
            "                    \"windGust\" : 15.5,\n" +
            "                    \"temperature\" : 28.8,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"dewPoint\" : 16.34,\n" +
            "                    \"humidity\" : 0.59,\n" +
            "                    \"cloudCover\" : 0.03,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 21.93,\n" +
            "                    \"pressure\" : 1005.94,\n" +
            "                    \"windSpeed\" : 6.61,\n" +
            "                    \"time\" : 1521824400,\n" +
            "                    \"windBearing\" : 350,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 430.33,\n" +
            "                    \"windGust\" : 15.26,\n" +
            "                    \"temperature\" : 26.86,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"dewPoint\" : 15.8,\n" +
            "                    \"humidity\" : 0.63,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 19.84,\n" +
            "                    \"pressure\" : 1006.31,\n" +
            "                    \"windSpeed\" : 6.32,\n" +
            "                    \"time\" : 1521828000,\n" +
            "                    \"windBearing\" : 2,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 428.91,\n" +
            "                    \"windGust\" : 14.37,\n" +
            "                    \"temperature\" : 25.29,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"dewPoint\" : 15.27,\n" +
            "                    \"humidity\" : 0.65,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 18.27,\n" +
            "                    \"pressure\" : 1006.74,\n" +
            "                    \"windSpeed\" : 5.97,\n" +
            "                    \"time\" : 1521831600,\n" +
            "                    \"windBearing\" : 11,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 427.33,\n" +
            "                    \"windGust\" : 13.02,\n" +
            "                    \"temperature\" : 23.5,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"dewPoint\" : 14.84,\n" +
            "                    \"humidity\" : 0.69,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 16.46,\n" +
            "                    \"pressure\" : 1007.15,\n" +
            "                    \"windSpeed\" : 5.62,\n" +
            "                    \"time\" : 1521835200,\n" +
            "                    \"windBearing\" : 21,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 425.84,\n" +
            "                    \"windGust\" : 11.31,\n" +
            "                    \"temperature\" : 21.94,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"dewPoint\" : 14.52,\n" +
            "                    \"humidity\" : 0.73,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 14.97,\n" +
            "                    \"pressure\" : 1007.42,\n" +
            "                    \"windSpeed\" : 5.28,\n" +
            "                    \"time\" : 1521838800,\n" +
            "                    \"windBearing\" : 33,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 424.54,\n" +
            "                    \"windGust\" : 8.87,\n" +
            "                    \"temperature\" : 20.97,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"dewPoint\" : 14.17,\n" +
            "                    \"humidity\" : 0.75,\n" +
            "                    \"cloudCover\" : 0.03,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 14.13,\n" +
            "                    \"pressure\" : 1007.61,\n" +
            "                    \"windSpeed\" : 4.99,\n" +
            "                    \"time\" : 1521842400,\n" +
            "                    \"windBearing\" : 50,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 423.36,\n" +
            "                    \"windGust\" : 6.06,\n" +
            "                    \"temperature\" : 20.99,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"dewPoint\" : 13.82,\n" +
            "                    \"humidity\" : 0.73,\n" +
            "                    \"cloudCover\" : 0.08,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 14.41,\n" +
            "                    \"pressure\" : 1007.66,\n" +
            "                    \"windSpeed\" : 4.77,\n" +
            "                    \"time\" : 1521846000,\n" +
            "                    \"windBearing\" : 69,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }\n" +
            "            ],\n" +
            "            \"summary\" : \"Mostly cloudy throughout the day.\"\n" +
            "        },\n" +
            "        \"currently\" : {\n" +
            "            \"ozone\" : 435.79,\n" +
            "            \"windGust\" : 18.8,\n" +
            "            \"temperature\" : 29.06,\n" +
            "            \"icon\" : \"partly-cloudy-night\",\n" +
            "            \"precipType\" : \"snow\",\n" +
            "            \"humidity\" : 0.94,\n" +
            "            \"cloudCover\" : 0.45,\n" +
            "            \"summary\" : \"Partly Cloudy\",\n" +
            "            \"apparentTemperature\" : 18.08,\n" +
            "            \"pressure\" : 1005.22,\n" +
            "            \"windSpeed\" : 14.32,\n" +
            "            \"visibility\" : 6.22,\n" +
            "            \"time\" : 1521673277,\n" +
            "            \"windBearing\" : 216,\n" +
            "            \"precipIntensity\" : 0.0013,\n" +
            "            \"uvIndex\" : 0,\n" +
            "            \"dewPoint\" : 27.61,\n" +
            "            \"precipProbability\" : 0.07\n" +
            "        },\n" +
            "        \"daily\" : {\n" +
            "            \"icon\" : \"snow\",\n" +
            "            \"data\" : [ \n" +
            "                {\n" +
            "                    \"apparentTemperatureMinTime\" : 1521676800,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"temperatureLow\" : 21.42,\n" +
            "                    \"precipIntensityMaxTime\" : 1521730800,\n" +
            "                    \"temperatureMin\" : 27.19,\n" +
            "                    \"temperatureHigh\" : 36.74,\n" +
            "                    \"summary\" : \"Mostly cloudy throughout the day.\",\n" +
            "                    \"dewPoint\" : 28.53,\n" +
            "                    \"apparentTemperatureMax\" : 28.88,\n" +
            "                    \"temperatureMax\" : 36.74,\n" +
            "                    \"windSpeed\" : 9.42,\n" +
            "                    \"apparentTemperatureHigh\" : 28.88,\n" +
            "                    \"windGustTime\" : 1521709200,\n" +
            "                    \"temperatureMaxTime\" : 1521723600,\n" +
            "                    \"windBearing\" : 251,\n" +
            "                    \"temperatureLowTime\" : 1521781200,\n" +
            "                    \"moonPhase\" : 0.17,\n" +
            "                    \"precipAccumulation\" : 0.23,\n" +
            "                    \"temperatureMinTime\" : 1521680400,\n" +
            "                    \"windGust\" : 31.15,\n" +
            "                    \"apparentTemperatureLow\" : 11.75,\n" +
            "                    \"sunsetTime\" : 1521736792,\n" +
            "                    \"pressure\" : 1000.11,\n" +
            "                    \"uvIndexTime\" : 1521705600,\n" +
            "                    \"cloudCover\" : 0.78,\n" +
            "                    \"apparentTemperatureLowTime\" : 1521781200,\n" +
            "                    \"apparentTemperatureMin\" : 17.21,\n" +
            "                    \"apparentTemperatureHighTime\" : 1521723600,\n" +
            "                    \"precipIntensityMax\" : 0.0096,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"apparentTemperatureMaxTime\" : 1521723600,\n" +
            "                    \"humidity\" : 0.89,\n" +
            "                    \"ozone\" : 430.65,\n" +
            "                    \"temperatureHighTime\" : 1521723600,\n" +
            "                    \"time\" : 1521669600,\n" +
            "                    \"precipIntensity\" : 0.0016,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"sunriseTime\" : 1521692211,\n" +
            "                    \"precipProbability\" : 0.33\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"apparentTemperatureMinTime\" : 1521781200,\n" +
            "                    \"apparentTemperatureLow\" : 14.13,\n" +
            "                    \"temperatureLow\" : 20.97,\n" +
            "                    \"precipIntensityMaxTime\" : 1521770400,\n" +
            "                    \"temperatureMin\" : 21.42,\n" +
            "                    \"temperatureHigh\" : 32.68,\n" +
            "                    \"summary\" : \"Partly cloudy in the afternoon.\",\n" +
            "                    \"dewPoint\" : 19.78,\n" +
            "                    \"apparentTemperatureMax\" : 25.93,\n" +
            "                    \"temperatureMax\" : 32.68,\n" +
            "                    \"windSpeed\" : 6.96,\n" +
            "                    \"apparentTemperatureHigh\" : 25.93,\n" +
            "                    \"windGustTime\" : 1521784800,\n" +
            "                    \"temperatureMaxTime\" : 1521813600,\n" +
            "                    \"windBearing\" : 330,\n" +
            "                    \"temperatureLowTime\" : 1521842400,\n" +
            "                    \"moonPhase\" : 0.21,\n" +
            "                    \"temperatureMinTime\" : 1521781200,\n" +
            "                    \"windGust\" : 18.89,\n" +
            "                    \"sunsetTime\" : 1521823338,\n" +
            "                    \"pressure\" : 1003.33,\n" +
            "                    \"uvIndexTime\" : 1521788400,\n" +
            "                    \"cloudCover\" : 0.19,\n" +
            "                    \"apparentTemperatureLowTime\" : 1521842400,\n" +
            "                    \"apparentTemperatureMin\" : 11.75,\n" +
            "                    \"apparentTemperatureHighTime\" : 1521813600,\n" +
            "                    \"precipIntensityMax\" : 0.0001,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"apparentTemperatureMaxTime\" : 1521813600,\n" +
            "                    \"humidity\" : 0.77,\n" +
            "                    \"ozone\" : 436.14,\n" +
            "                    \"temperatureHighTime\" : 1521813600,\n" +
            "                    \"time\" : 1521756000,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"sunriseTime\" : 1521778426,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"apparentTemperatureMinTime\" : 1521842400,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"temperatureLow\" : 26.14,\n" +
            "                    \"precipIntensityMaxTime\" : 1521914400,\n" +
            "                    \"temperatureMin\" : 20.97,\n" +
            "                    \"temperatureHigh\" : 34.56,\n" +
            "                    \"summary\" : \"Mostly cloudy throughout the day.\",\n" +
            "                    \"dewPoint\" : 21.75,\n" +
            "                    \"apparentTemperatureMax\" : 28.31,\n" +
            "                    \"temperatureMax\" : 34.56,\n" +
            "                    \"windSpeed\" : 2.83,\n" +
            "                    \"apparentTemperatureHigh\" : 26.7,\n" +
            "                    \"windGustTime\" : 1521910800,\n" +
            "                    \"temperatureMaxTime\" : 1521907200,\n" +
            "                    \"windBearing\" : 126,\n" +
            "                    \"temperatureLowTime\" : 1521939600,\n" +
            "                    \"moonPhase\" : 0.24,\n" +
            "                    \"precipAccumulation\" : 0.487,\n" +
            "                    \"temperatureMinTime\" : 1521842400,\n" +
            "                    \"windGust\" : 30.68,\n" +
            "                    \"apparentTemperatureLow\" : 17.7,\n" +
            "                    \"sunsetTime\" : 1521909885,\n" +
            "                    \"pressure\" : 1003.69,\n" +
            "                    \"uvIndexTime\" : 1521878400,\n" +
            "                    \"cloudCover\" : 0.82,\n" +
            "                    \"apparentTemperatureLowTime\" : 1521939600,\n" +
            "                    \"apparentTemperatureMin\" : 14.13,\n" +
            "                    \"apparentTemperatureHighTime\" : 1521910800,\n" +
            "                    \"precipIntensityMax\" : 0.008,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"apparentTemperatureMaxTime\" : 1521918000,\n" +
            "                    \"humidity\" : 0.77,\n" +
            "                    \"ozone\" : 412.39,\n" +
            "                    \"temperatureHighTime\" : 1521907200,\n" +
            "                    \"time\" : 1521842400,\n" +
            "                    \"precipIntensity\" : 0.0025,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"sunriseTime\" : 1521864642,\n" +
            "                    \"precipProbability\" : 0.3\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"apparentTemperatureMinTime\" : 1521939600,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"temperatureLow\" : 28.1,\n" +
            "                    \"precipIntensityMaxTime\" : 1522008000,\n" +
            "                    \"temperatureMin\" : 26.14,\n" +
            "                    \"temperatureHigh\" : 36.68,\n" +
            "                    \"summary\" : \"Snow (1–2 in.) starting in the evening.\",\n" +
            "                    \"dewPoint\" : 29.16,\n" +
            "                    \"apparentTemperatureMax\" : 27.96,\n" +
            "                    \"temperatureMax\" : 36.68,\n" +
            "                    \"windSpeed\" : 9.18,\n" +
            "                    \"apparentTemperatureHigh\" : 27.96,\n" +
            "                    \"windGustTime\" : 1521986400,\n" +
            "                    \"temperatureMaxTime\" : 1521982800,\n" +
            "                    \"windBearing\" : 235,\n" +
            "                    \"temperatureLowTime\" : 1522033200,\n" +
            "                    \"moonPhase\" : 0.28,\n" +
            "                    \"precipAccumulation\" : 0.607,\n" +
            "                    \"temperatureMinTime\" : 1521939600,\n" +
            "                    \"windGust\" : 35.79,\n" +
            "                    \"apparentTemperatureLow\" : 17.37,\n" +
            "                    \"sunsetTime\" : 1521996431,\n" +
            "                    \"pressure\" : 1005.76,\n" +
            "                    \"uvIndexTime\" : 1521964800,\n" +
            "                    \"cloudCover\" : 0.87,\n" +
            "                    \"apparentTemperatureLowTime\" : 1522033200,\n" +
            "                    \"apparentTemperatureMin\" : 17.7,\n" +
            "                    \"apparentTemperatureHighTime\" : 1521979200,\n" +
            "                    \"precipIntensityMax\" : 0.0204,\n" +
            "                    \"icon\" : \"snow\",\n" +
            "                    \"apparentTemperatureMaxTime\" : 1521979200,\n" +
            "                    \"humidity\" : 0.91,\n" +
            "                    \"ozone\" : 397.47,\n" +
            "                    \"temperatureHighTime\" : 1521982800,\n" +
            "                    \"time\" : 1521928800,\n" +
            "                    \"precipIntensity\" : 0.0039,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"sunriseTime\" : 1521950858,\n" +
            "                    \"precipProbability\" : 0.37\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"apparentTemperatureMinTime\" : 1522033200,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"temperatureLow\" : 29.02,\n" +
            "                    \"precipIntensityMaxTime\" : 1522033200,\n" +
            "                    \"temperatureMin\" : 28.1,\n" +
            "                    \"temperatureHigh\" : 34.73,\n" +
            "                    \"summary\" : \"Snow (1–2 in.) in the morning.\",\n" +
            "                    \"dewPoint\" : 29.34,\n" +
            "                    \"apparentTemperatureMax\" : 34.73,\n" +
            "                    \"temperatureMax\" : 34.73,\n" +
            "                    \"windSpeed\" : 1.49,\n" +
            "                    \"apparentTemperatureHigh\" : 34.73,\n" +
            "                    \"windGustTime\" : 1522033200,\n" +
            "                    \"temperatureMaxTime\" : 1522065600,\n" +
            "                    \"windBearing\" : 227,\n" +
            "                    \"temperatureLowTime\" : 1522101600,\n" +
            "                    \"moonPhase\" : 0.32,\n" +
            "                    \"precipAccumulation\" : 2.122,\n" +
            "                    \"temperatureMinTime\" : 1522033200,\n" +
            "                    \"windGust\" : 27.77,\n" +
            "                    \"apparentTemperatureLow\" : 20.5,\n" +
            "                    \"sunsetTime\" : 1522082971,\n" +
            "                    \"pressure\" : 1001.85,\n" +
            "                    \"uvIndexTime\" : 1522051200,\n" +
            "                    \"cloudCover\" : 1,\n" +
            "                    \"apparentTemperatureLowTime\" : 1522098000,\n" +
            "                    \"apparentTemperatureMin\" : 17.37,\n" +
            "                    \"apparentTemperatureHighTime\" : 1522065600,\n" +
            "                    \"precipIntensityMax\" : 0.0313,\n" +
            "                    \"icon\" : \"snow\",\n" +
            "                    \"apparentTemperatureMaxTime\" : 1522065600,\n" +
            "                    \"humidity\" : 0.9,\n" +
            "                    \"ozone\" : 419.33,\n" +
            "                    \"temperatureHighTime\" : 1522065600,\n" +
            "                    \"time\" : 1522011600,\n" +
            "                    \"precipIntensity\" : 0.0103,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"sunriseTime\" : 1522037080,\n" +
            "                    \"precipProbability\" : 0.63\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"apparentTemperatureMinTime\" : 1522098000,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"temperatureLow\" : 19.43,\n" +
            "                    \"precipIntensityMaxTime\" : 1522098000,\n" +
            "                    \"temperatureMin\" : 29.02,\n" +
            "                    \"temperatureHigh\" : 33.67,\n" +
            "                    \"summary\" : \"Overcast throughout the day.\",\n" +
            "                    \"dewPoint\" : 26.42,\n" +
            "                    \"apparentTemperatureMax\" : 30.26,\n" +
            "                    \"temperatureMax\" : 33.67,\n" +
            "                    \"windSpeed\" : 3.72,\n" +
            "                    \"apparentTemperatureHigh\" : 30.26,\n" +
            "                    \"windGustTime\" : 1522098000,\n" +
            "                    \"temperatureMaxTime\" : 1522144800,\n" +
            "                    \"windBearing\" : 158,\n" +
            "                    \"temperatureLowTime\" : 1522213200,\n" +
            "                    \"moonPhase\" : 0.36,\n" +
            "                    \"precipAccumulation\" : 0.215,\n" +
            "                    \"temperatureMinTime\" : 1522101600,\n" +
            "                    \"windGust\" : 19.94,\n" +
            "                    \"apparentTemperatureLow\" : 7.25,\n" +
            "                    \"sunsetTime\" : 1522169517,\n" +
            "                    \"pressure\" : 1005.97,\n" +
            "                    \"uvIndexTime\" : 1522134000,\n" +
            "                    \"cloudCover\" : 0.96,\n" +
            "                    \"apparentTemperatureLowTime\" : 1522213200,\n" +
            "                    \"apparentTemperatureMin\" : 20.5,\n" +
            "                    \"apparentTemperatureHighTime\" : 1522141200,\n" +
            "                    \"precipIntensityMax\" : 0.0049,\n" +
            "                    \"icon\" : \"cloudy\",\n" +
            "                    \"apparentTemperatureMaxTime\" : 1522141200,\n" +
            "                    \"humidity\" : 0.83,\n" +
            "                    \"ozone\" : 420.25,\n" +
            "                    \"temperatureHighTime\" : 1522144800,\n" +
            "                    \"time\" : 1522098000,\n" +
            "                    \"precipIntensity\" : 0.001,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"sunriseTime\" : 1522123296,\n" +
            "                    \"precipProbability\" : 0.3\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"apparentTemperatureMinTime\" : 1522216800,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"temperatureLow\" : 19.76,\n" +
            "                    \"precipIntensityMaxTime\" : 1522195200,\n" +
            "                    \"temperatureMin\" : 18.93,\n" +
            "                    \"temperatureHigh\" : 28.6,\n" +
            "                    \"summary\" : \"Mostly cloudy throughout the day.\",\n" +
            "                    \"dewPoint\" : 16.54,\n" +
            "                    \"apparentTemperatureMax\" : 21.95,\n" +
            "                    \"temperatureMax\" : 28.73,\n" +
            "                    \"windSpeed\" : 9.28,\n" +
            "                    \"apparentTemperatureHigh\" : 20.93,\n" +
            "                    \"windGustTime\" : 1522263600,\n" +
            "                    \"temperatureMaxTime\" : 1522184400,\n" +
            "                    \"windBearing\" : 66,\n" +
            "                    \"temperatureLowTime\" : 1522274400,\n" +
            "                    \"moonPhase\" : 0.39,\n" +
            "                    \"precipAccumulation\" : 0.609,\n" +
            "                    \"temperatureMinTime\" : 1522216800,\n" +
            "                    \"windGust\" : 27.23,\n" +
            "                    \"apparentTemperatureLow\" : 7.34,\n" +
            "                    \"sunsetTime\" : 1522256064,\n" +
            "                    \"pressure\" : 1004.48,\n" +
            "                    \"uvIndexTime\" : 1522220400,\n" +
            "                    \"cloudCover\" : 0.67,\n" +
            "                    \"apparentTemperatureLowTime\" : 1522278000,\n" +
            "                    \"apparentTemperatureMin\" : 6.63,\n" +
            "                    \"apparentTemperatureHighTime\" : 1522245600,\n" +
            "                    \"precipIntensityMax\" : 0.0078,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"apparentTemperatureMaxTime\" : 1522184400,\n" +
            "                    \"humidity\" : 0.72,\n" +
            "                    \"ozone\" : 495.93,\n" +
            "                    \"temperatureHighTime\" : 1522238400,\n" +
            "                    \"time\" : 1522184400,\n" +
            "                    \"precipIntensity\" : 0.0019,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"sunriseTime\" : 1522209512,\n" +
            "                    \"precipProbability\" : 0.3\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"apparentTemperatureMinTime\" : 1522278000,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"temperatureLow\" : 20.66,\n" +
            "                    \"precipIntensityMaxTime\" : 1522292400,\n" +
            "                    \"temperatureMin\" : 19.76,\n" +
            "                    \"temperatureHigh\" : 28.04,\n" +
            "                    \"summary\" : \"Snow (< 1 in.) overnight.\",\n" +
            "                    \"dewPoint\" : 18.92,\n" +
            "                    \"apparentTemperatureMax\" : 20.89,\n" +
            "                    \"temperatureMax\" : 32.22,\n" +
            "                    \"windSpeed\" : 12.89,\n" +
            "                    \"apparentTemperatureHigh\" : 16.72,\n" +
            "                    \"windGustTime\" : 1522353600,\n" +
            "                    \"temperatureMaxTime\" : 1522350000,\n" +
            "                    \"windBearing\" : 67,\n" +
            "                    \"temperatureLowTime\" : 1522386000,\n" +
            "                    \"moonPhase\" : 0.43,\n" +
            "                    \"precipAccumulation\" : 0.382,\n" +
            "                    \"temperatureMinTime\" : 1522274400,\n" +
            "                    \"windGust\" : 35.93,\n" +
            "                    \"apparentTemperatureLow\" : 8.12,\n" +
            "                    \"sunsetTime\" : 1522342610,\n" +
            "                    \"pressure\" : 1006.97,\n" +
            "                    \"uvIndexTime\" : 1522310400,\n" +
            "                    \"cloudCover\" : 0.98,\n" +
            "                    \"apparentTemperatureLowTime\" : 1522386000,\n" +
            "                    \"apparentTemperatureMin\" : 7.34,\n" +
            "                    \"apparentTemperatureHighTime\" : 1522317600,\n" +
            "                    \"precipIntensityMax\" : 0.0043,\n" +
            "                    \"icon\" : \"snow\",\n" +
            "                    \"apparentTemperatureMaxTime\" : 1522350000,\n" +
            "                    \"humidity\" : 0.75,\n" +
            "                    \"ozone\" : 436.62,\n" +
            "                    \"temperatureHighTime\" : 1522324800,\n" +
            "                    \"time\" : 1522270800,\n" +
            "                    \"precipIntensity\" : 0.0014,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"sunriseTime\" : 1522295728,\n" +
            "                    \"precipProbability\" : 0.24\n" +
            "                }\n" +
            "            ],\n" +
            "            \"summary\" : \"Light snow (2–5 in.) today through Monday, with temperatures falling to 29°F on Wednesday.\"\n" +
            "        },\n" +
            "        \"flags\" : {\n" +
            "            \"units\" : \"us\",\n" +
            "            \"sources\" : [ \n" +
            "                \"isd\", \n" +
            "                \"cmc\", \n" +
            "                \"gfs\", \n" +
            "                \"madis\"\n" +
            "            ],\n" +
            "            \"isd-stations\" : [ \n" +
            "                \"027030-99999\", \n" +
            "                \"027060-99999\", \n" +
            "                \"027580-99999\", \n" +
            "                \"027590-99999\", \n" +
            "                \"027940-99999\", \n" +
            "                \"027950-99999\", \n" +
            "                \"028290-99999\", \n" +
            "                \"029740-99999\", \n" +
            "                \"029750-99999\", \n" +
            "                \"029780-99999\", \n" +
            "                \"029840-99999\", \n" +
            "                \"029860-99999\", \n" +
            "                \"029870-99999\", \n" +
            "                \"029880-99999\", \n" +
            "                \"029910-99999\", \n" +
            "                \"260340-99999\"\n" +
            "            ]\n" +
            "        },\n" +
            "        \"longitude\" : 24.9383791,\n" +
            "        \"offset\" : 2,\n" +
            "        \"latitude\" : 60.1698557,\n" +
            "        \"timezone\" : \"Europe/Helsinki\"\n" +
            "    }";
    private final String FORECAST_2018_22_03 = "{\n" +
            "        \"hourly\" : {\n" +
            "            \"icon\" : \"partly-cloudy-day\",\n" +
            "            \"data\" : [ \n" +
            "                {\n" +
            "                    \"ozone\" : 435.84,\n" +
            "                    \"windGust\" : 18.81,\n" +
            "                    \"temperature\" : 29.09,\n" +
            "                    \"precipAccumulation\" : 0.012,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.94,\n" +
            "                    \"cloudCover\" : 0.44,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 18.11,\n" +
            "                    \"pressure\" : 1005.23,\n" +
            "                    \"windSpeed\" : 14.37,\n" +
            "                    \"windBearing\" : 216,\n" +
            "                    \"visibility\" : 6.22,\n" +
            "                    \"time\" : 1521673200,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"precipIntensity\" : 0.0013,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 27.62,\n" +
            "                    \"precipProbability\" : 0.07\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 433.44,\n" +
            "                    \"windGust\" : 18.39,\n" +
            "                    \"temperature\" : 27.64,\n" +
            "                    \"precipAccumulation\" : 0.01,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.98,\n" +
            "                    \"cloudCover\" : 0.63,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 17.21,\n" +
            "                    \"pressure\" : 1004.59,\n" +
            "                    \"windSpeed\" : 12.2,\n" +
            "                    \"windBearing\" : 221,\n" +
            "                    \"visibility\" : 6.22,\n" +
            "                    \"time\" : 1521676800,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"precipIntensity\" : 0.001,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 27.16,\n" +
            "                    \"precipProbability\" : 0.06\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 430.99,\n" +
            "                    \"windGust\" : 19.2,\n" +
            "                    \"temperature\" : 27.19,\n" +
            "                    \"precipAccumulation\" : 0.005,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 1,\n" +
            "                    \"cloudCover\" : 0.82,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 17.56,\n" +
            "                    \"pressure\" : 1003.91,\n" +
            "                    \"windSpeed\" : 10.37,\n" +
            "                    \"windBearing\" : 230,\n" +
            "                    \"visibility\" : 6.22,\n" +
            "                    \"time\" : 1521680400,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"precipIntensity\" : 0.0005,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 27.16,\n" +
            "                    \"precipProbability\" : 0.05\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 428.52,\n" +
            "                    \"windGust\" : 20.78,\n" +
            "                    \"temperature\" : 27.47,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 1,\n" +
            "                    \"cloudCover\" : 0.9,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 18.13,\n" +
            "                    \"pressure\" : 1003.21,\n" +
            "                    \"windSpeed\" : 9.98,\n" +
            "                    \"windBearing\" : 236,\n" +
            "                    \"visibility\" : 6.22,\n" +
            "                    \"time\" : 1521684000,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"precipIntensity\" : 0.0001,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 27.45,\n" +
            "                    \"precipProbability\" : 0.05\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 426.73,\n" +
            "                    \"windGust\" : 22.45,\n" +
            "                    \"temperature\" : 27.97,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"dewPoint\" : 27.82,\n" +
            "                    \"humidity\" : 0.99,\n" +
            "                    \"cloudCover\" : 0.9,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 18.47,\n" +
            "                    \"pressure\" : 1002.51,\n" +
            "                    \"windSpeed\" : 10.48,\n" +
            "                    \"time\" : 1521687600,\n" +
            "                    \"windBearing\" : 235,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 425.95,\n" +
            "                    \"windGust\" : 24.07,\n" +
            "                    \"temperature\" : 28.21,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"dewPoint\" : 28.01,\n" +
            "                    \"humidity\" : 0.99,\n" +
            "                    \"cloudCover\" : 0.91,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 18.52,\n" +
            "                    \"pressure\" : 1001.85,\n" +
            "                    \"windSpeed\" : 10.97,\n" +
            "                    \"time\" : 1521691200,\n" +
            "                    \"windBearing\" : 235,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 425.83,\n" +
            "                    \"windGust\" : 25.78,\n" +
            "                    \"temperature\" : 28.74,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"dewPoint\" : 28.25,\n" +
            "                    \"humidity\" : 0.98,\n" +
            "                    \"cloudCover\" : 0.93,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 18.91,\n" +
            "                    \"pressure\" : 1001.17,\n" +
            "                    \"windSpeed\" : 11.53,\n" +
            "                    \"time\" : 1521694800,\n" +
            "                    \"windBearing\" : 235,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 426.1,\n" +
            "                    \"windGust\" : 27.37,\n" +
            "                    \"temperature\" : 29.48,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"dewPoint\" : 28.6,\n" +
            "                    \"humidity\" : 0.96,\n" +
            "                    \"cloudCover\" : 0.9,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 19.54,\n" +
            "                    \"pressure\" : 1000.44,\n" +
            "                    \"windSpeed\" : 12.16,\n" +
            "                    \"time\" : 1521698400,\n" +
            "                    \"windBearing\" : 236,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 426.98,\n" +
            "                    \"windGust\" : 28.94,\n" +
            "                    \"temperature\" : 30.67,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.94,\n" +
            "                    \"cloudCover\" : 0.75,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 20.67,\n" +
            "                    \"pressure\" : 999.58,\n" +
            "                    \"windSpeed\" : 13.02,\n" +
            "                    \"windBearing\" : 239,\n" +
            "                    \"time\" : 1521702000,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.0001,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 29.13,\n" +
            "                    \"precipProbability\" : 0.07\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 428.22,\n" +
            "                    \"windGust\" : 30.37,\n" +
            "                    \"temperature\" : 32.16,\n" +
            "                    \"precipAccumulation\" : 0.005,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.91,\n" +
            "                    \"cloudCover\" : 0.55,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 22.22,\n" +
            "                    \"pressure\" : 998.66,\n" +
            "                    \"windSpeed\" : 13.86,\n" +
            "                    \"windBearing\" : 244,\n" +
            "                    \"time\" : 1521705600,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.0007,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 29.75,\n" +
            "                    \"precipProbability\" : 0.13\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 428.56,\n" +
            "                    \"windGust\" : 31.15,\n" +
            "                    \"temperature\" : 33.4,\n" +
            "                    \"precipAccumulation\" : 0.008,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.88,\n" +
            "                    \"cloudCover\" : 0.44,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 23.53,\n" +
            "                    \"pressure\" : 997.83,\n" +
            "                    \"windSpeed\" : 14.58,\n" +
            "                    \"windBearing\" : 238,\n" +
            "                    \"time\" : 1521709200,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.0011,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 30.2,\n" +
            "                    \"precipProbability\" : 0.17\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 426.61,\n" +
            "                    \"windGust\" : 30.97,\n" +
            "                    \"temperature\" : 34.25,\n" +
            "                    \"precipAccumulation\" : 0.005,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.85,\n" +
            "                    \"cloudCover\" : 0.5,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 25.58,\n" +
            "                    \"pressure\" : 997.06,\n" +
            "                    \"windSpeed\" : 12.09,\n" +
            "                    \"windBearing\" : 215,\n" +
            "                    \"time\" : 1521712800,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.0007,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 30.36,\n" +
            "                    \"precipProbability\" : 0.16\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 423.79,\n" +
            "                    \"windGust\" : 30.13,\n" +
            "                    \"temperature\" : 34.88,\n" +
            "                    \"precipAccumulation\" : 0.007,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.84,\n" +
            "                    \"cloudCover\" : 0.65,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 27.46,\n" +
            "                    \"pressure\" : 996.41,\n" +
            "                    \"windSpeed\" : 9.67,\n" +
            "                    \"windBearing\" : 296,\n" +
            "                    \"time\" : 1521716400,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.0009,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 30.4,\n" +
            "                    \"precipProbability\" : 0.16\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 422.55,\n" +
            "                    \"windGust\" : 29.1,\n" +
            "                    \"temperature\" : 35.34,\n" +
            "                    \"precipAccumulation\" : 0.015,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.82,\n" +
            "                    \"cloudCover\" : 0.78,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 26.74,\n" +
            "                    \"pressure\" : 996.06,\n" +
            "                    \"windSpeed\" : 12.6,\n" +
            "                    \"windBearing\" : 268,\n" +
            "                    \"time\" : 1521720000,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.002,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 30.34,\n" +
            "                    \"precipProbability\" : 0.19\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 424.34,\n" +
            "                    \"windGust\" : 28.08,\n" +
            "                    \"temperature\" : 36.74,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipType\" : \"rain\",\n" +
            "                    \"humidity\" : 0.77,\n" +
            "                    \"cloudCover\" : 0.86,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 28.88,\n" +
            "                    \"pressure\" : 996.21,\n" +
            "                    \"windSpeed\" : 11.65,\n" +
            "                    \"time\" : 1521723600,\n" +
            "                    \"windBearing\" : 249,\n" +
            "                    \"precipIntensity\" : 0.0033,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 30.19,\n" +
            "                    \"precipProbability\" : 0.27\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 427.68,\n" +
            "                    \"windGust\" : 26.89,\n" +
            "                    \"temperature\" : 36.16,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipType\" : \"rain\",\n" +
            "                    \"humidity\" : 0.78,\n" +
            "                    \"cloudCover\" : 0.92,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 28.28,\n" +
            "                    \"pressure\" : 996.68,\n" +
            "                    \"windSpeed\" : 11.34,\n" +
            "                    \"time\" : 1521727200,\n" +
            "                    \"windBearing\" : 296,\n" +
            "                    \"precipIntensity\" : 0.0069,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 29.96,\n" +
            "                    \"precipProbability\" : 0.33\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 430.87,\n" +
            "                    \"windGust\" : 25.6,\n" +
            "                    \"temperature\" : 35.27,\n" +
            "                    \"precipAccumulation\" : 0.073,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.8,\n" +
            "                    \"cloudCover\" : 0.95,\n" +
            "                    \"summary\" : \"Overcast\",\n" +
            "                    \"apparentTemperature\" : 26.53,\n" +
            "                    \"pressure\" : 997.28,\n" +
            "                    \"windSpeed\" : 12.93,\n" +
            "                    \"windBearing\" : 272,\n" +
            "                    \"time\" : 1521730800,\n" +
            "                    \"icon\" : \"cloudy\",\n" +
            "                    \"precipIntensity\" : 0.0096,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 29.66,\n" +
            "                    \"precipProbability\" : 0.34\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 433.64,\n" +
            "                    \"windGust\" : 24.05,\n" +
            "                    \"temperature\" : 34.46,\n" +
            "                    \"precipAccumulation\" : 0.049,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.81,\n" +
            "                    \"cloudCover\" : 0.98,\n" +
            "                    \"summary\" : \"Overcast\",\n" +
            "                    \"apparentTemperature\" : 28.83,\n" +
            "                    \"pressure\" : 998.02,\n" +
            "                    \"windSpeed\" : 6.46,\n" +
            "                    \"windBearing\" : 233,\n" +
            "                    \"time\" : 1521734400,\n" +
            "                    \"icon\" : \"cloudy\",\n" +
            "                    \"precipIntensity\" : 0.0065,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 29.36,\n" +
            "                    \"precipProbability\" : 0.28\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 436.22,\n" +
            "                    \"windGust\" : 22.42,\n" +
            "                    \"temperature\" : 33.17,\n" +
            "                    \"precipAccumulation\" : 0.017,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.85,\n" +
            "                    \"cloudCover\" : 0.99,\n" +
            "                    \"summary\" : \"Overcast\",\n" +
            "                    \"apparentTemperature\" : 26.62,\n" +
            "                    \"pressure\" : 998.89,\n" +
            "                    \"windSpeed\" : 7.44,\n" +
            "                    \"windBearing\" : 334,\n" +
            "                    \"time\" : 1521738000,\n" +
            "                    \"icon\" : \"cloudy\",\n" +
            "                    \"precipIntensity\" : 0.0023,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 29.03,\n" +
            "                    \"precipProbability\" : 0.18\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 438.08,\n" +
            "                    \"windGust\" : 21.11,\n" +
            "                    \"temperature\" : 32.01,\n" +
            "                    \"precipAccumulation\" : 0.004,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.86,\n" +
            "                    \"cloudCover\" : 0.99,\n" +
            "                    \"summary\" : \"Overcast\",\n" +
            "                    \"apparentTemperature\" : 23.94,\n" +
            "                    \"pressure\" : 999.63,\n" +
            "                    \"windSpeed\" : 9.62,\n" +
            "                    \"windBearing\" : 304,\n" +
            "                    \"time\" : 1521741600,\n" +
            "                    \"icon\" : \"cloudy\",\n" +
            "                    \"precipIntensity\" : 0.0006,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 28.4,\n" +
            "                    \"precipProbability\" : 0.1\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 438.8,\n" +
            "                    \"windGust\" : 20.41,\n" +
            "                    \"temperature\" : 30.88,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.86,\n" +
            "                    \"cloudCover\" : 0.93,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 23.02,\n" +
            "                    \"pressure\" : 1000.17,\n" +
            "                    \"windSpeed\" : 8.78,\n" +
            "                    \"windBearing\" : 300,\n" +
            "                    \"time\" : 1521745200,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"precipIntensity\" : 0.0002,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 27.28,\n" +
            "                    \"precipProbability\" : 0.05\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 438.73,\n" +
            "                    \"windGust\" : 20.06,\n" +
            "                    \"temperature\" : 29.61,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"dewPoint\" : 25.86,\n" +
            "                    \"humidity\" : 0.86,\n" +
            "                    \"cloudCover\" : 0.85,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 21.41,\n" +
            "                    \"pressure\" : 1000.56,\n" +
            "                    \"windSpeed\" : 8.85,\n" +
            "                    \"time\" : 1521748800,\n" +
            "                    \"windBearing\" : 308,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 438.82,\n" +
            "                    \"windGust\" : 19.6,\n" +
            "                    \"temperature\" : 28.07,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"dewPoint\" : 24.54,\n" +
            "                    \"humidity\" : 0.86,\n" +
            "                    \"cloudCover\" : 0.77,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 19.62,\n" +
            "                    \"pressure\" : 1000.83,\n" +
            "                    \"windSpeed\" : 8.7,\n" +
            "                    \"time\" : 1521752400,\n" +
            "                    \"windBearing\" : 315,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 439.64,\n" +
            "                    \"windGust\" : 18.83,\n" +
            "                    \"temperature\" : 26.47,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"dewPoint\" : 23.46,\n" +
            "                    \"humidity\" : 0.88,\n" +
            "                    \"cloudCover\" : 0.72,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 17.9,\n" +
            "                    \"pressure\" : 1000.95,\n" +
            "                    \"windSpeed\" : 8.32,\n" +
            "                    \"time\" : 1521756000,\n" +
            "                    \"windBearing\" : 315,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 440.53,\n" +
            "                    \"windGust\" : 17.97,\n" +
            "                    \"temperature\" : 25.16,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"dewPoint\" : 22.41,\n" +
            "                    \"humidity\" : 0.89,\n" +
            "                    \"cloudCover\" : 0.68,\n" +
            "                    \"summary\" : \"Mostly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 16.52,\n" +
            "                    \"pressure\" : 1000.94,\n" +
            "                    \"windSpeed\" : 7.99,\n" +
            "                    \"time\" : 1521759600,\n" +
            "                    \"windBearing\" : 312,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 441.08,\n" +
            "                    \"windGust\" : 17.13,\n" +
            "                    \"temperature\" : 24.45,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"dewPoint\" : 21.4,\n" +
            "                    \"humidity\" : 0.88,\n" +
            "                    \"cloudCover\" : 0.59,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 15.8,\n" +
            "                    \"pressure\" : 1000.97,\n" +
            "                    \"windSpeed\" : 7.79,\n" +
            "                    \"time\" : 1521763200,\n" +
            "                    \"windBearing\" : 311,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 440.82,\n" +
            "                    \"windGust\" : 16.07,\n" +
            "                    \"temperature\" : 24.1,\n" +
            "                    \"icon\" : \"partly-cloudy-night\",\n" +
            "                    \"dewPoint\" : 20.25,\n" +
            "                    \"humidity\" : 0.85,\n" +
            "                    \"cloudCover\" : 0.4,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 15.35,\n" +
            "                    \"pressure\" : 1001.08,\n" +
            "                    \"windSpeed\" : 7.83,\n" +
            "                    \"time\" : 1521766800,\n" +
            "                    \"windBearing\" : 315,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 440.05,\n" +
            "                    \"windGust\" : 15.02,\n" +
            "                    \"temperature\" : 23.69,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.83,\n" +
            "                    \"cloudCover\" : 0.17,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 14.74,\n" +
            "                    \"pressure\" : 1001.21,\n" +
            "                    \"windSpeed\" : 7.99,\n" +
            "                    \"windBearing\" : 320,\n" +
            "                    \"time\" : 1521770400,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"precipIntensity\" : 0.0001,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 19.16,\n" +
            "                    \"precipProbability\" : 0.02\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 439.61,\n" +
            "                    \"windGust\" : 14.71,\n" +
            "                    \"temperature\" : 22.76,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.84,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 13.51,\n" +
            "                    \"pressure\" : 1001.37,\n" +
            "                    \"windSpeed\" : 8.12,\n" +
            "                    \"windBearing\" : 324,\n" +
            "                    \"time\" : 1521774000,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"precipIntensity\" : 0.0001,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 18.55,\n" +
            "                    \"precipProbability\" : 0.02\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 439.72,\n" +
            "                    \"windGust\" : 15.91,\n" +
            "                    \"temperature\" : 21.94,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.87,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 12.43,\n" +
            "                    \"pressure\" : 1001.58,\n" +
            "                    \"windSpeed\" : 8.22,\n" +
            "                    \"windBearing\" : 327,\n" +
            "                    \"time\" : 1521777600,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"precipIntensity\" : 0.0001,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"dewPoint\" : 18.68,\n" +
            "                    \"precipProbability\" : 0.02\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 440.11,\n" +
            "                    \"windGust\" : 17.85,\n" +
            "                    \"temperature\" : 21.42,\n" +
            "                    \"icon\" : \"clear-day\",\n" +
            "                    \"dewPoint\" : 19.26,\n" +
            "                    \"humidity\" : 0.91,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 11.75,\n" +
            "                    \"pressure\" : 1001.8,\n" +
            "                    \"windSpeed\" : 8.29,\n" +
            "                    \"time\" : 1521781200,\n" +
            "                    \"windBearing\" : 330,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 440.11,\n" +
            "                    \"windGust\" : 18.89,\n" +
            "                    \"temperature\" : 21.86,\n" +
            "                    \"icon\" : \"clear-day\",\n" +
            "                    \"dewPoint\" : 20.1,\n" +
            "                    \"humidity\" : 0.93,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 12.37,\n" +
            "                    \"pressure\" : 1002.01,\n" +
            "                    \"windSpeed\" : 8.17,\n" +
            "                    \"time\" : 1521784800,\n" +
            "                    \"windBearing\" : 332,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 439.47,\n" +
            "                    \"windGust\" : 17.99,\n" +
            "                    \"temperature\" : 23.44,\n" +
            "                    \"icon\" : \"clear-day\",\n" +
            "                    \"dewPoint\" : 21.28,\n" +
            "                    \"humidity\" : 0.91,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 14.69,\n" +
            "                    \"pressure\" : 1002.18,\n" +
            "                    \"windSpeed\" : 7.64,\n" +
            "                    \"time\" : 1521788400,\n" +
            "                    \"windBearing\" : 333,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 438.61,\n" +
            "                    \"windGust\" : 16.22,\n" +
            "                    \"temperature\" : 25.76,\n" +
            "                    \"icon\" : \"clear-day\",\n" +
            "                    \"dewPoint\" : 22.67,\n" +
            "                    \"humidity\" : 0.88,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 18.03,\n" +
            "                    \"pressure\" : 1002.33,\n" +
            "                    \"windSpeed\" : 6.93,\n" +
            "                    \"time\" : 1521792000,\n" +
            "                    \"windBearing\" : 333,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 437.77,\n" +
            "                    \"windGust\" : 15.06,\n" +
            "                    \"temperature\" : 27.61,\n" +
            "                    \"icon\" : \"clear-day\",\n" +
            "                    \"dewPoint\" : 23.59,\n" +
            "                    \"humidity\" : 0.85,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 20.48,\n" +
            "                    \"pressure\" : 1002.51,\n" +
            "                    \"windSpeed\" : 6.64,\n" +
            "                    \"time\" : 1521795600,\n" +
            "                    \"windBearing\" : 331,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 437.49,\n" +
            "                    \"windGust\" : 15.34,\n" +
            "                    \"temperature\" : 29.03,\n" +
            "                    \"icon\" : \"clear-day\",\n" +
            "                    \"dewPoint\" : 23.88,\n" +
            "                    \"humidity\" : 0.81,\n" +
            "                    \"cloudCover\" : 0.17,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 21.8,\n" +
            "                    \"pressure\" : 1002.67,\n" +
            "                    \"windSpeed\" : 7.17,\n" +
            "                    \"time\" : 1521799200,\n" +
            "                    \"windBearing\" : 325,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 437.27,\n" +
            "                    \"windGust\" : 16.28,\n" +
            "                    \"temperature\" : 30.19,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.77,\n" +
            "                    \"cloudCover\" : 0.41,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 22.59,\n" +
            "                    \"pressure\" : 1002.84,\n" +
            "                    \"windSpeed\" : 8.09,\n" +
            "                    \"windBearing\" : 317,\n" +
            "                    \"time\" : 1521802800,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.0001,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 23.75,\n" +
            "                    \"precipProbability\" : 0.04\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 436.74,\n" +
            "                    \"windGust\" : 16.83,\n" +
            "                    \"temperature\" : 31.36,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.71,\n" +
            "                    \"cloudCover\" : 0.54,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 23.7,\n" +
            "                    \"pressure\" : 1003.16,\n" +
            "                    \"windSpeed\" : 8.61,\n" +
            "                    \"windBearing\" : 313,\n" +
            "                    \"time\" : 1521806400,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.0001,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 23.13,\n" +
            "                    \"precipProbability\" : 0.05\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 435.6,\n" +
            "                    \"windGust\" : 16.5,\n" +
            "                    \"temperature\" : 32.31,\n" +
            "                    \"precipAccumulation\" : 0,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"humidity\" : 0.65,\n" +
            "                    \"cloudCover\" : 0.47,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 25.05,\n" +
            "                    \"pressure\" : 1003.74,\n" +
            "                    \"windSpeed\" : 8.29,\n" +
            "                    \"windBearing\" : 316,\n" +
            "                    \"time\" : 1521810000,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"precipIntensity\" : 0.0001,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"dewPoint\" : 21.71,\n" +
            "                    \"precipProbability\" : 0.05\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 434.2,\n" +
            "                    \"windGust\" : 15.8,\n" +
            "                    \"temperature\" : 32.68,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"dewPoint\" : 19.72,\n" +
            "                    \"humidity\" : 0.58,\n" +
            "                    \"cloudCover\" : 0.29,\n" +
            "                    \"summary\" : \"Partly Cloudy\",\n" +
            "                    \"apparentTemperature\" : 25.93,\n" +
            "                    \"pressure\" : 1004.47,\n" +
            "                    \"windSpeed\" : 7.58,\n" +
            "                    \"time\" : 1521813600,\n" +
            "                    \"windBearing\" : 321,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 432.87,\n" +
            "                    \"windGust\" : 15.26,\n" +
            "                    \"temperature\" : 32.13,\n" +
            "                    \"icon\" : \"clear-day\",\n" +
            "                    \"dewPoint\" : 18.01,\n" +
            "                    \"humidity\" : 0.55,\n" +
            "                    \"cloudCover\" : 0.14,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 25.63,\n" +
            "                    \"pressure\" : 1005.1,\n" +
            "                    \"windSpeed\" : 7.04,\n" +
            "                    \"time\" : 1521817200,\n" +
            "                    \"windBearing\" : 328,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 432.01,\n" +
            "                    \"windGust\" : 15.28,\n" +
            "                    \"temperature\" : 30.75,\n" +
            "                    \"icon\" : \"clear-day\",\n" +
            "                    \"dewPoint\" : 17.01,\n" +
            "                    \"humidity\" : 0.56,\n" +
            "                    \"cloudCover\" : 0.07,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 24.12,\n" +
            "                    \"pressure\" : 1005.57,\n" +
            "                    \"windSpeed\" : 6.82,\n" +
            "                    \"time\" : 1521820800,\n" +
            "                    \"windBearing\" : 338,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 431.26,\n" +
            "                    \"windGust\" : 15.5,\n" +
            "                    \"temperature\" : 28.8,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"dewPoint\" : 16.34,\n" +
            "                    \"humidity\" : 0.59,\n" +
            "                    \"cloudCover\" : 0.03,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 21.93,\n" +
            "                    \"pressure\" : 1005.94,\n" +
            "                    \"windSpeed\" : 6.61,\n" +
            "                    \"time\" : 1521824400,\n" +
            "                    \"windBearing\" : 350,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 430.33,\n" +
            "                    \"windGust\" : 15.26,\n" +
            "                    \"temperature\" : 26.86,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"dewPoint\" : 15.8,\n" +
            "                    \"humidity\" : 0.63,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 19.84,\n" +
            "                    \"pressure\" : 1006.31,\n" +
            "                    \"windSpeed\" : 6.32,\n" +
            "                    \"time\" : 1521828000,\n" +
            "                    \"windBearing\" : 2,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 428.91,\n" +
            "                    \"windGust\" : 14.37,\n" +
            "                    \"temperature\" : 25.29,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"dewPoint\" : 15.27,\n" +
            "                    \"humidity\" : 0.65,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 18.27,\n" +
            "                    \"pressure\" : 1006.74,\n" +
            "                    \"windSpeed\" : 5.97,\n" +
            "                    \"time\" : 1521831600,\n" +
            "                    \"windBearing\" : 11,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 427.33,\n" +
            "                    \"windGust\" : 13.02,\n" +
            "                    \"temperature\" : 23.5,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"dewPoint\" : 14.84,\n" +
            "                    \"humidity\" : 0.69,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 16.46,\n" +
            "                    \"pressure\" : 1007.15,\n" +
            "                    \"windSpeed\" : 5.62,\n" +
            "                    \"time\" : 1521835200,\n" +
            "                    \"windBearing\" : 21,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 425.84,\n" +
            "                    \"windGust\" : 11.31,\n" +
            "                    \"temperature\" : 21.94,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"dewPoint\" : 14.52,\n" +
            "                    \"humidity\" : 0.73,\n" +
            "                    \"cloudCover\" : 0,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 14.97,\n" +
            "                    \"pressure\" : 1007.42,\n" +
            "                    \"windSpeed\" : 5.28,\n" +
            "                    \"time\" : 1521838800,\n" +
            "                    \"windBearing\" : 33,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 424.54,\n" +
            "                    \"windGust\" : 8.87,\n" +
            "                    \"temperature\" : 20.97,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"dewPoint\" : 14.17,\n" +
            "                    \"humidity\" : 0.75,\n" +
            "                    \"cloudCover\" : 0.03,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 14.13,\n" +
            "                    \"pressure\" : 1007.61,\n" +
            "                    \"windSpeed\" : 4.99,\n" +
            "                    \"time\" : 1521842400,\n" +
            "                    \"windBearing\" : 50,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"ozone\" : 423.36,\n" +
            "                    \"windGust\" : 6.06,\n" +
            "                    \"temperature\" : 20.99,\n" +
            "                    \"icon\" : \"clear-night\",\n" +
            "                    \"dewPoint\" : 13.82,\n" +
            "                    \"humidity\" : 0.73,\n" +
            "                    \"cloudCover\" : 0.08,\n" +
            "                    \"summary\" : \"Clear\",\n" +
            "                    \"apparentTemperature\" : 14.41,\n" +
            "                    \"pressure\" : 1007.66,\n" +
            "                    \"windSpeed\" : 4.77,\n" +
            "                    \"time\" : 1521846000,\n" +
            "                    \"windBearing\" : 69,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 0,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }\n" +
            "            ],\n" +
            "            \"summary\" : \"Mostly cloudy throughout the day.\"\n" +
            "        },\n" +
            "        \"currently\" : {\n" +
            "            \"ozone\" : 435.78,\n" +
            "            \"windGust\" : 18.8,\n" +
            "            \"temperature\" : 29.06,\n" +
            "            \"icon\" : \"partly-cloudy-night\",\n" +
            "            \"precipType\" : \"snow\",\n" +
            "            \"humidity\" : 0.94,\n" +
            "            \"cloudCover\" : 0.45,\n" +
            "            \"summary\" : \"Partly Cloudy\",\n" +
            "            \"apparentTemperature\" : 18.08,\n" +
            "            \"pressure\" : 1005.22,\n" +
            "            \"windSpeed\" : 14.32,\n" +
            "            \"visibility\" : 6.22,\n" +
            "            \"time\" : 1521673279,\n" +
            "            \"windBearing\" : 216,\n" +
            "            \"precipIntensity\" : 0.0013,\n" +
            "            \"uvIndex\" : 0,\n" +
            "            \"dewPoint\" : 27.61,\n" +
            "            \"precipProbability\" : 0.07\n" +
            "        },\n" +
            "        \"daily\" : {\n" +
            "            \"icon\" : \"snow\",\n" +
            "            \"data\" : [ \n" +
            "                {\n" +
            "                    \"apparentTemperatureMinTime\" : 1521676800,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"temperatureLow\" : 21.42,\n" +
            "                    \"precipIntensityMaxTime\" : 1521730800,\n" +
            "                    \"temperatureMin\" : 27.19,\n" +
            "                    \"temperatureHigh\" : 36.74,\n" +
            "                    \"summary\" : \"Mostly cloudy throughout the day.\",\n" +
            "                    \"dewPoint\" : 28.53,\n" +
            "                    \"apparentTemperatureMax\" : 28.88,\n" +
            "                    \"temperatureMax\" : 36.74,\n" +
            "                    \"windSpeed\" : 9.42,\n" +
            "                    \"apparentTemperatureHigh\" : 28.88,\n" +
            "                    \"windGustTime\" : 1521709200,\n" +
            "                    \"temperatureMaxTime\" : 1521723600,\n" +
            "                    \"windBearing\" : 251,\n" +
            "                    \"temperatureLowTime\" : 1521781200,\n" +
            "                    \"moonPhase\" : 0.17,\n" +
            "                    \"precipAccumulation\" : 0.23,\n" +
            "                    \"temperatureMinTime\" : 1521680400,\n" +
            "                    \"windGust\" : 31.15,\n" +
            "                    \"apparentTemperatureLow\" : 11.75,\n" +
            "                    \"sunsetTime\" : 1521736792,\n" +
            "                    \"pressure\" : 1000.11,\n" +
            "                    \"uvIndexTime\" : 1521705600,\n" +
            "                    \"cloudCover\" : 0.78,\n" +
            "                    \"apparentTemperatureLowTime\" : 1521781200,\n" +
            "                    \"apparentTemperatureMin\" : 17.21,\n" +
            "                    \"apparentTemperatureHighTime\" : 1521723600,\n" +
            "                    \"precipIntensityMax\" : 0.0096,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"apparentTemperatureMaxTime\" : 1521723600,\n" +
            "                    \"humidity\" : 0.89,\n" +
            "                    \"ozone\" : 430.65,\n" +
            "                    \"temperatureHighTime\" : 1521723600,\n" +
            "                    \"time\" : 1521669600,\n" +
            "                    \"precipIntensity\" : 0.0016,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"sunriseTime\" : 1521692211,\n" +
            "                    \"precipProbability\" : 0.33\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"apparentTemperatureMinTime\" : 1521781200,\n" +
            "                    \"apparentTemperatureLow\" : 14.13,\n" +
            "                    \"temperatureLow\" : 20.97,\n" +
            "                    \"precipIntensityMaxTime\" : 1521770400,\n" +
            "                    \"temperatureMin\" : 21.42,\n" +
            "                    \"temperatureHigh\" : 32.68,\n" +
            "                    \"summary\" : \"Partly cloudy in the afternoon.\",\n" +
            "                    \"dewPoint\" : 19.78,\n" +
            "                    \"apparentTemperatureMax\" : 25.93,\n" +
            "                    \"temperatureMax\" : 32.68,\n" +
            "                    \"windSpeed\" : 6.96,\n" +
            "                    \"apparentTemperatureHigh\" : 25.93,\n" +
            "                    \"windGustTime\" : 1521784800,\n" +
            "                    \"temperatureMaxTime\" : 1521813600,\n" +
            "                    \"windBearing\" : 330,\n" +
            "                    \"temperatureLowTime\" : 1521842400,\n" +
            "                    \"moonPhase\" : 0.21,\n" +
            "                    \"temperatureMinTime\" : 1521781200,\n" +
            "                    \"windGust\" : 18.89,\n" +
            "                    \"sunsetTime\" : 1521823338,\n" +
            "                    \"pressure\" : 1003.33,\n" +
            "                    \"uvIndexTime\" : 1521788400,\n" +
            "                    \"cloudCover\" : 0.19,\n" +
            "                    \"apparentTemperatureLowTime\" : 1521842400,\n" +
            "                    \"apparentTemperatureMin\" : 11.75,\n" +
            "                    \"apparentTemperatureHighTime\" : 1521813600,\n" +
            "                    \"precipIntensityMax\" : 0.0001,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"apparentTemperatureMaxTime\" : 1521813600,\n" +
            "                    \"humidity\" : 0.77,\n" +
            "                    \"ozone\" : 436.14,\n" +
            "                    \"temperatureHighTime\" : 1521813600,\n" +
            "                    \"time\" : 1521756000,\n" +
            "                    \"precipIntensity\" : 0,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"sunriseTime\" : 1521778426,\n" +
            "                    \"precipProbability\" : 0\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"apparentTemperatureMinTime\" : 1521842400,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"temperatureLow\" : 26.14,\n" +
            "                    \"precipIntensityMaxTime\" : 1521914400,\n" +
            "                    \"temperatureMin\" : 20.97,\n" +
            "                    \"temperatureHigh\" : 34.56,\n" +
            "                    \"summary\" : \"Mostly cloudy throughout the day.\",\n" +
            "                    \"dewPoint\" : 21.75,\n" +
            "                    \"apparentTemperatureMax\" : 28.31,\n" +
            "                    \"temperatureMax\" : 34.56,\n" +
            "                    \"windSpeed\" : 2.83,\n" +
            "                    \"apparentTemperatureHigh\" : 26.7,\n" +
            "                    \"windGustTime\" : 1521910800,\n" +
            "                    \"temperatureMaxTime\" : 1521907200,\n" +
            "                    \"windBearing\" : 126,\n" +
            "                    \"temperatureLowTime\" : 1521939600,\n" +
            "                    \"moonPhase\" : 0.24,\n" +
            "                    \"precipAccumulation\" : 0.487,\n" +
            "                    \"temperatureMinTime\" : 1521842400,\n" +
            "                    \"windGust\" : 30.68,\n" +
            "                    \"apparentTemperatureLow\" : 17.7,\n" +
            "                    \"sunsetTime\" : 1521909885,\n" +
            "                    \"pressure\" : 1003.69,\n" +
            "                    \"uvIndexTime\" : 1521878400,\n" +
            "                    \"cloudCover\" : 0.82,\n" +
            "                    \"apparentTemperatureLowTime\" : 1521939600,\n" +
            "                    \"apparentTemperatureMin\" : 14.13,\n" +
            "                    \"apparentTemperatureHighTime\" : 1521910800,\n" +
            "                    \"precipIntensityMax\" : 0.008,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"apparentTemperatureMaxTime\" : 1521918000,\n" +
            "                    \"humidity\" : 0.77,\n" +
            "                    \"ozone\" : 412.39,\n" +
            "                    \"temperatureHighTime\" : 1521907200,\n" +
            "                    \"time\" : 1521842400,\n" +
            "                    \"precipIntensity\" : 0.0025,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"sunriseTime\" : 1521864642,\n" +
            "                    \"precipProbability\" : 0.3\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"apparentTemperatureMinTime\" : 1521939600,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"temperatureLow\" : 28.1,\n" +
            "                    \"precipIntensityMaxTime\" : 1522008000,\n" +
            "                    \"temperatureMin\" : 26.14,\n" +
            "                    \"temperatureHigh\" : 36.68,\n" +
            "                    \"summary\" : \"Snow (1–2 in.) starting in the evening.\",\n" +
            "                    \"dewPoint\" : 29.16,\n" +
            "                    \"apparentTemperatureMax\" : 27.96,\n" +
            "                    \"temperatureMax\" : 36.68,\n" +
            "                    \"windSpeed\" : 9.18,\n" +
            "                    \"apparentTemperatureHigh\" : 27.96,\n" +
            "                    \"windGustTime\" : 1521986400,\n" +
            "                    \"temperatureMaxTime\" : 1521982800,\n" +
            "                    \"windBearing\" : 235,\n" +
            "                    \"temperatureLowTime\" : 1522033200,\n" +
            "                    \"moonPhase\" : 0.28,\n" +
            "                    \"precipAccumulation\" : 0.607,\n" +
            "                    \"temperatureMinTime\" : 1521939600,\n" +
            "                    \"windGust\" : 35.79,\n" +
            "                    \"apparentTemperatureLow\" : 17.37,\n" +
            "                    \"sunsetTime\" : 1521996431,\n" +
            "                    \"pressure\" : 1005.76,\n" +
            "                    \"uvIndexTime\" : 1521964800,\n" +
            "                    \"cloudCover\" : 0.87,\n" +
            "                    \"apparentTemperatureLowTime\" : 1522033200,\n" +
            "                    \"apparentTemperatureMin\" : 17.7,\n" +
            "                    \"apparentTemperatureHighTime\" : 1521979200,\n" +
            "                    \"precipIntensityMax\" : 0.0204,\n" +
            "                    \"icon\" : \"snow\",\n" +
            "                    \"apparentTemperatureMaxTime\" : 1521979200,\n" +
            "                    \"humidity\" : 0.91,\n" +
            "                    \"ozone\" : 397.47,\n" +
            "                    \"temperatureHighTime\" : 1521982800,\n" +
            "                    \"time\" : 1521928800,\n" +
            "                    \"precipIntensity\" : 0.0039,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"sunriseTime\" : 1521950858,\n" +
            "                    \"precipProbability\" : 0.37\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"apparentTemperatureMinTime\" : 1522033200,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"temperatureLow\" : 29.02,\n" +
            "                    \"precipIntensityMaxTime\" : 1522033200,\n" +
            "                    \"temperatureMin\" : 28.1,\n" +
            "                    \"temperatureHigh\" : 34.73,\n" +
            "                    \"summary\" : \"Snow (1–2 in.) in the morning.\",\n" +
            "                    \"dewPoint\" : 29.34,\n" +
            "                    \"apparentTemperatureMax\" : 34.73,\n" +
            "                    \"temperatureMax\" : 34.73,\n" +
            "                    \"windSpeed\" : 1.49,\n" +
            "                    \"apparentTemperatureHigh\" : 34.73,\n" +
            "                    \"windGustTime\" : 1522033200,\n" +
            "                    \"temperatureMaxTime\" : 1522065600,\n" +
            "                    \"windBearing\" : 227,\n" +
            "                    \"temperatureLowTime\" : 1522101600,\n" +
            "                    \"moonPhase\" : 0.32,\n" +
            "                    \"precipAccumulation\" : 2.122,\n" +
            "                    \"temperatureMinTime\" : 1522033200,\n" +
            "                    \"windGust\" : 27.77,\n" +
            "                    \"apparentTemperatureLow\" : 20.5,\n" +
            "                    \"sunsetTime\" : 1522082971,\n" +
            "                    \"pressure\" : 1001.85,\n" +
            "                    \"uvIndexTime\" : 1522051200,\n" +
            "                    \"cloudCover\" : 1,\n" +
            "                    \"apparentTemperatureLowTime\" : 1522098000,\n" +
            "                    \"apparentTemperatureMin\" : 17.37,\n" +
            "                    \"apparentTemperatureHighTime\" : 1522065600,\n" +
            "                    \"precipIntensityMax\" : 0.0313,\n" +
            "                    \"icon\" : \"snow\",\n" +
            "                    \"apparentTemperatureMaxTime\" : 1522065600,\n" +
            "                    \"humidity\" : 0.9,\n" +
            "                    \"ozone\" : 419.33,\n" +
            "                    \"temperatureHighTime\" : 1522065600,\n" +
            "                    \"time\" : 1522011600,\n" +
            "                    \"precipIntensity\" : 0.0103,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"sunriseTime\" : 1522037080,\n" +
            "                    \"precipProbability\" : 0.63\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"apparentTemperatureMinTime\" : 1522098000,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"temperatureLow\" : 19.43,\n" +
            "                    \"precipIntensityMaxTime\" : 1522098000,\n" +
            "                    \"temperatureMin\" : 29.02,\n" +
            "                    \"temperatureHigh\" : 33.67,\n" +
            "                    \"summary\" : \"Overcast throughout the day.\",\n" +
            "                    \"dewPoint\" : 26.42,\n" +
            "                    \"apparentTemperatureMax\" : 30.26,\n" +
            "                    \"temperatureMax\" : 33.67,\n" +
            "                    \"windSpeed\" : 3.72,\n" +
            "                    \"apparentTemperatureHigh\" : 30.26,\n" +
            "                    \"windGustTime\" : 1522098000,\n" +
            "                    \"temperatureMaxTime\" : 1522144800,\n" +
            "                    \"windBearing\" : 158,\n" +
            "                    \"temperatureLowTime\" : 1522213200,\n" +
            "                    \"moonPhase\" : 0.36,\n" +
            "                    \"precipAccumulation\" : 0.215,\n" +
            "                    \"temperatureMinTime\" : 1522101600,\n" +
            "                    \"windGust\" : 19.94,\n" +
            "                    \"apparentTemperatureLow\" : 7.25,\n" +
            "                    \"sunsetTime\" : 1522169517,\n" +
            "                    \"pressure\" : 1005.97,\n" +
            "                    \"uvIndexTime\" : 1522134000,\n" +
            "                    \"cloudCover\" : 0.96,\n" +
            "                    \"apparentTemperatureLowTime\" : 1522213200,\n" +
            "                    \"apparentTemperatureMin\" : 20.5,\n" +
            "                    \"apparentTemperatureHighTime\" : 1522141200,\n" +
            "                    \"precipIntensityMax\" : 0.0049,\n" +
            "                    \"icon\" : \"cloudy\",\n" +
            "                    \"apparentTemperatureMaxTime\" : 1522141200,\n" +
            "                    \"humidity\" : 0.83,\n" +
            "                    \"ozone\" : 420.25,\n" +
            "                    \"temperatureHighTime\" : 1522144800,\n" +
            "                    \"time\" : 1522098000,\n" +
            "                    \"precipIntensity\" : 0.001,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"sunriseTime\" : 1522123296,\n" +
            "                    \"precipProbability\" : 0.3\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"apparentTemperatureMinTime\" : 1522216800,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"temperatureLow\" : 19.76,\n" +
            "                    \"precipIntensityMaxTime\" : 1522195200,\n" +
            "                    \"temperatureMin\" : 18.93,\n" +
            "                    \"temperatureHigh\" : 28.6,\n" +
            "                    \"summary\" : \"Mostly cloudy throughout the day.\",\n" +
            "                    \"dewPoint\" : 16.54,\n" +
            "                    \"apparentTemperatureMax\" : 21.95,\n" +
            "                    \"temperatureMax\" : 28.73,\n" +
            "                    \"windSpeed\" : 9.28,\n" +
            "                    \"apparentTemperatureHigh\" : 20.93,\n" +
            "                    \"windGustTime\" : 1522263600,\n" +
            "                    \"temperatureMaxTime\" : 1522184400,\n" +
            "                    \"windBearing\" : 66,\n" +
            "                    \"temperatureLowTime\" : 1522274400,\n" +
            "                    \"moonPhase\" : 0.39,\n" +
            "                    \"precipAccumulation\" : 0.609,\n" +
            "                    \"temperatureMinTime\" : 1522216800,\n" +
            "                    \"windGust\" : 27.23,\n" +
            "                    \"apparentTemperatureLow\" : 7.34,\n" +
            "                    \"sunsetTime\" : 1522256064,\n" +
            "                    \"pressure\" : 1004.48,\n" +
            "                    \"uvIndexTime\" : 1522220400,\n" +
            "                    \"cloudCover\" : 0.67,\n" +
            "                    \"apparentTemperatureLowTime\" : 1522278000,\n" +
            "                    \"apparentTemperatureMin\" : 6.63,\n" +
            "                    \"apparentTemperatureHighTime\" : 1522245600,\n" +
            "                    \"precipIntensityMax\" : 0.0078,\n" +
            "                    \"icon\" : \"partly-cloudy-day\",\n" +
            "                    \"apparentTemperatureMaxTime\" : 1522184400,\n" +
            "                    \"humidity\" : 0.72,\n" +
            "                    \"ozone\" : 495.93,\n" +
            "                    \"temperatureHighTime\" : 1522238400,\n" +
            "                    \"time\" : 1522184400,\n" +
            "                    \"precipIntensity\" : 0.0019,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"sunriseTime\" : 1522209512,\n" +
            "                    \"precipProbability\" : 0.3\n" +
            "                }, \n" +
            "                {\n" +
            "                    \"apparentTemperatureMinTime\" : 1522278000,\n" +
            "                    \"precipType\" : \"snow\",\n" +
            "                    \"temperatureLow\" : 20.66,\n" +
            "                    \"precipIntensityMaxTime\" : 1522292400,\n" +
            "                    \"temperatureMin\" : 19.76,\n" +
            "                    \"temperatureHigh\" : 28.04,\n" +
            "                    \"summary\" : \"Snow (< 1 in.) overnight.\",\n" +
            "                    \"dewPoint\" : 18.92,\n" +
            "                    \"apparentTemperatureMax\" : 20.89,\n" +
            "                    \"temperatureMax\" : 32.22,\n" +
            "                    \"windSpeed\" : 12.89,\n" +
            "                    \"apparentTemperatureHigh\" : 16.72,\n" +
            "                    \"windGustTime\" : 1522353600,\n" +
            "                    \"temperatureMaxTime\" : 1522350000,\n" +
            "                    \"windBearing\" : 67,\n" +
            "                    \"temperatureLowTime\" : 1522386000,\n" +
            "                    \"moonPhase\" : 0.43,\n" +
            "                    \"precipAccumulation\" : 0.382,\n" +
            "                    \"temperatureMinTime\" : 1522274400,\n" +
            "                    \"windGust\" : 35.93,\n" +
            "                    \"apparentTemperatureLow\" : 8.12,\n" +
            "                    \"sunsetTime\" : 1522342610,\n" +
            "                    \"pressure\" : 1006.97,\n" +
            "                    \"uvIndexTime\" : 1522310400,\n" +
            "                    \"cloudCover\" : 0.98,\n" +
            "                    \"apparentTemperatureLowTime\" : 1522386000,\n" +
            "                    \"apparentTemperatureMin\" : 7.34,\n" +
            "                    \"apparentTemperatureHighTime\" : 1522317600,\n" +
            "                    \"precipIntensityMax\" : 0.0043,\n" +
            "                    \"icon\" : \"snow\",\n" +
            "                    \"apparentTemperatureMaxTime\" : 1522350000,\n" +
            "                    \"humidity\" : 0.75,\n" +
            "                    \"ozone\" : 436.62,\n" +
            "                    \"temperatureHighTime\" : 1522324800,\n" +
            "                    \"time\" : 1522270800,\n" +
            "                    \"precipIntensity\" : 0.0014,\n" +
            "                    \"uvIndex\" : 1,\n" +
            "                    \"sunriseTime\" : 1522295728,\n" +
            "                    \"precipProbability\" : 0.24\n" +
            "                }\n" +
            "            ],\n" +
            "            \"summary\" : \"Light snow (2–5 in.) today through Monday, with temperatures falling to 29°F on Wednesday.\"\n" +
            "        },\n" +
            "        \"flags\" : {\n" +
            "            \"units\" : \"us\",\n" +
            "            \"sources\" : [ \n" +
            "                \"isd\", \n" +
            "                \"cmc\", \n" +
            "                \"gfs\", \n" +
            "                \"madis\"\n" +
            "            ],\n" +
            "            \"isd-stations\" : [ \n" +
            "                \"027030-99999\", \n" +
            "                \"027060-99999\", \n" +
            "                \"027580-99999\", \n" +
            "                \"027590-99999\", \n" +
            "                \"027940-99999\", \n" +
            "                \"027950-99999\", \n" +
            "                \"028290-99999\", \n" +
            "                \"029740-99999\", \n" +
            "                \"029750-99999\", \n" +
            "                \"029780-99999\", \n" +
            "                \"029840-99999\", \n" +
            "                \"029860-99999\", \n" +
            "                \"029870-99999\", \n" +
            "                \"029880-99999\", \n" +
            "                \"029910-99999\", \n" +
            "                \"260340-99999\"\n" +
            "            ]\n" +
            "        },\n" +
            "        \"longitude\" : 24.9383791,\n" +
            "        \"offset\" : 2,\n" +
            "        \"latitude\" : 60.1698557,\n" +
            "        \"timezone\" : \"Europe/Helsinki\"\n" +
            "    }";
}
