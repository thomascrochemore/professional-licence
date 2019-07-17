package org.acrobatt.project.apiparsers;

import org.acrobatt.project.apiparsers.parsers.WundergroundParser;
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

public class WundergroundParserTest {
    private WundergroundParser apixuParser = WundergroundParser.getInstance();

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
            ApiData apiData = new ApiData(apiJson,"Strasbourg",ApiConst.WUNDERGROUND,0, Date.from(Instant.now()));
            apiData.setCountry("france");
            WeatherData weatherData = apixuParser.parseJsonResultToWeatherData(apiData);
            System.out.println("Realtime : ");
            System.out.println("delay : " + weatherData.getData_delay().getValue());
            System.out.println("city : "+weatherData.getData_location().getCity());
            System.out.println("api : "+weatherData.getData_api().getName());
            System.out.println("forecast : " + weatherData.getForecast());
            for(DataValue dataValue : weatherData.getData_values()){
                System.out.println(dataValue.getProperty().getName()+" - decimal : "+dataValue.getValue_decimal());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void forecastTest(){
        try {
            JSONObject apiJson = new JSONObject(FORECAST_JSON_2018_18_03);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,2018);
            calendar.set(Calendar.MONTH,2);
            calendar.set(Calendar.DAY_OF_MONTH,18);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            ApiData apiData = new ApiData(apiJson,"Strasbourg",ApiConst.WUNDERGROUND,72,calendar.getTime());
            apiData.setCountry("france");
            WeatherData weatherData = apixuParser.parseJsonForecastToWeathData(apiData);

            System.out.println("\nForecast :");
            System.out.println("delay : " + weatherData.getData_delay().getValue());
            System.out.println("city : "+weatherData.getData_location().getCity());
            System.out.println("api : "+weatherData.getData_api().getName());
            System.out.println("forecast : " + weatherData.getForecast());
            for(DataValue dataValue : weatherData.getData_values()){
                System.out.println(dataValue.getProperty().getName()+" - decimal : "+dataValue.getValue_decimal());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static final String REALTIME_JSON = "{\n" +
            "    \"response\": {\n" +
            "        \"version\": \"0.1\",\n" +
            "        \"termsofService\": \"http://www.wunderground.com/weather/api/d/terms.html\",\n" +
            "        \"features\": {\n" +
            "            \"conditions\": 1\n" +
            "        }\n" +
            "    },\n" +
            "    \"current_observation\": {\n" +
            "        \"image\": {\n" +
            "            \"url\": \"http://icons.wxug.com/graphics/wu2/logo_130x80.png\",\n" +
            "            \"title\": \"Weather Underground\",\n" +
            "            \"link\": \"http://www.wunderground.com\"\n" +
            "        },\n" +
            "        \"display_location\": {\n" +
            "            \"full\": \"Strasbourg, France\",\n" +
            "            \"city\": \"Strasbourg\",\n" +
            "            \"state\": \"67\",\n" +
            "            \"state_name\": \"France\",\n" +
            "            \"country\": \"FR\",\n" +
            "            \"country_iso3166\": \"FR\",\n" +
            "            \"zip\": \"00000\",\n" +
            "            \"magic\": \"340\",\n" +
            "            \"wmo\": \"07190\",\n" +
            "            \"latitude\": \"48.58000183\",\n" +
            "            \"longitude\": \"7.73999977\",\n" +
            "            \"elevation\": \"141.1\"\n" +
            "        },\n" +
            "        \"observation_location\": {\n" +
            "            \"full\": \"Strasbourg, ALSACE\",\n" +
            "            \"city\": \"Strasbourg\",\n" +
            "            \"state\": \"ALSACE\",\n" +
            "            \"country\": \"FR\",\n" +
            "            \"country_iso3166\": \"FR\",\n" +
            "            \"latitude\": \"48.581036\",\n" +
            "            \"longitude\": \"7.743816\",\n" +
            "            \"elevation\": \"456 ft\"\n" +
            "        },\n" +
            "        \"estimated\": {},\n" +
            "        \"station_id\": \"IALSACES3\",\n" +
            "        \"observation_time\": \"Last Updated on March 18, 2:30 PM CET\",\n" +
            "        \"observation_time_rfc822\": \"Sun, 18 Mar 2018 14:30:33 +0100\",\n" +
            "        \"observation_epoch\": \"1521379833\",\n" +
            "        \"local_time_rfc822\": \"Sun, 18 Mar 2018 14:59:36 +0100\",\n" +
            "        \"local_epoch\": \"1521381576\",\n" +
            "        \"local_tz_short\": \"CET\",\n" +
            "        \"local_tz_long\": \"Europe/Paris\",\n" +
            "        \"local_tz_offset\": \"+0100\",\n" +
            "        \"weather\": \"Overcast\",\n" +
            "        \"temperature_string\": \"33.1 F (0.6 C)\",\n" +
            "        \"temp_f\": 33.1,\n" +
            "        \"temp_c\": 0.6,\n" +
            "        \"relative_humidity\": \"77%\",\n" +
            "        \"wind_string\": \"From the NNE at 7.0 MPH Gusting to 13.0 MPH\",\n" +
            "        \"wind_dir\": \"NNE\",\n" +
            "        \"wind_degrees\": 26,\n" +
            "        \"wind_mph\": 7,\n" +
            "        \"wind_gust_mph\": \"13.0\",\n" +
            "        \"wind_kph\": 11.3,\n" +
            "        \"wind_gust_kph\": \"20.9\",\n" +
            "        \"pressure_mb\": \"1002\",\n" +
            "        \"pressure_in\": \"29.59\",\n" +
            "        \"pressure_trend\": \"0\",\n" +
            "        \"dewpoint_string\": \"27 F (-3 C)\",\n" +
            "        \"dewpoint_f\": 27,\n" +
            "        \"dewpoint_c\": -3,\n" +
            "        \"heat_index_string\": \"NA\",\n" +
            "        \"heat_index_f\": \"NA\",\n" +
            "        \"heat_index_c\": \"NA\",\n" +
            "        \"windchill_string\": \"27 F (-3 C)\",\n" +
            "        \"windchill_f\": \"27\",\n" +
            "        \"windchill_c\": \"-3\",\n" +
            "        \"feelslike_string\": \"27 F (-3 C)\",\n" +
            "        \"feelslike_f\": \"27\",\n" +
            "        \"feelslike_c\": \"-3\",\n" +
            "        \"visibility_mi\": \"6.2\",\n" +
            "        \"visibility_km\": \"10.0\",\n" +
            "        \"solarradiation\": \"--\",\n" +
            "        \"UV\": \"1\",\n" +
            "        \"precip_1hr_string\": \"0.00 in ( 0 mm)\",\n" +
            "        \"precip_1hr_in\": \"0.00\",\n" +
            "        \"precip_1hr_metric\": \" 0\",\n" +
            "        \"precip_today_string\": \"0.02 in (1 mm)\",\n" +
            "        \"precip_today_in\": \"0.02\",\n" +
            "        \"precip_today_metric\": \"1\",\n" +
            "        \"icon\": \"cloudy\",\n" +
            "        \"icon_url\": \"http://icons.wxug.com/i/c/k/cloudy.gif\",\n" +
            "        \"forecast_url\": \"http://www.wunderground.com/global/stations/07190.html\",\n" +
            "        \"history_url\": \"http://www.wunderground.com/weatherstation/WXDailyHistory.asp?ID=IALSACES3\",\n" +
            "        \"ob_url\": \"http://www.wunderground.com/cgi-bin/findweather/getForecast?query=48.581036,7.743816\",\n" +
            "        \"nowcast\": \"\"\n" +
            "    }\n" +
            "}";

    private static final String FORECAST_JSON_2018_18_03 = "{\n" +
            "    \"response\": {\n" +
            "        \"version\": \"0.1\",\n" +
            "        \"termsofService\": \"http://www.wunderground.com/weather/api/d/terms.html\",\n" +
            "        \"features\": {\n" +
            "            \"geolookup\": 1,\n" +
            "            \"conditions\": 1,\n" +
            "            \"forecast\": 1\n" +
            "        }\n" +
            "    },\n" +
            "    \"location\": {\n" +
            "        \"type\": \"INTLCITY\",\n" +
            "        \"country\": \"FR\",\n" +
            "        \"country_iso3166\": \"FR\",\n" +
            "        \"country_name\": \"France\",\n" +
            "        \"state\": \"67\",\n" +
            "        \"city\": \"Strasbourg\",\n" +
            "        \"tz_short\": \"CET\",\n" +
            "        \"tz_long\": \"Europe/Paris\",\n" +
            "        \"lat\": \"48.58000183\",\n" +
            "        \"lon\": \"7.73999977\",\n" +
            "        \"zip\": \"00000\",\n" +
            "        \"magic\": \"340\",\n" +
            "        \"wmo\": \"07190\",\n" +
            "        \"l\": \"/q/zmw:00000.340.07190\",\n" +
            "        \"requesturl\": \"global/stations/07190.html\",\n" +
            "        \"wuiurl\": \"https://www.wunderground.com/global/stations/07190.html\",\n" +
            "        \"nearby_weather_stations\": {\n" +
            "            \"airport\": {\n" +
            "                \"station\": [\n" +
            "                    {\n" +
            "                        \"city\": \"Strasbourg\",\n" +
            "                        \"state\": \"\",\n" +
            "                        \"country\": \"France\",\n" +
            "                        \"icao\": \"LFST\",\n" +
            "                        \"lat\": \"48.54999924\",\n" +
            "                        \"lon\": \"7.63333321\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"city\": \"Strasbourg\",\n" +
            "                        \"state\": \"\",\n" +
            "                        \"country\": \"FR\",\n" +
            "                        \"icao\": \"LFST\",\n" +
            "                        \"lat\": \"48.54999924\",\n" +
            "                        \"lon\": \"7.63000011\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"city\": \"Lahr\",\n" +
            "                        \"state\": \"\",\n" +
            "                        \"country\": \"DL\",\n" +
            "                        \"icao\": \"EDTL\",\n" +
            "                        \"lat\": \"48.36999893\",\n" +
            "                        \"lon\": \"7.82999992\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"city\": \"Karlsruhe\",\n" +
            "                        \"state\": \"\",\n" +
            "                        \"country\": \"DE\",\n" +
            "                        \"icao\": \"EDSB\",\n" +
            "                        \"lat\": \"48.77999878\",\n" +
            "                        \"lon\": \"8.07999992\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            \"pws\": {\n" +
            "                \"station\": [\n" +
            "                    {\n" +
            "                        \"neighborhood\": \"\",\n" +
            "                        \"city\": \"Strasbourg\",\n" +
            "                        \"state\": \"ALSACE\",\n" +
            "                        \"country\": \"FR\",\n" +
            "                        \"id\": \"IALSACES3\",\n" +
            "                        \"lat\": 48.581036,\n" +
            "                        \"lon\": 7.743816,\n" +
            "                        \"distance_km\": 0,\n" +
            "                        \"distance_mi\": 0\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        }\n" +
            "    },\n" +
            "    \"current_observation\": {\n" +
            "        \"image\": {\n" +
            "            \"url\": \"http://icons.wxug.com/graphics/wu2/logo_130x80.png\",\n" +
            "            \"title\": \"Weather Underground\",\n" +
            "            \"link\": \"http://www.wunderground.com\"\n" +
            "        },\n" +
            "        \"display_location\": {\n" +
            "            \"full\": \"Strasbourg, France\",\n" +
            "            \"city\": \"Strasbourg\",\n" +
            "            \"state\": \"67\",\n" +
            "            \"state_name\": \"France\",\n" +
            "            \"country\": \"FR\",\n" +
            "            \"country_iso3166\": \"FR\",\n" +
            "            \"zip\": \"00000\",\n" +
            "            \"magic\": \"340\",\n" +
            "            \"wmo\": \"07190\",\n" +
            "            \"latitude\": \"48.58000183\",\n" +
            "            \"longitude\": \"7.73999977\",\n" +
            "            \"elevation\": \"141.1\"\n" +
            "        },\n" +
            "        \"observation_location\": {\n" +
            "            \"full\": \"Strasbourg, ALSACE\",\n" +
            "            \"city\": \"Strasbourg\",\n" +
            "            \"state\": \"ALSACE\",\n" +
            "            \"country\": \"FR\",\n" +
            "            \"country_iso3166\": \"FR\",\n" +
            "            \"latitude\": \"48.581036\",\n" +
            "            \"longitude\": \"7.743816\",\n" +
            "            \"elevation\": \"456 ft\"\n" +
            "        },\n" +
            "        \"estimated\": {},\n" +
            "        \"station_id\": \"IALSACES3\",\n" +
            "        \"observation_time\": \"Last Updated on March 18, 3:00 PM CET\",\n" +
            "        \"observation_time_rfc822\": \"Sun, 18 Mar 2018 15:00:36 +0100\",\n" +
            "        \"observation_epoch\": \"1521381636\",\n" +
            "        \"local_time_rfc822\": \"Sun, 18 Mar 2018 15:00:51 +0100\",\n" +
            "        \"local_epoch\": \"1521381651\",\n" +
            "        \"local_tz_short\": \"CET\",\n" +
            "        \"local_tz_long\": \"Europe/Paris\",\n" +
            "        \"local_tz_offset\": \"+0100\",\n" +
            "        \"weather\": \"Overcast\",\n" +
            "        \"temperature_string\": \"33.1 F (0.6 C)\",\n" +
            "        \"temp_f\": 33.1,\n" +
            "        \"temp_c\": 0.6,\n" +
            "        \"relative_humidity\": \"77%\",\n" +
            "        \"wind_string\": \"From the NE at 5.0 MPH Gusting to 11.0 MPH\",\n" +
            "        \"wind_dir\": \"NE\",\n" +
            "        \"wind_degrees\": 44,\n" +
            "        \"wind_mph\": 5,\n" +
            "        \"wind_gust_mph\": \"11.0\",\n" +
            "        \"wind_kph\": 8,\n" +
            "        \"wind_gust_kph\": \"17.7\",\n" +
            "        \"pressure_mb\": \"1003\",\n" +
            "        \"pressure_in\": \"29.62\",\n" +
            "        \"pressure_trend\": \"0\",\n" +
            "        \"dewpoint_string\": \"27 F (-3 C)\",\n" +
            "        \"dewpoint_f\": 27,\n" +
            "        \"dewpoint_c\": -3,\n" +
            "        \"heat_index_string\": \"NA\",\n" +
            "        \"heat_index_f\": \"NA\",\n" +
            "        \"heat_index_c\": \"NA\",\n" +
            "        \"windchill_string\": \"28 F (-2 C)\",\n" +
            "        \"windchill_f\": \"28\",\n" +
            "        \"windchill_c\": \"-2\",\n" +
            "        \"feelslike_string\": \"28 F (-2 C)\",\n" +
            "        \"feelslike_f\": \"28\",\n" +
            "        \"feelslike_c\": \"-2\",\n" +
            "        \"visibility_mi\": \"6.2\",\n" +
            "        \"visibility_km\": \"10.0\",\n" +
            "        \"solarradiation\": \"--\",\n" +
            "        \"UV\": \"1\",\n" +
            "        \"precip_1hr_string\": \"0.00 in ( 0 mm)\",\n" +
            "        \"precip_1hr_in\": \"0.00\",\n" +
            "        \"precip_1hr_metric\": \" 0\",\n" +
            "        \"precip_today_string\": \"0.02 in (1 mm)\",\n" +
            "        \"precip_today_in\": \"0.02\",\n" +
            "        \"precip_today_metric\": \"1\",\n" +
            "        \"icon\": \"cloudy\",\n" +
            "        \"icon_url\": \"http://icons.wxug.com/i/c/k/cloudy.gif\",\n" +
            "        \"forecast_url\": \"http://www.wunderground.com/global/stations/07190.html\",\n" +
            "        \"history_url\": \"http://www.wunderground.com/weatherstation/WXDailyHistory.asp?ID=IALSACES3\",\n" +
            "        \"ob_url\": \"http://www.wunderground.com/cgi-bin/findweather/getForecast?query=48.581036,7.743816\",\n" +
            "        \"nowcast\": \"\"\n" +
            "    },\n" +
            "    \"forecast\": {\n" +
            "        \"txt_forecast\": {\n" +
            "            \"date\": \"1:33 PM CET\",\n" +
            "            \"forecastday\": [\n" +
            "                {\n" +
            "                    \"period\": 0,\n" +
            "                    \"icon\": \"cloudy\",\n" +
            "                    \"icon_url\": \"http://icons.wxug.com/i/c/k/cloudy.gif\",\n" +
            "                    \"title\": \"Sunday\",\n" +
            "                    \"fcttext\": \"Cloudy skies. Continued very cold. Temps nearly steady in the mid 30s. Winds NE at 10 to 20 mph.\",\n" +
            "                    \"fcttext_metric\": \"Cloudy. Continued very cold. High 3C. Winds NE at 15 to 30 km/h.\",\n" +
            "                    \"pop\": \"0\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"period\": 1,\n" +
            "                    \"icon\": \"nt_cloudy\",\n" +
            "                    \"icon_url\": \"http://icons.wxug.com/i/c/k/nt_cloudy.gif\",\n" +
            "                    \"title\": \"Sunday Night\",\n" +
            "                    \"fcttext\": \"Cloudy. Low 27F. Winds NNE at 10 to 15 mph.\",\n" +
            "                    \"fcttext_metric\": \"Cloudy skies. Low -3C. Winds NNE at 15 to 25 km/h.\",\n" +
            "                    \"pop\": \"0\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"period\": 2,\n" +
            "                    \"icon\": \"partlycloudy\",\n" +
            "                    \"icon_url\": \"http://icons.wxug.com/i/c/k/partlycloudy.gif\",\n" +
            "                    \"title\": \"Monday\",\n" +
            "                    \"fcttext\": \"Mostly cloudy skies early, then partly cloudy in the afternoon. High 39F. Winds NNE at 10 to 20 mph.\",\n" +
            "                    \"fcttext_metric\": \"Mostly cloudy skies early, then partly cloudy in the afternoon. High 4C. Winds NNE at 15 to 30 km/h.\",\n" +
            "                    \"pop\": \"0\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"period\": 3,\n" +
            "                    \"icon\": \"nt_partlycloudy\",\n" +
            "                    \"icon_url\": \"http://icons.wxug.com/i/c/k/nt_partlycloudy.gif\",\n" +
            "                    \"title\": \"Monday Night\",\n" +
            "                    \"fcttext\": \"Partly cloudy skies. Hard freeze expected. Low 23F. Winds NNE at 10 to 15 mph.\",\n" +
            "                    \"fcttext_metric\": \"Partly cloudy skies. Hard freeze expected. Low near -5C. Winds NNE at 15 to 25 km/h.\",\n" +
            "                    \"pop\": \"0\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"period\": 4,\n" +
            "                    \"icon\": \"partlycloudy\",\n" +
            "                    \"icon_url\": \"http://icons.wxug.com/i/c/k/partlycloudy.gif\",\n" +
            "                    \"title\": \"Tuesday\",\n" +
            "                    \"fcttext\": \"Intervals of clouds and sunshine. High 42F. Winds NNE at 5 to 10 mph.\",\n" +
            "                    \"fcttext_metric\": \"Sunshine and clouds mixed. High near 5C. Winds NNE at 10 to 15 km/h.\",\n" +
            "                    \"pop\": \"0\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"period\": 5,\n" +
            "                    \"icon\": \"nt_clear\",\n" +
            "                    \"icon_url\": \"http://icons.wxug.com/i/c/k/nt_clear.gif\",\n" +
            "                    \"title\": \"Tuesday Night\",\n" +
            "                    \"fcttext\": \"Clear skies. Hard freeze expected. Low near 25F. Winds N at 5 to 10 mph.\",\n" +
            "                    \"fcttext_metric\": \"Clear. Hard freeze expected. Low -4C. Winds N at 10 to 15 km/h.\",\n" +
            "                    \"pop\": \"10\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"period\": 6,\n" +
            "                    \"icon\": \"clear\",\n" +
            "                    \"icon_url\": \"http://icons.wxug.com/i/c/k/clear.gif\",\n" +
            "                    \"title\": \"Wednesday\",\n" +
            "                    \"fcttext\": \"Mainly sunny. High 43F. Winds NNE at 5 to 10 mph.\",\n" +
            "                    \"fcttext_metric\": \"Mainly sunny. High 6C. Winds NNE at 10 to 15 km/h.\",\n" +
            "                    \"pop\": \"10\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"period\": 7,\n" +
            "                    \"icon\": \"nt_partlycloudy\",\n" +
            "                    \"icon_url\": \"http://icons.wxug.com/i/c/k/nt_partlycloudy.gif\",\n" +
            "                    \"title\": \"Wednesday Night\",\n" +
            "                    \"fcttext\": \"A few clouds. Hard freeze expected. Low 26F. Winds light and variable.\",\n" +
            "                    \"fcttext_metric\": \"A few clouds. Hard freeze expected. Low -3C. Winds light and variable.\",\n" +
            "                    \"pop\": \"10\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        \"simpleforecast\": {\n" +
            "            \"forecastday\": [\n" +
            "                {\n" +
            "                    \"date\": {\n" +
            "                        \"epoch\": \"1521396000\",\n" +
            "                        \"pretty\": \"7:00 PM CET on March 18, 2018\",\n" +
            "                        \"day\": 18,\n" +
            "                        \"month\": 3,\n" +
            "                        \"year\": 2018,\n" +
            "                        \"yday\": 76,\n" +
            "                        \"hour\": 19,\n" +
            "                        \"min\": \"00\",\n" +
            "                        \"sec\": 0,\n" +
            "                        \"isdst\": \"0\",\n" +
            "                        \"monthname\": \"March\",\n" +
            "                        \"monthname_short\": \"Mar\",\n" +
            "                        \"weekday_short\": \"Sun\",\n" +
            "                        \"weekday\": \"Sunday\",\n" +
            "                        \"ampm\": \"PM\",\n" +
            "                        \"tz_short\": \"CET\",\n" +
            "                        \"tz_long\": \"Europe/Paris\"\n" +
            "                    },\n" +
            "                    \"period\": 1,\n" +
            "                    \"high\": {\n" +
            "                        \"fahrenheit\": \"37\",\n" +
            "                        \"celsius\": \"3\"\n" +
            "                    },\n" +
            "                    \"low\": {\n" +
            "                        \"fahrenheit\": \"27\",\n" +
            "                        \"celsius\": \"-3\"\n" +
            "                    },\n" +
            "                    \"conditions\": \"Overcast\",\n" +
            "                    \"icon\": \"cloudy\",\n" +
            "                    \"icon_url\": \"http://icons.wxug.com/i/c/k/cloudy.gif\",\n" +
            "                    \"skyicon\": \"\",\n" +
            "                    \"pop\": 0,\n" +
            "                    \"qpf_allday\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"mm\": 0\n" +
            "                    },\n" +
            "                    \"qpf_day\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"mm\": 0\n" +
            "                    },\n" +
            "                    \"qpf_night\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"mm\": 0\n" +
            "                    },\n" +
            "                    \"snow_allday\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"cm\": 0\n" +
            "                    },\n" +
            "                    \"snow_day\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"cm\": 0\n" +
            "                    },\n" +
            "                    \"snow_night\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"cm\": 0\n" +
            "                    },\n" +
            "                    \"maxwind\": {\n" +
            "                        \"mph\": 20,\n" +
            "                        \"kph\": 32,\n" +
            "                        \"dir\": \"NE\",\n" +
            "                        \"degrees\": 34\n" +
            "                    },\n" +
            "                    \"avewind\": {\n" +
            "                        \"mph\": 15,\n" +
            "                        \"kph\": 24,\n" +
            "                        \"dir\": \"NE\",\n" +
            "                        \"degrees\": 34\n" +
            "                    },\n" +
            "                    \"avehumidity\": 70,\n" +
            "                    \"maxhumidity\": 0,\n" +
            "                    \"minhumidity\": 0\n" +
            "                },\n" +
            "                {\n" +
            "                    \"date\": {\n" +
            "                        \"epoch\": \"1521482400\",\n" +
            "                        \"pretty\": \"7:00 PM CET on March 19, 2018\",\n" +
            "                        \"day\": 19,\n" +
            "                        \"month\": 3,\n" +
            "                        \"year\": 2018,\n" +
            "                        \"yday\": 77,\n" +
            "                        \"hour\": 19,\n" +
            "                        \"min\": \"00\",\n" +
            "                        \"sec\": 0,\n" +
            "                        \"isdst\": \"0\",\n" +
            "                        \"monthname\": \"March\",\n" +
            "                        \"monthname_short\": \"Mar\",\n" +
            "                        \"weekday_short\": \"Mon\",\n" +
            "                        \"weekday\": \"Monday\",\n" +
            "                        \"ampm\": \"PM\",\n" +
            "                        \"tz_short\": \"CET\",\n" +
            "                        \"tz_long\": \"Europe/Paris\"\n" +
            "                    },\n" +
            "                    \"period\": 2,\n" +
            "                    \"high\": {\n" +
            "                        \"fahrenheit\": \"39\",\n" +
            "                        \"celsius\": \"4\"\n" +
            "                    },\n" +
            "                    \"low\": {\n" +
            "                        \"fahrenheit\": \"23\",\n" +
            "                        \"celsius\": \"-5\"\n" +
            "                    },\n" +
            "                    \"conditions\": \"Partly Cloudy\",\n" +
            "                    \"icon\": \"partlycloudy\",\n" +
            "                    \"icon_url\": \"http://icons.wxug.com/i/c/k/partlycloudy.gif\",\n" +
            "                    \"skyicon\": \"\",\n" +
            "                    \"pop\": 0,\n" +
            "                    \"qpf_allday\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"mm\": 0\n" +
            "                    },\n" +
            "                    \"qpf_day\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"mm\": 0\n" +
            "                    },\n" +
            "                    \"qpf_night\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"mm\": 0\n" +
            "                    },\n" +
            "                    \"snow_allday\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"cm\": 0\n" +
            "                    },\n" +
            "                    \"snow_day\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"cm\": 0\n" +
            "                    },\n" +
            "                    \"snow_night\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"cm\": 0\n" +
            "                    },\n" +
            "                    \"maxwind\": {\n" +
            "                        \"mph\": 20,\n" +
            "                        \"kph\": 32,\n" +
            "                        \"dir\": \"NNE\",\n" +
            "                        \"degrees\": 28\n" +
            "                    },\n" +
            "                    \"avewind\": {\n" +
            "                        \"mph\": 13,\n" +
            "                        \"kph\": 21,\n" +
            "                        \"dir\": \"NNE\",\n" +
            "                        \"degrees\": 28\n" +
            "                    },\n" +
            "                    \"avehumidity\": 64,\n" +
            "                    \"maxhumidity\": 0,\n" +
            "                    \"minhumidity\": 0\n" +
            "                },\n" +
            "                {\n" +
            "                    \"date\": {\n" +
            "                        \"epoch\": \"1521568800\",\n" +
            "                        \"pretty\": \"7:00 PM CET on March 20, 2018\",\n" +
            "                        \"day\": 20,\n" +
            "                        \"month\": 3,\n" +
            "                        \"year\": 2018,\n" +
            "                        \"yday\": 78,\n" +
            "                        \"hour\": 19,\n" +
            "                        \"min\": \"00\",\n" +
            "                        \"sec\": 0,\n" +
            "                        \"isdst\": \"0\",\n" +
            "                        \"monthname\": \"March\",\n" +
            "                        \"monthname_short\": \"Mar\",\n" +
            "                        \"weekday_short\": \"Tue\",\n" +
            "                        \"weekday\": \"Tuesday\",\n" +
            "                        \"ampm\": \"PM\",\n" +
            "                        \"tz_short\": \"CET\",\n" +
            "                        \"tz_long\": \"Europe/Paris\"\n" +
            "                    },\n" +
            "                    \"period\": 3,\n" +
            "                    \"high\": {\n" +
            "                        \"fahrenheit\": \"42\",\n" +
            "                        \"celsius\": \"6\"\n" +
            "                    },\n" +
            "                    \"low\": {\n" +
            "                        \"fahrenheit\": \"25\",\n" +
            "                        \"celsius\": \"-4\"\n" +
            "                    },\n" +
            "                    \"conditions\": \"Partly Cloudy\",\n" +
            "                    \"icon\": \"partlycloudy\",\n" +
            "                    \"icon_url\": \"http://icons.wxug.com/i/c/k/partlycloudy.gif\",\n" +
            "                    \"skyicon\": \"\",\n" +
            "                    \"pop\": 0,\n" +
            "                    \"qpf_allday\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"mm\": 0\n" +
            "                    },\n" +
            "                    \"qpf_day\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"mm\": 0\n" +
            "                    },\n" +
            "                    \"qpf_night\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"mm\": 0\n" +
            "                    },\n" +
            "                    \"snow_allday\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"cm\": 0\n" +
            "                    },\n" +
            "                    \"snow_day\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"cm\": 0\n" +
            "                    },\n" +
            "                    \"snow_night\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"cm\": 0\n" +
            "                    },\n" +
            "                    \"maxwind\": {\n" +
            "                        \"mph\": 10,\n" +
            "                        \"kph\": 16,\n" +
            "                        \"dir\": \"NNE\",\n" +
            "                        \"degrees\": 14\n" +
            "                    },\n" +
            "                    \"avewind\": {\n" +
            "                        \"mph\": 8,\n" +
            "                        \"kph\": 13,\n" +
            "                        \"dir\": \"NNE\",\n" +
            "                        \"degrees\": 14\n" +
            "                    },\n" +
            "                    \"avehumidity\": 55,\n" +
            "                    \"maxhumidity\": 0,\n" +
            "                    \"minhumidity\": 0\n" +
            "                },\n" +
            "                {\n" +
            "                    \"date\": {\n" +
            "                        \"epoch\": \"1521655200\",\n" +
            "                        \"pretty\": \"7:00 PM CET on March 21, 2018\",\n" +
            "                        \"day\": 21,\n" +
            "                        \"month\": 3,\n" +
            "                        \"year\": 2018,\n" +
            "                        \"yday\": 79,\n" +
            "                        \"hour\": 19,\n" +
            "                        \"min\": \"00\",\n" +
            "                        \"sec\": 0,\n" +
            "                        \"isdst\": \"0\",\n" +
            "                        \"monthname\": \"March\",\n" +
            "                        \"monthname_short\": \"Mar\",\n" +
            "                        \"weekday_short\": \"Wed\",\n" +
            "                        \"weekday\": \"Wednesday\",\n" +
            "                        \"ampm\": \"PM\",\n" +
            "                        \"tz_short\": \"CET\",\n" +
            "                        \"tz_long\": \"Europe/Paris\"\n" +
            "                    },\n" +
            "                    \"period\": 4,\n" +
            "                    \"high\": {\n" +
            "                        \"fahrenheit\": \"43\",\n" +
            "                        \"celsius\": \"6\"\n" +
            "                    },\n" +
            "                    \"low\": {\n" +
            "                        \"fahrenheit\": \"26\",\n" +
            "                        \"celsius\": \"-3\"\n" +
            "                    },\n" +
            "                    \"conditions\": \"Clear\",\n" +
            "                    \"icon\": \"clear\",\n" +
            "                    \"icon_url\": \"http://icons.wxug.com/i/c/k/clear.gif\",\n" +
            "                    \"skyicon\": \"\",\n" +
            "                    \"pop\": 10,\n" +
            "                    \"qpf_allday\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"mm\": 0\n" +
            "                    },\n" +
            "                    \"qpf_day\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"mm\": 0\n" +
            "                    },\n" +
            "                    \"qpf_night\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"mm\": 0\n" +
            "                    },\n" +
            "                    \"snow_allday\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"cm\": 0\n" +
            "                    },\n" +
            "                    \"snow_day\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"cm\": 0\n" +
            "                    },\n" +
            "                    \"snow_night\": {\n" +
            "                        \"in\": 0,\n" +
            "                        \"cm\": 0\n" +
            "                    },\n" +
            "                    \"maxwind\": {\n" +
            "                        \"mph\": 10,\n" +
            "                        \"kph\": 16,\n" +
            "                        \"dir\": \"NNE\",\n" +
            "                        \"degrees\": 21\n" +
            "                    },\n" +
            "                    \"avewind\": {\n" +
            "                        \"mph\": 9,\n" +
            "                        \"kph\": 14,\n" +
            "                        \"dir\": \"NNE\",\n" +
            "                        \"degrees\": 21\n" +
            "                    },\n" +
            "                    \"avehumidity\": 57,\n" +
            "                    \"maxhumidity\": 0,\n" +
            "                    \"minhumidity\": 0\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    }\n" +
            "}";
}
