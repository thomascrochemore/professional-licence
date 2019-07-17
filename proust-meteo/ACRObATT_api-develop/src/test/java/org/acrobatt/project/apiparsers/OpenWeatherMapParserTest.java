package org.acrobatt.project.apiparsers;

import org.acrobatt.project.apiparsers.parsers.OpenWeatherMapParser;
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

public class OpenWeatherMapParserTest {

    private OpenWeatherMapParser openWeatherMapParser = OpenWeatherMapParser.getInstance();

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
            ApiData apiData = new ApiData(apiJson,"Strasbourg",ApiConst.OPEN_WEATHER_MAP,0, Date.from(Instant.now()));
            WeatherData weatherData = openWeatherMapParser.parseJsonResultToWeatherData(apiData);
            System.out.println("Realtime : ");
            System.out.println("date :" + weatherData.getInserted_at());
            System.out.println("delay :" + weatherData.getData_delay().getValue());
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
            JSONObject apiJson = new JSONObject(FORECAST_JSON_2018_03_16_12_00);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,2018);
            calendar.set(Calendar.MONTH,2);
            calendar.set(Calendar.DAY_OF_MONTH,16);
            calendar.set(Calendar.HOUR_OF_DAY,13);
            ApiData apiData = new ApiData(apiJson,"Strasbourg",ApiConst.OPEN_WEATHER_MAP,12,calendar.getTime());
            WeatherData weatherData = openWeatherMapParser.parseJsonForecastToWeathData(apiData);

            System.out.println("\nForecast :");
            System.out.println("delay :" + weatherData.getData_delay().getValue());
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
            "    \"coord\": {\n" +
            "        \"lon\": 7.75,\n" +
            "        \"lat\": 48.58\n" +
            "    },\n" +
            "    \"weather\": [\n" +
            "        {\n" +
            "            \"id\": 500,\n" +
            "            \"main\": \"Rain\",\n" +
            "            \"description\": \"light rain\",\n" +
            "            \"icon\": \"10d\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"base\": \"stations\",\n" +
            "    \"main\": {\n" +
            "        \"temp\": 282.5,\n" +
            "        \"pressure\": 1003,\n" +
            "        \"humidity\": 81,\n" +
            "        \"temp_min\": 282.15,\n" +
            "        \"temp_max\": 283.15\n" +
            "    },\n" +
            "    \"visibility\": 10000,\n" +
            "    \"wind\": {\n" +
            "        \"speed\": 1\n" +
            "    },\n" +
            "    \"clouds\": {\n" +
            "        \"all\": 90\n" +
            "    },\n" +
            "    \"dt\": 1521194400,\n" +
            "    \"sys\": {\n" +
            "        \"type\": 1,\n" +
            "        \"id\": 5657,\n" +
            "        \"message\": 0.0035,\n" +
            "        \"country\": \"FR\",\n" +
            "        \"sunrise\": 1521178781,\n" +
            "        \"sunset\": 1521221771\n" +
            "    },\n" +
            "    \"id\": 2973783,\n" +
            "    \"name\": \"Strasbourg\",\n" +
            "    \"cod\": 200\n" +
            "}";
    private static final String FORECAST_JSON_2018_03_16_12_00 = "{\n" +
            "    \"cod\": \"200\",\n" +
            "    \"message\": 0.0126,\n" +
            "    \"cnt\": 40,\n" +
            "    \"list\": [\n" +
            "        {\n" +
            "            \"dt\": 1521201600,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 285.1,\n" +
            "                \"temp_min\": 283.945,\n" +
            "                \"temp_max\": 285.1,\n" +
            "                \"pressure\": 986.83,\n" +
            "                \"sea_level\": 1016.56,\n" +
            "                \"grnd_level\": 986.83,\n" +
            "                \"humidity\": 86,\n" +
            "                \"temp_kf\": 1.15\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 802,\n" +
            "                    \"main\": \"Clouds\",\n" +
            "                    \"description\": \"scattered clouds\",\n" +
            "                    \"icon\": \"03d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 44\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 3.96,\n" +
            "                \"deg\": 249.51\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-16 12:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521212400,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 285.01,\n" +
            "                \"temp_min\": 284.148,\n" +
            "                \"temp_max\": 285.01,\n" +
            "                \"pressure\": 986.25,\n" +
            "                \"sea_level\": 1016,\n" +
            "                \"grnd_level\": 986.25,\n" +
            "                \"humidity\": 77,\n" +
            "                \"temp_kf\": 0.87\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 802,\n" +
            "                    \"main\": \"Clouds\",\n" +
            "                    \"description\": \"scattered clouds\",\n" +
            "                    \"icon\": \"03d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 36\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 3.3,\n" +
            "                \"deg\": 273.002\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-16 15:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521223200,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 281.34,\n" +
            "                \"temp_min\": 280.763,\n" +
            "                \"temp_max\": 281.34,\n" +
            "                \"pressure\": 986.1,\n" +
            "                \"sea_level\": 1016.07,\n" +
            "                \"grnd_level\": 986.1,\n" +
            "                \"humidity\": 77,\n" +
            "                \"temp_kf\": 0.58\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 0\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 1.8,\n" +
            "                \"deg\": 287.5\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-16 18:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521234000,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 277.1,\n" +
            "                \"temp_min\": 276.816,\n" +
            "                \"temp_max\": 277.1,\n" +
            "                \"pressure\": 986.37,\n" +
            "                \"sea_level\": 1016.55,\n" +
            "                \"grnd_level\": 986.37,\n" +
            "                \"humidity\": 91,\n" +
            "                \"temp_kf\": 0.29\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 801,\n" +
            "                    \"main\": \"Clouds\",\n" +
            "                    \"description\": \"few clouds\",\n" +
            "                    \"icon\": \"02n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 12\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 1.41,\n" +
            "                \"deg\": 326.503\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-16 21:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521244800,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 276.474,\n" +
            "                \"temp_min\": 276.474,\n" +
            "                \"temp_max\": 276.474,\n" +
            "                \"pressure\": 985.35,\n" +
            "                \"sea_level\": 1015.67,\n" +
            "                \"grnd_level\": 985.35,\n" +
            "                \"humidity\": 91,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 500,\n" +
            "                    \"main\": \"Rain\",\n" +
            "                    \"description\": \"light rain\",\n" +
            "                    \"icon\": \"10n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 76\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 1.21,\n" +
            "                \"deg\": 7.00146\n" +
            "            },\n" +
            "            \"rain\": {\n" +
            "                \"3h\": 0.01\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-17 00:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521255600,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 276.588,\n" +
            "                \"temp_min\": 276.588,\n" +
            "                \"temp_max\": 276.588,\n" +
            "                \"pressure\": 983.71,\n" +
            "                \"sea_level\": 1014.05,\n" +
            "                \"grnd_level\": 983.71,\n" +
            "                \"humidity\": 96,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 500,\n" +
            "                    \"main\": \"Rain\",\n" +
            "                    \"description\": \"light rain\",\n" +
            "                    \"icon\": \"10n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 48\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 1.17,\n" +
            "                \"deg\": 4.00269\n" +
            "            },\n" +
            "            \"rain\": {\n" +
            "                \"3h\": 0.05\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-17 03:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521266400,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 276.362,\n" +
            "                \"temp_min\": 276.362,\n" +
            "                \"temp_max\": 276.362,\n" +
            "                \"pressure\": 982.56,\n" +
            "                \"sea_level\": 1013.1,\n" +
            "                \"grnd_level\": 982.56,\n" +
            "                \"humidity\": 92,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 500,\n" +
            "                    \"main\": \"Rain\",\n" +
            "                    \"description\": \"light rain\",\n" +
            "                    \"icon\": \"10d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 76\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 1.82,\n" +
            "                \"deg\": 22.5011\n" +
            "            },\n" +
            "            \"rain\": {\n" +
            "                \"3h\": 0.02\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-17 06:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521277200,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 278.543,\n" +
            "                \"temp_min\": 278.543,\n" +
            "                \"temp_max\": 278.543,\n" +
            "                \"pressure\": 982.17,\n" +
            "                \"sea_level\": 1012.36,\n" +
            "                \"grnd_level\": 982.17,\n" +
            "                \"humidity\": 98,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 500,\n" +
            "                    \"main\": \"Rain\",\n" +
            "                    \"description\": \"light rain\",\n" +
            "                    \"icon\": \"10d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 76\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 3.66,\n" +
            "                \"deg\": 25.0024\n" +
            "            },\n" +
            "            \"rain\": {\n" +
            "                \"3h\": 0.06\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-17 09:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521288000,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 279.693,\n" +
            "                \"temp_min\": 279.693,\n" +
            "                \"temp_max\": 279.693,\n" +
            "                \"pressure\": 981.59,\n" +
            "                \"sea_level\": 1011.74,\n" +
            "                \"grnd_level\": 981.59,\n" +
            "                \"humidity\": 92,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 500,\n" +
            "                    \"main\": \"Rain\",\n" +
            "                    \"description\": \"light rain\",\n" +
            "                    \"icon\": \"10d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 88\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 4.82,\n" +
            "                \"deg\": 31.0023\n" +
            "            },\n" +
            "            \"rain\": {\n" +
            "                \"3h\": 0.135\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-17 12:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521298800,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 279.329,\n" +
            "                \"temp_min\": 279.329,\n" +
            "                \"temp_max\": 279.329,\n" +
            "                \"pressure\": 981.23,\n" +
            "                \"sea_level\": 1011.43,\n" +
            "                \"grnd_level\": 981.23,\n" +
            "                \"humidity\": 84,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 500,\n" +
            "                    \"main\": \"Rain\",\n" +
            "                    \"description\": \"light rain\",\n" +
            "                    \"icon\": \"10d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 92\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 5.98,\n" +
            "                \"deg\": 38.0022\n" +
            "            },\n" +
            "            \"rain\": {\n" +
            "                \"3h\": 0.15\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-17 15:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521309600,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 277.307,\n" +
            "                \"temp_min\": 277.307,\n" +
            "                \"temp_max\": 277.307,\n" +
            "                \"pressure\": 982.13,\n" +
            "                \"sea_level\": 1012.66,\n" +
            "                \"grnd_level\": 982.13,\n" +
            "                \"humidity\": 84,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 500,\n" +
            "                    \"main\": \"Rain\",\n" +
            "                    \"description\": \"light rain\",\n" +
            "                    \"icon\": \"10n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 92\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 6.9,\n" +
            "                \"deg\": 45.5005\n" +
            "            },\n" +
            "            \"rain\": {\n" +
            "                \"3h\": 0.15\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-17 18:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521320400,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 275.512,\n" +
            "                \"temp_min\": 275.512,\n" +
            "                \"temp_max\": 275.512,\n" +
            "                \"pressure\": 983.35,\n" +
            "                \"sea_level\": 1014.05,\n" +
            "                \"grnd_level\": 983.35,\n" +
            "                \"humidity\": 87,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 500,\n" +
            "                    \"main\": \"Rain\",\n" +
            "                    \"description\": \"light rain\",\n" +
            "                    \"icon\": \"10n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 88\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 6.36,\n" +
            "                \"deg\": 52.5012\n" +
            "            },\n" +
            "            \"rain\": {\n" +
            "                \"3h\": 0.18\n" +
            "            },\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.01\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-17 21:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521331200,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 274.054,\n" +
            "                \"temp_min\": 274.054,\n" +
            "                \"temp_max\": 274.054,\n" +
            "                \"pressure\": 984.03,\n" +
            "                \"sea_level\": 1014.77,\n" +
            "                \"grnd_level\": 984.03,\n" +
            "                \"humidity\": 82,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 600,\n" +
            "                    \"main\": \"Snow\",\n" +
            "                    \"description\": \"light snow\",\n" +
            "                    \"icon\": \"13n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 80\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 5.82,\n" +
            "                \"deg\": 52.5003\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.0475\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-18 00:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521342000,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 272.777,\n" +
            "                \"temp_min\": 272.777,\n" +
            "                \"temp_max\": 272.777,\n" +
            "                \"pressure\": 984.24,\n" +
            "                \"sea_level\": 1015.11,\n" +
            "                \"grnd_level\": 984.24,\n" +
            "                \"humidity\": 82,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 600,\n" +
            "                    \"main\": \"Snow\",\n" +
            "                    \"description\": \"light snow\",\n" +
            "                    \"icon\": \"13n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 88\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 5.86,\n" +
            "                \"deg\": 52.0002\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.11\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-18 03:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521352800,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 272.023,\n" +
            "                \"temp_min\": 272.023,\n" +
            "                \"temp_max\": 272.023,\n" +
            "                \"pressure\": 984.87,\n" +
            "                \"sea_level\": 1015.79,\n" +
            "                \"grnd_level\": 984.87,\n" +
            "                \"humidity\": 81,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 600,\n" +
            "                    \"main\": \"Snow\",\n" +
            "                    \"description\": \"light snow\",\n" +
            "                    \"icon\": \"13d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 88\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 5.82,\n" +
            "                \"deg\": 52.5009\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.1025\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-18 06:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521363600,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 273.051,\n" +
            "                \"temp_min\": 273.051,\n" +
            "                \"temp_max\": 273.051,\n" +
            "                \"pressure\": 985.96,\n" +
            "                \"sea_level\": 1016.78,\n" +
            "                \"grnd_level\": 985.96,\n" +
            "                \"humidity\": 81,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 600,\n" +
            "                    \"main\": \"Snow\",\n" +
            "                    \"description\": \"light snow\",\n" +
            "                    \"icon\": \"13d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 92\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 5.96,\n" +
            "                \"deg\": 57.5021\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.13\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-18 09:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521374400,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 274.526,\n" +
            "                \"temp_min\": 274.526,\n" +
            "                \"temp_max\": 274.526,\n" +
            "                \"pressure\": 986.42,\n" +
            "                \"sea_level\": 1017.18,\n" +
            "                \"grnd_level\": 986.42,\n" +
            "                \"humidity\": 77,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 600,\n" +
            "                    \"main\": \"Snow\",\n" +
            "                    \"description\": \"light snow\",\n" +
            "                    \"icon\": \"13d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 68\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 6.19,\n" +
            "                \"deg\": 59.5005\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.1025\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-18 12:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521385200,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 273.81,\n" +
            "                \"temp_min\": 273.81,\n" +
            "                \"temp_max\": 273.81,\n" +
            "                \"pressure\": 987.13,\n" +
            "                \"sea_level\": 1017.76,\n" +
            "                \"grnd_level\": 987.13,\n" +
            "                \"humidity\": 76,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 600,\n" +
            "                    \"main\": \"Snow\",\n" +
            "                    \"description\": \"light snow\",\n" +
            "                    \"icon\": \"13d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 88\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 6.51,\n" +
            "                \"deg\": 58.5058\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.105\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-18 15:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521396000,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 272.575,\n" +
            "                \"temp_min\": 272.575,\n" +
            "                \"temp_max\": 272.575,\n" +
            "                \"pressure\": 988.55,\n" +
            "                \"sea_level\": 1019.5,\n" +
            "                \"grnd_level\": 988.55,\n" +
            "                \"humidity\": 73,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 600,\n" +
            "                    \"main\": \"Snow\",\n" +
            "                    \"description\": \"light snow\",\n" +
            "                    \"icon\": \"13n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 80\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 5.91,\n" +
            "                \"deg\": 59.0033\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.16\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-18 18:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521406800,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 271.718,\n" +
            "                \"temp_min\": 271.718,\n" +
            "                \"temp_max\": 271.718,\n" +
            "                \"pressure\": 990.28,\n" +
            "                \"sea_level\": 1021.35,\n" +
            "                \"grnd_level\": 990.28,\n" +
            "                \"humidity\": 72,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 600,\n" +
            "                    \"main\": \"Snow\",\n" +
            "                    \"description\": \"light snow\",\n" +
            "                    \"icon\": \"13n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 64\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 5.51,\n" +
            "                \"deg\": 56.5002\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.0325\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-18 21:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521417600,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 270.542,\n" +
            "                \"temp_min\": 270.542,\n" +
            "                \"temp_max\": 270.542,\n" +
            "                \"pressure\": 991.06,\n" +
            "                \"sea_level\": 1022.32,\n" +
            "                \"grnd_level\": 991.06,\n" +
            "                \"humidity\": 72,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 36\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 5.06,\n" +
            "                \"deg\": 55.5003\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.0125\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-19 00:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521428400,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 269.462,\n" +
            "                \"temp_min\": 269.462,\n" +
            "                \"temp_max\": 269.462,\n" +
            "                \"pressure\": 991.49,\n" +
            "                \"sea_level\": 1022.89,\n" +
            "                \"grnd_level\": 991.49,\n" +
            "                \"humidity\": 72,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 56\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 4.77,\n" +
            "                \"deg\": 51.5002\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.0125\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-19 03:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521439200,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 268.773,\n" +
            "                \"temp_min\": 268.773,\n" +
            "                \"temp_max\": 268.773,\n" +
            "                \"pressure\": 992.5,\n" +
            "                \"sea_level\": 1023.9,\n" +
            "                \"grnd_level\": 992.5,\n" +
            "                \"humidity\": 72,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 64\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 4.81,\n" +
            "                \"deg\": 54.0093\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.01\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-19 06:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521450000,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 270.109,\n" +
            "                \"temp_min\": 270.109,\n" +
            "                \"temp_max\": 270.109,\n" +
            "                \"pressure\": 993.36,\n" +
            "                \"sea_level\": 1024.77,\n" +
            "                \"grnd_level\": 993.36,\n" +
            "                \"humidity\": 75,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 44\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 5.42,\n" +
            "                \"deg\": 59.5033\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.0075000000000001\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-19 09:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521460800,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 272.275,\n" +
            "                \"temp_min\": 272.275,\n" +
            "                \"temp_max\": 272.275,\n" +
            "                \"pressure\": 993.4,\n" +
            "                \"sea_level\": 1024.42,\n" +
            "                \"grnd_level\": 993.4,\n" +
            "                \"humidity\": 78,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 12\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 6.02,\n" +
            "                \"deg\": 60.5012\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.005\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-19 12:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521471600,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 273.127,\n" +
            "                \"temp_min\": 273.127,\n" +
            "                \"temp_max\": 273.127,\n" +
            "                \"pressure\": 993.16,\n" +
            "                \"sea_level\": 1024.05,\n" +
            "                \"grnd_level\": 993.16,\n" +
            "                \"humidity\": 73,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 801,\n" +
            "                    \"main\": \"Clouds\",\n" +
            "                    \"description\": \"few clouds\",\n" +
            "                    \"icon\": \"02d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 24\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 6.15,\n" +
            "                \"deg\": 60.5003\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {},\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-19 15:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521482400,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 271.526,\n" +
            "                \"temp_min\": 271.526,\n" +
            "                \"temp_max\": 271.526,\n" +
            "                \"pressure\": 994.8,\n" +
            "                \"sea_level\": 1025.93,\n" +
            "                \"grnd_level\": 994.8,\n" +
            "                \"humidity\": 68,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 802,\n" +
            "                    \"main\": \"Clouds\",\n" +
            "                    \"description\": \"scattered clouds\",\n" +
            "                    \"icon\": \"03n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 48\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 5.51,\n" +
            "                \"deg\": 60.5007\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {},\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-19 18:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521493200,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 270.366,\n" +
            "                \"temp_min\": 270.366,\n" +
            "                \"temp_max\": 270.366,\n" +
            "                \"pressure\": 996.91,\n" +
            "                \"sea_level\": 1028.21,\n" +
            "                \"grnd_level\": 996.91,\n" +
            "                \"humidity\": 67,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"02n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 8\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 4.61,\n" +
            "                \"deg\": 60.0045\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {},\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-19 21:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521504000,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 268.971,\n" +
            "                \"temp_min\": 268.971,\n" +
            "                \"temp_max\": 268.971,\n" +
            "                \"pressure\": 997.97,\n" +
            "                \"sea_level\": 1029.65,\n" +
            "                \"grnd_level\": 997.97,\n" +
            "                \"humidity\": 68,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 0\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 3.56,\n" +
            "                \"deg\": 46.0012\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {},\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-20 00:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521514800,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 267.114,\n" +
            "                \"temp_min\": 267.114,\n" +
            "                \"temp_max\": 267.114,\n" +
            "                \"pressure\": 998.91,\n" +
            "                \"sea_level\": 1030.7,\n" +
            "                \"grnd_level\": 998.91,\n" +
            "                \"humidity\": 74,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 0\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 2.67,\n" +
            "                \"deg\": 15.0078\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {},\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-20 03:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521525600,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 265.742,\n" +
            "                \"temp_min\": 265.742,\n" +
            "                \"temp_max\": 265.742,\n" +
            "                \"pressure\": 1000.36,\n" +
            "                \"sea_level\": 1032.19,\n" +
            "                \"grnd_level\": 1000.36,\n" +
            "                \"humidity\": 82,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 0\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 1.4,\n" +
            "                \"deg\": 3.00772\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {},\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-20 06:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521536400,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 272.024,\n" +
            "                \"temp_min\": 272.024,\n" +
            "                \"temp_max\": 272.024,\n" +
            "                \"pressure\": 1002.35,\n" +
            "                \"sea_level\": 1033.59,\n" +
            "                \"grnd_level\": 1002.35,\n" +
            "                \"humidity\": 85,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 0\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 1.86,\n" +
            "                \"deg\": 352\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {},\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-20 09:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521547200,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 276.087,\n" +
            "                \"temp_min\": 276.087,\n" +
            "                \"temp_max\": 276.087,\n" +
            "                \"pressure\": 1003.64,\n" +
            "                \"sea_level\": 1034.44,\n" +
            "                \"grnd_level\": 1003.64,\n" +
            "                \"humidity\": 81,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 24\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 3.1,\n" +
            "                \"deg\": 6.50165\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.01\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-20 12:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521558000,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 277.001,\n" +
            "                \"temp_min\": 277.001,\n" +
            "                \"temp_max\": 277.001,\n" +
            "                \"pressure\": 1004.4,\n" +
            "                \"sea_level\": 1035.13,\n" +
            "                \"grnd_level\": 1004.4,\n" +
            "                \"humidity\": 78,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 500,\n" +
            "                    \"main\": \"Rain\",\n" +
            "                    \"description\": \"light rain\",\n" +
            "                    \"icon\": \"10d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 8\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 3.66,\n" +
            "                \"deg\": 32.5031\n" +
            "            },\n" +
            "            \"rain\": {\n" +
            "                \"3h\": 0.01\n" +
            "            },\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.005\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-20 15:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521568800,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 274.964,\n" +
            "                \"temp_min\": 274.964,\n" +
            "                \"temp_max\": 274.964,\n" +
            "                \"pressure\": 1006.62,\n" +
            "                \"sea_level\": 1037.7,\n" +
            "                \"grnd_level\": 1006.62,\n" +
            "                \"humidity\": 74,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 32\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 3.31,\n" +
            "                \"deg\": 33.0026\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {\n" +
            "                \"3h\": 0.0024999999999999\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-20 18:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521579600,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 270.908,\n" +
            "                \"temp_min\": 270.908,\n" +
            "                \"temp_max\": 270.908,\n" +
            "                \"pressure\": 1009.11,\n" +
            "                \"sea_level\": 1040.62,\n" +
            "                \"grnd_level\": 1009.11,\n" +
            "                \"humidity\": 84,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 0\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 1.87,\n" +
            "                \"deg\": 33.0009\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {},\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-20 21:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521590400,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 268.551,\n" +
            "                \"temp_min\": 268.551,\n" +
            "                \"temp_max\": 268.551,\n" +
            "                \"pressure\": 1010.4,\n" +
            "                \"sea_level\": 1042.22,\n" +
            "                \"grnd_level\": 1010.4,\n" +
            "                \"humidity\": 78,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 0\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 1.76,\n" +
            "                \"deg\": 358.5\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {},\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-21 00:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521601200,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 268.091,\n" +
            "                \"temp_min\": 268.091,\n" +
            "                \"temp_max\": 268.091,\n" +
            "                \"pressure\": 1010.82,\n" +
            "                \"sea_level\": 1042.75,\n" +
            "                \"grnd_level\": 1010.82,\n" +
            "                \"humidity\": 81,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01n\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 0\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 2.46,\n" +
            "                \"deg\": 7.00006\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {},\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"n\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-21 03:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521612000,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 267.016,\n" +
            "                \"temp_min\": 267.016,\n" +
            "                \"temp_max\": 267.016,\n" +
            "                \"pressure\": 1011.31,\n" +
            "                \"sea_level\": 1043.3,\n" +
            "                \"grnd_level\": 1011.31,\n" +
            "                \"humidity\": 84,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 0\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 1.69,\n" +
            "                \"deg\": 12.5004\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {},\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-21 06:00:00\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dt\": 1521622800,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 274.436,\n" +
            "                \"temp_min\": 274.436,\n" +
            "                \"temp_max\": 274.436,\n" +
            "                \"pressure\": 1012.33,\n" +
            "                \"sea_level\": 1043.67,\n" +
            "                \"grnd_level\": 1012.33,\n" +
            "                \"humidity\": 92,\n" +
            "                \"temp_kf\": 0\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 800,\n" +
            "                    \"main\": \"Clear\",\n" +
            "                    \"description\": \"clear sky\",\n" +
            "                    \"icon\": \"01d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 0\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 1.81,\n" +
            "                \"deg\": 6.50009\n" +
            "            },\n" +
            "            \"rain\": {},\n" +
            "            \"snow\": {},\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2018-03-21 09:00:00\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"city\": {\n" +
            "        \"id\": 2973783,\n" +
            "        \"name\": \"Strasbourg\",\n" +
            "        \"coord\": {\n" +
            "            \"lat\": 48.5846,\n" +
            "            \"lon\": 7.7507\n" +
            "        },\n" +
            "        \"country\": \"FR\",\n" +
            "        \"population\": 15000\n" +
            "    }\n" +
            "}";
}
