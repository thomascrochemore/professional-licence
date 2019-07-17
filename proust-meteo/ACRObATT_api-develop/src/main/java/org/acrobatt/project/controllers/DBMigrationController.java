package org.acrobatt.project.controllers;

import org.acrobatt.project.apiparsers.ApiConst;
import org.acrobatt.project.dao.mysql.*;
import org.acrobatt.project.model.mysql.*;
import org.acrobatt.project.utils.GlobalVariableRegistry;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Path("/migration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DBMigrationController {

    ApiURLDAO apiURLDAO = ApiURLDAO.getInstance();
    ApiKeyDAO apiKeyDAO = ApiKeyDAO.getInstance();
    ApiDAO apiDAO = ApiDAO.getInstance();
    CountryDAO countryDAO = CountryDAO.getInstance();
    LocationDAO locationDAO = LocationDAO.getInstance();
    PropertyDAO propertyDAO = PropertyDAO.getInstance();
    DelayDAO delayDAO = DelayDAO.getInstance();
    TextDataDAO textDataDAO = TextDataDAO.getInstance();
    TextDataConvertDAO textDataConvertDAO = TextDataConvertDAO.getInstance();
    PropertyDAO categoryDAO = PropertyDAO.getInstance();

    @GET
    @Path("/sprint/2")
    @Produces(MediaType.APPLICATION_JSON)
    public Response migrateSprint2(){

        for(TextDataConvert textDataConvert : textDataConvertDAO.getAll()){
            textDataConvertDAO.delete(textDataConvert);
        }
        for(TextData textData : textDataDAO.getAll()){
            textDataDAO.delete(textData);
        }
        for(Property category : categoryDAO.getAll()){
            categoryDAO.delete(category);
        }

        for(Api api : apiDAO.getAll()){
            apiDAO.delete(api);
        }
        for(ApiKey apiKey : apiKeyDAO.getAll()){
            apiKeyDAO.delete(apiKey);
        }
        for(ApiURL apiURL : apiURLDAO.getAll()){
            apiURLDAO.delete(apiURL);
        }
        for(Location location : locationDAO.getAll()){
            locationDAO.delete(location);
        }
        for(Country country : countryDAO.getAll()){
            countryDAO.delete(country);
        }
        for(Property property : propertyDAO.getAll()){
            propertyDAO.delete(property);
        }
        for(Delay delay : delayDAO.getAll()){
            delayDAO.delete(delay);
        }

        Api apixu = createApi(ApiConst.APIXU,Arrays.asList(
                new ApiKey("8c1b0064b03d4b45ae2102125180802"),
                new ApiKey("03d100685d6b4002aed120255180802"),
                new ApiKey("e999f3ad486e4a2d904151735180902")
        ),Arrays.asList(
                new ApiURL("http://api.apixu.com/v1/current.json?q={city}&key={key}",false),
                new ApiURL("http://api.apixu.com/v1/forecast.json?q={city}&hour={hour}&days={days}&key={key}",true)
        ));
        Api openWeatherMap = createApi(ApiConst.OPEN_WEATHER_MAP,Arrays.asList(
                new ApiKey("159896bb96cdb582fa15416b99aaa6cf"),
                new ApiKey("7605250de75ac53944ff5ec3d3435741"),
                new ApiKey("8f6c62f3b4788cfc91339bd8cb6aacbb")
        ),Arrays.asList(
                new ApiURL("http://api.openweathermap.org/data/2.5/weather?appid={key}&q={city},{country.code}",false),
                new ApiURL("http://api.openweathermap.org/data/2.5/forecast?appid={key}&q={city},{country.code}",true)
        ));
        Api wunderground = createApi(ApiConst.WUNDERGROUND,Arrays.asList(
                new ApiKey("bdd95b6b9ccab87d"),
                new ApiKey("1f6c927adc661a9a"),
                new ApiKey("7343a35846e7e433")
        ),Arrays.asList(
                new ApiURL("http://api.wunderground.com/api/{key}/conditions/q/{country.name}/{city}.json",false),
                new ApiURL("http://api.wunderground.com/api/{key}/geolookup/conditions/forecast/q/{country.name}/{city}.json",true)
        ));
        Api darksky = createApi(ApiConst.DARK_SKY,Arrays.asList(
                new ApiKey("a08b7d34c208749d66761b0386b86900"),
                new ApiKey("16dd9531d2830d1208441865ec2883a8")
        ),Arrays.asList(
                new ApiURL("https://api.darksky.net/forecast/{key}/{latitude},{longitude}",false),
                new ApiURL("https://api.darksky.net/forecast/{key}/{latitude},{longitude}",true)
        ));
        apiDAO.insert(apixu);
        apiDAO.insert(openWeatherMap);
        apiDAO.insert(wunderground);
        apiDAO.insert(darksky);

        //countries
        Country france = new Country("France","fr");
        Country botswana = new Country("Botswana","bw");
        Country finland = new Country("Finland","fi");
        Country russia = new Country("Russia","ru");
        Country japan = new Country("Japan","jp");
        Country colombia = new Country("Colombia","co");

        countryDAO.insert(france);
        countryDAO.insert(botswana);
        countryDAO.insert(finland);
        countryDAO.insert(russia);
        countryDAO.insert(japan);
        countryDAO.insert(colombia);

        //locations
        Location strasbourg = new Location("Strasbourg",48.58392f,7.74553f);
        strasbourg.setCountry(france);
        Location gaborone = new Location("Gaborone",-24.65451f,25.90859f);
        gaborone.setCountry(botswana);
        Location helsinki = new Location("Helsinki",60.16952f,24.93545f);
        helsinki.setCountry(finland);
        Location moscow = new Location("Moscow",55.75222f,37.61556f);
        moscow.setCountry(russia);
        Location tokyo = new Location("Tokyo",35.6895f,139.69171f);
        tokyo.setCountry(japan);
        Location bogota = new Location("Bogota",4.60971f,-74.08175f);
        bogota.setCountry(colombia);

        locationDAO.insert(strasbourg);
        locationDAO.insert(gaborone);
        locationDAO.insert(helsinki);
        locationDAO.insert(moscow);
        locationDAO.insert(tokyo);
        locationDAO.insert(bogota);

        //properties
        Property[] properties = {
                new Property(GlobalVariableRegistry.PROPERTY_TEMPERATURE,"°C"),
                new Property(GlobalVariableRegistry.PROPERTY_PRESSURE,"millibar"),
                new Property(GlobalVariableRegistry.PROPERTY_HUMIDITY,"%"),
                new Property(GlobalVariableRegistry.PROPERTY_WIND_SPEED,"km/h"),
        };
        for(Property property : properties) propertyDAO.insert(property);

        //text-restricted properties
        Property[] txtproperties = {
                new Property("Nuages", "(0-3)"),
                new Property("Pluie", "(0-6)"),
                new Property("Neige", "(0-9)"),
                new Property("Orage", "(0-7)"),
                new Property("Brouillard", "(0-3)"),
                new Property("Poussière", "(0-8)"),
                new Property("Vent", "(0-6)"),
                new Property("Divers", "(0-4)"),
        };
        for(Property p : txtproperties) propertyDAO.insert(p);

        //delays
        Delay[] delays = {
                new Delay(0),
                new Delay(6),
                new Delay(12),
                new Delay(24),
                new Delay(72)
        };
        for(Delay delay : delays) delayDAO.insert(delay);

        //textdata
        TextData[] sundata = {
                //Soleil
                new TextData("Ensoleillé",              0, txtproperties[0]),
                new TextData("Quelques nuages",         1, txtproperties[0]),
                new TextData("Nuageux",                 2, txtproperties[0]),
                new TextData("Couvert",                 3, txtproperties[0]),
        };

        TextData[] raindata = {
                //Pluie
                new TextData("Clair",                   0, txtproperties[1]),
                new TextData("Légères averses",         1, txtproperties[1]),
                new TextData("Légère pluie",            2, txtproperties[1]),
                new TextData("Averses modérées",        3, txtproperties[1]),
                new TextData("Pluie",                   4, txtproperties[1]),
                new TextData("Fortes averses",          5, txtproperties[1]),
                new TextData("Forte pluie",             6, txtproperties[1]),
        };

        TextData[] snowdata = {
                //Neige
                new TextData("Pas de neige",            0, txtproperties[2]),
                new TextData("Légères giboulées",       1, txtproperties[2]),
                new TextData("Quelques grêlons",        2, txtproperties[2]),
                new TextData("Peu de neige",            3, txtproperties[2]),
                new TextData("Giboulées modérées",      4, txtproperties[2]),
                new TextData("Grêlons modérés",         5, txtproperties[2]),
                new TextData("Neigeux",                 6, txtproperties[2]),
                new TextData("Beaucoup de grêlons",     7, txtproperties[2]),
                new TextData("Beaucoup de neige",       8, txtproperties[2]),
                new TextData("Blizzard",                9, txtproperties[2]),
        };

        TextData[] thunderdata = {
                //Orage
                new TextData("Pas d'orage",             0, txtproperties[3]),
                new TextData("Léger orage",             1, txtproperties[3]),
                new TextData("Léger orage avec pluie",  2, txtproperties[3]),
                new TextData("Léger orage avec neige",  2, txtproperties[3]),
                new TextData("Léger orage avec grêlons",2, txtproperties[3]),
                new TextData("Orageux",                 3, txtproperties[3]),
                new TextData("Orageux avec averses",    4, txtproperties[3]),
                new TextData("Orageux avec pluie",      5, txtproperties[3]),
                new TextData("Orageux avec neige",      5, txtproperties[3]),
                new TextData("Orageux avec grêlons",    5, txtproperties[3]),
                new TextData("Forts orages",            6, txtproperties[3]),
                new TextData("Forts orages et pluie",   7, txtproperties[3]),
                new TextData("Forts orages et neige",   7, txtproperties[3]),
                new TextData("Forts orages et grêlons", 7, txtproperties[3]),
        };

        TextData[] mistdata = {
                //Brouillard
                new TextData("Pas de brouillard",       0, txtproperties[4]),
                new TextData("Légère brume",            1, txtproperties[4]),
                new TextData("Léger brouillard",        1, txtproperties[4]),
                new TextData("Brume modérée",           2, txtproperties[4]),
                new TextData("Brouillard modéré",       2, txtproperties[4]),
                new TextData("Forte brume",             3, txtproperties[4]),
                new TextData("Fort brouillard",         3, txtproperties[4]),
        };

        TextData[] dustdata = {
                //Poussière
                new TextData("Pas de pousssière",       0, txtproperties[5]),
                new TextData("Légère poussière",        1, txtproperties[5]),
                new TextData("Poussière modérée",       2, txtproperties[5]),
                new TextData("Forte poussière",         3, txtproperties[5]),
                new TextData("Fumée",                   4, txtproperties[5]),
                new TextData("Légère tempête de sable", 5, txtproperties[5]),
                new TextData("Tempête de sable",        6, txtproperties[5]),
                new TextData("Forte tempête de sable",  7, txtproperties[5]),
                new TextData("Poussière volcanique",    8, txtproperties[5]),
        };

        TextData[] winddata = {
                //Vent
                new TextData("Pas de vent",             0,  txtproperties[6]),
                new TextData("Léger vent",              1,  txtproperties[6]),
                new TextData("Vent modéré",             2,  txtproperties[6]),
                new TextData("Vent fort",               3,  txtproperties[6]),
                new TextData("Bourrasques",             4,  txtproperties[6]),
                new TextData("Tornade",                 5,  txtproperties[6]),
                new TextData("Ouragan",                 6,  txtproperties[6]),
        };

        TextData[] otherdata = {
                new TextData("Précipitations inconnues",0,  txtproperties[7]),
                new TextData("Inconnu",                 0,  txtproperties[7]),
                new TextData("Vague de froid",          4,  txtproperties[7]),
                new TextData("Canicule",                4,  txtproperties[7]),
                new TextData("Calme",                   0,  txtproperties[7]),
        };

        for(TextData td : sundata) textDataDAO.insert(td);
        for(TextData td : raindata) textDataDAO.insert(td);
        for(TextData td : snowdata) textDataDAO.insert(td);
        for(TextData td : thunderdata) textDataDAO.insert(td);
        for(TextData td : mistdata) textDataDAO.insert(td);
        for(TextData td : dustdata) textDataDAO.insert(td);
        for(TextData td : winddata) textDataDAO.insert(td);
        for(TextData td : otherdata) textDataDAO.insert(td);


        //conversion text
        TextDataConvert[] textDataConverts = {
                //Apixu
                new TextDataConvert("sunny",                                sundata[0],apixu),
                new TextDataConvert("clear",                                sundata[0],apixu),

                new TextDataConvert("partly cloudy",                        sundata[1],apixu),
                new TextDataConvert("cloudy",                               sundata[2],apixu),
                new TextDataConvert("overcast",                             sundata[3],apixu),

                new TextDataConvert("patchy rain possible",                 raindata[2],apixu),
                new TextDataConvert("patchy light rain",                    raindata[2],apixu),
                new TextDataConvert("light rain",                           raindata[2],apixu),
                new TextDataConvert("light freezing rain",                  raindata[2],apixu),
                new TextDataConvert("light rain shower",                    raindata[2],apixu),
                new TextDataConvert("moderate rain at times",               raindata[4],apixu),
                new TextDataConvert("moderate rain",                        raindata[4],apixu),
                new TextDataConvert("moderate or heavy freezing rain",      raindata[4],apixu),
                new TextDataConvert("moderate or heavy rain shower",        raindata[4],apixu),
                new TextDataConvert("heavy rain at times",                  raindata[6],apixu),
                new TextDataConvert("heavy rain",                           raindata[6],apixu),
                new TextDataConvert("torrential rain shower",               raindata[6],apixu),

                new TextDataConvert("thundery outbreaks possible",          thunderdata[1],apixu),
                new TextDataConvert("patchy light rain with thunder",       thunderdata[2],apixu),
                new TextDataConvert("moderate or heavy rain with thunder",  thunderdata[7],apixu),
                new TextDataConvert("patchy light snow with thunder",       thunderdata[3],apixu),
                new TextDataConvert("moderate or heavy snow with thunder",  thunderdata[8],apixu),

                new TextDataConvert("patchy snow possible",                 snowdata[3],apixu),
                new TextDataConvert("patchy light snow",                    snowdata[3],apixu),
                new TextDataConvert("light snow",                           snowdata[3],apixu),
                new TextDataConvert("light snow showers",                   snowdata[3],apixu),
                new TextDataConvert("blowing snow",                         snowdata[6],apixu),
                new TextDataConvert("Patchy moderate snow",                 snowdata[6],apixu),
                new TextDataConvert("Moderate snow",                        snowdata[6],apixu),
                new TextDataConvert("Moderate or heavy snow showers",       snowdata[6],apixu),
                new TextDataConvert("Patchy heavy snow",                    snowdata[8],apixu),
                new TextDataConvert("Heavy snow",                           snowdata[8],apixu),

                new TextDataConvert("Patchy sleet possible",                snowdata[1],apixu),
                new TextDataConvert("Light sleet",                          snowdata[1],apixu),
                new TextDataConvert("Light sleet showers",                  snowdata[1],apixu),
                new TextDataConvert("Moderate or heavy sleet showers",      snowdata[4],apixu),
                new TextDataConvert("Moderate or heavy sleet",              snowdata[4],apixu),

                new TextDataConvert("Patchy freezing drizzle possible",     raindata[1],apixu),
                new TextDataConvert("Patchy light drizzle",                 raindata[1],apixu),
                new TextDataConvert("Light drizzle",                        raindata[1],apixu),
                new TextDataConvert("Freezing drizzle",                     raindata[3],apixu),
                new TextDataConvert("Heavy freezing drizzle",               raindata[5],apixu),

                new TextDataConvert("Blizzard",                             snowdata[9],apixu),
                new TextDataConvert("Fog",                                  mistdata[4],apixu),
                new TextDataConvert("Freezing fog",                         mistdata[4],apixu),
                new TextDataConvert("Ice pellets",                          snowdata[5],apixu),
                new TextDataConvert("Light showers of ice pellets",         snowdata[5],apixu),
                new TextDataConvert("Moderate or heavy showers of ice pellets",snowdata[5],apixu),



                // OpenWeatherMap
                new TextDataConvert("thunderstorm with light rain",         thunderdata[7],openWeatherMap),
                new TextDataConvert("thunderstorm with rain",               thunderdata[8],openWeatherMap),
                new TextDataConvert("thunderstorm with heavy rain",         thunderdata[7],openWeatherMap),
                new TextDataConvert("light thunderstorm",                   thunderdata[1],openWeatherMap),
                new TextDataConvert("thunderstorm",                         thunderdata[5],openWeatherMap),
                new TextDataConvert("heavy thunderstorm",                   thunderdata[5],openWeatherMap),
                new TextDataConvert("ragged thunderstorm",                  thunderdata[5],openWeatherMap),
                new TextDataConvert("thunderstorm with light drizzle",      thunderdata[6],openWeatherMap),
                new TextDataConvert("thunderstorm with drizzle",            thunderdata[6],openWeatherMap),
                new TextDataConvert("thunderstorm with heavy drizzle",      thunderdata[7],openWeatherMap),

                new TextDataConvert("light intensity drizzle",              raindata[1],openWeatherMap),
                new TextDataConvert("drizzle",                              raindata[3],openWeatherMap),
                new TextDataConvert("heavy intensity drizzle",              raindata[5],openWeatherMap),
                new TextDataConvert("light intensity drizzle rain",         raindata[1],openWeatherMap),
                new TextDataConvert("drizzle rain",                         raindata[3],openWeatherMap),
                new TextDataConvert("heavy intensity drizzle rain",         raindata[5],openWeatherMap),
                new TextDataConvert("shower rain and drizzle",              raindata[3],openWeatherMap),
                new TextDataConvert("heavy shower rain and drizzle",        raindata[5],openWeatherMap),
                new TextDataConvert("shower drizzle",                       raindata[3],openWeatherMap),

                new TextDataConvert("light rain",                           raindata[2],openWeatherMap),
                new TextDataConvert("moderate rain",                        raindata[4],openWeatherMap),
                new TextDataConvert("heavy intensity rain",                 raindata[6],openWeatherMap),
                new TextDataConvert("very heavy rain",                      raindata[6],openWeatherMap),
                new TextDataConvert("extreme rain",                         raindata[6],openWeatherMap),
                new TextDataConvert("freezing rain",                        raindata[6],openWeatherMap),
                new TextDataConvert("light intensity shower rain",          raindata[2],openWeatherMap),
                new TextDataConvert("shower rain",                          raindata[4],openWeatherMap),
                new TextDataConvert("heavy intensity shower rain",          raindata[6],openWeatherMap),
                new TextDataConvert("ragged shower rain",                   raindata[6],openWeatherMap),

                new TextDataConvert("light snow",                           snowdata[3],openWeatherMap),
                new TextDataConvert("moderate snow",                        snowdata[6],openWeatherMap),
                new TextDataConvert("heavy snow",                           snowdata[8],openWeatherMap),
                new TextDataConvert("sleet",                                snowdata[4],openWeatherMap),
                new TextDataConvert("shower sleet",                         snowdata[4],openWeatherMap),
                new TextDataConvert("light rain or snow",                   raindata[1],openWeatherMap),
                new TextDataConvert("rain and snow",                        raindata[3],openWeatherMap),
                new TextDataConvert("light shower snow",                    snowdata[3],openWeatherMap),
                new TextDataConvert("shower snow",                          snowdata[6],openWeatherMap),
                new TextDataConvert("heavy shower snow",                    snowdata[8],openWeatherMap),

                new TextDataConvert("mist",                                 mistdata[5],openWeatherMap),
                new TextDataConvert("smoke",                                dustdata[4],openWeatherMap),
                new TextDataConvert("haze",                                 mistdata[5],openWeatherMap),
                new TextDataConvert("sand, dust whirls",                    dustdata[5],openWeatherMap),
                new TextDataConvert("sand",                                 dustdata[5],openWeatherMap),
                new TextDataConvert("dust",                                 dustdata[2],openWeatherMap),
                new TextDataConvert("fog",                                  mistdata[4],openWeatherMap),
                new TextDataConvert("volcanic ash",                         dustdata[8],openWeatherMap),
                new TextDataConvert("squalls",                              winddata[4],openWeatherMap),
                new TextDataConvert("tornado",                              winddata[5],openWeatherMap),

                new TextDataConvert("clear sky",                            sundata[0],openWeatherMap),
                new TextDataConvert("few clouds",                           sundata[1],openWeatherMap),
                new TextDataConvert("scattered clouds",                     sundata[1],openWeatherMap),
                new TextDataConvert("broken clouds",                        sundata[2],openWeatherMap),
                new TextDataConvert("overcast clouds",                      sundata[3],openWeatherMap),

                new TextDataConvert("tropical storm",                       sundata[1],openWeatherMap),
                new TextDataConvert("hurricane",                            sundata[1],openWeatherMap),
                new TextDataConvert("cold",                                 otherdata[2],openWeatherMap),
                new TextDataConvert("hot",                                  otherdata[3],openWeatherMap),
                new TextDataConvert("windy",                                winddata[2],openWeatherMap),
                new TextDataConvert("hail",                                 snowdata[5],openWeatherMap),

                new TextDataConvert("calm",                                 otherdata[4],openWeatherMap),
                new TextDataConvert("light breeze",                         winddata[1],openWeatherMap),
                new TextDataConvert("gentle breeze",                        winddata[1],openWeatherMap),
                new TextDataConvert("moderate breeze",                      winddata[1],openWeatherMap),
                new TextDataConvert("fresh breeze",                         winddata[1],openWeatherMap),
                new TextDataConvert("strong breeze",                        winddata[2],openWeatherMap),
                new TextDataConvert("high wind, near gale",                 winddata[3],openWeatherMap),
                new TextDataConvert("gale",                                 winddata[3],openWeatherMap),
                new TextDataConvert("severe gale",                          winddata[3],openWeatherMap),



                // Wunderground - Current (holy guacamole that's a lot of entries)
                new TextDataConvert("Light Drizzle",                        raindata[1],wunderground),
                new TextDataConvert("Drizzle",                              raindata[3],wunderground),
                new TextDataConvert("Heavy Drizzle",                        raindata[5],wunderground),

                new TextDataConvert("Light Rain",                           raindata[2],wunderground),
                new TextDataConvert("Light Rain Mist",                      raindata[2],wunderground),
                new TextDataConvert("Light Rain Showers",                   raindata[2],wunderground),
                new TextDataConvert("Rain",                                 raindata[4],wunderground),
                new TextDataConvert("Rain Mist",                            raindata[4],wunderground),
                new TextDataConvert("Rain Showers",                         raindata[4],wunderground),
                new TextDataConvert("Heavy Rain",                           raindata[6],wunderground),
                new TextDataConvert("Heavy Rain Mist",                      raindata[6],wunderground),
                new TextDataConvert("Heavy Rain Showers",                   raindata[6],wunderground),

                new TextDataConvert("Light Snow",                           snowdata[3],wunderground),
                new TextDataConvert("Light Snow Grains",                    snowdata[3],wunderground),
                new TextDataConvert("Light Low Drifting Snow",              snowdata[3],wunderground),
                new TextDataConvert("Light Blowing Snow",                   snowdata[3],wunderground),
                new TextDataConvert("Light Snow Showers",                   snowdata[3],wunderground),
                new TextDataConvert("Light Snow Blowing Snow Mist",         snowdata[3],wunderground),
                new TextDataConvert("Snow",                                 snowdata[6],wunderground),
                new TextDataConvert("Snow Grains",                          snowdata[6],wunderground),
                new TextDataConvert("Low Drifting Snow",                    snowdata[6],wunderground),
                new TextDataConvert("Low Blowing Snow",                     snowdata[6],wunderground),
                new TextDataConvert("Snow Showers",                         snowdata[6],wunderground),
                new TextDataConvert("Snow Blowing Snow Mist",               snowdata[6],wunderground),
                new TextDataConvert("Heavy Snow",                           snowdata[8],wunderground),
                new TextDataConvert("Heavy Snow Grains",                    snowdata[8],wunderground),
                new TextDataConvert("Heavy Low Drifting Snow",              snowdata[8],wunderground),
                new TextDataConvert("Heavy Blowing Snow",                   snowdata[8],wunderground),
                new TextDataConvert("Heavy Snow Showers",                   snowdata[8],wunderground),
                new TextDataConvert("Heavy Snow Blowing Snow Mist",         snowdata[8],wunderground),

                new TextDataConvert("Light Ice Crystals",                   snowdata[2],wunderground),
                new TextDataConvert("Light Ice Pellets",                    snowdata[2],wunderground),
                new TextDataConvert("Light Hail",                           snowdata[2],wunderground),
                new TextDataConvert("Ice Crystals",                         snowdata[4],wunderground),
                new TextDataConvert("Ice Pellets",                          snowdata[4],wunderground),
                new TextDataConvert("Hail",                                 snowdata[4],wunderground),
                new TextDataConvert("Heavy Ice Crystals",                   snowdata[7],wunderground),
                new TextDataConvert("Heavy Ice Pellets",                    snowdata[7],wunderground),
                new TextDataConvert("Heavy Hail",                           snowdata[7],wunderground),

                new TextDataConvert("Light Mist",                           mistdata[2],wunderground),
                new TextDataConvert("Mist",                                 mistdata[4],wunderground),
                new TextDataConvert("Heavy Mist",                           mistdata[6],wunderground),
                new TextDataConvert("Light Fog",                            mistdata[2],wunderground),
                new TextDataConvert("Fog",                                  mistdata[4],wunderground),
                new TextDataConvert("Heavy Fog",                            mistdata[6],wunderground),
                new TextDataConvert("Light Fog Patches",                    mistdata[2],wunderground),
                new TextDataConvert("Fog Patches",                          mistdata[4],wunderground),
                new TextDataConvert("Heavy Fog Patches",                    mistdata[6],wunderground),

                new TextDataConvert("Light Smoke",                          dustdata[4],wunderground),
                new TextDataConvert("Smoke",                                dustdata[4],wunderground),
                new TextDataConvert("Heavy Smoke",                          dustdata[4],wunderground),

                new TextDataConvert("Light Volcanic Ash",                   dustdata[8],wunderground),
                new TextDataConvert("Volcanic Ash",                         dustdata[8],wunderground),
                new TextDataConvert("Heavy Volcanic Ash",                   dustdata[8],wunderground),

                new TextDataConvert("Light Widespread Dust",                dustdata[1],wunderground),
                new TextDataConvert("Light Dust Whirls",                    dustdata[1],wunderground),
                new TextDataConvert("Light Low Drifting Widespread Dust",   dustdata[1],wunderground),
                new TextDataConvert("Light Blowing Widespread Dust",        dustdata[1],wunderground),
                new TextDataConvert("Widespread Dust",                      dustdata[2],wunderground),
                new TextDataConvert("Dust Whirls",                          dustdata[2],wunderground),
                new TextDataConvert("Low Drifting Widespread Dust",         dustdata[2],wunderground),
                new TextDataConvert("Low Blowing Widespread Dust",          dustdata[2],wunderground),
                new TextDataConvert("Heavy Widespread Dust",                dustdata[3],wunderground),
                new TextDataConvert("Heavy Dust Whirls",                    dustdata[3],wunderground),
                new TextDataConvert("Heavy Low Drifting Widespread Dust",   dustdata[3],wunderground),
                new TextDataConvert("Heavy Blowing Widespread Dust",        dustdata[3],wunderground),

                new TextDataConvert("Light Sand",                           dustdata[5],wunderground),
                new TextDataConvert("Sand",                                 dustdata[6],wunderground),
                new TextDataConvert("Heavy Sand",                           dustdata[7],wunderground),
                new TextDataConvert("Light Low Drifting Sand",              dustdata[5],wunderground),
                new TextDataConvert("Low Drifting Sand",                    dustdata[6],wunderground),
                new TextDataConvert("Heavy Low Drifting Sand",              dustdata[7],wunderground),
                new TextDataConvert("Light Blowing Sand",                   dustdata[5],wunderground),
                new TextDataConvert("Low Blowing Sand",                     dustdata[6],wunderground),
                new TextDataConvert("Heavy Blowing Sand",                   dustdata[7],wunderground),

                new TextDataConvert("Light Haze",                           mistdata[1],wunderground),
                new TextDataConvert("Haze",                                 mistdata[3],wunderground),
                new TextDataConvert("Heavy Haze",                           mistdata[5],wunderground),

                new TextDataConvert("Light Spray",                          raindata[1],wunderground),
                new TextDataConvert("Spray",                                raindata[3],wunderground),
                new TextDataConvert("Heavy Spray",                          raindata[5],wunderground),

                new TextDataConvert("Light Sandstorm",                      dustdata[5],wunderground),
                new TextDataConvert("Sandstorm",                            dustdata[6],wunderground),
                new TextDataConvert("Heavy Sandstorm",                      dustdata[7],wunderground),

                new TextDataConvert("Light Ice Pellet Showers",             snowdata[2],wunderground),
                new TextDataConvert("Ice Pellet Showers",                   snowdata[4],wunderground),
                new TextDataConvert("Heavy Ice Pellet Showers",             snowdata[7],wunderground),

                new TextDataConvert("Light Hail Showers",                   snowdata[2],wunderground),
                new TextDataConvert("Hail Showers",                         snowdata[4],wunderground),
                new TextDataConvert("Heavy Hail Showers",                   snowdata[7],wunderground),

                new TextDataConvert("Light Small Hail Showers",             snowdata[2],wunderground),
                new TextDataConvert("Small Hail Showers",                   snowdata[4],wunderground),
                new TextDataConvert("Heavy Small Hail Showers",             snowdata[4],wunderground),

                new TextDataConvert("Light Thunderstorm",                   thunderdata[1],wunderground),
                new TextDataConvert("Thunderstorm",                         thunderdata[5],wunderground),
                new TextDataConvert("Heavy Thunderstorm",                   thunderdata[10],wunderground),

                new TextDataConvert("Light Thunderstorms and Rain",         thunderdata[2],wunderground),
                new TextDataConvert("Thunderstorms and Rain",               thunderdata[7],wunderground),
                new TextDataConvert("Heavy Thunderstorms and Rain",         thunderdata[11],wunderground),

                new TextDataConvert("Light Thunderstorms and Snow",         thunderdata[2],wunderground),
                new TextDataConvert("Thunderstorms and Snow",               thunderdata[7],wunderground),
                new TextDataConvert("Heavy Thunderstorms and Snow",         thunderdata[11],wunderground),

                new TextDataConvert("Light Thunderstorms and Ice Pellets",  thunderdata[4],wunderground),
                new TextDataConvert("Thunderstorms and Ice Pellets",        thunderdata[9],wunderground),
                new TextDataConvert("Heavy Thunderstorms and Ice Pellets",  thunderdata[13],wunderground),

                new TextDataConvert("Light Thunderstorms and Hail",         thunderdata[4],wunderground),
                new TextDataConvert("Thunderstorms and Hail",               thunderdata[9],wunderground),
                new TextDataConvert("Heavy Thunderstorms and Hail",         thunderdata[13],wunderground),

                new TextDataConvert("Light Thunderstorms and Small Hail",   thunderdata[4],wunderground),
                new TextDataConvert("Thunderstorms and Small Hail",         thunderdata[9],wunderground),
                new TextDataConvert("Heavy Thunderstorms and Small Hail",   thunderdata[13],wunderground),

                new TextDataConvert("Light Freezing Drizzle",               raindata[1],wunderground),
                new TextDataConvert("Freezing Drizzle",                     raindata[3],wunderground),
                new TextDataConvert("Heavy Freezing Drizzle",               raindata[5],wunderground),

                new TextDataConvert("Light Freezing Rain",                  raindata[2],wunderground),
                new TextDataConvert("Freezing Rain",                        raindata[4],wunderground),
                new TextDataConvert("Heavy Freezing Rain",                  raindata[6],wunderground),

                new TextDataConvert("Light Freezing Fog",                   mistdata[2],wunderground),
                new TextDataConvert("Freezing Fog",                         mistdata[4],wunderground),
                new TextDataConvert("Heavy Freezing Fog",                   mistdata[6],wunderground),

                new TextDataConvert("Patches of Fog",                       mistdata[2],wunderground),
                new TextDataConvert("Shallow Fog",                          mistdata[2],wunderground),
                new TextDataConvert("Partial Fog",                          mistdata[4],wunderground),

                new TextDataConvert("Overcast",                             sundata[3],wunderground),
                new TextDataConvert("Clear",                                sundata[0],wunderground),
                new TextDataConvert("Partly Cloudy",                        sundata[1],wunderground),
                new TextDataConvert("Mostly Cloudy",                        sundata[2],wunderground),
                new TextDataConvert("Scattered Clouds",                     sundata[1],wunderground),
                new TextDataConvert("Small Hail",                           snowdata[2],wunderground),
                new TextDataConvert("Squalls",                              winddata[4],wunderground),
                new TextDataConvert("Funnel Cloud",                         winddata[4],wunderground),
                new TextDataConvert("Unknown Precipitation",                otherdata[0],wunderground),
                new TextDataConvert("Unknown",                              otherdata[1],wunderground),

                // Wunderground - Forecast
                new TextDataConvert("Chance of Flurries",                   snowdata[4],wunderground),
                new TextDataConvert("Chance of Rain",                       raindata[4],wunderground),
                new TextDataConvert("Chance Rain",                          raindata[4],wunderground),
                new TextDataConvert("Chance of Freezing Rain",              raindata[4],wunderground),
                new TextDataConvert("Chance of Sleet",                      snowdata[4],wunderground),
                new TextDataConvert("Chance of Snow",                       snowdata[6],wunderground),
                new TextDataConvert("Chance of Thunderstorms",              thunderdata[5],wunderground),
                new TextDataConvert("Chance of Thunderstorm",               thunderdata[5],wunderground),
                new TextDataConvert("Cloudy",                               sundata[2],wunderground),
                new TextDataConvert("Flurries",                             snowdata[4],wunderground),
                new TextDataConvert("Mostly Sunny",                         sundata[1],wunderground),
                new TextDataConvert("Partly Sunny",                         sundata[2],wunderground),
                new TextDataConvert("Sleet",                                snowdata[4],wunderground),
                new TextDataConvert("Sunny",                                sundata[0],wunderground),
                new TextDataConvert("Thunderstorms",                        thunderdata[5],wunderground),

                new TextDataConvert("clear-day",sundata[0],darksky),
                new TextDataConvert("clear-night",sundata[0],darksky),
                new TextDataConvert("rain",raindata[4],darksky),
                new TextDataConvert("snow",snowdata[6],darksky),
                new TextDataConvert("sleet",snowdata[3],darksky),
                new TextDataConvert("wind",winddata[2],darksky),
                new TextDataConvert("fog",sundata[3],darksky),
                new TextDataConvert("cloudy",sundata[2],darksky),
                new TextDataConvert("partly-cloudy-day",sundata[1],darksky),
                new TextDataConvert("partly-cloudy-night",sundata[1],darksky)
        };
        for(TextDataConvert tc : textDataConverts) textDataConvertDAO.insert(tc);

        return Response.ok().entity("{\"message\":\"Success\"}").build();
    }

    @GET
    @Path("/sprint/3")
    @Produces(MediaType.APPLICATION_JSON)
    public Response migrateSprint3(){
        migrateSprint2();

        Api apixu = apiDAO.getByName(ApiConst.APIXU);
        Api openWeatherMap = apiDAO.getByName(ApiConst.OPEN_WEATHER_MAP);
        Api wunderground = apiDAO.getByName(ApiConst.WUNDERGROUND);
        Api darksky = apiDAO.getByName(ApiConst.DARK_SKY);

        Api weatherbit = createApi(ApiConst.WEATHER_BIT,
                Arrays.asList(new ApiKey("b5445a3c5b7141849cb442555391d736")),
                Arrays.asList(
                        new ApiURL("https://api.weatherbit.io/v2.0/current?city={city}&country={country.name}",false),
                        new ApiURL("https://api.weatherbit.io/v2.0/forecast/daily?city={city}&country={country.name}",true)
                ));

        apixu.setReqPerHour(10000l / 31 / 24);
        apixu.setReqPerDay(10000l / 31);


        openWeatherMap.setReqPerHour(60l);
        openWeatherMap.setReqPerDay(60*24l);

        wunderground.setReqPerHour(10l);
        wunderground.setReqPerDay(500l);


        darksky.setReqPerHour(60l); // not the same calcul of pricing, we will change keys every 1000 req
        darksky.setReqPerDay(60*24l); // per key -> 1000 free, after that $0.0001 per req

        weatherbit.setReqPerHour(75l);
        weatherbit.setReqPerDay(200l);

        apiDAO.update(apixu);
        apiDAO.update(openWeatherMap);
        apiDAO.update(wunderground);
        apiDAO.update(darksky);

        apiDAO.insert(weatherbit);



        return Response.ok().entity("{\"message\":\"Success\"}").build();
    }

    public Api createApi(String name,List<ApiKey> apiKeys,List<ApiURL> apiURLS){
        return new Api(name,apiKeys,new HashSet<>(apiURLS));
    }

}
