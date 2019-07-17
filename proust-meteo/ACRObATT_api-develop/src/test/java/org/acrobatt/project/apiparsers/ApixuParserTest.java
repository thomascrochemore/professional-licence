package org.acrobatt.project.apiparsers;

import org.acrobatt.project.apiparsers.parsers.ApixuParser;
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

public class ApixuParserTest {

    private ApixuParser apixuParser = ApixuParser.getInstance();

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
            ApiData apiData = new ApiData(apiJson,"Strasbourg",ApiConst.APIXU,0, Date.from(Instant.now()));
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
            System.out.println("Test failed : "+e.getMessage());
        }
    }

    @Test
    public void forecastTest(){
        try {
            JSONObject apiJson = new JSONObject(FORECAST_JSON_2018_16_03);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,2018);
            calendar.set(Calendar.MONTH,2);
            calendar.set(Calendar.DAY_OF_MONTH,16);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            ApiData apiData = new ApiData(apiJson,"Strasbourg",ApiConst.APIXU,72,calendar.getTime());
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
            System.out.println("Test failed : "+e.getMessage());
        }
    }

    private static final String REALTIME_JSON = "{\n" +
            "    \"location\": {\n" +
            "        \"name\": \"Strasbourg\",\n" +
            "        \"region\": \"Alsace\",\n" +
            "        \"country\": \"France\",\n" +
            "        \"lat\": 48.58,\n" +
            "        \"lon\": 7.75,\n" +
            "        \"tz_id\": \"Europe/Paris\",\n" +
            "        \"localtime_epoch\": 1521214033,\n" +
            "        \"localtime\": \"2018-03-16 16:27\"\n" +
            "    },\n" +
            "    \"current\": {\n" +
            "        \"last_updated_epoch\": 1521213319,\n" +
            "        \"last_updated\": \"2018-03-16 16:15\",\n" +
            "        \"temp_c\": 14,\n" +
            "        \"temp_f\": 57.2,\n" +
            "        \"is_day\": 1,\n" +
            "        \"condition\": {\n" +
            "            \"text\": \"Partly cloudy\",\n" +
            "            \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\",\n" +
            "            \"code\": 1003\n" +
            "        },\n" +
            "        \"wind_mph\": 4.3,\n" +
            "        \"wind_kph\": 6.8,\n" +
            "        \"wind_degree\": 280,\n" +
            "        \"wind_dir\": \"W\",\n" +
            "        \"pressure_mb\": 1002,\n" +
            "        \"pressure_in\": 30.1,\n" +
            "        \"precip_mm\": 0,\n" +
            "        \"precip_in\": 0,\n" +
            "        \"humidity\": 51,\n" +
            "        \"cloud\": 75,\n" +
            "        \"feelslike_c\": 13.9,\n" +
            "        \"feelslike_f\": 57,\n" +
            "        \"vis_km\": 10,\n" +
            "        \"vis_miles\": 6\n" +
            "    }\n" +
            "}";

    private static final String FORECAST_JSON_2018_16_03 = "{\n" +
            "    \"location\": {\n" +
            "        \"name\": \"Strasbourg\",\n" +
            "        \"region\": \"Alsace\",\n" +
            "        \"country\": \"France\",\n" +
            "        \"lat\": 48.58,\n" +
            "        \"lon\": 7.75,\n" +
            "        \"tz_id\": \"Europe/Paris\",\n" +
            "        \"localtime_epoch\": 1521214106,\n" +
            "        \"localtime\": \"2018-03-16 16:28\"\n" +
            "    },\n" +
            "    \"current\": {\n" +
            "        \"last_updated_epoch\": 1521213319,\n" +
            "        \"last_updated\": \"2018-03-16 16:15\",\n" +
            "        \"temp_c\": 14,\n" +
            "        \"temp_f\": 57.2,\n" +
            "        \"is_day\": 1,\n" +
            "        \"condition\": {\n" +
            "            \"text\": \"Partly cloudy\",\n" +
            "            \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\",\n" +
            "            \"code\": 1003\n" +
            "        },\n" +
            "        \"wind_mph\": 4.3,\n" +
            "        \"wind_kph\": 6.8,\n" +
            "        \"wind_degree\": 280,\n" +
            "        \"wind_dir\": \"W\",\n" +
            "        \"pressure_mb\": 1002,\n" +
            "        \"pressure_in\": 30.1,\n" +
            "        \"precip_mm\": 0,\n" +
            "        \"precip_in\": 0,\n" +
            "        \"humidity\": 51,\n" +
            "        \"cloud\": 75,\n" +
            "        \"feelslike_c\": 13.9,\n" +
            "        \"feelslike_f\": 57,\n" +
            "        \"vis_km\": 10,\n" +
            "        \"vis_miles\": 6\n" +
            "    },\n" +
            "    \"forecast\": {\n" +
            "        \"forecastday\": [\n" +
            "            {\n" +
            "                \"date\": \"2018-03-16\",\n" +
            "                \"date_epoch\": 1521158400,\n" +
            "                \"day\": {\n" +
            "                    \"maxtemp_c\": 12.4,\n" +
            "                    \"maxtemp_f\": 54.3,\n" +
            "                    \"mintemp_c\": 4.1,\n" +
            "                    \"mintemp_f\": 39.4,\n" +
            "                    \"avgtemp_c\": 8.4,\n" +
            "                    \"avgtemp_f\": 47.1,\n" +
            "                    \"maxwind_mph\": 6.9,\n" +
            "                    \"maxwind_kph\": 11.2,\n" +
            "                    \"totalprecip_mm\": 0.9,\n" +
            "                    \"totalprecip_in\": 0.04,\n" +
            "                    \"avgvis_km\": 15.8,\n" +
            "                    \"avgvis_miles\": 9,\n" +
            "                    \"avghumidity\": 78,\n" +
            "                    \"condition\": {\n" +
            "                        \"text\": \"Moderate rain at times\",\n" +
            "                        \"icon\": \"//cdn.apixu.com/weather/64x64/day/299.png\",\n" +
            "                        \"code\": 1186\n" +
            "                    },\n" +
            "                    \"uv\": 1.6\n" +
            "                },\n" +
            "                \"astro\": {\n" +
            "                    \"sunrise\": \"06:40 AM\",\n" +
            "                    \"sunset\": \"06:35 PM\",\n" +
            "                    \"moonrise\": \"06:30 AM\",\n" +
            "                    \"moonset\": \"05:26 PM\"\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"date\": \"2018-03-17\",\n" +
            "                \"date_epoch\": 1521244800,\n" +
            "                \"day\": {\n" +
            "                    \"maxtemp_c\": 8.3,\n" +
            "                    \"maxtemp_f\": 46.9,\n" +
            "                    \"mintemp_c\": 1.4,\n" +
            "                    \"mintemp_f\": 34.5,\n" +
            "                    \"avgtemp_c\": 5.5,\n" +
            "                    \"avgtemp_f\": 41.9,\n" +
            "                    \"maxwind_mph\": 15.4,\n" +
            "                    \"maxwind_kph\": 24.8,\n" +
            "                    \"totalprecip_mm\": 0,\n" +
            "                    \"totalprecip_in\": 0,\n" +
            "                    \"avgvis_km\": 16.5,\n" +
            "                    \"avgvis_miles\": 10,\n" +
            "                    \"avghumidity\": 81,\n" +
            "                    \"condition\": {\n" +
            "                        \"text\": \"Overcast\",\n" +
            "                        \"icon\": \"//cdn.apixu.com/weather/64x64/day/122.png\",\n" +
            "                        \"code\": 1009\n" +
            "                    },\n" +
            "                    \"uv\": 1.4\n" +
            "                },\n" +
            "                \"astro\": {\n" +
            "                    \"sunrise\": \"06:38 AM\",\n" +
            "                    \"sunset\": \"06:37 PM\",\n" +
            "                    \"moonrise\": \"06:56 AM\",\n" +
            "                    \"moonset\": \"06:33 PM\"\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"date\": \"2018-03-18\",\n" +
            "                \"date_epoch\": 1521331200,\n" +
            "                \"day\": {\n" +
            "                    \"maxtemp_c\": 3.6,\n" +
            "                    \"maxtemp_f\": 38.5,\n" +
            "                    \"mintemp_c\": -1.6,\n" +
            "                    \"mintemp_f\": 29.1,\n" +
            "                    \"avgtemp_c\": 1.8,\n" +
            "                    \"avgtemp_f\": 35.2,\n" +
            "                    \"maxwind_mph\": 13.2,\n" +
            "                    \"maxwind_kph\": 21.2,\n" +
            "                    \"totalprecip_mm\": 0.5,\n" +
            "                    \"totalprecip_in\": 0.02,\n" +
            "                    \"avgvis_km\": 14.2,\n" +
            "                    \"avgvis_miles\": 8,\n" +
            "                    \"avghumidity\": 73,\n" +
            "                    \"condition\": {\n" +
            "                        \"text\": \"Moderate snow\",\n" +
            "                        \"icon\": \"//cdn.apixu.com/weather/64x64/day/332.png\",\n" +
            "                        \"code\": 1219\n" +
            "                    },\n" +
            "                    \"uv\": 2\n" +
            "                },\n" +
            "                \"astro\": {\n" +
            "                    \"sunrise\": \"06:36 AM\",\n" +
            "                    \"sunset\": \"06:38 PM\",\n" +
            "                    \"moonrise\": \"07:22 AM\",\n" +
            "                    \"moonset\": \"07:43 PM\"\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"date\": \"2018-03-19\",\n" +
            "                \"date_epoch\": 1521417600,\n" +
            "                \"day\": {\n" +
            "                    \"maxtemp_c\": 4,\n" +
            "                    \"maxtemp_f\": 39.2,\n" +
            "                    \"mintemp_c\": -2.8,\n" +
            "                    \"mintemp_f\": 27,\n" +
            "                    \"avgtemp_c\": 0.5,\n" +
            "                    \"avgtemp_f\": 32.9,\n" +
            "                    \"maxwind_mph\": 13.9,\n" +
            "                    \"maxwind_kph\": 22.3,\n" +
            "                    \"totalprecip_mm\": 0,\n" +
            "                    \"totalprecip_in\": 0,\n" +
            "                    \"avgvis_km\": 17.9,\n" +
            "                    \"avgvis_miles\": 11,\n" +
            "                    \"avghumidity\": 65,\n" +
            "                    \"condition\": {\n" +
            "                        \"text\": \"Partly cloudy\",\n" +
            "                        \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\",\n" +
            "                        \"code\": 1003\n" +
            "                    },\n" +
            "                    \"uv\": 2.3\n" +
            "                },\n" +
            "                \"astro\": {\n" +
            "                    \"sunrise\": \"06:34 AM\",\n" +
            "                    \"sunset\": \"06:40 PM\",\n" +
            "                    \"moonrise\": \"07:47 AM\",\n" +
            "                    \"moonset\": \"08:53 PM\"\n" +
            "                }\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "}";
}
